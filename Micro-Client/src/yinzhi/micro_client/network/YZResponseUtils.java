package yinzhi.micro_client.network;

import java.util.List;

import yinzhi.micro_client.network.constants.INetworkConstants;
import yinzhi.micro_client.network.vo.YZCatalogVO;
import yinzhi.micro_client.network.vo.YZChapterVO;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.util.LogUtils;

/**
 * Created by LIXUYU on 16/2/19.
 */
public class YZResponseUtils implements INetworkConstants {

	/**
	 * 将服务器返回结果解析为对象
	 * 
	 * @param response
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T parseObject(String response, Class<T> clazz) {
		String dataResponse = null;
		try {
			dataResponse = JSON.parseObject(response).get("data").toString();
		} catch (Exception e) {
			LogUtils.i("JSON 解析失败:"+response);
			return null;
		}
		if (dataResponse == null || "".equals(dataResponse)) {
			return null;
		}

		LogUtils.i(dataResponse + "&&&&&&&&&&&&&&&&&&&&&&&");
		return JSON.parseObject(dataResponse, clazz);
	}

	/**
	 * 将服务器返回结果解析为对象数组
	 * 
	 * @param response
	 * @param clazz
	 * @param <T>
	 * @return
	 */

	public static <T> List<T> parseArray(String response, Class<T> clazz) {
		String dataResponse = null;
		try {
			dataResponse = JSON.parseObject(response).get("data").toString();
		} catch (Exception e) {
			LogUtils.i("JSON 解析失败:"+response);
			return null;
		}
		if (dataResponse == null || "".equals(dataResponse)) {
			return null;
		}
		String listDataResponse = "";
		try {
			listDataResponse = JSON.parseObject(dataResponse).get("listdata")
					.toString();
		} catch (Exception e) {
			return JSON.parseArray(dataResponse, clazz);
		}

		if (listDataResponse == null || "".equals(listDataResponse)) {
			return null;
		}
		return JSON.parseArray(listDataResponse, clazz);
	}

	/**
	 * 将对象解析成JSON字符串
	 * 
	 * @param object
	 * @param <T>
	 * @return
	 */
	public static <T> String ObjecttoJSON(Object object) {

		return JSON.toJSONString(object);
	}

	/**
	 * 将对象数组解析成JSON字符串
	 * 
	 * @param array
	 * @param <T>
	 * @return
	 */
	public static <T> String ArraytoJSON(List<T> array) {

		return JSON.toJSONString(array);
	}

	public static YZCatalogVO parseCatalog(String response) {
		String dataResponse = null;
		try {
			dataResponse = JSON.parseObject(response).get("data").toString();
		} catch (Exception e) {
			return null;
		}
		
		List<YZChapterVO> chapters = JSON.parseArray(dataResponse,
				YZChapterVO.class);
		YZCatalogVO catalogVO = new YZCatalogVO();
		catalogVO.setChapters(chapters);

		return catalogVO;
	}

}
