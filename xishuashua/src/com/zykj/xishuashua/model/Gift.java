package com.zykj.xishuashua.model;

import java.io.Serializable;

public class Gift implements Serializable{
	
	/**
	 * 红包
	 */
	private static final long serialVersionUID = 1L;
	
	private String goods_id;//红包Id
	private String goods_name;//红包名称
	private String goods_jingle;//红包简介
	private String goods_price;//红包金额
	private String goods_image;//红包缩略图
	private String goods_longi;//经度
	private String goods_lati;//纬度
	private String goods_location;//红包地址
	private String goods_favoratenum;//点赞数
	private String goods_collectnum;//收藏数
	private String goods_commentnum;//评论数
	private String goods_sharenum;//分享数
	private String goods_storage_alarm;//最小停留秒数
	private String goods_selltime;//发布时间
	private String goods_marketprice;//即时红(持续时间)，为0是永久红包，

	private String giftxingzhi;//红包性质
	private String giftLabel;//红包标签
	private String currentSeconds;//当前剩余秒数
	private String store_name;//为"news"就是新闻资讯
	private String grade_id;//0个人、商家、app
	private String goods_serial;//兴趣标签
	
	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public String getGoods_jingle() {
		return goods_jingle;
	}
	public void setGoods_jingle(String goods_jingle) {
		this.goods_jingle = goods_jingle;
	}
	public String getGoods_price() {
		return goods_price;
	}
	public void setGoods_price(String goods_price) {
		this.goods_price = goods_price;
	}
	public String getGoods_image() {
		return goods_image;
	}
	public void setGoods_image(String goods_image) {
		this.goods_image = goods_image;
	}
	public String getGoods_longi() {
		return goods_longi;
	}
	public void setGoods_longi(String goods_longi) {
		this.goods_longi = goods_longi;
	}
	public String getGoods_lati() {
		return goods_lati;
	}
	public void setGoods_lati(String goods_lati) {
		this.goods_lati = goods_lati;
	}
	public String getGoods_location() {
		return goods_location;
	}
	public void setGoods_location(String goods_location) {
		this.goods_location = goods_location;
	}
	public String getGoods_favoratenum() {
		return goods_favoratenum;
	}
	public void setGoods_favoratenum(String goods_favoratenum) {
		this.goods_favoratenum = goods_favoratenum;
	}
	public String getGoods_collectnum() {
		return goods_collectnum;
	}
	public void setGoods_collectnum(String goods_collectnum) {
		this.goods_collectnum = goods_collectnum;
	}
	public String getGoods_commentnum() {
		return goods_commentnum;
	}
	public void setGoods_commentnum(String goods_commentnum) {
		this.goods_commentnum = goods_commentnum;
	}
	public String getGoods_sharenum() {
		return goods_sharenum;
	}
	public void setGoods_sharenum(String goods_sharenum) {
		this.goods_sharenum = goods_sharenum;
	}
	public String getGoods_storage_alarm() {
		return goods_storage_alarm;
	}
	public void setGoods_storage_alarm(String goods_storage_alarm) {
		this.goods_storage_alarm = goods_storage_alarm;
	}
	public String getGoods_selltime() {
		return goods_selltime;
	}
	public void setGoods_selltime(String goods_selltime) {
		this.goods_selltime = goods_selltime;
	}
	public String getGoods_marketprice() {
		return goods_marketprice;
	}
	public void setGoods_marketprice(String goods_marketprice) {
		this.goods_marketprice = goods_marketprice;
	}
	public String getGiftxingzhi() {
		return giftxingzhi;
	}
	public void setGiftxingzhi(String giftxingzhi) {
		this.giftxingzhi = giftxingzhi;
	}
	public String getGiftLabel() {
		return giftLabel;
	}
	public void setGiftLabel(String giftLabel) {
		this.giftLabel = giftLabel;
	}
	public String getCurrentSeconds() {
		return currentSeconds;
	}
	public void setCurrentSeconds(String currentSeconds) {
		this.currentSeconds = currentSeconds;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public String getGrade_id() {
		return grade_id;
	}
	public void setGrade_id(String grade_id) {
		this.grade_id = grade_id;
	}
	public String getGoods_serial() {
		return goods_serial;
	}
	public void setGoods_serial(String goods_serial) {
		this.goods_serial = goods_serial;
	}
}
