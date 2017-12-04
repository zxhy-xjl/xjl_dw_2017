package controllers.modules.mobile.bo;

import models.modules.mobile.XjlDwNoticeFile;
import utils.DateUtil;
import utils.SeqUtil;

public class XjlDwNoticeFileBo {
	
	public static XjlDwNoticeFile save(XjlDwNoticeFile xjlDwNoticeFile){
		if(xjlDwNoticeFile.noticeFileId == null){
			xjlDwNoticeFile.noticeFileId = SeqUtil.maxValue("xjl_dw_notice_file", "notice_file_id");
			xjlDwNoticeFile.status ="0AA";
			xjlDwNoticeFile.createTime = DateUtil.getNowDate();
		}
		xjlDwNoticeFile = xjlDwNoticeFile.save();
		return xjlDwNoticeFile;
	}

}
