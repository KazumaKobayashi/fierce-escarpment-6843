package com.example.jackson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * フロントに返却するオブジェクトを作成するクラス
 *
 * @author Kazuki Hasegawa
 */
public class Response {
	private Map<String, Object> objects;
	private ObjectMapper mapper;

	public Response() {
		objects = new HashMap<String, Object>();
		mapper = new ObjectMapper();
	}

	/**
	 * ステータスコードを設定する
	 *
	 * @param statusCode
	 */
	public void setStatusCode(Integer statusCode) {
		objects.put("code", statusCode);
	}

	/**
	 * エラーメッセージを追加する
	 *
	 * @param message
	 */
	@SuppressWarnings("unchecked")
	public void addErrorMessage(String message) {
		if (objects.containsKey("msg")) {
			Object obj = objects.get("msg");
			if (obj instanceof List) {
				((List<String>) obj).add(message);
			}
		} else {
			objects.put("msg", new ArrayList<String>(Arrays.asList(message)));
		}
	}

	/**
	 * ステータスコードの取得
	 *
	 * @return
	 */
	public Integer getStatusCode() {
		return (Integer) objects.get("code");
	}

	/**
	 * ステータスコード以外に追加したいオブジェクトがある場合はここに設定する
	 *
	 * @param key
	 * @param object
	 */
	public void addObjects(String key, Object object) {
		objects.put(key, object);
	}

	/**
	 * エラーメッセージがあるかどうか
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean hasErrors() {
		if (objects.containsKey("msg")) {
			List<String> errors = (List<String>) objects.get("msg");
			return !errors.isEmpty();
		}
		return false;
	}

	/**
	 * 返却するJson文字列を作成するクラス
	 * 失敗した場合は空文字
	 *
	 * @return
	 */
	public String getResponseJson() {
		try {
			return mapper.writeValueAsString(objects);
		} catch (IOException e) {
			return StringUtils.EMPTY;
		}
	}
}