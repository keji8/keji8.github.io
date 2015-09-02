package com.zykj.xishuashua.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.zykj.xishuashua.R;
import com.zykj.xishuashua.fragment.AdvertFragment;
import com.zykj.xishuashua.utils.CommonUtils;
import com.zykj.xishuashua.view.MyCommonTitle;

/**
 * @author Administrator
 * csh 2015-08-07
 */
public class GiftActivity extends FragmentActivity{

	private MyCommonTitle myCommonTitle;
    private AdvertFragment forthwithFragment;
    private AdvertFragment perpetualFragment;
	
	private RadioGroup tab_gift;
	private RadioButton gift_left,gift_right;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_gift_activity);
		
		initView();
		initFragment();
	}
	
	/**
	 * 加载页面
	 */
	private void initView(){
		myCommonTitle = (MyCommonTitle)findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("红包通知");
		myCommonTitle.setBackBtnVisible(false);
		myCommonTitle.setLisener(null, null);
		
		tab_gift = (RadioGroup) findViewById(R.id.tab_gift);
		gift_left = (RadioButton) findViewById(R.id.gift_left);
		gift_right = (RadioButton) findViewById(R.id.gift_right);
		
		tab_gift.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == gift_left.getId()) {
	                getSupportFragmentManager().beginTransaction().show(forthwithFragment).hide(perpetualFragment).commit();
				} else if (checkedId == gift_right.getId()) {
	                getSupportFragmentManager().beginTransaction().hide(forthwithFragment).show(perpetualFragment).commit();
				}
			}
		});
	}

	/**
	 * 请求服务器数据---首页
	 */
	private void initFragment(){
		forthwithFragment = AdvertFragment.newInstance("1");//即时广告
		perpetualFragment = AdvertFragment.newInstance("0");//永久广告

        getSupportFragmentManager().beginTransaction().add(R.id.advert_contain,forthwithFragment).
            add(R.id.advert_contain,perpetualFragment).show(forthwithFragment).hide(perpetualFragment).commit();
	}

	//退出操作
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		CommonUtils.exitkey(keyCode, this);
		return super.onKeyDown(keyCode, event);
	}
}
