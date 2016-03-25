package yinzhi.micro_client.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yinzhi.micro_client.R;
import yinzhi.micro_client.network.YZNetworkUtils;
import yinzhi.micro_client.network.YZResponseUtils;
import yinzhi.micro_client.network.vo.YZUserVO;
import yinzhi.micro_client.utils.SpMessageUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class LoginActivity extends Activity {

	@ViewInject(R.id.login_register)
	private TextView register;

	@ViewInject(R.id.login_close)
	private ImageButton close;

	@ViewInject(R.id.login_do)
	private Button loginDone;

	@ViewInject(R.id.login_username)
	private EditText inputUsername;

	@ViewInject(R.id.login_password)
	private EditText inputPwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ViewUtils.inject(this);
	}

	@OnClick(R.id.login_register)
	public void registerClick(View v) {
		Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.activity_anim_left_in, 0);
	}

	@OnClick(R.id.login_close)
	public void closeClick(View v) {
		this.finish();
		overridePendingTransition(0, R.anim.activity_anim_down_out);
	}

	@OnClick(R.id.login_do)
	public void doneClick(View v) {

		// 验证用户输入参数的正确性
		String msg = isParamsCorrect();
		if (!"YES".equals(msg)) {
			Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
			return;
		}
		
		YZNetworkUtils.doLogin(null, inputUsername.getText().toString(), inputPwd.getText().toString(), new RequestCallBack<String>() {
			
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String response = arg0.result;
				LogUtils.i("" + response);

				YZUserVO user = YZResponseUtils.parseObject(response,
						YZUserVO.class);
				if (user.getStatus() != 1) {
					Toast.makeText(getApplicationContext(),
							user.getMsg(), Toast.LENGTH_LONG).show();
					return;
				}
				Integer result = SpMessageUtil.storeYZUserVO(user,
						getApplicationContext());

				if (result != 1) {
					Toast.makeText(getApplicationContext(), "服务器忙(LA)",
							Toast.LENGTH_LONG).show();
					return;
				}

				finish();
				
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}
		});
	}

	@SuppressLint("NewApi")
	private String isParamsCorrect() {
		String msg = "YES";

		if (inputUsername.getText().toString().isEmpty()) {
			msg = "用户名不能为空";
			return msg;
		}

		if (!isEmailAddress(inputUsername.getText().toString())) {
			msg = "请输入正确邮箱";
			return msg;
		}

		if (inputPwd.getText().toString().isEmpty()) {
			msg = "请输入大于6位的密码";
			return msg;
		}

		if (inputPwd.getText().toString().length() < 6) {
			msg = "请输入大于6位的密码";
			return msg;
		}

		return msg;
	}

	public Boolean isEmailAddress(String email) {
		Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(email);
//		return matcher.matches();
		//TODO
		return true;
	}
}
