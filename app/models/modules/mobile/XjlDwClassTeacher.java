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
import utils.jpa.ParamObject;
import utils.jpa.SQLResult;
import utils.jpa.sql.SQLParser;

@Entity
@Table(name = "xjl_dw_class_teacher")
public class XjlDwClassTeacher extends GenericModel{

	@Id
	@Column(name = "CLASS_TEACHER_ID")
	public Long classTeacherId;

	@Column(name = "TEACHER_ID")
	public Long teacherId;

	@Column(name = "CLASS_ID")
	public Long classId;

	@Column(name = "STATUS")
	public String status;

	@Column(name = "CREATE_TIME")
	public Date createTime;

	public static Map queryXjlDwClassTeacherListByPage(Map<String, String> condition,
		int pageIndex, int pageSize) {
		String sql = "select * ";
		sql += "from xjl_dw_class_teacher a ";
		SQLResult ret = SQLParser.parseSQL(sql, condition);
		Query query = JPA.em().createNativeQuery(ret.getSql(), XjlDwClassTeacher.class);
		int i = 1;
		for (ParamObject o : ret.getParams()) {
			query.setParameter(i++, o.getValue());
		}
		List<XjlDwClassTeacher> data = query.setFirstResult((pageIndex - 1) * pageSize)
			.setMaxResults(pageSize).getResultList();
		Query query2 = JPA.em().createNativeQuery(ret.getCountSql());
		int j = 1;
		for (ParamObject o : ret.getParams()) {
			query2.setParameter(j++, o.getValue());
		}
		List<Object> countRet = query2.getResultList();
		long total = Long.parseLong(countRet.get(0).toString());
		Map hm = new HashMap();
		hm.put("total", total);
		hm.put("data", data);
		return hm;
	}
	
	public static XjlDwClassTeacher queryByTeacher(Long teacaherId){
		String sql = "select * "
				+ "from xjl_dw_class_teacher a where a.status='0AA' "
				+ "and teacher_id='"+teacaherId+"'";
		Map<String, String> condition = new HashMap<String, String>();
		condition.put("teacherId", String.valueOf(teacaherId));
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwClassTeacher> data = ModelUtils.queryData(ret,XjlDwClassTeacher.class);
		if(data.isEmpty()){
			return null;
		}else{
			XjlDwClassTeacher xjlDwClass = data.get(0);
			return xjlDwClass;
		}
	}
	
}
