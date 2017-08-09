package com.bitop.common.skeleton.codec.amf3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Input;
import flex.messaging.io.amf.Amf3Output;

public class Amf3Util {

	private final static SerializationContext serializationContext = new SerializationContext();
	private final static ThreadLocal< Amf3Input > amf3Input = new ThreadLocal<>();
	private final static ThreadLocal< Amf3Output > amf3Output = new ThreadLocal<>();

	static {
		serializationContext.legacyCollection = true;
	}

	private static Amf3Input getAmf3Input() {
		Amf3Input input = amf3Input.get();
		if( input == null ) {
			input = new Amf3Input( serializationContext );
			amf3Input.set( input );
		}
		return input;
	}

	private static Amf3Output getAmf3Output() {
		Amf3Output output = amf3Output.get();
		if( output == null ) {
			output = new Amf3Output( serializationContext );
			amf3Output.set( output );
		}
		return output;
	}

	public static byte[] encodeBytes( Object obj ) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream( baos );
		Amf3Output amfout = getAmf3Output();
		amfout.setOutputStream( dos );
		amfout.writeObject( obj );
		dos.flush();
		amfout.reset();
		return baos.toByteArray();
	}
	
	public static Object decodeBytes( byte[] bytes ) throws Exception {
		InputStream is = new ByteArrayInputStream( bytes );
		Amf3Input amfinput = getAmf3Input();
		amfinput.setInputStream( is );
		Object obj = amfinput.readObject();
		amfinput.reset();
		return obj;
	}
	
	public static ByteBuf encode( Object obj ) throws Exception {
		byte[] messageBytes = encodeBytes( obj );
		return Unpooled.wrappedBuffer( messageBytes );
	}
	
	public static Object decode( ByteBuf byteBuf ) throws Exception {
		byte[] bytes = new byte[byteBuf.readableBytes()];
		byteBuf.readBytes( bytes );
		return decodeBytes( bytes );
	}
}
