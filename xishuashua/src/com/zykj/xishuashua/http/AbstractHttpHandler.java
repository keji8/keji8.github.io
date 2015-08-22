package com.zykj.xishuashua.http;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.protocol.HTTP;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zykj.xishuashua.view.MyRequestDailog;

/**
 * Created by ss on 15-4-14.
 */
public abstract class AbstractHttpHandler extends AsyncHttpResponseHandler{

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        try {
            String responseString=new String(responseBody, HTTP.UTF_8);
            JSONObject json = (JSONObject) JSON.parse(responseString);
            onJsonSuccess(json);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
        	Log.e("JSON--parse", e.toString());
            onJsonSuccess(JSONObject.parseObject(UrlContants.ERROR));
        } 
    }

	@Override
	public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		MyRequestDailog.closeDialog();
	}
    
	public abstract void onJsonSuccess(JSONObject json);

}
