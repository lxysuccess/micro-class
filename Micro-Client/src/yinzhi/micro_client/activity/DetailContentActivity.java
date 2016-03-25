package yinzhi.micro_client.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import yinzhi.micro_client.R;
import yinzhi.micro_client.constant.Constants;
import yinzhi.micro_client.fragment.DiscussionFragment;
import yinzhi.micro_client.fragment.VideoCatalogFragment;
import yinzhi.micro_client.fragment.VideoDescriptionFragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class DetailContentActivity extends FragmentActivity implements MediaPlayer.OnCompletionListener,
MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, OnBufferingUpdateListener{
	/**
	 * 顶部工具栏layout
	 */
	@ViewInject(R.id.detail_menu_bar)
	private RelativeLayout menuBar;
	/**
	 * 视频控制栏layout
	 */
	@ViewInject(R.id.detail_video_control)
	private LinearLayout videoControl;
	
	/**
	 * 视频表面的工具栏是否在显示
	 */
	private Boolean isMenuBarShowing = true;
	
	/**
	 * 计时隐藏工具栏
	 */
	private Timer timer;
	
	/**
	 * 是否有task在schedule中等待执行
	 */
	
	/**
	 * 用于隐藏工具栏
	 */
	private final Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			
			if (msg.what == 1) {
				videoControl.setVisibility(View.GONE);
				menuBar.setVisibility(View.GONE);
				isMenuBarShowing = false;
			}
		}
		
	};
	
	/**
	 * 视频播放界面
	 */
	@ViewInject(R.id.detail_surfaceview)
	private SurfaceView surfaceView;

	/**
	 * surfaceView播放控制
	 */
	private SurfaceHolder surfaceHolder;

	/**
	 * 播放控制条
	 */
	@ViewInject(R.id.detail_seekbar)
	private SeekBar seekBar;

	/**
	 * 暂停播放按钮
	 */
	@ViewInject(R.id.detail_play_btn)
	private ImageButton playButton;

	/**
	 * 截图按钮
	 */
	private Button screenShotButton;

	/**
	 * 改变视频大小button
	 */
	@ViewInject(R.id.detail_video_size)
	private ImageButton videoSizeButton;

	/**
	 * 视频当前是否为全屏播放
	 */
	private Boolean isFullScreen = false;
	
	/**
	 * 加载进度显示条
	 */
	@ViewInject(R.id.detail_progressBar)
	private ProgressBar progressBar;

	/**
	 * 播放视频
	 */
	private static MediaPlayer mediaPlayer;

	/**
	 * 记录当前播放的位置
	 */
	private int playPosition = -1;

	/**
	 * seekBar是否自动拖动
	 */
	private boolean seekBarAutoFlag = false;

	/**
	 * 视频时间显示
	 */
	@ViewInject(R.id.detail_video_time)
	private TextView videoTimeTextView;

	/**
	 * 播放总时间
	 */
	private String videoTimeString;

	private long videoTimeLong;

	/**
	 * 播放路径
	 */
	private String pathString;

	/**
	 * 屏幕的宽度和高度
	 */
	private int screenWidth, screenHeight;
	/**
	 * 视频相关的介绍页面
	 */
	private ViewPager pager;
	
//	private TabPageIndicator indicator;
	/**
	 * 关闭页面按钮
	 */
	@ViewInject(R.id.detail_back)
	private ImageButton detailBackImgBtn;

	private String[] text = new String[] { "详情", "章节", "小贴士" };
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

	//来源页面
	private int source = -1;
	private String[] videoUrls={"part01.3gp",
			"part02.3gp",
			"part03.3gp",
			"part04.3gp",
			"part05.3gp",
			"part06.3gp",
			"part07.3gp"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_content);
		ViewUtils.inject(this);
		
		Intent intent = getIntent();
		if (intent != null) {
			source = intent.getIntExtra("choice", -1);
		}
		pager = (ViewPager) findViewById(R.id.detail_viewpager);
//		indicator = (TabPageIndicator) findViewById(R.id.detail_indicator);
		
		initFragment();
		//获取屏幕宽度和高度
		DisplayMetrics displayMetrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		screenWidth = displayMetrics.widthPixels;
		screenHeight = displayMetrics.heightPixels;
		initView();
		timer = new Timer();
