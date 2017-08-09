/**
 * 
 */
package com.hoolai.texaspoker.scandb;

import redis.clients.jedis.Jedis;

/**
 * @author Cedric(TaoShuang)
 * @create 2012-3-6
 */
public class JedisPool {
	private ThreadLocal<Jedis> jedisPool = new ThreadLocal<Jedis>();
	private String ip;
	private int port;

	public JedisPool(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	public JedisPool(String url) {
		String[] results = url.split(",");
		String result = results[0];
		String[] rawResult = result.split(":");
		this.ip = rawResult[0].trim();
		this.port = Integer.parseInt(rawResult[1].trim());
	}

	public Jedis getResource() {
		Jedis jedis = jedisPool.get();
		if (jedis == null || !validate(jedis)) {  
			jedis = new Jedis(ip, port);
			jedisPool.set(jedis);
		}
		return jedis;
	}
	
	public boolean validate(Jedis obj) {
        if (obj instanceof Jedis) {
            final Jedis jedis = (Jedis) obj;
            try {
                return jedis.isConnected() && jedis.ping().equals("PONG");
            } catch (final Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }
	
	public void clearMemory() {
		this.jedisPool = null;
	}
}
