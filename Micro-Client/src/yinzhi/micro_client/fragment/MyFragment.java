package yinzhi.micro_client.fragment;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

import yinzhi.micro_client.R;
import yinzhi.micro_client.activity.MainActivity;
import yinzhi.micro_client.activity.ProfileActivity;
import yinzhi.micro_client.utils.ImageUtil;
import yinzhi.micro_client.utils.adapter.MyFragmentPagerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class MyFragment extends Fragment {
	private static MyFragment fragmentMy;
	
	private MainActivity activity; 
	
	private ImageButton close;
	
	private ImageButton edit;
	
//	@ViewInject(R.id.my_avator)
//	private ImageView avator;
	
	@ViewInject(R.id.my_layout)
	private LinearLayout parentLayout;
	
	private String avatarFilePath;
	
	private PopupWindow popupWindow;
	
	private ViewPager pager;
	
//	private TabPageIndicator indicator;
	
	private Boolean isInitFragment = false;
	
	private MyFragmentPagerAdapter myFragmentPagerAdapter;
	
	public synchronized static MyFragment getInstance() {
		if (fragmentMy == null) {
			fragmentMy = new MyFragment();
		}
		return fragmentMy;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (MainActivity) activity;
	}
	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException();
		}catch (IllegalAccessException e) {
			throw new RuntimeException();
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		myFragmentPagerAdapter.notifyDataSetChanged();
		super.onResume();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_my, null);
		ViewUtils.inject(this, rootView);
		pager = (ViewPager) rootView.findViewById(R.id.my_detail_viewpager);
//		indicator = (TabPageIndicator) rootView.findViewById(R.id.my_detail_indicator);
		initFragment();
		close = (ImageButton) rootView.findViewById(R.id.my_menu);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				activity.toggle();
			}
		});
		edit = (ImageButton) rootView.findViewById(R.id.my_edit);
		edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),ProfileActivity.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.activity_anim_left_in, 0);
			}
		});
		return rootView;
	}
	private void initFragment() {
		LogUtils.i("initFragment");
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(MyCourseFragment.newInstance());
		fragments.add(XueZhiFragment.newInstance());
		myFragmentPagerAdapter = new MyFragmentPagerAdapter(
				this.getChildFragmentManager(), fragments, new String[] { "我的课程", "学秩网"});
		pager.setAdapter(myFragmentPagerAdapter);
		myFragmentPagerAdapter.notifyDataSetChanged();
		pager.setCurrentItem(0);
//		indicator.setViewPager(pager);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != getActivity().RESULT_OK) {
			return;
		} else {
			switch (requestCode) {
			/* 相册选择图片，并跳到裁剪功能 */
			case 3:
				LogUtils.i("onResult+3");
				Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");
//				avator.setImageBitmap(cameraBitmap);
				break;

			/* 拍照后保存图片，并跳到裁剪功能 */
			case 4:
				LogUtils.i("onResult+4");
				Bitmap avatartBitmap = BitmapFactory.decodeFile(avatarFilePath);
				LogUtils.i("保存了选自相册的头像，path->" + avatarFilePath);
				avatartBitmap = ImageUtil.getRotateBitmap(avatartBitmap, 90.0f);
//				avator.setImageBitmap(avatartBitmap);
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
//	@OnClick(R.id.my_avator)
//	public void avatorClick(View v) {
//		popupWindow = new PopupWindows(getActivity(), parentLayout);
//	}
	
	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {

			View view = View.inflate(mContext, R.layout.popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			// ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
			// R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
			bt1.setText("拍照");
			bt2.setText("相册");
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					boolean sdCardExist = Environment.getExternalStorageState()
							.equals(android.os.Environment.MEDIA_MOUNTED);
					if (sdCardExist) {
						avatarFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MicroClient/"
								 + "microclientavatar.jpg";
						LogUtils.i("sdcard存在"+avatarFilePath);
						Intent intent4 = new Intent(
								"android.media.action.IMAGE_CAPTURE");
						intent4.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(new File(avatarFilePath)));
						intent4.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
						startActivityForResult(intent4, 4);
					} else
						Toast.makeText(v.getContext(), "请插入sd卡",
								Toast.LENGTH_LONG).show();
					dismiss();
				}
			});

			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent3 = new Intent(Intent.ACTION_GET_CONTENT);
					intent3.addCategory(Intent.CATEGORY_OPENABLE);
					intent3.setType("image/*");
					intent3.putExtra("crop", "true");
					intent3.putExtra("aspectX", 1);
					intent3.putExtra("aspectY", 1);
					intent3.putExtra("outputX", 150);
					intent3.putExtra("outputY", 150);
					intent3.putExtra("return-data", true);
					startActivityForResult(intent3, 3);
					dismiss();
				}
			});

			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

}
