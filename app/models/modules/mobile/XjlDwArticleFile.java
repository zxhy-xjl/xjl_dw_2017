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
@Table(name = "xjl_dw_article_file")
public class XjlDwArticleFile extends GenericModel  {

	@Id
	@Column(name = "ARTICLE_FILE_ID")
	public Long articleFileId;
	@Column(name = "ARTICLE_ID")
	public Long ariticleId;
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
	
	
	public static Map queryArticleFile(Long ariticleId,int pageIndex, int pageSize){
		String sql="select * from xjl_dw_article_file where status='0AA' and ARTICLE_ID='"+ariticleId+"'";
		Map<String, String> condition = new HashMap<>();
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwArticleFile> data = ModelUtils.queryData(pageIndex, pageSize, ret,XjlDwArticleFile.class);
		XjlDwFile file = null;
		for (XjlDwArticleFile xjlDwArticleFile : data) {
			file = XjlDwFile.queryXjlDwFileById(xjlDwArticleFile.fileId);
			if (file != null){
				xjlDwArticleFile.fileUrl = file.fileUrl;
			}
		}
		return ModelUtils.createResultMap(ret, data);
	}
	
	public static int delArticleFIleModelByAriticelId(Long ariticelId){
		String sql = "update xjl_dw_article_file set status='0XX' where ARTICLE_ID='"+ariticelId+"'";
		Map<String, String> condition = new HashMap<String, String>();
		return ModelUtils.executeDelete(condition, sql);
	}
	
}
