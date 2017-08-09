/**
 * Author: guanxin
 * Date: 2015-08-03
 */

package com.hoolai.ccgames.config;

import com.google.protobuf.ExtensionRegistry;
import com.hoolai.ccgames.protocol.Auth;
import com.hoolai.ccgames.protocol.Command;
import com.hoolai.ccgames.protocol.HB;
import com.hoolai.ccgames.protocol.Info;
import com.hoolai.ccgames.protocol.UserModify;
import com.hoolai.ccgames.protocol.Error;

//@formatter:off
/**
 * 为了能够正确解析，需要所有的protobuf的大类在这里注册
 */
//@formatter:on

public class CommandConfig {

	public static ExtensionRegistry registry = ExtensionRegistry.newInstance();

	public static boolean init() {

		Command.registerAllExtensions( registry );
		Auth.registerAllExtensions( registry );
		Error.registerAllExtensions( registry );
		HB.registerAllExtensions( registry );
		Info.registerAllExtensions( registry );
		UserModify.registerAllExtensions( registry );

		return true;
	}

}
