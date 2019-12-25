/*
 * FileName: EncryptHelper.java
 * Date:     2014年6月20日 下午12:40:24
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.saic.ebiz.mall.controller.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meidusa.toolkit.common.util.StringUtil;
import com.saic.ebiz.mall.controller.vo.DianpingParmVO;

/**
 * 〈一句话功能简述〉<br> 
 * 〈功能详细描述〉
 *
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class EncryptHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EncryptHelper.class);
	private static final  String SPILTCHAR=",";
   	public static final String  REGEX_MOBILE = "^((13[0-9])|(14[5,7])|(15[^4,\\D])||(17[7])|(18[0-9]))\\d{8}$";
   	/**
     * Encrypt the given string by MD5 arithmetic.
     *
     * @param data
     * @return
     */
    public static String md5(String data) {
        StringBuffer result = new StringBuffer("");
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(data.getBytes("ISO-8859-1"));
            byte bytes[] = md5.digest();
            for (int i = 0; i < bytes.length; i++) {
                result.append(Integer.toHexString((0x000000ff & bytes[i]) | 0xffffff00).substring(6));
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return result.toString();
    }
    /**
     * Encrypt the given string with the given key in DES-3 arithmetic.
     *
     * @param data the given data to be encrypted
     * @param key the given key to encrypt the given data
     * @return
     */
    public static String encrypt3DES(String data, byte[] key) {
        if (data == null || key == null)
            return data;
        try {
            byte[] enData = doCrypt("DESede", Cipher.ENCRYPT_MODE, key,
                data.getBytes("UTF-16LE"));
            return byte2hex(enData);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Decrypt the given string with the given key in DES-3 arithmetic.
     *
     * @param data the given data to be decrypted
     * @param key the given key to decrypt the given data
     * @return
     */
    public static String decrypt3DES(String data, byte[] key) {
        if (data == null || key == null)
            return data;
        try {
            byte[] enData = hex2byte(data);
            byte[] deData = doCrypt("DESede", Cipher.DECRYPT_MODE, key, enData);
            return new String(deData, "UTF-16LE");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // Private Methods ---------------------------------------------------------
    private static byte[] doCrypt(String algorithm, int mode, byte[] key, byte[] data) {
        byte[] result = new byte[] {};
        try {
            byte[] md5Key = md5(new String(key, "ISO-8859-1")).substring(0, 24)
                .getBytes("ISO-8859-1");
            SecretKey deskey = new javax.crypto.spec.SecretKeySpec(md5Key, algorithm);
            Cipher c1 = Cipher.getInstance(algorithm);
            c1.init(mode, deskey);
            result = c1.doFinal(data);
        }
        catch (UnsupportedEncodingException e) {
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     *
     * @param byte[] b
     * @return String
     */
    private static String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer("");
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs.append("0").append(stmp);
            else
                hs.append(stmp);
            if (n < b.length - 1)
                hs.append("");
        }
        return hs.toString().toUpperCase();
    }
    /**
     *
     *
     * @param s
     * @return
     */
    private static byte[] hex2byte(String s) {
        int length = s.length() / 2;
        byte[] bs = new byte[length];
        for (int i = 0; i < length; i++) {
            String substr = s.substring(i * 2, (i + 1) * 2);
            bs[i] = (byte) Integer.parseInt(substr, 16);
        }
        return bs;
    }
    public static void main(String[] args) throws Exception {
         String str="18818264703,dzlb_01,1";
//        String encryptStr = "F30FA30839EE8310436053AAA8BFB4759BDB67E01EBC66A5EB972ED4013FCCE47EC5F798B56BA0A44EC0A189576919A8";
//        String encryptData = "akFoT2Y4d0k3NjBIQ0RscExWaFVUZTMxQzJxSUNwdDFDQVc2K0l1WG56V2lLVThrOUlFOUU5YTJtSTV5RnZYTUMraThRV2xPeGQxNXd4QWlUMXBtU3dJTU5qekdLSjlNd1hUaDF6KzlhMnZhMmVLVlFmeFE2WVlQeE1YbTRHS2UvaHdBTGgzbjdzcVdKTmk0TFlOZjRQK2ZadjdsUU5PU0lSQ1FnZ2hpcUpzN1ArTWRUMC94a1ZZRDR2YlBjNXN3TzltODJaSUJ4L3NtMm1ZbGVIQTkrbEdzUDBYdDJ2KzQ2aS9yQ3F5TVB5ZnVFMGpTakloK3JGTit4NU9nT0FqaXdXMmcyRkwwV3hOU0JUS1NMR2VVQXpEVkk5OVZ5YkE4Rm1CUjN5MXo2dHh1bWhTSlFpK3dTZHJUSFptVmluN1VTbElzVWdQcVFSdnh4b1kwTmRoZHV3WHRtMml6cjY2ZVRtVXlqY1ZrYm80UDBXSEpKbmlmWFJHRHMrcSs0QkIvT0ZDRkUrQk9hVk9uYnhSM0RyUURLdz09,anlyadmin,CADILLAC,"+System.currentTimeMillis();
//// String deData = EncryptHelper.decrypt3DES(encryptData, "F2nykjUCD5LQdD0s8ZnZwD0h6Hv2wm14zyQ62t9TNtt44BI9CazDI2CYJ2e".getBytes());
        //System.out.println("secretkey:"+secretkey);
        System.out.println(EncryptHelper.encrypt3DES(str,"F2nykjUCD5LQdD0s8ZnZwD0h6Hv2wm14zyQ62t9TNtt44BI9CazDI2CYJ2e".getBytes()));
//
      //  System.out.println(EncryptHelper.decrypt3DES(encryptStr, "1yz5wv3a80s668nvt3r9tap5sia01lw3w0fn8xbt33cbyniam9qtl9q5k01".getBytes()));
//      
   }
    public   static DianpingParmVO decrypt3DES(String encryptStr,String key)  throws Exception{
    	if(StringUtil.isEmpty(encryptStr)){
    		LOGGER.error("解密入参为NULL");
    		throw new NullPointerException("解密入参为NULL Exception！");
    	}
    	DianpingParmVO parmVo=null;
    	try{
     		 String str=decrypt3DES(encryptStr, key.getBytes());
    		 String[] str2=str.split(SPILTCHAR);
              parmVo=new DianpingParmVO();
              if(str2.length==3 && isMobile(str2[0])){
            	  parmVo.setMobile(str2[0]);
            	  parmVo.setGiftPackageCode(str2[1]);
            	  parmVo.setCooperationChannel(str2[2]);
              }
    	}catch(Exception e){
    		LOGGER.error("解密入参失败！encryptStr：{}",encryptStr,e.getMessage());
    		throw new Exception("解密入参失败！");
    	}
    	return parmVo;
    }
    
    public   static  boolean isMobile(String mobile){
        return Pattern.matches(REGEX_MOBILE, mobile);
    }
   public  static String encrypt3DES(DianpingParmVO vo,String key){
	   if(vo==null){
			LOGGER.error("加密入参对象为NULL");
		   return null;
	   }
	  String str=vo.getMobile()+SPILTCHAR+vo.getGiftPackageCode()+SPILTCHAR+vo.getCooperationChannel();
	  String enstr=null;
	  try{
		  enstr=encrypt3DES(str,key.getBytes());
	  }	catch(Exception e){
  		LOGGER.error("加密入参对象失败"+e.getMessage());
  	}
	  return enstr;
   }
}
