package com.hoolai.texaspoker.scandb;

import java.util.Map;

import redis.clients.jedis.Jedis;

public class DaoRedis implements DaoInterface {

	//private static final String SEPARATOR = "hdx2#^";
	
	private JedisPool pool;
	
	public DaoRedis(JedisPool pool){
		this.pool = pool;
	}
	
	
	@Override
	public boolean save(String key, String val) {
		Jedis client = pool.getResource();
		String result = client.set(key,val);
		if("OK".equals(result))
			return true;
		else
			return false;
	}

	@Override
	public boolean save(String key, Object val) {
		/*String result = client.set(key,this.getObjectValue(val));
		if("OK".equals(result))
			return true;
		else
			return false;*/
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean save(Object key, Object val) {
		/*String result = client.set(key.toString(),this.getObjectValue(val));
		if("OK".equals(result))
			return true;
		else
			return false;*/
		
		throw new UnsupportedOperationException();
	}

	@Override
	public Object load(String key) {
		Jedis client = pool.getResource();
		String value = client.get(key);
		if(value == null)
			return null;
		/*if("".equals(value) || !value.contains(SEPARATOR))
			return value;
		else
			return this.getValueObject(value);*/
		
		return value;
		
	}

	@Override
	public Object load(Object key) {
		Jedis client = pool.getResource();
		String value = client.get(key.toString());
		
		if(value == null)
			return null;
		/*if("".equals(value) || !value.contains(SEPARATOR))
			return value;
		else
			return this.getValueObject(value);*/
		
		return value;
	}

	@Override
	public Map<String, Object> load(String[] keys) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(String key) {
		Jedis client = pool.getResource();
		if(client.del(key).intValue() > 0)
			return true;
		else
			return false;
	}

	@Override
	public Integer get_uuid(String platform_type, String platform_id) {
		String  key = PlatformIDBuilder.build(platform_type, platform_id);
		Jedis client = pool.getResource();
		String result = client.get(key);
		
		if(result == null || "".equals(result))
			return null;
		
		Long  uuid = Long.parseLong(result);
		
		if( uuid == null ) 
			return null;
		return uuid.intValue();
		
	}

	@Override
	public int gen_uuid(String platform_type, String platform_id) {
		Jedis client = pool.getResource();
		long  uuid = client.incr("UUID");
		String  key = PlatformIDBuilder.build(platform_type, platform_id);
		client.set( key, String.valueOf(uuid));
		return (int)uuid;
	}

	@Override
	public long add_jackpot(long inc) {
		Jedis client = pool.getResource();
		return client.incrBy("JACKPOT", inc);
	}

	@Override
	public long win_jackpot(long dec) {
		Jedis client = pool.getResource();
		return client.decrBy("JACKPOT", dec );
	}

	@Override
	public long add_inning(long inc) {
		Jedis client = pool.getResource();
		return client.incrBy( "INNING", inc );
	}

	@Override
	public long add_phone_order(long inc) {
		Jedis client = pool.getResource();
		return client.incrBy( "PHONEORDER", inc );
	}
		
	
	/*private String getObjectValue(Object obj){
		StringBuffer buffer = new StringBuffer();
		buffer.append(JsonUtilForDB.toJson(obj))
		      .append(SEPARATOR)
		      .append(obj.getClass().getName());
		
		return buffer.toString();
	}
	
	private Object getValueObject(String value){
		String args[] = value.split(SEPARATOR);
		if(args.length == 2){
			try {
				Class<?> c = Class.forName(args[1]);
				return JsonUtilForDB.fromJson(args[0], c);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}*/

	
	
}
