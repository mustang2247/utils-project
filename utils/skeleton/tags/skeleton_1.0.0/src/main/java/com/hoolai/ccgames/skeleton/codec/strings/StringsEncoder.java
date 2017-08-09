package com.hoolai.ccgames.skeleton.codec.strings;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import com.hoolai.ccgames.skeleton.dispatch.NetMessage;

public class StringsEncoder extends MessageToByteEncoder< NetMessage > {

	@Override
	public void encode( ChannelHandlerContext ctx, NetMessage msg,
			ByteBuf out ) throws Exception {
		out.writeInt( msg.getKindId() );
		out.writeInt( msg.getMsgId() );
		StringsMessage message = (StringsMessage) msg.getMessage();
		out.writeBytes( message.toBytes() );
	}

}
