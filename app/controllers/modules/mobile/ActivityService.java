package controllers.modules.mobile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import models.modules.mobile.WxUser;
import models.modules.mobile.XjlDwAlbum;
import models.modules.mobile.XjlDwAlbumImage;
import models.modules.mobile.XjlDwAlbumTemplate;
import models.modules.mobile.XjlDwArticle;
import models.modules.mobile.XjlDwArticleFile;
import models.modules.mobile.XjlDwFile;
import models.modules.mobile.XjlDwGroupBuy;
import models.modules.mobile.XjlDwGroupBuyItem;
import models.modules.mobile.XjlDwGroupBuyOrder;
import models.modules.mobile.XjlDwNotice;
import models.modules.mobile.XjlDwNoticeFile;
import models.modules.mobile.XjlDwStudent;
import models.modules.mobile.XjlDwWxClass;
import models.modules.mobile.XjlDwWxStudent;
import play.Logger;
import play.cache.Cache;
import play.i18n.Messages;
import controllers.modules.mobile.bo.WxUserBo;
import controllers.modules.mobile.bo.XjlDwAlbumBo;
import controllers.modules.mobile.bo.XjlDwAlbumImageBo;
import controllers.modules.mobile.bo.XjlDwAriticelFIleBo;
import controllers.modules.mobile.bo.XjlDwArticleBo;
import controllers.modules.mobile.bo.XjlDwGroupBuyBo;
import controllers.modules.mobile.bo.XjlDwGroupBuyItemBo;
import controllers.modules.mobile.bo.XjlDwGroupBuyOrderBo;
import controllers.modules.mobile.bo.XjlDwWxClassBo;
import controllers.modules.mobile.filter.MobileFilter;
import controllers.modules.mobile.bo.XjlDwNoticeBo;
import controllers.modules.mobile.bo.XjlDwNoticeFileBo;
import utils.DateUtil;
import utils.ExcelEntityUtils;
import utils.ExcelUtil;
import utils.FileUploadPathUtil;
import utils.MsgPush;
import utils.StringUtil;

/**
 * 活动控制器，只有一级菜单才会有独立的控制器，二级菜单使用方法
 * 活动里面包含通知/团购/相册/美文四个二级菜单
 * @author lilisheng
 *
 */
public class ActivityService extends MobileFilter {
	/**
	 * 查询 通知列表
	 */
	public static void queryNoticeList() {
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
		Map condition = params.allSimple();
		Map ret = XjlDwNotice.queryNoticeListByPage(condition, pageIndex, pageSize);
		filterNoticeData(ret);
		ok(ret);
	}
	public static void delNoticeById(){
		Long noticeId = Long.parseLong(params.get("noticeId"));
		int ret = XjlDwNotice.delNoticeByNoticeId(noticeId);
		XjlDwNoticeFile.delNoticeFileByNoticeId(noticeId);
		ok(ret);
	}
	private static void filterNoticeData(Map ret){
		List list = (List)ret.get("data");
		for (int i = 0; i < list.size(); i++) {
			//Object o = list.get(i);
			XjlDwNotice xjlDwNotice = (XjlDwNotice)list.get(i);
			String content = xjlDwNotice.noticeContent;
			if (content.length()>50){
				content = content.substring(0, 50);
				xjlDwNotice.noticeContent = content;
				list.set(i,xjlDwNotice);
			}
		}
		ret.put("data", list);
	}
	
