/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package cn.jinterest.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author Mark sunlightcs@gmail.com
 */
public class R extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;


	public R data(Object data) {
		put("data", data);
		return this;
	}

	/**
	 * 把map里key为data的数据转换成某个类型的数据
	 * @param typeReference
	 * @param <T>
	 * @return
	 */
	public <T> T getData(TypeReference<T> typeReference) {
		Object obj = get("data");
		//先转换成json  再转换成想要的对象
		String json = JSON.toJSONString(obj);

		T t = JSON.parseObject(json, typeReference);

		return t;
	}

	public <T> T getData(String key, TypeReference<T> typeReference) {
		Object obj = get(key);

		String json = JSON.toJSONString(obj);

		T t = JSON.parseObject(json, typeReference);

		return t;
	}

	public R() {
		put("code", 0);
		put("msg", "success");
	}
	public static R error() {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
	}

	public static R error(String msg) {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
	}

	public static R error(int code, String msg) {
		R r = new R();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static R ok(String msg) {
		R r = new R();
		r.put("msg", msg);
		return r;
	}

	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}

	public static R ok() {
		return new R();
	}

	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}
	public  Integer getCode() {

		return (Integer) this.get("code");
	}

}
