package com.zykj.xishuashua.model;

import java.io.Serializable;

public class Record implements Serializable{
	/**
	 * 收益记录
	 */
	private static final long serialVersionUID = 1L;
	private String recordid;
	private String createtime;
	private String recordtype;
	private String recordathor;
	private String recordname;
	private String recordaddress;
	private String recordlable;
	
	public Record(String recordtype, String recordathor, String recordname){
		this.recordtype = recordtype;
		this.recordathor = recordathor;
		this.recordname = recordname;
	}
	
	public String getRecordid() {
		return recordid;
	}
	public void setRecordid(String recordid) {
		this.recordid = recordid;
	}
	public String getRecordname() {
		return recordname;
	}
	public void setRecordname(String recordname) {
		this.recordname = recordname;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getRecordtype() {
		return recordtype;
	}
	public void setRecordtype(String recordtype) {
		this.recordtype = recordtype;
	}
	public String getRecordathor() {
		return recordathor;
	}
	public void setRecordathor(String recordathor) {
		this.recordathor = recordathor;
	}
	public String getRecordaddress() {
		return recordaddress;
	}
	public void setRecordaddress(String recordaddress) {
		this.recordaddress = recordaddress;
	}
	public String getRecordlable() {
		return recordlable;
	}
	public void setRecordlable(String recordlable) {
		this.recordlable = recordlable;
	}
	
}