	/**
	 * 保存公告
	 */
	public static void saveNotice() {
		WxUser wxUser = getWXUser();
		XjlDwNotice xjlDwNotice = new XjlDwNotice();
        if (StringUtil.isNotEmpty(params.get("noticeId"))) {
            long custManagerId = StringUtil.getLong(params.get("noticeId"));
            xjlDwNotice = XjlDwNotice.findById(custManagerId);
            if (xjlDwNotice == null) {
                nok("查询不到该条通知 ，请查看");
            }
        }
        if (params.get("noticeTitle") == null) {
        	nok(Messages.get("paramsLose"));
        }
        xjlDwNotice.wxOpenId=wxUser.wxOpenId;
        if (params.get("noticeTitle") != null) {
        	xjlDwNotice.noticeTitle = params.get("noticeTitle");
        }
        if (params.get("noticeContent") != null) {
        	xjlDwNotice.noticeContent =params.get("noticeContent");
        }
        XjlDwNotice _xjlDwNotice = XjlDwNoticeBo.save(xjlDwNotice);
        XjlDwNoticeFile xjlDwNoticeFile = null;
        String fileids = params.get("noticeFileIds");
        String [] arrayFile = fileids.split(",");
        if(fileids.length()>0){
        	  for (String str : arrayFile) {
            	  Logger.info("str:"+str);
            	  xjlDwNoticeFile = new XjlDwNoticeFile();
            	  xjlDwNoticeFile.noticeId = _xjlDwNotice.noticeId;
            	  xjlDwNoticeFile.wxOpenid = wxUser.wxOpenId;
            	  xjlDwNoticeFile.fileId =Long.valueOf(str);
            	  XjlDwNoticeFileBo.save(xjlDwNoticeFile);
    		}
        }
        ok(_xjlDwNotice);
    }
	public static void noticePushMsg(){
		//通知通告消息模版
        String jumpUrl = "http://dw201709.com/dw/mobile/A/noticeList";
        String templateId = "SWSrukcVTbMLP1PgNl9Yer3V4l9yltwrfksA0FkVSgI";
        Map<String, Object> mapData = new HashMap<String, Object>();
		Map<String, Object> mapDataSon = new HashMap<String, Object>();
        mapDataSon.put("value","【"+params.get("noticeTitle")+"】");
        mapDataSon.put("color", "#68A8C3");
		mapData.put("first", mapDataSon);
		mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value","通知公告");
		mapData.put("keyword1", mapDataSon);
		mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value","成功");
		mapData.put("keyword2", mapDataSon);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value",df.format(new Date()));
		mapData.put("keyword3", mapDataSon);
		mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value","全班");
		mapData.put("keyword4", mapDataSon);
		mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value",params.get("noticeContent"));
		mapDataSon.put("color","#808080");
		mapData.put("remark", mapDataSon);
		MsgPush.wxMsgPusheTmplate(templateId,jumpUrl,mapData);
	}
	/**
	 * 获取美文列表
	 */
	public static void queryArticleList(){
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
		Map condition = params.allSimple();
		Map ret = XjlDwArticle.queryXjlDwArticleListByPage(condition, pageIndex, pageSize);
		filterArticleData(ret);
		ok(ret);
	}
	public static void delArticleById(){ 
		Long articleId = Long.parseLong(params.get("articleId"));
		int ret = XjlDwArticle.delArticleByarticleId(articleId);
		ok(ret);
	}
	/**
	 * 过滤美文数据，对返回页面的数据进行裁剪
	 * @param ret
	 */
	private static void filterArticleData(Map ret){
		List<XjlDwArticle> list = (List<XjlDwArticle>)ret.get("data");
		for (int i = 0; i < list.size(); i++) {
			XjlDwArticle xjlDwArticle = list.get(i);
			String content = xjlDwArticle.articleContent;
			if (content.length()>50){
				content = content.substring(0, 50);
				xjlDwArticle.articleContent = content;
				list.set(i,xjlDwArticle);
			}
		}
		ret.put("data", list);
	}
	/**
	 * 保存美文
	 */
	public static void saveArticle() {
		WxUser wxUser = getWXUser();
		XjlDwArticle xjlDwArticle = new XjlDwArticle();
        if (StringUtil.isNotEmpty(params.get("articleId"))) {
            long custManagerId = StringUtil.getLong(params.get("articleId"));
            xjlDwArticle = XjlDwArticle.findById(custManagerId);
            if (xjlDwArticle == null) {
                nok("查询不到该条文章 ，请查看");
            }
        }
        xjlDwArticle.wxOpenId=wxUser.wxOpenId;
        if (params.get("articleTitle") != null) {
        	xjlDwArticle.articleTitle = params.get("articleTitle");
        }
        if (params.get("articleContent") != null) {
        	xjlDwArticle.articleContent = params.get("articleContent");
        }
        if (params.get("articleAuthor") != null) {
        	xjlDwArticle.articleAuthor = params.get("articleAuthor");
        }
        if (params.get("articleState") != null) {
        	xjlDwArticle.articleState = params.get("articleState");
        }
        XjlDwArticle _xjlDwArticle = XjlDwArticleBo.save(xjlDwArticle);
		XjlDwArticleFile xjlDwArticleFile = null;
        String articelFilds = params.get("articleFiles");
        String[] articelArray = articelFilds.split(",");
        Logger.info("arrayFile:"+articelArray.length);
        Logger.info("arrayFile:"+articelFilds);
        if(articelFilds.length()>0){
        	 for (String str : articelArray) {
	           	  Logger.info("str:"+str);
	           	 xjlDwArticleFile = new XjlDwArticleFile();
	           	 xjlDwArticleFile.ariticleId = _xjlDwArticle.articleId;
	           	 xjlDwArticleFile.wxOpenId = wxUser.wxOpenId;
	           	 xjlDwArticleFile.fileId =Long.valueOf(str);
	           	 XjlDwAriticelFIleBo.save(xjlDwArticleFile);
        	 }
        }
        ok(_xjlDwArticle);
	}
	/**
	 * 查询团购条目列表
	 */
	public static void queryGroupItemList() {
		WxUser wxUser = getWXUser();
		Long groupBuyId=0l;
	    if(StringUtil.isNotEmpty(params.get("groupBuyId"))) {
	    	groupBuyId = StringUtil.getLong(params.get("groupBuyId"));
        }else{
        	nok("groupBuyId丢失!");
        }
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
		Map condition = params.allSimple();
		condition.put("groupBuyId", groupBuyId);
		Map ret = XjlDwGroupBuyItem.queryXjlDwGroupBuyItemListByPage(condition, pageIndex, pageSize);
		if(ret!=null&&ret.get("data")!=null){
			List<XjlDwGroupBuyItem> listGroupBuyItem = (List<XjlDwGroupBuyItem>)ret.get("data");
			for(XjlDwGroupBuyItem groupBuyItem : listGroupBuyItem){
				groupBuyItem.isGroupBuy=false;
				groupBuyItem.price = String.valueOf(groupBuyItem.groupItemPrice);
				Object [] paramObject = {groupBuyItem.groupBuyId,groupBuyItem.groupItemId,wxUser.wxOpenId};
				XjlDwGroupBuyOrder xjlDwGroupBuyOrder=XjlDwGroupBuyOrder.find("from XjlDwGroupBuyOrder where status='0AA' and groupBuyId=? and groupItemId=? and wxOpenId=?", paramObject).first();
				if(xjlDwGroupBuyOrder!=null){
					groupBuyItem.isGroupBuy=true;
				}
			}
		}
		ok(ret);
	}
	
