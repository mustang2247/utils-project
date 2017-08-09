package com.hoolai.texaspoker.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hoolai.centersdk.sdk.AppSdk;
import com.hoolai.centersdk.utils.ContextUtil;
import com.hoolai.texaspoker.config.Config;

public class Starter {

	private static final Logger logger = LoggerFactory.getLogger(Starter.class);

	public static void main(String[] args) {
		if(args==null || args.length<1){
			logger.error("APPID NO FIND");
			System.err.println("APPID NO FIND");
			return;
		}
		String appId = args[0].trim();
		if(!AppSdk.checkAppId(appId)){
			logger.error("checkAppId no pass appId="+appId);
			System.err.println("checkAppId no pass appId="+appId);
			return;
		}
		Config.appId = appId;
		//JVM
		printJvmInfo();
		// spring
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		ContextUtil.hold(context);
		DianWanBaService dianWanBaService = ContextUtil.getBean("dianWanBaService",DianWanBaService.class);
		dianWanBaService.change();
		System.exit(0);
	}

	private static void printJvmInfo() {
		logger.info(" Free memory: "+formatSize(new Long(Runtime.getRuntime().freeMemory()), true));
		logger.info(" Total memory: "+formatSize(new Long(Runtime.getRuntime().totalMemory()), true));
		logger.info(" Max memory: "+formatSize(new Long(Runtime.getRuntime().maxMemory()), true) );
	}
	
	private static String formatSize(Object obj, boolean mb) {
		long bytes = -1L;
		if (obj instanceof Long)
			bytes = ((Long) obj).longValue();
		else if (obj instanceof Integer) {
			bytes = ((Integer) obj).intValue();
		}
		if (mb) {
			long mbytes = bytes / 1048576L;
			long rest = (bytes - mbytes * 1048576L) * 100L / 1048576L;
			return mbytes + "." + ((rest < 10L) ? "0" : "") + rest + " m";
		}
		return bytes / 1024L + " k";
	}
}
