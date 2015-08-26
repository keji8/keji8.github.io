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
import com.zykj.xishuashua.adapter.CommonAdapter;
import com.zykj.xishuashua.adapter.ViewHolder;
import com.zykj.xishuashua.http.EntityHandler;
import com.zykj.xishuashua.http.HttpUtils;
import com.zykj.xishuashua.http.UrlContants;
import com.zykj.xishuashua.model.Gift;
import com.zykj.xishuashua.view.MyCommonTitle;
import com.zykj.xishuashua.view.XListView;
import com.zykj.xishuashua.view.XListView.IXListViewListener;

/**
 * @author Administrator
 * csh 2015-08-07
 */
public class IndexNewsActivity extends BaseActivity implements IXListViewListener,OnItemClickListener{
	
	public static String ADVERTTYPE = "1";
	private static final String NUM = "5";//每页显示条数
	private int page = 1;

	private MyCommonTitle myCommonTitle;
	private XListView advert_listview;
	private List<Gift> news = new ArrayList<Gift>();
	private Handler mHandler;
	private CommonAdapter<Gift> newAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_index_news);

		mHandler = new Handler();
		initView();
		requestData();
	}
	
	/**
	 * 加载页面
	 */
	private void initView(){
		myCommonTitle = (MyCommonTitle)findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("新闻资讯");

		advert_listview = (XListView)findViewById(R.id.advert_listview);
		newAdapter = new CommonAdapter<Gift>(this, R.layout.ui_item_new, news) {
			@Override
			public void convert(ViewHolder holder, Gift newinfo) {
				holder.setImageUrl(R.id.new_img, UrlContants.GIFTIMGURL+newinfo.getGoods_image(), 10f)
						.setText(R.id.new_title, newinfo.getGoods_name())
						.setText(R.id.new_content, newinfo.getGoods_jingle().length()>50?newinfo.getGoods_jingle().substring(0,50):newinfo.getGoods_jingle());
			}
		};
        advert_listview.setAdapter(newAdapter);
		advert_listview.setDividerHeight(0);
		advert_listview.setPullLoadEnable(true);
		advert_listview.setXListViewListener(this);
        advert_listview.setOnItemClickListener(this);
	}

	/**
	 * 请求服务器数据---首页
	 */
	private void requestData(){
		//获取首页新闻资讯列表
		RequestParams params = new RequestParams();
		params.put("page", page);//当前第几页
		params.put("per_page", NUM);//每页条数
		//获取首页新闻资讯列表
		HttpUtils.getnewslist(new EntityHandler<Gift>(Gift.class) {
			@Override
			public void onReadSuccess(List<Gift> list) {
				if(page == 1){news.clear();}
				news.addAll(list);
				newAdapter.notifyDataSetChanged();
			}
		}, params);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View convertView, int position, long checkedId) {
		String newId = news.get(position-1).getGoods_id();
		startActivity(new Intent(IndexNewsActivity.this, IndexNewDetailActivity.class).putExtra("newId", newId));
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
		advert_listview.stopRefresh();
		advert_listview.stopLoadMore();
		advert_listview.setRefreshTime("刚刚");
	}
}