	public static void queryChart(){
		WxUser wxUser = getWXUser();
		Long groupBuyId=0l;
	    if(StringUtil.isNotEmpty(params.get("groupBuyId"))) {
	    	groupBuyId = StringUtil.getLong(params.get("groupBuyId"));
        }else{
        	nok("groupBuyId丢失!");
        }
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
		Map condition = params.allSimple();
		condition.put("groupBuyId", groupBuyId);
		Map ret = XjlDwGroupBuyItem.queryXjlDwGroupBuyItemListByPage(condition, pageIndex, pageSize);
		Logger.info("得到团购列表："+new Date());
		Map hm=new HashMap();
		XjlDwGroupBuy groupBuy = XjlDwGroupBuy.findById(groupBuyId);
		XjlDwGroupBuyBo.checkState(groupBuy);
		hm.put("title", groupBuy.groupBuyTitle);
		hm.put("endTime", DateFormatUtils.format(groupBuy.groupBuyEndTime,"yyyy-MM-dd HH:mm"));
		hm.put("state", groupBuy.groupBuyState);
		hm.put("totalBuyer", XjlDwGroupBuyOrder.totalBuyer(groupBuyId));
		hm.put("totalAmount", XjlDwGroupBuyOrder.totalAmount(groupBuyId));
		//我有没有参与这个团购的标识
		boolean isMyOrder = XjlDwGroupBuyOrder.hasOrder(groupBuyId, wxUser.wxOpenId,wxUser.currentStudent.studentId);
		hm.put("isMyOrder", isMyOrder);
		//下面开始处理团购明细，包括每个明细的购买人数量和我是不是也购买了这个商品
		List<Map> list=new ArrayList<Map>();
		List<XjlDwGroupBuyItem> listGroupBuyItem = (List<XjlDwGroupBuyItem>)ret.get("data");
		Map<String, String> _itemInfo = null;
		String title_split="";
		int title_split_count = 0;
		for(XjlDwGroupBuyItem groupBuyItem : listGroupBuyItem){
			_itemInfo = new HashMap<String, String>();
			//商品标题
			_itemInfo.put("title", groupBuyItem.groupItemTitle);
			title_split+= groupBuyItem.groupItemTitle+",";
			title_split_count = groupBuyItem.groupItemTitle.length();
			//商品价格
			_itemInfo.put("price", String.valueOf(groupBuyItem.groupItemPrice));
			//购买者数量
			String itemBuyerCount = XjlDwGroupBuyOrder.totalItemBuyer(groupBuyId, groupBuyItem.groupItemId);
			_itemInfo.put("buyerCount", itemBuyerCount);
			//我有没有购买这个商品的标识
			boolean isMyItem = false;
			if (isMyOrder){
				isMyItem = XjlDwGroupBuyOrder.hasOrderItem(groupBuyId, groupBuyItem.groupItemId, wxUser.wxOpenId,wxUser.currentStudent.studentId);
			}
			_itemInfo.put("isMyItem", String.valueOf(isMyItem));
			list.add(_itemInfo);
			
		}
		//过滤字符串
		String result_str="";
		if(!"".equals(title_split)){
			 Logger.info("title_split："+title_split);
			 String [] titleSp = title_split.split(",");
			 for (int i = 0; i <titleSp.length; i++) {
				 String gy = "";
				 String encode1 = StringUtil.convert(titleSp[i]);
				 if(titleSp.length>i+1){
					 String encode2 = StringUtil.convert(titleSp[i+1]);
	 		 	     String[] split = encode1.replace("\\", "-").split("-");
		 		 	   for (int j = 0; j < split.length; j++) {
				            String s = split[j];
				            if (s != null && !"".equals(s)) {
				                if (encode2.indexOf("\\" + split[j]) != -1) {
				                    gy += "\\" + split[j];
				                }
				            }
				        }
				 }
				 Logger.info("gy："+ StringUtil.decodeUnicode(gy));
				 result_str += StringUtil.decodeUnicode(gy)+",";
			 }
		}
		result_str = result_str.substring(0,result_str.length()-1);
		Logger.info("标题长度"+title_split_count);
		Logger.info("标题"+result_str.substring(0, result_str.indexOf(",")));
		hm.put("itemInfoList", list);
		hm.put("titleSplit", result_str.substring(0, result_str.indexOf(",")));
		hm.put("titleSplitCount", title_split_count);
		Logger.info("--------------------------------------------"+hm.get("itemInfoList"));
		ok(hm);
	}
	/**
	 * 查询团购统计 购买数量统计
	 */
	public static void queryGroupStatistics() {
		WxUser wxUser = getWXUser();
		Long groupBuyId=0l;
	    if(StringUtil.isNotEmpty(params.get("groupBuyId"))) {
	    	groupBuyId = StringUtil.getLong(params.get("groupBuyId"));
        }else{
        	nok("groupBuyId丢失!");
        }
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
		Map condition = params.allSimple();
		condition.put("groupBuyId", groupBuyId);
		Map ret = XjlDwGroupBuyItem.queryXjlDwGroupBuyItemListByPage(condition, pageIndex, pageSize);
		
		Map hm=new HashMap();
		XjlDwGroupBuy groupBuy = XjlDwGroupBuy.findById(groupBuyId);
		XjlDwGroupBuyBo.checkState(groupBuy);
		hm.put("title", groupBuy.groupBuyTitle);
		hm.put("endTime", DateFormatUtils.format(groupBuy.groupBuyEndTime,"yyyy-MM-dd HH:mm"));
		hm.put("state", groupBuy.groupBuyState);
		hm.put("totalBuyer", XjlDwGroupBuyOrder.totalBuyer(groupBuyId));
		hm.put("totalAmount", XjlDwGroupBuyOrder.totalAmount(groupBuyId));
		//我有没有参与这个团购的标识
		//boolean isMyOrder = XjlDwGroupBuyOrder.hasOrder(groupBuyId, wxUser.wxOpenId,wxUser.currentStudent.studentId);
		//hm.put("isMyOrder", isMyOrder);
		//下面开始处理团购明细，包括每个明细的购买人数量和我是不是也购买了这个商品
		//List<Map> list=new ArrayList<Map>();
		List<XjlDwGroupBuyItem> listGroupBuyItem = (List<XjlDwGroupBuyItem>)ret.get("data");
//		for(XjlDwGroupBuyItem groupBuyItem : listGroupBuyItem){
//			Map<String, String> itemInfo = new HashMap<String, String>();
//			//商品标题
//			itemInfo.put("title", groupBuyItem.groupItemTitle);
//			//商品价格
//			itemInfo.put("price", String.valueOf(groupBuyItem.groupItemPrice));
//			//购买者数量
//			String itemBuyerCount = XjlDwGroupBuyOrder.totalItemBuyer(groupBuyId, groupBuyItem.groupItemId);
//			itemInfo.put("buyerCount", itemBuyerCount);
//			//我有没有购买这个商品的标识
//			boolean isMyItem = false;
//			if (isMyOrder){
//				isMyItem = XjlDwGroupBuyOrder.hasOrderItem(groupBuyId, groupBuyItem.groupItemId, wxUser.wxOpenId,wxUser.currentStudent.studentId);
//			}
//			itemInfo.put("isMyItem", String.valueOf(isMyItem));
//			list.add(itemInfo);
//			
//		}
//		
//		hm.put("itemInfoList", list);
		//下面处理每个学生的购买清单
		List<Map> studentInfoList = new ArrayList<Map>();
		List<XjlDwStudent> studentList = (List)XjlDwStudent.queryByClassId(wxUser.currentClass.classId).get("data");
		for (XjlDwStudent  student : studentList) {
			Map studentInfo = new HashMap();
			studentInfo.put("name", student.studentName);
			//这里应该判断是为那个学生团购的，也就是在团购表中应该加入studentId,但是现在数据库中没有增加，所以进行简化处理，只找到第一个关注人
			List<XjlDwGroupBuyOrder> orderList = XjlDwGroupBuyOrder.queryByGroupIdAndStudentId(groupBuyId,student.studentId);
			
			double amount = 0;
			for(int i = 0; i < orderList.size(); i++){
				for (XjlDwGroupBuyItem item : listGroupBuyItem){
					if (item.groupItemId == orderList.get(i).groupItemId){
						amount+=item.groupItemPrice;
						break;
					}
				}
					
			}
			List<Map> studentOrderItem = new ArrayList<Map>();
			for (XjlDwGroupBuyItem item : listGroupBuyItem){
				boolean isOrder = false;
				for(int i = 0; i < orderList.size(); i++){
					if (item.groupItemId == orderList.get(i).groupItemId){
						isOrder = true;
						break;
					}
				}
				Map itemInfo = new HashMap();
				itemInfo.put("itemTitle", item.groupItemTitle);
				itemInfo.put("isOrder", String.valueOf(isOrder));
				studentOrderItem.add(itemInfo);
			}
			studentInfo.put("itemInfo", studentOrderItem);
			studentInfo.put("count", orderList.size());
			studentInfo.put("amount", String.valueOf(amount));
			studentInfoList.add(studentInfo);
			
		}
		hm.put("studentInfoList", studentInfoList);
		ok(hm);
	}
	
