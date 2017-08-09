/**
 * Author: guanxin
 * Date: 2015-07-21
 */

package com.hoolai.ccgames.center.repo;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hoolai.ccgames.center.datatypes.ItemGiveList;
import com.hoolai.ccgames.center.datatypes.ItemGiveRecord;
import com.hoolai.ccgames.center.datatypes.ItemList;
import com.hoolai.ccgames.center.datatypes.ItemUnit;
import com.hoolai.ccgames.center.datatypes.Safebox;

import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

// @formatter:off
/**
 * 用户数据中心，提供如下功能
 *  1 存、取用户金币
 *  2 存、取用户道具
 *  3 存、取用户红利信息
 *  !!! 所有操作都是建立在key已经创建的前提上
 */
// @formatter:on

public class UserRepo {

	public final static Logger logger = LoggerFactory
			.getLogger( UserRepo.class );

	private MemcachedClient dbClient;

	private static final int MAX_RETRY = 10;
	private static final UserRepo INSTANCE = new UserRepo();
	private static ItemTranscoder ITEM_TC = new ItemTranscoder();
	private static SafeboxTranscoder SAFEBOX_TC = new SafeboxTranscoder();
	private static ItemGiveTranscoder ITEM_GIVE_TC = new ItemGiveTranscoder();

	private UserRepo() {
		// dbClient = DaoFactory.getDAO();
		// assert ( dbClient != null );
		// dbClient.add( getSeedKey( null, null ), 0, "0" );
	}
	
	public static UserRepo getInstance() {
		return INSTANCE;
	}
	
	public void setMC( MemcachedClient mc ) {
		dbClient = mc;
	}

	private String getGoldKey( long uID ) {
		return new StringBuilder().append( "Gold" ).append( ':' ).append( uID )
				.toString();
	}

	private String getHoolaiGoldKey( long uID ) {
		return new StringBuilder().append( "HGold" ).append( ':' ).append( uID )
				.toString();
	}

	private String getMasterPointKey( long uID ) {
		return new StringBuilder().append( "MPoint" ).append( ':' )
				.append( uID )
				.toString();
	}

	private String getHelpUsedKey( long uID ) {
		return new StringBuilder().append( "HelpUsed" ).append( ':' )
				.append( uID )
				.toString();
	}

	private String getItemKey( long uID ) {
		return new StringBuilder().append( "Item" ).append( ':' ).append( uID )
				.toString();
	}

	private String getItemUsedKey( long uID ) {
		return new StringBuilder().append( "ItemUsed" ).append( ':' )
				.append( uID )
				.toString();
	}

	private String getSafeboxKey( long uID ) {
		return new StringBuilder().append( "Safebox" ).append( ':' )
				.append( uID )
				.toString();
	}

	private String getItemGiveKey( long uID ) {
		return new StringBuilder().append( "ItemGive" ).append( ':' )
				.append( uID )
				.toString();
	}

