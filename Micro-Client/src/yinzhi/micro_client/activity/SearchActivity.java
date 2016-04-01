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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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

public class SearchActivity extends Activity {
	@ViewInject(R.id.search_input_et)
	private EditText searchInput;

	@ViewInject(R.id.search_cancel)
	private TextView cancel;

	// 搜索结果列表
	@ViewInject(R.id.search_result)
	private ListView resultList;

	// 存储搜索结果
	private List<YZCourseVO> resultDatas = new ArrayList<YZCourseVO>();

	private LxyCommonAdapter<YZCourseVO> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		ViewUtils.inject(this);

		refreshView();

	}

	/**
	 * 刷新界面视图
	 */
	private void refreshView() {
		// 监听用户在搜索框中的输入情况
		searchInput.addTextChangedListener(textWatcher);

		// 响应回车键
		searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH
						|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
					// TODO 搜索
					Toast.makeText(getApplicationContext(), "正在搜索", Toast.LENGTH_SHORT).show();
					return true;
				}
				return false;
			}
		});

		adapter = new LxyCommonAdapter<YZCourseVO>(this, resultDatas, R.layout.item_course_list) {
			@Override
			public void convert(LxyViewHolder holder, YZCourseVO course) {

				holder.setImageViewUrl(R.id.course_icon, INetworkConstants.YZMC_SERVER + course.getCoursePicPath());
				holder.setText(R.id.course_name, course.getTitle());
				holder.setText(R.id.course_click_count, course.getClickCount().toString());
				holder.setText(R.id.course_teacher_name, course.getTeacherName());

			}
		};
		resultList.setAdapter(adapter);

	}

	private TextWatcher textWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			LogUtils.i("beforeTextChanged");

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			LogUtils.i("onTextChanged");

		}

		@Override
		public void afterTextChanged(Editable s) {
			LogUtils.i("afterTextChanged");
			if (searchInput.getText().toString().equals(null) || searchInput.getText().toString().equals("")) {
				resultList.setVisibility(View.GONE);
			} else {

				// 根据关键词请求搜索数据

				YZNetworkUtils.courseSearch(searchInput.getText().toString(), "", 0, 10, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						String response = arg0.result;

						LogUtils.i(response);

						if (JSON.parseObject(JSON.parseObject(response).get("data").toString()).get("status")
								.equals("0")) {
							Toast.makeText(getApplicationContext(), JSON.parseObject(response).get("msg").toString(),
									Toast.LENGTH_LONG).show();
							return;
						}
						List<YZCourseVO> courses = null;

						try {

							courses = YZResponseUtils.parseArray(response, YZCourseVO.class);
						} catch (Exception e) {
							// TODO: handle exception
						}

						if (courses == null) {
							Toast.makeText(getApplicationContext(), "未检索到相关课程", Toast.LENGTH_LONG).show();
							return;
						}

						// 清除上次搜索结果
						resultDatas.clear();
						resultDatas.addAll(courses);

						// 通知列表数据变化
						adapter.notifyDataSetChanged();

					}
				});

				resultList.setVisibility(View.VISIBLE);
			}

		}
	};

	@OnClick(R.id.search_cancel)
	public void cancelClick(View v) {
		searchInput.clearFocus();
		View view = getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
		this.finish();
		overridePendingTransition(0, R.anim.activity_anim_left_out);
	}

}
