package yinzhi.micro_client.fragment;

import java.util.ArrayList;

import yinzhi.micro_client.R;
import yinzhi.micro_client.adapter.NavDrawerListAdapter;
import yinzhi.micro_client.bean.NavDrawerItem;
import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class MenuFragment extends Fragment implements OnItemClickListener{

	private ListView mDrawerList;
	private String[] mNavMenuTitles;
	private TypedArray mNavMenuIconsTypeArray;
	private ArrayList<NavDrawerItem> mNavDrawerItems;
	private NavDrawerListAdapter mAdapter;
	private SLMenuListOnItemClickListener mCallback;
	private int selected = -1;
	private RelativeLayout leftMenuLogout;
	
	private static MenuFragment menuFragment;
	
	public synchronized static MenuFragment getInstance() {
		if(menuFragment == null){
			menuFragment = new MenuFragment();
		}
		return menuFragment;
	}
	
	@Override
	public void onAttach(Activity activity) {
		try {
			mCallback = (SLMenuListOnItemClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnResolveTelsCompletedListener");
		}
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_menu, null);
		findView(rootView);
		return rootView;
	}

	private void findView(View rootView) {
		
		mDrawerList = (ListView) rootView.findViewById(R.id.left_menu_list);  
		
		mNavMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);  
        // nav drawer icons from resources  
        mNavMenuIconsTypeArray = getResources()  
                    .obtainTypedArray(R.array.nav_drawer_icons);  
              
        mNavDrawerItems = new ArrayList<NavDrawerItem>();  
  
        // Home  
        mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[0], mNavMenuIconsTypeArray  
                .getResourceId(0, -1)));  
        // Micro  
        mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[1], mNavMenuIconsTypeArray  
                .getResourceId(1, -1)));  
        // my  
        mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[2], mNavMenuIconsTypeArray  
                .getResourceId(2, -1)));  
        // Recycle the typed array  
        mNavMenuIconsTypeArray.recycle();  
          
        // setting the nav drawer list adapter  
        mAdapter = new NavDrawerListAdapter(getActivity(),  mNavDrawerItems);  
        
        mDrawerList.setAdapter(mAdapter);  
        mDrawerList.setOnItemClickListener(this);  
        
        if(selected!=-1){
        	mDrawerList.setItemChecked(selected, true);  
            mDrawerList.setSelection(selected);  
        }else{
        	mDrawerList.setItemChecked(0, true);  
            mDrawerList.setSelection(0);  
        }
        leftMenuLogout = (RelativeLayout) rootView.findViewById(R.id.left_menu_logout_layout);
        leftMenuLogout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mDrawerList.setItemChecked(5, true);  
		        mDrawerList.setSelection(5); 
				if(mCallback!=null){
		        	mCallback.selectItem(5, "setting");
		        }
				 selected = 5;
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		// update selected item and title, then close the drawer  
        mDrawerList.setItemChecked(position, true);  
        mDrawerList.setSelection(position);  
        
        if(mCallback!=null){
        	mCallback.selectItem(position, mNavMenuTitles[position]);
        }
        selected = position;
	}

	/**
     * 
     * @author 
     *
     */
    public interface SLMenuListOnItemClickListener{
    	
    	public void selectItem(int position,String title);
    }
    
}
