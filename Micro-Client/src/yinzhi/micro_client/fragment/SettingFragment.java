package yinzhi.micro_client.fragment;

import yinzhi.micro_client.activity.LoginActivity;
import yinzhi.micro_client.activity.MainActivity;
import yinzhi.micro_client.activity.ProfileActivity;
import yinzhi.micro_client.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class SettingFragment extends Fragment {
	
	@ViewInject(R.id.setting_logout)
	private Button logout;
	
	@ViewInject(R.id.setting_personal)
	private RelativeLayout personalRl;
	
	private static SettingFragment settingFragment;
	
	private MainActivity activity;
	
	private ImageButton close;
	
	
	public synchronized static SettingFragment getInstance(){
		if(settingFragment == null){
			settingFragment = new SettingFragment();
		}
		return settingFragment;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (MainActivity) activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_setting, null);
		ViewUtils.inject(this, rootView);
		close = (ImageButton) rootView.findViewById(R.id.setting_menu_imgbtn);
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				activity.toggle();
			}
		});
		return rootView;
	}
	
	@OnClick(R.id.setting_logout)
	public void logoutClick(View v){
		Intent intent = new Intent(getActivity(),LoginActivity.class);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.activity_anim_up_in, 0);
	}
	
	
	@OnClick(R.id.setting_personal)
	public void personalClick(View v){
		Intent intent = new Intent(getActivity(),ProfileActivity.class);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.activity_anim_left_in, 0);
	}
	
}
