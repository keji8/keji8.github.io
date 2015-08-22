package com.zykj.xishuashua.model;

import java.io.Serializable;

public class Comment implements Serializable{
	
	/**
	 * 评论
	 */
	private static final long serialVersionUID = 1L;
	private String member_id;
	private String member_name;
	private String goods_id;
	private String comment_content;
	private String comment_favoratenum;
	private String comment_subcommentnum;
	private String comment_supercommentid;
	private String comment_commenttime;
	private String comment_id;
	
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
	public String getComment_content() {
		return comment_content;
	}
	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}
	public String getComment_favoratenum() {
		return comment_favoratenum;
	}
	public void setComment_favoratenum(String comment_favoratenum) {
		this.comment_favoratenum = comment_favoratenum;
	}
	public String getComment_subcommentnum() {
		return comment_subcommentnum;
	}
	public void setComment_subcommentnum(String comment_subcommentnum) {
		this.comment_subcommentnum = comment_subcommentnum;
	}
	public String getComment_supercommentid() {
		return comment_supercommentid;
	}
	public void setComment_supercommentid(String comment_supercommentid) {
		this.comment_supercommentid = comment_supercommentid;
	}
	public String getComment_commenttime() {
		return comment_commenttime;
	}
	public void setComment_commenttime(String comment_commenttime) {
		this.comment_commenttime = comment_commenttime;
	}
	public String getComment_id() {
		return comment_id;
	}
	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}
}
