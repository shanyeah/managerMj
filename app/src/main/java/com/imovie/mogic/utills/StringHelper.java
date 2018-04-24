package com.imovie.mogic.utills;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

/**
 * <p>Title: 字符串辅助工具类</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p><p>
 * CreateTime: 2005-4-30 21:06:58
 * @author 21世纪的苦力 topilee@gmail.com
 * @version 1.3
 */
public class StringHelper {
	private static final String TAG = "XMLDataGetter";
	/**
	 * 将字符串转换成整数
	 * @param in String : 要转换的字符串"a"
	 * @param defaultValue int : 默认值0
	 * @return int : 如果字符串合法（可以转换）则返回转换后的整数，否则返回默认值
	 */
	public static int parseInt(final String in, final int defaultValue){
		if(in == null) return defaultValue;
		try{
			return Integer.parseInt(in);
		}catch(NumberFormatException nfe){
//			VV8Utils.printLog(TAG,"parseInt(" + in + "):NumberFormatException-->" + nfe.getMessage());
			return defaultValue;
		}
	}
	public static Integer parseInteger(final String in,final int defaultValue){
		return new Integer(parseInt(in,defaultValue));
	}
	/**
	 * 判断是否为正确的邮箱地址
	 * @param in String : 邮箱地址
	 * @return boolean : 如果是合法邮箱地址返回true，否则返回false
	 */
	public static boolean isEmail(final String in){		
		if(StringHelper.isEmpty(in)) return false;
		int at = in.indexOf("@");
		int dot = in.indexOf(".");
		if(at < 0 || dot < 0) return false;
		if((at + 1) >= dot) return false;
		if((dot + 1) >= in.length()) return false;
		return true;
	}
	/**
	 * 将字符串转换成double
	 * @param in String : 要转换的字符串
	 * @param defaultValue double : 默认值
	 * @return double : 如果字符串可以转换，则返回转换后的double，否则返回默认值
	 */
	public static double parseDouble(final String in, final double defaultValue){
		if(in == null) return defaultValue;
		try{
			return Double.parseDouble(in);
		}catch(NumberFormatException nfe){
//			VV8Utils.printLog(TAG,"parseDouble(" + in + "):NumberFormatException-->" + nfe.getMessage());
			return defaultValue;
		}
	}
	
