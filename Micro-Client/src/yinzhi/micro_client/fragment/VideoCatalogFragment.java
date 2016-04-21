package yinzhi.micro_client.fragment;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import yinzhi.micro_client.R;
import yinzhi.micro_client.activity.ExerciseActivity;
import yinzhi.micro_client.activity.IntroductionActivity;
import yinzhi.micro_client.activity.LoginActivity;
import yinzhi.micro_client.activity.TipsActivity;
import yinzhi.micro_client.activity.video.IjkVideoActicity;
import yinzhi.micro_client.adapter.StickyHeaderListBaseAdapter;
import yinzhi.micro_client.network.YZNetworkUtils;
import yinzhi.micro_client.network.YZResponseUtils;
import yinzhi.micro_client.network.vo.YZCatalogVO;
import yinzhi.micro_client.network.vo.YZChapterVO;
import yinzhi.micro_client.network.vo.YZItemResourceVO;
import yinzhi.micro_client.network.vo.YZVideoVO;
import yinzhi.micro_client.utils.SpMessageUtil;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class VideoCatalogFragment extends Fragment implements
		AdapterView.OnItemClickListener,
		StickyListHeadersListView.OnHeaderClickListener,
		StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
		StickyListHeadersListView.OnStickyHeaderChangedListener,
		View.OnTouchListener {

	public static final String TAG = "VideoCatalogFragment";

	@ViewInject(R.id.video_catalog_list)
	private StickyListHeadersListView stickyList;

	@ViewInject(R.id.video_catalog_empty)
	private TextView emptyView;

	public static VideoCatalogFragment fragment;
	public IntroductionActivity introductionActivity;
	private StickyHeaderListBaseAdapter mAdapter;
	private boolean fadeHeader = true;

	// 测试播放视频ID 保利威视
	private static String videoId = "c3df59288d6ecd9eb4174822850cc858_c";

	// 存储一个课程的所有子资源的List
	private List<YZItemResourceVO> itemResources = new ArrayList<YZItemResourceVO>();

	// 记录点击位置
	public static Integer curr_pos = -1;

	/**
	 * 更新目录回调接口
	 */
	IUpdateCatalogData iUpdateCatalogData;

	public synchronized static VideoCatalogFragment newInstance() {
		if (fragment == null) {
			fragment = new VideoCatalogFragment();
		}
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		introductionActivity = (IntroductionActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_video_catalog, null);
		ViewUtils.inject(this, rootView);

		iUpdateCatalogData.onUpdateCatalog();

		return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		curr_pos = position;

		if (position < 0 || position >= itemResources.size()) {
			return;
		}
		YZItemResourceVO tempItem = itemResources.get(position);
		LogUtils.i("类型：" + tempItem.getType() + ",点击列表中的位置:" + position);

		String selectedType = tempItem.getType();

		if (introductionActivity.isSubsribe != 1) {
			Toast.makeText(getActivity(), "请先订阅！", Toast.LENGTH_SHORT).show();
			return;
		}

		switch (ResourceType.toRescourseType(selectedType)) {
		case TEXT:
			Intent intentText = new Intent(getActivity(), TipsActivity.class);
			intentText.putExtra("itemResourceId", tempItem.getItemResourceId());
			startActivity(intentText);
			getActivity().overridePendingTransition(R.anim.activity_anim_up_in,
					0);
			break;
		case EXERCISE:

			// 打开练习页面
			Intent intentExercise = new Intent(getActivity(),
					ExerciseActivity.class);
			intentExercise.putExtra("itemResourceId",
					tempItem.getItemResourceId());
			intentExercise.putExtra("fromActivity", this.TAG);
			startActivity(intentExercise);
			getActivity().overridePendingTransition(R.anim.activity_anim_up_in,
					0);

			break;

		case VIDEO:

			// 跳转至视频加载界面

			// TODO token
			final String itemResourceId = tempItem.getItemResourceId();
			YZNetworkUtils.fetchVideo(tempItem.getItemResourceId().toString(),
					SpMessageUtil.getLogonToken(getActivity()
							.getApplicationContext()),
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							String response = arg0.result;

							LogUtils.i("fetchVideo ++++++" + response);
							if (!YZNetworkUtils.isAllowedContinue(
									getActivity(), response)) {
								return;
							}

							YZVideoVO video = YZResponseUtils.parseObject(
									response, YZVideoVO.class);

							if (video != null) {

								if (video.getIsAllowed() == 1) {
//									// 测试数据
//									IjkVideoActicity.intentTo(getActivity(),
//											IjkVideoActicity.PlayMode.portrait,
//											IjkVideoActicity.PlayType.vid,
//											"c3df59288d6ecd9eb4174822850cc858_c", "9",
//											false);
									
									IjkVideoActicity.intentTo(getActivity(),
											IjkVideoActicity.PlayMode.portrait,
											IjkVideoActicity.PlayType.vid,
											"sl8da4jjbx80cb8878980c1626c51923_s", itemResourceId,
											false);
								} else {
									LoginActivity.intentTo(getActivity(),
											getActivity().getClass().getName());
								}

							}
						}
					});

			break;

		case NONE:
			break;
		default:
			break;
		}

	}

	@Override
	public void onHeaderClick(StickyListHeadersListView l, View header,
			int itemPosition, long headerId, boolean currentlySticky) {

		// itemPositoin depends on the position of subitem。
		LogUtils.i("position " + itemPosition + "Header " + headerId
				+ " currentlySticky ? " + currentlySticky);
	}

	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onStickyHeaderOffsetChanged(StickyListHeadersListView l,
			View header, int offset) {
		if (fadeHeader
				&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			header.setAlpha(1 - (offset / (float) header.getMeasuredHeight()));
		}
		LogUtils.i("onStickyHeaderOffsetChanged + offset" + offset);
	}

	@Override
	public void onStickyHeaderChanged(StickyListHeadersListView l, View header,
			int itemPosition, long headerId) {
		// Toast.makeText(this, "Sticky Header Changed to " + headerId,
		// Toast.LENGTH_SHORT).show();
		LogUtils.i("Sticky Header Changed to " + headerId);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		LogUtils.i("OnTouch works");
		v.setOnTouchListener(null);
		return false;
	}

	/**
	 * 更新目录数据显示
	 * 
	 * @param catalog
	 */
	public void updateCatalogCompleted(YZCatalogVO catalog) {
		// TODO

		try {
			mAdapter = new StickyHeaderListBaseAdapter(getActivity(), catalog);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			LogUtils.e("init StickyHeaderListBaseAdapter error  , maybe getActivity() has been destory and it is null now");
		}

		stickyList.setOnItemClickListener(this);
		stickyList.setOnStickyHeaderOffsetChangedListener(this);
		stickyList.setEmptyView(emptyView);
		stickyList.setDrawingListUnderStickyHeader(true);
		stickyList.setAreHeadersSticky(true);
		stickyList.setAdapter(mAdapter);
		stickyList.setOnTouchListener(this);

		itemResources.clear();

		List<String> ids;
		ids = new ArrayList<String>();
		try {
			// 将itemResourceId存储在一个List中
			List<YZChapterVO> chapters = catalog.getChapters();
			List<YZItemResourceVO> resources = new ArrayList<YZItemResourceVO>();
			for (YZChapterVO chapter : chapters) {
				resources = chapter.getResourceList();
				for (YZItemResourceVO resource : resources) {
					LogUtils.i(resource.toString());
					itemResources.add(resource);
					ids.add(resource.getItemResourceId());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtils.e("updateCatalogCompleted combine itemResource error");
		}

		LogUtils.i("**************updateCatalogCompleted done");

		try {
			if (IntroductionActivity.fromActivity == "CaptureActivity") {
				// IntroductionActivity的来源页是二维码扫描界面，将制定位置的资源名称标红
				if (IntroductionActivity.itemResourceId != "-1") {

					// TODO
					// stickyList.setSelection(ids.indexOf(IntroductionActivity.itemResourceId));
					// stickyList.setSelection(ids.indexOf(33));
					// mAdapter.notifyDataSetChanged();
					int index = stickyList.getFirstVisiblePosition();
					View v = stickyList.getChildAt(ids
							.indexOf(IntroductionActivity.itemResourceId));
					int top = (v == null) ? 0 : v.getTop();
					stickyList.setSelectionFromTop(index, top);

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtils.e("updateCatalogCompleted list set selection error");
		}

	}

	public void setIUpdateCatalog(IUpdateCatalogData iUpdateCatalogData) {
		this.iUpdateCatalogData = iUpdateCatalogData;
	}

	public interface IUpdateCatalogData {
		public void onUpdateCatalog();
	}

	public void playVideo(String resourceId) {

	}

	public enum ResourceType {
		TEXT, EXERCISE, VIDEO, NONE;

		public static ResourceType toRescourseType(String type) {
			try {
				return valueOf(type);
			} catch (Exception e) {
				return NONE;
			}
		}
	}
}
