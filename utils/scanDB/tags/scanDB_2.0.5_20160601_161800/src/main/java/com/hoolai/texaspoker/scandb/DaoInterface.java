package com.hoolai.texaspoker.scandb;

import java.util.Map;

public interface DaoInterface {

	public  boolean    save( String key, String val );
	public  boolean    save( String key, Object val );
	public  boolean    save( Object key, Object val );
	
	public  Object     load( String key );
	public  Object     load( Object key );
	public  Map<String, Object>   load( String[] keys );
	
	public  boolean    remove( String key );
	
	public  Integer    get_uuid( String platform_type, String platform_id );
	public  int        gen_uuid( String platform_type, String platform_id );
	
	public  long       add_jackpot( long inc );
	public  long       win_jackpot( long dec );
	
	public  long       add_inning( long inc );
	public  long       add_phone_order( long inc );
}