	/**
	 * 保存团购
	 */
	public static void saveGropuBuy(){
		WxUser wxUser = getWXUser();
		if(wxUser.currentClass==null){
			nok("班级丢失");
		}
		XjlDwGroupBuy xjlDwGroupBuy = new XjlDwGroupBuy();
        if (StringUtil.isNotEmpty(params.get("groupBuyId"))) {
            long groupBuyId = StringUtil.getLong(params.get("groupBuyId"));
            xjlDwGroupBuy = XjlDwGroupBuy.findById(groupBuyId);
            if (xjlDwGroupBuy == null) {
                nok("查询不到该团购信息，请查看");
            }
        }
        xjlDwGroupBuy.classId=wxUser.currentClass.classId;
        xjlDwGroupBuy.wxOpenId=wxUser.wxOpenId;
        xjlDwGroupBuy.groupBuyState="1";
    	if(params.get("groupBuyTitle")==null||params.get("groupBuyEndTime")==null){
    		nok(Messages.get("paramsLose"));
    	}
        if (params.get("groupBuyTitle")!= null) {
        	xjlDwGroupBuy.groupBuyTitle = params.get("groupBuyTitle");
        }
        if (params.get("groupBuyEndTime")!= null) {
        	xjlDwGroupBuy.groupBuyEndTime =DateUtil.strToDateByFormat(params.get("groupBuyEndTime"), null);
        }
        xjlDwGroupBuy=XjlDwGroupBuyBo.save(xjlDwGroupBuy);
        if(xjlDwGroupBuy.groupBuyId!=null){
        	if(params.getAll("groupItems")!=null){
        		String [] groupItems=params.getAll("groupItems");
                JSONArray groupItemsArray = JSONArray.fromObject(groupItems); 
                XjlDwGroupBuyItem xjlDwGroupBuyItem ;
	        	for(int i=0;i<groupItemsArray.size();i++){
	        	    JSONObject jsonObject = groupItemsArray.getJSONObject(i);
	        	    xjlDwGroupBuyItem=new XjlDwGroupBuyItem();
	        	    if(StringUtil.isNotEmpty(jsonObject.get("groupItemId"))){
	        	    	long groupItemId = StringUtil.getLong(jsonObject.get("groupItemId").toString());
	        	    	xjlDwGroupBuyItem = XjlDwGroupBuyItem.findById(groupItemId);
	                    if (xjlDwGroupBuyItem == null) {
	                        nok("查询不到该团购条目信息，请查看");
	                    }
	        	    }
	        	    xjlDwGroupBuyItem.groupBuyId=xjlDwGroupBuy.groupBuyId;
	        	    xjlDwGroupBuyItem.groupItemTitle=jsonObject.get("groupItemTitle").toString();
	        	    xjlDwGroupBuyItem.groupItemPrice= Double.valueOf(jsonObject.get("groupItemPrice").toString());
	        	    xjlDwGroupBuyItem=XjlDwGroupBuyItemBo.save(xjlDwGroupBuyItem);
	        	}
			
        	}
	
        }
        //团购新增消息推送
        String jumpUrl = "http://dw201709.com/dw/mobile/A/groupList";
        String templateId = "k5jpj4exq5Kucqtlks-EHWEVGLAV35uGDj3423TTMUU";
        Map<String, Object> mapData = new HashMap<String, Object>();
		Map<String, Object> mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value", "有新团购消息提醒");
		mapData.put("first", mapDataSon);
		mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value",xjlDwGroupBuy.groupBuyTitle);
		mapData.put("Pingou_ProductName", mapDataSon);
		mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value",wxUser.currentClass.className+"的团购信息");
		mapData.put("Weixin_ID", mapDataSon);
		mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value", "有新的团购,赶紧来抢购吧！");
		mapData.put("remark", mapDataSon);
		MsgPush.wxMsgPusheTmplate(templateId, jumpUrl, mapData);
        ok(xjlDwGroupBuy);
        ok(xjlDwGroupBuy);
	}
	/**
	 * 关闭团购,需要团购id参数
	 */
	public static void closeGroupBuy(){
		Long groupBuyId = NumberUtils.toLong(params.get("groupBuyId"));
		XjlDwGroupBuyBo.close(groupBuyId);
		ok();
	}
	/**
	 * 团购列表
	 */
	public static void queryGroupBuyList(){
		WxUser wxUser = getWXUser();
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
		Map condition = params.allSimple();
		//condition.put("classId", wxUser.currentClass.classId);
		Logger.info("wxUser.currentClass.classId==="+wxUser.currentClass.classId);
		Map ret = XjlDwGroupBuy.queryXjlDwGroupBuyListByPage(condition, pageIndex, pageSize);
		if(ret!=null&&ret.get("data")!=null){
			//重新定义data数据内容，增加当前用户是否有订单标识信息
			List<Map> groupBuyInfo = new ArrayList<Map>();
			List<XjlDwGroupBuy> data = (List<XjlDwGroupBuy>)ret.get("data");
			for(XjlDwGroupBuy groupBuy:data){
				XjlDwGroupBuyBo.checkState(groupBuy);
				//1在团购中增加团购明细，这一步暂时不需要
				if (false){
					condition = new HashMap();
					condition.put("groupBuyId", groupBuy.groupBuyId);
					ret = XjlDwGroupBuyItem.queryXjlDwGroupBuyItemListByPage(condition, 1,100);
					if(ret!=null&&ret.get("data")!=null){
						groupBuy.listGroupBuyItem = (List<XjlDwGroupBuyItem>)ret.get("data");
					}
				}
				//2设置新的对象，包含团购和当前用户订单，
				//1），groupBuyInfo代表团购本身的信息
				//2），hasOrderFlag,当前是否参与了团购
				Map<String, Object> groupBuyMap = new HashMap<String, Object>();
				//添加团购信息
				groupBuyMap.put("groupBuyInfo", groupBuy);
				boolean hasOrderFlag = false;
				if(!wxUser.isTeacher){
					hasOrderFlag = XjlDwGroupBuyOrder.hasOrder(groupBuy.groupBuyId, wxUser.wxOpenId,wxUser.currentStudent.studentId);
				}
				//添加订单信息
				groupBuyMap.put("hasOrderFlag", hasOrderFlag);
				groupBuyMap.put("isRunning", XjlDwGroupBuy.groupBuyState_running.equals(groupBuy.groupBuyState));
				groupBuyInfo.add(groupBuyMap);
			}
			ret.put("data", groupBuyInfo);
		}
		filterGroupBuyData(ret);
		ok(ret);
	}
	public static void modifyGroup(){
		Object obj = params.getAll("gradeItem");
		JSONArray gradeList = JSONArray.fromObject(obj); 
		Logger.info("团购长度："+gradeList.size());
		for (int i = 0; i < gradeList.size(); i++) {
			JSONObject gradeJson = gradeList.getJSONObject(i);
			XjlDwGroupBuyItem.modifyPrice(gradeJson.getDouble("groupItemPrice"), gradeJson.getLong("groupItemId"));
		}
		ok();
	}
	/**
	 * 保存团购订单，新建和修改都使用这个方法
	 * 1、删除该用户在该团购中的原有订单
	 * 2、增加新的订单
	 */
	public static void saveGroupBuyOrder(){
		List<XjlDwGroupBuyOrder> listOrderItem=new ArrayList<XjlDwGroupBuyOrder>();
		WxUser wxUser = getWXUser();
		if(wxUser.currentClass==null){
			nok("班级丢失");
		}
		
    	if(params.getAll("groupItems")!=null){
    		String [] groupItems=params.getAll("groupItems");
            JSONArray groupItemsArray = JSONArray.fromObject(groupItems); 
            XjlDwGroupBuyOrder xjlDwGroupBuyOrder;
           
        	for(int i=0;i<groupItemsArray.size();i++){
        	    JSONObject jsonObject = groupItemsArray.getJSONObject(i);
        	    if (i==0){
        	    	//删除该用户在该团购下面的订单清单，只执行一次，放在这里执行主要是为了获取groupBuyId
        	    	Long groupBuyId = StringUtil.getLong(jsonObject.get("groupBuyId").toString());
        	    	XjlDwGroupBuyOrderBo.delByGroupIdAndUserId(groupBuyId, wxUser.wxOpenId, wxUser.currentStudent.studentId);
        	    }
        	    xjlDwGroupBuyOrder=new XjlDwGroupBuyOrder();
        	    if("true".equals(jsonObject.get("isGroupBuy").toString())){
        	    	xjlDwGroupBuyOrder.wxOpenId=wxUser.wxOpenId;
        	    	xjlDwGroupBuyOrder.studentId=wxUser.currentStudent.studentId;
	        	    xjlDwGroupBuyOrder.groupBuyId=StringUtil.getLong(jsonObject.get("groupBuyId").toString());
	        	    xjlDwGroupBuyOrder.groupItemId=StringUtil.getLong(jsonObject.get("groupItemId").toString());
	        	    xjlDwGroupBuyOrder=XjlDwGroupBuyOrderBo.save(xjlDwGroupBuyOrder);
	        	    if(xjlDwGroupBuyOrder!=null){
	        	    	listOrderItem.add(xjlDwGroupBuyOrder);
	        	    }
        	    }
        	}
		
    	}
        ok(listOrderItem);
	}
	
