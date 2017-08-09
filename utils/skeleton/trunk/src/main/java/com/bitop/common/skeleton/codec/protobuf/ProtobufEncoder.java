package com.bitop.common.skeleton.codec.protobuf;

import com.bitop.common.skeleton.dispatch.NetMessage;
import com.google.protobuf.MessageLiteOrBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * protobuf 编码
 */
public class ProtobufEncoder extends MessageToByteEncoder<NetMessage> {

    @Override
    public void encode( ChannelHandlerContext ctx, NetMessage msg,
                        ByteBuf out ) throws Exception {
        out.writeInt( msg.getKindId() );
        out.writeInt( msg.getMsgId() );
        out.writeBytes( ProtobufUtil.encodeBytes( (MessageLiteOrBuilder) msg.getMessage() ) );
    }

}
