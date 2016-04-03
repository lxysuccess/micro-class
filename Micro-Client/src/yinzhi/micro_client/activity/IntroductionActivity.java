package yinzhi.micro_client.activity;

import java.util.ArrayList;

import yinzhi.micro_client.R;
import yinzhi.micro_client.fragment.VideoCatalogFragment;
import yinzhi.micro_client.fragment.VideoCatalogFragment.IUpdateCatalogData;
import yinzhi.micro_client.fragment.VideoDescriptionFragment;
import yinzhi.micro_client.fragment.VideoDescriptionFragment.IUpdateData;
import yinzhi.micro_client.network.YZNetworkUtils;
import yinzhi.micro_client.network.YZResponseUtils;
import yinzhi.micro_client.network.constants.INetworkConstants;
import yinzhi.micro_client.network.vo.YZCatalogVO;
import yinzhi.micro_client.network.vo.YZCourseVO;
import yinzhi.micro_client.utils.ImageUtil;
import yinzhi.micro_client.utils.SpMessageUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.TabPageIndicator;

public class IntroductionActivity extends BaseActivity implements IUpdateData,
		IUpdateCatalogData {

	private static final String TAG = IntroductionActivity.class
			.getSimpleName();

	@ViewInject(R.id.introduction_back)
	private ImageView back;

	/**
	 * 课程介绍和目录
	 */
	@ViewInject(R.id.introduction_viewpager)
	private ViewPager pager;

	/**
	 * tab页切换指示标签
	 */
	@ViewInject(R.id.introduction_indicator)
	private TabPageIndicator indicator;

	/**
	 * 课程介绍图片
	 */
	@ViewInject(R.id.introduction_image)
	private ImageView introImage;

	/**
	 * 进入学习
	 */
	@ViewInject(R.id.introduction_start)
	private Button introStart;

	/**
	 * 上次学习进度提示
	 */
	@ViewInject(R.id.introduction_history_record)
	private TextView historyRecord;

	@ViewInject(R.id.introduction_wait)
	private RelativeLayout wait;

	/**
	 * 参加课程
	 */
	@ViewInject(R.id.introduction_subscribe)
	private Button subscribe;

	private String[] text = new String[] { "课程介绍", "目录" };
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

	/**
	 * 记录用户是否已经订阅该课程
	 */
	private Integer isSubsribe = 0;

	/**
	 * 课程ID
	 */
	private String courseId;

	/**
	 * 资源ID
	 */
	public static String itemResourceId;

	/**
	 * 记录来源页的标识
	 */
	public static String fromActivity;

	/**
	 * 选择首先显示的tab页
	 */
	private Integer tabPos;

	/**
	 * 任务计数器，完成任务标志 每个任务完成均有一个质数标志，任务a为2，任务b为3，任务c为5。。。
	 * jobCount为完成的任务的指数标志的乘积，如完成a和b，jobCount=6,完成a和c，jobCount=10
	 */
	private Integer jobCount = 1;

	private static final Integer DETAIL_DONE = 2;

	private static final Integer CATALOG_DONE = 3;

	private Handler mHandler;

	public static Intent newIntent(Context context, String itemResourceId,
			String courseId, Integer tabPos, String fromActivity) {
		Intent intent = new Intent(context, IntroductionActivity.class);
		intent.putExtra("itemResourceId", itemResourceId);
		intent.putExtra("courseId", courseId);
		intent.putExtra("tabPos", tabPos);
		intent.putExtra("fromActivity", fromActivity);
		return intent;
	}

	public static void intentTo(Context context, String itemResourceId,
			String courseId, Integer tabPos, String fromActivity) {
		context.startActivity(newIntent(context, itemResourceId, courseId,
				tabPos, fromActivity));
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_introduction);
		ViewUtils.inject(this);

		courseId = getIntent().getExtras().getString("courseId", "-1");
		itemResourceId = getIntent().getExtras().getString("itemResourceId",
				"-1");
		tabPos = getIntent().getExtras().getInt("tabPos", 0);
		fromActivity = getIntent().getExtras().getString("fromActivity", "-1");

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				jobCount = jobCount * msg.what;
				switch (jobCount) {
				// DETAIL_DONE * CATALOG_DONE,完成目录获取和详情获取
				case 6:
					wait.setVisibility(View.GONE);
					break;
				}

			}
		};
		initFragment(tabPos);
	}

	/**
	 * 子页相关
	 */
	private void initFragment(int pos) {
		fragments.add(VideoDescriptionFragment.newInstance());
		fragments.add(VideoCatalogFragment.newInstance());
		((VideoDescriptionFragment) fragments.get(0)).setIUpdateData(this);
		((VideoCatalogFragment) fragments.get(1)).setIUpdateCatalog(this);

		MyIntroductionAdapter myFragmentPagerAdapter = new MyIntroductionAdapter(
				getSupportFragmentManager(), fragments);
		pager.setAdapter(myFragmentPagerAdapter);
		indicator.setViewPager(pager);
		indicator.setCurrentItem(pos);

	}

	class MyIntroductionAdapter extends FragmentPagerAdapter {
		private ArrayList<Fragment> listFragments;

		public MyIntroductionAdapter(FragmentManager fm, ArrayList<Fragment> al) {
			super(fm);
			listFragments = al;
		}

		public MyIntroductionAdapter(FragmentManager fm) {
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

	/**
	 * 更新简介
	 */
	@Override
	public void onUpdateDescription() {
		// 显示正在加载
		String token = SpMessageUtil.getLogonToken(getApplicationContext());

		LogUtils.i("token===================" + token);
		YZNetworkUtils.courseDetail(token, courseId,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						String response = arg0.result;

						// TODO 日志
						LogUtils.i(response);

						if (!YZNetworkUtils.isAllowedContinue(
								IntroductionActivity.this, response)) {
							return;
						}

						YZCourseVO course = YZResponseUtils.parseObject(
								response, YZCourseVO.class);

						if (course == null) {
							Toast.makeText(IntroductionActivity.this,
									"尚无详情数据！", Toast.LENGTH_LONG).show();
							// 去掉正在加载
							Message mMessage = new Message();
							mMessage.what = DETAIL_DONE;
							mHandler.sendMessage(mMessage);
							return;
						}

						try {
							// 用户已订阅，去除订阅按钮
							if (course.getIsSubscribe() == 1) {
								subscribe.setVisibility(View.GONE);
							}
						} catch (Exception e) {
							e.printStackTrace();
							LogUtils.i("error when get issubscribe");
						}

						((VideoDescriptionFragment) fragments.get(0))
								.updateDataCompleted(course);

						// 显示课程介绍图片
						ImageLoader.getInstance().displayImage(
								INetworkConstants.YZMC_SERVER
										+ course.getCoursePicPath(),
								introImage, ImageUtil.getDisplayOption(0));

						// 去掉正在加载
						Message mMessage = new Message();
						mMessage.what = DETAIL_DONE;
						mHandler.sendMessage(mMessage);
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO 异常处理
					}
				});
	}

	@Override
	public void onUpdateCatalog() {
		// 显示正在加载
		String token = SpMessageUtil.getLogonToken(getApplicationContext());

		YZNetworkUtils.courseCatalog(token, courseId,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						String response = arg0.result;

						LogUtils.i(response + "000000000000000");

						if (!YZNetworkUtils.isAllowedContinue(
								IntroductionActivity.this, response)) {
							return;
						}

						YZCatalogVO catalog = YZResponseUtils
								.parseCatalog(response);

						if (catalog == null) {
							Toast.makeText(IntroductionActivity.this,
									"尚无目录数据！", Toast.LENGTH_LONG).show();
							// 去掉正在加载
							Message mMessage = new Message();
							mMessage.what = CATALOG_DONE;
							mHandler.sendMessage(mMessage);
							return;
						}
						// 通知目录页更新完成
						((VideoCatalogFragment) fragments.get(1))
								.updateCatalogCompleted(catalog);

						// 去掉正在加载
						Message mMessage = new Message();
						mMessage.what = CATALOG_DONE;
						mHandler.sendMessage(mMessage);
					}

				});
	}

	@OnClick(R.id.introduction_back)
	public void backClick(View v) {
		finish();
		overridePendingTransition(0, R.anim.activity_anim_left_out);
	}

	@OnClick(R.id.introduction_subscribe)
	public void subscribeClick(View v) {
		YZNetworkUtils.courseSubscribe(
				SpMessageUtil.getLogonToken(getApplicationContext()), courseId,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						String response = arg0.result;
						
						LogUtils.i("订阅-->"+response);

						if (!YZNetworkUtils.isAllowedContinue(
								IntroductionActivity.this, response)) {
							return;
						}

						Toast.makeText(IntroductionActivity.this, "订阅成功",
								Toast.LENGTH_SHORT).show();
						
						subscribe.setVisibility(View.GONE);
						isSubsribe = 1;
						

					}
				});

	}
}
