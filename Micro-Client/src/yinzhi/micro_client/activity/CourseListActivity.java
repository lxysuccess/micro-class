package yinzhi.micro_client.activity;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import yinzhi.micro_client.R;
import yinzhi.micro_client.adapter.LxyCommonAdapter;
import yinzhi.micro_client.adapter.LxyViewHolder;
import yinzhi.micro_client.network.YZNetworkUtils;
import yinzhi.micro_client.network.YZResponseUtils;
import yinzhi.micro_client.network.constants.INetworkConstants;
import yinzhi.micro_client.network.vo.YZCourseVO;

public class CourseListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
	@ViewInject(R.id.course_list_close)
	private ImageButton close;

	@ViewInject(R.id.course_list_title)
	private TextView title;

	@ViewInject(R.id.course_list_result)
	private ListView listView;

	@ViewInject(R.id.course_list_swipelayout)
	private SwipeRefreshLayout mSwipeLayout;

	// 存储搜索结果
	private List<YZCourseVO> datas = new ArrayList<YZCourseVO>();

	private LxyCommonAdapter<YZCourseVO> adapter;

	/**
	 * 分类ID
	 */
	private String classifyId;

	/**
	 * 分类名称
	 */
	private String classifyName;

	/**
	 * 页码
	 */
	private Integer currentPage = 0;

	/**
	 * 一页的数量
	 */
	private Integer size = 20;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_list);

		ViewUtils.inject(this);

		mSwipeLayout.setOnRefreshListener(this);
		onRefresh();

		mSwipeLayout.setColorScheme(android.R.color.holo_green_dark, android.R.color.holo_green_light,
				android.R.color.holo_orange_light, android.R.color.holo_red_light);

	}

	private void initData(int page, int size) {
		classifyId = getIntent().getExtras().getString("classifyId");
		classifyName = getIntent().getExtras().getString("classifyName", "默认");
		title.setText(classifyName);

		YZNetworkUtils.fetchListByClassify(classifyId, page, size, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String response = arg0.result;

				LogUtils.i(response);

				if(!YZNetworkUtils.isAllowedContinue(CourseListActivity.this, response)){
					return;
				}

				List<YZCourseVO> courses = YZResponseUtils.parseArray(response, YZCourseVO.class);

				if (courses == null) {
					Toast.makeText(getApplicationContext(), "未检索到相关课程", Toast.LENGTH_LONG).show();
					return;
				}

				datas.clear();

				datas.addAll(courses);
				if (currentPage == 0) {

					// 请求第一页，故应初始界面
					initView();
				}

				// 通知列表数据变化
				adapter.notifyDataSetChanged();

				rHandler.sendEmptyMessage(REFRESH_COMPLETE);

			}
		});

	}

	public void initView() {
		adapter = new LxyCommonAdapter<YZCourseVO>(this, datas, R.layout.item_course_list) {
			@Override
			public void convert(LxyViewHolder holder, YZCourseVO course) {
				try {
					holder.setImageViewUrl(R.id.course_icon, INetworkConstants.YZMC_SERVER + course.getCoursePicPath());
					holder.setText(R.id.course_name, course.getTitle());
					holder.setText(R.id.course_click_count, course.getClickCount().toString());
					holder.setText(R.id.course_teacher_name, course.getTeacherName());
				} catch (Exception e) {
					e.printStackTrace();
					LogUtils.e("show  course list by classify error");
				}
			}
		};
		listView.setAdapter(adapter);
	}

	@OnClick(R.id.course_list_close)
	public void closeClick(View v) {
		this.finish();
		overridePendingTransition(0, R.anim.activity_anim_left_out);
	}

	@Override
	public void onRefresh() {
		initData(currentPage, size);
	}

	@OnItemClick(R.id.course_list_result)
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		LogUtils.i("position------>" + position);

		try {
			Intent intent = new Intent(this, IntroductionActivity.class);
			intent.putExtra("courseId", datas.get(position).getCourseId());
			startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtils.e("intent to introductionactivity error,  classify title is -->" + classifyName);
		}
		overridePendingTransition(R.anim.activity_anim_left_in, 0);
	}

}
