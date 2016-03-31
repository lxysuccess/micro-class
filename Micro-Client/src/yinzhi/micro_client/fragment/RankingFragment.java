package yinzhi.micro_client.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tencent.bugly.proguard.ac;
import com.viewpagerindicator.TabPageIndicator;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import yinzhi.micro_client.R;
import yinzhi.micro_client.activity.MainActivity;

public class RankingFragment extends Fragment {

	/**
	 * 课程介绍和目录
	 */
	@ViewInject(R.id.ranking_viewpager)
	private ViewPager pager;

	@ViewInject(R.id.ranking_menu_imgbtn)
	private ImageButton mImageButton;

	/**
	 * tab页切换指示标签
	 */
	@ViewInject(R.id.ranking_indicator)
	private TabPageIndicator indicator;

	private String[] text = new String[] { "免费排行", "畅销排行" };
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

	private static RankingFragment fragmentRanking;

	private MainActivity activity;

	public synchronized static RankingFragment getInstance() {
		if (fragmentRanking == null) {
			fragmentRanking = new RankingFragment();
		}
		return fragmentRanking;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException();
		} catch (IllegalAccessException e) {
			throw new RuntimeException();
		}
		this.activity = (MainActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_ranking, null);
		ViewUtils.inject(this, rootView);

		initFragment();

		return rootView;
	}

	/**
	 * 子页相关
	 */
	private void initFragment() {

		MyIntroductionAdapter myFragmentPagerAdapter = new MyIntroductionAdapter(
				getActivity().getSupportFragmentManager());
		pager.setAdapter(myFragmentPagerAdapter);
		pager.setCurrentItem(0);
		indicator.setViewPager(pager);

	}

	/**
	 * 调出侧滑菜单
	 * 
	 * @param v
	 */
	@OnClick(R.id.ranking_menu_imgbtn)
	public void menuClick(View v) {
		activity.toggle();
	}

	class MyIntroductionAdapter extends FragmentPagerAdapter {

		public MyIntroductionAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public SubRankingFragment getItem(int position) {

			return new SubRankingFragment(position);
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
