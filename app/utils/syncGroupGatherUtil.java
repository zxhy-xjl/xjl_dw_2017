package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controllers.modules.mobile.bo.XjlDwGroupGatherBo;
import models.modules.mobile.WxUser;
import models.modules.mobile.XjlDwGroupBuy;
import models.modules.mobile.XjlDwGroupBuyItem;
import models.modules.mobile.XjlDwGroupBuyOrder;
import models.modules.mobile.XjlDwGroupGather;
import models.modules.mobile.XjlDwStudent;
import models.modules.mobile.XjlDwWxStudent;
import play.Logger;

public class syncGroupGatherUtil {

	
	public static void syncGroupGather(Long groupBuyId,String groupBuyTitle,String wxopenid){
		Logger.info("进入同步团购数据方法");
		//同步团购数据
		List<Map> listgather = new ArrayList<>();
		Map<String,Object> _temp = null;
		List<XjlDwGroupBuyOrder> groupBuyOrderList = null;
		Map condition = new HashMap<>();
		condition.put("classId", 1);
		 PinyinHelper ph = PinyinHelper.getInstance();
		Map map = XjlDwStudent.queryXjlDwStudentListByPage(condition, 1, 100);
		if(null != map){
			List<XjlDwStudent> studentDate = (List<XjlDwStudent>) map.get("data");
			for (int i = 0; i < studentDate.size(); i++) {
				_temp = new HashMap<>();
            	_temp.put("gatherStudentName",studentDate.get(i).studentName);
            	_temp.put("studentId",studentDate.get(i).studentId);
            	ph.setResource(studentDate.get(i).studentName);
            	_temp.put("gatherStudentPingyin",ph.getSpellingHead());
            	condition.put("groupBuyId",groupBuyId);
            	map = XjlDwGroupBuyItem.queryXjlDwGroupBuyItemListByPage(condition, 1, 999);
            	if(null !=map){
            		List<XjlDwGroupBuyItem> groupBuyItem = (List<XjlDwGroupBuyItem>) map.get("data");
            		if(!groupBuyItem.isEmpty()){
            			//该团购所有条目
            			for (int j = 0; j < groupBuyItem.size(); j++) {
            				_temp.put("param_"+(j+1),groupBuyItem.get(j).groupItemTitle);
						}
            		}
            		_temp.put("gatherPrice", 0);
                	_temp.put("singBuy", 0);
                	_temp.put("singBuyNum", 0);
                	_temp.put("gatherQuantity","0");
                	_temp.put("gatherPay","T");
                	listgather.add(_temp);
            	}
			}
			
			Logger.info("组装好数据结构："+listgather.size());
			if(!listgather.isEmpty()){
				XjlDwGroupGather gg = null;
				for (int i = 0; i < listgather.size(); i++) {
					gg = new XjlDwGroupGather();
					gg.groupBuyId = groupBuyId;
					gg.studentId = Long.parseLong(String.valueOf(listgather.get(i).get("studentId")));
					gg.gatherStudentName =String.valueOf( listgather.get(i).get("gatherStudentName"));
					gg.gatherStudentPingyin = String.valueOf(listgather.get(i).get("gatherStudentPingyin"));
					gg.groupbuyTitle = groupBuyTitle;
					gg.param_1 = String.valueOf(listgather.get(i).get("param_1"));
					gg.param_2 = String.valueOf(listgather.get(i).get("param_2"));
					gg.param_3 = String.valueOf(listgather.get(i).get("param_3"));
					gg.param_4 = String.valueOf(listgather.get(i).get("param_4"));
					gg.param_5 = String.valueOf(listgather.get(i).get("param_5"));
					gg.param_6 = String.valueOf(listgather.get(i).get("param_6"));
					gg.param_7 = String.valueOf(listgather.get(i).get("param_7"));
					gg.param_8 = String.valueOf(listgather.get(i).get("param_8"));
					gg.param_9 = String.valueOf(listgather.get(i).get("param_9"));
					gg.param_10 = String.valueOf(listgather.get(i).get("param_10"));
					gg.param_11 = String.valueOf(listgather.get(i).get("param_11"));
					gg.param_12 = String.valueOf(listgather.get(i).get("param_12"));
					gg.param_13 = String.valueOf(listgather.get(i).get("param_13"));
					gg.param_14 = String.valueOf(listgather.get(i).get("param_14"));
					gg.param_15 = String.valueOf(listgather.get(i).get("param_15"));
					gg.param_16 = String.valueOf(listgather.get(i).get("param_16"));
					gg.param_17 = String.valueOf(listgather.get(i).get("param_17"));
					gg.param_18 = String.valueOf(listgather.get(i).get("param_18"));
					gg.param_19 = String.valueOf(listgather.get(i).get("param_19"));
					gg.param_20 = String.valueOf(listgather.get(i).get("param_20"));
					gg.gatherQuantity = String.valueOf(listgather.get(i).get("gatherQuantity"));
					gg.gatherPrice = String.valueOf(listgather.get(i).get("gatherPrice"));
					gg.gatherPay = String.valueOf(listgather.get(i).get("gatherPay"));
					gg.wxOpenId = wxopenid;
					gg.singBuy = String.valueOf(listgather.get(i).get("singBuy"));
					gg.singBuyNum = String.valueOf(listgather.get(i).get("singBuyNum"));
					XjlDwGroupGatherBo.save(gg);
				}
			}
			
		}
	}
}
