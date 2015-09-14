package com.zykj.xishuashua.activity;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zykj.xishuashua.BaseActivity;
import com.zykj.xishuashua.BaseApp;
import com.zykj.xishuashua.R;
import com.zykj.xishuashua.adapter.CommonAdapter;
import com.zykj.xishuashua.adapter.ViewHolder;
import com.zykj.xishuashua.http.EntityHandler;
import com.zykj.xishuashua.http.HttpErrorHandler;
import com.zykj.xishuashua.http.HttpUtils;
import com.zykj.xishuashua.model.Interest;
import com.zykj.xishuashua.utils.Tools;
import com.zykj.xishuashua.view.MyCheckBox;

public class UserFirstLableActivity extends BaseActivity{
	
	private ImageView aci_back_btn;
	private TextView aci_title_tv;
	private GridView label_list;
	private LinearLayout label_bottom;
	private CommonAdapter<Interest> adapter;
	private Integer[] colors = new Integer[]{Color.rgb(255, 143, 0),Color.rgb(37,171,231),Color.rgb(233,123,252),
			Color.rgb(14,215,112),Color.rgb(96,83,254),Color.rgb(31,226,240),Color.rgb(246,99,164),Color.rgb(222,34,49),
			Color.rgb(5,72,150),Color.rgb(58,124,26),Color.rgb(238,214,62),Color.rgb(46,137,158),Color.rgb(149,121,109),
			Color.rgb(53,58,62),Color.rgb(130,182,75),Color.rgb(222,108,30),Color.rgb(82,186,46),Color.rgb(0, 255, 0),
			Color.rgb(0, 144, 256),Color.rgb(255, 0, 255),Color.rgb(96, 63, 225),Color.rgb(42, 70, 30),Color.rgb(0, 192, 255)};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_user_label);
		
		initView();
		requestData();
	}

	private void initView() {
		aci_back_btn = (ImageView)findViewById(R.id.aci_back_btn);
		aci_title_tv = (TextView)findViewById(R.id.aci_title_tv);
		aci_title_tv.setText("兴趣标签");

		label_list = (GridView)findViewById(R.id.label_list);//标签列表
		label_bottom = (LinearLayout)findViewById(R.id.label_bottom);//智能开启
		((TextView)findViewById(R.id.power_on)).setText("完成注册");;
		
		setListener(aci_back_btn, label_bottom);
	}
	
	/**
	 * 请求登录、兴趣标签
	 */
	private void requestData(){
		RequestParams params = new RequestParams();
		params.put("login_name", getIntent().getStringExtra("username"));
		params.put("login_password", getIntent().getStringExtra("password"));
		params.put("lati", BaseApp.getModel().getLatitude());
		params.put("longi", BaseApp.getModel().getLongitude());
        HttpUtils.login(res_login, params);//请求登录
		HttpUtils.getAllInterests(res_getAllInterests, null);//兴趣标签
	}
	
	/**
	 * 处理请求登录
	 */
	private AsyncHttpResponseHandler res_login = new HttpErrorHandler() {
		@Override
		public void onRecevieSuccess(JSONObject json) {
			JSONObject data = json.getJSONObject("data");
			if(data == null){BaseApp.getModel().clear();}
			else{
				BaseApp.getModel().setAvatar(data.getString("member_avatar"));//头像
				BaseApp.getModel().setMobile(data.getString("member_mobile"));//手机号
				BaseApp.getModel().setPassword(BaseApp.getModel().getPassword());//登录密码
				BaseApp.getModel().setUserid(data.getString("member_id"));//用户Id
				BaseApp.getModel().setUsername(data.getString("member_name"));//登录账号
			}
		}
		@Override
		public void onRecevieFailed(String status, JSONObject json) {
			BaseApp.getModel().clear();
			Tools.toast(UserFirstLableActivity.this, "登录失效!");
		}
	};

	
	/**
	 * 处理兴趣标签
	 */
	private AsyncHttpResponseHandler res_getAllInterests = new EntityHandler<Interest>(Interest.class) {
		@Override
		public void onReadSuccess(List<Interest> list) {
			for (int i = 0; i < list.size(); i++) {
				/* 默认选中前5个 */
				if(i < 5){ list.get(i).setChecked(true); }
			}
			adapter = new CommonAdapter<Interest>(UserFirstLableActivity.this, R.layout.ui_item_label, list) {
				@Override
				public void convert(ViewHolder holder, final Interest interest) {
					final MyCheckBox mCheckBox = holder.getView(R.id.label_item);
					if(Tools.M_SCREEN_WIDTH < 800){
						LayoutParams checkboxParms = mCheckBox.getLayoutParams();
						checkboxParms.width = Tools.M_SCREEN_WIDTH * 2 / 7;
						checkboxParms.height = Tools.M_SCREEN_WIDTH * 2 / 7;
					}
					mCheckBox.setChecked(interest.isChecked());
					mCheckBox.setText(interest.getInterest_name());

			        final int position = holder.getPosition() % colors.length;
					mCheckBox.setTextColor(mCheckBox.isChecked()?Color.rgb(255, 255, 255):colors[position]);
					mCheckBox.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							interest.setChecked(mCheckBox.isChecked());
							mCheckBox.setTextColor(mCheckBox.isChecked()?Color.rgb(255, 255, 255):colors[position]);
						}
					});
				}
			};
			label_list.setAdapter(adapter);
		}
	};

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.aci_back_btn:
			/*返回*/
			finish();
			break;
		case R.id.label_bottom:
			/*只能开启*/
			StringBuffer CheIds = new StringBuffer();
			for (int i = 0; i < adapter.mDatas.size(); i++) {
				if(adapter.mDatas.get(i).isChecked()){
					CheIds.append(adapter.mDatas.get(i).getInterest_id()+",");
				}
			}
			String ids = CheIds.length() == 0?"":CheIds.substring(0, CheIds.length()-1);
			if(ids.split(",").length<5){
				Tools.toast(UserFirstLableActivity.this, "至少选择5个标签!");
			}else{
				RequestParams params = new RequestParams();
				params.put("interestid", ids);
				HttpUtils.addinterestsmeta(rel_addinterestsmeta, params);
			}
			break;
		default:
			break;
		}
	}
	
	private AsyncHttpResponseHandler rel_addinterestsmeta = new HttpErrorHandler() {
		@Override
		public void onRecevieSuccess(JSONObject json) {
			startActivity(new Intent(UserFirstLableActivity.this, MainActivity.class));
			MainActivity.switchTabActivity(2);
		}
		@Override
		public void onRecevieFailed(String status, JSONObject json) {
			Tools.toast(UserFirstLableActivity.this, "提交失败!");
		}
	};
}