	/**
	 * 将字符串转换成float
	 * @param in String : 要转换的字符串
	 * @param defaultValue double : 默认值
	 * @return double : 如果字符串可以转换，则返回转换后的double，否则返回默认值
	 */
	public static float parseFloat(final String in, final float defaultValue){
		if(in == null) return defaultValue;
		try{
			return Float.parseFloat(in);
		}catch(NumberFormatException nfe){
//			VV8Utils.printLog(TAG,"parseFloat(" + in + "):NumberFormatException-->" + nfe.getMessage());
			return defaultValue;
		}
	}
	/**
	 * 按字节合理截取字符串 例：<br>
	 * truncate("china,我是中国人",5) = "china"; <br>
	 * truncate("china,我是中国人",8) = "china,我"; <br>
	 * truncate("china,我是中国人",9) = "china,我"; <br>
	 * @param in String : 待截取的字符串，包含中文及字符
	 * @param length int : 要截取的字符串字节数
	 * @return String : 截取后的字符串
	 */
	public static String truncate(final String in, final int length){
		return truncate(in,length,"");
	}
	/**
	 * 按字节合理截取字符串 例：<br>
	 * truncate("china,我是中国人",5) = "china"; <br>
	 * truncate("china,我是中国人",8) = "china,我"; <br>
	 * truncate("china,我是中国人",9) = "china,我"; <br>
	 * @param in String : 待截取的字符串，包含中文及字符
	 * @param length int : 要截取的字符串字节数
	 * @param ender String : 结束符
	 * @return String : 截取后的字符串
	 */
	public static String truncate(final String in,final int length,final String ender){
		if(length <0 || in == null || in.getBytes().length <= length ) return in;
		int index = getMaxTruncateLength(in, length);
		return in.substring(0, index) + ender;
	}
	/**
	 * 取得可截取的字符串的最大长度
	 * @param in String : 待截取的字符中，包含中文等
	 * @param length int : 待截取的字符串字节数
	 * @return int : 最大可截取的长度
	 */
	public static int getMaxTruncateLength(final String in, final int length){
		if(in == null || in.trim().length() <= 0) return 0;
		int len = in.getBytes().length;
		if(len <= length) return in.length();
		byte[] bytes = in.getBytes();
		int count = 0;
		for (int i = 0; i < length; i++){
			if(bytes[i] < 0){
				count++;
			}
		}
		if(count % 2 == 0){
			return length - count / 2;
		}else{
			return length - 1 - count / 2;
		}
	}
	/**
	 * 字符串替换，如： <br>
	 * replace("ABCDEFGHIJKL",1,"222")="A222EFGHIJKL";
	 * @param mother String : 待替换的源串
	 * @param index int : 开始替换的序号，从0开始
	 * @param child String : 替换字符串
	 * @return String : 替换后的字符串
	 */
	public static String replace(final String mother, final int index, final String child){
		if(index < 0 || index >= mother.length()) return mother;
		StringBuffer out = new StringBuffer();
		//前缀
		if(index > 0){
			out.append(mother.substring(0, index));
		}
		//中间替换
		if(index + child.length() > mother.length()){
			out.append(child.substring(0, mother.length() - index));
		}else{
			out.append(child);
			//后缀
			out.append(mother.substring(index + child.length()));
		}
		return out.toString();
	}
	/**
	 * 判断字符串是否为空或是空串
	 * @param in String : 待判断的字符串
	 * @return boolean : 为空或为空串均返回true，否则返回false
	 */
	public static boolean isEmpty(final String in){
		if(in == null || in.trim().length() <= 0) return true;
		return false;
	}
	/**
	 * 显示数组内容
	 * @param in Object[] : 数组
	 * @return String : 相关内容
	 */
	public static String toString(final Object[] in){
		StringBuffer desc = new StringBuffer();
		desc.append("[");
		if(in != null){
			for(int i=0;i<in.length;i++){
				desc.append(in[i]).append(";");
			}
		}
		desc.append("]");
		return desc.toString();
	}
	/**
	 * 取得字符串指定编码的字符显示
	 * @param in String : 源字符串
	 * @param charset String : 编码
	 * @return String : 返回相应的字符串，如果指定的编码不合法，则返回源字符串
	 */
	public static String getBytes(final String in, final String charset){
		if(in == null) return null;
		try{
			return new String(in.getBytes(charset));
		}catch(UnsupportedEncodingException uee){
//			VV8Utils.printLog(TAG,"getBytes(" + in + "," + charset + "):UnsupportedEncodingException-->" + uee.getMessage());
			return in;
		}
	}
	/**
	 * 判断字符串的字节数是否超出允许字节数
	 * @param in String : 待判断的字符串
	 * @param length int : 所允许的最大字节数
	 * @return boolean : 如果超出限制，返回true，否则返回false
	 */
	public static boolean isExceed(final String in, final int length){
		if(in == null || length <= 0) return false;
		if(in.getBytes().length <= length) return false;
		return true;
	}
	/**
	 * 功能举例： <br>
	 * String mother = "child1@@@child2@@@child3"; <br>
	 * add(mother,"@@@","child1") = "child1@@@child2@@@child3"; <br>
	 * add(mother,"@@@","child4") = "child1@@@child2@@@child3@@@child4"; <br>
	 * add(mother,"@@@@","child1") = "child1@@@child2@@@child3@@@@child1"; <br>
	 * @param mother String : 原有字符串
	 * @param splitter String : 分隔符
	 * @param child String : 新增字符串
	 * @return String : 根据规则返回合并后的字符串
	 */
	public static String add(final String mother, final String splitter, final String child){
		return add(mother, splitter, child, false);
	}
	/**
	 * 用指定分隔符拼凑字符串
	 * @param mother String : 原有字符串
	 * @param splitter String : 分隔符
	 * @param child String : 新增字符串
	 * @param duplicate boolean : 是否允许有重复的字串
	 * @return String : 根据规则返回合并后的字符串
	 */
	public static String add(final String mother, final String splitter, final String child, final boolean duplicate){
		if(mother == null) return child;
		StringBuffer ret = new StringBuffer();
		ret.append(mother);
		//允许child重复
		if(duplicate == true){
			ret.append(splitter).append(child);
		}else{
			if(mother.indexOf(child) < 0){
				ret.append(splitter).append(child);
			}
		}
		return ret.toString();
	}
	/**
	 * 功能举例： <br>
	 * String mother = "child1@@@child2@@@child3"; remove(mother,"@@@","child1") = "child2@@@child3"; <br>
	 * remove(mother,"@@@","child4") = "child1@@@child2@@@child3"; <br>
	 * remove(mother,"@@@@","child1") = "child1@@@child2@@@child3"; <br>
	 * @param mother String : 待处理的字符串
	 * @param splitter String : 分隔符
	 * @param child String : 子字符串
	 * @return String : 返回移除后的字符串
	 */
	public static String remove(final String mother, final String splitter, final String child){
		if(isEmpty(child) || isEmpty(mother)) return mother;
		StringBuffer ret = new StringBuffer();
		int index = mother.indexOf(child);
		if(index < 0) return mother;
		if(index == 0){
			if(index + child.length() + splitter.length() < mother.length()){
				ret.append(mother.substring(index + child.length() + splitter.length()));
			}else{
				return null;
			}
		}else{
			ret.append(mother.substring(0, index - splitter.length()));
			if(index + child.length() + splitter.length() < mother.length()){
				ret.append(mother.substring(index + child.length()));
			}
		}
		return ret.toString();
	}
	/**
	 * 将字符串转换成long
	 * @param in String : 要转换的字符串
	 * @param defaultValue long : 默认值
	 * @return long : 如果字符串可以转换，则返回转换后的long，否则返回默认值
	 */
	public static long parseLong(final String in, final long defaultValue){
		if(in == null) return defaultValue;
		try{
			return Long.parseLong(in);
		}catch(NumberFormatException nfe){
//			VV8Utils.printLog(TAG,"parseLong(" + in + "):NumberFormatException-->" + nfe.getMessage());
			return defaultValue;
		}
	}
	/**
	 * 二进制转字符串（16进制）
	 * @param b byte[] : 二进制数组
	 * @return String : 转换后的字符串
	 */
	public static String bytes2Hex(final byte[] b){
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++){
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if(stmp.length() == 1) hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs;
	}
	/**
	 * 十六进制字符串转换成byte[]，摘自梁德钢
	 * @param in String : 十六进制字符串
	 * @return byte[] : 转换后的byte[]
	 */
	public static byte[] hex2Bytes(String in){
		byte buf[] = new byte[in.length() / 2];
		char chars[] = in.toCharArray();
		for (int i = 0, j = 0; i < buf.length; i++){
			char c1 = chars[j++];
			char c2 = chars[j++];
			buf[i] = getByte(getValue(c1), getValue(c2));
		}
		return buf;
	}
	/**
	 * 得到十六进制字符对应的数值，摘自梁德钢
	 * @param c char : [0,9],[a,f],[A,F]
	 * @return byte : 对应数值的byte表示
	 */
	public static byte getValue(char c){
		if(c >= '0' && c<='9') return (byte)(c-'0');
		if(c >= 'a' && c<='f') return (byte)(c-'a'+10);
		if(c >= 'A' && c<='F') return (byte)(c-'A'+10);
		return (byte)0;
	}
	//摘自梁德钢
	private static byte getByte(byte b1, byte b2){
		return (byte) (b1 << 4 | b2);
	}
	/**
	 * 判断字符串是否为非法字符
	 * @param in String : 待判断的字符串
	 * @param illegal String : 非法字符集
	 * @return boolean : 如果字符串内含有任一非法字符，返回true,否则返回false
	 */
	public static boolean isIllegal(final String in, final String illegal){
		if(illegal == null || illegal.length() <= 0) return false;
		if(in == null || in.length() <= 0) return false;
		for (int i = 0; i < illegal.length(); i++){
			char c = illegal.charAt(i);
			if(in.indexOf(c) >= 0) return true;
		}
		return false;
	}
	/**
	 * 字符串是否合法
	 * @param in String : 待判断的字符串
	 * @param legal String : 所有的合法字符
	 * @return boolean : 如果字符串里不合任何非法字符，则返回ture,否则返回false
	 */
	public static boolean isLegal(final String in, final String legal){
		if(legal == null || legal.length() <= 0) return false;
		if(in == null || in.length() <= 0) return true;
		for (int i = 0; i < in.length(); i++){
			char c = in.charAt(i);
			if(legal.indexOf(c) < 0) return false;
		}
		return true;
	}

