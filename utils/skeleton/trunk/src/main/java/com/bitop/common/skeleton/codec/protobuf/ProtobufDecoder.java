package com.bitop.common.skeleton.codec.protobuf;

import com.bitop.common.skeleton.dispatch.CommandProperties;
import com.bitop.common.skeleton.dispatch.CommandRegistry;
import com.bitop.common.skeleton.dispatch.NetMessage;
import com.google.protobuf.MessageLite;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * protobuf 解码
 */
public class ProtobufDecoder extends ByteToMessageDecoder {

    private CommandRegistry registry;

    public ProtobufDecoder(CommandRegistry registry ) {
        this.registry = registry;
    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf msg,
                       List< Object > out ) throws Exception {

        int kindId = msg.readInt();
        int msgId = msg.readInt();
        CommandProperties properties = registry.get( kindId );
        if( properties == null ) {
            throw new RuntimeException( "[ProtobufDecoder::decode] Can't find cmd " + kindId );
        }
        Object message = ProtobufUtil.decode( msg, (MessageLite) properties.remoteObjectInst );
        out.add( new NetMessage( kindId, msgId, message ) );
    }

    @Override
    public void decodeLast(ChannelHandlerContext ctx, ByteBuf msg,
                           List< Object > out ) throws Exception {
        // do nothing
    }

}
