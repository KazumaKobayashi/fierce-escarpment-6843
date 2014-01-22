package com.example.jackson;

import java.io.IOException;
import java.util.HashMap;
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