package yinzhi.micro_client.utils.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.lidroid.xutils.util.LogUtils;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> listFragments;
	private String[] text;
	private FragmentManager fm;
	
	public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> al, String[] text) {
		super(fm);
		this.listFragments = al;
		this.text = text;
		this.fm = fm;
	}

	public MyFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		LogUtils.i("getFragment position:"+position);
		Fragment fragment = null;
		if (listFragments != null && position < listFragments.size()) {
			fragment = listFragments.get(position);
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return listFragments.size();
	}

	@Override
	public Object instantiateItem(View container, int position) {
		Fragment fragment = (Fragment) super.instantiateItem(container, position);
		listFragments.remove(position);
		listFragments.add(position, fragment);
		return fragment;
	}
	
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		LogUtils.i("getTitle position:"+position);
		return text[position];
	}
	
}
