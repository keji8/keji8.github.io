package com.zykj.xishuashua.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.loopj.android.http.RequestParams;
import com.zykj.xishuashua.BaseActivity;
import com.zykj.xishuashua.R;
import com.zykj.xishuashua.adapter.GiftAdapter;
import com.zykj.xishuashua.http.EntityHandler;
import com.zykj.xishuashua.http.HttpUtils;
import com.zykj.xishuashua.model.Gift;
import com.zykj.xishuashua.utils.StringUtil;
import com.zykj.xishuashua.view.MyCommonTitle;
import com.zykj.xishuashua.view.MyRequestDailog;
import com.zykj.xishuashua.view.XListView;
import com.zykj.xishuashua.view.XListView.IXListViewListener;

public class UserStoreActivity extends BaseActivity implements IXListViewListener,OnItemClickListener{

	private static final String NUM = "5";//每页显示条数
	private int page = 1;//////////////////////////////////////////////////////////////////
	private Handler mHandler = new Handler();
	
	private MyCommonTitle myCommonTitle;
    private XListView myListView;
	private GiftAdapter adapter;
	private List<Gift> gifts = new ArrayList<Gift>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_index_news);
		
		initView();
		requestData();
	}

	/**
	 * 加载页面
	 */
	private void initView() {
		myCommonTitle = (MyCommonTitle)findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("我的收藏");
		
		myListView = (XListView)findViewById(R.id.advert_listview);
		adapter = new GiftAdapter(UserStoreActivity.this, R.layout.ui_item_gift, gifts);
		myListView.setAdapter(adapter);
		myListView.setDividerHeight(0);
		myListView.setPullLoadEnable(true);
		myListView.setXListViewListener(this);
		myListView.setOnItemClickListener(this);

		handler.sendEmptyMessage(1);
	}

    /**
     * 请求数据
     */
    public void requestData() {
		MyRequestDailog.showDialog(this, "");
		RequestParams params = new RequestParams();
		params.put("page", page);//当前第几页
		params.put("per_page", NUM);//每页条数
		HttpUtils.getmembercollect(new EntityHandler<Gift>(Gift.class) {
			@Override
			public void onReadSuccess(List<Gift> list) {
				MyRequestDailog.closeDialog();
				if(page == 1){gifts.clear();}
				gifts.addAll(list);
				adapter.notifyDataSetChanged();
			}
		}, params);
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

	@Override
	public void onItemClick(AdapterView<?> parent, View convertView, int position, long checkedId) {
		Gift gift = gifts.get(position-1);
		if("news".equals(gift.getStore_name())){
			startActivity(new Intent(UserStoreActivity.this, IndexNewDetailActivity.class).putExtra("newId", gift.getGoods_id()));
		}else{
			startActivity(new Intent(UserStoreActivity.this, GiftDetailActivity.class)
				.putExtra("goods_id", gift.getGoods_id()).putExtra("saw", gift.getSaw()));
		}
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				page = 1;
				requestData();
				onLoad();
			}
		}, 1000);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				page += 1;
				requestData();
				onLoad();
			}
		}, 1000);
	}

	private void onLoad() {
		myListView.stopRefresh();
		myListView.stopLoadMore();
		myListView.setRefreshTime("刚刚");
	}
}
