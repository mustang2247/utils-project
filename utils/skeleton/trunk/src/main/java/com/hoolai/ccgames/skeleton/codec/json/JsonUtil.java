package com.hoolai.ccgames.skeleton.codec.json;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {

	private static GsonBuilder gsonBuilder = new GsonBuilder();
	private static ThreadLocal< Gson > gsonFactory = new ThreadLocal< Gson >();

	private static Gson getGson() {
		Gson gson = gsonFactory.get();
		if( gson == null ) {
			gson = gsonBuilder.create();
			gsonFactory.set( gson );
		}
		return gson;
	}

	public static String toString( Object obj ) {
		Gson gson = getGson();
		return gson.toJson( obj );
	}
	
	public static < E > E decode( String json, Class< E > clazz ) {
		Gson gson = getGson();
		return gson.fromJson( json, clazz );
	}

	public static < E > E decode( String json, Type typeOfT ) {
		Gson gson = getGson();
		return gson.fromJson( json, typeOfT );
	}
	
	public static ByteBuf encode( Object obj ) {
		byte[] messageBytes = encodeBytes( obj );
		return Unpooled.wrappedBuffer( messageBytes );
	}
	
	public static < E > E decode( ByteBuf byteBuf, Class< E > clazz ) {
		byte[] bytes = new byte[byteBuf.readableBytes()];
		byteBuf.readBytes( bytes );
		return decodeBytes( bytes, clazz );
	}

	public static byte[] encodeBytes( Object object, Charset set ) {
		Gson gson = getGson();
		return gson.toJson( object ).getBytes( set );
	}

	public static byte[] encodeBytes( Object object ) {
		Gson gson = getGson();
		return gson.toJson( object ).getBytes();
	}

	public static < E > E decodeBytes( byte[] json, Class< E > clazz,
			Charset set ) {
		Gson gson = getGson();
		return gson.fromJson( new String( json, set ), clazz );
	}

	public static < E > E decodeBytes( byte[] json, Class< E > clazz ) {
		Gson gson = getGson();
		return gson.fromJson( new String( json ), clazz );
	}

}
