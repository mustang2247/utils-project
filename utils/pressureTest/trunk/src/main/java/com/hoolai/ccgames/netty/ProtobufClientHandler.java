package com.hoolai.ccgames.netty;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import com.hoolai.ccgames.protocol.Command;
import com.hoolai.ccgames.protocol.Command.BaseCommand;

public class ProtobufClientHandler extends SimpleChannelInboundHandler<Command.BaseCommand> {

	public final static Logger logger = LoggerFactory.getLogger( ProtobufClientHandler.class );
	
	public static int counter = 0;
	
	public List< Long >  rcv = new LinkedList< Long >();
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, BaseCommand msg)
			throws Exception {
		
		// logger.debug( "receive msg {}", msg );
		// logger.debug( "{}", ++counter );
		rcv.add( System.currentTimeMillis() );
		
		/*CommandType type = msg.getType();
		switch (type) {
			case SET_USER_ITEM: {
				UserItemStatus userItemStatus = msg
						.getExtension( UserModify.UserItemStatus.cmd );
				logger.debug( userItemStatus.toString() );
			}
				break;
			case ERR_CMD: {
				ErrorMsg errMsg = msg.getExtension( ErrorMsg.cmd );
				logger.error( "Error: " + errMsg.getErrCode() );
			}
				break;
			default:
				logger.error( "Command not support {}", msg );
		}*/
	}

}
