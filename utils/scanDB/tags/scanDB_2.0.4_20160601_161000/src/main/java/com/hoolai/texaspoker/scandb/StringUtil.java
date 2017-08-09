/**
 * 
 */
package com.hoolai.texaspoker.scandb;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * 瀛楃涓插伐鍏风被
 * 
 * @author luzj
 */
public class StringUtil {

	/** 榛樿鐨勫瓧绗︿覆鏁扮粍缁勮鎴愬瓧绗︿覆鐨勫垎闅旂 */
	public final static String ELEMENT_SEPARATOR_COMMA = ",";

	public final static String ELEMENT_SEPARATOR_DOT = ".";

	public final static String ELEMENT_SEPARATOR_SEMICOLON = ":";

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	/**
	 * 浣跨敤,鍒嗛殧鐨勫瓧绗︿覆涓坊鍔犱竴涓猠lement
	 * 
	 * @param x
	 * @param element
	 * @return
	 */
	public static String addElement(String x, String element) {
		if (x == null || x.isEmpty()) {
			return element;
		}
		return new StringBuilder(x).append(ELEMENT_SEPARATOR_COMMA)
				.append(element).toString();
	}

	/**
	 * 浣跨敤REG鍒嗛殧鐨勫瓧绗︿覆remove鎺夊叾涓竴涓猠lement
	 * 
	 * @param element
	 * @param x
	 * @return
	 */
	public static String removeElement(String x, String element) {
		if (x == null) {
			return x;
		}
		if (x.equals(element)) {
			return "";
		}
		if (firstElement(x).equals(element)) {
			return x.substring(element.length() + 1);
		}
		if (lastElement(x).equals(element)) {
			return x.substring(0, x.length() - element.length() - 1);
		}
		return x.replace(ELEMENT_SEPARATOR_COMMA + element
				+ ELEMENT_SEPARATOR_COMMA, ELEMENT_SEPARATOR_COMMA);
	}

	/**
	 * 璇″紓鐨凷tring鍒犻櫎鍐呭瀹炵幇銆傘�銆傞潪甯歌繚娉曘�銆�
	 * 
	 * 鏇存敼涓烘甯哥殑瀹炵幇銆傘�銆�
	 * 
	 * @param x
	 * @param element
	 * @param spliter
	 * @return
	 * @author Cedric(TaoShuang)
	 */
	public static String deleteContent(String x, String element, String spliter) {
		if (x != null && element != null && spliter != null) {
			StringBuilder builder = new StringBuilder(x);
			int i = -1;
			int j = -1;
			i = element.length();
			j = builder.indexOf(spliter + element);
			if (j != -1) {
				return builder.delete(j, i + j + 1).toString();
			} else {
				j = builder.indexOf(element);
				if (j != -1) {
					return builder.delete(j, i + j + 1).toString();
				} else {
					return x;
				}
			}
		} else {
			return x;
		}
	}

	public static String removeFirst(String x) {
		int index = x.indexOf(ELEMENT_SEPARATOR_COMMA);
		if (index == -1) {
			return "";
		}
		return x.substring(index + 1);
	}

	/**
	 * 鏌ユ壘string涓互REG鍒嗛殧鐨勫厓绱犵殑鎬讳釜鏁�
	 * 
	 * @param x
	 * @return
	 */
	public static int elementCount(String x) {
		if (isEmpty(x)) {
			return 0;
		}

		int count = 0;
		int index = 0;
		while (index != -1) {
			index = x.indexOf(ELEMENT_SEPARATOR_COMMA, index + 1);
			count++;
		}
		return count;
	}

	/**
	 * 鏄惁瀛樺湪
	 * 
	 * @param x
	 * @param element
	 * @return
	 */
	public static boolean existsElement(String x, String element) {
		if (x == null) {
			return false;
		}
		if (firstElement(x).equals(element)) {
			return true;
		}
		if (lastElement(x).equals(element)) {
			return true;
		}
		return x.indexOf(ELEMENT_SEPARATOR_COMMA + element
				+ ELEMENT_SEPARATOR_COMMA) >= 0;
	}

