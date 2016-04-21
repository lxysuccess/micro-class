package yinzhi.micro_client.activity;

import yinzhi.micro_client.R;
import yinzhi.micro_client.network.YZNetworkUtils;
import yinzhi.micro_client.network.YZResponseUtils;
import yinzhi.micro_client.network.vo.YZTipsVO;
import yinzhi.micro_client.utils.SpMessageUtil;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class TipsActivity extends Activity {

	@ViewInject(R.id.tips_content)
	private WebView content;

	@ViewInject(R.id.tips_close)
	private ImageButton close;

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
			fetchTips(resourceId);

	}

	private void initView() {

		String html = "";
		try {
			html = "<html>" + "<body>" + tip.getContent() + "</body>"
					+ "</html>";
		} catch (NullPointerException e) {
			html = "<H4>数据为空</H4>";
		}

		content.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);

		// 支持javascript
		content.getSettings().setJavaScriptEnabled(true);
//		// 设置可以支持缩放
//		content.getSettings().setSupportZoom(true);
//		// 设置出现缩放工具
//		content.getSettings().setBuiltInZoomControls(true);
//		// 扩大比例的缩放
//		content.getSettings().setUseWideViewPort(true);
		// 自适应屏幕
		content.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		content.getSettings().setLoadWithOverviewMode(true);

		content.setWebChromeClient(new WebChromeClient());

	}

	private void fetchTips(String resourceId) {
		YZNetworkUtils.fetchCourseTips(
				SpMessageUtil.getLogonToken(getApplicationContext()),
				resourceId, new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {

						String response = arg0.result;

						LogUtils.i(response + "==============");

						
						// TODO 数据校验，权限控制
//						if (!YZNetworkUtils.isAllowedContinue(
//								TipsActivity.this, response)) {
//							return;
//						}

						tip = YZResponseUtils.parseObject(response,
								YZTipsVO.class);

						tip.getContent();
						
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
