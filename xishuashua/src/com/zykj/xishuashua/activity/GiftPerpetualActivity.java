package com.zykj.xishuashua.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zykj.xishuashua.BaseActivity;
import com.zykj.xishuashua.R;
import com.zykj.xishuashua.adapter.GiftAdapter;
import com.zykj.xishuashua.http.EntityHandler;
import com.zykj.xishuashua.http.HttpUtils;
import com.zykj.xishuashua.model.Gift;
import com.zykj.xishuashua.model.Interest;
import com.zykj.xishuashua.utils.CommonUtils;
import com.zykj.xishuashua.utils.Tools;
import com.zykj.xishuashua.view.MyRequestDailog;
import com.zykj.xishuashua.view.SegmentThreeView;
import com.zykj.xishuashua.view.SegmentThreeView.onSegmentViewClickListener;
import com.zykj.xishuashua.view.XListView;
import com.zykj.xishuashua.view.XListView.IXListViewListener;

/**
 * @author Administrator
 * 首页永久红包
 */
public class GiftPerpetualActivity extends BaseActivity implements OnCheckedChangeListener
,IXListViewListener,OnItemClickListener,onSegmentViewClickListener{

	private RadioGroup cradioGroup;//兴趣标签
	private XListView mListView;//红包列表
	private SegmentThreeView order_seg;//App、商家、者个人
	
	private static final String NUM = "2";//每页显示条数
	private int page = 1;//当前第几页
	private String grade_id = "app";//0-个人红包  1-商家红包  app-app红包
	private String interesttag;//兴趣标签
	private List<Gift> gifts = new ArrayList<Gift>();//红包数据
	private GiftAdapter adapter;//设配器
	private Handler mHandler;//异步加载或刷新
	private String[] interestIds;//用户爱好标签
    private RadioGroup.LayoutParams mRadioParams;//标签布局

	private List<Interest> interest1 = new ArrayList<Interest>();//用户爱好标签
	private List<Interest> interest2 = new ArrayList<Interest>();//其他标签
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_gift_perpetual);
		interestIds = getIntent().getStringExtra("interestIds").split(",");
		
		mHandler = new Handler();
		mRadioParams = new RadioGroup.LayoutParams(Tools.M_SCREEN_WIDTH/6, LinearLayout.LayoutParams.MATCH_PARENT);
		initView();
	}

	/**
	 * 首次加载页面
	 */
	private void initView() {
		findViewById(R.id.aci_back_btn).setOnClickListener(this);;//公司简介
		order_seg = (SegmentThreeView)findViewById(R.id.order_seg);//即时红包分类
		order_seg.setSegmentText("App红包", 0);
		order_seg.setSegmentText("商家红包", 1);
		order_seg.setSegmentText("个人红包", 2);
		order_seg.setOnSegmentViewClickListener(this);
		cradioGroup = (RadioGroup)findViewById(R.id.cradioGroup);//标签切换
		cradioGroup.setOnCheckedChangeListener(this);
		mListView = (XListView)findViewById(R.id.gift_listview);//选择标签
        adapter = new GiftAdapter(this, R.layout.ui_item_gift, gifts);
        mListView.setAdapter(adapter);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setOnItemClickListener(this);
		HttpUtils.getAllInterests(getAllInterests);
		cradioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				interesttag = checkedId+"";
				requestData();
			}
		});
	}
	
	/**
	 * 请求网络数据----标签、红包
	 */
	private void requestData(){
    	RequestParams params = new RequestParams();
    	params.put("page", String.valueOf(page));
    	params.put("per_page", NUM);
    	params.put("marketprice", "0");//"1"即时红包, "0"永久红包
    	params.put("interestid", interesttag == null?"1":interesttag);//兴趣标签Id
    	params.put("grade_id", grade_id);//0-个人红包  1-商家红包  app-app红包(默认)
		MyRequestDailog.showDialog(this, "");
		HttpUtils.getsomekindenvelist(rel_getEnveList, params);
	}
	
	/**
	 * 网络请求标签列表
	 * getAllInterests 所有标签
	 */
	private AsyncHttpResponseHandler getAllInterests = new EntityHandler<Interest>(Interest.class) {
		@Override
		public void onReadSuccess(List<Interest> list) {
			screeningChecked(list);
			/** 加载用户爱好兴趣 */
			addInterests(interest1);
			/** 加载更多兴趣 */
			addMoreButton();
		}
	};
	
	/**
	 * 加载便签列表
	 * @param list 标签列表
	 */
	private void addInterests(List<Interest> list){
		for (int i = 0; i < list.size() ; i++) {
            RadioButton radioButton = new RadioButton(GiftPerpetualActivity.this);
            radioButton.setId(Integer.valueOf(list.get(i).getInterest_id()));
            radioButton.setText(list.get(i).getInterest_name());
            radioButton.setTextSize(18f);
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setTextColor(getResources().getColorStateList(R.drawable.tab_font_color));
            radioButton.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
            radioButton.setBackgroundResource(R.drawable.gift_tab_bg);
            radioButton.setChecked(i == 0?true:false);
            if(i == 0){interesttag = list.get(i).getInterest_id();}
            cradioGroup.addView(radioButton,mRadioParams);
    		requestData();
		}
	}
	
	/** 加载其他更多兴趣标签 */
	private void addMoreButton(){
		final TextView textview = new TextView(GiftPerpetualActivity.this);
		textview.setText("更多");
		textview.setTextSize(18f);
		textview.setGravity(Gravity.CENTER);//居中显示
		textview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				/** 点击更多加载 */
				cradioGroup.removeView(textview);
				addInterests(interest2);
			}
		});
        cradioGroup.addView(textview,mRadioParams);
	}
	
	/**
	 * @param list 总标签
	 * 筛选用户兴趣标签
	 */
	private void screeningChecked(List<Interest> list){
		interest2 = list;
		for (int i = 0; i < list.size(); i++) {
			for (String interest_id : interestIds) {
				if(interest_id.equals(list.get(i).getInterest_id())){
					interest1.add(list.get(i));
					interest2.remove(i);
				}
			}
		}
	}
	
	/**
	 * 获取红包列表
	 */
	private AsyncHttpResponseHandler rel_getEnveList = new EntityHandler<Gift>(Gift.class) {
		@Override
		public void onReadSuccess(List<Gift> list) {
			MyRequestDailog.closeDialog();
			if(page == 1){gifts.clear();}
			gifts.addAll(list);
			adapter.notifyDataSetChanged();
		}
	};

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.aci_back_btn){ finish(); }
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View convertView, int position, long selectId) {
		if(!CommonUtils.CheckLogin()){ Tools.toast(this, "请先登录"); return; }
		Intent detailIntent = new Intent(this, GiftDetailActivity.class);
		detailIntent.putExtra("goods_id", gifts.get(position-1).getGoods_id());
		startActivity(detailIntent);
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
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}

	@Override
	public void onSegmentViewClick(View convertView, int position) {
		if(position == 0){
			page = 1;
			grade_id = "app";
			requestData();
			onLoad();
		}else if(position == 1){
			page = 1;
			grade_id = "1";
			requestData();
			onLoad();
		}else{
			page = 1;
			grade_id = "0";
			requestData();
			onLoad();
		}
	}
}
