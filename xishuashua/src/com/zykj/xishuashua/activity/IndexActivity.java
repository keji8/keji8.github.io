package com.zykj.xishuashua.activity;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.xishuashua.BaseActivity;
import com.zykj.xishuashua.R;
import com.zykj.xishuashua.adapter.CommonAdapter;
import com.zykj.xishuashua.adapter.RecyclingPagerAdapter;
import com.zykj.xishuashua.adapter.ViewHolder;
import com.zykj.xishuashua.http.EntityHandler;
import com.zykj.xishuashua.http.HttpErrorHandler;
import com.zykj.xishuashua.http.HttpUtils;
import com.zykj.xishuashua.http.UrlContants;
import com.zykj.xishuashua.model.Gift;
import com.zykj.xishuashua.utils.CommonUtils;
import com.zykj.xishuashua.utils.DateUtil;
import com.zykj.xishuashua.utils.Tools;
import com.zykj.xishuashua.view.AutoListView;
import com.zykj.xishuashua.view.MyCommonTitle;


@SuppressLint("HandlerLeak")
public class IndexActivity extends BaseActivity implements OnItemClickListener{
	/**
	 * @author csh 2015-08-22
	 * 首页
	 */
	private static final String NUM = "5";//每页显示条数
	private int page = 1;
	
	private MyCommonTitle myCommonTitle;
	private AutoScrollViewPager viewPager;
	private AutoListView index_list;
	private LinearLayout tv_news_more,index_gift;
	private ScrollView container;
	/** 当前的位置 */
	private int now_pos = 0;
	private List<String> imageList = new ArrayList<String>();
	private ImageView index_image1,index_image2,index_image3;
	private List<Gift> news = new ArrayList<Gift>();
	private List<Gift> specialList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(R.layout.ui_index_activity);
		
		initView();
		requestData();
	}
	
	/**
	 * 加载页面
	 */
	private void initView(){
		/*头部标题*/
		myCommonTitle = (MyCommonTitle)findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("喜刷刷");
		myCommonTitle.setBackBtnVisible(false);
		myCommonTitle.setLisener(null, this);
		
		container = (ScrollView)findViewById(R.id.container);
		
		viewPager = (AutoScrollViewPager) findViewById(R.id.index_slider);//轮播图
		tv_news_more = (LinearLayout)findViewById(R.id.tv_index_gift);//新闻更多
		index_list = (AutoListView)findViewById(R.id.index_list);//新闻资讯
		index_list.setOnItemClickListener(this);
		index_gift = (LinearLayout)findViewById(R.id.index_gift);//中间三大模块(即时，永久，兴趣)

		index_image1 = (ImageView)findViewById(R.id.index_image1);//即时红包
		index_image2 = (ImageView)findViewById(R.id.index_image2);//永久红包
		index_image3 = (ImageView)findViewById(R.id.index_image3);//兴趣标签
		
		LayoutParams pageParms = viewPager.getLayoutParams();
		pageParms.width = Tools.M_SCREEN_WIDTH;
		pageParms.height = Tools.M_SCREEN_WIDTH * 2 / 5;
		
		LayoutParams imagelayout = index_gift.getLayoutParams();
		imagelayout.width = Tools.M_SCREEN_WIDTH;
		imagelayout.height = Tools.M_SCREEN_WIDTH * 11 / 30;
		
		viewPager.setInterval(2000);
		viewPager.startAutoScroll();
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int position) {
				// 回调view
				uihandler.obtainMessage(0, position).sendToTarget();
			}
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPx) {}
			public void onPageScrollStateChanged(int position) {}
		});
		
		setListener(index_image1, index_image2, index_image3, tv_news_more);
	}
	
	/**
	 * 请求服务器数据---首页
	 */
	private void requestData(){
		//获取轮播图
		HttpUtils.getSpecialList(new EntityHandler<Gift>(Gift.class) {
			@Override
			public void onReadSuccess(List<Gift> list) {
				specialList = list;
				for(int i = 0; i < list.size(); i++){
					imageList.add(list.get(i).getGoods_image());
				}
				// 设置轮播
				viewPager.setAdapter(adapter);
				// 设置选中的标识
				LinearLayout pointLinear = (LinearLayout) findViewById(R.id.gallery_point_linear);
				for (int i = 0; i < imageList.size(); i++) {
					ImageView pointView = new ImageView(IndexActivity.this);
					if (i == 0) { pointView.setBackgroundResource(R.drawable.feature_point_cur);}
					else { pointView.setBackgroundResource(R.drawable.feature_point);}
					pointLinear.addView(pointView);
				}
			}
		});
		//获取首页新闻资讯列表
		RequestParams params = new RequestParams();
		params.put("page", page);//当前第几页
		params.put("per_page", NUM);//每页条数
		HttpUtils.getnewslist(rel_getnewslist, params);
	}
	
	/**
	 * 获取新闻资讯列表
	 */
	private AsyncHttpResponseHandler rel_getnewslist = new EntityHandler<Gift>(Gift.class) {
		@Override
		public void onReadSuccess(List<Gift> list) {
			news = list;
			index_list.setAdapter(new CommonAdapter<Gift>(IndexActivity.this, R.layout.ui_item_new,  list.size()>5?list.subList(0, 5):list) {
				@Override
				public void convert(ViewHolder holder, Gift newinfo) {
					String imgurl = newinfo.getGoods_image();
					imgurl = imgurl.substring(0, imgurl.indexOf("_"));
					if(holder.getPosition()<5){
						try {
							String date = DateUtil.longToString(Long.valueOf(newinfo.getGoods_selltime()+"000"), "yyyy-MM-dd");
							holder.setImageUrl(R.id.new_img, UrlContants.GIFTIMGURL+imgurl+File.separator+newinfo.getGoods_image(), 10f)
							.setText(R.id.new_title, newinfo.getGoods_name()).setText(R.id.new_createtime, date)
							.setText(R.id.new_content, newinfo.getGoods_jingle().length()>50?newinfo.getGoods_jingle().substring(0,50):newinfo.getGoods_jingle());
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
			});
			container.smoothScrollTo(0, 0);
		}
	};
	
	/**
	 * 设置轮播图
	 */
	private PagerAdapter adapter = new RecyclingPagerAdapter() {
		@Override
		public int getCount() {
			return imageList.size();
		}
		@Override
		public View getView(int position, View convertView, ViewGroup container) {
			String imgurl = imageList.get(position);
			imgurl = imgurl.substring(0, imgurl.indexOf("_"));
			ImageView imageView;
			if (convertView == null) {
				convertView = imageView = new ImageView(IndexActivity.this);
				imageView.setScaleType(ScaleType.FIT_XY);
				imageView.setId(Integer.valueOf(position));
				convertView.setTag(imageView);
			} else {
				imageView = (ImageView) convertView.getTag();
			}
			ImageLoader.getInstance().displayImage(UrlContants.GIFTIMGURL+imgurl+File.separator+imageList.get(position), imageView);
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					int index = Integer.valueOf(view.getId());
					startActivity(new Intent(IndexActivity.this, GiftDetailActivity.class)
						.putExtra("goods_id", specialList.get(index).getGoods_id()).putExtra("saw", specialList.get(index).getGoods_id()));
				}
			});
			return convertView;
		}
	};
	
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.aci_shared_btn:
			/*App分享*/
			CommonUtils.showShare(this, getString(R.string.app_name), "喜刷刷是一款可以边看新闻边抢红包的App", 
					"http://dashboard.mob.com/Uploads/1b692f6c9fceaf93c407afd889c36090.png", "");
			break;
		case R.id.index_image1:
			/*即时红包*/
