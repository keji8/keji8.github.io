package com.zykj.xishuashua.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.zykj.xishuashua.R;
import com.zykj.xishuashua.fragment.AlipayFragment;
import com.zykj.xishuashua.fragment.CallingFragment;
import com.zykj.xishuashua.view.MyCommonTitle;

public class UserWalletActivity extends FragmentActivity{
	
	private MyCommonTitle myCommonTitle;
    private AlipayFragment alipayFragment;
    private CallingFragment callingFragment;
	private RadioButton btn_1,btn_2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_user_wallet);
		
		initView();
		initFragment();
	}

	private void initView() {
		myCommonTitle = (MyCommonTitle)findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("我的钱包");

		RadioGroup group= (RadioGroup) findViewById(R.id.radios);

        btn_1= (RadioButton) findViewById(R.id.radio_btn1);
        btn_2= (RadioButton) findViewById(R.id.radio_btn2);

		
        group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == btn_1.getId()) {
	                getSupportFragmentManager().beginTransaction().show(alipayFragment).hide(callingFragment).commit();
				} else if (checkedId == btn_2.getId()) {
	                getSupportFragmentManager().beginTransaction().hide(alipayFragment).show(callingFragment).commit();
				}
			}
		});
	}
	
    private void initFragment() {
    	Bundle bundle = new Bundle();
    	bundle.putString("money", "25");
        alipayFragment = new AlipayFragment();alipayFragment.setArguments(bundle);
        callingFragment= new CallingFragment();callingFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.frag_layout,alipayFragment).
                add(R.id.frag_layout,callingFragment)
                .show(alipayFragment).hide(callingFragment).commit();
    }
}