	/**
	 * 浠lement_sepparator鎷嗗垎
	 * 
	 * @param <T>
	 * @param x
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> split(String x, Class<T> clazz) {
		return split(x, clazz, ELEMENT_SEPARATOR_COMMA);
	}

	public static <T> List<T> split(String x, Class<T> clazz,
			String elementSeparator) {
		List<T> members = new ArrayList<T>();
		if (isEmpty(x)) {
			return members;
		}
		int beginIndex = 0;
		while (beginIndex != -1) {
			int endIndex = x.indexOf(elementSeparator, beginIndex);
			if (endIndex != -1) {
				members.add(valueOf(x.substring(beginIndex, endIndex), clazz));
				beginIndex = endIndex + 1;
			} else {
				members.add(valueOf(x.substring(beginIndex), clazz));
				beginIndex = -1;
			}
		}
		return members;
	}

	/**
	 * 浠lement_sepparator鎷嗗垎
	 * 
	 * @param <T>
	 * @param x
	 * @param clazz
	 * @return
	 */
	public static int[] splitAsInt(String x) {
		if (isEmpty(x)) {
			return new int[0];
		}
		int[] members = new int[elementCount(x)];
		int beginIndex = 0;
		int i = 0;
		while (beginIndex != -1) {
			int endIndex = x.indexOf(ELEMENT_SEPARATOR_COMMA, beginIndex);
			int member;
			if (endIndex != -1) {
				member = Integer.parseInt(x.substring(beginIndex, endIndex));
				beginIndex = endIndex + 1;
			} else {
				member = Integer.parseInt(x.substring(beginIndex));
				beginIndex = -1;
			}
			members[i] = member;
			i++;
		}
		return members;
	}

	/**
	 * 鑾峰彇浠EG鍒嗛殧鐨剆tring涓殑绗竴涓厓绱�
	 * 
	 * @param x
	 * @return
	 */
	public static String firstElement(String x) {
		int index = x.indexOf(ELEMENT_SEPARATOR_COMMA);
		if (index == -1) {
			return x;
		}
		return x.substring(0, index);
	}

	/**
	 * 鑾峰彇浠EG鍒嗛殧鐨剆tring涓殑鏈�悗涓�釜鍏冪礌
	 * 
	 * @param x
	 * @return
	 */
	public static String lastElement(String x) {
		int index = x.lastIndexOf(ELEMENT_SEPARATOR_COMMA);
		if (index == -1) {
			return x;
		}
		return x.substring(index + 1);
	}

	private static Map<Class<?>, Method> valueOfMap = new HashMap<Class<?>, Method>();
	static {
		try {
			Method l = Long.class.getMethod("valueOf", String.class);
			valueOfMap.put(Long.class, l);
			valueOfMap.put(long.class, l);
			Method i = Integer.class.getMethod("valueOf", String.class);
			valueOfMap.put(Integer.class, i);
			valueOfMap.put(int.class, i);
			Method f = Float.class.getMethod("valueOf", String.class);
			valueOfMap.put(Float.class, f);
			valueOfMap.put(float.class, f);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		}
	}

	/**
	 * 闅忔満鍙栧厓绱�杩斿洖long绫诲瀷list
	 * 
	 * @param randomNum
	 * @param exceptId
	 * @param members
	 * @return
	 */
	public static List<Long> randomMemberByLong(int randomNum, long exceptId,
			String members) {
		Random r = new Random();
		List<Long> randomMembers = new ArrayList<Long>();

		if (isEmpty(members)) {
			return randomMembers;
		}

		int membersSize = 0;
		int len = members.length();
		if (len < 300) {
			int beginIndex = 0;
			while (membersSize < randomNum) {
				int endIndex = members.indexOf(ELEMENT_SEPARATOR_COMMA,
						beginIndex);
				if (endIndex == -1) {
					long randomMember = Long.parseLong(members
							.substring(beginIndex));
					if (randomMember != exceptId) {
						randomMembers.add(randomMember);
					}
					return randomMembers;
				}
				long randomMember = Long.parseLong(members.substring(
						beginIndex, endIndex));
				if (randomMember != exceptId) {
					randomMembers.add(randomMember);
					membersSize++;
				}
				beginIndex = endIndex + 1;
			}
		} else {
			while (membersSize < randomNum) {
				int beginIndex = r.nextInt(len);
				beginIndex = members.indexOf(ELEMENT_SEPARATOR_COMMA,
						beginIndex);
				if (beginIndex == -1) {
					continue;
				}
				int endIndex = members.indexOf(ELEMENT_SEPARATOR_COMMA,
						beginIndex + 1);
				if (endIndex == -1) {
					continue;
				}
				long randomMember = Long.parseLong(members.substring(
						beginIndex + 1, endIndex));
				if (randomMember != exceptId
						&& !randomMembers.contains(randomMember)) {
					randomMembers.add(randomMember);
					membersSize++;
				}
			}
		}
		return randomMembers;
	}

	/**
	 * 鎶妔tring杞崲鎴恘umber 浠ュ強string绫诲瀷
	 * 
	 * @param <T>
	 * @param value
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T valueOf(String value, Class<T> clazz) {
		if (value == null) {
			return null;
		}

		Method valueOfMethod = valueOfMap.get(clazz);
		if (valueOfMethod == null) {
			return (T) value;
		}

		try {
			return (T) valueOfMethod.invoke(null, value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 鍒ゆ柇涓�釜瀛楃涓叉槸鍚︾敱0-9缁勬垚
	 * 
	 * @param x
	 * @return
	 */
	public static boolean isNumber(String x) {
		int len;
		if (x == null || (len = x.length()) == 0)
			return false;
		while (len-- > 0) {
			char temp = x.charAt(len);
			if (temp > 57 || temp < 48)
				return false;
		}
		return true;
	}

