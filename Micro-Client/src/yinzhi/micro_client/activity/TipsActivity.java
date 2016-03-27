package yinzhi.micro_client.activity;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import yinzhi.micro_client.R;
import yinzhi.micro_client.network.YZNetworkUtils;
import yinzhi.micro_client.network.YZResponseUtils;
import yinzhi.micro_client.network.vo.YZTipsVO;

public class TipsActivity extends Activity {

	@ViewInject(R.id.tips_content)
	private WebView content;

	@ViewInject(R.id.tips_close)
	private Button close;

	/**
	 * 小贴士内容
	 */
	private YZTipsVO tip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tips);
		ViewUtils.inject(this);
		initData();
		initView();
	}

	private void initData() {
		String resourceId = getIntent().getStringExtra("itemResourceId");
		if (resourceId != null)
			fetchExercice(resourceId);

	}

	private void initView() {

		String html = "<html>" + "<body>" + tip.getContent() + "</body>" + "</html>";
		content.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
		content.getSettings().setJavaScriptEnabled(true);
		content.getSettings().setSupportZoom(true);
		content.setWebChromeClient(new WebChromeClient());

	}

	private void fetchExercice(String resourceId) {
		YZNetworkUtils.fetchCourseTips("", resourceId, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {

				String response = arg0.result;

				tip = YZResponseUtils.parseObject(response, YZTipsVO.class);

				// TODO 数据校验，权限控制
				initView();

			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 关闭
	 * 
	 * @param v
	 */
	@OnClick(R.id.tips_close)
	public void closeClick(View v) {

		TipsActivity.this.finish();
		overridePendingTransition(0, R.anim.activity_anim_down_out);
	}
}
