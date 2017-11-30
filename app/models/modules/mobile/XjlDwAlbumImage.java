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

import play.Logger;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.db.jpa.Model;
import utils.jpa.ParamObject;
import utils.jpa.SQLResult;
import utils.jpa.sql.SQLParser;

@Entity
@Table(name = "xjl_dw_album_image")
public class XjlDwAlbumImage extends GenericModel{

	@Id
	@Column(name = "ALBUM_IMAGE_ID")
	public Long albumImageId;

	@Column(name = "FILE_ID")
	public Long fileId;

	@Column(name = "ALBUM_ID")
	public Long albumId;

	@Column(name = "IMAGE_TITLE")
	public String imageTitle;

	@Column(name = "IMAGE_ORDER")
	public Long imageOrder;

	@Column(name = "WX_OPEN_ID")
	public String wxOpenId;

	@Column(name = "STATUS")
	public String status;

	@Column(name = "CREATE_TIME")
	public Date createTime;
	//文件对一个的url地址
	@Transient
	public String fileUrl;
	
	public static Map queryXjlDwAlbumImageListByPage(Map<String, String> condition,
		int pageIndex, int pageSize) {
			String sql = "select * ";
			sql += "from xjl_dw_album_image a where status='0AA' ";
			if(null !=condition&&null!=condition.get("albumId")){
				sql +=" and ALBUM_ID = "+condition.get("albumId");
			}
			sql+=" order by IMAGE_ORDER";
			SQLResult ret = ModelUtils.createSQLResult(condition, sql);
			Logger.info("retsql:"+ret.getSql());
			List<XjlDwAlbumImage> data = ModelUtils.queryData(pageIndex, pageSize, ret,XjlDwAlbumImage.class);
			Logger.info("datasize:"+data);
			for (XjlDwAlbumImage xjlDwAlbumImage : data) {
				XjlDwFile file = XjlDwFile.queryXjlDwFileById(xjlDwAlbumImage.fileId);
				if (file != null){
					xjlDwAlbumImage.fileUrl = file.fileUrl;
				}
			}
			return ModelUtils.createResultMap(ret, data);
	}
	
	public static Map queryXjlDwAlbumImageByAlbumId(Long albumId,int pageIndex, int pageSize){
		String sql = "select * from xjl_dw_album_image  where status='0AA'  and ALBUM_ID = '"+albumId+"'";
		Map<String, String> condition = new HashMap<String, String>();
		condition.put("albumId", String.valueOf(albumId));
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwAlbumImage> data = ModelUtils.queryData(pageIndex, pageSize,ret,XjlDwGroupBuyOrder.class);
		return ModelUtils.createResultMap(ret, data);
	}
	

	public static int delAlbumImageByAlbumId(Long albumId){
		String sql = "update xjl_dw_album_image set status='0XX' where ALBUM_ID='"+albumId+"'";
		Map<String, String> condition = new HashMap<String, String>();
		return ModelUtils.executeDelete(condition, sql);
	}

	public static int delAlbumImageByAlbumImageId(Long albumImageId){
		String sql = "update xjl_dw_album_image set status='0XX' where ALBUM_IMAGE_ID='"+albumImageId+"'";
		Map<String, String> condition = new HashMap<String, String>();
		return ModelUtils.executeDelete(condition, sql);
	}
}
