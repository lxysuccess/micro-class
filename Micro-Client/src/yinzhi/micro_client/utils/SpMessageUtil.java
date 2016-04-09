package yinzhi.micro_client.utils;

import yinzhi.micro_client.network.vo.YZUserVO;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.lidroid.xutils.util.LogUtils;

/**
 * sharedPreferences 工具类
 * 
 * @author lixuyu
 * 
 */
public class SpMessageUtil {

	/**
	 * 存储用户信息至sp
	 * 
	 * @param userVO
	 * @param context
	 * @return
	 */
	public static int storeYZUserVO(YZUserVO userVO, Context context) {
		LogUtils.i(userVO.toString());

		//
		int result = 0;
		try {
			SharedPreferences mSharedPreferences = context
					.getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = mSharedPreferences.edit();

			editor.putString("username", userVO.getUsername());
			editor.putString("token", userVO.getToken());
			
			editor.putString("nickname", userVO.getNickname());
			Log.i("setUser",userVO.getNickname());

			if (userVO.getAvatarPicPath() == null) {
				editor.putString("avatarPicPath", "");
			} else {
				editor.putString("avatarPicPath", userVO.getAvatarPicPath());
			}
			if (userVO.getAvatarPicPath() == null) {
				editor.putString("classes", "");
			} else {
				editor.putString("classes", userVO.getClasses());
			}
			if (userVO.getAvatarPicPath() == null) {
				editor.putString("grade", "");
			} else {
				editor.putString("grade", userVO.getGrade());
			}
			if (userVO.getAvatarPicPath() == null) {
				editor.putString("college", "");
			} else {
				editor.putString("college", userVO.getCollege());
			}
			if (userVO.getAvatarPicPath() == null) {
				editor.putString("school", "");
			} else {
				editor.putString("school", userVO.getSchool());
			}

			editor.commit();
			result = 1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取在sp中用户信息
	 * 
	 * @param context
	 * @return
	 */
	public static YZUserVO getYZUserVO(Context context) {
		//
		YZUserVO userVO = new YZUserVO();
		try {
			SharedPreferences mSharedPreferences = context
					.getSharedPreferences("userinfo", Activity.MODE_PRIVATE);

			userVO.setUsername(mSharedPreferences.getString("username", ""));
			userVO.setToken(mSharedPreferences.getString("token", ""));
			userVO.setNickname(mSharedPreferences.getString("nickname", ""));
			
			//TODO
			Log.i("getUser",userVO.getNickname());
			
			
			userVO.setAvatarPicPath(mSharedPreferences.getString(
					"avatarPicPath", null));
			userVO.setClasses(mSharedPreferences.getString("classes", null));
			userVO.setGrade(mSharedPreferences.getString("grade", null));
			userVO.setCollege(mSharedPreferences.getString("college", null));
			userVO.setSchool(mSharedPreferences.getString("school", null));

		} catch (Exception e) {
			LogUtils.i("读取sharedPreferences异常");
			return null;
		}
		LogUtils.i(userVO.toString());
		return userVO;
	}

	/**
	 * 获取存储在sp中的token
	 * 
	 * @param context
	 * @return
	 */
	public static String getLogonToken(Context context) {
		String token = null;
		try {
			SharedPreferences mSharedPreferences = context
					.getSharedPreferences("userinfo", Activity.MODE_PRIVATE);

			token = mSharedPreferences.getString("token", null);

		} catch (Exception e) {
			LogUtils.i("读取token数据异常");
			return null;
		}
		return token;
	}

	public static int deleteSPMsg(String key, Context context) {
		int result = 0;
		try {
			SharedPreferences mSharedPreferences = context
					.getSharedPreferences(key, Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = mSharedPreferences.edit();
			editor.clear().commit();
			result = 1;
		} catch (Exception e) {
			LogUtils.i("删除异常");
			return result;
		}
		return result;
	}
}
