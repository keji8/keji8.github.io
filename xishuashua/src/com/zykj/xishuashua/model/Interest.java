package com.zykj.xishuashua.model;

import java.io.Serializable;

public class Interest implements Serializable{

	/**
	 * 兴趣标签
	 */
	private static final long serialVersionUID = 1L;
	
	private String interest_id;//标签Id
	private String interest_name;//标签名称
	private String interest_content;//标签内容
	private String count;
	
	private boolean isChecked;//是否选中

	public String getInterest_id() {
		return interest_id;
	}

	public void setInterest_id(String interest_id) {
		this.interest_id = interest_id;
	}

	public String getInterest_name() {
		return interest_name;
	}

	public void setInterest_name(String interest_name) {
		this.interest_name = interest_name;
	}

	public String getInterest_content() {
		return interest_content;
	}

	public void setInterest_content(String interest_content) {
		this.interest_content = interest_content;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}
	
}
