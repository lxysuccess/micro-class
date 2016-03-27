package yinzhi.micro_client.fragment;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

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
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import yinzhi.micro_client.R;
import yinzhi.micro_client.activity.ExerciseActivity;
import yinzhi.micro_client.activity.IntroductionActivity;
import yinzhi.micro_client.activity.video.IjkVideoActicity;
import yinzhi.micro_client.adapter.StickyHeaderListBaseAdapter;
import yinzhi.micro_client.network.YZNetworkUtils;
import yinzhi.micro_client.network.vo.YZCatalogVO;
import yinzhi.micro_client.network.vo.YZChapterVO;
import yinzhi.micro_client.network.vo.YZItemResourceVO;

public class VideoCatalogFragment extends Fragment implements AdapterView.OnItemClickListener,
		StickyListHeadersListView.OnHeaderClickListener, StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
		StickyListHeadersListView.OnStickyHeaderChangedListener, View.OnTouchListener {

	public static final String TAG = "VideoCatalogFragment";
	
	public static VideoCatalogFragment fragment;
	public IntroductionActivity introductionActivity;
	private StickyHeaderListBaseAdapter mAdapter;
	private boolean fadeHeader = true;

	// 测试播放视频ID 保利威视
	private static String videoId = "9b55dbfec52e18a98869af498127d00e_9";

	// 存储一个课程的所有子资源的List
	private List<YZItemResourceVO> itemResources = new ArrayList<YZItemResourceVO>();

	/**
	 * 更新目录回调接口
	 */
	IUpdateCatalogData iUpdateCatalogData;

	@ViewInject(R.id.video_catalog_list)
	private StickyListHeadersListView stickyList;

	@ViewInject(R.id.video_catalog_empty)
	private TextView emptyView;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_video_catalog, null);
		ViewUtils.inject(this, rootView);

		iUpdateCatalogData.onUpdateCatalog();

		return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		YZItemResourceVO tempItem = itemResources.get(position);
		LogUtils.i("类型：" + tempItem.getType() + ",点击列表中的位置:" + position);

		String selectedType = tempItem.getType();

		switch (ResourceType.toRescourseType(selectedType)) {
		case TEXT:
			Intent intentText = new Intent(getActivity(), ExerciseActivity.class);
			intentText.putExtra("itemResourceId", tempItem.getItemResourceId());
			startActivity(intentText);
			getActivity().overridePendingTransition(R.anim.activity_anim_up_in, 0);
			break;
		case EXERCISE:

			// 打开练习页面
			Intent intentExercise = new Intent(getActivity(), ExerciseActivity.class);
			intentExercise.putExtra("itemResourceId", tempItem.getItemResourceId());
			intentExercise.putExtra("fromActivity", this.TAG);
			startActivity(intentExercise);
			getActivity().overridePendingTransition(R.anim.activity_anim_up_in, 0);

			break;

		case VIDEO:

			// 跳转至视频加载界面
			playVideo(tempItem.getItemResourceId().toString());

			break;

		default:
			break;
		}

	}

	@Override
	public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId,
			boolean currentlySticky) {

		// itemPositoin depends on the position of subitem。
		LogUtils.i("position " + itemPosition + "Header " + headerId + " currentlySticky ? " + currentlySticky);
	}

	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onStickyHeaderOffsetChanged(StickyListHeadersListView l, View header, int offset) {
		if (fadeHeader && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			header.setAlpha(1 - (offset / (float) header.getMeasuredHeight()));
		}
		LogUtils.i("onStickyHeaderOffsetChanged + offset" + offset);
	}

	@Override
	public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {
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

		mAdapter = new StickyHeaderListBaseAdapter(getActivity(), catalog);

		stickyList.setOnItemClickListener(this);
		stickyList.setOnHeaderClickListener(this);
		stickyList.setOnStickyHeaderChangedListener(this);
		stickyList.setOnStickyHeaderOffsetChangedListener(this);
		stickyList.addHeaderView(getActivity().getLayoutInflater().inflate(R.layout.list_header, null));
		stickyList.addFooterView(getActivity().getLayoutInflater().inflate(R.layout.list_footer, null));
		stickyList.setEmptyView(emptyView);
		stickyList.setDrawingListUnderStickyHeader(true);
		stickyList.setAreHeadersSticky(true);
		stickyList.setAdapter(mAdapter);

		stickyList.setOnTouchListener(this);

		// 将itemResourceId存储在一个List中
		List<YZChapterVO> chapters = catalog.getChapters();
		List<YZItemResourceVO> resources = new ArrayList<YZItemResourceVO>();
		for (YZChapterVO chapter : chapters) {
			resources = chapter.getResourceList();
			for (YZItemResourceVO resource : resources) {
				itemResources.add(resource);
			}
		}

		LogUtils.i("**************updateCatalogCompleted done");

	}

	public void setIUpdateCatalog(IUpdateCatalogData iUpdateCatalogData) {
		this.iUpdateCatalogData = iUpdateCatalogData;
	}

	public interface IUpdateCatalogData {
		public void onUpdateCatalog();
	}

	private void playVideo(String resourceId) {

		// TODO token
		YZNetworkUtils.fetchVideo(resourceId, "", new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String response = arg0.result;

				// YZVideoVO video =
				// YZResponseUtils.parseObject(response,
				// YZVideoVO.class);
				//
				// if (video != null) {
				// 测试数据
				IjkVideoActicity.intentTo(getActivity(), IjkVideoActicity.PlayMode.portrait,
						IjkVideoActicity.PlayType.vid, videoId, false);
				// }
			}
		});
	}

	public enum ResourceType {
		TEXT, EXERCISE, VIDEO;

		public static ResourceType toRescourseType(String type) {
			try {
				return valueOf(type);
			} catch (Exception e) {
				return VIDEO;
			}
		}
	}
}
