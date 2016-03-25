package yinzhi.micro_client.activity;

import yinzhi.micro_client.R;
import yinzhi.micro_client.network.vo.YZExerciseVO;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class ExerciseActivity extends Activity {

	// 题目显示控件
	@ViewInject(R.id.exercise_single_question)
	private TextView question;

	@ViewInject(R.id.exercise_reference_answser)
	private TextView resultTextView;

	@ViewInject(R.id.exercise_single_answer_list)
	private ListView singleAnswerList;

	private SingleAnswerListAdapter singleAnswerListAdapter;
	@ViewInject(R.id.exercise_done)
	private Button done;

	@ViewInject(R.id.exercise_close)
	private Button close;

	@ViewInject(R.id.exercise_answer_detail_btn)
	private Button exerciseAnswerDetailBtn;

	@ViewInject(R.id.exercise_answer_keys)
	private LinearLayout keysLayout;
	
	//
	private YZExerciseVO exercise;

	// 题目题干
	private String[] questionStrings = { "下列项目不适用零税率的是",
			"( )认为会计是一种技术手段，是反映和监督生产过程的一种方法，是管理经济的一个工具。",
			"()适用于定额管理基础较好，各项消耗定额或费用定额较准确、稳定，但各月末在产品数量变动较大的产品。",
			"()的具体核算必须严格遵守财政部颁发的《企业会计准则》、《企业会计准则——应用指南》和《企业财务通则》的规定。",
			"()是一种通过所有者约束经营者的办法，所有者对经营者予以监督。", "数据恢复是指", "我国最早的复式记账法是" };
	// 正确答案
	private int correctAnswer[] = { 0, 1, 0, 1, 1, 2, 1 };
	private String[] abcdStrings = { "A", "B", "C", "D" };

	private int choice = -1;
	// 标记来源页面
	private int source = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise);
		ViewUtils.inject(this);

		
		
		source = getIntent().getIntExtra("choice", -1);

		question.setText(questionStrings[source - 1]);

		singleAnswerListAdapter = new SingleAnswerListAdapter(this);
		singleAnswerList.setAdapter(singleAnswerListAdapter);
		singleAnswerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				choice = position;
			}
		});

	}

	class SingleAnswerListAdapter extends BaseAdapter {

		private String[][] answerStrings = {
				{ "对境内不动产提供的设计服务", "提供国际运输劳务", "免向境外单位提供的设计服务",
						"境内的单位和个人提供的往返香港、澳门、台湾的交通运输服务" },
				{ "信息系统论", "管理工具论", "管理活动论", "技术论" },
				{ "定额比例法", "在产品按固定成本计价法", "在产品按定额成本计价法", "在产品按所耗原材料费用计价法" },
				{ "成本会计", "财务会计", "管理会计", "涉外会计" },
				{ "接收", "解聘", "罢免", "激励" },
				{ "软盘数据恢复到硬盘上", "硬盘数据拷贝到硬盘", "会计数据还原成原来的状态",
						"软盘或硬盘数据恢复到硬盘指定目录下" }, { "朱雀账", "龙门账", "龙潭账", "虎穴账" } };
		private LayoutInflater mInflater;

		public SingleAnswerListAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return abcdStrings.length;
		}

		@Override
		public String getItem(int position) {
			return answerStrings[source - 1][position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View converView, ViewGroup viewGroup) {
			ViewHolder holder = null;
			if (converView == null) {
				converView = mInflater.inflate(
						R.layout.exercise_answer_list_item, null);
				holder = new ViewHolder(converView);
				converView.setTag(holder);
			} else {
				holder = (ViewHolder) converView.getTag();
			}
			holder.answerTitle.setText(abcdStrings[position]);
			holder.answerContent.setText(answerStrings[source - 1][position]);
			return converView;
		}

	}

	public static class ViewHolder {
		public TextView answerTitle;
		public TextView answerContent;

		public ViewHolder(View view) {
			answerTitle = (TextView) view
					.findViewById(R.id.exercise_single_answer_title);
			answerContent = (TextView) view
					.findViewById(R.id.exercise_single_answer_content);
		}
	}

	@OnClick(R.id.exercise_done)
	public void doneClick(View v) {
		// 检查是否完成了习题，若完成开放下一个视频的播放权限
		if (choice == -1) {
			Toast.makeText(ExerciseActivity.this, "请完成习题", Toast.LENGTH_LONG)
					.show();
			return;
		}
		if (source == -1) {
			ExerciseActivity.this.finish();
			overridePendingTransition(0, R.anim.activity_anim_down_out);
		} else {
			// 做完课前测试，跳转至视频播放界面
			Intent intent = new Intent(ExerciseActivity.this,
					DetailContentActivity.class);
			intent.putExtra("choice", source);
			startActivity(intent);
			overridePendingTransition(R.anim.activity_anim_left_in, 0);
			ExerciseActivity.this.finish();

		}
	}

	@OnClick(R.id.exercise_close)
	public void close(View v) {
		ExerciseActivity.this.finish();
		overridePendingTransition(0, R.anim.activity_anim_down_out);
	}

	@OnClick(R.id.exercise_answer_detail_btn)
	public void detail(View v) {
		if (choice == -1) {
			Toast.makeText(ExerciseActivity.this, "请完成习题后查看答案！",
					Toast.LENGTH_LONG).show();
			return;
		}
		if (choice == correctAnswer[source - 1]) {
			// 回答正确
			resultTextView.setText(abcdStrings[choice] + "\n恭喜！您的答案正确！");
			resultTextView.setTextColor(Color.GREEN);
		} else {
			// 回答错误
			resultTextView.setText("您的答案错误！正确答案是\n" + abcdStrings[choice]);
			resultTextView.setTextColor(Color.RED);
		}
		if (keysLayout.getVisibility() == 8) {
			keysLayout.setVisibility(View.VISIBLE);
		} else {
			keysLayout.setVisibility(View.GONE);
		}
	}
}
