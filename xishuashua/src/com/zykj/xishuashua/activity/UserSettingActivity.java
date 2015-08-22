package com.zykj.xishuashua.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.zykj.xishuashua.BaseActivity;
import com.zykj.xishuashua.R;
import com.zykj.xishuashua.view.MyCommonTitle;

public class UserSettingActivity extends BaseActivity{
	
	private MyCommonTitle myCommonTitle;
	private LinearLayout company_info,app_introduction,apply_league,version_update;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_user_setting);
		
		initView();
	}

	private void initView() {
		myCommonTitle = (MyCommonTitle)findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("设置");

		company_info = (LinearLayout)findViewById(R.id.company_info);//公司简介
		app_introduction = (LinearLayout)findViewById(R.id.app_introduction);//应用说明
		apply_league = (LinearLayout)findViewById(R.id.apply_league);//申请加盟
		version_update = (LinearLayout)findViewById(R.id.version_update);//版本更新
		
		setListener(company_info, app_introduction, apply_league, version_update);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.company_info:
			/*公司简介*/
			startActivity(new Intent(UserSettingActivity.this, UserLeagueActivity.class).putExtra("type", 1));
			break;
		case R.id.app_introduction:
			/*应用说明*/
			startActivity(new Intent(UserSettingActivity.this, UserLeagueActivity.class).putExtra("type", 2));
			break;
		case R.id.apply_league:
			/*申请加盟*/
			startActivity(new Intent(UserSettingActivity.this, UserLeagueActivity.class).putExtra("type", 3));
			break;
		case R.id.version_update:
			/*版本更新*/
			startActivity(new Intent(UserSettingActivity.this, UserLeagueActivity.class).putExtra("type", 4));
			break;
		default:
			break;
		}
	}
}
