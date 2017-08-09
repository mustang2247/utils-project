package com.bitop.common.skeleton.codec.json;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import com.bitop.common.skeleton.dispatch.CommandProperties;
import com.bitop.common.skeleton.dispatch.CommandRegistry;
import com.bitop.common.skeleton.dispatch.NetMessage;

public class JsonDecoder extends ByteToMessageDecoder {

	private CommandRegistry registry;
	
	public JsonDecoder( CommandRegistry registry ) {
		this.registry = registry;
	}
	
	@Override
	public void decode( ChannelHandlerContext ctx, ByteBuf msg,
			List< Object > out ) throws Exception {
		
		int kindId = msg.readInt();
		int msgId = msg.readInt();
		CommandProperties properties = registry.get( kindId );
		if( properties == null ) throw new RuntimeException( "[JsonDecoder::decode] Can't find cmd " + kindId );
		Object message = JsonUtil.decode( msg, properties.remoteObjectClass );
		out.add( new NetMessage( kindId, msgId, message ) );
	}
	
	@Override
	public void decodeLast( ChannelHandlerContext ctx, ByteBuf msg,
			List< Object > out ) throws Exception {
		// do nothing
	}

}
