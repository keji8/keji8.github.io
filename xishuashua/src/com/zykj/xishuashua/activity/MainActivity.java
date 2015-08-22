package com.zykj.xishuashua.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

import com.umeng.message.PushAgent;
import com.zykj.xishuashua.BaseTabActivity;
import com.zykj.xishuashua.R;
import com.zykj.xishuashua.utils.Tools;
import com.zykj.xishuashua.view.MyRadioButton;

/**
 * @author Administrator
 * csh 2015-08-07
 */
public class MainActivity extends BaseTabActivity{
	
	private static TabHost m_tab;
	private Intent intent_1;
	private Intent intent_2;
	private Intent intent_3;
	// 单选按钮组
	private RadioGroup m_rgroup;
	// 4个单选按钮
	private static MyRadioButton m_radio_index;
	private static MyRadioButton m_radio_restaurant;
	private static MyRadioButton m_radio_setting;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs_layout);
		
		PushAgent.getInstance(this).onAppStart();
		m_tab = getTabHost();
		initView();
	}

	/**
	 * 初始化页面
	 */
	private void initView() {
		intent_1 = new Intent(this, IndexActivity.class);
		intent_2 = new Intent(this, GiftActivity.class);
		intent_3 = new Intent(this, UserActivity.class);

		m_tab.addTab(buildTagSpec("test1", 0, intent_1));
		m_tab.addTab(buildTagSpec("test2", 1, intent_2));
		m_tab.addTab(buildTagSpec("test3", 2, intent_3));

		m_rgroup = (RadioGroup) findViewById(R.id.tab_rgroup);
		m_radio_index = (MyRadioButton) findViewById(R.id.tab_radio1);
		m_radio_index.getLayoutParams().width = Tools.M_SCREEN_WIDTH/3;
		m_radio_restaurant = (MyRadioButton) findViewById(R.id.tab_radio2);
		m_radio_restaurant.getLayoutParams().width = Tools.M_SCREEN_WIDTH/3;
		m_radio_setting = (MyRadioButton) findViewById(R.id.tab_radio3);
		m_radio_setting.getLayoutParams().width = Tools.M_SCREEN_WIDTH/3;

		m_rgroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == m_radio_index.getId()) {
					m_tab.setCurrentTabByTag("test1");
				} else if (checkedId == m_radio_restaurant.getId()) {
					m_tab.setCurrentTabByTag("test2");
				} else if (checkedId == m_radio_setting.getId()) {
					m_tab.setCurrentTabByTag("test3");
				}
			}
		});
		m_tab.setCurrentTab(0);
	}

	/**
	 * @param tagName
	 * @param tagLable
	 * @param content
	 * @return 创建页面标签
	 */
	private TabHost.TabSpec buildTagSpec(String tagName, int tagLable,
			Intent content) {
		return m_tab.newTabSpec(tagName).setIndicator(tagLable + "")
				.setContent(content);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onDestroy() {
		Tools.Log("当前tabActivity退出");
		super.onDestroy();
	}
	
	/**
	 * 将活动Activity切换到“红包通知”上
	 */
	public static void switchTabActivity(int tabActivity){
		if(tabActivity == 1){
			m_radio_restaurant.setChecked(true);
		}else if(tabActivity == 2){
			m_radio_setting.setChecked(true);
		}
		m_tab.setCurrentTab(tabActivity);
	}
}
