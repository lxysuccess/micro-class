package yinzhi.micro_client.activity;

import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import yinzhi.micro_client.R;
import yinzhi.micro_client.adapter.LxyCommonAdapter;
import yinzhi.micro_client.adapter.LxyViewHolder;
import yinzhi.micro_client.network.YZNetworkUtils;
import yinzhi.micro_client.network.YZResponseUtils;
import yinzhi.micro_client.network.vo.YZCommentVO;
import yinzhi.micro_client.utils.SpMessageUtil;

public class CommentActivity extends BaseActivity {

	@ViewInject(R.id.comment_list)
	private ListView listView;

	@ViewInject(R.id.comment_publish)
	private ImageButton goPublish;

	private LxyCommonAdapter<YZCommentVO> commentListAdapter;

	private List<YZCommentVO> datas;

	private String itemResourceId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		ViewUtils.inject(this);

		// itemResourceId = getIntent().getExtras().getString("itemResourceId");

		// TODO 测试
		itemResourceId = "9";

		updateData(0, 20);

	}

	private void updateData(int page, int size) {

		YZNetworkUtils.fetchCommentList(SpMessageUtil.getLogonToken(getApplicationContext()), itemResourceId, page,
				size, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {

						String response = arg0.result;

						LogUtils.i("comment---------------" + response);

						datas = YZResponseUtils.parseArray(response, YZCommentVO.class);

						if (datas != null) {
							initView();
						}

					}
				});
	}

	private void initView() {

		commentListAdapter = new LxyCommonAdapter<YZCommentVO>(this, datas, R.layout.item_comment) {

			@SuppressWarnings("deprecation")
			@Override
			public void convert(LxyViewHolder holder, YZCommentVO t) {
				holder.setText(R.id.comment_username, t.getNickname());
				holder.setText(R.id.comment_content, t.getContent());
				holder.setText(R.id.comment_date, t.getPublishDate().toGMTString());

			}
		};
		listView.setAdapter(commentListAdapter);
		commentListAdapter.notifyDataSetChanged();
	}

	@OnClick(R.id.comment_publish)
	public void goPublishClick(View v) {
		// 打开发表评论页面
		Intent intent = new Intent(this, CommentWriteActivity.class);
		intent.putExtra("itemResourceId", itemResourceId);
		startActivity(intent);

	}

}