	private static void filterGroupBuyData(Map ret){
		//暂时不需要过滤团购数据
	}
	/**
	 * 相册列表
	 */
	public static void queryAlbumList(){
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
		Map condition = params.allSimple();
		//WxUser wxUser = getWXUser();
		//condition.put("wxopenId",wxUser.wxOpenId);
		Map ret = XjlDwAlbum.queryXjlDwAlbumListByPage(condition, pageIndex, pageSize);
		ok(ret);
	}
	
	public static void delAlbum(){
		Long albumId = Long.parseLong(params.get("albumId"));
		int ret = XjlDwAlbumImage.delAlbumImageByAlbumId(albumId);
		ret = XjlDwAlbum.delAlbumByAlbumId(albumId);
		ok(ret);
	}
	public static void queryAlbumImageList(){
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = 99999;
		Logger.info("pageSize:"+pageSize);
		Map condition = params.allSimple();
		condition.put("albumId",String.valueOf(params.get("albumId")));
		Map ret = XjlDwAlbumImage.queryXjlDwAlbumImageListByPage(condition, pageIndex, pageSize);
		ok(ret);
	}
	/**
	 * 创建相册
	 * @throws ParseException
	 */
	public static void saveAlbum() throws ParseException {
		WxUser wxUser = getWXUser();
		XjlDwAlbum album = new XjlDwAlbum();
        if (StringUtil.isNotEmpty(params.get("albumId"))) {
            long albumId = StringUtil.getLong(params.get("albumId"));
            album = XjlDwAlbum.findById(albumId);
            if (album == null) {
                nok("查询不到该条相册");
            }
        }
        album.wxOpenId=wxUser.wxOpenId;
        if (params.get("albumTemplateId") != null) {
        	album.albumTemplateId = StringUtil.getLong(params.get("albumTemplateId"));
        }
        if (params.get("albumTitle") != null) {
        	album.albumTitle = params.get("albumTitle");
        }
        if (params.get("albumTitle") != null) {
        	album.albumTitle = params.get("albumTitle");
        }
        XjlDwAlbum xjlAlbum = XjlDwAlbumBo.save(album);
        //创建相册增加消息推送
        String jumpUrl = "http://dw201709.com/dw/mobile/A/albumList";
        String templateId = "PPe-Nsdyf-z0v7qSdWODYkxWLOx-aG1ySuDE61jEWlE";
        Map<String, Object> mapData = new HashMap<String, Object>();
		Map<String, Object> mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value", "有新的相册消息提醒哦");
		mapData.put("first", mapDataSon);
		mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value", xjlAlbum.albumTitle);
		mapData.put("keyword1", mapDataSon);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value",df.format(xjlAlbum.createTime));
		mapData.put("keyword2", mapDataSon);
		mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value", "赶紧来看看孩子的照片吧！");
		mapData.put("remark", mapDataSon);
		MsgPush.wxMsgPusheTmplate(templateId,jumpUrl,mapData);
        ok(xjlAlbum);
	}
	 public static void saveAlbumImage(){
		   WxUser wxUser = getWXUser();
		   XjlDwAlbumImage albumImage = new XjlDwAlbumImage();
		   albumImage.wxOpenId = wxUser.wxOpenId;
		   if(params.get("fileId") != null){
			   albumImage.fileId = Long.parseLong(params.get("fileId"));
		   }
		   if(params.get("albumId") != null){
			   albumImage.albumId = Long.parseLong(params.get("albumId"));
		   }
		   albumImage.imageTitle = "Wxphoto";
		   albumImage.imageOrder = Long.parseLong("1");
		   ok(XjlDwAlbumImageBo.save(albumImage));
	   }
	 
