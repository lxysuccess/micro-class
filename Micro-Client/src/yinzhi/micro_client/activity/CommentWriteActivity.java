package yinzhi.micro_client.activity;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import yinzhi.micro_client.R;
import yinzhi.micro_client.network.YZNetworkUtils;

public class CommentWriteActivity extends BaseActivity {

	@ViewInject(R.id.write_comment_star_one)
	private ImageView starOne;
	@ViewInject(R.id.write_comment_star_two)
	private ImageView starTwo;
	@ViewInject(R.id.write_comment_star_three)
	private ImageView starThree;
	@ViewInject(R.id.write_comment_star_four)
	private ImageView starFour;
	@ViewInject(R.id.write_comment_star_five)
	private ImageView starFive;
	@ViewInject(R.id.write_comment_content)
	private EditText contentInput;
	@ViewInject(R.id.write_comment_word_count)
	private TextView wordCount;

	@ViewInject(R.id.comment_publish)
	private Button publishComment;

	// 评论字数限制
	private static final int COMMENT_CONTENT_LENGTH_LIMITED = 300;

	private Integer currentScore = 0;

	private String itemResourceId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_write);

		ViewUtils.inject(this);

		itemResourceId = getIntent().getExtras().getString("itemResourceId");

		// 初始化，统计内容字数
		String content = contentInput.getText().toString();
		wordCount.setText(content.length() + "/" + COMMENT_CONTENT_LENGTH_LIMITED);

		contentInput.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				LogUtils.i("before");
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				LogUtils.i("inaction");
				String content = contentInput.getText().toString();
				wordCount.setText(content.length() + "/" + COMMENT_CONTENT_LENGTH_LIMITED);
			}

			@Override
			public void afterTextChanged(Editable s) {
				LogUtils.i("after");
			}
		});

	}

	@OnClick(R.id.write_comment_star_one)
	public void starOneClick(View v) {
		updateStarStae(-1);
		updateStarStae(1);
	}

	@OnClick(R.id.write_comment_star_two)
	public void starTwoClick(View v) {
		updateStarStae(-1);
		updateStarStae(2);
	}

	@OnClick(R.id.write_comment_star_three)
	public void starThreelick(View v) {
		updateStarStae(-1);
		updateStarStae(3);
	}

	@OnClick(R.id.write_comment_star_four)
	public void starFourClick(View v) {
		updateStarStae(-1);
		updateStarStae(4);
	}

	@OnClick(R.id.write_comment_star_five)
	public void starFiveClick(View v) {
		updateStarStae(-1);
		updateStarStae(5);
	}

	private void updateStarStae(int score) {
		switch (score) {
		case 1:
			starOne.setImageResource(R.drawable.write_comment_star_selected);
			currentScore = 1;
			break;
		case 2:
			starOne.setImageResource(R.drawable.write_comment_star_selected);
			starTwo.setImageResource(R.drawable.write_comment_star_selected);
			currentScore = 2;
			break;
		case 3:
			starOne.setImageResource(R.drawable.write_comment_star_selected);
			starTwo.setImageResource(R.drawable.write_comment_star_selected);
			starThree.setImageResource(R.drawable.write_comment_star_selected);
			currentScore = 3;
			break;
		case 4:
			starOne.setImageResource(R.drawable.write_comment_star_selected);
			starTwo.setImageResource(R.drawable.write_comment_star_selected);
			starThree.setImageResource(R.drawable.write_comment_star_selected);
			starFour.setImageResource(R.drawable.write_comment_star_selected);
			currentScore = 4;
			break;
		case 5:
			starOne.setImageResource(R.drawable.write_comment_star_selected);
			starTwo.setImageResource(R.drawable.write_comment_star_selected);
			starThree.setImageResource(R.drawable.write_comment_star_selected);
			starFour.setImageResource(R.drawable.write_comment_star_selected);
			starFive.setImageResource(R.drawable.write_comment_star_selected);
			currentScore = 5;
			break;
		default:
			starOne.setImageResource(R.drawable.write_comment_star);
			starTwo.setImageResource(R.drawable.write_comment_star);
			starThree.setImageResource(R.drawable.write_comment_star);
			starFour.setImageResource(R.drawable.write_comment_star);
			starFive.setImageResource(R.drawable.write_comment_star);
			break;
		}
	}

	@OnClick(R.id.comment_publish)
	public void publishClick(View v) {
		YZNetworkUtils.publishComment("", contentInput.getText().toString(), new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// 根据反馈判断是否发布成功，提示用户

			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				//

			}
		});

	}

}
