package com.zykj.xishuashua.activity;

import java.util.List;

import android.os.Bundle;
import android.widget.ListView;

import com.zykj.xishuashua.BaseActivity;
import com.zykj.xishuashua.R;
import com.zykj.xishuashua.adapter.GiftAdapter;
import com.zykj.xishuashua.http.EntityHandler;
import com.zykj.xishuashua.http.HttpUtils;
import com.zykj.xishuashua.model.Gift;
import com.zykj.xishuashua.view.MyCommonTitle;
import com.zykj.xishuashua.view.MyRequestDailog;

public class UserStoreActivity extends BaseActivity{
	
	private MyCommonTitle myCommonTitle;
    private ListView myListView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_user_list);
		
		initView();
		requestData();
	}

	/**
	 * 加载页面
	 */
	private void initView() {
		myCommonTitle = (MyCommonTitle)findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("我的收藏");
		
		myListView = (ListView)findViewById(R.id.store_mylistview);
	}

    /**
     * 请求数据
     */
    public void requestData() {
		MyRequestDailog.showDialog(this, "");
		HttpUtils.getmembercollect(new EntityHandler<Gift>(Gift.class) {
			@Override
			public void onReadSuccess(List<Gift> list) {
				MyRequestDailog.closeDialog();
				myListView.setAdapter(new GiftAdapter(UserStoreActivity.this, R.layout.ui_item_gift, list, "1"));
			}
		});
    }
}
