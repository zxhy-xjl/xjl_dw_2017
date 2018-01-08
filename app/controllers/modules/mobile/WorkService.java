package controllers.modules.mobile;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import models.modules.mobile.ModelUtils;
import models.modules.mobile.WxUser;
import models.modules.mobile.XjlDWGradeChart;
import models.modules.mobile.XjlDwAlbum;
import models.modules.mobile.XjlDwAlbumTemplate;
import models.modules.mobile.XjlDwArticle;
import models.modules.mobile.XjlDwClass;
import models.modules.mobile.XjlDwExam;
import models.modules.mobile.XjlDwExamGrade;
import models.modules.mobile.XjlDwExamSubject;
import models.modules.mobile.XjlDwGroupBuy;
import models.modules.mobile.XjlDwGroupBuyItem;
import models.modules.mobile.XjlDwHomework;
import models.modules.mobile.XjlDwHomeworkFile;
import models.modules.mobile.XjlDwHomeworkModel;
import models.modules.mobile.XjlDwNotice;
import models.modules.mobile.XjlDwWxClass;
import models.modules.mobile.XjlDwStudent;
import models.modules.mobile.XjlDwSubject;
import models.modules.mobile.XjlDwWxStudent;
import play.Logger;
import play.cache.Cache;
import play.i18n.Messages;
import controllers.comm.SessionInfo;
import controllers.modules.mobile.bo.WxUserBo;
import controllers.modules.mobile.bo.XjlDwArticleBo;
import controllers.modules.mobile.bo.XjlDwExamBo;
import controllers.modules.mobile.bo.XjlDwExamGradeBo;
import controllers.modules.mobile.bo.XjlDwExamSubjectBo;
import controllers.modules.mobile.bo.XjlDwGroupBuyBo;
import controllers.modules.mobile.bo.XjlDwGroupBuyItemBo;
import controllers.modules.mobile.bo.XjlDwHomeworkBo;
import controllers.modules.mobile.bo.XjlDwHomeworkFileBo;
import controllers.modules.mobile.bo.XjlDwHomeworkModelBo;
import controllers.modules.mobile.bo.XjlDwWxStudentBo;
import controllers.modules.mobile.filter.MobileFilter;
import controllers.modules.mobile.bo.XjlDwNoticeBo;
import utils.DateUtil;
import utils.MsgPush;
import utils.StringUtil;

/**
 * 作业控制器 作业/成绩
 * 
 * @author lilisheng
 * 
 */
public class WorkService extends MobileFilter {
	
