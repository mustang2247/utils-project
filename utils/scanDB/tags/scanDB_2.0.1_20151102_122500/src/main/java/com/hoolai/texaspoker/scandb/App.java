package com.hoolai.texaspoker.scandb;

import java.util.Properties;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;

import org.apache.log4j.PropertyConfigurator;

import com.hoolai.ccgames.center.repo.GlobalRepo;
import com.hoolai.ccgames.center.repo.UserRepo;
import com.hoolai.ccgames.center.utils.PropUtil;
import com.hoolai.texaspoker.bi.BICollector;
import com.hoolai.texaspoker.bi.BIManager;
import com.hoolai.texaspoker.config.GlobalConfig;
import com.hoolai.texaspoker.datatype.BasicInfo;
import com.hoolai.texaspoker.datatype.Clan;
import com.hoolai.texaspoker.datatype.ClanMember;
import com.hoolai.texaspoker.datatype.ExpInfo;
import com.hoolai.texaspoker.datatype.ItemList;
import com.hoolai.texaspoker.datatype.LimitsAndConsts;
import com.hoolai.texaspoker.datatype.SuperVIP;
import com.hoolai.texaspoker.datatype.TopPlayer;
import com.hoolai.texaspoker.datatype.TopPlayerList;
import com.hoolai.texaspoker.datatype.VIPInfo;

