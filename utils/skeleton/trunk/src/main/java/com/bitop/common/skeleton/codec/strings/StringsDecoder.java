package com.bitop.common.skeleton.codec.strings;

import java.util.List;

import com.bitop.common.skeleton.dispatch.NetMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class StringsDecoder extends ByteToMessageDecoder {

	@Override
	public void decode( ChannelHandlerContext ctx, ByteBuf msg,
			List< Object > out ) throws Exception {
		
		int kindId = msg.readInt();
		int msgId = msg.readInt();
		byte[] bytes = new byte[msg.readableBytes()];
		msg.readBytes( bytes );
		StringsMessage message = StringsMessage.parseFrom( bytes );
		out.add( new NetMessage( kindId, msgId, message ) );
	}
	
	@Override
	public void decodeLast( ChannelHandlerContext ctx, ByteBuf msg,
			List< Object > out ) throws Exception {
		// do nothing
	}

}
