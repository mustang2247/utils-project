/**
 * 
 */
package com.hoolai.texaspoker.bi;

import java.text.SimpleDateFormat;
import java.util.Properties;

import com.dw.services.TrackServices;
import com.hoolai.texaspoker.config.GlobalConfig;
import com.hoolai.texaspoker.scandb.Log;
import com.hoolai.texaspoker.scandb.PropertiesReader;

/**
 * @author Cedric(TaoShuang)
 * @create 2012-6-6
 */
public final class BIManager {
	private static final BIManager INSTANCE = new BIManager();
	public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static boolean isTrackingON;
	public static boolean needTransfer;
	public static String platform;
	private BIManager() {
		
	}
	public static BIManager getInstance() {
		return INSTANCE;
	}
	
	public void init() {
		Properties props = PropertiesReader.create(GlobalConfig.BI_CONFIG_URL);
		String sn_id = props.getProperty("sn_id");
		String client_id = props.getProperty("client_id");
		String game_id = props.getProperty("game_id");
		String scribed_host = props.getProperty("scribed_host");
		int core_pool_size = Integer.parseInt(props.getProperty("core_pool_size"));
		int max_pool_size = Integer.parseInt(props.getProperty("max_pool_size"));
		int queue_capacity = Integer.parseInt(props.getProperty("queue_capacity"));
		isTrackingON = Boolean.parseBoolean(props.getProperty("isBiTrackingOpen"));
		needTransfer = Boolean.parseBoolean(props.getProperty("need_transfer"));
		platform = props.getProperty("platform");
		Log.i("BIManager::init() " + platform);
		TrackServices.init(sn_id, client_id, game_id, scribed_host, core_pool_size, max_pool_size, queue_capacity);
	}
}
