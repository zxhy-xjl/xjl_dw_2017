package controllers.modules.mobile;
import models.modules.mobile.WxUser;
import models.modules.mobile.XjlDwArticle;
import models.modules.mobile.XjlDwFile;
import models.modules.mobile.XjlDwGroupBuy;
import models.modules.mobile.XjlDwNotice;
import models.modules.mobile.XjlDwNoticeFile;
import models.modules.mobile.XjlDwWxClass;
import net.sf.json.JSONArray;
import play.Logger;
import play.cache.Cache;
import play.i18n.Messages;
import utils.StringUtil;
import utils.SysParamUtil;
import utils.WxRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import controllers.comm.BaseController;
import controllers.comm.SessionInfo;
import controllers.comm.Sign;
import controllers.modules.mobile.bo.XjlDwFileBo;
import controllers.modules.mobile.bo.XjlDwWxClassBo;
import controllers.modules.mobile.filter.MobileFilter;
import controllers.modules.mobile.bo.WxUserBo;

public class A extends MobileFilter {
	 public static void manager() {
		   render("modules/xjldw/mobile/main.html");
	 } 
    public static void noticeDetail() {
    	WxUser wxUser =  getWXUser();
		renderArgs.put("wxUser",wxUser);
		renderArgs.put("noticeId",params.get("noticeId"));
        render("modules/xjldw/mobile/activity/notice_detail.html");
    }
    
    public static void queryNotice(){
    	int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
    	XjlDwNotice notice = XjlDwNotice.findById(params.get("noticeId", Long.class));
    	List<XjlDwNoticeFile> list = (List<XjlDwNoticeFile>) XjlDwNoticeFile.queryNoticeFile(notice.noticeId, pageIndex, pageSize).get("data");
    	notice.fileList = list;
    	Logger.info("输出通知通告内容："+notice.noticeContent);
        renderArgs.put("notice", notice);
        ok(notice);
    }
    public static void noticeAdd() {
    	WxUser wxUser =  getWXUser();
		renderArgs.put("wxUser",wxUser);
        render("modules/xjldw/mobile/activity/notice_add.html");
    }
    public static void noticeList() {
    	WxUser wxUser =  getWXUser();
		renderArgs.put("wxUser",wxUser);
        render("modules/xjldw/mobile/activity/notice_list.html");
    }
    public static void articleList() {
		WxUser wxUser =  getWXUser();
		renderArgs.put("wxUser",wxUser);
		if(params.get("type")!= null) {
			renderArgs.put("type", params.get("type"));
		}else{
			renderArgs.put("type", "list");
		}
        render("modules/xjldw/mobile/activity/article_list.html");
    }
    public static void articleDetail() {
    	XjlDwArticle article = XjlDwArticle.findById(params.get("articleId", Long.class));
        renderArgs.put("detail", article);
        renderArgs.put("type",params.get("type"));
        render("modules/xjldw/mobile/activity/article_detail.html");
    }
    public static void articleAdd() {
		XjlDwArticle article=null;
		if(params.get("articleId")!= null) {
			article = XjlDwArticle.findById(params.get("articleId", Long.class));
		}else{
			WxUser wxUser = getWXUser();
			article=new XjlDwArticle();
			article.articleAuthor=wxUser.nickName;
			article.articleState="0";
		}
		renderArgs.put("article", article);
        render("modules/xjldw/mobile/activity/article_add.html");
    }
    
