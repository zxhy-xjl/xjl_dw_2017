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

import play.Logger;
import play.db.jpa.GenericModel;
import utils.StringUtil;
import utils.jpa.SQLResult;

@Entity
@Table(name = "xjl_dw_group_gather")
public class XjlDwGroupGather extends GenericModel{
	
	@Id
	@Column(name = "gather_id")
	public Long gatherId;
	@Column(name = "GROUP_BUY_ID")
	public Long groupBuyId;
	@Column(name = "student_id")
	public Long studentId;
	@Column(name = "gather_student_name")
	public String gatherStudentName;
	@Column(name = "gather_student_pingyin")
	public String gatherStudentPingyin;
	@Column(name = "param_1")
	public String param_1;
	@Column(name = "param_2")
	public String param_2;
	@Column(name = "param_3")
	public String param_3;
	@Column(name = "param_4")
	public String param_4;
	@Column(name = "param_5")
	public String param_5;
	@Column(name = "param_6")
	public String param_6;
	@Column(name = "param_7")
	public String param_7;
	@Column(name = "param_8")
	public String param_8;
	@Column(name = "param_9")
	public String param_9;
	@Column(name = "param_10")
	public String param_10;
	@Column(name = "param_11")
	public String param_11;
	@Column(name = "param_12")
	public String param_12;
	@Column(name = "param_13")
	public String param_13;
	@Column(name = "param_14")
	public String param_14;
	@Column(name = "param_15")
	public String param_15;
	@Column(name = "param_16")
	public String param_16;
	@Column(name = "param_17")
	public String param_17;
	@Column(name = "param_18")
	public String param_18;
	@Column(name = "param_19")
	public String param_19;
	@Column(name = "param_20")
	public String param_20;
	@Column(name = "gather_quantity")
	public String gatherQuantity;
	@Column(name = "gather_price")
	public String gatherPrice;
	@Column(name = "gather_pay")
	public String gatherPay;
	@Column(name = "WX_OPEN_ID")
	public String wxOpenId;
	@Column(name = "STATUS")
	public String status;
	@Column(name = "CREATE_TIME")
	public Date createTime;
	@Column(name = "group_title")
	public String groupbuyTitle;
	@Column(name = "sing_Buy")
	public String singBuy;
	@Column(name = "sing_Buy_num")
	public String singBuyNum;
	
	@Transient
	public String _param_1="false";
	@Transient
	public String _param_2="false";
	@Transient
	public String _param_3="false";
	@Transient
	public String _param_4="false";
	@Transient
	public String _param_5="false";
	@Transient
	public String _param_6="false";
	@Transient
	public String _param_7="false";
	@Transient
	public String _param_8="false";
	@Transient
	public String _param_9="false";
	@Transient
	public String _param_10="false";
	@Transient
	public String _param_11="false";
	@Transient
	public String _param_12="false";
	@Transient
	public String _param_13="false";
	@Transient
	public String _param_14="false";
	@Transient
	public String _param_15="false";
	@Transient
	public String _param_16="false";
	@Transient
	public String _param_17="false";
	@Transient
	public String _param_18="false";
	@Transient
	public String _param_19="false";
	@Transient
	public String _param_20="false";
	
	@Transient
	public int param_1_buy_num = 0;
	@Transient
	public int param_2_buy_num = 0;
	@Transient
	public int param_3_buy_num = 0;
	@Transient
	public int param_4_buy_num = 0;
	@Transient
	public int param_5_buy_num = 0;
	@Transient
	public int param_6_buy_num = 0;
	@Transient
	public int param_7_buy_num = 0;
	@Transient
	public int param_8_buy_num = 0;
	@Transient
	public int param_9_buy_num = 0;
	@Transient
	public int param_10_buy_num = 0;
	@Transient
	public int param_11_buy_num = 0;
	@Transient
	public int param_12_buy_num = 0;
	@Transient
	public int param_13_buy_num = 0;
	@Transient
	public int param_14_buy_num = 0;
	@Transient
	public int param_15_buy_num = 0;
	@Transient
	public int param_16_buy_num = 0;
	@Transient
	public int param_17_buy_num = 0;
	@Transient
	public int param_18_buy_num = 0;
	@Transient
	public int param_19_buy_num = 0;
	@Transient
	public int param_20_buy_num = 0;
	@Transient
	public String[] singBuyArr;
	@Transient
	public int allPrice;
	@Transient
	public int allQuantity;
	@Transient
	public int allBuypeopleCount;

	public static Map queryXjlDwGroupGatherListByPage(Map<String, String> condition,int pageIndex, int pageSize){
		String sql = "select * from xjl_dw_group_gather a  where a.status='0AA' [ and a.GROUP_BUY_ID =l:groupBuyId]  ";
		sql+=" order by gather_student_pingyin asc";
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwGroupGather> data = ModelUtils.queryData(pageIndex,pageSize,ret, XjlDwGroupGather.class);
		return ModelUtils.createResultMap(data);
	}
	
	public static XjlDwGroupGather queryXjlDwGroupGather(Long groupBuyId,Long studentid){
		String sql = "select * from xjl_dw_group_gather a  where a.status='0AA' and  a.GROUP_BUY_ID ='"+groupBuyId+"'   and a.student_id ='"+studentid+"'";
		sql+=" order by gather_student_pingyin asc";
		Map<String, String> condition = new HashMap<>();
		SQLResult ret = ModelUtils.createSQLResult(condition, sql);
		List<XjlDwGroupGather> data = ModelUtils.queryData(ret, XjlDwGroupGather.class);
		if(null !=data){
			return data.get(0);
		}else{
			return null;
		}
		
	}
	
	public static int modifyXjlDwGroupGather(int quantity,double gatherPrice,Long studentId,Long groupBuyId,String singBuy,String singBuyNum){
		String sql="update xjl_dw_group_gather set gather_quantity='"+quantity+"',gather_price='"+gatherPrice+"',sing_Buy='"+singBuy+"',sing_buy_num='"+singBuyNum+"' where student_id='"+studentId+"' and GROUP_BUY_ID='"+groupBuyId+"'";
		Map<String, String> condition = new HashMap<String, String>();
		return ModelUtils.executeDelete(condition, sql);
	}
	
	public static int modifyXjlDwGroupGatherPrice(int gatherPrice,Long studentId,Long groupBuyId){
		String sql="update xjl_dw_group_gather set  gather_price='"+gatherPrice+"' where student_id='"+studentId+"' and GROUP_BUY_ID='"+groupBuyId+"'";
		Map<String, String> condition = new HashMap<String, String>();
		return ModelUtils.executeDelete(condition, sql);
	}
	
}
