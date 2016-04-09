package yinzhi.micro_client.fragment;

import yinzhi.micro_client.R;
import yinzhi.micro_client.activity.LoginActivity;
import yinzhi.micro_client.activity.MainActivity;
import yinzhi.micro_client.activity.ProfileActivity;
import yinzhi.micro_client.activity.video.MyApplication;
import yinzhi.micro_client.network.constants.INetworkConstants;
import yinzhi.micro_client.network.vo.YZUserVO;
import yinzhi.micro_client.utils.ImageUtil;
import yinzhi.micro_client.utils.SpMessageUtil;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SettingFragment extends Fragment {
	@ViewInject(R.id.setting_logout)
	private Button logout;

	@ViewInject(R.id.setting_personal)
	private RelativeLayout personalRl;

	@ViewInject(R.id.setting_avator)
	private ImageView avatar;

	@ViewInject(R.id.setting_nickname)
	private TextView nickname;

	private static SettingFragment settingFragment;

	private MainActivity activity;

	private ImageButton close;

	public synchronized static SettingFragment getInstance() {
		if (settingFragment == null) {
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

	@Override
	public void onStart() {
		super.onStart();
		ImageLoader
				.getInstance()
				.displayImage(
						"http://roadshow.4i-test.com/data/upload/20160405/1459849464214.jpeg",
						avatar, ImageUtil.getDisplayOption(90));

		try {
			YZUserVO userInfo = MyApplication.getUserInfo();

			LogUtils.i(userInfo.toString());
			// ImageLoader.getInstance()
			// .displayImage(
			// INetworkConstants.YZMC_SERVER
			// + userInfo.getAvatarPicPath(), avatar,
			// ImageUtil.getDisplayOption(90));

			nickname.setText(userInfo.getNickname());
			logout.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.e("用户信息读取异常");
			Toast.makeText(getActivity(), "未登录", Toast.LENGTH_LONG).show();

			nickname.setText("未登录");
			logout.setVisibility(View.GONE);

		}

	}

	@OnClick(R.id.setting_logout)
	public void logoutClick(View v) {
		SpMessageUtil.deleteSPMsg("userinfo", getActivity()
				.getApplicationContext());

		MyApplication.setUserInfo(null);

		// 跳往登录页面
		LoginActivity.intentTo(getActivity(), getActivity().getClass()
				.getName());
		getActivity().overridePendingTransition(R.anim.activity_anim_up_in, 0);
	}

	@OnClick(R.id.setting_personal)
	public void personalClick(View v) {

		if (MyApplication.getUserInfo() != null) {

			Intent intent = new Intent(getActivity(), ProfileActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(
					R.anim.activity_anim_left_in, 0);
		} else {
			// 跳往登录页面
			LoginActivity.intentTo(getActivity(), getActivity().getClass()
					.getName());
		}
	}

}
