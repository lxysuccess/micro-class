package yinzhi.micro_client.utils;

import yinzhi.micro_client.network.vo.YZUserVO;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.lidroid.xutils.util.LogUtils;

/**
 * sharedPreferences 工具类
 * 
 * @author lixuyu
 * 
 */
public class SpMessageUtil {
	
	private static Context context;

	/**
	 * 存储用户信息至sp
	 * @param YZUserVO
	 * @param context
	 * @return
	 */
	public static int storeYZUserVO(YZUserVO YZUserVO, Context context) {
		//
		int result = 0;
		try {
			SharedPreferences mSharedPreferences = context.getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = mSharedPreferences.edit();

			editor.putString("username", YZUserVO.getUsername());
			editor.putString("token", YZUserVO.getToken());
			editor.putString("nickname", YZUserVO.getNickname());

			if (YZUserVO.getAvatarPicPath() == null) {
				editor.putString("avatarPicPath", "");
			} else {
				editor.putString("avatarPicPath", YZUserVO.getAvatarPicPath());
			}
			if (YZUserVO.getAvatarPicPath() == null) {
				editor.putString("classes", "");
			} else {
				editor.putString("classes", YZUserVO.getClasses());
			}
			if (YZUserVO.getAvatarPicPath() == null) {
				editor.putString("grade", "");
			} else {
				editor.putString("grade", YZUserVO.getGrade());
			}
			if (YZUserVO.getAvatarPicPath() == null) {
				editor.putString("college", "");
			} else {
				editor.putString("college", YZUserVO.getCollege());
			}
			if (YZUserVO.getAvatarPicPath() == null) {
				editor.putString("school", "");
			} else {
				editor.putString("school", YZUserVO.getSchool());
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
	 * @param context
	 * @return
	 */
	public static YZUserVO getYZUserVO(Context context) {
		//
		YZUserVO userVO = new YZUserVO();
		try {
			SharedPreferences mSharedPreferences = context.getSharedPreferences("userinfo", Activity.MODE_PRIVATE);

			userVO.setUsername(mSharedPreferences.getString("username", ""));
			userVO.setToken(mSharedPreferences.getString("token", ""));
			userVO.setNickname(mSharedPreferences.getString("nickname", ""));
			
			userVO.setAvatarPicPath(mSharedPreferences.getString("avatarPicPath", null));
			userVO.setClasses(mSharedPreferences.getString("classes", null));
			userVO.setNickname(mSharedPreferences.getString("grade", null));
			userVO.setCollege(mSharedPreferences.getString("college", null));
			userVO.setSchool(mSharedPreferences.getString("school", null));
			
			
		} catch (Exception e) {
			LogUtils.i("读取sharedPreferences异常");
			return null;
		}
		return userVO;
	}

	/**
	 * 获取存储在sp中的token
	 * @param context
	 * @return
	 */
	public static String getLogonToken(Context context) {
		String token = "";
		try {
			SharedPreferences mSharedPreferences = context.getSharedPreferences("userinfo", Activity.MODE_PRIVATE);

			token = mSharedPreferences.getString("token", "");
			
		} catch (Exception e) {
			LogUtils.i("读取token数据异常");
			return null;
		}
		return token;
	}
	
	public int deleteSPMsg(String key) {
		int result = 0;
		try {
			SharedPreferences mSharedPreferences = context.getSharedPreferences(key, Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = mSharedPreferences.edit();
			editor.clear().commit();
			result = 1;
		} catch (Exception e) {
			LogUtils.i("删除异常");
		}
		return result;
	}
}
