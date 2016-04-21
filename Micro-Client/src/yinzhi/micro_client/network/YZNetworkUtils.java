package yinzhi.micro_client.network;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import yinzhi.micro_client.activity.LoginActivity;
import yinzhi.micro_client.activity.video.MyApplication;
import yinzhi.micro_client.network.constants.INetworkConstants;
import yinzhi.micro_client.network.vo.YZBaseVO;
import yinzhi.micro_client.utils.SecureUtil;
import yinzhi.micro_client.utils.SpMessageUtil;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

/**
 * Created by LIXUYU on 16/3/19.
 */
public class YZNetworkUtils implements INetworkConstants {
	public static HttpUtils http = new HttpUtils();
	public static Map<String, Object> paramMap = new HashMap<String, Object>();

	/**
	 * 检查网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				if (mNetworkInfo.isAvailable()) {
					return true;
				} else {
					Toast.makeText(context, "请检查网络连接", Toast.LENGTH_LONG)
							.show();
				}

			}
		}
		return false;
	}

	public static boolean isAllowedContinue(Context context, String response) {

		try {
			YZBaseVO base = YZResponseUtils.parseObject(response,
					YZBaseVO.class);

			if (base.getStatus() == 0) {
				Toast.makeText(context, base.getMsg(), Toast.LENGTH_SHORT)
						.show();
				return false;
			} else if (base.getStatus() == 2) {

				Toast.makeText(context, base.getMsg(), Toast.LENGTH_SHORT)
						.show();
				SpMessageUtil.deleteSPMsg("userinfo",
						context.getApplicationContext());
				
				MyApplication.setUserInfo(null);

				LoginActivity.intentTo(context, context.getClass().getName());
				
				LogUtils.i("this context's name-->"+context.getClass().getName());
				return false;

			} else {
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 获取轮播信息
	 * 
	 * @param logonToken
	 *            令牌(非必须)
	 * @param deviceId
	 *            设备标识（非必须）
	 * @param callBack
	 */
	public static void fetchSlideList(Context context, String logonToken,
			String deviceId, RequestCallBack<String> callBack) {
		if (!isNetworkConnected(context)) {
			return;
		}

		RequestParams params = new RequestParams();
		params.addHeader("Content-Type", "application/json;charset=utf-8");

		paramMap.clear();

		if (logonToken != null) {
			paramMap.put(LOGON_TOKEN, logonToken);
		}
		if (logonToken != null) {
			paramMap.put("deviceId", deviceId);
		}
		try {
			params.setBodyEntity(new StringEntity(JSON.toJSONString(paramMap),
					INetworkConstants.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		http.send(HttpRequest.HttpMethod.POST, API_COURSE_SLIDELIST, params,
				callBack);

	}

	/**
	 * 获取首页人气推荐视频
	 * 
	 * @param callBack
	 */
	public static void fetchChargeRecommendCourse(String logonToken,
			Integer page, Integer size, RequestCallBack<String> callBack) {
		RequestParams params = new RequestParams();
		params.addHeader("Content-Type", "application/json;charset=utf-8");

		paramMap.clear();

		paramMap.put("page", page.toString());
		paramMap.put("size", size.toString());

		if (logonToken != null) {
			paramMap.put(LOGON_TOKEN, logonToken);
		}
		try {
			params.setBodyEntity(new StringEntity(JSON.toJSONString(paramMap),
					INetworkConstants.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		http.send(HttpRequest.HttpMethod.POST, API_COURSE_CHARGERECOMMEND,
				params, callBack);
	}

	/**
	 * 获取首页免费推荐视频
	 * 
	 * @param callBack
	 */
	public static void fetchFreeRecommendCourse(String logonToken,
			Integer page, Integer size, RequestCallBack<String> callBack) {
		RequestParams params = new RequestParams();
		params.addHeader("Content-Type", "application/json;charset=utf-8");

		paramMap.clear();

		paramMap.put(LOGON_TOKEN, logonToken);
		paramMap.put("page", page.toString());
		paramMap.put("size", size.toString());

		if (logonToken != null) {
			paramMap.put(LOGON_TOKEN, logonToken);
		}
		try {
			params.setBodyEntity(new StringEntity(JSON.toJSONString(paramMap),
					INetworkConstants.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		http.send(HttpRequest.HttpMethod.POST, API_COURSE_FREERECOMMEND,
				params, callBack);
	}

	/**
	 * 获取免费排行榜列表
	 * 
	 * @param logonToken
	 *            令牌(非必须)
	 * @param deviceId
	 *            设备标识（非必须）
	 * @param page
	 *            页码
	 * @param size
	 *            一页的数量
	 * @param callBack
	 */
	public static void fetchFreeRankingList(String logonToken, String deviceId,
			Integer page, Integer size, RequestCallBack<String> callBack) {
		RequestParams params = new RequestParams();
		params.addHeader("Content-Type", "application/json;charset=utf-8");

		paramMap.clear();

		paramMap.put(LOGON_TOKEN, logonToken);
		paramMap.put("page", page.toString());
		paramMap.put("size", size.toString());

		if (logonToken != null) {
			paramMap.put(LOGON_TOKEN, logonToken);
		}
		if (deviceId != null) {
			paramMap.put("deviceId", deviceId);
		}
		try {
			params.setBodyEntity(new StringEntity(JSON.toJSONString(paramMap),
					INetworkConstants.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		http.send(HttpRequest.HttpMethod.POST, API_COURSE_FREERANKINGLIST,
				params, callBack);
	}

	/**
	 * 获取畅销排行榜列表
	 * 
	 * @param logonToken
	 *            令牌(非必须)
	 * @param deviceId
	 *            设备标识（非必须）
	 * @param page
	 *            页码
	 * @param size
	 *            一页的数量
	 * @param callBack
	 */
	public static void fetchChargeRankingList(String logonToken,
			String deviceId, Integer page, Integer size,
			RequestCallBack<String> callBack) {
		RequestParams params = new RequestParams();
		params.addHeader("Content-Type", "application/json;charset=utf-8");

		paramMap.clear();

		paramMap.put(LOGON_TOKEN, logonToken);
		paramMap.put("page", page.toString());
		paramMap.put("size", size.toString());

		if (logonToken != null) {
			paramMap.put(LOGON_TOKEN, logonToken);
		}
		if (deviceId != null) {
			paramMap.put("deviceId", deviceId);
		}
		try {
			params.setBodyEntity(new StringEntity(JSON.toJSONString(paramMap),
					INetworkConstants.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		http.send(HttpRequest.HttpMethod.POST, API_COURSE_CHARGERANKINGLIST,
				params, callBack);
	}

	/**
	 * 搜索
	 * 
	 * @param keyWord
	 * @param logonToken
	 * @param page
	 * @param size
	 * @param callBack
	 */
	public static void courseSearch(String keyWord, String logonToken,
			Integer page, Integer size, RequestCallBack<String> callBack) {
		RequestParams params = new RequestParams();
		params.addHeader("Content-Type", "application/json;charset=utf-8");

		paramMap.clear();
		paramMap.put("keyWord", keyWord);
		paramMap.put("page", page);
		paramMap.put("size", size);

		if (logonToken != null) {
			paramMap.put(LOGON_TOKEN, logonToken);
		}
		try {
			params.setBodyEntity(new StringEntity(JSON.toJSONString(paramMap),
					INetworkConstants.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		http.send(HttpRequest.HttpMethod.POST, API_COURSE_SEARCH, params,
				callBack);
	}

	/**
	 * 请求课程详细信息
	 * 
	 * @param logonToken
	 * @param courseId
	 * @param callBack
	 */
	public static void courseDetail(String logonToken, String courseId,
			RequestCallBack<String> callBack) {
		RequestParams params = new RequestParams();
		params.addHeader("Content-Type", "application/json;charset=utf-8");

		paramMap.clear();
		paramMap.put("courseId", courseId);

		if (logonToken != null) {
			paramMap.put(LOGON_TOKEN, logonToken);
		}

		try {
			params.setBodyEntity(new StringEntity(JSON.toJSONString(paramMap),
					INetworkConstants.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		http.send(HttpRequest.HttpMethod.POST, API_COURSE_DETAIL, params,
				callBack);
	}

	/**
	 * 请求课程目录
	 * 
	 * @param logonToken
	 * @param courseId
	 * @param callBack
	 */
	public static void courseCatalog(String logonToken, String courseId,
			RequestCallBack<String> callBack) {
		RequestParams params = new RequestParams();
		params.addHeader("Content-Type", "application/json;charset=utf-8");

		paramMap.clear();
		paramMap.put("courseId", courseId);

		if (logonToken != null) {
			paramMap.put(LOGON_TOKEN, logonToken);
		}
		try {
			params.setBodyEntity(new StringEntity(JSON.toJSONString(paramMap),
					INetworkConstants.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		http.send(HttpRequest.HttpMethod.POST, API_COURSE_CATALOG, params,
				callBack);
	}

	/**
	 * 请求订阅课程
	 * 
	 * @param logonToken
	 * @param courseId
	 * @param callBack
	 */
	public static void courseSubscribe(Context context, String logonToken,
			String courseId, RequestCallBack<String> callBack) {
		RequestParams params = new RequestParams();
		params.addHeader("Content-Type", "application/json;charset=utf-8");

		LogUtils.i("courseSubscribe======context.getClass.getName-->"
				+ context.getClass().getName());

		paramMap.clear();
		paramMap.put("courseId", courseId);

		if (logonToken != null) {
			paramMap.put(LOGON_TOKEN, logonToken);
		}
		try {
			params.setBodyEntity(new StringEntity(JSON.toJSONString(paramMap),
					INetworkConstants.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		http.send(HttpRequest.HttpMethod.POST, API_COURSE_SUBSCRIBE, params,
				callBack);
	}

	/**
	 * 请求练习
	 * 
	 * @param itemResourceId
	 * @param logonToken
	 * @param callBack
	 */
	public static void fetchExercise(Context context, String logonToken,
			String itemResourceId, RequestCallBack<String> callBack) {
		RequestParams params = new RequestParams();

		params.addHeader("Content-Type", "application/json;charset=utf-8");

		paramMap.clear();

		paramMap.put("itemResourceId", itemResourceId);

		if (logonToken == null) {
			LogUtils.i("courseSubscribe======context.getClass.getName-->"
					+ context.getClass().getName());
			return;
		}
		paramMap.put(LOGON_TOKEN, logonToken);
		try {
			params.setBodyEntity(new StringEntity(JSON.toJSONString(paramMap),
					INetworkConstants.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		http.send(HttpRequest.HttpMethod.POST, API_EXERCISE, params, callBack);
	}
	/**
	 * 请求视频播放随机弹出练习
	 * 
	 * @param itemResourceId
	 * @param logonToken
	 * @param callBack
	 */
	public static void fetchrandomExercise(Context context, String logonToken,
			String itemResourceId, RequestCallBack<String> callBack) {
		RequestParams params = new RequestParams();
		
		params.addHeader("Content-Type", "application/json;charset=utf-8");
		
		paramMap.clear();
		
		paramMap.put("itemResourceId", itemResourceId);
		
		if (logonToken == null) {
			LogUtils.i("courseSubscribe======context.getClass.getName-->"
					+ context.getClass().getName());
			return;
		}
		paramMap.put(LOGON_TOKEN, logonToken);
		try {
			params.setBodyEntity(new StringEntity(JSON.toJSONString(paramMap),
					INetworkConstants.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		http.send(HttpRequest.HttpMethod.POST, API_RANDOM_EXERCISE, params, callBack);
	}

	/**
	 * 记录练习情况
	 * 
	 * @param logonToken
	 * @param itemResourceId
	 * @param userChoice
	 * @param isTrue
	 * @param callBack
	 */
	public static void exerciseRecord(String logonToken, String itemResourceId,
			String userChoice, String isTrue, RequestCallBack<String> callBack) {
		RequestParams params = new RequestParams();
		params.addHeader(LOGON_TOKEN, logonToken);
		params.addBodyParameter("itemResourceId", itemResourceId);
		params.addBodyParameter("userChoice", userChoice);
		params.addBodyParameter("isTrue", isTrue);
		http.send(HttpRequest.HttpMethod.POST, API_EXERCISE, params, callBack);
	}

	/**
	 * 请求小贴士
	 * 
	 * @param itemResourceId
	 * @param logonToken
	 * @param callBack
	 */
	public static void fetchCourseTips(String logonToken,
			String itemResourceId, RequestCallBack<String> callBack) {

		RequestParams params = new RequestParams();

		params.addHeader("Content-Type", "application/json;charset=utf-8");

		paramMap.clear();

		paramMap.put("itemResourceId", itemResourceId);

		if (logonToken != null) {
			paramMap.put(LOGON_TOKEN, logonToken);
		}

		try {
			params.setBodyEntity(new StringEntity(JSON.toJSONString(paramMap),
					INetworkConstants.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		http.send(HttpRequest.HttpMethod.POST, API_COURSE_TIPS, params,
				callBack);
	}

	/**
	 * 请求视频
	 * 
	 * @param itemResourceId
	 * @param token
	 * @param callBack
	 */
	public static void fetchVideo(String itemResourceId, String logonToken,
			RequestCallBack<String> callBack) {
		RequestParams params = new RequestParams();

		params.addHeader("Content-Type", "application/json;charset=utf-8");

		paramMap.clear();

		paramMap.put("itemResourceId", itemResourceId);

		if (logonToken != null) {
			paramMap.put(LOGON_TOKEN, logonToken);
		}

		try {
			params.setBodyEntity(new StringEntity(JSON.toJSONString(paramMap),
					INetworkConstants.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		http.send(HttpRequest.HttpMethod.POST, API_VIDEO, params, callBack);
	}

	/**
	 * 视频字幕搜索
	 * 
	 * @param logonToken
	 * @param keyWord
	 * @param itemResourceId
	 * @param page
	 * @param size
	 * @param callBack
	 */
	public static void searchVideoSubtitle(String logonToken, String keyWord,
			String itemResourceId, Integer page, Integer size,
			RequestCallBack<String> callBack) {
		RequestParams params = new RequestParams();

		params.addHeader("Content-Type", "application/json;charset=utf-8");
		paramMap.clear();

		paramMap.put("itemResourceId", itemResourceId);
		paramMap.put("keyWord", keyWord);
		paramMap.put("page", page);
		paramMap.put("size", size);

		if (logonToken != null) {
			paramMap.put(LOGON_TOKEN, logonToken);
		}

		try {
			params.setBodyEntity(new StringEntity(JSON.toJSONString(paramMap),
					INetworkConstants.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		http.send(HttpRequest.HttpMethod.POST, API_VIDEO_SUBTITLE, params,
				callBack);
	}

	/**
	 * 获取评论列表
	 * 
	 * @param bookId
	 * @param page
	 * @param callBack
	 */
	public static void fetchCommentList(String logonToken,
			String itemResourceId, Integer page, Integer size,
			RequestCallBack<String> callBack) {
		RequestParams params = new RequestParams();

		params.addHeader("Content-Type", "application/json;charset=utf-8");
		paramMap.clear();

		paramMap.put("itemResourceId", itemResourceId);
		paramMap.put("page", page);
		paramMap.put("size", size);
		if (logonToken != null) {
			paramMap.put(LOGON_TOKEN, logonToken);
		}

		try {
			params.setBodyEntity(new StringEntity(JSON.toJSONString(paramMap),
					INetworkConstants.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		http.send(HttpRequest.HttpMethod.POST, API_COMMENT_LIST, params,
				callBack);
	}

	/**
	 * 发表评论
	 * 
	 * @param token
	 * @param callBack
	 */
	public static void publishComment(String token, String content,
			String itemResourceId, RequestCallBack<String> callBack) {
		RequestParams params = new RequestParams();

		params.addHeader("Content-Type", "application/json;charset=utf-8");
		paramMap.clear();

		paramMap.put("content", content);
		paramMap.put("itemResourceId", itemResourceId);

		paramMap.put(LOGON_TOKEN, token);

		try {
			params.setBodyEntity(new StringEntity(JSON.toJSONString(paramMap),
					INetworkConstants.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		http.send(HttpRequest.HttpMethod.POST, API_COMMENT_PUBLISH, params,
				callBack);
	}

	/**
	 * 发表评分
	 * 
	 * @param token
	 * @param callBack
	 */
	public static void markScore(String token, String score,
			String itemResourceId, RequestCallBack<String> callBack) {
		RequestParams params = new RequestParams();

		params.addHeader("Content-Type", "application/json;charset=utf-8");
		paramMap.clear();

		paramMap.put("score", score);
		paramMap.put("itemResourceId", itemResourceId);

		paramMap.put(LOGON_TOKEN, token);

		try {
			params.setBodyEntity(new StringEntity(JSON.toJSONString(paramMap),
					INetworkConstants.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		http.send(HttpRequest.HttpMethod.POST, API_SCORE_MARK, params, callBack);
	}

	/**
	 * 获取用户对该资源的评分
	 * 
	 * @param token
	 * @param itemResourceId
	 * @param callBack
	 */
	public static void fetchPersonalScore(String token, String itemResourceId,
			RequestCallBack<String> callBack) {
		RequestParams params = new RequestParams();

		params.addHeader("Content-Type", "application/json;charset=utf-8");
		paramMap.clear();

		paramMap.put("itemResourceId", itemResourceId);
		paramMap.put(LOGON_TOKEN, token);

		// params.addHeader(LOGON_TOKEN, token);

		try {
			params.setBodyEntity(new StringEntity(JSON.toJSONString(paramMap),
					INetworkConstants.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		http.send(HttpRequest.HttpMethod.POST, API_SCORE_PERSONAL, params,
				callBack);
	}

	/**
	 * 请求分类信息
	 * 
	 * @param callBack
	 */
	public static void fetchClassifyList(RequestCallBack<String> callBack) {
		http.send(HttpRequest.HttpMethod.POST, API_CLASSIFY_LIST, null,
				callBack);
	}

	/**
	 * 请求某分类课程
	 * 
	 * @param callBack
	 */
	public static void fetchListByClassify(String classifyId, Integer page,
			Integer size, RequestCallBack<String> callBack) {
		RequestParams params = new RequestParams();

		params.addHeader("Content-Type", "application/json;charset=utf-8");
		paramMap.clear();

		paramMap.put("classifyId", classifyId);
		paramMap.put("page", page.toString());
		paramMap.put("size", size.toString());

		try {
			params.setBodyEntity(new StringEntity(JSON.toJSONString(paramMap),
					INetworkConstants.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		http.send(HttpRequest.HttpMethod.POST, API_CLASSIFY_LISTBYCLASSIFY,
				params, callBack);
	}

	/**
	 * 请求解析二维码字符串
	 * 
	 * @param logonToken
	 * @param barcode
	 * @param identify
	 * @param callBack
	 */
	public static void barcode(String logonToken, String barcode,
			String identify, RequestCallBack<String> callBack) {
		RequestParams params = new RequestParams();
		params.addHeader("Content-Type", "application/json;charset=utf-8");

		paramMap.clear();

		paramMap.put("barcode", barcode);
		paramMap.put("identify", identify);

		if (logonToken != null) {
			paramMap.put(LOGON_TOKEN, logonToken);
		}

		try {
			params.setBodyEntity(new StringEntity(JSON.toJSONString(paramMap),
					INetworkConstants.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		http.send(HttpRequest.HttpMethod.POST, API_BARCODE, params, callBack);
	}

	/**
	 * 我的订阅列表
	 * 
	 * @param token
	 * @param callBack
	 */
	public static void fetchMyCourseList(String logonToken, Integer page,
			Integer size, RequestCallBack<String> callBack) {
		RequestParams params = new RequestParams();
		params.addHeader("Content-Type", "application/json;charset=utf-8");

		paramMap.clear();

		if (logonToken != null) {
			paramMap.put(LOGON_TOKEN, logonToken);
		}

		paramMap.put("page", page);
		paramMap.put("size", size);

		try {
			params.setBodyEntity(new StringEntity(JSON.toJSONString(paramMap),
					INetworkConstants.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		http.send(HttpRequest.HttpMethod.POST, API_USER_COURSELIST, params,
				callBack);
	}

	/**
	 * 用户登录
	 * 
	 * @param deviceId
	 * @param username
	 * @param password
	 * @param callBack
	 */
	public static void doLogin(String deviceId, String username,
			String password, RequestCallBack<String> callBack) {
		RequestParams params = new RequestParams();
		params.addHeader("Content-Type", "application/json;charset=utf-8");

		paramMap.clear();

		if (deviceId != null) {
			paramMap.put("deviceId", deviceId);
		}
		paramMap.put("username", username);
		paramMap.put("password", SecureUtil.MD5(password));

		try {
			params.setBodyEntity(new StringEntity(JSON.toJSONString(paramMap),
					INetworkConstants.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		http.send(HttpRequest.HttpMethod.POST, API_USER_LOGIN, params, callBack);

	}

	/**
	 * 用户注册
	 * 
	 * @param username
	 * @param password
	 * @param deviceId
	 * @param callBack
	 */
	public static void doRegister(String username, String password,
			String deviceId, String nickname, RequestCallBack<String> callBack) {
		RequestParams params = new RequestParams();

		params.addHeader("Content-Type", "application/json;charset=utf-8");

		paramMap.clear();

		paramMap.put("username", username);
		paramMap.put("nickname", nickname);
		paramMap.put("password", SecureUtil.MD5(password));

		if (deviceId != null) {
			paramMap.put("deviceId", deviceId);
		}

		try {
			params.setBodyEntity(new StringEntity(JSON.toJSONString(paramMap),
					INetworkConstants.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		http.send(HttpRequest.HttpMethod.POST, API_USER_REGISTER, params,
				callBack);
	}

	/**
	 * 验证用户名唯一性
	 * 
	 * @param username
	 * @param callBack
	 */
	public static void verifyUsername(String username,
			RequestCallBack<String> callBack) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("username", username);
		http.send(HttpMethod.POST, API_USER_VERIFYUSERNAME, callBack);
	}

	/**
	 * 修改昵称
	 * 
	 * @param nickname
	 * @param logonToken
	 * @param callBack
	 */
	public static void modifyNickname(String nickname, String logonToken,
			RequestCallBack<String> callBack) {
		RequestParams params = new RequestParams();

		params.addHeader("Content-Type", "application/json;charset=utf-8");

		paramMap.clear();

		paramMap.put("nickname", nickname);

		if (logonToken != null) {
			paramMap.put("logonToken", logonToken);
		}

		try {
			params.setBodyEntity(new StringEntity(JSON.toJSONString(paramMap),
					INetworkConstants.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		http.send(HttpMethod.POST, API_USER_MODIFYNICKNAME, callBack);
	}
}