	public boolean openAccount( long uID ) {

		boolean rv = true;

		try {
			String key = getGoldKey( uID );
			rv = dbClient.add( key, 0, 0L ).get().booleanValue();
			if( !rv )
				return rv;

			key = getHoolaiGoldKey( uID );
			rv = dbClient.add( key, 0, 0L ).get().booleanValue();
			if( !rv )
				return rv;

			key = getMasterPointKey( uID );
			rv = dbClient.add( key, 0, 0L ).get().booleanValue();
			if( !rv )
				return rv;

			key = getHelpUsedKey( uID );
			rv = dbClient.add( key, 0, System.currentTimeMillis() + ":0" )
					.get().booleanValue();
			if( !rv )
				return rv;

			key = getItemKey( uID );
			ItemList list = new ItemList();
			rv = dbClient.add( key, 0, list, ITEM_TC ).get().booleanValue();
			if( !rv )
				return rv;

			key = getItemUsedKey( uID );
			list = new ItemList();
			rv = dbClient.add( key, 0, list, ITEM_TC ).get().booleanValue();
			if( !rv )
				return rv;

//			key = getSafeboxKey( uID );
//			Safebox box = new Safebox();
//			rv = dbClient.add( key, 0, box, SAFEBOX_TC ).get().booleanValue();
//			if( !rv )
//				return rv;

			key = getItemGiveKey( uID );
			ItemGiveList giveList = new ItemGiveList();
			rv = dbClient.add( key, 0, giveList, ITEM_GIVE_TC ).get()
					.booleanValue();
			if( !rv )
				return rv;

			return rv;
		}
		catch( InterruptedException e ) {
			e.printStackTrace();
		}
		catch( ExecutionException e ) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * @param key
	 *            数据库key
	 * @return 成功返回long型值，否则返回-1
	 */
	private long getLongValue( String key ) {
		Object obj = dbClient.get( key );
		if( obj == null )
			return -1L;
		return ( (Long) obj ).longValue();
	}

	public long getGold( long uID ) {
		return getLongValue( getGoldKey( uID ) );
	}

	public long getHoolaiGold( long uID ) {
		return getLongValue( getHoolaiGoldKey( uID ) );
	}

	public long getMasterPoint( long uID ) {
		return getLongValue( getMasterPointKey( uID ) );
	}

	public long getHelpUsed( long uID ) {
		String val = (String) dbClient.get( getHelpUsedKey( uID ) );
		if( val == null )
			return -1L;

		long lastTime = Long.parseLong( val.split( ":" )[0] );
		long lastCount = Long.parseLong( val.split( ":" )[1] );
		long now = System.currentTimeMillis();

		return TimeUtil.isSameDay( lastTime, now ) ? lastCount : 0L;
	}

	/**
	 * @param key
	 *            数据库key
	 * @param change
	 *            变化值
	 * @return 该key对应的改变后的数据，变化结果不可以小于0，失败返回-1
	 */
	private long changeLongValue( String key, long change ) {
		for( int i = 0; i < MAX_RETRY; ++i ) {
			CASValue< Object > rv = dbClient.gets( key );
			if( rv == null )
				return -1L;
			long now = ( (Long) rv.getValue() ).longValue() + change;
			if( now < 0 )
				return -1L;

			if( CASResponse.OK == dbClient.cas( key, rv.getCas(), now ) ) {
				return now;
			}
		}

		return -1L;
	}

	public long changeGold( long uID, long change ) {
		return changeLongValue( getGoldKey( uID ), change );
	}

	public long changeHoolaiGold( long uID, long change ) {
		return changeLongValue( getHoolaiGoldKey( uID ), change );
	}

	public long changeMasterPoint( long uID, long change ) {
		return changeLongValue( getMasterPointKey( uID ), change );
	}

	public long incrHelpUsed( long uID ) {

		String key = getHelpUsedKey( uID );
		long now = System.currentTimeMillis();
		long count = 0;

		for( int i = 0; i < MAX_RETRY; ++i ) {
			CASValue< Object > rv = dbClient.gets( key );
			if( rv == null )
				return -1L;
			String val = (String) rv.getValue();
			// 判断如果上次记录起始点是前一天，则是今天第一次；如果是同一天，加1
			long lastTime = Long.parseLong( val.split( ":" )[0] );
			long lastCount = Long.parseLong( val.split( ":" )[1] );

			if( TimeUtil.isSameDay( lastTime, now ) ) {
				count = lastCount + 1;
			}
			else {
				count = 1;
			}

			val = now + ":" + count;

			if( CASResponse.OK == dbClient.cas( key, rv.getCas(), val ) ) {
				return count;
			}
		}

		return -1L;
	}

	/**
	 * @param key
	 *            数据库key
	 * @param newValue
	 *            设置的新值
	 * @return 成功返回新值，失败返回-1
	 */
	private long replaceLongValue( String key, long newValue ) {
		try {
			if( dbClient.replace( key, 0, newValue ).get() )
				return newValue;
		}
		catch( InterruptedException e ) {
			e.printStackTrace();
		}
		catch( ExecutionException e ) {
			e.printStackTrace();
		}
		return -1L;
	}

	public long replaceGold( long uID, long gold ) {
		return replaceLongValue( getGoldKey( uID ), gold );
	}

	public long replaceHoolaiGold( long uID, long gold ) {
		return replaceLongValue( getHoolaiGoldKey( uID ), gold );
	}

	public long replaceMasterPoint( long uID, long mp ) {
		return replaceLongValue( getMasterPointKey( uID ), mp );
	}
	
	
	private ItemList filterExpire( ItemList list ) {
		if( list == null ) return null;
		long now = System.currentTimeMillis();
		int  expireCnt = 0;
		for( ItemUnit item : list.items ) {
			if( item.expireTime != 0 && item.expireTime <= now )
				++expireCnt;
		}
		if( expireCnt == 0 ) return list;
		ItemUnit[] items = new ItemUnit[list.items.length - expireCnt];
		int i = 0;
		for( ItemUnit item : list.items ) {
			if( item.expireTime != 0 && item.expireTime <= now )
				continue;
			items[i++] = item;
		}
		return new ItemList( items );
	}

	/**
	 * @param uID
	 *            用户ID
	 * @return 如果找到了返回用户道具，否则返回null
	 */
	public ItemList getItems( long uID ) {
		String key = getItemKey( uID );
		ItemList list = dbClient.get( key, ITEM_TC );
		return filterExpire( list );
	}

	/**
	 * @param uID
	 *            用户ID
	 * @param itemID
	 *            道具ID
	 * @param itemCount
	 *            道具数量
	 * @return 更新成功返回道具列表，否则返回null
	 */
	public ItemList changeItem0( long uID, int itemID, int itemCount, long expire ) {

		String key = getItemKey( uID );

		for( int i = 0; i < MAX_RETRY; ++i ) {
			CASValue< ItemList > rv = dbClient.gets( key, ITEM_TC );
			if( rv == null )
				return null;
			ItemList list = filterExpire( rv.getValue() );
			if( list == null )
				return null;
			if( !list.change( itemID, itemCount, expire ) )
				return null;

			if( CASResponse.OK == dbClient
					.cas( key, rv.getCas(), list, ITEM_TC ) ) {
				return list;
			}
		}

		return null;
	}

	public ItemList changeItems( long uID, ItemList changeList ) {

		String key = getItemKey( uID );

		for( int i = 0; i < MAX_RETRY; ++i ) {
			CASValue< ItemList > rv = dbClient.gets( key, ITEM_TC );
			if( rv == null )
				return null;
			ItemList list = filterExpire( rv.getValue() );
			if( list == null )
				return null;

			for( ItemUnit change : changeList.items ) {
				if( !list.change( change.itemID, change.itemCount, change.expireTime ) )
					return null;
			}

			if( CASResponse.OK == dbClient
					.cas( key, rv.getCas(), list, ITEM_TC ) ) {
				return list;
			}
		}

		return null;
	}

	/**
	 * @param uID
	 *            用户ID
	 * @param list
	 *            道具列表
	 * @return 设置成功返回用户道具列表，否则返回null
	 */
	public ItemList replaceItems( long uID, ItemList list ) {
		String key = getItemKey( uID );
		logger.debug( "replaceItems key {}", key );
		try {
			OperationFuture< Boolean > of = dbClient
					.replace( key, 0, list, ITEM_TC );
			if( of.get() ) {
				return list;
			}
			logger.debug( "op status {}", of.getStatus() );
		}
		catch( InterruptedException e ) {
			e.printStackTrace();
		}
		catch( ExecutionException e ) {
			e.printStackTrace();
		}
		return null;
	}

	public ItemList getItemUsed( long uID ) {
		String key = getItemUsedKey( uID );
		return dbClient.get( key, ITEM_TC );
	}

	public ItemList addItemUsed( long uID, ItemList itemUsed ) {

		String key = getItemUsedKey( uID );

		for( int i = 0; i < MAX_RETRY; ++i ) {
			CASValue< ItemList > rv = dbClient.gets( key, ITEM_TC );
			if( rv == null )
				return null;
			ItemList list = rv.getValue();
			if( list == null )
				return null;

			for( ItemUnit used : itemUsed.items ) {
				if( !list.change( used.itemID, used.itemCount, 0L ) )
					return null;
			}

			if( CASResponse.OK == dbClient
					.cas( key, rv.getCas(), list, ITEM_TC ) ) {
				return list;
			}
		}

		return null;
	}

	public ItemList replaceItemUsed( long uID, ItemList list ) {
		String key = getItemUsedKey( uID );
		try {
			OperationFuture< Boolean > of = dbClient
					.replace( key, 0, list, ITEM_TC );
			if( of.get() ) {
				return list;
			}
		}
		catch( InterruptedException e ) {
			e.printStackTrace();
		}
		catch( ExecutionException e ) {
			e.printStackTrace();
		}
		return null;
	}

	public ItemGiveList getItemGive( long uID ) {
		String key = getItemGiveKey( uID );
		return dbClient.get( key, ITEM_GIVE_TC );
	}

	public ItemGiveList addItemGive( long uID, ItemGiveList giveList ) {

		String key = getItemGiveKey( uID );

		for( int i = 0; i < MAX_RETRY; ++i ) {
			CASValue< ItemGiveList > rv = dbClient.gets( key, ITEM_GIVE_TC );
			if( rv == null )
				return null;
			ItemGiveList list = rv.getValue();
			if( list == null )
				return null;

			for( ItemGiveRecord r : giveList.records ) {
				list.addRecord( r );
			}

			if( CASResponse.OK == dbClient
					.cas( key, rv.getCas(), list, ITEM_GIVE_TC ) ) {
				return list;
			}
		}

		return null;
	}

	public ItemGiveList replaceItemGive( long uID, ItemGiveList list ) {
		String key = getItemGiveKey( uID );
		try {
			OperationFuture< Boolean > of = dbClient
					.replace( key, 0, list, ITEM_GIVE_TC );
			if( of.get() ) {
				return list;
			}
		}
		catch( InterruptedException e ) {
			e.printStackTrace();
		}
		catch( ExecutionException e ) {
			e.printStackTrace();
		}
		return null;
	}

	@Deprecated
	public Safebox getSafebox( long uID ) {
		String key = getSafeboxKey( uID );
		return dbClient.get( key, SAFEBOX_TC );
	}

}
