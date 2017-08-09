package com.hoolai.texaspoker.scandb;

import java.util.Map;

public class DaoMemcached implements DaoInterface {

	private  ExtendedMemcachedClient   client;

	public  DaoMemcached( ExtendedMemcachedClient c ) {
		this.client = c;
	}
	
	@Override
	public boolean save( String key, String val ) {
		return client.set( key, val );
	}

	@Override
	public boolean save( String key, Object val ) {
		return client.set( key, val );
	}

	@Override
	public boolean save( Object key, Object val ) {
		return client.set( key.toString(), val );
	}

	@Override
	public Object load( String key ) {
		return client.get( key );
	}

	@Override
	public Object load( Object key ) {
		return client.get( key.toString() );
	}

	@Override
	public Map<String, Object> load( String[] keys ) {
		return client.mget(keys);
	}

	@Override
	public int gen_uuid( String platform_type, String platform_id ) {
		long  uuid = client.incr( "UUID" );
		String  key = PlatformIDBuilder.build(platform_type, platform_id);
		client.set( key, uuid );
		return (int)uuid;
	}

	@Override
	public Integer get_uuid( String platform_type, String platform_id ) {
		String  key = PlatformIDBuilder.build(platform_type, platform_id);
		Long  uuid = (Long)client.get( key );
		if( uuid == null ) return null;
		return uuid.intValue();
	}

	@Override
	public long add_jackpot(long inc) {
		return client.incr( "JACKPOT", inc );
	}

	@Override
	public long win_jackpot( long dec ) {
		return client.decr( "JACKPOT", dec );
	}
	
	@Override
	public long add_inning( long inc ) {
		return client.incr( "INNING", inc );
	}

	@Override
	public long add_phone_order(long inc) {
		return client.incr( "PHONEORDER", inc );
	}

	@Override
	public boolean remove(String key) {
		return client.delete( key );
	}
}
