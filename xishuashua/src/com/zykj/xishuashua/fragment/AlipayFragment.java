package com.zykj.xishuashua.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zykj.xishuashua.R;
import com.zykj.xishuashua.activity.GiftPerpetualActivity;
import com.zykj.xishuashua.activity.MainActivity;
import com.zykj.xishuashua.http.HttpErrorHandler;
import com.zykj.xishuashua.http.HttpUtils;
import com.zykj.xishuashua.utils.CommonUtils;
import com.zykj.xishuashua.utils.Tools;

public class AlipayFragment extends Fragment implements OnClickListener{
	
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
		TextView go_advice = (TextView)getView().findViewById(R.id.go_advice);
		TextView go_share = (TextView)getView().findViewById(R.id.go_share);
		setListener(go_advice, go_share);
	}

	public void setListener(View... view) {
		for (int i = 0; i < view.length; i++) {
			view[i].setOnClickListener(this);
		}
	}

	private void requestData() {
		
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.go_advice:
			/** 刷广告 */
			MainActivity.switchTabActivity(1);
			getActivity().finish();
			break;
		case R.id.go_share:
			/** 继续分享 */
			if(!CommonUtils.CheckLogin()){ Tools.toast(getActivity(), "请先登录"); return; }
			getmemberinterests();
			break;
		default:
			break;
		}
	}
	
	/**
	 * @param viewId
	 * 获取用户选择的兴趣标签
	 */
	private void getmemberinterests(){
		HttpUtils.getmemberinterests(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				JSONArray jsonArray = json.getJSONArray("list");
				String interestIds = "";
				for (int i = 0; i < jsonArray.size(); i++) {
					interestIds += jsonArray.getJSONObject(i).getString("interest_id")+",";
				}
				interestIds = interestIds.length()>0?interestIds.substring(0,interestIds.length()-1):"";
				startActivity(new Intent(getActivity(), GiftPerpetualActivity.class).putExtra("interestIds", interestIds));//永久红包
			}
		});
	}
}
