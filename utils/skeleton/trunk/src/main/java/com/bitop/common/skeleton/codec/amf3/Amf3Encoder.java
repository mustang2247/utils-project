package com.bitop.common.skeleton.codec.amf3;

import com.bitop.common.skeleton.dispatch.NetMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Amf3Encoder extends MessageToByteEncoder<NetMessage> {

	@Override
	public void encode( ChannelHandlerContext ctx, NetMessage msg,
			ByteBuf out ) throws Exception {
		out.writeInt( msg.getKindId() );
		out.writeInt( msg.getMsgId() );
		out.writeBytes( Amf3Util.encodeBytes( msg.getMessage() ) );
	}

}