	public static final String ALL_LOWER_LETTER = "abcdefghijklmnopqrstuvwxyz"; //小写字母
	public static final String ALL_UPPER_LETTER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; //大写字母
	public static final String ALL_NUMERIC_LETTER = "0123456789"; //数字
	/**
	 * 判断字符串是否为正确的手机号码串，正确的手机号码是以"13"或"15"开头的11位数字字符串
	 * @param in String : 待验证的号码串
	 * @return boolean : 如果是，返回true，否则返回false
	 */
	public static boolean isMobile(final String in){
		if(in == null || in.length() < 0) return false;
		if(in.length() != 11) return false;
		if(isLegal(in, ALL_NUMERIC_LETTER) == false) return false;
		if(in.startsWith("13") || in.startsWith("15")) return true;
		return false;
	}
	/**
	 * 判断是否为全数字字符串
	 * @param in String : 字符串
	 * @return boolean : 如果全由数字组成返回true，否则返回false
	 */
	public static boolean isNumberic(final String in){
		if(in == null || in.length() < 0) return false;
		return isLegal(in,ALL_NUMERIC_LETTER);
	}
	/**
	 * 由数字数组组成串输出
	 * @param in int[] : 数字数组
	 * @param splitter String : 分隔符
	 * @return String : 成串后的字符
	 */	
	public static String toBunch(final int[] in,final String splitter){
		if(in == null) return null;
		String[] ins = new String[in.length];
		for(int i=0;i<in.length;i++){
			ins[i] = String.valueOf(in[i]);			
		}
		return toBunch(ins,splitter,0);
	}
	/**
	 * 由字符串数组组成串输出
	 * @param in String[] : 字符串数组
	 * @param splitter String : 分隔符
	 * @return String : 成串后的字符
	 */
	public static String toBunch(final String[] in,final String splitter){
		return toBunch(in,splitter,0);
	}
	/**
	 * 由字符串数组组成串输出
	 * @param in String[] : 字符串数组
	 * @param splitter String : 分隔符
	 * @param start int : 开始位置
	 * @return String : 成串后的字符
	 */
	public static String toBunch(final String[] in,final String splitter,final int start){
		if(in == null || in.length <0 || start >= in.length) return null;
		StringBuffer bunch = new StringBuffer();
		for(int i = 0;i<in.length;i++){
			if(i < start) continue;
			bunch.append(in[i]).append(splitter);
		}
		return bunch.toString().substring(0,bunch.toString().length()-splitter.length());
	}
	/**
	 * 由字符串列表组成串输出
	 * @param in List : 字符串列表
	 * @param splitter String : 分隔符
	 * @return String : 成串后的字符
	 */
	public static String toBunch(final List<Object> in,final String splitter){
		if(in == null || in.size() <= 0) return null;
		StringBuffer bunch = new StringBuffer();
		for(Iterator<Object> it=in.iterator();it.hasNext();){
			bunch.append((String)it.next()).append(splitter);
		}
		return bunch.toString().substring(0,bunch.toString().length()-splitter.length());		
	}
	/**
	 * 小写第一个字母
	 * @param in String : 待格式化的字符串
	 * @return String : 如果in==null或in长度为0则返回in
	 */
	public static String lowerFirstChar(final String in){
		if(in == null) return null;
		return in.substring(0,1).toLowerCase()+in.substring(1);
	}
	/**
	 * 大写第一个字母
	 * @param in String : 待格式化的字符串
	 * @return String : 如果in==null或in长度为0则返回in
	 */	
	public static String upperFirstChar(final String in){
		if(in == null || in.length() <= 0) return in;
		return in.substring(0,1).toUpperCase()+in.substring(1);
		
	}
	/**
	 * 格式化输出数字
	 * @param in double : 待输出数值
	 * @param format String : 输出格式，如：#0.00,###,###.00
	 * @return String : 格式化后的字符串
	 */
	public static String toString(final double in,final String format){
		DecimalFormat formatter = new DecimalFormat(format);
		return formatter.format(in);		
	}
	/**
	 * 格式化输入字符串，对null值作处理
	 * @param in String : 待输入的字符串
	 * @param defaultNullValue String : 如果in == null时输出的默认值
	 * @return String : 格式化后的字符串，如果in == null则返回默认值
	 */
	public static String formatNullValue(String in,String defaultNullValue){
		return (in==null)?defaultNullValue:in;
	}
	/**
	 * 格式化输出数字
	 * @param in int : 待输出的整数
	 * @param format String : 输出格式，如：#0.00,###,###.00
	 * @return String : 格式化后的字符串
	 */
	public static String toString(final int in,final String format){
		DecimalFormat formatter = new DecimalFormat(format);
		
		return formatter.format(in);		
	}
	/**
	 * 返回重复N次后的字符串
	 * @param in String : 待重复的字符串
	 * @param count int : 重复次数，[0,)
	 * @return String : 重复后的字符串
	 */
	public static String repeat(String in,int count){
		if(in == null || count < 1) return "";
		StringBuffer out = new StringBuffer();
		for(int i=0;i<count;i++){
			out.append(in);
		}
		return out.toString();
	}
	/**
	 * 
	 * @param in String : 源字符串
	 * @param index int : 从0开始[0,)
	 * @param splitter String : 分隔符
	 * @param defaultValue String : 默认值
	 * @return String : 
	 */
	public static String getIndexString(String in,int index,String splitter,String defaultValue){
		if(in == null || splitter == null) return defaultValue;
		String[] infos = in.split(splitter);
		if(infos.length <= index) return defaultValue;
		if(infos[index] == null) return defaultValue;
		return infos[index];
	}
	public static String repLineFirstToUpper(String str){
		StringBuffer buf=new StringBuffer(str);
		int length=buf.length();
		for(int i=0;i<buf.length();i++){
			char c=buf.charAt(i);
			if(c=='_'){
				i++;
//				if ((i<length) && (!isNumberic(String.valueOf(buf.charAt(i))))){
				if (i < length) {

					buf.setCharAt(i, (char)(buf.charAt(i)-32));
				}
			}
		}
		
		return buf.toString().replaceAll("_", "");
	}
	
	public static void main(String[] args){
//		System.out.println("ABC?CDE".replaceAll("\\?", "_"));
//		System.out.println("abcd*def?ccc".replaceAll("\\*", ".*").replace('?', '.'));
//		String in = "a";
//		System.out.println(StringHelper.parseInt(in, 0));
//		if(StringHelper.parseInt(in, -999999) == -9999999)
		System.out.println(URLEncoder.encode("http://zg.mop.com/index.html?id=127"));

	}	
}