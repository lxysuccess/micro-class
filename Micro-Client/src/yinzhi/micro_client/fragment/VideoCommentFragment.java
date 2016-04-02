package yinzhi.micro_client.fragment;

import java.util.List;

import yinzhi.micro_client.R;
import yinzhi.micro_client.activity.CommentActivity;
import yinzhi.micro_client.adapter.LxyCommonAdapter;
import yinzhi.micro_client.adapter.LxyViewHolder;
import yinzhi.micro_client.network.YZNetworkUtils;
import yinzhi.micro_client.network.YZResponseUtils;
import yinzhi.micro_client.network.vo.YZCommentVO;
import yinzhi.micro_client.utils.SpMessageUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class VideoCommentFragment extends Fragment {

	@ViewInject(R.id.video_comment_list)
	private ListView listView;

	@ViewInject(R.id.video_comment_view_list)
	private Button viewComment;

	private String itemResourceId;

	private LxyCommonAdapter<YZCommentVO> commentListAdapter;

	private List<YZCommentVO> datas;

	public VideoCommentFragment(String itemResourceId) {
		// TODO Auto-generated constructor stub
		this.itemResourceId = itemResourceId;

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_video_comment_list,
				null);
		ViewUtils.inject(this, rootView);
		updateData(0, 10);
		return rootView;
	}

	private void updateData(int page, int size) {

		YZNetworkUtils.fetchCommentList(SpMessageUtil
				.getLogonToken(getActivity().getApplicationContext()),
				itemResourceId, page, size, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {

						String response = arg0.result;

						LogUtils.i("comment---------------" + response);

						if (!YZNetworkUtils.isAllowedContinue(getActivity(),
								response)) {
							return;
						}

						datas = YZResponseUtils.parseArray(response,
								YZCommentVO.class);

						if (datas != null) {
							initView();
						}

					}
				});
	}

	private void initView() {

		commentListAdapter = new LxyCommonAdapter<YZCommentVO>(getActivity(),
				datas, R.layout.item_comment) {

			@SuppressWarnings("deprecation")
			@Override
			public void convert(LxyViewHolder holder, YZCommentVO t) {
				holder.setText(R.id.comment_username, t.getNickname());
				holder.setText(R.id.comment_content, t.getContent());
				holder.setText(R.id.comment_date, t.getPublishDate()
						.toGMTString());

			}
		};
		listView.setAdapter(commentListAdapter);
		commentListAdapter.notifyDataSetChanged();
	}

	@OnClick(R.id.video_comment_view_list)
	public void viewListClick(View v) {
		Intent intent = new Intent(getActivity(), CommentActivity.class);
		intent.putExtra("itemResourceId", itemResourceId);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.activity_anim_left_in, 0);
	}
}
