package com.bitop.common.skeleton.codec;

import com.bitop.common.skeleton.codec.amf3.Amf3Decoder;
import com.bitop.common.skeleton.codec.json.JsonDecoder;
import com.bitop.common.skeleton.codec.mixed.MixedDecoder;
import com.bitop.common.skeleton.codec.amf3.Amf3Encoder;
import com.bitop.common.skeleton.codec.json.JsonEncoder;
import com.bitop.common.skeleton.codec.mixed.MixedEncoder;
import com.bitop.common.skeleton.codec.protobuf.ProtobufDecoder;
import com.bitop.common.skeleton.codec.protobuf.ProtobufEncoder;
import com.bitop.common.skeleton.codec.strings.StringsDecoder;
import com.bitop.common.skeleton.codec.strings.StringsEncoder;
import com.bitop.common.skeleton.dispatch.CommandRegistry;

import io.netty.channel.ChannelHandler;

public class CodecFactory {

	public static ChannelHandler getDecoder( CodecName codecName, CommandRegistry registry ) {
		switch( codecName ) {
			case MIXED: {
				return new MixedDecoder( registry );
			}
			case AMF3: {
				return new Amf3Decoder();
			}
			case PROTOBUF: {
				return new ProtobufDecoder( registry );
			}
			case JSON: {
				return new JsonDecoder( registry );
			}
			case STRINGS: {
				return new StringsDecoder();
			}
			default:
				throw new RuntimeException( "Not support decoder " + codecName );
		}
	}
	
	public static ChannelHandler getEncoder( CodecName codecName ) {
		switch( codecName ) {
			case MIXED: {
				return new MixedEncoder();
			}
			case AMF3: {
				return new Amf3Encoder();
			}
			case PROTOBUF: {
				return new ProtobufEncoder();
			}
			case JSON: {
				return new JsonEncoder();
			}
			case STRINGS: {
				return new StringsEncoder();
			}
			default:
				throw new RuntimeException( "Not support encoder " + codecName );
		}
	}
}