	/**
	 * 鍒ゆ柇瀛楃涓叉槸鍚︿负绌�null or "" return true; "  " return true; " pop" return false;
	 * "  pop  " return false;
	 * 
	 * @param data
	 * @return
	 */
	public static boolean isBlank(String data) {
		int len;
		if (data == null || (len = data.length()) == 0)
			return true;

		while (len-- > 0) {
			if (!Character.isWhitespace(data.charAt(len)))
				return false;
		}

		return true;
	}

	/**
	 * 
	 * 涓�釜姹夊瓧绠椾袱涓瓧绗�
	 * 
	 * @param data
	 * @return
	 */
	public static int bytelength(String data) {
		try {
			return data.getBytes("GBK").length;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 浣跨敤md5绠楁硶鍔犲瘑瀛楃涓�
	 * 
	 * @param x
	 * @return
	 */
	public static String encryptToMd5(String x) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(x.getBytes("UTF-8"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		byte[] byteArray = messageDigest.digest();
		StringBuilder md5SB = new StringBuilder();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5SB.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5SB.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5SB.toString();
	}

	/**
	 * 鎶�鍒�杩�0涓暟瀛楁寜鐓ч厤缃殑鏄犲皠琛ㄦ槧灏勬垚涓哄彟澶栫殑10涓瓧绗︼紝 鐒跺悗鍦ㄦ妸鐩搁偦涓や釜瀛楃浜ゆ崲涓�笅浣嶇疆锛�
	 * 濡傛灉瀛楃涓查暱搴︿负濂囨暟涓紝閭ｄ箞鏈�悗涓�釜鏁板瓧涓嶅弬涓庡瓧绗︿氦鎹㈠鐞嗐�
	 * 鍋囪鏄犲皠琛ㄤ负9876543210锛屼篃灏辨槸璇村師鏉ョ殑0琚槧灏勪负9锛岃�鍘熸潵鐨�琚槧灏勪负0锛屼腑闂翠互姝ょ被鎺ㄣ�
	 * 鑰岃緭鍏ョ殑鐢ㄦ埛id涓�0001锛岄偅涔堟槧灏勫悗鐨勬暟鎹簲璇ユ槸89998銆傝�缁忚繃浜ゆ崲澶勭悊鍚庢槸98998銆�
	 * 
	 * @param number
	 * @return
	 */
	public static String encryptNumber(long number) {
		List<Integer> arr = convertNumberList(number);
		int[] newArr = convertByMapping(arr);
		swapPerTowElement(newArr);
		return arrayToString(newArr);
	}

	/**
	 * 姣忎袱涓浉閭绘暟瀛椾箣闂翠氦鎹㈤『搴�
	 * 
	 * @param arr
	 */
	private static void swapPerTowElement(int[] arr) {
		int len = arr.length;
		for (int i = 1; i < len; i += 2) {
			swap(arr, i, i - 1);
		}
	}

	private static void swap(int[] arr, int i, int j) {
		int t = arr[i];
		arr[i] = arr[j];
		arr[j] = t;
	}

	/**
	 * 鎶奿nt[]杞崲鎴愬瓧绗︿覆
	 * 
	 * @param arr
	 * @return
	 */
	private static String arrayToString(int[] arr) {
		int len = arr.length;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(arr[i]);
		}
		return sb.toString();
	}

	/**
	 * 0 --> 9 1 --> 8 ... 8 --> 1 9 --> 0 鐢变簬convertToList鐨勬椂鍊欐妸椤哄簭寮勫弽浜�杩欓噷鎶婇『搴忓紕姝ｇ‘
	 * 
	 * @param arr
	 * @param len
	 * @return
	 */
	private static int[] convertByMapping(List<Integer> arr) {
		int len = arr.size();
		int[] newArr = new int[len];
		for (int i = len; i-- > 0;) {
			int n = arr.get(i);
			int m = 9 - n;
			newArr[len - 1 - i] = m;
		}
		return newArr;
	}

	/**
	 * 12345 --> {5,4,3,2,1}
	 * 
	 * @param number
	 * @return
	 */
	private static List<Integer> convertNumberList(long number) {
		List<Integer> arr = new ArrayList<Integer>();
		do {
			int t = (int) (number % 10);
			arr.add(t);
			number = number / 10;
		} while (number > 0);
		return arr;
	}

	/**
	 * 闅忔満鍙栧厓绱�
	 * 
	 * @param randomNum
	 * @param exceptMember
	 * @param members
	 * @return
	 */
	public static List<String> randomMembers(int randomNum,
			String exceptMember, String members) {
		List<String> randomMembers = new ArrayList<String>();
		if (isEmpty(members)) {
			return randomMembers;
		}

		Random r = new Random();
		int membersSize = 0;
		int len = members.length();
		while (membersSize < randomNum) {
			int beginIndex = r.nextInt(len);

			beginIndex = members.indexOf(ELEMENT_SEPARATOR_COMMA, beginIndex);
			if (beginIndex == -1) {
				continue;

			}
			int endIndex = members.indexOf(ELEMENT_SEPARATOR_COMMA,
					beginIndex + 1);
			if (endIndex == -1) {
				continue;
			}
			String randomMember = members.substring(beginIndex + 1, endIndex);
			if (randomMember != exceptMember
					&& !randomMembers.contains(randomMember)) {
				randomMembers.add(randomMember);
				membersSize++;
			}
		}

		return randomMembers;
	}

	/**
	 * 鑾峰彇瀛楃涓插紑澶寸殑鍑犱釜鍏冪礌
	 * 
	 * @param num
	 * @param exceptMember
	 * @param members
	 * @return
	 */
	public static List<String> getFistMembers(int num, String exceptMember,
			String members) {
		List<String> rMembers = new ArrayList<String>();

		int beginIndex = 0;
		int membersSize = 0;
		while (membersSize < num) {
			int endIndex = members.indexOf(ELEMENT_SEPARATOR_COMMA, beginIndex);
			if (endIndex == -1) {
				String randomMember = members.substring(beginIndex);
				if (randomMember != exceptMember) {
					rMembers.add(randomMember);
				}
				return rMembers;
			}
			String randomMember = members.substring(beginIndex, endIndex);
			if (randomMember != exceptMember) {
				rMembers.add(randomMember);
				membersSize++;
			}
			beginIndex = endIndex + 1;
		}
		return rMembers;
	}

	/**
	 * 鍒ゆ柇瀛楃涓叉槸鍚︿负绌�null or "" return true; "  " return true; " pop" return false;
	 * "  pop  " return false;
	 * 
	 * @param x
	 * @return
	 */
	public static boolean isEmpty(String x) {
		int len;
		if (x == null || (len = x.length()) == 0)
			return true;

		while (len-- > 0) {
			if (!Character.isWhitespace(x.charAt(len)))
				return false;
		}

		return true;
	}

	/**
	 * trim
	 * 
	 * @param x
	 * @return
	 */
	public static String trim(String x) {
		if (x == null)
			return null;

		return x.trim();
	}

	/**
	 * 浣跨敤md5绠楁硶鍔犲瘑瀛楃涓�
	 * 
	 * @param x
	 * @return
	 */
	public static String encrypt(String x) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(x.getBytes("UTF-8"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		byte[] byteArray = messageDigest.digest();
		StringBuilder md5SB = new StringBuilder();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5SB.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5SB.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5SB.toString();
	}

	/**
	 * 绠楁硶鍚嶇О hmacsha1
	 */
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	/**
	 * hmacsha256
	 */
	private static String HMAC_SHA256_ALGORITHM = "HMACSHA256";;

	/**
	 * 浣跨敤hmacSHA1鍔犲瘑骞惰浆鎹㈡垚16浣嶅瓧绗︿覆
	 * 
	 * @param key
	 * @param data
	 * @return
	 */
	public static String hmacSHA1(String key, String data) {
		BigInteger hash = new BigInteger(1, hmacSHA1ToBytes(key, data));

		String hmac = hash.toString(16);
		if (hmac.length() % 2 != 0) {
			hmac = "0" + hmac;
		}
		return hmac;
	}

	/**
	 * 浣跨敤hmacSHA1绠楁硶鍔犲瘑
	 * 
	 * @param key
	 * @param data
	 * @return
	 */
	public static byte[] hmacSHA1ToBytes(String key, String data) {
		try {
			// Get an hmac_sha1 key from the raw key bytes
			byte[] keyBytes = key.getBytes();
			SecretKeySpec signingKey = new SecretKeySpec(keyBytes,
					HMAC_SHA1_ALGORITHM);

			// Get an hmac_sha1 Mac instance and initialize with the signing key
			Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);

			return mac.doFinal(data.getBytes());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param key
	 * @param data
	 * @return
	 */
	public static byte[] hmacsha256ToBytes(String key, String data) {
		try {
			SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(),
					HMAC_SHA256_ALGORITHM);
			Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
			mac.init(secretKeySpec);
			return mac.doFinal(data.getBytes());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
