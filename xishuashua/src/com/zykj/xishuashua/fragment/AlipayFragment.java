package com.zykj.xishuashua.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zykj.xishuashua.R;

public class AlipayFragment extends Fragment {
	
	private String money;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ui_user_alipay,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        money=getArguments().getString("money");
        initView();
        requestData();
    }

	private void initView() {
		TextView alipay_allow = (TextView)getView().findViewById(R.id.alipay_allow);
		alipay_allow.setText("可提现金额："+money+"元");
	}

	private void requestData() {
		
	}

}