	/**
	 * 考试成绩单列表
	 */
	public static void queryExam() {
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
		WxUser wxUser = getWXUser();
		Map condition = params.allSimple();
		condition.put("classId", wxUser.currentClass.classId);
		Map ret = XjlDwExam.query(condition, pageIndex, 200);
		List<XjlDwExam> list = (List<XjlDwExam>)ret.get("data");
		List<Map> examInfoList = new ArrayList();
		//List<XjlDwStudent> dataStudent = null;
		//List<XjlDwExamSubject> dataExamSubject = null;
		for (XjlDwExam xjlDwExam : list) {
			//Logger.info("进入循环"+xjlDwExam.examTitle);
			Map examInfo = new HashMap();
			examInfo.put("exam", xjlDwExam);
			//dataStudent  = (List<XjlDwStudent>) XjlDwStudent.queryByClassId(wxUser.currentClass.classId).get("data");
		    //dataExamSubject =(List<XjlDwExamSubject>) XjlDwExamSubject.queryByExam(xjlDwExam.examId).get("data");
			//Map mma = XjlDwExamGrade.queryMaxMinAvg(xjlDwExam.examId,dataStudent.size(),dataExamSubject.size());
			examInfo.put("max", xjlDwExam.max == null?0:xjlDwExam.max);
			examInfo.put("min", xjlDwExam.min == null?0:xjlDwExam.min);
			examInfo.put("avg", xjlDwExam.avg == null?0:xjlDwExam.avg);
			examInfoList.add(examInfo);
		}
		ret.put("data", examInfoList);
		ok(ret);
	}
	public static void delExam(){
		Long examId = Long.parseLong(params.get("examId"));
		int ret = XjlDwExamGrade.delExamGradeByExamId(examId);
		ret = XjlDwExam.delExamByExamId(examId);
		ok(ret);
	}
	/**
	 * 保存考试科目，如果考试id为空，则创建考试
	 */
	public static void saveExamSubject(){
		WxUser wxUser = getWXUser();
		XjlDwExam exam = null;
		if (StringUtil.isNotEmpty(params.get("examId"))){
			exam = XjlDwExam.findById(Long.parseLong(params.get("examId")));
		} else {
			exam = new XjlDwExam();
			exam.classId = wxUser.currentClass.classId;
			exam.examTitle = params.get("examTitle");
			if (StringUtil.isEmpty(params.get("examDate"))){
				exam.examDate = DateUtil.getNowDate();
			} else {
				exam.examDate = DateUtil.strToDateByFormat(params.get("examDate"), "yyyy-MM-dd");
			}
			exam.wxOpenId = wxUser.wxOpenId;
			exam = XjlDwExamBo.save(exam);
		}
		String [] examSubjectList=params.getAll("examSubjectList");
        JSONArray examSubjectJsonList = JSONArray.fromObject(examSubjectList); 
        XjlDwExamSubjectBo.delByExam(exam.examId);
        //List<JSONObject> subjectList = new ArrayList<JSONObject>();
        for (int i = 0; i < examSubjectJsonList.size(); i++){
        	if (examSubjectJsonList.getJSONObject(i).getBoolean("isExamSubject")){
        		long subjectId = examSubjectJsonList.getJSONObject(i).getLong("subjectId");
        		XjlDwExamSubjectBo.add(exam.examId, subjectId);
        		//subjectList.add(examSubjectJsonList.getJSONObject(i));
        	}
        }
        //WorkService.initExamGrad(exam.examId,subjectList);
      //考试成绩推送消息
        String jumpUrl = "http://dw201709.com/dw/mobile/W/examList";
        String templateId = "Z1DAi3c8w84yNi50EKSoK-qjTR4_rK3avS-16NJXVac";
        Map<String, Object> mapData = new HashMap<String, Object>();
		Map<String, Object> mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value", "有考试消息提醒");
		mapData.put("first", mapDataSon);
		mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value",exam.examTitle);
		mapData.put("keyword1", mapDataSon);
		mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value",wxUser.currentClass.className);
		mapData.put("keyword2", mapDataSon);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value",df.format(exam.examDate)+"发布");
		mapData.put("keyword3", mapDataSon);
		mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value", "有新的考试,赶紧录入孩子的成绩吧！");
		mapData.put("remark", mapDataSon);
		MsgPush.wxMsgPusheTmplate(templateId,jumpUrl,mapData);
        ok(exam);
	}
//	public static void initExamGrad(Long examId, List<JSONObject> subjectList){
//		WxUser wxUser = getWXUser();
//		List<XjlDwStudent> studentList = (List<XjlDwStudent>)XjlDwStudent.queryByClassId(wxUser.currentClass.classId).get("data");
//		for (XjlDwStudent xjlDwStudent : studentList) {
//			for (JSONObject subject : subjectList) {
//				XjlDwExamGrade grade = new XjlDwExamGrade();
//				grade.examGrade = 0D;
//				grade.examId = examId;
//				grade.studentId = xjlDwStudent.studentId;
//				grade.subjectId = subject.getLong("subjectId");
//				
//				XjlDwExamGradeBo.save(grade);
//			}
//		}
//	}
	/**
	 * 得到考试科目列表
	 */
	public static void queryExamSubjectList(){
		WxUser wxUser = getWXUser();
		Long examId = Long.parseLong(params.get("examId"));
		Map map = XjlDwExamSubject.queryByExam(examId);
        ok(map);
	}
	/**
	 * 保存考试成绩
	 */
	public static void saveExamGrade(){
		WxUser wxUser = getWXUser();
		Long examId = Long.parseLong(params.get("examId"));
		Long studentId = Long.parseLong(params.get("studentId"));
		String [] grade=params.getAll("grade");
		Logger.info("examgrade:"+params.get("grade"));
        JSONArray gradeList = JSONArray.fromObject(grade); 
        for (int i = 0; i < gradeList.size(); i++){
        	JSONObject gradeJson = gradeList.getJSONObject(i);
        	long gradeId = gradeJson.getLong("gradeId");
        	XjlDwExamGrade examGrade = null;
        	if (gradeId==0){
        		examGrade = new XjlDwExamGrade();
        		examGrade.examId = examId;
        		examGrade.studentId = studentId;
        		examGrade.subjectId = gradeJson.getLong("subjectId");
        	} else {
        		examGrade = XjlDwExamGrade.findById(gradeId);
        	}
        	Double.parseDouble(String.valueOf(gradeJson.get("gradeValue")));
        	examGrade.examGrade = gradeJson.getDouble("gradeValue");
        	XjlDwExamGradeBo.save(examGrade);
        }
		ok();
	}
	