	 public static void delAlbumImage (){
		 Long albumImageId = Long.parseLong(params.get("albumImageId"));
		 int ret = XjlDwAlbumImage.delAlbumImageByAlbumImageId(albumImageId);
		 ok(ret);
	 }
	/**
	 * 相册模板列表
	 */
	public static void queryAlbumTemplateList(){
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
		Map condition = params.allSimple();
		Map ret = XjlDwAlbumTemplate.queryXjlDwAlbumTemplateListByPage(condition, pageIndex, pageSize);
		ok(ret);
	}
	
	public static void saveStudent() {    
        String userKey=getSessionKey();
    	WxUser wxUser =  getWXUser();
    	if(wxUser!=null){
    		XjlDwWxClass xjlDwWxClass=new XjlDwWxClass();
    		xjlDwWxClass.classId=1l;
    		xjlDwWxClass.isDefault="Y";
    		xjlDwWxClass.wxOpenId=wxUser.wxOpenId;
    		xjlDwWxClass=XjlDwWxClassBo.save(xjlDwWxClass);
    	}
    	wxUser = WxUser.getFindByOpenId(wxUser.wxOpenId);
    	Cache.set(userKey,wxUser);
    	ok(wxUser);
	}
	
	public static void qryMeunByUserId() {
		//Long userId = params.get("userId", Long.class);
		String openId = params.get("openId");
		WxUser zzbUser = WxUserBo.qryRoleMenu(openId);
		ok(zzbUser);
	}
	//解决logo缓存问题
	public static void getLogo() {
		Long id = params.get("fileId", Long.class);
		XjlDwFile cm = XjlDwFile.findById(id);
		if (cm != null) {
			ok(cm);
		} else {
			nok("该文件不存在");
		}
	}
	
