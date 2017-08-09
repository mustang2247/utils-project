package com.hoolai.ccgames.skeleton.codec.amf3;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import com.hoolai.ccgames.skeleton.dispatch.NetMessage;

public class Amf3Encoder extends MessageToByteEncoder< NetMessage > {

	@Override
	public void encode( ChannelHandlerContext ctx, NetMessage msg,
			ByteBuf out ) throws Exception {
		out.writeInt( msg.getKindId() );
		out.writeInt( msg.getMsgId() );
		out.writeBytes( Amf3Util.encodeBytes( msg.getMessage() ) );
	}

}
