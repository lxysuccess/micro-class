package yinzhi.micro_client.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import yinzhi.micro_client.R;
import yinzhi.micro_client.activity.CourseListActivity;
import yinzhi.micro_client.activity.MainActivity;
import yinzhi.micro_client.activity.ProfileActivity;
import yinzhi.micro_client.adapter.LxyCommonAdapter;
import yinzhi.micro_client.adapter.LxyViewHolder;
import yinzhi.micro_client.network.YZNetworkUtils;
import yinzhi.micro_client.network.YZResponseUtils;
import yinzhi.micro_client.network.constants.INetworkConstants;
import yinzhi.micro_client.network.vo.YZCourseVO;
import yinzhi.micro_client.utils.ImageUtil;
import yinzhi.micro_client.utils.SpMessageUtil;
import yinzhi.micro_client.utils.adapter.MyFragmentPagerAdapter;

public class MyFragment extends Fragment implements
		SwipeRefreshLayout.OnRefreshListener {
	private static MyFragment fragmentMy;

	private MainActivity activity;

	@ViewInject(R.id.my_menu)
	private ImageButton close;

	@ViewInject(R.id.my_edit)
	private ImageButton edit;

	@ViewInject(R.id.my_avator)
	private ImageView avatar;

	@ViewInject(R.id.my_avator)
	private ImageView avator;

	@ViewInject(R.id.my_layout)
	private LinearLayout parentLayout;

	private String avatarFilePath;

	private PopupWindow popupWindow;

	@ViewInject(R.id.my_course_list_result)
	private ListView listView;

	@ViewInject(R.id.my_course_list_swipelayout)
	private SwipeRefreshLayout mSwipeLayout;

	// 存储搜索结果
	private List<YZCourseVO> datas = new ArrayList<YZCourseVO>();

	private LxyCommonAdapter<YZCourseVO> adapter;

	/**
	 * 下拉刷新
	 * 
	 * @return
	 */
	private static final int REFRESH_COMPLETE = 0X110;

	private Handler rHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_COMPLETE:
				mSwipeLayout.setRefreshing(false);
				break;

			}
		};
	};

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
		// try {
		// Field childFragmentManager =
		// Fragment.class.getDeclaredField("mChildFragmentManager");
		// childFragmentManager.setAccessible(true);
		// childFragmentManager.set(this, null);
		// } catch (NoSuchFieldException e) {
		// throw new RuntimeException();
		// } catch (IllegalAccessException e) {
		// throw new RuntimeException();
		// }
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_study_center, null);
		ViewUtils.inject(this, rootView);

		ImageLoader
				.getInstance()
				.displayImage(
						"http://roadshow.4i-test.com/data/upload/20160202/1454417235576.png",
						avatar, ImageUtil.getDisplayOption(90));

		initView();

		mSwipeLayout.setOnRefreshListener(this);
		onRefresh();

		mSwipeLayout.setColorScheme(android.R.color.holo_green_dark,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		return rootView;
	}

	private void initData() {
		YZNetworkUtils.fetchMyCourseList(SpMessageUtil
				.getLogonToken(getActivity().getApplicationContext()), 0, 10000,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						String response = arg0.result;

						LogUtils.i(response);

						if (!YZNetworkUtils.isAllowedContinue(getActivity(),
								response)) {
							return;
						}

						List<YZCourseVO> courses = YZResponseUtils.parseArray(
								response, YZCourseVO.class);

						if (courses == null) {
							Toast.makeText(getActivity(), "未参与相关课程",
									Toast.LENGTH_LONG).show();
							return;
						}

						datas.clear();

						datas.addAll(courses);

						// 通知列表数据变化
						adapter.notifyDataSetChanged();

						rHandler.sendEmptyMessage(REFRESH_COMPLETE);

					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

					}
				});
	}

	public void initView() {

		datas = new ArrayList<YZCourseVO>();
		adapter = new LxyCommonAdapter<YZCourseVO>(getActivity(), datas,
				R.layout.item_course_list) {
			@Override
			public void convert(LxyViewHolder holder, YZCourseVO course) {
				try {
					holder.setImageViewUrl(
							R.id.course_icon,
							INetworkConstants.YZMC_SERVER
									+ course.getCoursePicPath());
					holder.setText(R.id.course_name, course.getTitle());
					holder.setText(R.id.course_click_count, course
							.getClickCount().toString());
					holder.setText(R.id.course_teacher_name,
							course.getTeacherName());
				} catch (Exception e) {
					e.printStackTrace();
					LogUtils.e("show  course list by classify error");
				}
			}
		};
		listView.setAdapter(adapter);
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
				// avator.setImageBitmap(cameraBitmap);
				break;

			/* 拍照后保存图片，并跳到裁剪功能 */
			case 4:
				LogUtils.i("onResult+4");
				Bitmap avatartBitmap = BitmapFactory.decodeFile(avatarFilePath);
				LogUtils.i("保存了选自相册的头像，path->" + avatarFilePath);
				avatartBitmap = ImageUtil.getRotateBitmap(avatartBitmap, 90.0f);
				// avator.setImageBitmap(avatartBitmap);
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@OnClick(R.id.my_avator)
	public void avatorClick(View v) {
		popupWindow = new PopupWindows(getActivity(), parentLayout);
	}

	@OnClick(R.id.my_edit)
	public void editClick(View v) {

		// 跳转到用户个人信息详情页
		Intent intent = new Intent(getActivity(), ProfileActivity.class);
		startActivity(intent);
		getActivity()
				.overridePendingTransition(R.anim.activity_anim_left_in, 0);
	}

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

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setText("拍照");
			bt2.setText("相册");
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					boolean sdCardExist = Environment.getExternalStorageState()
							.equals(android.os.Environment.MEDIA_MOUNTED);
					if (sdCardExist) {
						avatarFilePath = Environment
								.getExternalStorageDirectory()
								.getAbsolutePath()
								+ "/MicroClient/" + "microclientavatar.jpg";
						LogUtils.i("sdcard存在" + avatarFilePath);
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

	@OnClick(R.id.my_menu)
	public void closeClick(View v) {
		activity.toggle();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		initData();
	}
}
