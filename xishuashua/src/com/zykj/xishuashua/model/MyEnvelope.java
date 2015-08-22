package com.zykj.xishuashua.model;

import java.io.Serializable;

public class MyEnvelope implements Serializable{
	/**
	 * 我的红包
	 */
	private static final long serialVersionUID = 1L;
	private String membersawgoods_id;
	private String member_id;
	private String member_name;
	private String goods_id;
	private String membersawgoods_lastsawtime;
	private String membersawgoods_sawtimes;
	private String membersawgoods_gotpoint;
	public String getMembersawgoods_id() {
		return membersawgoods_id;
	}
	public void setMembersawgoods_id(String membersawgoods_id) {
		this.membersawgoods_id = membersawgoods_id;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getMember_name() {
		return member_name;
	}
	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}
	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}
	public String getMembersawgoods_lastsawtime() {
		return membersawgoods_lastsawtime;
	}
	public void setMembersawgoods_lastsawtime(String membersawgoods_lastsawtime) {
		this.membersawgoods_lastsawtime = membersawgoods_lastsawtime;
	}
	public String getMembersawgoods_sawtimes() {
		return membersawgoods_sawtimes;
	}
	public void setMembersawgoods_sawtimes(String membersawgoods_sawtimes) {
		this.membersawgoods_sawtimes = membersawgoods_sawtimes;
	}
	public String getMembersawgoods_gotpoint() {
		return membersawgoods_gotpoint;
	}
	public void setMembersawgoods_gotpoint(String membersawgoods_gotpoint) {
		this.membersawgoods_gotpoint = membersawgoods_gotpoint;
	}
	
}
