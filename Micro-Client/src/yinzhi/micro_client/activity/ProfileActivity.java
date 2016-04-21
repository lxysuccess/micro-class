package yinzhi.micro_client.activity;

import yinzhi.micro_client.R;
import yinzhi.micro_client.network.YZNetworkUtils;
import yinzhi.micro_client.network.vo.YZUserVO;
import yinzhi.micro_client.utils.SpMessageUtil;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class ProfileActivity extends BaseActivity {

	private PopupWindow popupWindow = null;

	@ViewInject(R.id.profile_close)
	private ImageButton close;
	@ViewInject(R.id.profile_save)
	private Button save;

	@ViewInject(R.id.profile_mail)
	private TextView mailTextView;

	@ViewInject(R.id.profile_nick)
	private EditText nickEditText;
	@ViewInject(R.id.profile_nick_layout)
	private RelativeLayout nickLayout;

	@ViewInject(R.id.profile_classes)
	private TextView classes;
	@ViewInject(R.id.profile_grade_layout)
	private RelativeLayout classesLayout;

	@ViewInject(R.id.profile_grade)
	private TextView grade;
	@ViewInject(R.id.profile_grade_layout)
	private RelativeLayout gradeLayout;

	@ViewInject(R.id.profile_college)
	private TextView collegeTextView;
	@ViewInject(R.id.profile_college_layout)
	private RelativeLayout collegeLayout;

	@ViewInject(R.id.profile_school)
	private TextView schoolTextView;
	@ViewInject(R.id.profile_school_layout)
	private RelativeLayout schoolLayout;

	@ViewInject(R.id.profile_layout)
	private RelativeLayout parentLayout;

	private YZUserVO userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		ViewUtils.inject(this);

		initData();

		initView();
	}

	/**
	 * 获取在内存中的用户数据
	 */
	private void initData() {
		userInfo = SpMessageUtil.getYZUserVO(getApplicationContext());
	}

	private void initView() {
		try {
			mailTextView.setText(userInfo.getUsername());
			nickEditText.setText(userInfo.getNickname());

			LogUtils.i(userInfo.getNickname() + "----------->nickname");
			classes.setText(userInfo.getClasses());
			grade.setText(userInfo.getGrade());
			collegeTextView.setText(userInfo.getCollege());
			schoolTextView.setText(userInfo.getSchool());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	 * @param v
	 */
	@OnClick(R.id.profile_close)
	public void closeClick(View v) {

		// TODO 若昵称已修改，提示未保存

		String nickname = nickEditText.getText().toString();

		if (!SpMessageUtil.getYZUserVO(getApplicationContext()).getNickname()
				.equals(nickname)) {
			dialog();

		} else {
			ProfileActivity.this.finish();
			overridePendingTransition(0, R.anim.activity_anim_left_out);
		}

	}

	private void dialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder build = new Builder(ProfileActivity.this);
		build.setMessage("未保存，确定退出？");
		build.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				ProfileActivity.this.finish();
				overridePendingTransition(0, R.anim.activity_anim_left_out);

			}
		});
		build.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});

		build.create().show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			dialog();
		}

		return false;
	}

	/**
	 * 保存资料修改
	 * 
	 * @param v
	 */
	@OnClick(R.id.profile_save)
	public void saveClick(View v) {

		String nickname = nickEditText.getText().toString();

		if (nickname.length() == 0 || nickname == null) {
			Toast.makeText(ProfileActivity.this, "昵称不能为空", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		if (SpMessageUtil.getYZUserVO(getApplicationContext()).getNickname()
				.equals(nickname)) {
			Toast.makeText(ProfileActivity.this, "昵称未修改", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		YZNetworkUtils.modifyNickname(nickname,
				SpMessageUtil.getLogonToken(getApplicationContext()),
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						String response = arg0.result;

						if (!YZNetworkUtils.isAllowedContinue(
								ProfileActivity.this, response)) {
							return;
						}

						ProfileActivity.this.finish();
						overridePendingTransition(0,
								R.anim.activity_anim_left_out);

						Toast.makeText(ProfileActivity.this, "修改成功",
								Toast.LENGTH_SHORT).show();

					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {

					}
				});

	}

	/**
	 * 设置昵称
	 * 
	 * @param v
	 */
	@OnClick(R.id.profile_nick_layout)
	public void nickLayoutClick(View v) {
		nickEditText.requestFocus();
		InputMethodManager imm = (InputMethodManager) nickEditText.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
	}

	/**
	 * 点击班级
	 * 
	 * @param v
	 */
	@OnClick(R.id.profile_classes_layout)
	public void classesLayoutClick(View v) {

	}

	/**
	 * 点击年级
	 * 
	 * @param v
	 */
	@OnClick(R.id.profile_grade_layout)
	public void gradeLayoutClick(View v) {

	}

	/**
	 * 点击院系
	 * 
	 * @param v
	 */
	@OnClick(R.id.profile_college_layout)
	public void collegeLayoutClick(View v) {

	}

	/**
	 * 点击学校
	 * 
	 * @param v
	 */
	@OnClick(R.id.profile_school_layout)
	public void professionLayoutClick(View v) {

	}

	// /**
	// * 设置日生日期
	// *
	// * @param v
	// */
	// @OnClick(R.id.profile_birthday_layout)
	// public void birthdayLayoutClick(View v) {
	// Calendar calendar = Calendar.getInstance();
	// new DatePickerDialog(ProfileActivity.this, new
	// DatePickerDialog.OnDateSetListener() {
	//
	// @Override
	// public void onDateSet(DatePicker arg0, int year, int month, int day) {
	//
	// birthdayTextView.setText(year + "-" + month + "-" + day);
	// }
	// }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
	// calendar.get(Calendar.DAY_OF_MONTH)).show();
	// }

	// public class PopupWindows extends PopupWindow {
	//
	// public PopupWindows(Context mContext, View parent) {
	//
	// View view = View.inflate(mContext, R.layout.popupwindows, null);
	// view.startAnimation(AnimationUtils.loadAnimation(mContext,
	// R.anim.fade_ins));
	// LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
	// // ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
	// // R.anim.push_bottom_in_2));
	//
	// setWidth(LayoutParams.FILL_PARENT);
	// setHeight(LayoutParams.FILL_PARENT);
	// setBackgroundDrawable(new BitmapDrawable());
	// setFocusable(true);
	// setOutsideTouchable(true);
	// setContentView(view);
	// showAtLocation(parent, Gravity.BOTTOM, 0, 0);
	// update();
	//
	// Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
	// Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
	// Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
	// bt1.setOnClickListener(new OnClickListener() {
	// public void onClick(View v) {
	// sexTextView.setText("男");
	// dismiss();
	// }
	// });
	// bt2.setOnClickListener(new OnClickListener() {
	// public void onClick(View v) {
	// sexTextView.setText("女");
	// dismiss();
	// }
	// });
	// bt3.setOnClickListener(new OnClickListener() {
	// public void onClick(View v) {
	// dismiss();
	// }
	// });
	//
	// }
	// }

}