	public static void saveExamStat(){
		String[] allgrade = params.getAll("allgrade");
		Logger.info("allgrade:"+params.get("allgrade"));
		JSONArray allgradeList = JSONArray.fromObject(allgrade); 
		String[] subjectGrade = params.getAll("subjectGrade");
		Logger.info("subjectGrade:"+params.get("subjectGrade"));
		JSONArray gradeList = JSONArray.fromObject(subjectGrade); 
		XjlDwExam dwExam = new XjlDwExam();
		dwExam.max = allgradeList.getJSONObject(0).getDouble("max");
		dwExam.min = allgradeList.getJSONObject(0).getDouble("min");
		dwExam.avg = allgradeList.getJSONObject(0).getDouble("avg");
		dwExam.examId = allgradeList.getJSONObject(0).getLong("examId");
		XjlDwExam.modifyExamStatByExamId(dwExam);
		XjlDwExamSubject xjlDwExamSubject = null;
		for (int i = 0; i < gradeList.size(); i++) {
			JSONObject gradeJson = gradeList.getJSONObject(i);
			xjlDwExamSubject = new XjlDwExamSubject();
			xjlDwExamSubject.max = gradeJson.getDouble("max");
			xjlDwExamSubject.min = gradeJson.getDouble("min");
			xjlDwExamSubject.avg = gradeJson.getDouble("avg");
			xjlDwExamSubject.examSubjectId = gradeJson.getLong("examSubjectId");
			XjlDwExamSubject.modifyExamByExam(xjlDwExamSubject);
		}
		ok();
	}
	/**
	 * 得到所有学生成绩信息
	 */
	public static void queryStudentExamGradeList(){
		WxUser wxUser = getWXUser();
		//获取所有科目
		List<XjlDwSubject> subjectList = XjlDwSubject.all().fetch();
		//通过考试编号获取所有成绩
		Long examId = StringUtil.getLong(params.get("examId"));
		List<XjlDwExamSubject> examSubjectList = (List<XjlDwExamSubject>)XjlDwExamSubject.queryByExam(examId).get("data");
		XjlDwExam  xjldwExam = XjlDwExam.queryById(examId);
		Logger.info("得到考场名称:"+xjldwExam.examTitle);
		List<Map> studentInfoList = new ArrayList<Map>();
		List<XjlDwExamGrade> studentGradeList = null;
		List<Map> gradeList = null;
		//通过班级得到所有学生
		Map map = XjlDwStudent.queryByClassId(wxUser.currentClass.classId);
		Logger.info("入口："+wxUser.isTeacher+":"+wxUser.isCommittee);
		//老师入口
		if(wxUser.isTeacher){
			List<XjlDwStudent> studentList = (List<XjlDwStudent>)map.get("data");
			Map studentInfo = null;
			Map gradeInfo  = null;
			//遍历学生
			for (XjlDwStudent student : studentList) {
				studentInfo = new HashMap();
				studentInfo.put("student", student);
				gradeList = new ArrayList();
				//通过考试编号与学生编号得到成绩
				studentGradeList = (List<XjlDwExamGrade>)XjlDwExamGrade.queryByStudentAndExam(examId, student.studentId).get("data");
				//遍历所有考试成绩
				for (XjlDwExamSubject xjlDwExamSubject : examSubjectList) {
					gradeInfo = new HashMap();
					gradeInfo.put("subjectId", xjlDwExamSubject.subjectId);
					//遍历所有科目
					for (XjlDwSubject xjlDwSubject : subjectList) {
						//把学生的考试成绩与所有科目进行比对记录
						if (xjlDwSubject.subjectId == xjlDwExamSubject.subjectId){
							gradeInfo.put("subjectTitle", xjlDwSubject.subjectTitle);
							gradeInfo.put("gradeId", "0");
							gradeInfo.put("gradeValue", "0");
							break;
						}
					}
					//得到学习成绩
					for (XjlDwExamGrade grade : studentGradeList) {
						if (grade.subjectId == xjlDwExamSubject.subjectId){
							gradeInfo.put("gradeId", grade.examGradeId);
							gradeInfo.put("gradeValue", grade.examGrade);
							break;
						}
					}
					//得到每科的 最高分 最低分 平均成绩
					Logger.info("得到每一科目平均分"+xjlDwExamSubject.max);
					gradeInfo.put("max",xjlDwExamSubject.max == null?0:xjlDwExamSubject.max);
					gradeInfo.put("min",xjlDwExamSubject.min == null?0:xjlDwExamSubject.min);
					gradeInfo.put("avg",xjlDwExamSubject.avg == null?0:xjlDwExamSubject.avg);
					gradeInfo.put("examSubjectId", xjlDwExamSubject.examSubjectId);
					gradeList.add(gradeInfo);
				}
				studentInfo.put("grade", gradeList);
				double total = 0;
				for (XjlDwExamGrade grade : studentGradeList) {
					Logger.info("遍历成绩计算总分："+total);
					total += grade.examGrade;
				}
				studentInfo.put("total", total);
				studentInfo.put("exam", xjldwExam);
				studentInfoList.add(studentInfo);
			}
			
		}
		//家长和家委会入口
		else{
			Map studentInfo = new HashMap();
			Map gradeInfo = null;
			gradeList = new ArrayList();
			studentInfo.put("student", wxUser.currentStudent);
			Logger.info("家长入口："+wxUser.currentStudent.studentName);
			//通过考试编号与学生编号得到成绩
			studentGradeList = (List<XjlDwExamGrade>)XjlDwExamGrade.queryByStudentAndExam(examId,wxUser.currentStudent.studentId).get("data");
			Logger.info("通过考试编号和学生编号得到成绩:"+studentGradeList.size());
			//遍历所有考试成绩
			for (XjlDwExamSubject xjlDwExamSubject : examSubjectList) {
				gradeInfo = new HashMap();
				gradeInfo.put("subjectId", xjlDwExamSubject.subjectId);
				//遍历所有科目
				for (XjlDwSubject xjlDwSubject : subjectList) {
					Logger.info("科目:"+xjlDwSubject.subjectTitle);
					//把学生的考试成绩与所有科目进行比对记录
					if (xjlDwSubject.subjectId == xjlDwExamSubject.subjectId){
						gradeInfo.put("subjectTitle", xjlDwSubject.subjectTitle);
						gradeInfo.put("gradeId", 0);
						gradeInfo.put("gradeValue",0);
						break;
					}
				}
				//得到学习成绩
				for (XjlDwExamGrade grade : studentGradeList) {
					if (grade.subjectId == xjlDwExamSubject.subjectId){
						gradeInfo.put("gradeId", grade.examGradeId);
						gradeInfo.put("gradeValue", grade.examGrade);
						break;
					}
				}
				//得到每科的 最高分 最低分 平均成绩
				Logger.info("得到每一科目平均分"+xjlDwExamSubject.max);
				gradeInfo.put("max",xjlDwExamSubject.max == null?0:xjlDwExamSubject.max);
				gradeInfo.put("min",xjlDwExamSubject.min == null?0:xjlDwExamSubject.min);
				gradeInfo.put("avg",xjlDwExamSubject.avg == null?0:xjlDwExamSubject.avg);
				gradeInfo.put("examSubjectId", xjlDwExamSubject.examSubjectId);
				gradeList.add(gradeInfo);
			}
			studentInfo.put("grade", gradeList);
			double total = 0;
			for (XjlDwExamGrade grade : studentGradeList) {
				Logger.info("遍历成绩计算总分："+total);
				Logger.info("总分:"+grade.studentId+">"+grade.subjectId);
				total += grade.examGrade;
			}
			studentInfo.put("total", total);
			studentInfo.put("exam", xjldwExam);
			studentInfoList.add(studentInfo);
		}
		map.put("data", studentInfoList);
		ok(map);
	}
	
