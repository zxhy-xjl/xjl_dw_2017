package controllers.modules.mobile.bo;

import models.modules.mobile.XjlDwArticleFile;
import utils.DateUtil;
import utils.SeqUtil;

public class XjlDwAriticelFIleBo {
	public static XjlDwArticleFile save(XjlDwArticleFile xjlDwArticleFile){
		xjlDwArticleFile.articleFileId = SeqUtil.maxValue("xjl_dw_article_file", "article_file_id");
		xjlDwArticleFile.status ="0AA";
		xjlDwArticleFile.createTime = DateUtil.getNowDate();
		xjlDwArticleFile = xjlDwArticleFile.save();
		return xjlDwArticleFile;
	};
	
}
