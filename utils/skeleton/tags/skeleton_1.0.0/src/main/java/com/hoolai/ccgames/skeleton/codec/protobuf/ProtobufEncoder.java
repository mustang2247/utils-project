package com.hoolai.ccgames.skeleton.codec.protobuf;

import com.google.protobuf.MessageLiteOrBuilder;
import com.hoolai.ccgames.skeleton.dispatch.NetMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ProtobufEncoder extends MessageToByteEncoder< NetMessage > {

    @Override
    public void encode( ChannelHandlerContext ctx, NetMessage msg,
                        ByteBuf out ) throws Exception {
        out.writeInt( msg.getKindId() );
        out.writeInt( msg.getMsgId() );
        out.writeBytes( ProtobufUtil.encodeBytes( (MessageLiteOrBuilder) msg.getMessage() ) );
    }

}
