/**
 * 
 */
package com.hoolai.texaspoker.scandb;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.schooner.MemCached.MemcachedItem;

public interface ExtendedMemcachedClient {
	
	boolean isStarted();
	
	public void start();

	boolean add(String key, Object value);

	boolean add(String key, Object value, Date expiry);

	boolean append(String key, Object value);

	long decr(String key, long inc);

	long decr(String key);

	boolean delete(String key);

	Object get(String key);

	long incr(String key, long inc);

	/**濡傛灉key涓嶅瓨鍦ㄤ細鍏堣缃负0*/
	long incr(String key);

	/**鍙墽琛宨ncr鎿嶄綔, 濡傛灉涓嶅瓨鍦ㄥ垯杩斿洖-1, 璋冪敤姝ゆ柟娉曞繀椤诲厛鍒濆key*/
	long justIncr(String key);

	boolean keyExists(String key);

	boolean prepend(String key, Object value);

	boolean replace(String key, Object value, Date expiry);

	boolean replace(String key, Object value);

	boolean set(String key, Object value, Date expiry);

	boolean set(String key, Object value);

	Map<String, Object> mget(Collection<String> keys);

	public boolean cas(String key, Object value, long casUnique);

	public boolean cas(String key, Object value, Date expiry, long casUnique);

	public MemcachedItem gets(String key);
	public MemcachedItem gets(String key,Integer hashCode);

	/* --------------- extentions --------------- */
	<T extends Serializable> T smember2(String key, long identity);

	long scard(String key);

	long scard2(String key);

	/**
	 * two-phase fetch
	 * @param <T>
	 * @param key
	 * @param value
	 * @param identity
	 */
	<T extends Serializable> boolean sadd2(String key, T value, String identity);

	<T extends Serializable> boolean sreplace2(String key, T value, long identity);

	boolean sismember(String key, long identity);
	/**
	 * two-phase fetch
	 * @param key
	 * @param identity
	 */
	boolean srem2(String key, String id);

	<T extends Serializable> void mset(Map<String, T> keyValuesMap);

	Map<String, Object> mget(String... moreKeys);

	boolean sexists2(String key);

	/**
	 * two-phase fetch
	 * @param <T>
	 * @param key
	 * @param clazz
	 * @return
	 */
	<T extends Serializable> Map<Long, T> smembers2(String key);

	<T extends Serializable> List<T> smembersReturnList2(String key);

	<T extends Serializable> List<T> smembersReturnList2(String key, List<? extends Number> identities);

	/**
	 * 鑾峰彇闆嗗悎鎵�湁鍏冪礌锛屼笉鍋歵wo-phase fetch
	 * @param key
	 * @param clazz 浠呮敮鎸佸熀纭�被鍨嬬殑鍖呰绫诲瀷锛屼互鍙奡tring
	 * @return
	 */
	<T extends Serializable> List<T> smembers(String key, Class<T> clazz);

	/**
	 * 鍦ㄩ泦鍚堥噷娣诲姞涓�釜鍏冪礌锛屼笉鍋歵wo-phase fetch
	 * @param key
	 * @param value
	 */
	<T extends Number> void sadd(String key, T value);

	boolean sadd(String key, String value);

	/**
	 * single column, not a two-phase fetch
	 * @param <T>
	 * @param key
	 * @param value
	 */
	<T extends Serializable> boolean srem(String key, T value);

	/**
	 * 闅忔満鑾峰彇闆嗗悎涓殑鏌愪竴涓厓绱�
	 * two-phase fetch
	 * @param <T>
	 * @param key
	 * @param clazz
	 * @return
	 */
	<T extends Serializable> T srandmember2(String key);

	/**
	 * 闅忔満鑾峰彇闆嗗悎涓殑鏌愪竴涓厓绱�
	 * not two-phase fetch
	 * @param key
	 * @return
	 */
	String srandmember(String key);

	void sremAll2(String key);

	/* ----------- top board tree ------------- */

	/**
	 * 绉婚櫎list绗竴涓厓绱�
	 * @param key
	 * @return 琚Щ闄ょ殑鍏冪礌
	 */
	public <T extends Serializable> T lpop(String key);

