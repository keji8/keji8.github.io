package com.zykj.xishuashua.http;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by ss on 15-4-14.
 */
public abstract class HttpErrorHandler extends AbstractHttpHandler {
    private static final String TAG="httpResponse";
    @Override
    public void onJsonSuccess(JSONObject json) {
       String status= json.getString("result");
       String msg=  json.getString("result_text");
        if(TextUtils.isEmpty(status) || !status.equals("1")){
            if(!TextUtils.isEmpty(msg)){
            Log.e(TAG,msg);}
            onRecevieFailed(status,json);
        }else{
            onRecevieSuccess(json);
        }
    }

    public abstract void onRecevieSuccess(JSONObject json);

    public void onRecevieFailed(String status,JSONObject json){};
}