    public static void groupList() {
		WxUser wxUser = getWXUser();
		renderArgs.put("wxUser",wxUser);
        render("modules/xjldw/mobile/activity/group_list.html");
    }
    public static void groupAdd() {
		WxUser wxUser =  getWXUser();
		renderArgs.put("wxUser",wxUser);
        render("modules/xjldw/mobile/activity/group_add.html");
    }
    public static void groupDetail() {
        renderArgs.put("groupBuyId", params.get("groupBuyId"));
        renderArgs.put("wxUser",getWXUser());
        render("modules/xjldw/mobile/activity/group_detail.html");
     }
    /**
     * 相册
     */
    public static void albumList() {
		WxUser wxUser = getWXUser();
		renderArgs.put("wxUser",getWXUser());
    	renderArgs.put("fileId", "1");
        render("modules/xjldw/mobile/activity/album_list.html");
    }
    public static void albumAdd(){
    	WxUser wxUser = getWXUser();
		renderArgs.put("wxUser",getWXUser());
        render("modules/xjldw/mobile/activity/album_add.html");
    }
    public static void albumImageList(){
    	WxUser wxUser = getWXUser();
		renderArgs.put("wxUser",getWXUser());
		renderArgs.put("albumId", params.get("albumId"));
//		String accessToken = Sign.getAccessToken("wx4ae50eb9b72cef71","2f66a0dd662948bc9b2b8aa26ebd0a4f");
//		String noceStr = UUID.randomUUID().toString();
//		String timestamp = Long.toString(System.currentTimeMillis() / 1000);
//		String str = "jsapi_ticket="+WxRegister.getTicket(accessToken)+"&noncestr="+noceStr+"&timestamp="+timestamp+"&url=http://dw201709.com/mobile/A/albumImageList?albumId="+params.get("albumId");
//		renderArgs.put("signature", WxRegister.SHA1(str));
//		renderArgs.put("nonceStr", noceStr);
//		renderArgs.put("timestamp",timestamp);
		render("modules/xjldw/mobile/activity/album_image_list.html");
    }
    //选择照片
    public static void albumAddImage(){
    	WxUser wxUser = getWXUser();
		renderArgs.put("wxUser",getWXUser());
        render("modules/xjldw/mobile/activity/album_add_image.html");
    }
    //上传照片
    public static void albumUploadImage(){
    	WxUser wxUser = getWXUser();
		renderArgs.put("wxUser",getWXUser());
        render("modules/xjldw/mobile/activity/album_upload_image.html");
    }
    //上传照片成功之后的提示
    public static void albumUploadImageOk(){
    	WxUser wxUser = getWXUser();
		renderArgs.put("wxUser",getWXUser());
        render("modules/xjldw/mobile/activity/album_upload_image_ok.html");
    }
    public static void addClassAdmin(){
   	    String userKey = session.getId()+"_userkey";
    	WxUser wxUser =  getWXUser();
    	wxUser=WxUser.getFindByOpenId(wxUser.wxOpenId);
    	wxUser=WxUserBo.save(wxUser);
    	if(wxUser!=null){
    		XjlDwWxClass xjlDwWxClass=new XjlDwWxClass();
    		xjlDwWxClass.classId=1l;
    		xjlDwWxClass.isDefault="Y";
    		xjlDwWxClass.wxOpenId=wxUser.wxOpenId;
    		xjlDwWxClass=XjlDwWxClassBo.save(xjlDwWxClass);
    	}
    	wxUser = WxUser.getFindByOpenId(wxUser.wxOpenId);
    	Cache.set(userKey,wxUser);
    	renderArgs.put("wxUser",wxUser);
    	render("modules/xjldw/mobile/main.html");
    }
    
    //上传头像
    public static void uploadAlbum() {

        String url = params.get("url");
        String openId = params.get("openId");
        Logger.info("uploadopenId:"+openId);
        Logger.info("上传图片url====" + url);

        Logger.info(url);
        String picUrl = downloadPhoto(url,openId, "A",null);
        
        WxUser wxUser =  getWXUser();
        XjlDwFile xjlDwFile = XjlDwFileBo.saveImage(picUrl, wxUser.wxOpenId);
        if(picUrl.contains(".json;")&&picUrl.endsWith("encoding=utf-8")){
        	//上传失败
        	nok("上传失败，公众号没有足够的权限");
        }
        Logger.error("==========oldFileDelete ",params.get("oldFileDelete"));
        if (params.get("oldFileDelete")!=null) {
        	//如果上传图片的时候指定了旧图片（或文件），则上传成功后删除原文件
            BaseController.deleteFile(params.get("oldFileDelete"));
		}
        Logger.info("picUrl" + picUrl);
        ok(xjlDwFile);
    }
    //上传头像
    public static void uploadLogo() {

        String url = params.get("url");
        String openId = params.get("openId");
        Logger.info("上传图片url====" + url);

        Logger.info(url);
        String picUrl = downloadPhoto(url,openId, "A",null);
        WxUser wxUser =  getWXUser();
        XjlDwFile xjlDwFile = XjlDwFileBo.saveImage(picUrl, wxUser.wxOpenId);
        if(picUrl.contains(".json;")&&picUrl.endsWith("encoding=utf-8")){
        	//上传失败
        	nok("上传失败，公众号没有足够的权限");
        }
        Logger.error("==========oldFileDelete ",params.get("oldFileDelete"));
        if (params.get("oldFileDelete")!=null) {
        	//如果上传图片的时候指定了旧图片（或文件），则上传成功后删除原文件
            BaseController.deleteFile(params.get("oldFileDelete"));
		}
        Logger.info("picUrl" + picUrl);
        ok(picUrl);
    }

	
	public static void totest(){
		WxUser wxUser = getWXUser();
		renderArgs.put("wxUser",getWXUser());
        render("modules/xjldw/mobile/activity/text.html");
	}
}

