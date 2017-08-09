/**
 * 
 */
package com.hoolai.texaspoker.config;

import com.hoolai.texaspoker.scandb.Log;

/**
 * @author Cedric(TaoShuang)
 * @create 2012-3-1
 */
public class GlobalConfig {
	public static final String CONFIG_URL = "config.properties";
	public static final String SPRING_CONTEXT_URL = "applicationContext.xml";
	public static final String SPRING_CONTEXT_URL_FOR_TEST = "capplicationContext.xml";
//	public static final String CLIENT_REQUESTS_XML_URL = "src/main/java/clientRequestCommands.xml";
	public static final String CLIENT_REQUESTS_XML_URL = "clientRequestCommands.xml";
	public static final String BASIC_ITEM_TEMPLATE_RESOURCE_XML_URL = "basic_item_template.xml";
	public static final String ITEM_COMMON_XML_URL = "item_common.xml";
	public static final String CLAN_THRESHOLD_URL = "clan_threshold_template.xml";
	public static final String CLAN_LEVEL_URL = "clan_level_template.xml";
	public static String DAO_CONFIG_URL = null;
	public static final String DESK_CONFIG_URL = "desk_config.xml";
	public static final String MATCH_CONFIG_URL = "match_config.xml";
	public static final String PROCESS_CONFIG_URL = "process_config.xml";
	public static final String GAME_CONFIG_URL = "game_config.xml";
	public static final String ITEM_CONFIG_URL = "item_config.xml";
	public static final String BACKYARD_CONFIG_URL = "backyard.xml";
	public static String PLATFORM_CONFIG_URL = null;
	public static final String ACHIEVEMENT_CONFIG_URL = "achievements_template.xml";
	public static final String FAKE_SUPPORT_URL = "fake_friends_list.xml";
	public static final String QUEST_TEMPLATE_URL = "quest_template.xml";
	public static final String DAILY_QUEST_CONFIG_URL = "daily_quest_template.properties";
	public static String BI_CONFIG_URL = null;
	public static String CTU_CONFIG_URL = "ctu.properties";
	public static String CGATE_CONFIG_URL = "cgate.properties";
	
	public static void init(String daoUrl, String bi_url, String dirName) {
		GlobalConfig.DAO_CONFIG_URL = daoUrl;
		GlobalConfig.BI_CONFIG_URL = dirName + "/" + bi_url;
		GlobalConfig.CGATE_CONFIG_URL = "/" + dirName + "/" + GlobalConfig.CGATE_CONFIG_URL;
		Log.i( "Read cgate path " + GlobalConfig.CGATE_CONFIG_URL );
	}
}
