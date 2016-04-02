package yinzhi.micro_client.activity.video;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import yinzhi.micro_client.R;
import yinzhi.micro_client.activity.BaseActivity;
import yinzhi.micro_client.activity.CommentActivity;
import yinzhi.micro_client.adapter.LxyCommonAdapter;
import yinzhi.micro_client.adapter.LxyViewHolder;
import yinzhi.micro_client.network.YZNetworkUtils;
import yinzhi.micro_client.network.YZResponseUtils;
import yinzhi.micro_client.network.vo.YZSubtitleVO;
import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easefun.polyvsdk.QuestionVO;
import com.easefun.polyvsdk.Video.ADMatter;
import com.easefun.polyvsdk.ijk.IjkVideoView;
import com.easefun.polyvsdk.ijk.IjkVideoView.ErrorReason;
import com.easefun.polyvsdk.ijk.OnPreparedListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;

public class IjkVideoActicity extends BaseActivity {

	private ListView subtitleResult;

	private Button startSearch;

	private EditText subtitleInput;

	private ImageView backIv;

	private ImageView commentIv;

	private ImageView shareIv;

	/**
	 * 底部操作栏
	 */
	private RelativeLayout bottomBar;

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

	private List<YZSubtitleVO> sutitleDatas = new ArrayList<YZSubtitleVO>();

	private LxyCommonAdapter<YZSubtitleVO> stAdapter;
	/**
	 * 视频资源的id
	 */
	private String itemResourceId;

	public static Intent newIntent(Context context, PlayMode playMode, PlayType playType, String value,
			String itemResourceId, boolean startNow) {
		Intent intent = new Intent(context, IjkVideoActicity.class);
		intent.putExtra("playMode", playMode.getCode());
		intent.putExtra("playType", playType.getCode());
		intent.putExtra("value", value);
		intent.putExtra("startNow", startNow);
		intent.putExtra("itemResourceId", itemResourceId);
		return intent;
	}

	public static void intentTo(Context context, PlayMode playMode, PlayType playType, String value,
			String itemResourceId, boolean startNow) {
		context.startActivity(newIntent(context, playMode, playType, value, itemResourceId, startNow));
	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_small2);

		bottomBar = (RelativeLayout) findViewById(R.id.video_bottom_bar);

		backIv = (ImageView) findViewById(R.id.video_back);
		commentIv = (ImageView) findViewById(R.id.video_comment);
//		shareIv = (ImageView) findViewById(R.id.video_share);

		subtitleResult = (ListView) findViewById(R.id.subtitle_search_result);
		startSearch = (Button) findViewById(R.id.search_start);
		subtitleInput = (EditText) findViewById(R.id.subtitle_search_input);

		// 资源ID
		itemResourceId = getIntent().getStringExtra("itemResourceId");

		// 播放模式：横屏，竖屏
		int playModeCode = getIntent().getIntExtra("playMode", 0);
		PlayMode playMode = PlayMode.getPlayMode(playModeCode);

		// 播放类型：vid或url
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
		// 小窗口的比例
		float ratio = (float) 16 / 9;
		adjusted_h = (int) Math.ceil((float) w / ratio);
		rl = (RelativeLayout) findViewById(R.id.rl);
		rl.setLayoutParams(new RelativeLayout.LayoutParams(w, adjusted_h));
		videoView = (IjkVideoView) findViewById(R.id.videoview);

		ProgressBar progressBar = (ProgressBar) findViewById(R.id.loadingprogress);
		TextView videoAdCountDown = (TextView) findViewById(R.id.count_down);
		logo = (ImageView) findViewById(R.id.logo);

		// 在缓冲时出现的loading
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

		mediaController = new MediaController(this, true);
		mediaController.setIjkVideoView(videoView);
		mediaController.setAnchorView(videoView);
		videoView.setMediaController(mediaController);

		// 设置切屏事件
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

