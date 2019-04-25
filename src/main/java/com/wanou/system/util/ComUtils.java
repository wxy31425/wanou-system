package com.wanou.system.util;

import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComUtils {

	public static boolean IsNullOrEmpty(String inpStr) {
		if (inpStr == null || inpStr.equals("")) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 随机生成指定位数的数字
	 * @param length 要生成的数字的位数
	 * @return 生成的数字字符串
	 */
	public static String getRandomNumber(int length) {
		String result = "";
		Random random = new Random();
		for(int i=0; i<length; i++) {
			result += random.nextInt(10);
		}
		return result;
	}
	private static final String DATE_STYLE = "yyyy-MM-dd";
	private static final String TIME_STYLE = "yyyy-MM-dd HH:mm:ss";
	private static final String TIMEID_STYLE = "yyyy-MM-dd HH.mm.ss";
	public static final String xmltitle = "<?xml version=\"1.0\" encoding=\"GB2312\"?>";
	private static final Log log = LogFactory.getLog(ComUtils.class);
	public static String vison;
	public static HashMap<String, String> numPhone;
	public static String phone;
	public static String zhuanhunum;
	private static Document config;
	public static HashMap<String, String> phonenumMap = new HashMap<String, String>();

	public static String getId() {
		//return UUID.randomUUID().toString().replaceAll("-", "");
		return UUID.randomUUID().toString();
	}

	public static boolean pattern(String patternstr, String r) {
		Pattern pattern = Pattern.compile(patternstr);
		Matcher matcher = pattern.matcher(r);
		return matcher.matches();
	}
	public static void loadConfig(Document config) {
		ComUtils.config = config;
		String numstr = getConfigValueByKey("phonenum");
		zhuanhunum = numstr;
		String[] array = numstr.split(";");
		ComUtils.numPhone = new HashMap<String, String>();
		String[] num;
		phone = ";";
		for (int i = 0; i < array.length; i++) {
			num = array[i].split(":");
			numPhone.put(num[1], num[0]);
			phone += (num[1] + ";");
		}
	}

	public static String getConfigValueByKey(String key) {
		Element node = config.getRootElement().element(key);
		if (node == null) {
			return null;
		}
		return node.getTextTrim();
	}

	public static String getSubString(String str, int length) {
		String str1 = str;
		if (str == null) {
			return "";
		}
		if (str1.length() < length) {
			return str1;
		} else {
			return str1.substring(length) + "…";
		}
	}

	public static String replaceSpace2nbsp(String str) {
		String str1 = str;
		if (str1 == null || str1.trim().equals("") || str1.toLowerCase().equals("null"))
			str1 = "&nbsp;";
		return str1.trim();
	}

	public static String replaceNull2Space(String s) {
		if (s == null)
			return "";
		if (s.trim().toUpperCase().equals("NULL"))
			return "";
		return s.trim();
	}

	/**
	 * @author donghc 将类属性转化为数据库表对应字段
	 * @param property
	 *            String
	 * @return String
	 */
	public static String propertyToField(String property) {
		String field = "";
		if (property != null && !property.equals("")) {
			int size = property.length();
			char[] _charArray = property.toCharArray();
			if (_charArray != null && size > 0) {
				for (int i = 0; i < size; i++) {
					if (_charArray[i] >= 65 && _charArray[i] <= 90) {
						_charArray[i] = (char) (_charArray[i] + 32);
						field += "_" + _charArray[i];
					} else {
						field += _charArray[i];
					}
				}
			}

		}
		return field;
	}

	/**
	 * @author donghc 将数据库表对应字段转化为类属性
	 * @return String
	 */
	public static String FieldToProperty(String field) {
		String property = "";
		if (field != null && !field.equals("")) {
			int size = field.length();
			char[] _charArray = field.toCharArray();
			if (_charArray != null && size > 0) {
				for (int i = 0; i < size; i++) {
					if (_charArray[i] == '_') {
						i += 1;
						if (i < size) {
							if (_charArray[i] >= 97 && _charArray[i] <= 122) {
								_charArray[i] = (char) (_charArray[i] - 32);
							}
							property += _charArray[i];
						}
					} else {
						property += _charArray[i];
					}
				}
			}

		}
		return property;
	}

	/**
	 * @author zhuz double and bigdecimal is availiable right now
	 * @param obj
	 * @return
	 */
	public static BigDecimal convertSqlNumberObjectToBigDecimal(Object obj) {
		if (obj instanceof Double) {
			return BigDecimal.valueOf((Double) obj);
		}
		if (obj instanceof BigDecimal) {
			String f = obj.toString();
			if (f.length() > 2 && f.substring(0, 2).equalsIgnoreCase("0E"))
				return BigDecimal.valueOf(0);
			String lastChar;
			lastChar = String.valueOf(f.charAt(f.length() - 1));
			while ("0".equals(lastChar) || ".".equals(lastChar)) {
				f = f.substring(0, f.length() - 1);
				lastChar = String.valueOf(f.charAt(f.length() - 1));
			}
			return BigDecimal.valueOf(Float.valueOf(f));
		}

		return (BigDecimal) obj;

	}

	public static String Integer2String(Integer i) {
		if (i == null)
			return "";
		return i.toString();
	}

	public static String Date2String(Object dt) {
		if (dt == null || dt.equals(""))
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_STYLE);
		try {
			return sdf.format(dt);
		} catch (Exception ex) {
			log.error("==ComUtil:Date2String==：" + ex);
			return "";
		}
	}

	public static String Time2String(Date dt) {
		if (dt == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(TIME_STYLE);
		try {
			return sdf.format(dt);
		} catch (Exception ex) {
			log.error("==ComUtil:Time2String==：" + ex);
			return "";
		}
	}

	public static String Time2StringId(Date dt) {
		if (dt == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(TIMEID_STYLE);
		try {
			return sdf.format(dt);
		} catch (Exception ex) {
			log.error("==ComUtil:Time2String==：" + ex);
			return "";
		}
	}

	public static String Time2StringNoSecond(Date dt) {
		if (dt == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(TIME_STYLE);
		try {
			String timeNoSecond = sdf.format(dt);
			return timeNoSecond.substring(0, timeNoSecond.length() - 3);
		} catch (Exception ex) {
			log.error("==ComUtil:Time2String==：" + ex);
			return "";
		}
	}

	/**
	 * 时间日期替换为yyyy年mm月dd日
	 * 
	 * @param dt
	 *            Date
	 * @return String
	 */
	public static String Time2StringYMD(Date dt) {
		if (dt == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(TIME_STYLE);
		try {
			String timeNoSecond = sdf.format(dt);
			String yyyy = timeNoSecond.substring(0, 4);
			String mm = timeNoSecond.substring(5, 7);
			String dd = timeNoSecond.substring(8, 10);
			return yyyy + "年" + mm + "月" + dd + "日";
		} catch (Exception ex) {
			log.error("==ComUtil:Time2String==：" + ex);
			return "";
		}
	}

	/**
	 * 日期替换为yyyy年mm月dd日
	 * 
	 * @param dt
	 *            Date
	 * @return String
	 */
	public static String Date2StringYMD(Date dt) {
		if (dt == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_STYLE);
		try {
			String timeNoSecond = sdf.format(dt);
			String yyyy = timeNoSecond.substring(0, 4);
			String mm = timeNoSecond.substring(5, 7);
			String dd = timeNoSecond.substring(8, 10);
			return yyyy + "年" + mm + "月" + dd + "日";
		} catch (Exception ex) {
			log.error("==ComUtil:Time2String==：" + ex);
			return "";
		}
	}

	public static String getTimeNoSecond(String time) {
		if (time == null)
			return "";
		if (time.length() <= 10)
			return time;
		return time.substring(0, time.length() - 3);
	}

	public static Date String2Time(String time) {
		if ("".equals(replaceNull2Space(time))) {
			return null;
		}
		if (time.length() < 19) {
			time = time + ":00";
		}
		SimpleDateFormat format = new SimpleDateFormat(TIME_STYLE);
		Date d = null;
		if (time != null && !time.equals("")) {
			try {
				d = format.parse(time);
			} catch (ParseException ex) {
				log.error("==ComUtil:String2Time==：" + ex);
			}
		}
		return d;
	}

	public static Date String2TimeForBeginQuery(String date) {
		if ("".equals(replaceNull2Space(date))) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat(TIME_STYLE);
		Date d = null;
		if (date != null && !date.equals("")) {
			try {
				d = format.parse(date + " 00:00:00");
			} catch (ParseException ex) {
				log.error("==ComUtil:String2Time==：" + ex);
			}
		}
		return d;
	}

	public static Date String2TimeForQuery(String date) {
		if ("".equals(replaceNull2Space(date))) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat(TIME_STYLE);
		Date d = null;
		if (date != null && !date.equals("")) {
			try {
				d = format.parse(date + " 23:59:59");
			} catch (ParseException ex) {
				log.error("==ComUtil:String2Time==：" + ex);
			}
		}
		return d;
	}

	public static Date String2Date(String date) {
		if ("".equals(replaceNull2Space(date))) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat(DATE_STYLE);
		Date d = null;
		if (date != null && !date.equals("")) {
			try {
				d = format.parse(date);
			} catch (ParseException ex) {
				log.error("==ComUtil:String2Date==：" + ex);
			}
		}
		return d;
	}

	public static String getDateStyle() {
		return DATE_STYLE;
	}

	public static String getTimeStyle() {
		return TIME_STYLE;
	}

	/**
	 * 获取应用服务器系统日期、时间
	 * 
	 * @return 日期+时间字符串。格式“yyyy-mm-dd hh:mm:ss”
	 */
	static public String getTimeStamp() {
		return Time2String(new Date());
	}

	/**
	 * 获取应用服务器系统日期
	 * 
	 * @return 日期。格式“yyyy-mm-dd”
	 */
	static public String getDateStamp() {
		Calendar ca = Calendar.getInstance();
		return (ca.get(Calendar.YEAR) + "年" + (ca.get(Calendar.MONTH) + 1) + "月" + ca.get(Calendar.DAY_OF_MONTH)) + "日";
	}

	/**
	 * 恶意脚本过滤函数
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
	public static String inHTML(String str) {
		String sTemp;
		sTemp = str;
		if (sTemp == null || sTemp.equals("")) {
			return "";
		}
		sTemp = sTemp.replaceAll("&", "&amp;");
		sTemp = sTemp.replaceAll("<", "&lt;");
		sTemp = sTemp.replaceAll(">", "&gt;");
		sTemp = sTemp.replaceAll("\"", "&quot;");
		return sTemp;
	}

	/**
	 * xml流程文件中的特殊符号替换
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
	public static String xmlReplace(String str) {
		String sTemp;
		sTemp = str;
		if (sTemp == null || sTemp.equals("")) {
			return "";
		}
		sTemp = sTemp.replaceAll("&lt;", "<");
		sTemp = sTemp.replaceAll("&gt;", ">");
		return sTemp;
	}

	public static String SubString(String source, int length) {
		if (source == null || source.trim().equals(""))
			return "";
		int _length = source.length() < length ? source.length() : length;
		return source.substring(0, _length);
	}

	// public static String getModuleName(String moduleId) {
	// ComQuery query = new ComQuery(false);
	// return query.moduleID2moduleName(moduleId);
	// }
	/**
	 * 替换字符串中的字符串
	 * 
	 * @param str
	 *            字符串
	 * @param str1
	 *            被替换字符串
	 * @param str2
	 *            替换字符串
	 * @return String 替换后的字符串
	 */
	static public String strReplace(String str, String str1, String str2) {
		String ret = "";
		int i;
		if (str == null)
			return ("");
		while ((i = str.indexOf(str1)) >= 0) {
			ret = ret + str.substring(0, i) + str2;
			str = str.substring(i + str1.length());
		}
		ret = ret + str;
		return (ret);
	}

	/**
	 * 去掉字符串前的所有0 add by sunqm
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
	public static String replace0Front(String str) {
		String strReplaced = "";

		while (null != str && str.length() > 0 && str.substring(0, 1).equals("0")) {
			strReplaced = str.replaceFirst("0", "");
		}
		return strReplaced;
	}

	/**
	 * 首字母大写 add by Zhuz
	 */
	public static String upperFirstChar(String words) {
		if (words == null || words.length() < 0) {
			throw new RuntimeException("系统首字母转换出错！");
		}
		String firstChar = words.substring(0, 1);
		return firstChar.toUpperCase() + words.substring(1, words.length());
	}

	/**
	 * @author donghc 获取str中间隔符regex1与regex2之间的子串
	 * @param str
	 *            String
	 * @param regex1
	 *            String
	 * @param regex2
	 *            String
	 * @return String
	 */
	public static String getSubString(String str, String regex1, String regex2) {
		String subString = "";
		if ("".equals(ComUtils.replaceNull2Space(str))) {
			return subString;
		}
		if (str != null && !str.equals("") && regex1 != null && !regex1.equals("")) {
			int a = str.indexOf(regex1);
			int b;
			if ("".equals(regex2) || regex2 == null) {
				b = str.length();
			} else {
				b = str.lastIndexOf(regex2);
			}
			if (a > -1 && b > -1) {
				subString = str.substring(a + regex1.length(), b);
			}
		}
		return subString;
	}

	/**
	 * @author donghc 获取str中间隔符regex1与regex2之间的子串
	 * @param str
	 *            String
	 * @param regex1
	 *            String
	 * @param regex2
	 *            String
	 * @param regex3
	 *            String
	 * @return String
	 */
	public static String getSubString(String str, String regex1, String regex2, String regex3) {
		String subString = "";
		if (str != null && !str.equals("") && regex1 != null && !regex1.equals("") && regex2 != null && !regex2.equals("") && regex3 != null && !regex3.equals("")) {
			String _char = "";
			int begin = 0;
			int end = 0;
			boolean k1 = false;
			boolean k2 = false;
			for (int i = 0; i < str.length(); i++) {
				_char = str.substring(i, i + 1);
				if (!k1 && _char.equals(regex1)) {
					begin = i + 1;
					k1 = true;
				} else if (k1 && _char.equals(regex2)) {
					end = i;
					k2 = true;
				}
				if (k1 && k2) {
					String subStr = str.substring(begin, end);
					subString += subStr + regex3;
					k1 = false;
					k2 = false;
				}
			}
			if (subString.indexOf(regex3) > -1) {
				subString = subString.substring(0, subString.length() - regex3.length());
			}
		}
		return subString;
	}

	/**
	 * @author donghc 在str2中查询子串str1
	 * @param str1
	 *            子串
	 * @param str2
	 *            父串
	 * @param regex
	 *            父串分隔符
	 * @return String
	 */
	public static boolean findSubStr2(String str1, String str2, String regex) {
		if (str1 != null && !str1.equals("") && str2 != null && !str2.equals("") && regex != null && !regex.equals("")) {
			for (int i = 0; i < str2.split(regex).length; i++) {
				String str = str2.split(regex)[i];
				if (str1.equals(str)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @author donghc 在str2中查询子串str1
	 * @param str1
	 *            子串
	 * @param str2
	 *            父串
	 * @param regex
	 *            父串分隔符
	 * @return String
	 */
	public static boolean findSubStr(String str1, String str2, String regex) {
		if (str1 != null && !str1.equals("") && str2 != null && !str2.equals("") && regex != null && !regex.equals("")) {
			String[] str2Array = str2.split(regex);
			Map<String, String> str2Map = new HashMap<String, String>();
			for (int i = 0; i < str2Array.length; i++) {
				String _substr2 = str2Array[i];
				str2Map.put(_substr2, _substr2);
			}
			String _str1 = (String) str2Map.get(str1);
			if (_str1 != null && !_str1.equals("")) {
				return true;
			}

		}

		return false;
	}

	/**
	 * @author donghc 在str2中查询子串str1
	 * @param str1
	 *            子串
	 * @param str2
	 *            父串
	 * @param regex1
	 *            子串分隔符
	 * @param regex2
	 *            父串分隔符
	 * @return String
	 */
	public static boolean findSubStrForPart(String str1, String str2, String regex1, String regex2) {
		if (str1 != null && !str1.equals("") && str2 != null && !str2.equals("") && regex1 != null && !regex1.equals("") && regex2 != null && !regex2.equals("")) {
			for (int i = 0; i < str1.split(regex1).length; i++) {
				String str = str1.split(regex1)[i];
				boolean k = findSubStr(str, str2, regex2);
				if (k) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @author donghc
	 * @过滤str,去掉regex分隔的重复子项,保持顺序
	 * @param str
	 *            String
	 * @param regex
	 *            String
	 * @return String
	 */
	public static String filteStr(String str, String regex) {
		String newStr = "";
		if (str != null && !str.equals("") && regex != null && !regex.equals("")) {
			String strArray[] = str.split(regex);
			for (int i = 0; i < strArray.length; i++) {
				String tempStr = strArray[i];
				if (findSubStr(tempStr, newStr, regex)) {
					continue;
				}
				newStr += tempStr + regex;
			}
			if (newStr.indexOf(regex) > -1) {
				newStr = newStr.substring(0, newStr.length() - regex.length());
			}
		}
		return newStr;
	}

	/**
	 * @author donghc
	 * @过滤str,去掉regex分隔的重复子项,无顺序
	 * @param str
	 *            String
	 * @param regex
	 *            String
	 * @return String
	 */
	public static String filteStrNoSeq(String str, String regex) {
		String newStr = "";
		if (str != null && !str.equals("") && regex != null && !regex.equals("")) {
			String strArray[] = str.split(regex);
			Map<String, String> strMap = new HashMap<String, String>();
			for (int i = 0; i < strArray.length; i++) {
				String tempStr = strArray[i];
				strMap.put(tempStr, tempStr);
				// newStr += tempStr+regex;
			}
			Set<String> strSet = strMap.keySet();
			if (strSet != null && strSet.size() > 0) {
				Iterator<String> is = strSet.iterator();
				while (is.hasNext()) {
					String _str = (String) is.next();
					if (_str != null && !_str.equals("")) {
						newStr += _str + regex;
					}
				}
			}
			if (newStr.indexOf(regex) > -1) {
				newStr = newStr.substring(0, newStr.length() - regex.length());
			}
		}
		return newStr;
	}

	/**
	 * @author donghc
	 * @过滤str,去掉regex分隔串中的str1子串
	 * @param str1
	 *            String
	 * @param str
	 *            String
	 * @param regex
	 *            String
	 * @return String
	 */
	public static String dropSubStr(String str1, String str, String regex) {
		String newStr = "";
		if (str1 != null && !str1.equals("") && str != null && !str.equals("") && regex != null && !regex.equals("")) {
			for (int i = 0; i < str.split(regex).length; i++) {
				String subStr = str.split(regex)[i];
				if (subStr.equals(str1)) {
					continue;
				} else {
					newStr += subStr + regex;
				}
			}
			if (newStr.indexOf(regex) > -1) {
				newStr = newStr.substring(0, newStr.length() - regex.length());
			}
		}
		return newStr;
	}
	 /**
	  * <pre>
	  * 
	  * 提供精确的小数位四舍五入处理。       
	  *  
	  * @param v 需要四舍五入的数字       
	  * @param scale 小数点后保留几位       
	  * @return 四舍五入后的结果
	  * </pre>
	  */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	/**
	 * @author zhuz @ 规矩、编码response,使得它为ajax返回xml格式
	 * @throws IOException
	 */
	public static PrintWriter sortRes2Ajax(HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("UTF8");
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = response.getWriter();
		return out;
	}

	/**
	 * @author zhuz 将最后的一个或多个字符去掉
	 * @param str
	 * @param regx
	 * @return
	 */
	public static String removeLastChar(String str, String regx) {
		if (str == null)
			return str;
		int l = regx.length();
		if (str.length() < l)
			return str;

		if (str.substring(str.length() - l).equals(regx)) {
			return str.substring(0, str.length() - l);
		} else {
			return str;
		}
	}

	/**
	 * @author zhuz 将map中的值转化为xml,目前只能转化全为String的表
	 */
	public static Element map2Document(Map<?, ?> map) {
		Set<?> set = map.keySet();
		String key = "";
		Document doc = DocumentHelper.createDocument();
		Element rs = doc.addElement("rs");
		Element nd = rs.addElement("nd");
		for (Iterator<?> it = set.iterator(); it.hasNext();) {
			key = (String) it.next();
			nd.addElement(key).setText(String.valueOf(map.get(key)));
		}
		return rs;
	}

	/**
	 * @author zhuz 去掉字符串中相同的字符
	 * @param words
	 * @param regx
	 * @return
	 */
	public static String replaceSameWords(String words, String regx) {
		words = removeLastChar(words, regx);
		String[] wordsArray = words.split(regx);
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < wordsArray.length; i++) {
			map.put(wordsArray[i], wordsArray[i]);
		}
		String result = "";
		Set<String> set = map.keySet();
		for (Iterator<String> it = set.iterator(); it.hasNext();) {
			result += (String) map.get(it.next()) + regx;
		}
		result = removeLastChar(result, regx);
		return result;
	}

	/**
	 * @author wangxz
	 * @param miliSecond
	 * @return 将两个Date的long型差值转换为中文描述时间
	 */
	public static String MiliSecond2Time(long miliSecond) {
		log.info("=================miliSecond=" + miliSecond);
		long DAY = 24L * 60L * 60L * 1000L;
		long HOUR = 60L * 60L * 1000L;
		long MINUTE = 60L * 1000L;
		long SECOND = 1000L;
		String time = "";
		long day = 0;
		long hour = 0;
		long minute = 0;
		if (miliSecond >= DAY) {
			day = miliSecond / DAY;
			miliSecond = miliSecond - day * DAY;
		}
		if (miliSecond >= HOUR) {
			hour = miliSecond / HOUR;
			miliSecond = miliSecond - hour * HOUR;
		}
		if (miliSecond >= MINUTE) {
			minute = miliSecond / MINUTE;
		}
		if (miliSecond >= SECOND) {
		}
		if (day != 0) {
			time = String.valueOf(day) + "天";
		}
		if (hour != 0) {
			time += String.valueOf(hour) + "小时";
		}
		if (minute != 0) {
			time += String.valueOf(minute) + "分钟";
		}
		if (day == 0 && hour == 0 && minute == 0) {
			time = "小于1分钟";
		}
		/*
		 * if(second!=0){ time += String.valueOf(second)+"秒"; }
		 * if(day==0&&hour==0&&minute==0&&second==0){ time = "0分钟"; }
		 */
		return time;
	}

	/**
	 * 时间日期替换为yyyy年mm月dd日,其中年月日的表示为大写:〇一二三四五六七八九十
	 * 
	 * @param
	 * @return String
	 */
	public static String Time2StringYMDUpper(String time) {
		log.info("===================qianfaTime=" + time);
		if (time == null || time.equals(""))
			return "";
		try {
			String yUpper[] = { "〇", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
			String ylower[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
			String mUpper[] = { "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二" };
			String mlower[] = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
			String dUpper[] = { "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十", "二十一", "二十二", "二十三", "二十四", "二十五", "二十六", "二十七", "二十八",
					"二十九", "三十", "三十一" };
			String dlower[] = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
					"29", "30", "31" };

			String yyyy = time.substring(0, 4);
			String mm = time.substring(5, 7);
			String dd = time.substring(8, 10);
			String _yyyy = "";
			String _mm = "";
			String _dd = "";
			for (int i = 0; i < yyyy.length(); i++) {
				for (int j = 0; j < ylower.length; j++) {

					if (yyyy.substring(i, i + 1).equals(ylower[j])) {
						_yyyy = _yyyy + yUpper[j];
						break;
					}
				}
			}
			for (int j = 0; j < mlower.length; j++) {
				if (mm.equals(mlower[j])) {
					_mm = mUpper[j];
					break;
				}
			}
			for (int j = 0; j < dlower.length; j++) {
				if (dd.equals(dlower[j])) {
					_dd = dUpper[j];
					break;
				}
			}
			return _yyyy + "年" + _mm + "月" + _dd + "日";
		} catch (Exception ex) {

			log.error("==ComUtil:Time2StringYMDUpper==：" + ex.getMessage());
			ex.printStackTrace();
			return "";
		}
	}

	/**
	 * 左加0格式化
	 * 
	 * @param length
	 * @param num
	 * @return
	 */
	public static String leftFormat(int length, int num) {
		String left = "0000000000";
		String s = "";
		s = left + String.valueOf(num);
		s = s.substring(s.length() - length, s.length());
		return s;
	}

	// 拼两个字符串
	static public String joinTwoStrBySplit(String s1, String s2, String split) {
		String s = "";
		if (s1 != null && !s1.trim().equals("")) {
			if (s2 != null && s2 != "") {
				s = s1 + split + s2;
			} else {
				s = s1;
			}
		} else {
			if (s2 != null && !s2.trim().equals("")) {
				s = s2;
			}
		}
		return s;

	}

	/**
	 * 两个字符串做减法运算
	 * 
	 * @param str1
	 * @param str2
	 * @param reg
	 * @return
	 */
	public static String sub2String(String str1, String str2, String reg) {
		String[] str1Array = str1.split(reg);
		String newStr = "";
		for (int i = 0; i < str1Array.length; i++) {
			if (str2.indexOf(str1Array[i] + ";") == -1) {
				newStr += str1Array[i] + reg;
			}

		}
		return newStr;
	}

	/**
	 * String 转为 Clob
	 * 
	 * @param parameter
	 * @return
	 */
	public static Clob string2Clob(Object parameter) {
		Clob obj = null;
		if (parameter != null) {
			try {
				obj = new SerialClob(String.valueOf(parameter).toCharArray());
			} catch (SerialException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return obj;

	}

	/**
	 * 删除文件夹以及文件夹下的子目录与文件
	 * 
	 * @param file
	 * @return
	 */

	public static void deleteFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFile(files[i]);
				}
			}
			file.delete();
		}
	}
	/**
	 * MD5加密
	 * @param secoret
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String md5Code(String secoret) {
		String md5 = "";
        MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("md5");
			m.update(secoret.getBytes("utf-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}  
        byte[] md5b = m.digest(); 
        String temp = ""; 
        for(byte b:md5b) {
        	int i = b;
        	if(i<0) {
        		i = i+256;
        	}
        	temp = Integer.toHexString(i);
        	if(temp.length()==1) {
        		temp = "0"+temp;
        	}
        	md5 = md5 + temp;
        }
		return md5.toUpperCase();
	}
	/**
	 * 随机获取18位数字
	 * @return
	 */
	public static String getDigit18(){
		StringBuffer sBuffer=new StringBuffer();
		Calendar calendar=Calendar.getInstance();
		sBuffer.append(calendar.getTimeInMillis()).append(getRandomNumber(5));
		return sBuffer.toString();
	}
	/**
	 * 获取项目basePath
	 * @param request
	 * @return
	 */
	public static String getBasePath(HttpServletRequest request){
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://"
				+ request.getServerName() + ":" + request.getServerPort()
				+ path + "/";
		return basePath;
	}
	/**
	 * JSON字符串转map
	 * @param s
	 * @return
	 */
	public static Map parserToMap(String s){  
	    Map map=new HashMap();  
	    JSONObject json=JSONObject.fromObject(s);  
	    Iterator keys=json.keys();  
	    while(keys.hasNext()){  
	        String key=(String) keys.next();  
	        String value=json.get(key).toString();  
	        if(value.startsWith("{")&&value.endsWith("}")){  
	            map.put(key, parserToMap(value));  
	        }else{  
	            map.put(key, value);  
	        }  
	  
	    }  
	    return map;  
	}  
}
