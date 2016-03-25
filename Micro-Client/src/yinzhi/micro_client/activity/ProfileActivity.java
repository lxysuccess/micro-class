package yinzhi.micro_client.activity;

import java.util.Calendar;

import yinzhi.micro_client.R;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class ProfileActivity extends Activity{
	
	private PopupWindow popupWindow = null;
	
	@ViewInject(R.id.profile_close)
	private ImageButton close;
	@ViewInject(R.id.profile_save)
	private Button save;

	@ViewInject(R.id.profile_mail)
	private TextView mailTextView;

	@ViewInject(R.id.profile_phone)
	private EditText phoneEditText;
	@ViewInject(R.id.profile_phone_layout)
	private RelativeLayout phoneLayout;

	@ViewInject(R.id.profile_sex)
	private TextView sexTextView;
	@ViewInject(R.id.profile_sex_layout)
	private RelativeLayout sexLayout;

	@ViewInject(R.id.profile_birthday)
	private TextView birthdayTextView;
	@ViewInject(R.id.profile_birthday_layout)
	private RelativeLayout birthdayLayout;

	@ViewInject(R.id.profile_profession)
	private TextView professionTextView;
	@ViewInject(R.id.profile_profession_layout)
	private RelativeLayout professionLayout;

	@ViewInject(R.id.profile_layout)
	private RelativeLayout parentLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		ViewUtils.inject(this);
	}

	/**
	 * 
	 * 
	 * @param v
	 */
	@OnClick(R.id.profile_close)
	public void closeClick(View v) {
		ProfileActivity.this.finish();
		overridePendingTransition(0, R.anim.activity_anim_left_out);
	}

	/**
	 * 
	 * 
	 * @param v
	 */
	@OnClick(R.id.profile_save)
	public void saveClick(View v) {

	}

	/**
	 * 
	 * 
	 * @param v
	 */
	@OnClick(R.id.profile_phone_layout)
	public void phoneLayoutClick(View v) {
		phoneEditText.requestFocus();
		InputMethodManager imm = (InputMethodManager) phoneEditText
				.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
	}

	/**
	 * 
	 * 
	 * @param v
	 */
	@OnClick(R.id.profile_sex_layout)
	public void sexLayoutClick(View v) {
		popupWindow = new PopupWindows(this,parentLayout);
	}

	/**
	 * �༭�޸�����
	 * 
	 * @param v
	 */
	@OnClick(R.id.profile_birthday_layout)
	public void birthdayLayoutClick(View v) {
		Calendar calendar = Calendar.getInstance();
		new DatePickerDialog(ProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker arg0, int year, int month, int day) {
				
				birthdayTextView.setText(year+"-"+month+"-"+day);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
	}

	/**
	 * �༭�޸�ְҵ
	 * 
	 * @param v
	 */
	@OnClick(R.id.profile_profession_layout)
	public void professionLayoutClick(View v) {

	}
	
	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {

			View view = View.inflate(mContext, R.layout.popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
			// ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
			// R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					sexTextView.setText("男");
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					sexTextView.setText("女");
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

	
}
