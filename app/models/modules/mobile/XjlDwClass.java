package models.modules.mobile;
import java.sql.*;
import java.util.Date;
import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.db.jpa.Model;
import utils.StringUtil;
import utils.jpa.ParamObject;
import utils.jpa.SQLResult;
import utils.jpa.sql.SQLParser;

@Entity
@Table(name = "xjl_dw_class")
public class XjlDwClass extends GenericModel{

	@Id
	@Column(name = "CLASS_ID")
	public Long classId;

	@Column(name = "CLASS_NAME")
	public String className;

	@Column(name = "CLASS_CODE")
	public String classCode;
	
	@Column(name = "SCHOOL_ID")
	public Long schoolId;

	@Column(name = "STATUS")
	public String status;

	@Column(name = "CREATE_TIME")
	public Date createTime;

	public static Map queryXjlDwClassListByPage(Map<String, String> condition,
		int pageIndex, int pageSize) {
		String sql = "select * ";
		sql += "from xjl_dw_class a ";
		sql += "where STATUS='0AA' [and a.school_id=l:schoolId ][ and a.class_id=l:classId ]  ";
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwClass> data = ModelUtils.queryData(pageIndex, pageSize, ret,XjlDwClass.class);
		return ModelUtils.createResultMap(ret,data);
	}
	/**
	 * 根据班级id得到班级信息
	 * @param classId
	 * @return
	 */
	public static XjlDwClass queryXjlDwClassById(Long classId) {
			String sql = "select * ";
			sql += "from xjl_dw_class a where a.CLASS_ID=[l:classId]";
			Map<String, String> condition = new HashMap<String, String>();
			condition.put("classId", String.valueOf(classId));
			SQLResult ret = ModelUtils.createSQLResult(condition, sql);
			List<XjlDwClass> data = ModelUtils.queryData(0, -1, ret, XjlDwClass.class);
			if (data.isEmpty()){
				throw new RuntimeException("没有找到班级:" + classId);
			} else {
				return data.get(0);
			}
		}
}
