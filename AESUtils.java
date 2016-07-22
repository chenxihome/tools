package com.saic.ebiz.mall.controller.util;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Im即时客服会员添加额外信息及AES加密(来源于主站代码)
 * @updaterAuthor lijialiang
 * @creatTime 20150917
 */
public class AESUtils {

	private static final String IV = "0102030405060708";

	/**
	 * AES加密过程 
	 * @param strKey
	 * @return
	 * @throws Exception
	 */
	 public static String encrypt(String strKey, String info) throws Exception {
	        SecretKeySpec skeySpec = getKey(strKey);
	        String ive = IV;
	        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
	        int blockSize = cipher.getBlockSize();
	        
	        byte[] dataBytes = info.getBytes("utf-8");
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
	        IvParameterSpec iv = new IvParameterSpec(ive.getBytes());
	        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
	        byte[] encrypted = cipher.doFinal(plaintext);

	      //  return new BASE64Encoder().encode(encrypted);
	        return byte2hex(encrypted);
	    }
	 
	 
	 /***
	  * AES解密
	  * @param strKey
	  * @param strIn
	  * @return
	  * @throws Exception
	  */
	 public static String decrypt(String strKey, String info) throws Exception {
		 try{
			 byte[] byteResult = decrypt2Byte(strKey, info);
			 String result = new String(byteResult,"UTF-8");
			 return result;
		 }catch(Exception e){
			 return info;
		 }
		 
	 }
	 
	 /***
	  * AES解密详细过程
	  * @param strKey
	  * @param content
	  * @return
	  * @throws Exception
	  */
	 private static byte[] decrypt2Byte(String strKey,String content) throws Exception{
		 SecretKeySpec skeySpec = getKey(strKey);
		 String ive = IV;
	
		 Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		 IvParameterSpec iv = new IvParameterSpec(ive.getBytes());
		 cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
		// byte[] encrypted = Base64.decode(content)
		 byte[] encrypted = hex2byte(content);
		 byte[] original = flushZeroElements(cipher.doFinal(encrypted));
		
		 return original;
		
	 }
	 
	 
	 /**
	  * 清除AES加密前补位的内容
	  * @param source
	  * @return
	  */
	 private static byte[] flushZeroElements(byte[] source){
		 int i =0;
		 for(byte b:source){
			 if(b==0){
				 break;
			 }
			 i++;
		 }
		 byte[] result = Arrays.copyOf(source, i);
		 return result;
	 }
	 
	 /***
	  * 针对长度不满16的密钥进行补0操作
	  * @param strKey
	  * @return
	  * @throws Exception
	  */
	 private static  SecretKeySpec getKey(String strKey) throws Exception {
	        byte[] arrBTmp = strKey.getBytes();
	        byte[] arrB = new byte[16]; // 创建一个空的16位字节数组（默认值为0）

	        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
	            arrB[i] = arrBTmp[i];
	        }

	        SecretKeySpec skeySpec = new SecretKeySpec(arrB, "AES");

	        return skeySpec;
	 }
	 /***
	  * 把16进制转成byte类型数组 
	  * @param strhex
	  * @return
	  */
	  public static byte[] hex2byte(String strhex) {
	        if (strhex == null) {
	            return null;
	        }
	        int l = strhex.length();
	        if (l % 2 == 1) {
	            return null;
	        }
	        byte[] b = new byte[l / 2];
	        for (int i = 0; i != l / 2; i++) {
	            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);
	        }
	        return b;
	   }
	  
	  /**
	   * 把byte数组转成16进制
	   * @param b
	   * @return
	   */
	  public static String byte2hex(byte[] b) {
	        String hs = "";
	        String stmp = "";
	        for (int n = 0; n < b.length; n++) {
	            stmp = (Integer.toHexString(b[n] & 0XFF));
	            if (stmp.length() == 1) {
	                hs = hs + "0" + stmp;
	            } else {
	                hs = hs + stmp;
	            }
	        }
	        return hs.toUpperCase();
	   }

	
}
