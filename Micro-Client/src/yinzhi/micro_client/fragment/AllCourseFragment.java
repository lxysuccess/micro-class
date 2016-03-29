package yinzhi.micro_client.fragment;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import yinzhi.micro_client.R;
import yinzhi.micro_client.activity.CourseListActivity;
import yinzhi.micro_client.activity.MainActivity;
import yinzhi.micro_client.activity.SearchActivity;
import yinzhi.micro_client.adapter.LxyCommonAdapter;
import yinzhi.micro_client.adapter.LxyViewHolder;
import yinzhi.micro_client.network.YZNetworkUtils;
import yinzhi.micro_client.network.YZResponseUtils;
import yinzhi.micro_client.network.constants.INetworkConstants;
import yinzhi.micro_client.network.vo.YZClassifyVO;

public class AllCourseFragment extends Fragment {

	@ViewInject(R.id.all_search_imgbtn)
	private ImageButton searchBtn;

	@ViewInject(R.id.all_course_menu_imgbtn)
	private ImageButton mImageButton;

	@ViewInject(R.id.all_course_listview)
	private ListView listView;

	@ViewInject(R.id.all_course_count)
	private TextView courseCount;

	// 所有课程页课程列表
	private List<YZClassifyVO> datas = new ArrayList<YZClassifyVO>();

	// 列表适配器
	private LxyCommonAdapter<YZClassifyVO> adapter;

	private static AllCourseFragment allCourseFragment;

	private MainActivity activity;

	public synchronized static AllCourseFragment getInstance() {
		if (allCourseFragment == null) {
			allCourseFragment = new AllCourseFragment();
		}
		return allCourseFragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (MainActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// !!!!!!!!!!!!!!!!!!!!!!!!!
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_all_course, null);

		ViewUtils.inject(this, rootView);

		initData();

		return rootView;
	}

	/**
	 * 获取所有的分类信息
	 */
	private void initData() {
		YZNetworkUtils.fetchClassifyList(new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String response = arg0.result;
				LogUtils.i(response);

				datas = YZResponseUtils.parseArray(response, YZClassifyVO.class);
				initView();
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 更新View
	 */
	public void initView() {
		adapter = new LxyCommonAdapter<YZClassifyVO>(getActivity(), datas, R.layout.item_classify_list) {

			@Override
			public void convert(LxyViewHolder holder, YZClassifyVO t) {
				holder.setImageViewUrl(R.id.classify_icon, INetworkConstants.YZMC_SERVER + t.getClassifyPicPath());
				holder.setText(R.id.classify_name, t.getTitle());
				holder.setText(R.id.classify_intro, t.getIntroduction());

			}
		};
		listView.setAdapter(adapter);
	}

	@OnClick(R.id.all_search_imgbtn)
	public void searchClick(View v) {
		Intent intent = new Intent(getActivity(), SearchActivity.class);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.activity_anim_left_in, 0);
	}

	@OnClick(R.id.all_course_menu_imgbtn)
	public void toggleClick(View v) {
		activity.toggle();
	}

	@OnItemClick(R.id.all_course_listview)
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		LogUtils.i("position------>" + position);

		Intent intent = new Intent(getActivity(), CourseListActivity.class);
		intent.putExtra("classifyId", datas.get(position).getId());
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.activity_anim_left_in, 0);
	}

}
