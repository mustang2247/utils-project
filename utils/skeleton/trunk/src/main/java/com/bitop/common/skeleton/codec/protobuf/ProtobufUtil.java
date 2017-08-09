package com.bitop.common.skeleton.codec.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ProtobufUtil {

    public static < E extends MessageLite > E decode( ByteBuf byteBuf, E inst ) throws InvalidProtocolBufferException {
        byte[] data = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes( data );
        return decodeBytes( data, inst );
    }

    public static ByteBuf encode( MessageLiteOrBuilder msg ) {
        return Unpooled.wrappedBuffer( encodeBytes( msg ) );
    }

    public static < E extends MessageLite > E decodeBytes( byte[] data, E inst ) throws InvalidProtocolBufferException {
        return (E) ( (MessageLite) inst ).getParserForType().parseFrom( data );
    }

    public static byte[] encodeBytes( MessageLiteOrBuilder msg ) {
        if( msg instanceof MessageLite ) {
            return ( (MessageLite) msg ).toByteArray();
        }
        if( msg instanceof MessageLite.Builder ) {
            ( (MessageLite.Builder) msg ).build().toByteArray();
        }
        return null;
    }

}
