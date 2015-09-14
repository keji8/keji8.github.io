package com.zykj.xishuashua.activity;

import java.util.List;

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
import com.zykj.xishuashua.R;
import com.zykj.xishuashua.adapter.CommonAdapter;
import com.zykj.xishuashua.adapter.ViewHolder;
import com.zykj.xishuashua.http.EntityHandler;
import com.zykj.xishuashua.http.HttpErrorHandler;
import com.zykj.xishuashua.http.HttpUtils;
import com.zykj.xishuashua.model.Interest;
import com.zykj.xishuashua.utils.Tools;
import com.zykj.xishuashua.view.MyCheckBox;

public class UserLableActivity extends BaseActivity{
	
	private ImageView aci_back_btn;
	private TextView aci_title_tv;
	private GridView label_list;
	private LinearLayout label_bottom;
	private CommonAdapter<Interest> adapter;
	private String[] interestIds;
	private Integer[] colors = new Integer[]{Color.rgb(255, 143, 0),Color.rgb(37,171,231),Color.rgb(233,123,252),
			Color.rgb(14,215,112),Color.rgb(96,83,254),Color.rgb(31,226,240),Color.rgb(246,99,164),Color.rgb(222,34,49),
			Color.rgb(5,72,150),Color.rgb(58,124,26),Color.rgb(238,214,62),Color.rgb(46,137,158),Color.rgb(149,121,109),
			Color.rgb(53,58,62),Color.rgb(130,182,75),Color.rgb(222,108,30),Color.rgb(82,186,46),Color.rgb(0, 255, 0),
			Color.rgb(0, 144, 256),Color.rgb(255, 0, 255),Color.rgb(96, 63, 225),Color.rgb(42, 70, 30),Color.rgb(0, 192, 255)};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_user_label);
		interestIds = getIntent().getStringExtra("interestIds").split(",");
		
		initView();
		requestData();
	}

	private void initView() {
		aci_back_btn = (ImageView)findViewById(R.id.aci_back_btn);
		aci_title_tv = (TextView)findViewById(R.id.aci_title_tv);
		aci_title_tv.setText("兴趣标签");

		label_list = (GridView)findViewById(R.id.label_list);//标签列表
		label_bottom = (LinearLayout)findViewById(R.id.label_bottom);//智能开启
		
		setListener(aci_back_btn, label_bottom);
	}
	
	private void requestData(){
		HttpUtils.getAllInterests(new EntityHandler<Interest>(Interest.class) {
			@Override
			public void onReadSuccess(List<Interest> list) {
				initChecked(list);//初始化标签的状态
				adapter = new CommonAdapter<Interest>(UserLableActivity.this, R.layout.ui_item_label, list) {
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
		}, null);
	}
	
	/**
	 * @param list
	 * 初始化标签的状态
	 */
	private void initChecked(List<Interest> list){
		if(interestIds.length >= 5){
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < interestIds.length; j++) {
					if(list.get(i).getInterest_id().equals(interestIds[j])){
						list.get(i).setChecked(true);
					}
				}
			}
		}else{
			for (int i = 0; i < list.size(); i++) {
				if(i < 5){
					list.get(i).setChecked(true);
				}
			}
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.aci_back_btn:
			finish();
			break;
		case R.id.label_bottom:
			StringBuffer CheIds = new StringBuffer();
			for (int i = 0; i < adapter.mDatas.size(); i++) {
				if(adapter.mDatas.get(i).isChecked()){
					CheIds.append(adapter.mDatas.get(i).getInterest_id()+",");
				}
			}
			String ids = CheIds.length() == 0?"":CheIds.substring(0, CheIds.length()-1);
			RequestParams params = new RequestParams();
			params.put("interestid", ids);
			if(ids.split(",").length<5){
				Tools.toast(UserLableActivity.this, "至少选择5个标签!");
			}else{
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
			Tools.toast(UserLableActivity.this, "开启成功!");
			UserLableActivity.this.finish();
		}
		@Override
		public void onRecevieFailed(String status, JSONObject json) {
			Tools.toast(UserLableActivity.this, "开启失败!");
		}
	};
}
