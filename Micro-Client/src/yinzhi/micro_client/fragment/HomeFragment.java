package yinzhi.micro_client.fragment;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import yinzhi.micro_client.R;
import yinzhi.micro_client.activity.IntroductionActivity;
import yinzhi.micro_client.activity.MainActivity;
import yinzhi.micro_client.activity.SearchActivity;
import yinzhi.micro_client.network.YZNetworkUtils;
import yinzhi.micro_client.network.YZResponseUtils;
import yinzhi.micro_client.network.constants.INetworkConstants;
import yinzhi.micro_client.network.vo.YZSlideVO;
import yinzhi.micro_client.utils.barcode.CaptureActivity;
import yinzhi.micro_client.view.ImageCycleView;

public class HomeFragment extends Fragment implements OnPageChangeListener {
	private String tag = "FeaturedFragment";

	// 测试播放视频ID 保利威视
	private static String videoId = "9b55dbfec52e18a98869af498127d00e_9";

	@ViewInject(R.id.id_ad_view)
	private ImageCycleView mImageCycleView;

	private ArrayList<String> mImageUrl = new ArrayList<String>();

	private List<YZSlideVO> slides = new ArrayList<YZSlideVO>();

	@ViewInject(R.id.home_menu_imgbtn)
	private ImageButton mImageButton;

	public static HomeFragment homeFragment;

	private MainActivity activity;

	@ViewInject(R.id.home_search_imgbtn)
	private ImageButton searchBtn;

	@ViewInject(R.id.scan_imgbtn)
	private ImageButton scan;

	/**
	 * 下拉刷新
	 * 
	 * @return
	 */
	private static final int REFRESH_COMPLETE = 0X110;
	private SwipeRefreshLayout mSwipeLayout;
	private Handler rHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_COMPLETE:
				mSwipeLayout.setRefreshing(false);
				break;

			}
		};
	};

	public synchronized static HomeFragment getInstance() {
		if (homeFragment == null) {
			homeFragment = new HomeFragment();
		}
		return homeFragment;
	}

	@Override
	public void onAttach(Activity activity) {
		Log.i(tag, "onAttach");
		super.onAttach(activity);
		this.activity = (MainActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(tag, "onCreate");
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i(tag, "onCreateView");
		View rootView = inflater.inflate(R.layout.fragment_home, null);
		ViewUtils.inject(this, rootView);

		// 向服务器请求获取推荐书籍宣传图
		setAdvImageUrl();

		mSwipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.home_swipelayout);
		mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

			@Override
			public void onRefresh() {
				LogUtils.i("正在刷新...");
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						LogUtils.i("发送信息停止刷新");
						rHandler.sendEmptyMessage(REFRESH_COMPLETE);
					}
				}).start();
			}
		});
		mSwipeLayout.setColorScheme(android.R.color.holo_green_dark, android.R.color.holo_green_light,
				android.R.color.holo_orange_light, android.R.color.holo_red_light);

		return rootView;
	}

	/**
	 * 扫描二维码
	 * 
	 * @param v
	 */
	@OnClick(R.id.scan_imgbtn)
	public void scanClick(View v) {
		startActivity(new Intent(getActivity(), CaptureActivity.class));
		getActivity().overridePendingTransition(R.anim.activity_anim_left_in, 0);
	}

	/**
	 * 调出侧滑菜单
	 * 
	 * @param v
	 */
	@OnClick(R.id.home_menu_imgbtn)
	public void menuClick(View v) {
		activity.toggle();
	}

	/**
	 * 搜索按钮
	 */
	@OnClick(R.id.home_search_imgbtn)
	public void searchClick() {
		Intent intent = new Intent(getActivity(), SearchActivity.class);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.activity_anim_left_in, 0);
	}

	/**
	 * 获取轮播图片数据
	 */
	private void setAdvImageUrl() {
		// TODO 轮播图片
		mImageUrl = new ArrayList<String>();
		YZNetworkUtils.fetchSlideList(null, "mockdeviceId", new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String response = arg0.result;
				LogUtils.i(response);
				try {
					slides = YZResponseUtils.parseArray(response, YZSlideVO.class);
					for (int i = 0; i < slides.size(); i++) {
						mImageUrl.add(INetworkConstants.YZMC_SERVER + slides.get(i).getSlidePicPath());
					}

					mImageCycleView.setImageResources(mImageUrl, mImageCycleViewListener);
				} catch (Exception e) {
					Toast.makeText(getActivity(), "数据解析失败", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO 错误处理

			}
		});

	}

	private ImageCycleView.ImageCycleViewListener mImageCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
		@Override
		public void displayImage(String imageURL, ImageView imageView) {
			LogUtils.i("displayImage---------------------------------");

			BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
			bitmapUtils.display(imageView, imageURL);
		}

		@Override
		public void onImageClick(int position, View imageView) {
			// TODO 点击图片后处理事件
			Intent intent = new Intent();
			intent.putExtra("courseId", slides.get(position).getCourseId());
			intent.setClass(getActivity(), IntroductionActivity.class);
			startActivity(intent);
			// TODO 动画有问题？
			getActivity().overridePendingTransition(R.anim.activity_anim_left_in, R.anim.activity_anim_left_out);
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		mImageCycleView.startImageCycle();
	}

	@Override
	public void onPause() {
		super.onPause();
		mImageCycleView.pauseImageCycle();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mImageCycleView.pauseImageCycle();
	}

	@Override
	public void onStart() {
		Log.i(tag, "onStart");
		super.onStart();
	}

	@Override
	public void onStop() {
		Log.i(tag, "onStop");
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		Log.i(tag, "onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		Log.i(tag, "onDetach");
		super.onDetach();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub

	}

}
