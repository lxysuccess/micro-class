package yinzhi.micro_client.fragment;

import java.util.ArrayList;
import java.util.List;

import yinzhi.micro_client.R;
import yinzhi.micro_client.activity.IntroductionActivity;
import yinzhi.micro_client.adapter.LxyCommonAdapter;
import yinzhi.micro_client.adapter.LxyViewHolder;
import yinzhi.micro_client.network.YZNetworkUtils;
import yinzhi.micro_client.network.YZResponseUtils;
import yinzhi.micro_client.network.constants.INetworkConstants;
import yinzhi.micro_client.network.vo.YZCourseVO;
import yinzhi.micro_client.utils.SpMessageUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

public class SubRankingFragment extends Fragment implements
		SwipeRefreshLayout.OnRefreshListener {

	@ViewInject(R.id.ranking_list_result)
	private ListView listView;

	@ViewInject(R.id.ranking_list_swipelayout)
	private SwipeRefreshLayout mSwipeLayout;

	// 所有课程页课程列表
	private List<YZCourseVO> datas = new ArrayList<YZCourseVO>();

	// 列表适配器
	private LxyCommonAdapter<YZCourseVO> adapter;

	// private static SubRankingFragment rankingFragment;

	/**
	 * 排行榜列表类型，0：免费排行，1：畅销排行
	 */
	private Integer rankingType = 0;

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

	public SubRankingFragment(int type) {
		rankingType = type;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_sub_ranking, null);

		ViewUtils.inject(this, rootView);
		initView();

		mSwipeLayout.setOnRefreshListener(this);
		onRefresh();

		mSwipeLayout.setColorScheme(android.R.color.holo_green_dark,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		return rootView;
	}

	/**
	 * 获取所有的分类信息
	 */
	private void initData(int type, int page, int size) {
		String logonToken = SpMessageUtil.getLogonToken(getActivity()
				.getApplicationContext());

		switch (type) {
		case 0:

			// 获取免费课程数据
			YZNetworkUtils.fetchFreeRankingList(logonToken, "", page, size,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							String response = arg0.result;

							updateDatas(response);

						}

					});
			break;
		case 1:

			// 获取畅销排行榜数据
			YZNetworkUtils.fetchChargeRankingList(logonToken, "", page, size,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							String response = arg0.result;

							updateDatas(response);

						}
					});
			break;

		default:

			// 获取免费课程数据
			YZNetworkUtils.fetchFreeRankingList(logonToken, "", page, size,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							String response = arg0.result;

							updateDatas(response);

						}

					});
			break;
		}

	}

	/**
	 * 更新列表数据
	 * 
	 * @param response
	 */
	private void updateDatas(String response) {
		datas.clear();

		if (!YZNetworkUtils.isAllowedContinue(getActivity(), response)) {
			return;
		}

		datas.addAll(YZResponseUtils.parseArray(response, YZCourseVO.class));

		adapter.notifyDataSetChanged();

		rHandler.sendEmptyMessage(REFRESH_COMPLETE);
	}

	/**
	 * 更新View
	 */
	public void initView() {
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
					if (rankingType == 1) {
						// 如果是畅销排行，显示价格
						holder.getView(R.id.course_price).setVisibility(
								View.VISIBLE);
						holder.setText(R.id.course_price, course.getPrice());
					} else {
						holder.getView(R.id.course_price).setVisibility(
								View.GONE);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					LogUtils.e("show  suRanking list type is -->" + rankingType
							+ "!error");
				}

			}
		};
		listView.setAdapter(adapter);
	}

	@OnItemClick(R.id.ranking_list_result)
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		LogUtils.i("position------>" + position);

		try {
			Intent intent = new Intent(getActivity(),
					IntroductionActivity.class);
			intent.putExtra("courseId", datas.get(position).getCourseId());
			startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtils.e("intent to introductionactivity error,  suRanking list type is -->"
					+ rankingType);
		}
		getActivity()
				.overridePendingTransition(R.anim.activity_anim_left_in, 0);
	}

	@Override
	public void onRefresh() {
		initData(rankingType, 0, 20);

	}

}