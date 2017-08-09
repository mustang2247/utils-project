/**
 * 
 */
package com.hoolai.ccgames.scandb.utils;

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
	 * 添加元素并限制整个列表的长度 需要删除时将删除最早的元素
	 * 
	 * @param <T>
	 * @param key
	 * @param value
	 * @param identity
	 * @param limit
	 *            整个列表的最大长度
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
	 * 在list列表的position位置插入值value 如果identity已经在列表里了，则移动到正确的位置
	 * 
	 * @param key
	 *            列表键名
	 * @param value
	 *            要插入的值
	 * @param position
	 *            从0开始
	 * @param identity
	 *            列表项Id
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
	 * 获取list列表内容
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
	 * 移除list最后的元素
	 * 
	 * @param key
	 * @return 被移除的元素
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
	 * 移除list第一个元素
	 * 
	 * @param key
	 * @return 被移除的元素
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
	 * 在ids列表里的指定位置插入element 如果已经有element，则删除原位置，
	 * 这里假设位置不因删除原位置而变化（即旧element永远在position后面）
	 * 
	 * @param list1
	 *            {@link #element_separator}分割的列表
	 * @param element
	 *            待插入的元素
	 * @param position
	 *            从0开始
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
		// 作为long型的最大数值占用字符长度
		int longLength = 20;
		int splitMaxLength = 300;
		if (idsStr.length() < splitMaxLength) {
			String[] values = idsStr.split(",");
			int randomIndex = r.nextInt(values.length);
			id = values[randomIndex];
		} else {
			// 舍弃最后longLength字符的长度，保证至少有一个id
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
		if (randomMemberNum >= size) {// 不够randomMemberNum时返回所有
			randomMembers = values;
		} else if (size < (randomMemberNum * 3)) {// 当全部的member在randomMemberNum的3倍之内时
													// 取前randomMemberNum个
			while ((size = values.size()) > randomMemberNum) {
				values.remove(r.nextInt(size));
			}
			randomMembers = values;
		} else {// 随机randomMemberNum个
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
		if (randomMemberNum >= size) {// 不够randomMemberNum时返回所有
			randomMembers = values;
			randomMembers.remove(exceptMember);
		} else if (size <= (randomMemberNum * 3)) {// 当全部的member在randomMemberNum的3倍之内时
													// 取前randomMemberNum个
			randomMembers = new ArrayList<T>();
			for (int i = 0; i < randomMemberNum; i++) {
				randomMembers.add(values.get(i));
			}
			if (randomMembers.contains(exceptMember)) {
				randomMembers.remove(exceptMember);
				randomMembers.add(values.get(randomMemberNum));
			}
		} else {// 随机randomMemberNum个
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
	 * 以下是提供给大数据量的sadd srem的方法 不保证线程安全,多线程添加时有可能丢失 目前只提供long类型的支持(暂时只用到long类型)
	 * 内部实现, 每个分布的key最多对应2000元素, 参数中的key用来记录总的分布的key的个数 移除一个元素时,会移除最后个元素补到前面
	 * random的时候不会选择最后一个分布的key(只有一个分布key的情况例外)
	 * 
	 * @param member
	 */
	@Override
	public void auAdd(String key, long member) {
		// 相当于除以2048
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