	public static void printExcel() throws IOException{
		WxUser wxUser = getWXUser();
		Long groupBuyId = Long.parseLong(params.get("groupBuyId"));
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 1000);
		Map condition = params.allSimple();
		condition.put("groupBuyId", groupBuyId);
		//设置excel头部
		//String[] headers = {"姓名","七年级下册语文书","七年级下册数学书","七年级下册英语书","合计(元)"};
		List<ExcelEntityUtils> dataset = null;
		String [] header = null;
		//根据团购编号获取团购信息
		List<XjlDwGroupBuyItem> data = (List<XjlDwGroupBuyItem>) XjlDwGroupBuyItem.queryXjlDwGroupBuyItemListByPage(condition, pageIndex, pageSize).get("data");
		//组装动态excel头部
		if(null != data && !data.isEmpty()){
			header = new String[data.size()+3];
			//记录动态标题
			String itemTitlerecord = "";
			header[0]="姓名";
			for (int i = 0;i<data.size();i++) {
				header[i+1] = data.get(i).groupItemTitle;
				itemTitlerecord+=data.get(i).groupItemTitle+",";
			}
			header[data.size()+1] = "合计(元)";
			header[data.size()+2] = "是否付款";
			
			//过滤出家长微信号
			condition = params.allSimple();
			condition.put("patriarch","patriarch");
			List<WxUser> userData = (List<WxUser>) WxUser.queryWxUserListByPage(condition,pageIndex,pageSize).get("data");
			Logger.info("进入循环获取数据:"+userData.size());
			if(null != userData && !userData.isEmpty()){
				//申明存储数据
				dataset = new ArrayList<ExcelEntityUtils>();
				XjlDwWxStudent wxStudent = null;
				boolean isMyOrder = false;
				boolean isMyItem = false;
				ExcelEntityUtils entity  = null;
				double price = 0;
				//开始循环
				for (int i = 0; i < userData.size(); i++) {
					price = 0;
					Logger.info("得到关注微信："+userData.get(i).wxOpenId);
					//所关注学生
					wxStudent = XjlDwWxStudent.queryDefaultByOpenId(userData.get(i).wxOpenId);
					if(null != wxStudent){
						entity = new ExcelEntityUtils();//创建数据集
						entity.setName(wxStudent.student.studentName);
						Logger.info("得到关注学生:"+wxStudent.student.studentName);
						List<Map<String,Object>> _itemGroup = new ArrayList<>();
						Map<String,Object> _mapGroup = null;
						//是否参与购团标示
						isMyOrder = XjlDwGroupBuyOrder.hasOrder(groupBuyId,userData.get(i).wxOpenId,wxStudent.studentId);
						Logger.info("是否参与团购:"+isMyOrder);
						String[] itemTitleArr = null;
						if(isMyOrder){
							//参与团购哪一类商品
							for (int j = 0; j < data.size(); j++) {
								isMyItem = XjlDwGroupBuyOrder.hasOrderItem(groupBuyId, data.get(j).groupItemId, userData.get(i).wxOpenId,wxStudent.studentId);
								Logger.info("参与哪一项团购:"+data.get(j).groupItemTitle+"=result:"+isMyItem);
								if(isMyItem){
									if(!"".equals(itemTitlerecord)){
										itemTitleArr = itemTitlerecord.split(",");
										for (int k = 0; k < itemTitleArr.length; k++) {
											if(itemTitleArr[k].equals((data.get(j).groupItemTitle))){
												_mapGroup = new HashMap<>();
												_mapGroup.put(data.get(j).groupItemTitle,"☑️");
												_itemGroup.add(_mapGroup);
											}
										}
									}
									price +=data.get(j).groupItemPrice;
								}else{
									_mapGroup = new HashMap<>();
									_mapGroup.put(data.get(j).groupItemTitle,"️");
									_itemGroup.add(_mapGroup);
								}
							}
							Logger.info("存入集合："+_itemGroup);
							entity.set_map(_itemGroup);
							entity.setPrice(price);
							entity.setIsPay("否");
							dataset.add(entity);
						}else{
							//没有参与团购参数都为空
							if(!"".equals(itemTitlerecord)){
								itemTitleArr = itemTitlerecord.split(",");
								for (int k = 0; k < itemTitleArr.length; k++) {
										_mapGroup = new HashMap<>();
										_mapGroup.put(itemTitleArr[k],"");
										_itemGroup.add(_mapGroup);
								}
							}
							entity.set_map(_itemGroup);
							entity.setPrice(price);
							entity.setIsPay("否");
							Logger.info("结果:"+entity);
							dataset.add(entity);
						}
					}
				}
			}
		}
		
