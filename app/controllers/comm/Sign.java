package controllers.comm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

import models.modules.weixin.AccessToken;
import models.modules.weixin.AppletSessionKey;
import models.modules.weixin.JsapiTicket;

import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import play.Logger;
 /**
 * @author    姓名   E-mail: 邮箱  Tel: 电话
 * @version   创建时间：2017-3-28 下午4:57:50
 * @describe  类说明
 */
public class Sign {
	private static org.slf4j.Logger log = LoggerFactory.getLogger(Sign.class);
	//保存上一次获取access_token的时间以及取得的值
	private static HashMap<String, AccessToken> hashMapAccessToken = new HashMap<String, AccessToken>();
	//保存上一次获取JSApiToken的时间以及取得的值
	private static HashMap<String, JsapiTicket> hashMapJspApiTicket = new HashMap<String, JsapiTicket>();
	//保存上一次获取JSApiToken的时间以及取得的值
	private static HashMap<String, AppletSessionKey> hashMapAppletSessionKey = new HashMap<String, AppletSessionKey>();
	
	/**
	 * 获取access_token，保存在7200秒以内只取一次
	 * @param appid 
	 * @param secret
	 * @return
	 */
	public static String getAccessToken(String appid,String secret) {
		log.debug("获取accessToken");
		log.debug("appid",appid);
		log.debug("secret",secret);
		//请求Jsapi_Ticket的时间
		Date requestDate = null;
		
		String access_token = "";
		if (hashMapAccessToken.get(appid)!=null) {
			requestDate = hashMapAccessToken.get(appid).getRequestDate();
			access_token = hashMapAccessToken.get(appid).getAccessToken();
		}
		Date nowTime = new Date();
		int second = 0;
		if (requestDate!=null) {
			second = (int)(nowTime.getTime() - requestDate.getTime())/1000;
		}
		if (requestDate==null||second>7000) {
			log.debug("以前的accessToken超时，需要重新获取");
			try {
				requestDate = new Date();
				String return_josn="";
				StringBuilder sBuilder;
				URL url;
				InputStream is;
				BufferedReader reader;
				JSONObject myJsonObject;
				sBuilder = new StringBuilder("https://api.weixin.qq.com/cgi-bin/token");
				sBuilder.append("?grant_type=client_credential");
				sBuilder.append("&appid="+appid);
				sBuilder.append("&secret="+secret);
				//System.out.println("-----------请求access_token的URL:"+sBuilder.toString());
				log.debug("url",sBuilder.toString());
				url = new URL(sBuilder.toString());
				is = url.openStream();
				reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
				return_josn = reader.readLine();
				log.debug("url return",return_josn);
				// 将字符串转换成jsonObject对象
				myJsonObject = new JSONObject(return_josn);
				// 获取对应的值
				access_token = myJsonObject.getString("access_token");
				hashMapAccessToken.put(appid, new AccessToken(requestDate,access_token));
				log.debug("access_token",access_token);
			} catch (Exception e) {
				System.out.println("-------------ERROR------"+e.toString());
				log.error("获取新的access_token发生错误",e);
			}
		}
		log.debug("本次请求返回的access_token",access_token);
		return access_token;
	}
	/**
	 * 获取小程序的session_key，保存在7200秒以内只取一次
	 * @param appid 
	 * @param secret
	 * @return
	 */
	public static String getAppletSessionKey(String appid,String secret,String jsCode) {
		//请求Jsapi_Ticket的时间
		Date requestDate = null;
		
		String session_key = "";
		if (hashMapAppletSessionKey.get(jsCode)!=null) {
			requestDate = hashMapAppletSessionKey.get(jsCode).getRequestDate();
			session_key = hashMapAppletSessionKey.get(jsCode).getSessionKey();
		}
		Date nowTime = new Date();
		int second = 0;
		if (requestDate!=null) {
			second = (int)(nowTime.getTime() - requestDate.getTime())/1000;
		}
		if (requestDate==null||second>7000) {
			try {
				requestDate = new Date();
				String return_josn="";
				StringBuilder sBuilder;
				URL url;
				InputStream is;
				BufferedReader reader;
				JSONObject myJsonObject;
				sBuilder = new StringBuilder("https://api.weixin.qq.com/sns/jscode2session");
				sBuilder.append("?appid="+appid);
				sBuilder.append("&secret="+secret);
				sBuilder.append("&js_code="+jsCode);
				sBuilder.append("&grant_type=authorization_code");
				//System.out.println("-----------请求access_token的URL:"+sBuilder.toString());
				url = new URL(sBuilder.toString());
				is = url.openStream();
				reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
				return_josn = reader.readLine();
				Logger.info("-------------return_josn------"+return_josn);
				// 将字符串转换成jsonObject对象
				myJsonObject = new JSONObject(return_josn);
				// 获取对应的值
				if (myJsonObject.has("session_key")) {
					session_key = myJsonObject.getString("session_key");
					hashMapAppletSessionKey.put(jsCode, new AppletSessionKey(requestDate,session_key));
				}
			} catch (Exception e) {
				System.out.println("-------------ERROR------"+e.toString());
				Logger.info("-------------ERROR------"+e.toString());
			}
		}
		return session_key;
	}
}


 