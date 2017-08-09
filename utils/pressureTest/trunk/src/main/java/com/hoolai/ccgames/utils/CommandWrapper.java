/**
 * Author: guanxin
 * Date: 2015-07-27
 */

package com.hoolai.ccgames.utils;

import com.google.protobuf.GeneratedMessage;
import com.hoolai.ccgames.config.ServerConfig;
import com.hoolai.ccgames.protocol.Command.BaseCommand.CommandType;
import com.hoolai.ccgames.protocol.Command.BaseCommand;
import com.hoolai.ccgames.protocol.common.CodecUtil;

public class CommandWrapper {

	public static < Type > BaseCommand wrap( int msgID, CommandType type,
			GeneratedMessage.GeneratedExtension< BaseCommand, Type > extension,
			Type msg ) {
		
		int  prop = 0;
		prop = CodecUtil.setVer( prop, ServerConfig.messageVersion );
		
		return BaseCommand.newBuilder()
				.setMsgId( msgID )
				.setType( type )
				.setProps( prop )
				.setExtension( extension, msg )
				.build();
	}
}