		String title = "团购导出";
		String savePath = FileUploadPathUtil.getUploadPath(wxUser.wxOpenId);
		Logger.info("excelpath:"+savePath);
		System.out.println(savePath);
		OutputStream out = new FileOutputStream("/home/lls/xjl_dw_zz/_web_/text/"+wxUser.wxOpenId+".xls");
		String downFile="http://dw201709.com/zz/"+"_web_/text/"+wxUser.wxOpenId+".xls";
		Logger.info(""+dataset.size());
		List<Map<String,Object>> dataList = new ArrayList<>();
		Map<String,Object> _map = null;
		for (ExcelEntityUtils excelEntityUtils : dataset) {
			_map = new  LinkedHashMap<>();
			_map.put("name", excelEntityUtils.getName());
			Logger.info("姓名："+excelEntityUtils.getName());
			if(null != excelEntityUtils.get_map() && !excelEntityUtils.get_map().isEmpty()){
				for (Map<String,Object> map: excelEntityUtils.get_map()) {
					for (String key: map.keySet()) {
						Logger.info(key+":"+map.get(key));
						_map.put(key, map.get(key));
					}
				}
			}
			_map.put("price", excelEntityUtils.getPrice());
			_map.put("isPay", excelEntityUtils.getIsPay());
			Logger.info("价格："+excelEntityUtils.getPrice());
			Logger.info("map顺序:"+_map);
			dataList.add(_map);
		}
		Logger.info("整理后集合长度:"+dataList.size());
		ExcelUtil<ExcelEntityUtils> ex = new ExcelUtil<ExcelEntityUtils>();
		ex.exportExcel(title,header,dataList,out,"yyyy-MM-dd");
		out.close();  
		ok(downFile);
	}
	
}
