package yinzhi.micro_client.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yinzhi.micro_client.R;
import yinzhi.micro_client.network.YZNetworkUtils;
import yinzhi.micro_client.network.YZResponseUtils;
import yinzhi.micro_client.network.vo.YZUserVO;
import yinzhi.micro_client.utils.SpMessageUtil;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class RegisterActivity extends BaseActivity {

	@ViewInject(R.id.register_close)
	private ImageButton register;

	@ViewInject(R.id.register_username)
	private EditText inputName;

	@ViewInject(R.id.register_password)
	private EditText inputPwd;

	@ViewInject(R.id.register_done)
	private Button doneBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		ViewUtils.inject(this);
	}

	@OnClick(R.id.register_close)
	public void registerClick(View v) {
		this.finish();
		overridePendingTransition(0, R.anim.activity_anim_left_out);
	}

	@OnClick(R.id.register_done)
	public void doneClick(View v) {

		// 验证用户输入参数的正确性
		String msg = isParamsCorrect();
		if (!"YES".equals(msg)) {
			Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
			return;
		}

		YZNetworkUtils.doRegister(inputName.getText().toString(), inputPwd.getText().toString(), null,
				"YZ" + (int) (Math.random() * 1000000), new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(getApplicationContext(), "register error", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {

						String response = arg0.result;
						LogUtils.i("" + response);

						YZUserVO user = YZResponseUtils.parseObject(response, YZUserVO.class);

						try {
							if (user.getStatus() != 1) {
								Toast.makeText(getApplicationContext(), user.getMsg(), Toast.LENGTH_LONG).show();
								return;
							}
						} catch (NullPointerException e) {
							Toast.makeText(getApplicationContext(), "返回数据错误", Toast.LENGTH_LONG).show();
							e.printStackTrace();
						}
						
						Integer result = SpMessageUtil.storeYZUserVO(user, getApplicationContext());

						if (result != 1) {
							Toast.makeText(getApplicationContext(), "服务器忙(RA)", Toast.LENGTH_LONG).show();
							return;
						}

						// 跳往首页
						Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);

						finish();
					}
				});
	}

	@SuppressLint("NewApi")
	private String isParamsCorrect() {
		String msg = "YES";

		if (inputName.getText().toString().isEmpty()) {
			msg = "用户名不能为空";
			return msg;
		}

		if (!isEmailAddress(inputName.getText().toString())) {
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
		Pattern pattern = Pattern.compile(
				"^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(email);
		// return matcher.matches();
		// TODO
		return true;
	}

}
