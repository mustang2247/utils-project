/**
 * 
 */
package com.hoolai.ccgames.center.utils;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Json工具类，基于Gson
 * 
 * @author Cedric(TaoShuang)
 * @create 2012-2-29
 */
public abstract class JsonUtil {
	private static GsonBuilder builder = new GsonBuilder();
	private static ThreadLocal<Gson> gsonFactory = new ThreadLocal<Gson>();

	public static String toJson(Object object) {
		Gson gson = init();
		return gson.toJson(object);
	}

	private static Gson init() {
//		builder.excludeFieldsWithoutExposeAnnotation();
		Gson gson = gsonFactory.get();
		if (gson == null) {
			gson = builder.create();
			gsonFactory.set(gson);
		}
		return gson;
	}

	public static <E> E fromJson(String json, Class<E> clazz) {
		Gson gson = init();
		return gson.fromJson(json, clazz);
	}
	
	public static <E> E fromJson(String json,Type typeOfT) {
		Gson gson = init();
		return gson.fromJson(json, (Type) typeOfT);
	}
	public static byte[] toJsonBytes(Object object, Charset set) {
		Gson gson = init();
		return gson.toJson(object).getBytes(set);
	}

	public static byte[] toJsonBytes(Object object) {
		Gson gson = init();
		return gson.toJson(object).getBytes();
	}

	public static <E> E fromJsonBytes(byte[] json, Class<E> clazz, Charset set) {
		Gson gson = init();
		return gson.fromJson(new String(json, set), clazz);
	}

	public static <E> E fromJsonBytes(byte[] json, Class<E> clazz) {
		Gson gson = init();
		return gson.fromJson(new String(json), clazz);
	}
	
	public static void clearMemory() {
		gsonFactory = null;
	}
}
