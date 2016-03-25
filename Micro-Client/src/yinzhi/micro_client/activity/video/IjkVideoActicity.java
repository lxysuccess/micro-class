package yinzhi.micro_client.activity.video;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import yinzhi.micro_client.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easefun.polyvsdk.QuestionVO;
import com.easefun.polyvsdk.Video.ADMatter;
import com.easefun.polyvsdk.ijk.IjkVideoView;
import com.easefun.polyvsdk.ijk.IjkVideoView.ErrorReason;
import com.easefun.polyvsdk.ijk.OnPreparedListener;

public class IjkVideoActicity extends Activity {
	private static final String TAG = "IjkVideoActicity";
	private IjkVideoView videoView = null;
	private PolyvQuestionView questionView = null;
	private PolyvAuditionView auditionView = null;
	private PolyvPlayerAdvertisementView adView = null;
	private MediaController mediaController = null;
	private ImageView logo = null;
	private PolyvPlayerFirstStartView playerFirstStartView = null;
	int w = 0, h = 0, adjusted_h = 0;
	private RelativeLayout rl = null;
	private int stopPosition = 0;
    
    public static Intent newIntent(Context context, PlayMode playMode, PlayType playType, String value, boolean startNow) {
        Intent intent = new Intent(context, IjkVideoActicity.class);
        intent.putExtra("playMode", playMode.getCode());
        intent.putExtra("playType", playType.getCode());
        intent.putExtra("value", value);
        intent.putExtra("startNow", startNow);
        return intent;
    }

    public static void intentTo(Context context, PlayMode playMode, PlayType playType, String value, boolean startNow) {
        context.startActivity(newIntent(context, playMode, playType, value, startNow));
    }
	
