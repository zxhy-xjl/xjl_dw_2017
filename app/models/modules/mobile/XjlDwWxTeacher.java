package models.modules.mobile;
import java.sql.*;
import java.util.Date;
import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.db.jpa.Model;
import utils.StringUtil;
import utils.jpa.ParamObject;
import utils.jpa.SQLResult;
import utils.jpa.sql.SQLParser;

@Entity
@Table(name = "xjl_dw_wx_teacher")
public class XjlDwWxTeacher extends GenericModel{

	@Id
	@Column(name = "TEACHER_WX_ID")
	public Long teacherWxId;

	@Column(name = "TEACHER_ID")
	public Long teacherId;

	@Column(name = "WX_OPEN_ID")
	public String wxOpenId;

	@Column(name = "STATUS")
	public String status;
	@Column(name = "CREATE_TIME")
	public Date createTime;
	
	@Transient
	public XjlDwTeacher teacher;
	/**
	 * 得到微信用户所有关注的学生
	 * @param wxOpenId
	 * @return
	 */
	public static Map queryXjlDwWxTeacherListByOpenId(String wxOpenId){
		String sql = "select * ";
		sql += "from xjl_dw_wx_teacher a where a.status='0AA' and WX_OPEN_ID='" + wxOpenId + "'";
		Map<String, String> condition = new HashMap<String, String>();
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwWxTeacher> data = ModelUtils.queryData(0, -1, ret,XjlDwWxTeacher.class);
		//老师信息
		for (XjlDwWxTeacher xjlDwWxTeacher : data) {
			xjlDwWxTeacher.teacher = XjlDwTeacher.findById(xjlDwWxTeacher.teacherId);
		}
		return ModelUtils.createResultMap(ret, data);
	}
	/**
	 * 判断是不是老师
	 * @param wxOpenId
	 * @return
	 */
	public static boolean isTeacher(String wxOpenId){
		String sql = "select count(*) ";
		sql += "from xjl_dw_wx_teacher a where a.status='0AA' and WX_OPEN_ID='" + wxOpenId + "'";
		Map<String, String> condition = new HashMap<String, String>();
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<Object> data = ModelUtils.queryData(0, -1, ret);
		return Long.parseLong(data.get(0).toString())>0;
	}
}
