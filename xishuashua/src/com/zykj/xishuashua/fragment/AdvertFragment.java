package com.zykj.xishuashua.fragment;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.loopj.android.http.RequestParams;
import com.zykj.xishuashua.R;
import com.zykj.xishuashua.activity.GiftDetailActivity;
import com.zykj.xishuashua.adapter.GiftAdapter;
import com.zykj.xishuashua.http.EntityHandler;
import com.zykj.xishuashua.http.HttpUtils;
import com.zykj.xishuashua.model.Gift;
import com.zykj.xishuashua.utils.CommonUtils;
import com.zykj.xishuashua.utils.StringUtil;
import com.zykj.xishuashua.utils.Tools;
import com.zykj.xishuashua.view.MyRequestDailog;
import com.zykj.xishuashua.view.XListView;
import com.zykj.xishuashua.view.XListView.IXListViewListener;

/**
 * @author Administrator
 * 红包通知里面的列表
 */
@SuppressLint("HandlerLeak")
public class AdvertFragment extends Fragment implements IXListViewListener,OnItemClickListener{

    private String isperpetual="1";//1即时红包 0永久红包

	private static final String NUM = "5";//每页显示条数
	private int page = 1;
	private List<Gift> gifts = new ArrayList<Gift>();
	private GiftAdapter adapter;
    private XListView myListView;
	private Handler mHandler;

    public static AdvertFragment newInstance(String isperpetual){
    	AdvertFragment shopAssessFragment = new AdvertFragment();
        Bundle bundle=new Bundle();
        bundle.putString("iscontaintext",isperpetual);
        shopAssessFragment.setArguments(bundle);
        return shopAssessFragment;
    }

    /**
     * 加载页面
     */
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        myListView = new XListView(getActivity(), null);
        myListView.setDividerHeight(0);
        myListView.setLayoutParams(params);
        myListView.setPullLoadEnable(true);
		myListView.setXListViewListener(this);
        return myListView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
		mHandler = new Handler();
        isperpetual=getArguments().getString("iscontaintext");
        adapter = new GiftAdapter(getActivity(), R.layout.ui_item_gift, gifts);
		myListView.setAdapter(adapter);
		if("1".equals(isperpetual)){
			handler.sendEmptyMessage(1);
		}
		myListView.setOnItemClickListener(this);
        requestData();
    }

    /**
     * 请求红包列表
     */
    public void requestData() {
    	RequestParams params = new RequestParams();
    	params.put("marketprice", isperpetual);//0即时红包  1永久红包
    	params.put("page", String.valueOf(page));
    	params.put("per_page", NUM);
		MyRequestDailog.showDialog(getActivity(), "");
		HttpUtils.getenvelist_foroneuser(new EntityHandler<Gift>(Gift.class) {
			@Override
			public void onReadSuccess(List<Gift> list) {
				MyRequestDailog.closeDialog();
				if(page == 1){gifts.clear();}
				gifts.addAll(list);
				adapter.notifyDataSetChanged();
			}
		}, params);
    }

	private void onLoad() {
		myListView.stopRefresh();
		myListView.stopLoadMore();
		myListView.setRefreshTime("刚刚");
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(!CommonUtils.CheckLogin()){ Tools.toast(getActivity(), "请先登录"); return; }
		Intent detailIntent = new Intent(getActivity(), GiftDetailActivity.class);
		detailIntent.putExtra("goods_id", gifts.get(position-1).getGoods_id());
		startActivity(detailIntent);
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
