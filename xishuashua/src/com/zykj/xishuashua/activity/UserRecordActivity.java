package com.zykj.xishuashua.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zykj.xishuashua.BaseActivity;
import com.zykj.xishuashua.R;
import com.zykj.xishuashua.adapter.CommonAdapter;
import com.zykj.xishuashua.adapter.ViewHolder;
import com.zykj.xishuashua.model.Record;
import com.zykj.xishuashua.utils.StringUtil;
import com.zykj.xishuashua.view.MyCommonTitle;

/**
 * @author Administrator
 * 我的收益记录
 */
public class UserRecordActivity extends BaseActivity{
	
	private MyCommonTitle myCommonTitle;
    private ListView myListView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_user_list);
		
		initView();
	}

	/**
	 * 加载页面
	 */
	private void initView() {
		myCommonTitle = (MyCommonTitle)findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("收益记录");
		myCommonTitle.setBackBtnVisible(false);
		
		myListView = (ListView)findViewById(R.id.store_mylistview);
	}

    /**
     * 请求数据
     */
    public void requestData() {
		List<Record> records = new ArrayList<Record>();
		records.add(new Record("临时红包", "大馅饺子楼", "浏览/分享红包2.156元"));
		records.add(new Record("临时红包", "集集小镇", "浏览/分享红包1.119元"));
		records.add(new Record("永久红包", "小馋猫", "浏览/分享红包0.006元"));
		records.add(new Record("支付宝提现", "", "提现金额20.25元"));
		records.add(new Record("话费提现", "", "提现30.00元"));
		myListView.setAdapter(new CommonAdapter<Record>(this, R.layout.ui_item_record, records) {
			@Override
			public void convert(ViewHolder holder, Record record) {
				holder.setText(R.id.record_name, record.getRecordtype()+"："+record.getRecordathor())
					.setText(R.id.record_money, record.getRecordname());
				TextView label = holder.getView(R.id.gift_label);
				label.setVisibility(StringUtil.isEmpty(record.getRecordathor())?View.GONE:View.VISIBLE);
			}
		});
    }

	@Override
	public void onClick(View v) {
		
	}
}
