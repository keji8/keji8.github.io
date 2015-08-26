package com.zykj.xishuashua.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.xishuashua.BaseActivity;
import com.zykj.xishuashua.BaseApp;
import com.zykj.xishuashua.R;
import com.zykj.xishuashua.http.EntityHandler;
import com.zykj.xishuashua.http.HttpUtils;
import com.zykj.xishuashua.http.UrlContants;
import com.zykj.xishuashua.model.MyEnvelope;
import com.zykj.xishuashua.utils.CommonUtils;
import com.zykj.xishuashua.utils.StringUtil;
import com.zykj.xishuashua.utils.Tools;
import com.zykj.xishuashua.view.MyCommonTitle;
import com.zykj.xishuashua.view.MyRequestDailog;
import com.zykj.xishuashua.view.RoundImageView;

public class UserActivity extends BaseActivity {

	private MyCommonTitle myCommonTitle;
	private RelativeLayout rl_me_top;
	private RoundImageView rv_me_avatar;
	private ArrayList<MyEnvelope> envelist;
	private TextView tv_me_mobile,user_login,gift_money,gift_num;
	private LinearLayout user_money,user_record,user_store,user_setting,user_info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_user_activity);
		
		initView();
	}
	
	/**
	 * 加载页面
	 */
	private void initView(){
		myCommonTitle = (MyCommonTitle)findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("个人中心");
		myCommonTitle.setBackBtnVisible(false);
		
		rl_me_top = (RelativeLayout)findViewById(R.id.rl_me_top);//用户信息
		rv_me_avatar = (RoundImageView)findViewById(R.id.rv_me_avatar);//用户头像
		tv_me_mobile = (TextView)findViewById(R.id.tv_me_mobile);//用户手机
		user_login = (TextView)findViewById(R.id.user_login);//用户登录
		
		gift_money = (TextView)findViewById(R.id.gift_money);//红包金额
		gift_num = (TextView)findViewById(R.id.gift_num);//红包个数
		
		user_money = (LinearLayout)findViewById(R.id.user_money);//我的红包
		user_record = (LinearLayout)findViewById(R.id.user_record);//收益记录
		user_store = (LinearLayout)findViewById(R.id.user_store);//我的收藏
		user_setting = (LinearLayout)findViewById(R.id.user_setting);//设置
		user_info = (LinearLayout)findViewById(R.id.user_info);//消息提示
		
		LayoutParams pageParms = rl_me_top.getLayoutParams();
		pageParms.width = Tools.M_SCREEN_WIDTH;
		pageParms.height = Tools.M_SCREEN_WIDTH / 4;

		LayoutParams imgParms = rv_me_avatar.getLayoutParams();
		imgParms.width = Tools.M_SCREEN_WIDTH * 2 / 11;
		imgParms.height = Tools.M_SCREEN_WIDTH * 2 / 11;
		
		setListener(user_login, user_money, user_record, user_store, user_setting, user_info);
	}
	
	/**
	 * 请求用户信息
	 */
	private void requestData(){
		if(CommonUtils.CheckLogin()){
			tv_me_mobile.setVisibility(View.VISIBLE);
			user_login.setVisibility(View.GONE);
			rl_me_top.setOnClickListener(this);
			String avatar = BaseApp.getModel().getAvatar();
			tv_me_mobile.setText(StringUtil.isEmpty(BaseApp.getModel().getMobile())?BaseApp.getModel().getUsername():BaseApp.getModel().getMobile());//默认账户
			ImageLoader.getInstance().displayImage(UrlContants.ABATARURL+avatar, rv_me_avatar);//用户头像
			getmemberenvelopes();
		}else{
			tv_me_mobile.setVisibility(View.GONE);
			user_login.setVisibility(View.VISIBLE);
			rl_me_top.setOnClickListener(null);
			
			gift_money.setText("0");//用户余额
			gift_num.setText("0");//用户资产
			rv_me_avatar.setImageResource(R.drawable.user_head_img);;//用户头像
		}
	}
	
	private void getmemberenvelopes(){
		MyRequestDailog.showDialog(this, "");
		HttpUtils.getmemberenvelopes(new EntityHandler<MyEnvelope>(MyEnvelope.class){
			@Override
			public void onReadSuccess(List<MyEnvelope> list) {
				envelist = (ArrayList<MyEnvelope>)list;
				MyRequestDailog.closeDialog();
				float envelope = 0f;
				for (MyEnvelope myEnvelope : list) {
					envelope += Float.valueOf(myEnvelope.getMembersawgoods_gotpoint());
					gift_money.setText(String.valueOf(envelope));//用户余额
					gift_num.setText(list.size()+"");//用户资产
					BaseApp.getModel().setMoney(String.valueOf(envelope));//红包金额
					BaseApp.getModel().setNumber(list.size()+"");//红包个数
				}
			}
			@Override
			public void onRecevieFailed(String status, JSONObject json) {
				Tools.toast(UserActivity.this, "请求失败");
				super.onRecevieFailed(status, json);
			}
		});
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		requestData();
	}

//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		switch (requestCode) {
//		case 11:
//			/*成功登陆之后*/
//			if (data != null) {requestData();}
//			break;
//		default:
//			break;
//		}
//		super.onActivityResult(requestCode, resultCode, data);
//	}

	@Override
	public void onClick(View view) {
		if(view.getId() != R.id.user_login && view.getId() != R.id.user_setting){
			if(!CommonUtils.CheckLogin()){ Tools.toast(this, "请先登录"); return; }
		}
		switch(view.getId()){
		case R.id.rl_me_top:
			/*用户详情信息*/
			startActivity(new Intent(UserActivity.this,UserInfoActivity.class));
			//startActivityForResult(new Intent(UserActivity.this,UserInfoActivity.class),11);
			break;
		case R.id.user_login:
			/*点击登录*/
			startActivity(new Intent(UserActivity.this,LoginActivity.class));
			//startActivityForResult(new Intent(UserActivity.this,LoginActivity.class), 11);
			break;
		case R.id.user_gift_left:
			/*点击查看红包金额*/
			break;
		case R.id.user_gift_right:
			/*点击查看红包数量*/
			break;
		case R.id.user_money:
			/*点击我的钱包*/
			startActivity(new Intent(UserActivity.this,UserWalletActivity.class));
			break;
		case R.id.user_record:
			/*点击收益记录*/
			startActivity(new Intent(UserActivity.this,UserRecordActivity.class).putParcelableArrayListExtra("envelist", envelist));
			break;
		case R.id.user_store:
			/*点击我的收藏*/
			startActivity(new Intent(UserActivity.this, UserStoreActivity.class));
			break;
		case R.id.user_setting:
			/*点击设置*/
			startActivity(new Intent(UserActivity.this, UserSettingActivity.class));
			break;
		case R.id.user_info:
			/*点击消息提示*/
			startActivity(new Intent(UserActivity.this, UserMessageActivity.class));
			break;
		}
	}
	
	//退出操作
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		CommonUtils.exitkey(keyCode, this);
		return super.onKeyDown(keyCode, event);
	}
}
