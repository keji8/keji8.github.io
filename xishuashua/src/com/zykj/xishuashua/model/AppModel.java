package com.zykj.xishuashua.model;

import android.content.Context;

import com.zykj.xishuashua.utils.SharedPreferenceUtils;

/**
 * @author Administrator
 * 登录用户信息
 */
public class AppModel {
	
    private String username;//登录账号
    private String password;//登录密码
    private String userid;//用户Id
    private String avatar;//头像
    private String mobile;//手机
    private String money;//红包金额
    private String number;//红包个数
    private String latitude;//经度
    private String longitude;//纬度

    private static SharedPreferenceUtils utils;
    
    public static AppModel init(Context context){
        AppModel model =new AppModel();
        utils = SharedPreferenceUtils.init(context);

        if(utils.getUsername()!=null){
            model.username = utils.getUsername();
        }

        if(utils.getPassword() != null){
            model.password= utils.getPassword();
        }

        if(utils.getUserid() != null){
            model.userid= utils.getUserid();
        }

        if(utils.getAvatar() != null){
            model.avatar= utils.getAvatar();
        }

        if(utils.getMobile() != null){
            model.mobile= utils.getMobile();
        }

        if(utils.getMoney() != null){
            model.money= utils.getMoney();
        }

        if(utils.getNumber() != null){
            model.number= utils.getNumber();
        }

        if(utils.getLatitude() != null){
            model.latitude= utils.getLatitude();
        }

        if(utils.getLongitude() != null){
            model.longitude= utils.getLongitude();
        }

        return model;
    }
    
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
        utils.setUsername(username);
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
        utils.setPassword(password);
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
        utils.setUserid(userid);
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
        utils.setAvatar(avatar);
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
        utils.setMobile(mobile);
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
        utils.setMoney(money);
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
        utils.setNumber(number);
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public void clear(){
		this.setUsername("");
		this.setPassword("");
		this.setUserid("");
		this.setAvatar("");
		this.setMobile("");
		this.setMoney("");
		this.setNumber("");
	}
}
