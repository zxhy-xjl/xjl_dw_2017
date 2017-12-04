package controllers.modules.mobile.bo;

import models.modules.mobile.XjlDwHomeworkFile;
import utils.DateUtil;
import utils.SeqUtil;

public class XjlDwHomeworkFileBo {

	public static XjlDwHomeworkFile save(XjlDwHomeworkFile xjlDwHomeworkFile){
		if(xjlDwHomeworkFile.homeworkFileId == null){
			xjlDwHomeworkFile.homeworkFileId = SeqUtil.maxValue("xjl_dw_homework_file", "homework_file_id");
			xjlDwHomeworkFile.status ="0AA";
			xjlDwHomeworkFile.createTime = DateUtil.getNowDate();
		}
		xjlDwHomeworkFile = xjlDwHomeworkFile.save();
		return xjlDwHomeworkFile;
	}
}
