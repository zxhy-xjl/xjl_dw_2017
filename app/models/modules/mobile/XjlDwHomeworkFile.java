package models.modules.mobile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.jpa.GenericModel;
import utils.jpa.SQLResult;

@Entity
@Table(name = "xjl_dw_homework_file")
public class XjlDwHomeworkFile extends GenericModel {

	@Id
	@Column(name = "HOMEWORK_FILE_ID")
	public Long homeworkFileId;
	@Column(name = "HOMEWORK_ID")
	public Long homeworkId;
	@Column(name = "FILE_ID")
	public Long fileId;
	@Column(name = "WX_OPEN_ID")
	public String wxOpenId;

	@Column(name = "STATUS")
	public String status;

	@Column(name = "CREATE_TIME")
	public Date createTime;
	
	@Transient
	public String fileUrl;
	
	
	public static Map queryHomeworkFile(Long homeworkId,int pageIndex, int pageSize){
		String sql="select * from xjl_dw_homework_file where status='0AA' and HOMEWORK_ID='"+homeworkId+"'";
		Map<String, String> condition = new HashMap<>();
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwHomeworkFile> data = ModelUtils.queryData(pageIndex, pageSize, ret,XjlDwHomeworkFile.class);
		XjlDwFile file = null;
		for (XjlDwHomeworkFile xjlDwHomeworkFile : data) {
			file = XjlDwFile.queryXjlDwFileById(xjlDwHomeworkFile.fileId);
			if (file != null){
				xjlDwHomeworkFile.fileUrl = file.fileUrl;
			}
		}
		return ModelUtils.createResultMap(ret, data);
	}
	public static int delHomeworkFileModelByhomeworkId(Long homeworkId){
		String sql = "update xjl_dw_homework_file set status='0XX' where HOMEWORK_ID='"+homeworkId+"'";
		Map<String, String> condition = new HashMap<String, String>();
		return ModelUtils.executeDelete(condition, sql);
	}
}
