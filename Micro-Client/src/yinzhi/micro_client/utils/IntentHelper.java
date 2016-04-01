package yinzhi.micro_client.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class IntentHelper {

	/**
	 * 页面跳转工具
	 * 
	 * @param context
	 *            当前Activity context
	 * @param params
	 *            需要传递的参数
	 * @param clazz
	 *            目标Activity.class
	 * @return
	 */
	public static <T> void intentTo(Context context, Bundle params, Class<T> clazz) {
		Intent intent = new Intent(context, clazz);

		intent.putExtra("BUNDLE", params);

		context.startActivity(intent);
	}
}
