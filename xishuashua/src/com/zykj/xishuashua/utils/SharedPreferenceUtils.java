package com.zykj.xishuashua.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Administrator
 * 保存用户信息搭配本地
 */
public class SharedPreferenceUtils {

    private static SharedPreferenceUtils mUtil;
    private static final String PREFERENCE_NAME="_ZYKJMJ";
    private static SharedPreferences mSharedPreference;
    private SharedPreferences.Editor mEditor;

    private static final String USERID="userid";
    private static final String USERNAME="username";
    private static final String PASSWORD="password";
    private static final String AVATAR="avatar";
    private static final String MOBILE="mobile";
    private static final String MONEY="money";
    private static final String NUMBER="number";
    private static final String LATITUDE="latitude";
    private static final String LONGITUDE="longitude";
	
    private SharedPreferenceUtils(Context context){
        mSharedPreference=context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        mEditor=mSharedPreference.edit();
    }
    
    public static synchronized SharedPreferenceUtils init(Context context){
        if(mUtil==null){
            mUtil=new SharedPreferenceUtils(context);
        }
        return mUtil;
    }

	public String getUserid() {
		return mSharedPreference.getString(USERID, null);
	}

	public String getUsername() {
		return mSharedPreference.getString(USERNAME, null);
	}

	public String getPassword() {
		return mSharedPreference.getString(PASSWORD, null);
	}

	public String getAvatar() {
		return mSharedPreference.getString(AVATAR, null);
	}

	public String getMobile() {
		return mSharedPreference.getString(MOBILE, null);
	}

	public String getMoney() {
		return mSharedPreference.getString(MONEY, null);
	}

	public String getNumber() {
		return mSharedPreference.getString(NUMBER, null);
	}

	public String getLatitude() {
		return mSharedPreference.getString(LATITUDE, null);
	}

	public String getLongitude() {
		return mSharedPreference.getString(LONGITUDE, null);
	}

    public void setUserid(String userid){
        mEditor.putString(USERID, userid);
        mEditor.commit();
    }
    
    public void setUsername(String username){
        mEditor.putString(USERNAME, username);
        mEditor.commit();
    }

    public void setPassword(String password){
        mEditor.putString(PASSWORD, password);
        mEditor.commit();
    }

    public void setAvatar(String avatar){
        mEditor.putString(AVATAR,avatar);
        mEditor.commit();
    }

    public void setMobile(String mobile){
        mEditor.putString(MOBILE,mobile);
        mEditor.commit();
    }

    public void setMoney(String money){
        mEditor.putString(MONEY,money);
        mEditor.commit();
    }

    public void setNumber(String number){
        mEditor.putString(NUMBER,number);
        mEditor.commit();
    }

    public void setLatitude(String latitude){
        mEditor.putString(LATITUDE,latitude);
        mEditor.commit();
    }

    public void setLongitude(String longitude){
        mEditor.putString(LONGITUDE,longitude);
        mEditor.commit();
    }
    
    public void clear(){
        mEditor.clear();
    }
}