	public static void queryChart(){
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
		WxUser wxUser = getWXUser();
		Map condition = params.allSimple();
		condition.put("classId", wxUser.currentClass.classId);
		//通过班级Id得到关联的所有考试
		Map ret = XjlDwExam.query(condition, pageIndex, pageSize);
		List<XjlDwExam> list = (List<XjlDwExam>)ret.get("data");
		List<XjlDWGradeChart> dataChart = new ArrayList<>();
		XjlDWGradeChart chart = null;
		List<XjlDwExamSubject> dataExamSubject = null;
		List<XjlDwSubject> xjlDwSubjectList = null;
		double  grade = 0;
		Long  studentId = 0L;
		Logger.info("用户类型老师："+wxUser.isTeacher);
		Logger.info("用户类型家委会："+wxUser.isCommittee);
		Logger.info("用户类型家长："+wxUser.isTeacher);
		String flag = params.get("flag");
		//通过班级得到所有学生
		Map map = XjlDwStudent.queryByClassId(wxUser.currentClass.classId);
		List<XjlDwStudent> studentList = (List<XjlDwStudent>)map.get("data");
		DecimalFormat df= new DecimalFormat("######0.00");   
		//判断老师家委会 得到所有学生的统计数据
		if(wxUser.isTeacher&&flag==null){
			//考试名称、各个科目，每场考试的各个科目的总分
			//遍历
			for (XjlDwExam xjlDwExam : list) {
				//得到每一场考试的科目
				dataExamSubject =(List<XjlDwExamSubject>) XjlDwExamSubject.queryByExam(xjlDwExam.examId).get("data");
				for (XjlDwExamSubject xjlDwExamSubject : dataExamSubject) {
					chart = new XjlDWGradeChart();
					chart.exam = xjlDwExam.examTitle;
					Logger.info("考试:"+xjlDwExam.examTitle);
					//得到科目名称
					xjlDwSubjectList = (List<XjlDwSubject>) XjlDwSubject.queryXjlDwBySubjectId(xjlDwExamSubject.subjectId).get("data");
					grade = XjlDwExamGrade.queryGrade(xjlDwExam.examId, xjlDwExamSubject.subjectId,studentId);
					Logger.info("科目:"+xjlDwSubjectList.get(0).subjectTitle+" 分数:"+grade);
					chart.type = xjlDwSubjectList.get(0).subjectTitle;
					chart.temperature = xjlDwExamSubject.avg;
					dataChart.add(chart);
				}
			}
		}
		//家长入口
		else{
			//通过班级得到所有学生
			//Map map = XjlDwStudent.queryByClassId(wxUser.currentClass.classId);
			//遍历所有场考试
			for (XjlDwExam xjlDwExam : list) {
				//得到每一场考试的科目
				dataExamSubject =(List<XjlDwExamSubject>) XjlDwExamSubject.queryByExam(xjlDwExam.examId).get("data");
				//遍历该场考试的科目
				for (XjlDwExamSubject xjlDwExamSubject : dataExamSubject) {
					chart = new XjlDWGradeChart();
					chart.exam = xjlDwExam.examTitle;
					Logger.info("考试:"+xjlDwExam.examTitle);
					//得到科目名称
					xjlDwSubjectList = (List<XjlDwSubject>) XjlDwSubject.queryXjlDwBySubjectId(xjlDwExamSubject.subjectId).get("data");
					grade = XjlDwExamGrade.queryGrade(xjlDwExam.examId, xjlDwExamSubject.subjectId,wxUser.currentStudent.studentId);
					Logger.info("科目:"+xjlDwSubjectList.get(0).subjectTitle+" 分数:"+grade);
					chart.type = xjlDwSubjectList.get(0).subjectTitle;
					chart.temperature = grade;
					dataChart.add(chart);
				}
			}
		}
		ok(dataChart);
	}
	/**
	 * 得到所有科目
	 */
	public static void querySubjectList(){
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
		WxUser wxUser = getWXUser();
		Map condition = params.allSimple();
		Map map = XjlDwSubject.queryXjlDwSubjectListByPage(condition, pageIndex, pageSize);
		ok(map);
	}
	/**
	 * 查询考试成绩
	 */
	public static void queryExamGrade(){
		WxUser wxUser = getWXUser();
		Long examId = StringUtil.getLong(params.get("examId"));
		Long studentId = StringUtil.getLong(params.get("studentId"));
		Map map = XjlDwExamGrade.queryByStudentAndExam(examId, studentId);
		Logger.info("平均分:"+map);
		List<XjlDwExamGrade> gradeList= (List<XjlDwExamGrade>)map.get("data");
		ok(map);
	}
	public static void delHomeWork(){
		Long homeWorkId = Long.parseLong(params.get("homeWorkId"));
		XjlDwHomeworkFile.delHomeworkFileModelByhomeworkId(homeWorkId);
		int ret = XjlDwHomeworkModel.delHomeworkModelByhomeworkId(homeWorkId);
		ret = XjlDwHomework.delHomeworkByhomeworkId(homeWorkId);
		ok(ret);
	}
	/**
	 * 保存家庭作业
	 */
	public static void saveHomework(){
		WxUser wxUser = getWXUser();
		String homeworkString=params.get("homework");
        JSONObject homeworkJson = JSONObject.fromObject(homeworkString); 
        XjlDwHomework homework = new XjlDwHomework();
        homework.classId = wxUser.currentClass.classId;
        homework.homeworkContent = homeworkJson.getString("homeworkContent");
        homework.homeworkTitle = homeworkJson.getString("homeworkTitle");
        homework.subjectId = homeworkJson.getJSONObject("subject").getLong("subjectId");
        XjlDwHomework xjlDwHomework  = XjlDwHomeworkBo.save(homework);
        
        XjlDwHomeworkFile _xjlDwHomework = null;
        String fileids = homeworkJson.getString("homeworkFileIds");
        String [] arrayFile = fileids.split(",");
        if(fileids.length() > 0){
        	for (String str : arrayFile) {
            	_xjlDwHomework = new XjlDwHomeworkFile();
            	_xjlDwHomework.homeworkId = xjlDwHomework.homeworkId;
            	_xjlDwHomework.fileId = Long.valueOf(str);
            	_xjlDwHomework.wxOpenId = wxUser.wxOpenId;
            	XjlDwHomeworkFileBo.save(_xjlDwHomework);
            }
        }
        ok(xjlDwHomework);
	}
	public static void homeworkPushMsg(){
		 List<XjlDwSubject> data  = (List<XjlDwSubject>) XjlDwSubject.queryXjlDwBySubjectId(StringUtil.getLong(String.valueOf(params.get("subjectId")))).get("data");
		 //家庭作业消息推送
       String jumpUrl = "http://dw201709.com/zz/mobile/W/homeworkList";
       String templateId = "9H40e9q5DHGkzuirevpRCynHV9llBgdawImeZV7EEOQ";
       Map<String, Object> mapData = new HashMap<String, Object>();
		Map<String, Object> mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value", "【"+params.get("homeworkTitle")+"】");
		mapDataSon.put("color", "#68A8C3");
		mapData.put("first", mapDataSon);
		mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value",data.get(0).subjectTitle);
		mapData.put("keyword1", mapDataSon);
		mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value",params.get("homeworkTitle"));
		mapData.put("keyword2", mapDataSon);
		mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value","周一");
		mapData.put("keyword3", mapDataSon);
		mapDataSon = new HashMap<String, Object>();
		mapDataSon.put("value",params.get("homeworkContent"));
		mapDataSon.put("color","#808080");
		mapData.put("remark", mapDataSon);
		MsgPush.wxMsgPusheTmplate(templateId, jumpUrl, mapData);
	}
	/**
	 * 保存标榜
	 */
	public static void saveHomeworkRemark(){
		WxUser wxUser = getWXUser();
		String remarkString=params.get("remark");
		JSONObject remarkJson = JSONObject.fromObject(remarkString); 
		XjlDwHomeworkModel model = new XjlDwHomeworkModel();
		model.homeworkId=Long.parseLong(remarkJson.getString("homeworkId"));
		model.modelTitle = remarkJson.getString("remark");
		model.modelContent=remarkJson.getString("image");
		XjlDwHomeworkModelBo.save(model);
		ok();
	}
	/**
	 * 查询家庭作业
	 */
	public static void queryHomeworkList(){
		WxUser wxUser = getWXUser();
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
		Map condition = params.allSimple();
		condition.put("classId", wxUser.currentClass.classId);
		Map map = XjlDwHomework.queryXjlDwHomeworkListByPage(condition, pageIndex, pageSize);
		List<XjlDwHomework> list = (List<XjlDwHomework>)map.get("data");
		List<Map> homeworkInfoList = new ArrayList();
		for (XjlDwHomework homework : list){
			Map homeworkInfo = new HashMap<>();
			homeworkInfo.put("homework", homework);
			if (homework.subjectId != null){
				XjlDwSubject subject = XjlDwSubject.findById(homework.subjectId);
				homeworkInfo.put("subjectTitle", subject.subjectTitle);
			} else {
				homeworkInfo.put("subjectTitle", "");
			}
			long modelCount = XjlDwHomeworkModel.queryCountByHomework(homework.homeworkId);
			homeworkInfo.put("modelCount", modelCount);
			homeworkInfoList.add(homeworkInfo);
		}
		map.put("data", homeworkInfoList);
		ok(map);
	}
	/**
	 * 
	 */
	public static void queryHomeworkRemarkList(){
		WxUser wxUser = getWXUser();
		int pageIndex = StringUtil.getInteger(params.get("PAGE_INDEX"), 1);
		int pageSize = StringUtil.getInteger(params.get("PAGE_SIZE"), 100);
		Map condition = params.allSimple();
		Map map = XjlDwHomeworkModel.queryXjlDwHomeworkModelListByPage(condition, pageIndex, pageSize);
		ok(map);
	}
}
