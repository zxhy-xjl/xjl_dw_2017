package controllers.modules.mobile;
import models.modules.mobile.WxUser;
import models.modules.mobile.XjlDwArticle;
import models.modules.mobile.XjlDwExam;
import models.modules.mobile.XjlDwExamGrade;
import models.modules.mobile.XjlDwExamSubject;
import models.modules.mobile.XjlDwGroupBuy;
import models.modules.mobile.XjlDwHomework;
import models.modules.mobile.XjlDwHomeworkFile;
import models.modules.mobile.XjlDwNotice;
import models.modules.mobile.XjlDwSchool;
import models.modules.mobile.XjlDwStudent;
import models.modules.mobile.XjlDwSubject;
import models.modules.mobile.XjlDwWxClass;
import net.sf.json.JSONObject;
import play.Logger;
import play.cache.Cache;
import play.i18n.Messages;
import utils.StringUtil;
import utils.SysParamUtil;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import controllers.comm.BaseController;
import controllers.comm.SessionInfo;
import controllers.comm.Sign;
import controllers.modules.mobile.bo.XjlDwWxClassBo;
import controllers.modules.mobile.filter.MobileFilter;
import controllers.modules.mobile.bo.WxUserBo;
/**
 * 作业 视图转换器
 * @author lilisheng
 *
 */
public class W extends MobileFilter {
	/**
	 * 到首页
	 */
	 public static void manager() {
		   render("modules/xjldw/mobile/main.html");
	 } 
	 //成绩单列表
	 public static void examList() {
		WxUser wxUser =  getWXUser();
		renderArgs.put("wxUser",wxUser);
		Logger.info("加载:"+wxUser.currentClass);
		Logger.info("加载:"+wxUser.currentClass.className);
	    render("modules/xjldw/mobile/work/exam_list.html");
	 }
	//成绩单发布
	 public static void examAdd() {
		WxUser wxUser =  getWXUser();
		renderArgs.put("wxUser",wxUser);
	    render("modules/xjldw/mobile/work/exam_add.html");
	 }
	//成绩单详情
	 public static void examDetail() {
		WxUser wxUser =  getWXUser();
		renderArgs.put("wxUser",wxUser);
		XjlDwExam exam = XjlDwExam.findById(Long.parseLong(params.get("examId")));
		Logger.info("最大分数"+exam.max);
		List<Object[]> total = XjlDwExamGrade.queryCount(exam.examId);
		Logger.info("total"+total.get(0));
		renderArgs.put("max", exam.max == null?0:doubleTrans1(exam.max));
		renderArgs.put("min", exam.min == null?0:doubleTrans1(exam.min));
		renderArgs.put("avg", exam.avg == null?0:doubleTrans1(exam.avg));
		renderArgs.put("exam",exam);
		renderArgs.put("total",total.get(0) == null?0:total.get(0));
	    render("modules/xjldw/mobile/work/exam_detail.html");
	 }
	 public static String doubleTrans1(double num){
	    if(num % 1.0 == 0){
		        return String.valueOf((long)num);
	    }
	    return String.valueOf(num);
	}
	//作业列表
	 public static void homeworkList() {
		WxUser wxUser =  getWXUser();
		renderArgs.put("wxUser",wxUser);
	    render("modules/xjldw/mobile/work/homework_list.html");
	 }
	//作业发布
	 public static void homeworkAdd() {
		WxUser wxUser =  getWXUser();
		renderArgs.put("wxUser",wxUser);
	    render("modules/xjldw/mobile/work/homework_add.html");
	 }
	//作业详情
	 public static void homeworkDetail() throws ParseException {
		WxUser wxUser =  getWXUser();
		renderArgs.put("wxUser",wxUser);
//		XjlDwHomework homework = XjlDwHomework.findById(Long.parseLong(params.get("homeworkId")));
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		if(null != homework){
//			Logger.info("工作时间："+sdf.format(homework.createTime));
//			String time = sdf.format(homework.createTime);
//			homework.time = time;
//		}
//		XjlDwSubject subject = XjlDwSubject.findById(homework.subjectId);
//		renderArgs.put("subjectTitle", subject == null ? "" : subject.subjectTitle);
 		renderArgs.put("homeworkId", params.get("homeworkId"));
	    render("modules/xjldw/mobile/work/homework_detail.html");
	 }
	 public static void queryHomeWorkDetail(){
		 XjlDwHomework homework = XjlDwHomework.findById(Long.parseLong(params.get("homeworkId")));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(null != homework){
				Logger.info("工作时间："+sdf.format(homework.createTime));
				String time = sdf.format(homework.createTime);
				homework.time = time;
			}
			XjlDwSubject subject = XjlDwSubject.findById(homework.subjectId);
			int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
			int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
			List<XjlDwHomeworkFile> data = (List<XjlDwHomeworkFile>) XjlDwHomeworkFile.queryHomeworkFile(homework.homeworkId, pageIndex, pageSize).get("data");
			homework.subjectTitle = subject == null ? "" : subject.subjectTitle;
			homework.fileList = data;
			renderArgs.put("homework", homework);
			ok(homework);
	 }
	//作业发布标榜
	 public static void homeworkAddRemark() {
		WxUser wxUser =  getWXUser();
		renderArgs.put("wxUser",wxUser);
		renderArgs.put("homeworkId", params.get("homeworkId"));
		String accessToken = Sign.getAccessToken("wx4ae50eb9b72cef71","2f66a0dd662948bc9b2b8aa26ebd0a4f",false);
		String noceStr = UUID.randomUUID().toString();
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);
		String str = "jsapi_ticket="+getTicket(accessToken)+"&noncestr="+noceStr+"&timestamp="+timestamp+"&url=http://dw201709.com/mobile/W/homeworkAddRemark?homeworkId="+params.get("homeworkId");
		renderArgs.put("signature", SHA1(str));
		renderArgs.put("nonceStr", noceStr);
		renderArgs.put("timestamp",timestamp);
	    render("modules/xjldw/mobile/work/homework_remark.html");
	 }
	 //跳转到我的成绩统计
	 public static void toGradeStatistice(){
		 WxUser wxUser =  getWXUser();
		 renderArgs.put("wxUser",wxUser);
		  render("modules/xjldw/mobile/my/grade_statistics.html");
	 }
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