		// 播放界面底部三个按钮事件监听
		setIvListener();

		startSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (subtitleInput.getText().length() == 0) {
					Toast.makeText(IjkVideoActicity.this, "关键词不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				isReset = true;
				fetchDatas(0, 20);

			}
		});

		// 初始化适配器
		stAdapter = new LxyCommonAdapter<YZSubtitleVO>(IjkVideoActicity.this, sutitleDatas,
				R.layout.item_subtitle_result) {

			@Override
			public void convert(LxyViewHolder holder, YZSubtitleVO t) {
				try {
					holder.setText(R.id.subtitle_content, t.getSubtitle());
					holder.setText(R.id.subtitle_time, t.getTime());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					LogUtils.e("error! video view when show subtile list");
				}
			}
		};

		// 设置字幕搜索结果list 的适配器
		subtitleResult.setAdapter(stAdapter);

		subtitleResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				LogUtils.i("position------>" + position);

				// 将时间转化为viewView可用格式

				String time = sutitleDatas.get(position).getTime();
				Long millsTime = getMillsTime(time);

				// TODO 视频跳至某个时间点
				videoView.seekTo(millsTime);

			}
		});
	}

	/**
	 * 设置底部三个按钮监听
	 */
	private void setIvListener() {

		backIv.setOnClickListener(new IvLisenter());
		commentIv.setOnClickListener(new IvLisenter());
		shareIv.setOnClickListener(new IvLisenter());
	}

	private class IvLisenter implements View.OnClickListener {

		@Override
		public void onClick(View arg0) {
			Integer id = arg0.getId();

			switch (id) {
			case R.id.video_back:
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
				finish();
				break;

			case R.id.video_comment:

				Intent intent = new Intent(IjkVideoActicity.this, CommentActivity.class);
				intent.putExtra("itemResourceId", itemResourceId);
				startActivity(intent);
				break;
//			case R.id.video_share:
//
//				break;
			default:
				break;
			}

		}

	}

	/**
	 * 切换到横屏
	 */
	public void changeToLandscape() {
		// 隐藏除视频播放界面以外视图
		bottomBar.setVisibility(View.GONE);
		subtitleResult.setVisibility(View.GONE);

		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(h, w);
		rl.setLayoutParams(p);
		stopPosition = videoView.getCurrentPosition();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	/**
	 * 切换到竖屏
	 */
	public void changeToPortrait() {
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(w, adjusted_h);
		rl.setLayoutParams(p);
		stopPosition = videoView.getCurrentPosition();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		bottomBar.setVisibility(View.VISIBLE);
		subtitleResult.setVisibility(View.VISIBLE);
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
	 * 
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
		 * 
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
	 * 
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
		 * 
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

	/**
	 * 将视频的时间转换为毫秒
	 * 
	 * @param videoTimeLong2
	 * @return
	 */
	private Long getMillsTime(String stTime) {

		SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
		Date d1 = null;
		try {
			d1 = sf.parse(stTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d1.getTime();

	}

	private Boolean isReset = true;

	/**
	 * 获取字幕搜索数据
	 * 
	 * @param page
	 * @param size
	 */
	private void fetchDatas(int page, int size) {
		YZNetworkUtils.searchVideoSubtitle("", subtitleInput.getText().toString(), itemResourceId, page, size,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {

						String response = arg0.result;

						LogUtils.i(response + "------------------subtitle");

						if(!YZNetworkUtils.isAllowedContinue(IjkVideoActicity.this, response)){
							return;
						}

						List<YZSubtitleVO> results = new ArrayList<YZSubtitleVO>();
						results = YZResponseUtils.parseArray(response, YZSubtitleVO.class);

						if (isReset) {
							// 如果用户点击了搜索按钮，则清空之前的数据重新刷新列表
							sutitleDatas.clear();
						}
						sutitleDatas.addAll(results);

						stAdapter.notifyDataSetChanged();

					}
				});

	}
}
