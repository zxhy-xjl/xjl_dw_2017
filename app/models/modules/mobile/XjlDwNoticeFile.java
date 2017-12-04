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
@Table(name = "xjl_dw_notice_file")
public class XjlDwNoticeFile extends GenericModel {

	@Id
	@Column(name = "NOTICE_FILE_ID")
	public Long noticeFileId;
	
	@Column(name = "NOTICE_ID")
	public Long noticeId;
	
	@Column(name = "FILE_ID")
	public Long fileId;
	
	@Column(name = "WX_OPEN_ID")
	public String wxOpenid;
	
	@Column(name = "STATUS")
	public String status;

	@Column(name = "CREATE_TIME")
	public Date createTime;
	//文件对一个的url地址
	@Transient
	public String fileUrl;
	
	public static Map queryNoticeFile(Long noticeId,int pageIndex, int pageSize){
		String sql="select * from xjl_dw_notice_file where status='0AA' and notice_id='"+noticeId+"'";
		Map<String, String> condition = new HashMap<>();
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwNoticeFile> data = ModelUtils.queryData(pageIndex, pageSize, ret,XjlDwNoticeFile.class);
		XjlDwFile file = null;
		for (XjlDwNoticeFile xjlDwNoticeFile : data) {
			file = XjlDwFile.queryXjlDwFileById(xjlDwNoticeFile.fileId);
			if (file != null){
				xjlDwNoticeFile.fileUrl = file.fileUrl;
			}
		}
		return ModelUtils.createResultMap(ret, data);
	}
	public static int delNoticeFileByNoticeId(Long noticeId){
		String sql = "update xjl_dw_notice_file set status='0XX' where NOTICE_ID='"+noticeId+"'";
		Map<String, String> condition = new HashMap<String, String>();
		return ModelUtils.executeDelete(condition, sql);
	}
}
