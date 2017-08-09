package com.bitop.common.skeleton.codec.strings;

import com.bitop.common.skeleton.dispatch.NetMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class StringsEncoder extends MessageToByteEncoder<NetMessage> {

	@Override
	public void encode( ChannelHandlerContext ctx, NetMessage msg,
			ByteBuf out ) throws Exception {
		out.writeInt( msg.getKindId() );
		out.writeInt( msg.getMsgId() );
		StringsMessage message = (StringsMessage) msg.getMessage();
		out.writeBytes( message.toBytes() );
	}

}