//			GiftActivity.ADVERTTYPE = "1";
//			MainActivity.switchTabActivity(1);
			if(!CommonUtils.CheckLogin()){ Tools.toast(this, "请先登录"); return; }
			getmemberinterests(R.id.index_image1);
			break;
		case R.id.index_image2:
			/*永久红包*/
//			GiftActivity.ADVERTTYPE = "2";
//			MainActivity.switchTabActivity(1);
			if(!CommonUtils.CheckLogin()){ Tools.toast(this, "请先登录"); return; }
			getmemberinterests(R.id.index_image2);
			break;
		case R.id.index_image3:
			/*兴趣标签*/
			if(!CommonUtils.CheckLogin()){ Tools.toast(this, "请先登录"); return; }
			getmemberinterests(R.id.index_image3);
			break;
		case R.id.tv_index_gift:
			/*更多新闻*/
			startActivity(new Intent(this, IndexNewsActivity.class));
			break;
		}
	}
	
	/**
	 * @param viewId
	 * 获取用户选择的兴趣标签
	 */
	private void getmemberinterests(final int viewId){
		HttpUtils.getmemberinterests(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				JSONArray jsonArray = json.getJSONArray("list");
				String interestIds = "";
				for (int i = 0; i < jsonArray.size(); i++) {
					interestIds += jsonArray.getJSONObject(i).getString("interest_id")+",";
				}
				interestIds = interestIds.length()>0?interestIds.substring(0,interestIds.length()-1):"";
				if(viewId == R.id.index_image3){
					startActivity(new Intent(IndexActivity.this, UserLableActivity.class).putExtra("interestIds", interestIds));//兴趣标签
				}else if(viewId == R.id.index_image2){
					startActivity(new Intent(IndexActivity.this, GiftPerpetualActivity.class).putExtra("interestIds", interestIds));//永久红包
				}else{
					startActivity(new Intent(IndexActivity.this, GiftForthwithActivity.class).putExtra("interestIds", interestIds));//即时红包
				}
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (viewPager != null) {
			viewPager.startAutoScroll();
		}
		if(container != null){
			container.smoothScrollTo(0, 0);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (viewPager != null) {
			viewPager.stopAutoScroll();
		}
	}

	Handler uihandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:// 滚动的回调
				changePointView((Integer) msg.obj);
				break;
			}
		}
	};

	/**
	 * @param cur 切换当前的图片
	 */
	public void changePointView(int cur) {
		LinearLayout pointLinear = (LinearLayout) findViewById(R.id.gallery_point_linear);
		View view = pointLinear.getChildAt(now_pos);
		View curView = pointLinear.getChildAt(cur);
		if (view != null && curView != null) {
			ImageView pointView = (ImageView) view;
			ImageView curPointView = (ImageView) curView;
			pointView.setBackgroundResource(R.drawable.feature_point);
			curPointView.setBackgroundResource(R.drawable.feature_point_cur);
			now_pos = cur;
		}
	}
	
	//退出操作
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		CommonUtils.exitkey(keyCode, this);
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View convertView, int position, long checkedId) {
		String newId = news.get(position).getGoods_id();
//		Tools.toast(this, journalism.getGoods_id()+"");
		startActivity(new Intent(IndexActivity.this, IndexNewDetailActivity.class).putExtra("newId", newId));
	}
}
