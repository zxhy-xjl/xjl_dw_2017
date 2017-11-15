package utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.sf.json.JSONObject;
import play.Logger;

public class WxRegister {

	public static String getTicket(String accessToken){
		 String ticket = null;
		 String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+ accessToken +"&type=jsapi";//这个url链接和参数不能变
		 Logger.info("homeworkurl:"+url);
		 try {  
		 URL urlGet = new URL(url);  
        HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();  
        http.setRequestMethod("GET"); // 必须是get方式请求  
        http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");  
        http.setDoOutput(true);  
        http.setDoInput(true);  
        System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒  
        System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒  
        http.connect();  
        InputStream is = http.getInputStream();  
        int size = is.available();  
        byte[] jsonBytes = new byte[size];  
        is.read(jsonBytes);  
        String message = new String(jsonBytes, "UTF-8");  
        JSONObject demoJson = JSONObject.fromObject(message);  
        Logger.info("JSON字符串:"+demoJson);
        ticket = demoJson.getString("ticket");  
        is.close();  
		   } catch (Exception e) {  
	            e.printStackTrace();  
	    }
		return ticket;
	 }
	 public static String SHA1(String decript) {  
		    try {  
		        MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");  
		        digest.update(decript.getBytes());  
		        byte messageDigest[] = digest.digest();  
		        // Create Hex String  
		        StringBuffer hexString = new StringBuffer();  
		        // 字节数组转换为 十六进制 数  
		            for (int i = 0; i < messageDigest.length; i++) {  
		                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);  
		                if (shaHex.length() < 2) {  
		                    hexString.append(0);  
		                }  
		                hexString.append(shaHex);  
		            }  
		            return hexString.toString();  
		   
		        } catch (NoSuchAlgorithmException e) {  
		            e.printStackTrace();  
		        }  
		        return "";  
		} 
}
