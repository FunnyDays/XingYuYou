package com.xingyuyou.xingyuyou.Utils.MCUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class RequestParamUtil {

	private static final String TAG = "RequestParamUtil";

	public static String getRequestParamString(Map<String, String> paramMap) {
		if (null == paramMap || paramMap.size() < 1) {
			return "";
		}

		List<Map.Entry<String, String>> mappingList = new ArrayList<Map.Entry<String, String>>(
				paramMap.entrySet());
		Collections.sort(mappingList,
				new Comparator<Map.Entry<String, String>>() {

					@Override
					public int compare(Map.Entry<String, String> mapping1,
							Map.Entry<String, String> mapping2) {
						return mapping1.getKey().compareTo(mapping2.getKey());
					}
				});
		JSONObject jsonparam = new JSONObject();
		String values = "";
		try {
			for (Map.Entry<String, String> mapping : mappingList) {
				String key = mapping.getKey().trim();
				String value = mapping.getValue();
				// MCLog.e(TAG, key + ":" + value);
				jsonparam.put(key, value);

				values += mapping.getValue();
			}
			//MCLog.i(TAG, "key " + OrderInfoUtils.MCH_MD5KEY());
			String md5key = PaykeyUtil.stringToMD5(values.trim()
					+ "021555");
			jsonparam.put("md5_sign", md5key);
			//MCLog.i(TAG, "发送的参数" + jsonparam.toString());
			return Base64.encode(jsonparam.toString().getBytes());
		} catch (JSONException e) {
		}
		return "";
	}
}