//		timer.schedule(new Task(), 5000);
	}

	private void initView() {
		//设置surfaceHolder
		surfaceHolder = surfaceView.getHolder();
		//设置holder类型，该类型表示surfaceView自己不管理缓存区，虽然提示过时，但最好还是要设置
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.addCallback(new SurfaceCallback());
//		surfaceView.setZOrderOnTop(true);
//		surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
	}

	private void initFragment() {
		fragments.add(VideoDescriptionFragment.newInstance());
		fragments.add(VideoCatalogFragment.newInstance());
		fragments.add(DiscussionFragment.newInstance());
		MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(
				getSupportFragmentManager(), fragments);
		pager.setAdapter(myFragmentPagerAdapter);
		pager.setCurrentItem(0);
//		indicator.setViewPager(pager);
		
	}

	private class SurfaceCallback implements SurfaceHolder.Callback{

		@Override
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
				int arg3) {
			
		}

		@Override
		public void surfaceCreated(SurfaceHolder arg0) {
			// surfaceView被创建
			//设置播放资源
			playVideo();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			// surfaceView销毁,同时销毁mediaPlayer
            if (null != mediaPlayer) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
		}
		
	}
	
	/**
	 * 播放视频
	 */
	public void playVideo() {
		Log.i("detail", "playvideo,position"+Constants.playPosition); 
		//初始化MediaPlayer
		mediaPlayer = new MediaPlayer();
		//重置mediaPaly，建议初始化mediaplay立即调用
		mediaPlayer.reset();
		//设置声音效果
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		//设置播放完成监听
		mediaPlayer.setOnCompletionListener(this);
		//设置没提加载完以后回调函数
		mediaPlayer.setOnPreparedListener(this);
		//错误监听回调函数
		mediaPlayer.setOnErrorListener(this);
		//设置缓存监听
		mediaPlayer.setOnBufferingUpdateListener(this);
		
		Uri uri = Uri.parse(videoUrls[0]);
		if (source != -1) {
			uri = Uri.parse(videoUrls[source-1]);
		}
		try {
			AssetFileDescriptor fileDescriptor = getAssets().openFd(videoUrls[source-1]);
		   mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),fileDescriptor.getStartOffset(), fileDescriptor.getLength());
			//设置异步加载视频
			mediaPlayer.prepareAsync();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "加载视频错误！", Toast.LENGTH_LONG).show();
		}
	}
	
	public void setUri(Uri uri) {
		try {
			progressBar.setVisibility(View.VISIBLE);
			mediaPlayer.stop();
			mediaPlayer.reset();
			mediaPlayer.setDataSource(this, uri);
			//设置异步加载视频
			mediaPlayer.prepareAsync();
			mediaPlayer.start();
			progressBar.setVisibility(View.GONE);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "加载视频错误！", Toast.LENGTH_LONG).show();
		}

	}
	
	/**
	 * 关闭页面按钮点击事件
	 * @param v
	 */
	@OnClick(R.id.detail_back)
	public void backClick(View v) {
		LogUtils.i("点击backclick");
		if (isFullScreen) {
			changeVideoSize();
			LogUtils.i("当前全屏状态");
		}else {
			this.finish();
			overridePendingTransition(0, R.anim.activity_anim_left_out);
		}
	}

	/**
	 * 视频播放界面点击事件
	 * @param v
	 */
	@OnClick(R.id.detail_surfaceview)
	public void surfaceviewClick(View v) {
		Animation animationUp = AnimationUtils.loadAnimation(this, R.anim.activity_anim_up_in);
		Animation animationDown = AnimationUtils.loadAnimation(this, R.anim.activity_anim_down_out);
		if (isMenuBarShowing) {
			videoControl.startAnimation(animationDown);
			videoControl.setVisibility(View.GONE);
			menuBar.setVisibility(View.GONE);
			isMenuBarShowing = false;
			
		}else{
			videoControl.startAnimation(animationUp);
			videoControl.setVisibility(View.VISIBLE);
			menuBar.setVisibility(View.VISIBLE);
			isMenuBarShowing = true;
//			timer.schedule(new Task(), 5000);
		}
	}

	/**
	 * 视频播放按钮点击事件
	 * @param v
	 */
	@OnClick(R.id.detail_play_btn)
	public void playClick(View v) {
		if(mediaPlayer.isPlaying()){
			Constants.playPosition = mediaPlayer.getCurrentPosition();
			mediaPlayer.pause();
			playButton.setBackgroundResource(R.drawable.home_play);
		}else{
			if (Constants.playPosition >= 0) {
				mediaPlayer.seekTo(Constants.playPosition);
				mediaPlayer.start();
				playButton.setBackgroundResource(R.drawable.video_pause);
				Constants.playPosition = -1;
			}
		}
	}

	/**
	 * 视频全屏切换按钮点击事件
	 * @param v
	 */
	@OnClick(R.id.detail_video_size)
	public void videoSizeClick(View v) {
		// 调用改变大小的方法
        changeVideoSize();
	}

	/**
	 * 视频缓冲更新
	 */
	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int arg1) {
        // percent 表示缓存加载进度，0为没开始，100表示加载完成，在加载完成以后也会一直调用该方法
//        LogUtils.i("onBufferingUpdate-->" + arg1);
        // 可以根据大小的变化来
	}
	
	/**
	 * 视频加载完毕调用
	 */
	@Override
	public void onPrepared(MediaPlayer arg0) {
		Log.i("detail", "beforeonPrepared,position is ->"+Constants.playPosition);
		if (mediaPlayer == null) {
			return;
		}
		//隐藏进度条
		progressBar.setVisibility(View.GONE);
		try {
			//判断是否有保存播放位置
			if (Constants.playPosition >= 0) {
				mediaPlayer.seekTo(Constants.playPosition);
				Constants.playPosition = -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		seekBarAutoFlag = true;
		//设置控制条，放在加载完成后进行，防止获取getDuration();错误
		seekBar.setMax(mediaPlayer.getDuration());
		//设置播放时间
		videoTimeLong = mediaPlayer.getDuration();
		LogUtils.i("onprepared+"+videoTimeLong);
		videoTimeString = getShowTime(videoTimeLong);
		videoTimeTextView.setText("00:00:00/"+videoTimeString);
		//设置拖动监听事件
		seekBar.setOnSeekBarChangeListener(new SeekBarChangeListener());
		//开始播放视频
		mediaPlayer.start();
		//设置显示到屏幕
		mediaPlayer.setDisplay(surfaceHolder);
		//开启线程
		new Thread(runnable).start();
		//设置surfaceview保持在屏幕上
		mediaPlayer.setScreenOnWhilePlaying(true);
		surfaceHolder.setKeepScreenOn(true);
		Log.i("detail", "afteronPrepared,position is ->"+Constants.playPosition);
	}
	

	public Boolean isShowingExercise = false;
	/**
	 * 视频播放结束调用
	 */
	@Override
	public void onCompletion(MediaPlayer arg0) {
		LogUtils.i("执行onCompletion");
		Log.i("detail", "onCompletion,position"+Constants.playPosition);
		// 设置seeKbar跳转到最后位置
        seekBar.setProgress(Integer.parseInt(String.valueOf(videoTimeLong)));
        // 设置播放标记为false
        seekBarAutoFlag = false;
//        if (isShowingExercise == false) {
//        	Intent intent = new Intent(DetailContentActivity.this, ExerciseActivity.class);
//            startActivity(intent);
//            Log.i("detail", "startExerciseActivity");
//            isShowingExercise = true;
//		}
        
	}

	/**
	 * 视频播放错误调用
	 */
	@Override
	public boolean onError(MediaPlayer arg0, int what, int extra) {
		switch (what) {
        case MediaPlayer.MEDIA_ERROR_UNKNOWN:
            Toast.makeText(this, "MEDIA_ERROR_UNKNOWN", Toast.LENGTH_SHORT).show();
            break;
        case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
            Toast.makeText(this, "MEDIA_ERROR_SERVER_DIED", Toast.LENGTH_SHORT).show();
            break;
        default:
            break;
    }

    switch (extra) {
        case MediaPlayer.MEDIA_ERROR_IO:
            Toast.makeText(this, "MEDIA_ERROR_IO", Toast.LENGTH_SHORT).show();
            break;
        case MediaPlayer.MEDIA_ERROR_MALFORMED:
            Toast.makeText(this, "MEDIA_ERROR_MALFORMED", Toast.LENGTH_SHORT).show();
            break;
        case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
            Toast.makeText(this, "MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK",
                    Toast.LENGTH_SHORT).show();
            break;
        case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
            Toast.makeText(this, "MEDIA_ERROR_TIMED_OUT", Toast.LENGTH_SHORT).show();
            break;
        case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
            Toast.makeText(this, "MEDIA_ERROR_UNSUPPORTED", Toast.LENGTH_SHORT).show();
            break;
    }
    return false;
	}

	/**
	 * 滑动条变化线程
	 */
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			// 增加对异常的捕获，防止在判断mediaPlayer.isPlaying的时候，报IllegalStateException异常
			try {
				while(seekBarAutoFlag){
					if (null != mediaPlayer && DetailContentActivity.this.mediaPlayer.isPlaying()) {
						seekBar.setProgress(mediaPlayer.getCurrentPosition());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}; 
	/**
	 * seekBar拖动监听类
	 * @author LIXUYU
	 *
	 */
	private class SeekBarChangeListener implements OnSeekBarChangeListener{

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			if (progress >= 0) {
				//如果用户手动拖动控件，则设置视频跳转
				if (fromUser) {
					mediaPlayer.seekTo(progress);
				}
				//设置当前播放时间
				videoTimeTextView.setText(getShowTime(progress)+"/"+videoTimeString);
			}
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			
		}
		
	}
	
	/**
	 * 将视频的时间转换为String以显示
	 * @param videoTimeLong2
	 * @return
	 */
	private String getShowTime(long videoTimeLong2) {
		//获取日历函数
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(videoTimeLong2);
		SimpleDateFormat dateFormat = null;
		//判断是否大于60分钟，如果大于就显示小时。设置日期格式
		if (videoTimeLong2 / 60000 > 60) {
			dateFormat = new SimpleDateFormat("hh:mm:ss");
		}else {
			dateFormat = new SimpleDateFormat("mm:ss");
		}
		return dateFormat.format(calendar.getTime());
	}
	
	/**
	 * 改变视频窗口的大小
	 */
	private void changeVideoSize() {
		final float scale = getResources().getDisplayMetrics().density;
		if (isFullScreen) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			// 设置小屏
            // 设置SurfaceView的大小并居中显示
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(screenWidth,
            		(int)(220 * scale + 0.5f));
            surfaceView.setLayoutParams(layoutParams);
			isFullScreen = false;
			LogUtils.i("转换为小屏状态"+"isFullScreen->"+isFullScreen);
		}else{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			// 设置全屏
            // 设置SurfaceView的大小并居中显示
			FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(screenHeight,
                    screenWidth-(int)(20 * scale + 0.5f));
            surfaceView.setLayoutParams(layoutParams);
			isFullScreen = true;
			LogUtils.i("转换为全屏状态"+"isFullScreen->"+isFullScreen);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		LogUtils.i("onPause");
		Log.i("detail", "onPause");
		Log.i("detail", "beforePause,position is ->"+Constants.playPosition);
        try {
            if (null != mediaPlayer && mediaPlayer.isPlaying()) {
                Constants.playPosition = mediaPlayer.getCurrentPosition();
                Log.i("detail", "afterPosition,position is ->"+Constants.playPosition);
                mediaPlayer.pause();
                seekBarAutoFlag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		LogUtils.i("onResume");
		Log.i("detail", "onResume,position"+Constants.playPosition);
        // 判断播放位置
        if (Constants.playPosition >= 0) {

            if (null != mediaPlayer) {
                seekBarAutoFlag = true;
                mediaPlayer.seekTo(Constants.playPosition);
                mediaPlayer.start();
            } else {
                playVideo();
            }
        }
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogUtils.i("onDestroy");
		Log.i("detail", "onDestory,position"+Constants.playPosition);
        // 由于MediaPlay非常占用资源，所以建议屏幕当前activity销毁时，则直接销毁
        try {
            if (null != this.mediaPlayer) {
                // 提前标志为false,防止在视频停止时，线程仍在运行。
                seekBarAutoFlag = false;
                // 如果正在播放，则停止。
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                Constants.playPosition = -1;
                // 释放mediaPlayer
                this.mediaPlayer.release();
                this.mediaPlayer = null;
                Log.i("detail", "onDestory,mediaPlayer.release,position"+Constants.playPosition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
        if (null != mediaPlayer) {
            // 保存播放位置onSaveInstanceState
            Constants.playPosition = mediaPlayer.getCurrentPosition();
            Log.i("detail", "onSaveInstanceState,position"+Constants.playPosition);
        }
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	private class Task extends TimerTask{

		@Override
		public void run() {
			if (isMenuBarShowing) {
				handler.sendEmptyMessage(1);
			}
		}
		
	}
	
	class MyFragmentPagerAdapter extends FragmentPagerAdapter {
		private ArrayList<Fragment> listFragments;

		public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> al) {
			super(fm);
			listFragments = al;
		}

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return listFragments.get(position);
		}

		@Override
		public int getCount() {
			return listFragments.size();
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return text[position];
		}
	}
}
