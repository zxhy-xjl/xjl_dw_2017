package controllers.modules.mobile.filter;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import models.modules.mobile.WxUser;
import models.modules.mobile.XjlDwSchool;
import models.modules.mobile.XjlDwWxClass;
import models.modules.mobile.XjlDwWxStudent;
import net.sf.json.JSONObject;
import play.Logger;
import play.cache.Cache;
import play.i18n.Messages;
import play.mvc.Before;
import utils.CommonValidateUtil;
import utils.DateUtil;
import utils.HttpClientUtil;
import utils.StringUtil;
import controllers.LoginService;
import controllers.comm.BaseController;
import controllers.comm.SessionInfo;
import controllers.comm.Sign;
import controllers.modules.mobile.bo.WxUserBo;
 /**
 * @author    姓名   E-mail: 邮箱  Tel: 电话
 * @version   创建时间：2017-3-24 下午1:47:15
 * @describe  类说明
 */
public class MobileFilter extends BaseController{
	private static org.slf4j.Logger log = LoggerFactory.getLogger(MobileFilter.class);
	@Before(unless = { "Application.index","LoginService.index", "LoginService.mIndex","LoginService.login",
			"LoginService.logout","LoginService.mlogout" })
	static void checkLogin() {
		log.debug("checkLogin开始检查登录状态");
		String userAgent = request.headers.get("user-agent").value().toLowerCase();
		String deviceFlag = params.get("flag");
		log.debug("userAgent:"+ userAgent);
		log.debug("deviceFlag:" + deviceFlag);
		boolean isNeedInterface =false;
		SessionInfo sessionInfo=getSessionInfo();
		WxUser 	wxUser = null;
		if(sessionInfo!=null){
			log.debug("当前session不为空");
			wxUser = sessionInfo.getWxUser();
			log.debug("当前用户:" + wxUser);
			if(CommonValidateUtil.isMobile(userAgent)){ //手机端获取微信用户信息
				log.debug("当前访问是通过手机访问");
				if(wxUser!=null&&(DateUtil.getNowDate().compareTo(DateUtil.getDateTimeNowFun(wxUser.upOpenidTime,"d",1))<=0)){
					//还在有效期内
					log.debug("还在有效期内");
					isNeedInterface = false;
				}else{
					log.debug("已经过期，需要重新获取");
					//已经是三天前的数据，过期重新获取
					isNeedInterface = true;
				}
			}
		}else{
			log.debug("当前session为空");
			if(!isMobile(userAgent)){ //pc
				log.debug("当前浏览器不是手机，这里认为是pc");
				if("testPC".equals(deviceFlag)){
					log.debug("是pc上的测试标示testPC");
					wxUser = WxUser.getFindByOpenId(null);
					if(null != wxUser){
						log.debug("使用模拟用户加入到session中，昵称是:" + wxUser.nickName);
						sessionInfo=new SessionInfo();
		        		sessionInfo.setWxUser(wxUser);
		        		setSessionInfo(sessionInfo);
						isNeedInterface = false;
					}else{
						log.error("数据库中一个微信用户都没有,模拟用户失败");
						Logger.error("++++++++++++++++模拟oxh64jkHZeWtbUYc2AMqDc0HiJZg登录失败");
					}
				}else{
					LoginService.index();
				}
			}else{
				sessionInfo=new SessionInfo();
				isNeedInterface=true;
			}
		}
//		if(!isMobile(userAgent)){
//			wxUser = WxUser.getFindByOpenId(null);
//			if(null != wxUser){
//				//Cache.set(userKey, wxUser);
//        		sessionInfo.setWxUser(wxUser);
//        		setSessionInfo(sessionInfo);
//				isNeedInterface = false;
//			}else{
//				Logger.error("++++++++++++++++模拟oxh64jkHZeWtbUYc2AMqDc0HiJZg登录失败");
//			}
//		}
		if(isNeedInterface){
			String schoolKey = session.getId()+"_"+request.domain;
			XjlDwSchool xjlDwSchool;
			if(Cache.get(schoolKey)==null){
				String schoolDoMain = request.domain;
				if("localhost".equals(schoolDoMain)){
					schoolDoMain="dw201709.com";
				}
				xjlDwSchool = XjlDwSchool.getSchoolBySchoolId(null,schoolDoMain);
				if(xjlDwSchool!=null){
					Cache.set(schoolKey,xjlDwSchool);
				}else{
					render("modules/xjldw/mobile/no_find_school.html");
				}
			}else{
				xjlDwSchool = (XjlDwSchool)Cache.get(schoolKey);
			}
			String appId = xjlDwSchool.wxServer.appId;
			String secret = xjlDwSchool.wxServer.appSecret;
			if(!StringUtil.isNotEmpty(appId)||!StringUtil.isNotEmpty(secret)){
	    		nok(Messages.get("paramsLose"));
	    	}
			Logger.info("----------------appIdappIdappId:"+appId);
			//应用授权作用域scope
			//snsapi_base:不弹出授权页面，直接跳转，只能获取用户openid
			//snsapi_userinfo：弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息
			String scope = "snsapi_base"; 
			String openId = params.get("openId");
			String code = params.get("code");
			//URL示例(不带参数)：http://www.baidu.com/wechat/user/index
			String url ="http://" + request.domain + request.url;
			//String url ="http://" + request.domain + StringUtils.substringBefore(request.url,"?");
			//String url=URLEncoder.encode("http://xjldw.airclub.xin/mobile/m/noticeAdd");
			Logger.info("----------------url:"+url);
			if (code == null) {
				Logger.info("===========redirectURL=========== " + code);
				String redirectURL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
						+ appId
						+ "&redirect_uri="
						+ url
						+ "&response_type=code&scope="+scope+"&state=1"
						+ "#wechat_redirect";
				redirect(redirectURL);
			} else {
				Logger.info("===========code=========== " + code);
				url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
			            + appId + "&secret=" + secret + "&code=" + code
			            + "&grant_type=authorization_code";
				JSONObject json = HttpClientUtil.invoke(url, "POST", null);
				Logger.info("openid json = " + json);
				String subscribe = "0"; //是否关注了，0：未关注，1：已关注
				if (json!=null&&json.containsKey("openid")) {
			        openId = json.getString("openid");
			        subscribe = json.containsKey("subscribe")?json.getString("subscribe"):subscribe;
			        params.remove("code");// 一次性，用完作废
					params.put("openId", openId);
					wxUser = WxUser.findById(openId);
					//最多三天取一次
					if(wxUser!=null&&(DateUtil.getNowDate().compareTo(DateUtil.getDateTimeNowFun(wxUser.upOpenidTime,"d",1))<=0)){
						wxUser = WxUser.getFindByOpenId(openId);
						sessionInfo.setWxUser(wxUser);
		        		setSessionInfo(sessionInfo);
					}else{
						if(wxUser==null){
							wxUser = new WxUser();
							wxUser.wxOpenId = openId;
						}
				        String accessToken = null;
						if("snsapi_userinfo".equals(scope)){
					        Logger.info("通过****网页授权****获取用户基本信息");
					        //通过code换取的是一个特殊的网页授权access_token,与基础支持中的access_token（该access_token用于调用其他接口）不同
					        accessToken = json.getString("access_token");
				        	url = "https://api.weixin.qq.com/sns/userinfo?access_token="+accessToken+"&openid="+openId+"&lang=zh_CN ";
				        	json = HttpClientUtil.invoke(url, "POST", null);
				        	Logger.info("snsapi_userinfo json = " + json);
				        	if(json!=null&&json.containsKey("openid")&&json.containsKey("nickname")){
				        		wxUser.nickName = json.getString("nickname");
				        		wxUser.sex = json.getString("sex");
				        		wxUser.sex = "1".equals(wxUser.sex)?"男":"女";
				        		wxUser.language = json.getString("language");
				        		wxUser.city = json.getString("city");
				        		wxUser.province = json.getString("province"); 
				        		wxUser.country = json.getString("country");
				        		wxUser.headImgUrl = json.getString("headimgurl").replace("/0", "/132");
				        		wxUser.schoolId =xjlDwSchool.schoolId;
				        		wxUser.isConcerned = "Y";
				    			//wxUser.wxRole="0";
				    			wxUser.openIdChanncel="web_grant";
				    			wxUser.upOpenidTime= DateUtil.getNowDate();
				        		wxUser = WxUserBo.save(wxUser);
				        		wxUser = WxUser.getFindByOpenId(openId);
				        		sessionInfo.setWxUser(wxUser);
				        		setSessionInfo(sessionInfo);
								//Cache.set(userKey,wxUser);
				        	}
						}else if("snsapi_base".equals(scope)){
					    	Logger.info("通过****获取用户基本信息接口****获取用户信息");
					    	accessToken = Sign.getAccessToken(appId,secret);
					    	url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+accessToken+"&openid="+openId+"&lang=zh_CN";
					    	json = HttpClientUtil.invoke(url, "POST", null);
					    	Logger.info("snsapi_base json = " + json);
					    	Logger.info(" wxUser.wxOpenId==== = " + wxUser.wxOpenId);
					    	if(json!=null&&json.containsKey("openid")){
					    		if(json.containsKey("nickname")){
					    			wxUser.nickName = json.getString("nickname");
					    			wxUser.sex = json.getString("sex");
					    			wxUser.sex = "1".equals(wxUser.sex)?"男":"女";
					        		wxUser.language = json.getString("language");
					    			wxUser.city = json.getString("city");
					    			wxUser.province = json.getString("province"); 
					    			wxUser.country = json.getString("country");
					    			wxUser.headImgUrl = json.getString("headimgurl").replace("/0", "/132");
					    			wxUser.schoolId =xjlDwSchool.schoolId;
					    			wxUser.isConcerned = "Y";
					    			//wxUser.wxRole="0";
					    			wxUser.openIdChanncel="web_grant";
					    			wxUser.upOpenidTime=DateUtil.getNowDate();
					    			wxUser = WxUserBo.save(wxUser);
					        		wxUser = WxUser.getFindByOpenId(openId);
					        		sessionInfo.setWxUser(wxUser);
					        		setSessionInfo(sessionInfo);
					    		}
					    	}
						}
					}
			    }else{
			    	Logger.info("...获取openid失败，请重试重试");
				}
				Logger.info("openid = " + openId);
			}
		}
		//检查有没有绑定学生，如果没有绑定学生需要跳转到绑定学生页面
		//wxUser = getWXUser();
		if (wxUser.currentStudent == null&&("testPC".equals(deviceFlag)||isMobile(userAgent))){ //本地调试手机版页面使用
			render("modules/xjldw/mobile/my/student_none.html");
		} else {
			if(!"PC".equals(sessionInfo.getDeviceFlag())){
				log.debug("当前登录设备不是pc，是:" + sessionInfo.getDeviceFlag());
				log.debug("当前登录用户:"+wxUser.nickName);
				 //Logger.info("当前登录用户:"+wxUser.nickName + " 当前班级:"+wxUser.currentClass.className);	
			}
		}
	}
	
	
	private static boolean isMobile(String userAgent) {
		if (userAgent != null) {
			if (userAgent.indexOf("micromessenger") >= 0) {
				return true;
			} else if (userAgent.indexOf("pad") >= 0) {
				return true;
			} else if (userAgent.indexOf("android") >= 0) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 得到微信用户，同时从缓存中获取微信登录用户信息
	 * @return
	 */
	public static SessionInfo getSessionInfo() {
		String userKey = getSessionKey();
		log.debug("userKey:"+userKey);
		SessionInfo sessionInfo = null;
		if (Cache.get(userKey)!=null) {
			sessionInfo = (SessionInfo) Cache.get(userKey);
		}
		return sessionInfo;
	}
	public static WxUser getWXUser() {
		SessionInfo sessionInfo = getSessionInfo();
		if (sessionInfo!=null) {
			WxUser wxUser=sessionInfo.getWxUser();
			log.debug("wxUser信息 openId:"+wxUser.wxOpenId + " nickName:"+wxUser.nickName);
			return wxUser;
		}
		else{
			log.debug("session为空");
        	nok(Messages.get("appletSessionBeOverdue"));
		}
		return null;
	}

	private static void setSessionInfo(SessionInfo sessionInfo){
		Cache.set(getSessionKey(), sessionInfo);
	}
	public static String getSessionKey(){
		return session.getId()+"_userkey";
	}
}
 