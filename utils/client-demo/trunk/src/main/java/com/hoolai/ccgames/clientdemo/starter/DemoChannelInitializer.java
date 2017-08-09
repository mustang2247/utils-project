/**
 * Author: guanxin
 * Date: 2015-07-24
 */

package com.hoolai.ccgames.clientdemo.starter;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import com.hoolai.ccgames.skeleton.codec.CodecFactory;
import com.hoolai.ccgames.skeleton.codec.CodecName;

public class DemoChannelInitializer extends
		ChannelInitializer< SocketChannel > {

	@Override
	protected void initChannel( SocketChannel ch ) throws Exception {

		ch.pipeline()
				.addLast( "FrameDecoder", new LengthFieldBasedFrameDecoder( 64 * 1024, 0, 4, 0, 4 ) )
				.addLast( "ProtobufDecoder", CodecFactory.getDecoder( CodecName.PROTOBUF ) )
				.addLast( "FrameEncoder", new LengthFieldPrepender( 4 ) )
				.addLast( "ProtobufEncoder", CodecFactory.getEncoder( CodecName.PROTOBUF ) )
				.addLast( "MessageHandler", new MessageHandler() );

	}

}
