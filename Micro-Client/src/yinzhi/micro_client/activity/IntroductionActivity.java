package yinzhi.micro_client.activity;

import java.util.ArrayList;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.TabPageIndicator;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

public class IntroductionActivity extends BaseActivity implements
		IUpdateData, IUpdateCatalogData {

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

	private String courseId ;
	

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activiyt_introduction);
		ViewUtils.inject(this);
		courseId = getIntent().getExtras().getString("courseId", "-1");

		initFragment();
	}

	/**
	 * 子页相关
	 */
	private void initFragment() {
		fragments.add(VideoDescriptionFragment.newInstance());
		fragments.add(VideoCatalogFragment.newInstance());
		((VideoDescriptionFragment) fragments.get(0)).setIUpdateData(this);
		((VideoCatalogFragment) fragments.get(1)).setIUpdateCatalog(this);

		MyIntroductionAdapter myFragmentPagerAdapter = new MyIntroductionAdapter(
				getSupportFragmentManager(), fragments);
		pager.setAdapter(myFragmentPagerAdapter);
		pager.setCurrentItem(0);
		indicator.setViewPager(pager);

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
		wait.setVisibility(View.VISIBLE);

		YZNetworkUtils.courseDetail(null, courseId,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						String response = arg0.result;

						// TODO 日志
						LogUtils.i(response);

						YZCourseVO course = YZResponseUtils.parseObject(
								response, YZCourseVO.class);
						
						if (course == null) {
							Toast.makeText(IntroductionActivity.this, "尚无详情数据！", Toast.LENGTH_LONG).show();
							// 去掉正在加载
							wait.setVisibility(View.GONE);
							return;
						}
						
						((VideoDescriptionFragment) fragments.get(0)).updateDataCompleted(course);

						BitmapUtils bitmapUtils = new BitmapUtils(
								IntroductionActivity.this);
						bitmapUtils.display(
								introImage,
								INetworkConstants.YZMC_SERVER
										+ course.getTeacherPicPath());

						// 去掉正在加载
						wait.setVisibility(View.GONE);
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
		wait.setVisibility(View.VISIBLE);
		YZNetworkUtils.courseCatalog(null, courseId, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String response = arg0.result;
				
				YZCatalogVO catalog = YZResponseUtils.parseCatalog(response);
				
				if (catalog == null) {
					Toast.makeText(IntroductionActivity.this, "尚无目录数据！", Toast.LENGTH_LONG).show();
					// 去掉正在加载
					wait.setVisibility(View.GONE);
					return;
				}
				//通知目录页更新完成
				((VideoCatalogFragment)fragments.get(1)).updateCatalogCompleted(catalog);
				
				// 去掉正在加载
				wait.setVisibility(View.GONE);
			}
			
		});
	}
	
}
