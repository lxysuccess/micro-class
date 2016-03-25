package yinzhi.micro_client.fragment;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import yinzhi.micro_client.activity.IntroductionActivity;
import yinzhi.micro_client.activity.video.IjkVideoActicity;
import yinzhi.micro_client.adapter.StickyHeaderListBaseAdapter;
import yinzhi.micro_client.network.vo.YZCatalogVO;
import yinzhi.micro_client.R;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class VideoCatalogFragment extends Fragment implements
		AdapterView.OnItemClickListener,
		StickyListHeadersListView.OnHeaderClickListener,
		StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
		StickyListHeadersListView.OnStickyHeaderChangedListener,
		View.OnTouchListener {

	public static VideoCatalogFragment fragment;
	private IntroductionActivity introductionActivity;
	private StickyHeaderListBaseAdapter mAdapter;
	private boolean fadeHeader = true;
	
	//测试播放视频ID 保利威视
	private static String videoId = "9b55dbfec52e18a98869af498127d00e_9";

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
		
		IjkVideoActicity.intentTo(getActivity(), IjkVideoActicity.PlayMode.portrait, IjkVideoActicity.PlayType.vid, videoId, false);
		
	}

	@Override
	public void onHeaderClick(StickyListHeadersListView l, View header,
			int itemPosition, long headerId, boolean currentlySticky) {
		
		//itemPositoin depends on the position of subitem。
		LogUtils.i("position "+itemPosition+"Header " + headerId + " currentlySticky ? "
				+ currentlySticky);
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
		//TODO 
		
		mAdapter = new StickyHeaderListBaseAdapter(getActivity(),catalog);

		stickyList.setOnItemClickListener(this);
		stickyList.setOnHeaderClickListener(this);
		stickyList.setOnStickyHeaderChangedListener(this);
		stickyList.setOnStickyHeaderOffsetChangedListener(this);
		stickyList.addHeaderView(getActivity().getLayoutInflater().inflate(
				R.layout.list_header, null));
		stickyList.addFooterView(getActivity().getLayoutInflater().inflate(R.layout.list_footer, null));
		stickyList.setEmptyView(emptyView);
		stickyList.setDrawingListUnderStickyHeader(true);
		stickyList.setAreHeadersSticky(true);
		stickyList.setAdapter(mAdapter);

		stickyList.setOnTouchListener(this);
		
		LogUtils.i("**************updateCatalogCompleted done");
		
	}

	public void setIUpdateCatalog(IUpdateCatalogData iUpdateCatalogData) {
		this.iUpdateCatalogData = iUpdateCatalogData;
	}

	public interface IUpdateCatalogData {
		public void onUpdateCatalog();
	}

}
