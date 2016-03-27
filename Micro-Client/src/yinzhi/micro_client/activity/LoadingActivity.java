package yinzhi.micro_client.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import yinzhi.micro_client.R;
import yinzhi.micro_client.utils.UIUtils;
import yinzhi.micro_client.view.LoadingPager;
import yinzhi.micro_client.view.LoadingPager.LoadResult;

public abstract class LoadingActivity extends Activity {
	public LoadingPager loadingPage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		loadingPage = new LoadingPager(UIUtils.getContext(), R.layout.loadpage_loading, R.layout.loadpage_error,
				R.layout.loadpage_empty) {
			@Override
			protected LoadResult load() {
				return LoadingActivity.this.load();
			}

			@Override
			protected View createSuccessView() {
				return LoadingActivity.this.createSuccessView();
			}
		};
		// 可以点击
		loadingPage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadingPage.show();
			}
		});
		// 显示 loading的页面
		loadingPage.show();
		setContentView(loadingPage);
	}

	/**
	 * 刷新页面工程
	 * 
	 * @return
	 */
	protected abstract View createSuccessView();

	/**
	 * 请求服务器 获取当前状态
	 * 
	 */
	protected abstract LoadResult load();

}