	/**
	 * 绉婚櫎list鏈�悗鐨勫厓绱�
	 * @param key
	 * @return 琚Щ闄ょ殑鍏冪礌
	 */
	public <T extends Serializable> T rpop(String key);

	/**
	 * 鑾峰彇list鍒楄〃鍐呭
	 * @param <T>
	 * @param key
	 * @param from
	 * @param end
	 * @param clazz
	 * @return
	 */
	public <T> List<T> lrange(String key, int from, int end);

	/**
	 * 鍦╨ist鍒楄〃鐨刾osition浣嶇疆鎻掑叆鍊紇alue
	 * 濡傛灉鏈夐噸澶嶏紙identity锛夛紝鍒犻櫎鍘熶綅缃�
	 * @param key 鍒楄〃閿悕
	 * @param value 瑕佹彃鍏ョ殑鍊�
	 * @param position 浠�寮�
	 */
	public <T extends Serializable> void linsert(String key, T value, int position, String identity);

	void sremAll(String key);

	boolean sexists(String key);

	<T extends Serializable> List<T> srandomMembers(String key, int randomMemberNum, Class<T> clazz);

	<T extends Serializable> Map<Long, T> smembers2(String key, List<? extends Number> identities);

	<T extends Serializable> List<T> srandomMembers(String key, int randomMemberNum, T exceptMember, Class<T> clazz);

	void flushAll();

	/**
	 * 浣跨敤get + 1 set鐨勬柟寮忔ā鎷焛ncr
	 * @param key
	 * @return
	 */
	int incrByGetAndSet(String key);

	/**
	 * 閿佸畾鏌愪竴涓猭ey涓嶈鍏朵粬浜烘搷浣�
	 * @param key
	 * @return
	 */
	boolean lock(String key);

	/**
	 * 瑙ｉ攣鏌愪竴涓猭ey
	 * @param key
	 */
	void unlock(String key);

	/*
	 * 浠ヤ笅鏄彁渚涚粰澶ф暟鎹噺鐨剆add srem鐨勬柟娉�
	 * 涓嶄繚璇佺嚎绋嬪畨鍏�澶氱嚎绋嬫坊鍔犳椂鏈夊彲鑳戒涪澶�
	 * 鐩墠鍙彁渚沴ong绫诲瀷鐨勬敮鎸�鏆傛椂鍙敤鍒發ong绫诲瀷)
	 * 鍐呴儴瀹炵幇, 姣忎釜鍒嗗竷鐨刱ey鏈�瀵瑰簲2000鍏冪礌, 鍙傛暟涓殑key鐢ㄦ潵璁板綍鎬荤殑鍒嗗竷鐨刱ey鐨勪釜鏁�
	 * 绉婚櫎涓�釜鍏冪礌鏃�浼氱Щ闄ゆ渶鍚庝釜鍏冪礌琛ュ埌鍓嶉潰
	 * random鐨勬椂鍊欎笉浼氶�鎷╂渶鍚庝竴涓垎甯冪殑key(鍙湁涓�釜鍒嗗竷key鐨勬儏鍐典緥澶�
	 */
	void auAdd(String key, long member);

	List<Long> auRandomMembers(String key, int randomNum, long exceptId, long beginId);

	void ruAdd(String key, long member);

	List<Long> ruRandomMembers(String key, int randomNum, long exceptId, long beginId);

	void ruRemove(String key, long member);

	/**涓�娣诲姞澶氫釜鍗曞�(Collection<Object>) 蹇呴』鍙互杞崲鎴怱tring*/
	boolean sadd(String key, Collection<Object> values);

	void auRemove(String key, long member);

	/**
	 * 涔嬪墠鐨刲ock鐨勪竴涓寮虹増鏈紝濡傛灉鍙戠幇閿佷綇鐨勬椂闂磋秴杩囦簡expTime锛屽垯浼氶噸鏂伴攣瀹�
	 * @param key
	 * @param expTime
	 * @return
	 */
    boolean lock(String key, long expTime);

}
