package com.hoolai.ccgames.skeleton.codec.strings;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class StringsMessage {

	public static final String[] NOT_NULL = new String[0];
	public static final Charset CHARSET = Charset.forName( "utf-8" );
	public static final int MAX_CAPACITY = 4096;
	public static final byte SPLITTER = 0;
	
	public String[] values = NOT_NULL;
	
	public StringsMessage() {}
	
	public StringsMessage( String ...args ) {
		this.values = args;
	}
	
	public StringsMessage( byte[] bs ) {
		fromBytes( bs );
	}
	
	public byte[] toBytes() throws UnsupportedEncodingException {
		// 消息参数个数不能大于127
		byte[] bs = new byte[MAX_CAPACITY];
		int pos = 0;
		bs[pos++] = (byte) values.length;
		
		for( int i = 0; i < values.length; ++i ) {
			byte[] tmp;
			tmp = values[i].getBytes( CHARSET );
			System.arraycopy( tmp, 0, bs, pos, tmp.length );
			pos += tmp.length;
			bs[pos++] = SPLITTER;
		}
		
		byte[] res = new byte[pos];
		System.arraycopy( bs, 0, res, 0, pos );

		return res;
	}
	
	public void fromBytes( byte[] bs ) {
		
		if( bs.length == 0 ) {
			throw new RuntimeException( "Invalid byte data, can't parse to CommonMessage" );
		}
		
		if( bs.length == 1 && bs[0] == 0 ) {
			values = NOT_NULL;
			return;
		}
		
		int pos = 0;
		int sz = bs[pos++];
		values = new String[sz];
		
		for( int i = 0; i < sz; ++i ) {
			int beg = pos;
			while( bs[pos] != SPLITTER ) ++pos;
			values[i] = new String( bs, beg, pos - beg, CHARSET );
			++pos;
		}
	}
	
	public static StringsMessage parseFrom( byte[] bs ) {
		return new StringsMessage( bs );
	}
	
	public static void main( String[] args ) throws UnsupportedEncodingException {
		
		StringsMessage cm = new StringsMessage( "haha", "你好", "123.045" );
		
		byte[] bs = cm.toBytes();
		System.out.println( "bs len = " + bs.length );
		
		StringsMessage cm2 = StringsMessage.parseFrom( bs );
		
		for( String v : cm2.values ) {
			System.out.println( v );
		}
		
	} 
	
}
