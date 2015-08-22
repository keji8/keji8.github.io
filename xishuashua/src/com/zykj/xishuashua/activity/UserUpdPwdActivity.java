package com.zykj.xishuashua.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.zykj.xishuashua.BaseActivity;
import com.zykj.xishuashua.BaseApp;
import com.zykj.xishuashua.R;
import com.zykj.xishuashua.http.HttpErrorHandler;
import com.zykj.xishuashua.http.HttpUtils;
import com.zykj.xishuashua.utils.StringUtil;
import com.zykj.xishuashua.utils.TextUtil;
import com.zykj.xishuashua.utils.Tools;
import com.zykj.xishuashua.view.MyCommonTitle;

public class UserUpdPwdActivity extends BaseActivity{
	
	private MyCommonTitle myCommonTitle;
	private EditText user_pwd,user_cfmpwd;
	private String username,type;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_user_updpwd);
		username = getIntent().getStringExtra("username");
		type = getIntent().getStringExtra("type");
		
		initView();
	}

	private void initView() {
		myCommonTitle = (MyCommonTitle)findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("forget".equals(type)?"修改密码":"添加密码");
		
		user_pwd = (EditText)findViewById(R.id.user_pwd);
		user_cfmpwd = (EditText)findViewById(R.id.user_cfmpwd);

		Button app_register_in = (Button)findViewById(R.id.app_register_in);//提交密码
		app_register_in.setText("forget".equals(type)?"修改密码":"下一步");
		setListener(app_register_in);
	}

	@Override
	public void onClick(View v) {
		final String pwd = user_pwd.getText().toString().trim();
		String pwd2 = user_cfmpwd.getText().toString().trim();
        if(StringUtil.isEmpty(pwd) || StringUtil.isEmpty(pwd2)){
            Tools.toast(this, "密码不能为空");return;
        }
		if (!pwd.equals(pwd2)) {
			Tools.toast(this, "两次密码必须相同");return;
		}
		if (!TextUtil.isPasswordLengthLegal(pwd)){
			Tools.toast(this, "密码长度合法性校验6-20位任意字符");return;
		}
		RequestParams params = new RequestParams();
		if ("forget".equals(type)) {
			params.put("member_phone", username);
			params.put("password", pwd);
			HttpUtils.forgetPwd(new HttpErrorHandler() {
				@Override
				public void onRecevieSuccess(JSONObject json) {
					Tools.toast(UserUpdPwdActivity.this, "修改成功");
					BaseApp.getModel().clear();
					startActivity(new Intent(UserUpdPwdActivity.this, LoginActivity.class));
				}
				@Override
				public void onRecevieFailed(String status, JSONObject json) {
					Tools.toast(UserUpdPwdActivity.this, json.getString("message"));
				}
			}, params);
			
		} else {
			params.put("member_name", username);
			params.put("member_passwd", pwd);
			params.put("member_email", "");
			params.put("member_phone", username);
			HttpUtils.register(new HttpErrorHandler() {
				@Override
				public void onRecevieSuccess(JSONObject json) {
					Tools.toast(UserUpdPwdActivity.this, json.getString("result_text"));
					startActivity(new Intent(UserUpdPwdActivity.this, UserFirstLableActivity.class)
							.putExtra("username", username).putExtra("password", pwd));
				}
				@Override
				public void onRecevieFailed(String status, JSONObject json) {
					Tools.toast(UserUpdPwdActivity.this, json.getString("message"));
				}
			}, params);
		}
	}
}
