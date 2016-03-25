package yinzhi.micro_client.activity;

import yinzhi.micro_client.R;
import yinzhi.micro_client.view.SwipeBackLayout;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;

/**
 * @ClassName: BaseActivity
 * @Description:
 * @author LIXUYU
 */
public class BaseActivity extends FragmentActivity {
	public SwipeBackLayout swipeBackLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.activity_anim_left_in, 0);

		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		getWindow().getDecorView().setBackgroundDrawable(null);
		swipeBackLayout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
				R.layout.swipebackbase, null);
		swipeBackLayout.attachToActivity(this);
	}

	public void setSwipeBackEnable(boolean enable) {
		swipeBackLayout.setEnableGesture(enable);
	}

	/**
	 * ���ؼ���ɴ˷���
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(0, R.anim.activity_anim_left_out);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.activity_anim_left_out);
	}
}
