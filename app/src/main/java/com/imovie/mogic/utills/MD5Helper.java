package com.imovie.mogic.utills;

import android.util.Log;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * MD5加密算法专用工具类
 * @author 李忠仁
 * 2012-2-3上午10:59:28
 */
public class MD5Helper {

	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F' };

	private static final String TAG = "MD5Helper";
	/**
	 * 用MD5算法加密
	 * @param in String : 待加密的原文
	 * @return String : 加密后的密文，如果原文为空，则返回null;
	 */
	public static String encode(final String in){
		return encode(in, "");
	}
	/**
	 * 用MD5算法加密
	 * @param in String : 待加密的原文
	 * @param charset String : 加密算法字符集
	 * @return String : 加密后的密文，若出错或原文为null，则返回null
	 */
	public static String encode(final String in, final String charset){
		if(in == null) return null;
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			if(StringHelper.isEmpty(charset)){
				md.update(in.getBytes());
			}else{
				try{
					md.update(in.getBytes(charset));
				}catch(Exception e){
					md.update(in.getBytes());
				}
			}
			byte[] digesta = md.digest();
			return StringHelper.bytes2Hex(digesta);
		}catch(NoSuchAlgorithmException ex){
			//出错
			Log.e(TAG, "encode("+in+","+charset+"):NoSuchAlgorithmException -->"+ex.getMessage());
			return null;
		}
	}
	public static byte[] md5(byte[] bytes){
		try{
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(bytes);
			byte[] ret = digest.digest();
			return ret;
		} catch (NoSuchAlgorithmException ex){
			Log.e(TAG, "encode(byte[]):NoSuchAlgorithmException -->"+ex.getMessage());
			return null;
		}		
	}




	public static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	public static String md5sum(String filename) {

		String s1;
		InputStream fis;
		byte[] buffer = new byte[1024];
		int numRead = 0;
		MessageDigest md5;
		try{
			fis = new FileInputStream(filename);
			md5 = MessageDigest.getInstance("MD5");
			while((numRead=fis.read(buffer)) > 0) {
				md5.update(buffer,0,numRead);
			}
			fis.close();
			s1 = toHexString(md5.digest());
			Log.d(TAG,"hash:" +s1);
			return s1;
		} catch (Exception e) {
			System.out.println("error");
			return null;
		}
	}

}