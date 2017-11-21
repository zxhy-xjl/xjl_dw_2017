package models.modules.mobile;

import javax.persistence.Transient;

public class XjlDWGradeChart {
	
	//考试名称
	@Transient
	public String exam;
	//科目名称
	@Transient
	public String type;
	//分数
	@Transient
	public Double temperature;

}