	@SuppressLint("NewApi") @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.video_small2);
		
    	// 播放模式：横屏，竖屏
        int playModeCode = getIntent().getIntExtra("playMode", 0);
        PlayMode playMode = PlayMode.getPlayMode(playModeCode);
        
        //播放类型：vid或url
        int playTypeCode = getIntent().getIntExtra("playType", 0);
        final PlayType playType = PlayType.getPlayType(playTypeCode);
        
        final String value = getIntent().getStringExtra("value");
        final boolean startNow = getIntent().getBooleanExtra("startNow", false);
        
        if (playMode == null || playType == null || TextUtils.isEmpty(value)) {
        	Log.e(TAG, "Null Data Source");
            finish();
            return;
        }
		
    	Point point = new Point();
    	WindowManager wm = this.getWindowManager();
		wm.getDefaultDisplay().getSize(point);
		w = point.x;
		h = point.y;
		//小窗口的比例
		float ratio = (float) 4 / 3;
		adjusted_h = (int) Math.ceil((float) w / ratio);
		rl = (RelativeLayout) findViewById(R.id.rl);
		rl.setLayoutParams(new RelativeLayout.LayoutParams(w, adjusted_h));
		videoView = (IjkVideoView) findViewById(R.id.videoview);
		
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.loadingprogress);
		TextView videoAdCountDown = (TextView) findViewById(R.id.count_down);
		logo = (ImageView) findViewById(R.id.logo);
		
		//在缓冲时出现的loading
		videoView.setMediaBufferingIndicator(progressBar);
		videoView.setAdCountDown(videoAdCountDown);
		videoView.setVideoLayout(IjkVideoView.VIDEO_LAYOUT_SCALE);
		videoView.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(IMediaPlayer mp) {
				videoView.setVideoLayout(IjkVideoView.VIDEO_LAYOUT_SCALE);
				logo.setVisibility(View.VISIBLE);
				if (stopPosition > 0) {
					Log.d(TAG, "seek to stopPosition:" + stopPosition);
					videoView.seekTo(stopPosition);
				}
				
				if (startNow == false) {
					videoView.pause(true);
					if (playType == PlayType.vid) {
						playerFirstStartView.show(rl, value);
					}
				}
			}
		});
		
		videoView.setOnVideoStatusListener(new IjkVideoView.OnVideoStatusListener() {
			
			@Override
			public void onStatus(int status) {
				
			}
		});
		
		videoView.setOnVideoPlayErrorLisener(new IjkVideoView.OnVideoPlayErrorLisener() {
			
			@Override
			public boolean onVideoPlayError(ErrorReason errorReason) {
				return false;
			}
		});
		
		videoView.setOnQuestionOutListener(new IjkVideoView.OnQuestionOutListener() {
			
			@Override
			public void onOut(final QuestionVO questionVO) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						switch (questionVO.getType()) {
							case QuestionVO.TYPE_QUESTION:
								if (questionView == null) {
									questionView = new PolyvQuestionView(IjkVideoActicity.this);
									questionView.setIjkVideoView(videoView);
								}
								
								questionView.show(rl, questionVO);
								break;
								
							case QuestionVO.TYPE_AUDITION:
								if (auditionView == null) {
									auditionView = new PolyvAuditionView(IjkVideoActicity.this);
									auditionView.setIjkVideoView(videoView);
								}
								
								auditionView.show(rl, questionVO);
								break;
						}
					}
				});
			}
		});
		
		videoView.setOnQuestionAnswerTipsListener(new IjkVideoView.OnQuestionAnswerTipsListener() {
			
			@Override
			public void onTips(String msg) {
				questionView.showAnswerTips(msg);
			}
		});
		
		videoView.setOnAdvertisementOutListener(new IjkVideoView.OnAdvertisementOutListener() {
			
			@Override
			public void onOut(ADMatter adMatter) {
				stopPosition = videoView.getCurrentPosition();
				if (adView == null) {
					adView = new PolyvPlayerAdvertisementView(IjkVideoActicity.this);
					adView.setIjkVideoView(videoView);
				}
				
				adView.show(rl, adMatter);
			}
		});
		
		videoView.setOnPlayPauseListener(new IjkVideoView.OnPlayPauseListener() {
			
			@Override
			public void onPlay() {
				
			}
			
			@Override
			public void onPause() {
				
			}
			
			@Override
			public void onCompletion() {
				logo.setVisibility(View.GONE);
				mediaController.setProgressMax();
			}
		});
		
		mediaController = new MediaController(this,true);
		mediaController.setIjkVideoView(videoView);
		mediaController.setAnchorView(videoView);
		videoView.setMediaController(mediaController);
		
		//设置切屏事件
		mediaController.setOnBoardChangeListener(new MediaController.OnBoardChangeListener() {
			
			@Override
			public void onPortrait() {
				changeToLandscape();
			}
			
			@Override
			public void onLandscape() {
				changeToPortrait();
			}
		});
		
		// 设置视频尺寸 ，在横屏下效果较明显
		mediaController.setOnVideoChangeListener(new MediaController.OnVideoChangeListener() {
		
			@Override
			public void onVideoChange(final int layout) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						videoView.setVideoLayout(layout);
						switch (layout) {
							case IjkVideoView.VIDEO_LAYOUT_ORIGIN:
								Toast.makeText(IjkVideoActicity.this, "VIDEO_LAYOUT_ORIGIN", Toast.LENGTH_SHORT).show();
								break;
							case IjkVideoView.VIDEO_LAYOUT_SCALE:
								Toast.makeText(IjkVideoActicity.this, "VIDEO_LAYOUT_SCALE", Toast.LENGTH_SHORT).show();
								break;
							case IjkVideoView.VIDEO_LAYOUT_STRETCH:
								Toast.makeText(IjkVideoActicity.this, "VIDEO_LAYOUT_STRETCH", Toast.LENGTH_SHORT).show();
								break;
							case IjkVideoView.VIDEO_LAYOUT_ZOOM:
								Toast.makeText(IjkVideoActicity.this, "VIDEO_LAYOUT_ZOOM", Toast.LENGTH_SHORT).show();
								break;
						}
					}
				});
			}
		});
		
		switch (playMode) {
	        case landScape:
	        	changeToLandscape();
	        	break;
	        	
	        case portrait:
	        	changeToPortrait();
	        	break;
	    }
		
		switch (playType) {
		case vid:
			videoView.setVid(value);
			break;
		case url:
			progressBar.setVisibility(View.GONE);
			videoView.setVideoPath(value);
			break;
		}
		
		if (startNow) {
        	videoView.start();
        } else {
        	if (playType == PlayType.vid) {
        		if (playerFirstStartView == null) {
        			playerFirstStartView = new PolyvPlayerFirstStartView(this);
        			playerFirstStartView.setCallback(new PolyvPlayerFirstStartView.Callback() {
            			
            			@Override
            			public void onClickStart() {
            				videoView.start();
            				playerFirstStartView.hide();
            			}
            		});
        		}
        	}
        }
    }

	/**
	 * 切换到横屏
	 */
	public void changeToLandscape(){
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(h,w);
		rl.setLayoutParams(p);
		stopPosition = videoView.getCurrentPosition();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
	
	/**
	 * 切换到竖屏
	 */
	public void changeToPortrait(){
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(w,adjusted_h);
		rl.setLayoutParams(p);
		stopPosition = videoView.getCurrentPosition();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	// 配置文件设置congfigchange 切屏调用一次该方法，hide()之后再次show才会出现在正确位置
	@Override
	public void onConfigurationChanged(Configuration arg0) {
		super.onConfigurationChanged(arg0);
		videoView.setVideoLayout(IjkVideoView.VIDEO_LAYOUT_SCALE);
		mediaController.hide();
		
		if (questionView != null && questionView.isShowing()) {
			questionView.hide();
			questionView.refresh();
		} else if (auditionView != null && auditionView.isShowing()) {
			auditionView.hide();
			auditionView.refresh();
		}
		
		if (playerFirstStartView != null && playerFirstStartView.isShowing()) {
			playerFirstStartView.hide();
			playerFirstStartView.refresh();
		}
		
		if (adView != null && adView.isShowing()) {
			adView.hide();
			adView.refresh();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean value = mediaController.dispatchKeyEvent(event);
		if (value)
			return true;
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (videoView != null) {
			videoView.stopPlayback();
			videoView.release(true);
		}
		
		if (questionView != null) {
			questionView.hide();
		}
		
		if (auditionView != null) {
			auditionView.hide();
		}
		
		if (playerFirstStartView != null) {
			playerFirstStartView.hide();
		}
		
		if (adView != null) {
			adView.hide();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (videoView != null) {
			videoView.stopPlayback();
			videoView.release(true);
		}
		
		if (questionView != null) {
			questionView.hide();
		}
		
		if (auditionView != null) {
			auditionView.hide();
		}
		
		if (playerFirstStartView != null) {
			playerFirstStartView.hide();
		}
		
		if (adView != null) {
			adView.hide();
		}
	};
	
	/**
	 * 播放类型
	 * @author TanQu
	 */
	public enum PlayType {
		/** 使用vid播放 */
		vid(1),
		/** 使用url播放 */
		url(2);
		
		private final int code;
		private PlayType(int code) {
			this.code = code;
		}
		
		/**
		 * 取得类型对应的code
		 * @return
		 */
		public int getCode() {
			return code;
		}
		
		public static PlayType getPlayType(int code) {
			switch (code) {
				case 1:
					return vid;
				case 2:
					return url;
			}
			
			return null;
		}
	}
	
	/**
	 * 播放模式
	 * @author TanQu
	 */
	public enum PlayMode {
		/** 横屏 */
		landScape(3),
		/** 竖屏 */
		portrait(4);
		
		private final int code;
		private PlayMode(int code) {
			this.code = code;
		}
		
		/**
		 * 取得类型对应的code
		 * @return
		 */
		public int getCode() {
			return code;
		}
		
		public static PlayMode getPlayMode(int code) {
			switch (code) {
				case 3:
					return landScape;
				case 4:
					return portrait;
			}
			
			return null;
		}
	}
}