public class App 
{
    public static void main( String[] args )
    {
    	try {
    		if (args.length == 0) {
        		System.err.println("USAGE [GI] (G for gold) | (I for item)");
        		return;
        	}
        	
        	Properties properties = PropertiesReader.create("log4j_common.properties");
        	PropertyConfigurator.configure(properties);
        	
        	ContextUtil.init();
        	
        	boolean rptGold = args[0].contains( "G" );
        	boolean rptItem = args[0].contains( "I" );
        	int     topn = 20;
        	if( args.length > 1 && args[1].startsWith( "TOP" ) ){
        		topn = Integer.parseInt( args[1].substring( 3 ) );
        	}
        	TopPlayerList  tpl = new TopPlayerList( topn );
        	MinHeap<TopPlayer>  heap = new MinHeap<TopPlayer>( tpl.players );
        	
        	initializeDBConfig();
        	BIManager.getInstance().init();
        	
        	// 这个是胡莱德州扑克玩法的数据库
        	DaoInterface client = DaoService.getInstance();
        	
        	// 下面这个是用户中心数据库
        	String databaseAddr = PropUtil.getProp( GlobalConfig.CGATE_CONFIG_URL, "databaseAddr" );
        	String platformName = PropUtil.getProp( GlobalConfig.CGATE_CONFIG_URL, "platformName" );
        	String applicationID = PropUtil.getProp( GlobalConfig.CGATE_CONFIG_URL, "applicationID" );
        	
        	MemcachedClient mcNew = new MemcachedClient(
					new ConnectionFactoryBuilder().setProtocol( Protocol.TEXT )
							.build(),
					AddrUtil.getAddresses( databaseAddr ) );
        	
        	UserRepo.getInstance().setMC( mcNew );
			GlobalRepo.getInstance().setMC( mcNew );
        	
			long uuidBegin = Long.parseLong( (String) mcNew.get( GlobalRepo.getInstance()
					.getUUIDBeginKey( platformName, applicationID ) ) );
			long uuidEnd = Long.parseLong( (String) mcNew.get( GlobalRepo.getInstance()
					.getUUIDEndKey( platformName, applicationID ) ) );
					
        	
    		for (long ii = uuidBegin; ii <= uuidEnd; ++ii) {
    			try {
    				// 以后统一用long表示内部userID
    				// 但是texaspoker之前用的int，并且分配给它的uuid范围不会超过int
    				// 所以这里做了个强转
    				// 将来别的玩法都应该直接用long
    				int i = (int)ii;
    				
    				// 因为是直接操作数据库，目前还是单线程，离数据库瓶颈还差很远
    				// 没必要再暂停了
//        			if( i % 1000 == 0 ) {
//        				try {
//        					Thread.sleep( 1000 );
//        				} catch (InterruptedException e) {
//        					Log.e( e.getMessage(), e );
//        				}
//        			}
        			
        			String basicKey = String.format( "U%x-6", i );
        			String basicValue = (String) client.load( basicKey );
        			String expKey = String.format( "U%x-0", i );
        			String expValue = (String) client.load( expKey );
        			String vipKey = String.format( "U%x-7", i );
        			String vipValue = (String) client.load( vipKey );
        			
        			Log.i( "[UserID:" + i + "] " + basicValue );
        			Log.i( "[UserID:" + i + "] " + expValue );
        			
        			if( basicValue != null && expValue != null ) {
        				BasicInfo  basicInfo = JsonUtil.fromJson( basicValue, BasicInfo.class );
        				if( basicInfo == null || basicInfo.platform_id == null ) {
        					continue;
        				}
        				Clan clan = null;
        				if (basicInfo.team_id != null && basicInfo.team_id != LimitsAndConsts.UNKNOWN_ID) {
        					String clanKey = String.format("clan-%d", basicInfo.team_id);
        					String clanValue = (String) client.load(clanKey);
        					clan = JsonUtil.fromJson(clanValue, Clan.class);
        					Log.i( "[UserID:" + i + "] " + clanValue );
        				}
        				
        				String[] plat = basicInfo.platform_id.split( "\\-" );
        				if( !plat[0].equals( BIManager.platform ) ) continue;
        				String  platID = plat[1];
        				//String  platID = "1";
        				
        				if( rptGold ) {
        					
        					long gold = UserRepo.getInstance().getGold( i );
        					long hoolaiGold = UserRepo.getInstance().getHoolaiGold( i );
        					
        					Log.i( "[UserID:" + i + "] gold:" + gold + "  hoolai:" + hoolaiGold );
        					
        					ExpInfo expInfo = JsonUtil.fromJson( expValue, ExpInfo.class );
        					
        					VIPInfo vipInfo = JsonUtil.fromJson( vipValue, VIPInfo.class );
        					
        					int code = 0;
        					if (clan != null) {
        						ClanMember member = clan.members.get(i);
        						if (member != null) {
        							code = member.No != LimitsAndConsts.UNKNOWN_INT ? 1 : 0; // 说明是领队
        						}
        					}
        					
        					// 这段纯粹为了读取保险箱，而保险箱已经移除了
//        					ItemList   itemList = JsonUtil.fromJson( itemValue, ItemList.class );
//        					long chip=0L;
//        					if(itemList.safeBox!=null){
//        						chip=itemList.safeBox.getChip();
//        					}
        					BICollector.onSendGoldInfo( platID, basicInfo,
        							String.valueOf( gold ),
        							hoolaiGold,
        							String.valueOf( expInfo.level ),String.valueOf(i),code,0);
        					
        					if(tpl.players[topn]!=null){
            					tpl.players[topn].setValue((int)i, basicInfo.user_name, 
            							basicInfo.head_img_url,
            							gold,
            							(int)expInfo.level,
            							(int)vipInfo.vip_now );
        					}
        					heap.push_heap( topn );
        				}
        				//2014-6-10 。BI要求注释掉。
//        				if( rptItem ) {
//        					ItemList   itemList = JsonUtil.fromJson( itemValue, ItemList.class );
//        					for( ItemDesc  item : itemList.item_packs ) {
//        						BICollector.onSendItemInfo( platID,
//        									String.valueOf( item.item_id ),
//        									String.valueOf( item.count ) );
//        					}
//        				}
        			}else {
        				Log.e("[ScanDB] can't find userinfo " + i );
        			}
				} catch (Exception e) {
					Log.e("[ScanDB] Exception " + ii,e);
				}
    		}
    		heap.sort_heap( topn );
    		client.save( "TOP_PLAYERS", JsonUtil.toJson( tpl ) );
    	} catch (Exception e) {
    		Log.e(e.getMessage(), e);
    	}
		System.exit( 0 );
    }
    
    private static void initializeDBConfig() {
		Log.i("Initializing DBConfig...");
		Properties db = PropertiesReader.create(GlobalConfig.DAO_CONFIG_URL);
		String db_type = db.getProperty("type");
		if (db_type.equals("memcached")) {
			DaoService.setInstance(new DaoMemcached((ExtendedMemcachedClientImpl) ContextUtil.getBean("memPool")));
			Object orderObj = DaoService.getInstance().load("PHONEORDER");
			if (orderObj == null || (Long.parseLong((String) orderObj)) < 10000) {
				DaoService.getInstance().save("PHONEORDER", "9999");
			}
		} else {
			DaoService.setInstance(new DaoRedis((JedisPool)ContextUtil.getBean("jedisPool")));
			Object orderObj = DaoService.getInstance().load("PHONEORDER");
			if (orderObj == null || (Long.parseLong((String) orderObj)) < 10000) {
				DaoService.getInstance().save("PHONEORDER", "9999");
			}
		}
		Log.i("Initializing DBConfig complete!");
	}
}
