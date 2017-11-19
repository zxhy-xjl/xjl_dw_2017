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

import org.slf4j.LoggerFactory;

import controllers.comm.BOResult;
import controllers.comm.SessionInfo;


import play.Logger;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.db.jpa.Model;
import utils.StringUtil;
import utils.jpa.ParamObject;
import utils.jpa.SQLResult;
import utils.jpa.sql.SQLParser;

@Entity
@Table(name = "wx_user")
public class WxUser extends GenericModel{
	private static org.slf4j.Logger log = LoggerFactory.getLogger(WxUser.class);
	@Id
	@Column(name = "WX_OPEN_ID")
	public String wxOpenId;

	@Column(name = "TEACHER_ID")
	public Long teacherId;
	
	@Column(name = "SCHOOL_ID")
	public Long schoolId;

	@Column(name = "NICK_NAME")
	public String nickName;

	@Column(name = "HEAD_IMG_URL")
	public String headImgUrl;

	@Column(name = "SEX")
	public String sex;

	@Column(name = "LANGUAGE")
	public String language;

	@Column(name = "COUNTRY")
	public String country;

	@Column(name = "PROVINCE")
	public String province;

	@Column(name = "CITY")
	public String city;

	@Column(name = "WX_PHONE")
	public String wxPhone;
	
	@Column(name = "USER_PWD")
	public String userPwd;

	@Column(name = "STATUS")
	public String status;

	@Column(name = "CREATE_TIME")
	public Date createTime;
	
	@Column(name = "UP_OPENID_TIME")
	public Date upOpenidTime;

	@Column(name = "IS_CONCERNED")
	public String isConcerned;
	
	@Column(name = "OPEN_ID_CHANNCEL")
	public String openIdChanncel;
	//当前班级（家长和老师都会有这个值）
	@Transient
	public XjlDwClass currentClass;
	@Transient
	public String teacherName;
	//当前学生（家长会有这个值，老师没有）
	@Transient
	public XjlDwStudent currentStudent;
	//当前是家长
	@Transient
	public boolean isParent;
	//当前是家委会
	@Transient
	public boolean isCommittee;
	//当前是老师
	@Transient
	public boolean isTeacher;
	//菜单List
	@Transient
	public List<XjlDwMenu> menuArrayList;

	/***
	 * 根据openId得到微信用户
	 * @param openid
	 * @return
	 */
	public static WxUser getFindByOpenId(String openid) {
		log.debug("getFindByOpenId方法openId:" + openid);
		int pageIndex = 1;
        int pageSize = 100;
        Map condition = new HashMap<String, String>();
        if(StringUtil.isNotEmpty(openid)){
            condition.put("wxOpenId", openid);
        }
        Map returnMap = queryWxUserListByPage(condition,pageIndex,pageSize);
        List<WxUser> retData = (List<WxUser>)returnMap.get("data");
        if (retData.isEmpty()){
        	throw new RuntimeException("没有该用户:"+openid);
        } else {
        	log.debug("一共查询符合条件的数据有:" + retData.size());
        	WxUser wxUser = retData.get(0);
        	log.debug("获取第一个符合条件的微信数据:" + wxUser.nickName);
//        	//查询该用户默认关注的编辑和学生
//        	XjlDwWxClass wxClass = XjlDwWxClass.getDefaultByOpenId(openid);
//        	if (wxClass != null){
//        		wxUser.currentClass = XjlDwClass.queryXjlDwClassById(wxClass.classId);
//        		XjlDwWxStudent wxStudent = XjlDwWxStudent.queryDefaultByOpenId(openid);
//        		if (wxStudent != null){
//        			wxUser.currentStudent = wxStudent.student;
//        		}
//        	}
        	//查询该用户默认关注的编辑和学生
        	XjlDwWxStudent wxStudent = XjlDwWxStudent.queryDefaultByOpenId(wxUser.wxOpenId);
        	if (wxStudent != null){
    			wxUser.currentStudent = wxStudent.student;
    			wxUser.currentClass=wxStudent.dwClass;
    			wxUser.isParent = true;
    			wxUser.isCommittee = XjlDwParentCommittee.isParentCommittee(wxUser.currentClass.classId, wxUser.wxOpenId);
    			wxUser.teacherName="default";
    		}
        	//查询老师对应的班级信息
        	else if(wxUser.teacherId != null){
        		XjlDwClassTeacher classTeacher = XjlDwClassTeacher.queryByTeacher(wxUser.teacherId);
    			if(null != classTeacher){
    				XjlDwClass dwClass = XjlDwClass.queryXjlDwClassById(classTeacher.classId);
    				Logger.info("wxuserClassTeacher"+dwClass.className);
    				wxUser.currentClass=dwClass;
    				XjlDwStudent student = new XjlDwStudent();
    				student.studentName = "text";
    				wxUser.currentStudent = student;
    				wxUser.teacherName = XjlDwTeacher.queryByTeacherId(wxUser.teacherId).teacherName;
    			}
        	}
        	Logger.info("获取第一个符合条件老师:" + wxUser.teacherId);
        	Logger.info("获取第一个符合条件家会家长:" + wxUser.isCommittee);
        	wxUser.isTeacher = wxUser.teacherId != null;
        	Logger.info("WxisTeacher:"+wxUser.isTeacher);
        	return wxUser;
        }
	}
	/**
	 * 查询用户
	 * @param condition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public static Map queryWxUserListByPage(Map<String, String> condition,
		int pageIndex, int pageSize) {
		String sql = "select * ";
		sql += "from wx_user a ";
		sql += "where 1=1 ";
		if(StringUtil.isNotEmpty(condition.get("wxOpenId"))){
			String searchKeyWord = condition.get("wxOpenId");
			sql += "and a.wx_open_id='"+searchKeyWord+"' ";
	    }
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<WxUser> data = ModelUtils.queryData(pageIndex, pageSize, ret, WxUser.class);
		return ModelUtils.createResultMap(ret, data);
	}
	

	public static int bindTeacher(Long teacherId,String wxOpenId){
		String sql = "update wx_user set teacher_id='"+teacherId+"' where wx_open_id='"+wxOpenId+"'";
		Map<String, String> condition = new HashMap<String, String>();
		return ModelUtils.executeDelete(condition, sql);
	}
}
