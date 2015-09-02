package com.zykj.xishuashua.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import com.zykj.xishuashua.BaseActivity;
import com.zykj.xishuashua.R;
import com.zykj.xishuashua.adapter.GiftAdapter;
import com.zykj.xishuashua.http.EntityHandler;
import com.zykj.xishuashua.http.HttpUtils;
import com.zykj.xishuashua.model.Gift;
import com.zykj.xishuashua.utils.StringUtil;
import com.zykj.xishuashua.view.MyCommonTitle;
import com.zykj.xishuashua.view.MyRequestDailog;

public class UserStoreActivity extends BaseActivity{
	
	private MyCommonTitle myCommonTitle;
    private ListView myListView;
	private GiftAdapter adapter;
	private List<Gift> gifts = new ArrayList<Gift>();
	
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
		adapter = new GiftAdapter(UserStoreActivity.this, R.layout.ui_item_gift, gifts);
		myListView.setAdapter(adapter);

		handler.sendEmptyMessage(1);
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
				gifts.addAll(list);
				adapter.notifyDataSetChanged();
			}
		});
    }
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				//①：其实在这块需要精确计算当前时间
				for(int index =0;index<gifts.size();index++){
					Gift gift = gifts.get(index);
					if(gift.getCurrentSeconds() == null){
						long continueTime = Long.parseLong(StringUtil.toString(gift.getGoods_marketprice(), "0"));
						long startTime = Long.parseLong(StringUtil.toString(gift.getGoods_selltime(), "0"));
						long seconds = startTime + continueTime - System.currentTimeMillis()/1000;
						gift.setCurrentSeconds(String.valueOf(seconds));
					}
					long time = Long.parseLong(gift.getCurrentSeconds());
					if(time>1){//判断是否还有条目能够倒计时，如果能够倒计时的话，延迟一秒，让它接着倒计时
						gift.setCurrentSeconds(String.valueOf(time-1));
					}else{
						gift.setCurrentSeconds("0");
					}
				}
				//②：for循环执行的时间
				adapter.notifyDataSetChanged();
				handler.sendEmptyMessageDelayed(1, 1000);
				break;
			}
		}
	};
}
