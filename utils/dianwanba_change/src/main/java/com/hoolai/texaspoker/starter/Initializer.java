package com.hoolai.texaspoker.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.hoolai.centersdk.repo.DaoManager;
import com.hoolai.centersdk.sdk.ItemSdk;
import com.hoolai.texaspoker.bi.NewBICollector;
import com.hoolai.texaspoker.config.GlobalConfig;

@Component
public class Initializer implements InitializingBean{
	
	private static final Logger logger = LoggerFactory.getLogger(Initializer.class);

	public void afterPropertiesSet() throws Exception {
		//cgate init
		DaoManager.init(GlobalConfig.CENTER_GATE_URL);
		//bi init
		NewBICollector.init(GlobalConfig.BIENTER_GATE_URL);
		//sdk init
		ItemSdk.init(DaoManager.centerRepo);
		logger.info("Initializer ok!");
	}
}
