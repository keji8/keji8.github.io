package com.zykj.xishuashua.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zykj.xishuashua.R;

public class MyCommonTitle extends RelativeLayout {
	
	private ImageView titleBack;
	private TextView titleEdit;
	private ImageView sharedBtn;

	public MyCommonTitle(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.ui_mycommontitle, this);
		titleBack = (ImageView) findViewById(R.id.aci_back_btn);//后退
		sharedBtn = (ImageView) findViewById(R.id.aci_shared_btn);//分享
		titleEdit = (TextView) findViewById(R.id.aci_edit_btn);//编辑
		titleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((Activity) getContext()).finish();
			}
		});
	}

	public void setLisener(OnClickListener editListener, OnClickListener sharedListener) {
		if(editListener != null) {
			titleEdit.setVisibility(View.VISIBLE);
			titleEdit.setOnClickListener(editListener);
		}
		if(sharedListener != null) {
			sharedBtn.setVisibility(View.VISIBLE);
			sharedBtn.setOnClickListener(sharedListener);
		}
	}

	public void setBackLisener(OnClickListener backListener) {
		if(backListener != null) {
			titleBack.setOnClickListener(backListener);
		}else{
			titleBack.setOnClickListener(null);
		}
	}
	
	public void setTitle(String title) {
		TextView titleText = (TextView) findViewById(R.id.aci_title_tv);
		titleText.setText(title);
	}
	
	public void setEditTitle(String title) {
		titleEdit.setText(title);
	}

	public void setSubTitle(String subtitle) {
		TextView subtitleText = (TextView) findViewById(R.id.aci_subtitle_tv);
		subtitleText.setVisibility(View.VISIBLE);
		subtitleText.setText(subtitle);
	}

	public void setBackBtnVisible(boolean flag) {
		if(flag) {
			titleBack.setVisibility(View.VISIBLE);
		} else {
			titleBack.setVisibility(View.INVISIBLE);
		}
	}
}
