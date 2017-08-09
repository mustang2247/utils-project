/**
 * Author: guanxin
 * Date: 2015-07-21
 */

package com.hoolai.ccgames.center.repo;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;


// @formatter:off
/**
 * 全局Repo
 *  1 维护所有用户的ID分配
 *  2 生成用户ID后，业务端需要调用UserRepo的方法自动生成所有的属性，初始化为空
 *  !!! 业务层需要注意，getUserID只负责返回已有的，newUserID负责生成新的
 */
// @formatter:on

public class GlobalRepo {
	
	private static Logger logger = LoggerFactory.getLogger( GlobalRepo.class );

	private DaoPool pool;
	
	private static final GlobalRepo INSTANCE = new GlobalRepo();
	
	// id区间混用
	// 因为这个改动上线时，已经有4个appID上线了，他们公用一个UUID生成，混在一起，所以
	// 这里做一个映射，遇到这4个appID，统一映射为一个appID
	private Map< String, String > old4 = new HashMap< String, String >();

	private GlobalRepo() {
		// 在创建一个应用时，需要手动添加UUID范围
		// dbClient.add( getUUIDEndKey( null, null ), 0, "0" );
		// old4.put( "100632434", "100632434" );
		old4.put( "1104830871", "100632434" );
		old4.put( "1104737759", "100632434" );
		old4.put( "1104754063", "100632434" );
		pool = DaoFactory.getDaoPool();
	}

	public static GlobalRepo getInstance() {
		return INSTANCE;
	}

	public String getUserKey( String platform, String appID, String openID ) {
		return new StringBuilder( 64 ).append( platform ).append( ':' )
				.append( appID ).append( ':' ).append( openID ).toString();
	}
	

	public String getUUIDBeginKey( String platform, String appID ) {
		if( platform.equals( "TENCENT" ) && old4.containsKey( appID ) )
			appID = old4.get( appID );
		
		return new StringBuilder( 64 ).append( "UUID:" ).append( platform )
				.append( ':' ).append( appID ).append( ":L" ).toString();
	}
	
	/**
	 * @param platform
	 * @param appID
	 * @return 生成新uid的key
	 */
	public String getUUIDEndKey( String platform, String appID ) {
		if( platform.equals( "TENCENT" ) && old4.containsKey( appID ) )
			appID = old4.get( appID );
		
		return new StringBuilder( 64 ).append( "UUID:" ).append( platform )
				.append( ':' ).append( appID ).append( ":R" ).toString();
	}
	
//	public RangeUUID getUUIDRange( String platform, String appID ) {
//		
//		MemcachedClient dbClient = pool.getClient();
//		
//		try {
//    		RangeUUID rv = new RangeUUID( Constants.INVALID_UID, Constants.INVALID_UID );
//    		Object obj = dbClient.get( getUUIDBeginKey( platform, appID ) );
//    		if( obj != null )
//    			rv.begin = Long.parseLong( (String) obj );
//    		obj = dbClient.get( getUUIDEndKey( platform, appID ) );
//    		if( obj != null )
//    			rv.end = Long.parseLong( (String) obj );
//    		return rv;
//		}
//		finally {
//			pool.releaseClient( dbClient );
//		}
//	}
	
	/**
	 * @param platform
	 *            平台名称
	 * @param appID
	 *            app ID
	 * @return 检测这两个参数是否是可识别的，必须是可识别的才可以入库
	 */
	private boolean checkParamValid( String platform, String appID ) {
//		return DataPool.exist( platform, appID );
		return true;
	}
	
	private boolean saveInfo( long uid, String info ) {
		
		MemcachedClient dbClient = pool.getClient();
		
		try {
    		String key = "User:" + uid;
    		boolean rv = false;
    		try {
    			rv = dbClient.add( key, 0, info ).get().booleanValue();
    		}
    		catch( Exception e ) {
    			logger.error( e.getMessage(), e );
    		}
    		return rv;
		}
		finally {
			pool.releaseClient( dbClient );
		}
	}

	/**
	 * @param platform
	 *            平台名称
	 * @param appID
	 *            app ID
	 * @param openID
	 *            用户在该app下的openID
	 * @return 生成全局的uid，如果失败，返回Constants.INVALID_UID
	 */
	public long newUserID( String platform, String appID, String openID,
			long oldUserID ) {
		
		MemcachedClient dbClient = pool.getClient();
		
		try {

    		long uid = Constants.INVALID_UID;
    		if( !checkParamValid( platform, appID ) )
    			return uid;
    
    		long newID = -1;
    		if( oldUserID != Constants.INVALID_UID ) {
    			newID = oldUserID;
    		}
    		else {
    			String key = getUUIDEndKey( platform, appID );
    			newID = dbClient.incr( key, 1 );
    		}
    
    		/**
    		 * 如果成功将该AppID对应的Seed加1，即创建了一个新的uid，这时需要将(platform+appID+openID)->uid
    		 * 的映射关系放入数据库，因为是新加数据，我们调用add方法 如果add失败，那么有两种可能： 1 数据库服务器未正常工作 2
    		 * 该key已经存在于数据库，add方法可以避免将这个key覆盖 不过也会有一个问题，就是新生成的uid浪费了，这可能无法避免
    		 * 这讨论的是极端情况，现实中应该极少发生
    		 */
    		if( newID != -1 ) {
    			// 存储正向映射关系（从平台信息查找uid）
    			String key = getUserKey( platform, appID, openID );
    			OperationFuture< Boolean > rv = dbClient.add( key, 0, newID );
    			try {
    				if( rv.get().booleanValue() ) {
    					uid = newID;
    					// 存储反向映射关系（从uid查找平台信息）
    					saveInfo( newID, key );
    				}
    			}
    			catch( Exception e ) {
    				logger.error( e.getMessage(), e );
    			}
    		}
    		return uid;
		}
		finally {
			pool.releaseClient( dbClient );
		}
	}

	/**
	 * @param platform
	 *            平台名称
	 * @param appID
	 *            app ID
	 * @param openID
	 *            用户在该app下的openID
	 * @return 已经储存在数据库中的ID，如果失败，返回Constants.INVALID_UID
	 */
	public long getUserID( String platform, String appID, String openID ) {
		
		MemcachedClient dbClient = pool.getClient();
		
		try {
    		long uid = Constants.INVALID_UID;
    		if( !checkParamValid( platform, appID ) )
    			return uid;
    
    		String key = getUserKey( platform, appID, openID );
    		Object obj = dbClient.get( key );
    		if( obj != null )
    			uid = ( (Long) obj ).longValue();
    
    		return uid;
    	}
    	finally {
    		pool.releaseClient( dbClient );
    	}
	}
}
