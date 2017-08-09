/**
 * 
 */
package com.hoolai.ccgames.scandb.utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import com.schooner.MemCached.MemcachedItem;

/**
 * 
 * @author joseph
 * 
 */
public class ExtendedMemcachedClientImpl extends
		AbstractExtendedMemcachedClient {

	public static AtomicLong storageReqCount = new AtomicLong(0);
	public static AtomicLong getReqCount = new AtomicLong(0);

	public static AtomicLong storageUsedTime = new AtomicLong(0);
	public static AtomicLong getUsedTime = new AtomicLong(0);

	public static AtomicLong storageUsedMTime = new AtomicLong(0);
	public static AtomicLong getUsedMTime = new AtomicLong(0);

	public static boolean isStatistical = false;

	private MemCachedClient mcc;
	private String poolName;
	private String urls;

	private MemCachedClient createClient(String poolName, int algo,
			String... memcacheUrls) {
		String[] servers = memcacheUrls;
		Integer[] weights = new Integer[memcacheUrls.length];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = 1;
		}

		SockIOPool pool = SockIOPool.getInstance(poolName);
		
		pool.setHashingAlg(algo);
		pool.setServers(servers);
		pool.setWeights(weights);

		PropertiesUtil propertiesUtil = new PropertiesUtil(
				"mc_config.properties");
		if (!propertiesUtil.available()) {
			propertiesUtil = new PropertiesUtil("default_config.properties");
		}

		pool.setInitConn(propertiesUtil.getPropertyAsInt("initConn"));
		pool.setMinConn(propertiesUtil.getPropertyAsInt("minConn"));
		pool.setMaxConn(propertiesUtil.getPropertyAsInt("maxConn"));
		pool.setMaxIdle(propertiesUtil.getPropertyAsInt("maxIdle"));
		pool.setMaintSleep(propertiesUtil.getPropertyAsInt("maintSleep"));
		pool.setAliveCheck(propertiesUtil.getPropertyAsBoolean("aliveCheck"));
		pool.setNagle(propertiesUtil.getPropertyAsBoolean("nagle"));
		pool.setSocketTO(propertiesUtil.getPropertyAsInt("socketTO"));
		String bufferSizeStr = propertiesUtil.getProperty("bufferSize");
		int bufferSize = 256 * 1024;
		if (bufferSizeStr != null)
			bufferSize = Integer.parseInt(bufferSizeStr);
		pool.setBufferSize(bufferSize);
		pool.initialize();

		return new MemCachedClient(poolName);
	}

	/**
	 * 初始化单个client
	 * 
	 * @param poolName
	 * @param connectUrls
	 */
	public ExtendedMemcachedClientImpl(String poolName, String connectUrls) {
		// this.mcc = createClient(poolName, SockIOPool.ROUND_ROBIN,
		// connectUrls.split(","));
		this.poolName = poolName;
		this.urls = connectUrls;
	}

	public void start() {
		this.mcc = createClient(poolName, SockIOPool.ROUND_ROBIN,
				urls.split(","));
	}

	public boolean isStarted() {
		return this.mcc != null;
	}

	public boolean add(String key, Object value, Date expiry) {
		init();
		return mcc.add(key, value, expiry);
	}

	public boolean add(String key, Object value) {
		init();
		if (isStatistical) {
			long start = System.nanoTime();
			long ms = System.currentTimeMillis();
			try {
				return mcc.add(key, value);
			} finally {
				storageReqCount.incrementAndGet();
				storageUsedTime.addAndGet(System.nanoTime() - start);
				storageUsedMTime.addAndGet(System.currentTimeMillis() - ms);
			}
		} else {
			return mcc.add(key, value);
		}

	}

	public boolean append(String key, Object value) {
		init();
		return mcc.append(key, value);
	}

	public boolean cas(String key, Object value, Date expiry, long casUnique) {
		init();
		return mcc.cas(key, value, expiry, casUnique);
	}

	public boolean cas(String key, Object value, long casUnique) {
		init();
		return mcc.cas(key, value, casUnique);
	}

	public MemcachedItem gets(String key) {
		init();
		return mcc.gets(key);
	}

	public MemcachedItem gets(String key, Integer hashCode) {
		init();
		return mcc.gets(key, hashCode);
	}

	public long decr(String key, long inc) {
		init();
		return mcc.decr(key, inc);
	}

	public long decr(String key) {
		init();
		return mcc.decr(key);
	}

	public boolean delete(String key) {
		init();
		if (isStatistical) {
			long start = System.nanoTime();
			long ms = System.currentTimeMillis();
			try {
				return mcc.delete(key);
			} finally {
				getReqCount.incrementAndGet();
				getUsedTime.addAndGet(System.nanoTime() - start);
				getUsedMTime.addAndGet(System.currentTimeMillis() - ms);
			}
		} else {
			return mcc.delete(key);
		}
	}

	public void flushAll() {
		init();
		mcc.flushAll();
	}

	public Object get(String key) {
		init();
		if (isStatistical) {
			long start = System.nanoTime();
			long ms = System.currentTimeMillis();
			try {
				Object o = mcc.get(key);
				if (o == null) {
					o = mcc.get(key);
				}
				return o;
			} finally {
				getReqCount.incrementAndGet();
				getUsedTime.addAndGet(System.nanoTime() - start);
				getUsedMTime.addAndGet(System.currentTimeMillis() - ms);
			}
		} else {
			Object o = mcc.get(key);
			if (o == null) {
				o = mcc.get(key);
			}
			return o;
		}
	}

	public long incr(String key, long inc) {
		init();
		long r = mcc.incr(key, inc);
		if (r < 0) {
			if (!mcc.add(key, String.valueOf(inc))) {
				return mcc.incr(key, inc);
			} else {
				return inc;
			}
		}
		return r;
	}

	public long incr(String key) {
		init();
		long r = mcc.incr(key);
		if (r < 0) {
			if (!mcc.add(key, "1")) {
				return mcc.incr(key);
			} else {
				return 1;
			}
		}

		return r;
	}

	public long justIncr(String key) {
		init();
		if (isStatistical) {
			long start = System.nanoTime();
			long ms = System.currentTimeMillis();
			try {
				long value = mcc.incr(key);
				if (value < 1) {
					value = mcc.incr(key);
				}
				return value;
			} finally {
				getReqCount.incrementAndGet();
				getUsedTime.addAndGet(System.nanoTime() - start);
				getUsedMTime.addAndGet(System.currentTimeMillis() - ms);
			}
		} else {
			long value = mcc.incr(key);
			if (value < 1) {
				value = mcc.incr(key);
			}
			return value;
		}
	}

	public boolean keyExists(String key) {
		return this.get(key) != null;
	}

	public boolean prepend(String key, Object value) {
		init();
		return mcc.prepend(key, value);
	}

	public boolean replace(String key, Object value, Date expiry) {
		init();
		return mcc.replace(key, value, expiry);
	}

	public boolean replace(String key, Object value) {
		init();
		if (isStatistical) {
			long start = System.nanoTime();
			long ms = System.currentTimeMillis();
			try {
				return mcc.replace(key, value);
			} finally {
				storageReqCount.incrementAndGet();
				storageUsedTime.addAndGet(System.nanoTime() - start);
				storageUsedMTime.addAndGet(System.currentTimeMillis() - ms);
			}
		} else {
			return mcc.replace(key, value);
		}
	}

	public boolean set(String key, Object value, Date expiry) {
		init();
		if (isStatistical) {
			long start = System.nanoTime();
			long ms = System.currentTimeMillis();
			try {
				return mcc.set(key, value, expiry);
			} finally {
				storageReqCount.incrementAndGet();
				storageUsedTime.addAndGet(System.nanoTime() - start);
				storageUsedMTime.addAndGet(System.currentTimeMillis() - ms);
			}
		} else {
			return mcc.set(key, value, expiry);
		}
	}

	public boolean set(String key, Object value) {
		init();
		if (isStatistical) {
			long start = System.nanoTime();
			long ms = System.currentTimeMillis();
			try {
				return mcc.set(key, value);
			} finally {
				storageReqCount.incrementAndGet();
				storageUsedTime.addAndGet(System.nanoTime() - start);
				storageUsedMTime.addAndGet(System.currentTimeMillis() - ms);
			}
		} else {
			return mcc.set(key, value);
		}
	}

	@Override
	public Map<String, Object> mget(String... keys) {
		init();
		if (keys == null || keys.length == 0) {
			return new HashMap<String, Object>();
		}

		if (isStatistical) {
			long start = System.nanoTime();
			long ms = System.currentTimeMillis();
			try {
				return mcc.mget(keys);
			} finally {
				getReqCount.incrementAndGet();
				getUsedTime.addAndGet(System.nanoTime() - start);
				getUsedMTime.addAndGet(System.currentTimeMillis() - ms);
			}
		} else {
			return mcc.mget(keys);
		}
	}

	@Override
	public <T extends Serializable> void mset(Map<String, T> keyValuesMap) {
		for (Entry<String, T> entry : keyValuesMap.entrySet()) {
			this.set(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public Map<String, Object> mget(Collection<String> keys) {
		init();
		if (keys == null || keys.size() == 0) {
			return new HashMap<String, Object>();
		}
		if (isStatistical) {
			long start = System.nanoTime();
			long ms = System.currentTimeMillis();
			try {
				return mcc.mget(keys);
			} finally {
				getReqCount.incrementAndGet();
				getUsedTime.addAndGet(System.nanoTime() - start);
				getUsedMTime.addAndGet(System.currentTimeMillis() - ms);
			}
		} else {
			return mcc.mget(keys);
		}
	}

	/**
	 * 
	 * @author Cedric(TaoShuang)
	 */
	private void init() {
		if (!this.isStarted()) {
			this.start();
		}
	}
}
