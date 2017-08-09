package com.bitop.common.skeleton.codec.json;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import com.bitop.common.skeleton.dispatch.NetMessage;

public class JsonEncoder extends MessageToByteEncoder< NetMessage > {

	@Override
	public void encode( ChannelHandlerContext ctx, NetMessage msg,
			ByteBuf out ) throws Exception {
		out.writeInt( msg.getKindId() );
		out.writeInt( msg.getMsgId() );
		out.writeBytes( JsonUtil.encodeBytes( msg.getMessage() ) );
	}

}
