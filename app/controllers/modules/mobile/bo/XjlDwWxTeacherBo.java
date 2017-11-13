package controllers.modules.mobile.bo;

import java.util.List;

import controllers.modules.mobile.filter.MobileFilter;
import models.modules.mobile.WxUser;
import models.modules.mobile.XjlDwWxClass;
import models.modules.mobile.XjlDwWxStudent;
import models.modules.mobile.XjlDwWxTeacher;
import utils.DateUtil;
import utils.SeqUtil;
import utils.StringUtil;
/**
 * @author    lilisheng
 * @version   创建时间：2017-09-25 下午02:46:14
 * @describe  老师微信关系表
*/
public class XjlDwWxTeacherBo {
	/**
	 * 增加或者修改微信和老师对象的关系表
	 * @param xjlDwWxStudent
	 * @return
	 */
	public static XjlDwWxTeacher save(XjlDwWxTeacher wxTeacher) {
		if (wxTeacher.teacherWxId != null) {
		}
		if (wxTeacher.teacherWxId == null) {
			wxTeacher.teacherWxId = SeqUtil.maxValue("xjl_dw_wx_teacher", "teacher_wx_id");
			wxTeacher.status = "0AA";
			wxTeacher.createTime = DateUtil.getNowDate();
		}
		wxTeacher = wxTeacher.save();
		return wxTeacher;
	}
	// 删除
	public static XjlDwWxTeacher del(XjlDwWxTeacher wxTeacher) {
		if (wxTeacher != null) {
			wxTeacher.status = "0XX";
			wxTeacher = wxTeacher.save();
			return wxTeacher;
		}
		return null;
	}
}