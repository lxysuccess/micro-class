package yinzhi.micro_client.activity.video;

import java.io.File;

import yinzhi.micro_client.network.vo.YZUserVO;
import yinzhi.micro_client.utils.SpMessageUtil;
import android.app.Application;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.easefun.polyvsdk.PolyvSDKClient;
import com.easefun.polyvsdk.SDKUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class MyApplication extends Application {

	// 获取到主线程的上下文
	public static MyApplication mContext = null;
	// 获取到主线程的handler
	public static Handler mMainThreadHandler = null;
	// 获取到主线程的looper
	public static Looper mMainThreadLooper = null;
	// 获取到主线程
	public static Thread mMainThead = null;
	// 获取到主线程的id
	public static int mMainTheadId;

	public static YZUserVO userInfo = null;

	public MyApplication() {

	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		PolyvSDKClient client = PolyvSDKClient.getInstance();
		client.stopService(getApplicationContext(), PolyvDemoService.class);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		this.mContext = this;
		this.mMainThreadHandler = new Handler();
		this.mMainThreadLooper = getMainLooper();
		this.mMainThead = Thread.currentThread();
		// android.os.Process.myUid()获取到用户id
		// android.os.Process.myPid();//获取到进程id
		// android.os.Process.myTid()获取到调用线程的id
		this.mMainTheadId = android.os.Process.myTid();// 主線程id

		// 获取sp中的用户信息
		userInfo = SpMessageUtil.getYZUserVO(mContext);

		File cacheDir = StorageUtils.getOwnCacheDirectory(
				getApplicationContext(), "polyvSDK/Cache");
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.memoryCacheExtraOptions(480, 800)
				.threadPoolSize(2)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				// .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 *
				// 1024))
				// You can pass your own memory cache implementation/
				.memoryCache(new WeakMemoryCache())
				.memoryCacheSize(2 * 1024 * 1024)
				.diskCacheSize(50 * 1024 * 1024)
				// .discCacheFileNameGenerator(new Md5FileNameGenerator())//
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.diskCacheFileCount(100)
				.diskCache(new UnlimitedDiscCache(cacheDir))
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(
						new BaseImageDownloader(getApplicationContext(),
								5 * 1000, 30 * 1000)).writeDebugLogs() // Remove
																		// for
																		// release
																		// app
				.build();

		// Initialize ImageLoader with configuration
		ImageLoader.getInstance().init(config);
		initPolyvCilent();
	}

	public void initPolyvCilent() {
		File saveDir = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			saveDir = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/polyvdownload");
			if (saveDir.exists() == false)
				saveDir.mkdir();
		}

		// 网络方式取得SDK加密串，（推荐）
		// new LoadConfigTask().execute();
		PolyvSDKClient client = PolyvSDKClient.getInstance();
		// 设置SDK加密串
		// client.setConfig("你的SDK加密串");
		// 测试数据
		client.setConfig("iPGXfu3KLEOeCW4KXzkWGl1UYgrJP7hRxUfsJGldI6DEWJpYfhaXvMA+32YIYqAOocWd051v5XUAU17LoVlgZCSEVNkx11g7CxYadcFPYPozslnQhFjkxzzjOt7lUPsW");
		// client.setConfig("mxQYuEGNwyYwdd6LbK5IcPf1DfoeukQubN9L6HRKMlLM6Y+kGhCX8rrMsD5bXLiJ8DrgJmFSrlb9HuBT1JXn2PJldgTmVrWIxKg/1FEq8Hcrrrq8HNddgbYp9Ld2JGRkE9Xz1RWG1FfAe6uorAqk0g==");
		// 下载文件的目录
		client.setDownloadDir(saveDir);
		// 初始化数据库服务
		client.initDatabaseService(this);
		// 启动服务
		client.startService(getApplicationContext(), PolyvDemoService.class);
		// 启动Bugly
		client.initCrashReport(getApplicationContext());
		// 启动Bugly后，在学员登录时设置学员id
		// client.crashReportSetUserId(userId);
	}

	public class LoadConfigTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			String config = SDKUtil.getUrl2String(
					"http://demo.polyv.net/demo/appkey.php", false);
			if (TextUtils.isEmpty(config)) {
				try {
					throw new Exception("没有取到数据");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return config;
		}

		@Override
		protected void onPostExecute(String config) {
			PolyvSDKClient client = PolyvSDKClient.getInstance();
			client.setConfig(config);
		}
	}

	public static MyApplication getApplication() {
		return mContext;
	}

	public static Handler getMainThreadHandler() {
		return mMainThreadHandler;
	}

	public static Looper getMainThreadLooper() {
		return mMainThreadLooper;
	}

	public static Thread getMainThread() {
		return mMainThead;
	}

	public static int getMainThreadId() {
		return mMainTheadId;
	}

	public static void setUserInfo(YZUserVO user) {
		userInfo = user;
	}

	public static YZUserVO getUserInfo() {
		return userInfo;
	}

}
