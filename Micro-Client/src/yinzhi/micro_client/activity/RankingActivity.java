package yinzhi.micro_client.activity;

import java.util.ArrayList;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.TabPageIndicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import yinzhi.micro_client.R;
import yinzhi.micro_client.fragment.RankingFragment;

public class RankingActivity extends BaseActivity {

	/**
	 * 课程介绍和目录
	 */
	@ViewInject(R.id.ranking_viewpager)
	private ViewPager pager;

	/**
	 * tab页切换指示标签
	 */
	@ViewInject(R.id.ranking_indicator)
	private TabPageIndicator indicator;

	private String[] text = new String[] { "免费排行", "畅销排行" };
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

	private Integer pageType = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranking);
		ViewUtils.inject(this);

		pageType = getIntent().getExtras().getInt("pageType");

		initFragment(pageType);
	}

	/**
	 * 子页相关
	 */
	private void initFragment(int type) {

		MyIntroductionAdapter myFragmentPagerAdapter = new MyIntroductionAdapter(getSupportFragmentManager());
		pager.setAdapter(myFragmentPagerAdapter);
		pager.setCurrentItem(type);
		indicator.setViewPager(pager);

	}

	class MyIntroductionAdapter extends FragmentPagerAdapter {

		public MyIntroductionAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			return RankingFragment.getInstance(position);
		}

		@Override
		public int getCount() {
			return 2;
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
