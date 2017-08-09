package com.hoolai.ccgames.netty;

import com.google.protobuf.ExtensionRegistry;
import com.hoolai.ccgames.protocol.Auth;
import com.hoolai.ccgames.protocol.Command;
import com.hoolai.ccgames.protocol.Error;
import com.hoolai.ccgames.protocol.Info;
import com.hoolai.ccgames.protocol.UserModify;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class ProtobufClientInitializer extends
		ChannelInitializer< SocketChannel > {

	@Override
	public void initChannel( SocketChannel ch ) {
		ExtensionRegistry registry = ExtensionRegistry.newInstance();
		Command.registerAllExtensions( registry );
		Auth.registerAllExtensions( registry );
		UserModify.registerAllExtensions( registry );
		Error.registerAllExtensions( registry );
		Info.registerAllExtensions( registry );

		ChannelPipeline p = ch.pipeline();

		p.addLast( new ProtobufVarint32FrameDecoder() );
		p.addLast( new ProtobufDecoder( Command.BaseCommand
				.getDefaultInstance(), registry ) );

		p.addLast( new ProtobufVarint32LengthFieldPrepender() );
		p.addLast( new ProtobufEncoder() );

		p.addLast( "MessageHandler", new ProtobufClientHandler() );
	}
}
