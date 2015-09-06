package com.zykj.xishuashua.activity;

import java.text.ParseException;
import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.zykj.xishuashua.BaseActivity;
import com.zykj.xishuashua.R;
import com.zykj.xishuashua.adapter.CommonAdapter;
import com.zykj.xishuashua.adapter.ViewHolder;
import com.zykj.xishuashua.model.MyEnvelope;
import com.zykj.xishuashua.utils.DateUtil;
import com.zykj.xishuashua.view.MyCommonTitle;

/**
 * @author Administrator
 * 我的收益记录
 */
public class UserRecordActivity extends BaseActivity{
	
	private MyCommonTitle myCommonTitle;
    private ListView myListView;
	private ArrayList<MyEnvelope> envelist;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_index_list);
		envelist = getIntent().getParcelableArrayListExtra("envelist");
		
		initView();
		requestData();
	}

	/**
	 * 加载页面
	 */
	private void initView() {
		myCommonTitle = (MyCommonTitle)findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("收益记录");
		
		myListView = (ListView)findViewById(R.id.advert_listview);
	}

    /**
     * 请求数据
     */
	private void requestData() {
		myListView.setAdapter(new CommonAdapter<MyEnvelope>(this, R.layout.ui_item_record, envelist) {
			@Override
			public void convert(ViewHolder holder, MyEnvelope envelope) {
				try {
					String time = DateUtil.longToString(Long.parseLong(envelope.getMembersawgoods_lastsawtime()+"000"), "yyyy-MM-dd");
					holder.setText(R.id.record_name, envelope.getEnve_serial()+"："+envelope.getStore_name())
						.setText(R.id.record_money, "浏览/分享红包"+envelope.getMembersawgoods_gotpoint()+"元")
						.setText(R.id.gift_label, envelope.getGoods_serial())
						.setText(R.id.record_address, time);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		});
    }

	@Override
	public void onClick(View v) {
		
	}
}
