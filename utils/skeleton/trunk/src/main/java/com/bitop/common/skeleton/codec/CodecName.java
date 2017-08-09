package com.bitop.common.skeleton.codec;

public enum CodecName {
	
	MIXED( 0 ),
	AMF3( 1 ),
	PROTOBUF( 2 ),
	JSON( 3 ),
	STRINGS( 4 );
	
	private int value;
	CodecName( int v ) {
		this.value = v;
	}
	
	public int getValue() {
		return this.value;
	}
}
