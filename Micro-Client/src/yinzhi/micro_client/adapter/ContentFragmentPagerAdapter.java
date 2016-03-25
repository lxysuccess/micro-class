package yinzhi.micro_client.adapter;

import java.util.List;

import yinzhi.micro_client.bean.ContentBean;
import yinzhi.micro_client.fragment.TestContentFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ContentFragmentPagerAdapter extends FragmentPagerAdapter {

	private List<ContentBean> list;
	public ContentFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public ContentFragmentPagerAdapter(FragmentManager fm,List<ContentBean> list) {
		super(fm);
		this.list = list;
	}

	@Override
	public Fragment getItem(int arg0) {
		
		return TestContentFragment.newInstance(list.get(arg0).getContent());
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return list.get(position).getTitle();
	}

}
