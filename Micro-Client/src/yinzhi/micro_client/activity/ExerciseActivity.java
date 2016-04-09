package yinzhi.micro_client.activity;

import java.util.ArrayList;
import java.util.List;

import yinzhi.micro_client.R;
import yinzhi.micro_client.adapter.LxyCommonAdapter;
import yinzhi.micro_client.adapter.LxyViewHolder;
import yinzhi.micro_client.network.YZNetworkUtils;
import yinzhi.micro_client.network.YZResponseUtils;
import yinzhi.micro_client.network.vo.YZExerciseVO;
import yinzhi.micro_client.utils.SpMessageUtil;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

public class ExerciseActivity extends Activity {

	// 题目显示控件
	@ViewInject(R.id.exercise_single_question)
	private TextView question;

	@ViewInject(R.id.exercise_reference_answser)
	private TextView resultTextView;

	@ViewInject(R.id.exercise_single_answer_list)
	private ListView choiceList;

	@ViewInject(R.id.exercise_done)
	private Button done;

	@ViewInject(R.id.exercise_close)
	private ImageButton close;

	@ViewInject(R.id.exercise_answer_detail_btn)
	private Button exerciseAnswerDetailBtn;

	@ViewInject(R.id.exercise_answer_keys)
	private LinearLayout keysLayout;

	@ViewInject(R.id.exercise_wait)
	private RelativeLayout wait;

	// 练习题对象
	private YZExerciseVO exercise;

	List<String> choiceMarks = new ArrayList<String>();

	// 标记用户回答情况
	private int choice = -1;
	// 标记来源页面
	private String fromActivity = "";

	// 选项listview 的adapter
	private LxyCommonAdapter<ChoiceItem> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise);
		ViewUtils.inject(this);

		initData();

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		fromActivity = getIntent().getStringExtra("fromActivity");
		if (fromActivity == null) {
			fromActivity = "";
		}

		String resourceId = getIntent().getStringExtra("itemResourceId");
		if (resourceId != null)
			fetchExercice(resourceId);
		choiceMarks.add("A");
		choiceMarks.add("B");
		choiceMarks.add("C");
		choiceMarks.add("D");

	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		// 设置题目内容
		question.setText(exercise.getTitle());

		// 设置选项内容
		List<ChoiceItem> choiceItems = new ArrayList<ChoiceItem>();
		choiceItems.add(new ChoiceItem("A", exercise.getChoiceA()));
		choiceItems.add(new ChoiceItem("B", exercise.getChoiceB()));
		choiceItems.add(new ChoiceItem("C", exercise.getChoiceC()));
		choiceItems.add(new ChoiceItem("D", exercise.getChoiceD()));

		adapter = new LxyCommonAdapter<ChoiceItem>(this, choiceItems,
				R.layout.exercise_answer_list_item) {

			@Override
			public void convert(LxyViewHolder holder, ChoiceItem t) {
				holder.setText(R.id.exercise_choice_mark, t.mark);
				holder.setText(R.id.exercise_choice_content, t.content);
			}

		};

		choiceList.setAdapter(adapter);

	}

	/**
	 * 获取练习题目数据
	 * 
	 * @param resourceId
	 */
	private void fetchExercice(String resourceId) {

		YZNetworkUtils.fetchExercise(ExerciseActivity.this,
				SpMessageUtil.getLogonToken(getApplicationContext()),
				resourceId, new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// 数据获取
						String response = arg0.result;

						LogUtils.i(response + "lianxitimu ==============");

						if (!YZNetworkUtils.isAllowedContinue(
								ExerciseActivity.this, response)) {
							wait.setVisibility(View.GONE);
							return;
						}
						exercise = YZResponseUtils.parseObject(response,
								YZExerciseVO.class);

						initView();
						wait.setVisibility(View.GONE);

					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {

					}
				});
	}

	@OnClick(R.id.exercise_done)
	public void doneClick(View v) {
		// 检查是否完成了习题，若完成开放下一个视频的播放权限
		if (choice == -1) {
			Toast.makeText(ExerciseActivity.this, "请完成习题", Toast.LENGTH_LONG)
					.show();
			return;
		}

		if (fromActivity.equals("")) {

			ExerciseActivity.this.finish();
			overridePendingTransition(0, R.anim.activity_anim_down_out);

		} else {

			// // 做完课前测试，跳转至视频播放界面
			// Intent intent = new Intent(ExerciseActivity.this,
			// DetailContentActivity.class);
			// intent.putExtra("choice", fromActivity);
			// startActivity(intent);
			// overridePendingTransition(R.anim.activity_anim_left_in, 0);
			// ExerciseActivity.this.finish();

			// TODO 视频中间跳出的练习，完成习题后继续

			ExerciseActivity.this.finish();
			overridePendingTransition(0, R.anim.activity_anim_down_out);
		}

	}

	@OnClick(R.id.exercise_close)
	public void close(View v) {
		ExerciseActivity.this.finish();
		overridePendingTransition(0, R.anim.activity_anim_down_out);
	}

	@OnClick(R.id.exercise_answer_detail_btn)
	public void analysisClick(View v) {
		if (choice == -1) {
			Toast.makeText(ExerciseActivity.this, "请完成习题后查看答案！",
					Toast.LENGTH_LONG).show();
			return;
		}

		if (choice == (choiceMarks.indexOf(exercise.getAnswer()) + 1)) {
			// 回答正确
			resultTextView.setText(exercise.getAnswer() + "\n恭喜！您的答案正确！");
			resultTextView.setTextColor(Color.GREEN);
		} else {
			// 回答错误
			resultTextView.setText("您的答案错误！正确答案是\n" + exercise.getAnswer());
			resultTextView.setTextColor(Color.RED);
		}
		if (keysLayout.getVisibility() == 8) {
			keysLayout.setVisibility(View.VISIBLE);
		} else {
			keysLayout.setVisibility(View.GONE);
		}
	}

	/**
	 * 选项list点击监听
	 * 
	 * @param v
	 */
	@OnItemClick(R.id.exercise_single_answer_list)
	public void listClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 记录用户选择的选项
		choice = position + 1;
	}

	/**
	 * 选择题的答案选项bean
	 * 
	 * @author lixuyu
	 * 
	 */
	private class ChoiceItem {

		private String mark;
		private String content;

		public ChoiceItem(String mark, String content) {
			super();
			this.mark = mark;
			this.content = content;
		}

		public String getMark() {
			return mark;
		}

		public void setMark(String mark) {
			this.mark = mark;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

	}
}
