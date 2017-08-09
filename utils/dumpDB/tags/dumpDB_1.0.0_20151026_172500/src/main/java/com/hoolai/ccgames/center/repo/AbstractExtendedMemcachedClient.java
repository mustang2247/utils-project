/**
 * 
 */
package com.hoolai.ccgames.center.repo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.schooner.MemCached.MemcachedItem;

public abstract class AbstractExtendedMemcachedClient implements
		ExtendedMemcachedClient {

	static final String separator = "_";
	static final String member_flag = "mb";
	static String element_separator = StringUtil.ELEMENT_SEPARATOR_COMMA;
	Random r = new Random(47);
	static final int TREE_MAX_LEVEL = 2;

	public AbstractExtendedMemcachedClient() {
		super();
	}

	@Override
	public <T extends Serializable> boolean sadd2(String key, T value,
			String identity) {
		boolean isSuccess = false;
		String idSetKey = getIdSetKey(key);
		String memberKey = getMemberKey(key, identity);
		this.set(memberKey, value);
		Object data = this.get(idSetKey);
		if (data == null) {
			isSuccess = this.set(idSetKey, identity);
		} else {
			String ids = (String) data;
			if (StringUtil.existsElement(ids, identity)) {
				return true;
			}
			ids = StringUtil.addElement(ids, identity);
			isSuccess = this.set(idSetKey, ids);
		}
		return isSuccess;
	}

	/**
	 * 娣诲姞鍏冪礌骞堕檺鍒舵暣涓垪琛ㄧ殑闀垮害 闇�鍒犻櫎鏃跺皢鍒犻櫎鏈�棭鐨勫厓绱�
	 * 
	 * @param <T>
	 * @param key
	 * @param value
	 * @param identity
	 * @param limit
	 *            鏁翠釜鍒楄〃鐨勬渶澶ч暱搴�
	 */
	public <T extends Serializable> void sadd2(String key, T value,
			String identity, int limit) {
		String idSetKey = getIdSetKey(key);
		String memberKey = getMemberKey(key, identity);
		this.set(memberKey, value);
		Object data = this.get(idSetKey);
		if (data == null) {
			this.set(idSetKey, identity);
		} else {
			String ids = (String) data;
			if (StringUtil.existsElement(ids, identity)) {
				return;
			}
			ids = StringUtil.addElement(ids, identity);
			while (StringUtil.elementCount(ids) > limit) {
				String firstElement = StringUtil.firstElement(ids);
				String toRemoveMember = getMemberKey(key, firstElement);
				ids = StringUtil.removeElement(ids, firstElement);
				this.delete(toRemoveMember);
			}
			this.set(idSetKey, ids);
		}
	}

	/**
	 * 鍦╨ist鍒楄〃鐨刾osition浣嶇疆鎻掑叆鍊紇alue 濡傛灉identity宸茬粡鍦ㄥ垪琛ㄩ噷浜嗭紝鍒欑Щ鍔ㄥ埌姝ｇ‘鐨勪綅缃�
	 * 
	 * @param key
	 *            鍒楄〃閿悕
	 * @param value
	 *            瑕佹彃鍏ョ殑鍊�
	 * @param position
	 *            浠�寮�
	 * @param identity
	 *            鍒楄〃椤笽d
	 */
	@Override
	public <T extends Serializable> void linsert(String key, T value,
			int position, String identity) {
		String idListKey = getIdListKey(key);
		String memberKey = getMemberKey(key, identity);
		this.set(memberKey, value);

		Object data = this.get(idListKey);
		if (data == null) {
			this.set(idListKey, identity);
		} else {
			String ids = (String) data;
			ids = this.insert(ids, identity, position);
			this.set(idListKey, ids);
		}
	}

	public String insert4Test(String ids, String element, int position) {
		String returnString = insert(ids, element, position);
		return returnString;
	}

	/**
	 * 鑾峰彇list鍒楄〃鍐呭
	 * 
	 * @param <T>
	 * @param key
	 * @param from
	 * @param end
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> lrange(String key, int from, int end) {
		String idListKey = getIdListKey(key);
		String idListStr = (String) this.get(idListKey);
		List<T> list = new ArrayList<T>();

		if (idListStr != null) {
			String[] idSet = idListStr.split(element_separator);
			for (String id : idSet) {
				String memberKey = getMemberKey(key, id);
				list.add((T) this.get(memberKey));
			}
		}
		return list;
	}

	/**
	 * 绉婚櫎list鏈�悗鐨勫厓绱�
	 * 
	 * @param key
	 * @return 琚Щ闄ょ殑鍏冪礌
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Serializable> T rpop(String key) {
		String idListKey = getIdListKey(key);
		String idListStr = (String) this.get(idListKey);

		int lastIndex = idListStr.lastIndexOf(",");
		String remainingIds = idListStr.substring(0, lastIndex);
		String identity = idListStr.substring(lastIndex + 1);

		String memberKey = getMemberKey(idListKey, identity);
		T value = (T) this.get(memberKey);
		this.delete(memberKey);
		this.set(idListKey, remainingIds);
		return value;
	}

	/**
	 * 绉婚櫎list绗竴涓厓绱�
	 * 
	 * @param key
	 * @return 琚Щ闄ょ殑鍏冪礌
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Serializable> T lpop(String key) {
		String idListKey = getIdListKey(key);
		String idListStr = (String) this.get(idListKey);

		int lastIndex = idListStr.indexOf(",");
		String identity = idListStr.substring(0, lastIndex);
		String remainingIds = idListStr.substring(lastIndex + 1);

		String memberKey = getMemberKey(idListKey, identity);
		T value = (T) this.get(memberKey);
		this.delete(memberKey);
		this.set(idListKey, remainingIds);
		return value;
	}

	public String removeElementFromList4Test(String list, String element) {
		String returnString = removeElementFromList(list, element);
		return returnString;
	}

	private String removeElementFromList(String list, String element) {
		if (list.startsWith(element + element_separator)) {
			list = list.substring(element.length() + 1);
		} else if (list.endsWith(element_separator + element)) {
			list = list.substring(0, list.length() - element.length() - 1);
		} else {
			int i = list.indexOf(element_separator + element
					+ element_separator);
			if (i >= 0) {
				list = list.substring(0, i)
						+ list.substring(i + element.length() + 1);
			}
		}
		return list;
	}

	/**
	 * 鍦╥ds鍒楄〃閲岀殑鎸囧畾浣嶇疆鎻掑叆element 濡傛灉宸茬粡鏈塭lement锛屽垯鍒犻櫎鍘熶綅缃紝
	 * 杩欓噷鍋囪浣嶇疆涓嶅洜鍒犻櫎鍘熶綅缃�鍙樺寲锛堝嵆鏃lement姘歌繙鍦╬osition鍚庨潰锛�
	 * 
	 * @param list1
	 *            {@link #element_separator}鍒嗗壊鐨勫垪琛�
	 * @param element
	 *            寰呮彃鍏ョ殑鍏冪礌
	 * @param position
	 *            浠�寮�
	 * @return
	 */
	private String insert(String list, String element, int position) {
		String list1 = removeElementFromList(list, element);
		// if (list1.length() < list.length()){
		// position = position - 1;
		// }

		if (list1 == null || list1.isEmpty()) {
			return element;
		}
		if (position == 0) {
			return new StringBuilder().append(element)
					.append(element_separator).append(list1).toString();
		}

		int index = list1.indexOf(element_separator);
		int count = 1;
		while (index >= 0 && count < position) {
			count++;
			index = list1.indexOf(element_separator, index + 1);
		}

		if (index == -1) {
			if (count < position) {
				throw new IndexOutOfBoundsException();
			}
			return new StringBuilder(list1).append(element_separator)
					.append(element).toString();
		}

		return new StringBuilder().append(list1.substring(0, index))
				.append(element_separator).append(element)
				.append(element_separator).append(list1.substring(index + 1))
				.toString();
	}

	String getIdListKey(String baseKey) {
		String returnString = new StringBuilder(baseKey).append(separator)
				.append("idlist").toString();
		return returnString;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Serializable> T smember2(String key, long identity) {
		String memberKey = getMemberKey(key, identity);
		Object data = this.get(memberKey);
		if (data == null) {
			return null;
		}
		return (T) data;
	}

	@Override
	public long scard(String key) {
		String idsStr = (String) this.get(key);
		return StringUtil.elementCount(idsStr);
	}

	@Override
	public long scard2(String key) {
		String idSetKey = getIdSetKey(key);
		String idsStr = (String) this.get(idSetKey);
		return StringUtil.elementCount(idsStr);
	}

	@Override
	public boolean sismember(String key, long identity) {
		String ids = (String) this.get(key);
		boolean returnboolean = StringUtil.existsElement(ids,
				String.valueOf(identity));
		return returnboolean;
	}

	@Override
	public boolean srem2(String key, String identity) {
		String idSetKey = getIdSetKey(key);
		String idSetStr = (String) this.get(idSetKey);

		if (!StringUtil.existsElement(idSetStr, identity)) {
			return false;
		}

		String newIdSetStr = StringUtil.removeElement(idSetStr,
				String.valueOf(identity));

		return this.set(idSetKey, newIdSetStr) ? this.delete(getMemberKey(key,
				identity)) : false;
	}

	public <T extends Serializable> boolean sreplace2(String key, T value,
			long identity) {
		String memberKey = getMemberKey(key, identity);
		return this.set(memberKey, value);
	}

	private String getIdSetKey(String baseKey) {
		return new StringBuilder(baseKey).append(separator).append("ids")
				.toString();
	}

	private String getMemberKey(String baseKey, long identity) {
		return getMemberKey(baseKey, String.valueOf(identity));
	}

	String getMemberKey(String baseKey, String identity) {
		return new StringBuilder(baseKey).append(separator).append(member_flag)
				.append(separator).append(identity).toString();
	}

	@Override
	public boolean sexists2(String key) {
		return this.keyExists(getIdSetKey(key));
	}

	@Override
	public <T extends Number> void sadd(String key, T value) {
		this.sadd(key, String.valueOf(value));
	}

	@Override
	public boolean sadd(String key, String value) {
		String ids = (String) this.get(key);
		if (StringUtil.existsElement(ids, value)) {
			return true;
		}
		ids = StringUtil.addElement(ids, value);
		return this.set(key, ids);
	}

	@Override
	public boolean sadd(String key, Collection<Object> values) {
		String ids = (String) this.get(key);
		for (Object valueObj : values) {
			String value = String.valueOf(valueObj);

			if (!StringUtil.existsElement(ids, value)) {
				ids = StringUtil.addElement(ids, value);
			}
		}
		return this.set(key, ids);
	}

	@Override
	public <T extends Serializable> boolean srem(String key, T value) {
		String x = (String) get(key);
		String newValue = StringUtil.removeElement(x, String.valueOf(value));
		return this.set(key, newValue);
	}

	@Override
	public <T extends Serializable> List<T> smembers(String key, Class<T> clazz) {
		String ids = (String) this.get(key);
		return StringUtil.split(ids, clazz);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Serializable> T srandmember2(String key) {
		String idSetKey = getIdSetKey(key);
		String[] values = ((String) this.get(idSetKey)).split(",");
		int randomIndex = r.nextInt(values.length);
		String memberKey = getMemberKey(key, values[randomIndex]);
		T returnT = (T) this.get(memberKey);
		return returnT;
	}

	public String srandmember(String key) {
		String id = null;

		String idsStr = (String) this.get(key);
		// 浣滀负long鍨嬬殑鏈�ぇ鏁板�鍗犵敤瀛楃闀垮害
		int longLength = 20;
		int splitMaxLength = 300;
		if (idsStr.length() < splitMaxLength) {
			String[] values = idsStr.split(",");
			int randomIndex = r.nextInt(values.length);
			id = values[randomIndex];
		} else {
			// 鑸嶅純鏈�悗longLength瀛楃鐨勯暱搴︼紝淇濊瘉鑷冲皯鏈変竴涓猧d
			int randomIndex = r.nextInt(idsStr.length() - longLength);
			int from = idsStr.indexOf(",", randomIndex) + 1;
			int to = idsStr.indexOf(",", from);
			id = idsStr.substring(from, to);
		}
		return id;
	}

	@Override
	public <T extends Serializable> List<T> srandomMembers(String key,
			int randomMemberNum, Class<T> clazz) {
		List<T> randomMembers;
		String idsStr = (String) this.get(key);
		List<T> values = StringUtil.split(idsStr, clazz);
		int size = values.size();
		if (randomMemberNum >= size) {// 涓嶅randomMemberNum鏃惰繑鍥炴墍鏈�
			randomMembers = values;
		} else if (size < (randomMemberNum * 3)) {// 褰撳叏閮ㄧ殑member鍦╮andomMemberNum鐨�鍊嶄箣鍐呮椂
													// 鍙栧墠randomMemberNum涓�
			while ((size = values.size()) > randomMemberNum) {
				values.remove(r.nextInt(size));
			}
			randomMembers = values;
		} else {// 闅忔満randomMemberNum涓�
			randomMembers = new ArrayList<T>();
			while (randomMembers.size() < randomMemberNum) {
				T t = values.get(r.nextInt(size));
				if (!randomMembers.contains(t)) {
					randomMembers.add(t);
				}
			}
		}
		return randomMembers;
	}

	@Override
	public <T extends Serializable> List<T> srandomMembers(String key,
			int randomMemberNum, T exceptMember, Class<T> clazz) {
		List<T> randomMembers;
		String idsStr = (String) this.get(key);
		List<T> values = StringUtil.split(idsStr, clazz);
		int size = values.size();
		if (randomMemberNum >= size) {// 涓嶅randomMemberNum鏃惰繑鍥炴墍鏈�
			randomMembers = values;
			randomMembers.remove(exceptMember);
		} else if (size <= (randomMemberNum * 3)) {// 褰撳叏閮ㄧ殑member鍦╮andomMemberNum鐨�鍊嶄箣鍐呮椂
													// 鍙栧墠randomMemberNum涓�
			randomMembers = new ArrayList<T>();
			for (int i = 0; i < randomMemberNum; i++) {
				randomMembers.add(values.get(i));
			}
			if (randomMembers.contains(exceptMember)) {
				randomMembers.remove(exceptMember);
				randomMembers.add(values.get(randomMemberNum));
			}
		} else {// 闅忔満randomMemberNum涓�
			randomMembers = new ArrayList<T>();
			while (randomMembers.size() < randomMemberNum) {
				T t = values.get(r.nextInt(size));
				if (!randomMembers.contains(t) && !exceptMember.equals(t)) {
					randomMembers.add(t);
				}
			}
		}
		return randomMembers;
	}

	@Override
	public void sremAll2(String key) {
		String idSetKey = getIdSetKey(key);
		String idSet = (String) this.get(idSetKey);
		if (!StringUtil.isEmpty(idSet)) {
			String[] values = idSet.split(",");
			for (String value : values) {
				String memberKey = getMemberKey(key, value);
				this.delete(memberKey);
			}
		}
		this.delete(idSetKey);
	}

	@Override
	public <T extends Serializable> Map<Long, T> smembers2(String key) {
		String idSetKey = getIdSetKey(key);
		String idSetStr = (String) this.get(idSetKey);

		List<Long> ids = StringUtil.split(idSetStr, Long.class);
		Map<Long, T> members = this.smembers2(key, ids);
		return members;
	}

	@SuppressWarnings("unchecked")
	public <T extends Serializable> Map<Long, T> smembers2(String key,
			List<? extends Number> identities) {
		int size = identities.size();
		String[] keys = new String[size];
		for (int i = 0; i < size; i++) {
			String mkey = getMemberKey(key, String.valueOf(identities.get(i)));
			keys[i] = mkey;
		}
		Map<String, Object> multi = this.mget(keys);
		Map<Long, T> members = new HashMap<Long, T>();
		if (multi != null) {
			String postfix = member_flag + separator;
			for (Entry<String, Object> entry : multi.entrySet()) {
				String memerKey = entry.getKey();
				Long mkey = Long.parseLong(memerKey.substring(memerKey
						.lastIndexOf(postfix) + postfix.length()));
				members.put(mkey, (T) entry.getValue());
			}
		}
		return members;
	}

	@Override
	public <T extends Serializable> List<T> smembersReturnList2(String key) {
		String idSetKey = getIdSetKey(key);
		String idsStr = (String) this.get(idSetKey);
		List<Long> ids = StringUtil.split(idsStr, Long.class);

		return smembersReturnList2(key, ids);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Serializable> List<T> smembersReturnList2(String key,
			List<? extends Number> identities) {
		List<T> members = new ArrayList<T>();
		int len = identities.size();

		String[] mkeys = new String[len];
		for (int i = 0; i < len; i++) {
			mkeys[i] = getMemberKey(key, String.valueOf(identities.get(i)));
		}

		Map<String, Object> multiValues = this.mget(mkeys);
		for (Object member : multiValues.values()) {
			members.add((T) member);
		}
		return members;
	}

	@Override
	public void sremAll(String key) {
		this.delete(key);
	}

	@Override
	public boolean sexists(String key) {
		return this.keyExists(key);
	}

	@Override
	public int incrByGetAndSet(String key) {
		Object data = this.get(key);
		int value;
		if (data == null) {
			value = 1;
			this.set(key, value);
		} else {
			value = (Integer) data + 1;
			this.set(key, value);
		}
		return value;
	}

	@Override
	public boolean lock(String key) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, 1);
		return this.add(key, true, calendar.getTime());
	}

	@Override
	public void unlock(String key) {
		this.delete(key);
	}

	@Override
	public boolean lock(String key, long expTime) {
		long now = System.currentTimeMillis();
		if (this.add(key, now)) {
			return true;
		}
		MemcachedItem item = this.gets(key);
		if (item != null) {
			long lockTime = (Long) item.getValue();
			if (now - lockTime >= expTime) {
				return this.cas(key, now, item.getCasUnique());
			}
		}
		return false;
	}

	/**
	 * 浠ヤ笅鏄彁渚涚粰澶ф暟鎹噺鐨剆add srem鐨勬柟娉�涓嶄繚璇佺嚎绋嬪畨鍏�澶氱嚎绋嬫坊鍔犳椂鏈夊彲鑳戒涪澶�鐩墠鍙彁渚沴ong绫诲瀷鐨勬敮鎸�鏆傛椂鍙敤鍒發ong绫诲瀷)
	 * 鍐呴儴瀹炵幇, 姣忎釜鍒嗗竷鐨刱ey鏈�瀵瑰簲2000鍏冪礌, 鍙傛暟涓殑key鐢ㄦ潵璁板綍鎬荤殑鍒嗗竷鐨刱ey鐨勪釜鏁�绉婚櫎涓�釜鍏冪礌鏃�浼氱Щ闄ゆ渶鍚庝釜鍏冪礌琛ュ埌鍓嶉潰
	 * random鐨勬椂鍊欎笉浼氶�鎷╂渶鍚庝竴涓垎甯冪殑key(鍙湁涓�釜鍒嗗竷key鐨勬儏鍐典緥澶�
	 * 
	 * @param member
	 */
	@Override
	public void auAdd(String key, long member) {
		// 鐩稿綋浜庨櫎浠�048
		int disNum = (int) (member - 10000 >> per_key_size_4au);
		disAdd(key, member, disNum);
	}

	@Override
	public List<Long> auRandomMembers(String key, int randomNum, long exceptId,
			long beginId) {
		Integer disNum = (Integer) this.get(key);
		int beginDis = (int) (beginId - 10000 >> per_key_size_4au);
		return disRandomMembers(key, randomNum, exceptId, disNum, beginDis);
	}

	@Override
	public void ruAdd(String key, long member) {
		int disNum = (int) (member - 10000 >> per_key_size_4ru);
		disAdd(key, member, disNum);

		String virKey = getVirKey(key);
		String vir = (String) this.get(virKey);
		if (vir == null || !"false".equals(vir)) {
			if (StringUtil.elementCount(vir) < vir_max_count) {
				vir = StringUtil.addElement(vir, String.valueOf(member));
				this.set(virKey, vir);
			} else {
				this.set(virKey, "false");
			}
		}
	}

	private void disAdd(String key, long member, int disNum) {
		Object o = this.get(key);
		if (o == null || ((Integer) o) < disNum) {
			this.set(key, disNum);
		}

		String disKey = getDisKey(key, disNum);
		String data = (String) this.get(disKey);
		data = StringUtil.addElement(data, String.valueOf(member));

		this.set(disKey, data);
	}

	@Override
	public List<Long> ruRandomMembers(String key, int randomNum, long exceptId,
			long beginId) {
		String virKey = getVirKey(key);
		String vir = (String) this.get(virKey);
		if (vir == null || !"false".equals(vir)) {
			return StringUtil.randomMemberByLong(randomNum, exceptId, vir);
		} else {
			Integer disNum = (Integer) this.get(key);
			int beginDis = (int) (beginId - 10000 >> per_key_size_4ru);
			return disRandomMembers(key, randomNum, exceptId, disNum, beginDis);
		}
	}

	@Override
	public void ruRemove(String key, long member) {
		int disNum = (int) (member - 10000 >> per_key_size_4ru);
		String disKey = getDisKey(key, disNum);

		String memberStr = String.valueOf(member);
		String disMembers = (String) this.get(disKey);
		disMembers = StringUtil.removeElement(disMembers, memberStr);
		this.set(disKey, disMembers);

		String virKey = getVirKey(key);
		String vir = (String) this.get(virKey);
		if (vir == null || !"false".equals(vir)) {
			vir = StringUtil.removeElement(vir, memberStr);
			this.set(virKey, vir);
		}
	}

	public void auRemove(String key, long member) {
		int disNum = (int) (member - 10000 >> per_key_size_4au);
		String disKey = getDisKey(key, disNum);

		String memberStr = String.valueOf(member);
		String disMembers = (String) this.get(disKey);
		disMembers = StringUtil.removeElement(disMembers, memberStr);
		this.set(disKey, disMembers);
	}

	private List<Long> disRandomMembers(String key, int randomNum,
			long exceptId, Integer disNum, int beginDis) {
		int randomDisNum = 0;
		if (disNum > beginDis) {
			randomDisNum = beginDis + r.nextInt(disNum - beginDis);
		}

		String disKey = getDisKey(key, randomDisNum);
		String members = (String) this.get(disKey);
		return StringUtil.randomMemberByLong(randomNum, exceptId, members);
	}

	public int per_key_size_4au = 11;
	public int per_key_size_4ru = 13;
	private static final int vir_max_count = 2000;

	private static String getDisKey(String baseKey, long disNum) {
		return new StringBuilder(baseKey).append("d").append(separator)
				.append(disNum).toString();
	}

	private static String getVirKey(String baseKey) {
		return new StringBuilder(baseKey).append(separator).append("vir")
				.toString();
	}

}
