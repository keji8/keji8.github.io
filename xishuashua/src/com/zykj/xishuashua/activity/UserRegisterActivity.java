package com.zykj.xishuashua.activity;

import static cn.smssdk.framework.utils.R.getStringRes;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.zykj.xishuashua.BaseActivity;
import com.zykj.xishuashua.R;
import com.zykj.xishuashua.utils.TextUtil;
import com.zykj.xishuashua.utils.Tools;
import com.zykj.xishuashua.view.MyCommonTitle;

public class UserRegisterActivity extends BaseActivity{
	
	private MyCommonTitle myCommonTitle;
	private EditText uu_username,phone_code;
	private String username;
	private String type;

	private static String APPKEY = "93073e1ab3a8";
	private static String APPSECRET = "e1ae8ab56cc63e61be4e4c16159af622";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_user_register);
		type = getIntent().getStringExtra("type");
		//初始化短信验证
		SMSSDK.initSDK(this,APPKEY,APPSECRET);
		EventHandler eh=new EventHandler(){
			@Override
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		};
		SMSSDK.registerEventHandler(eh);
		initView();
	}

	/**
	 * 初次加载页面
	 */
	private void initView() {
		myCommonTitle = (MyCommonTitle)findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("forget".equals(type)?"找回密码":"注册");
		
		uu_username = (EditText)findViewById(R.id.uu_username);//手机号
		phone_code = (EditText)findViewById(R.id.phone_code);//手机号
		Button identifying_code = (Button)findViewById(R.id.identifying_code);//发送验证码
		Button app_register_in = (Button)findViewById(R.id.app_register_in);//注册
		app_register_in.setText("forget".equals(type)?"找回密码":"注册");
		
		setListener(identifying_code, app_register_in);
	}

	@Override
	public void onClick(View view) {
        username=uu_username.getText().toString().trim();
		switch(view.getId()){
		case R.id.identifying_code:
	        if(!TextUtil.isMobile(username)){
	        	Tools.toast(UserRegisterActivity.this, "手机号格式不对");
	        	return;
	        }
			/*发送手机验证码*/
			SMSSDK.getVerificationCode("86",username);
			break;
		case R.id.app_register_in:
			/*注册*/
	        String code=phone_code.getText().toString().trim();
	        if(!TextUtil.isCode(code,4)){
	        	Tools.toast(UserRegisterActivity.this, "填写错误");
	        	return;
	        }
			SMSSDK.submitVerificationCode("86", username, phone_code.getText().toString().trim());
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		SMSSDK.unregisterAllEventHandler();
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			Log.e("result", "result="+result);
			Log.e("event", "event="+event);
			if (result == SMSSDK.RESULT_COMPLETE) {
				//短信注册成功后，返回MainActivity,然后提示新好友
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
					Tools.toast(UserRegisterActivity.this, "提交验证码成功");
					startActivity(new Intent(UserRegisterActivity.this, UserUpdPwdActivity.class)
						.putExtra("username", username).putExtra("type", type));
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
					Tools.toast(UserRegisterActivity.this, "验证码已经发送");
				}
			} else {
				((Throwable) data).printStackTrace();
				int resId = getStringRes(UserRegisterActivity.this, "smssdk_network_error");
				Tools.toast(UserRegisterActivity.this, event == SMSSDK.EVENT_GET_VERIFICATION_CODE?"验证码频繁，请稍后再试！":"验证码错误");
				if (resId > 0) {
					Tools.toast(UserRegisterActivity.this, resId+"");
				}
			}
		}
	};
}
