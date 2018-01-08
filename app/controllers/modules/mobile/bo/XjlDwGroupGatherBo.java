package controllers.modules.mobile.bo;


import models.modules.mobile.XjlDwGroupGather;
import utils.DateUtil;
import utils.SeqUtil;

public class XjlDwGroupGatherBo {

	
	public static XjlDwGroupGather save(XjlDwGroupGather xjlDwGroupGather){
		xjlDwGroupGather.gatherId = SeqUtil.maxValue("xjl_dw_group_gather","gather_id");
		xjlDwGroupGather.status ="0AA";
		xjlDwGroupGather.createTime = DateUtil.getNowDate();
		
		xjlDwGroupGather = xjlDwGroupGather.save();
		return xjlDwGroupGather;
	}
	
	
	public static XjlDwGroupGather del(XjlDwGroupGather xjlDwGroupGather){
		xjlDwGroupGather.status = "0XX";
		xjlDwGroupGather = xjlDwGroupGather.save();
		return xjlDwGroupGather;
	}
}
