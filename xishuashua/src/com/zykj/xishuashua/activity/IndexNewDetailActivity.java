package com.zykj.xishuashua.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.xishuashua.BaseActivity;
import com.zykj.xishuashua.R;
import com.zykj.xishuashua.adapter.CommonAdapter;
import com.zykj.xishuashua.adapter.ViewHolder;
import com.zykj.xishuashua.http.EntityHandler;
import com.zykj.xishuashua.http.HttpErrorHandler;
import com.zykj.xishuashua.http.HttpUtils;
import com.zykj.xishuashua.http.UrlContants;
import com.zykj.xishuashua.model.Comment;
import com.zykj.xishuashua.utils.Tools;
import com.zykj.xishuashua.view.MyCheckBox;
import com.zykj.xishuashua.view.MyCommonTitle;
import com.zykj.xishuashua.view.MyRequestDailog;
import com.zykj.xishuashua.view.UIDialog;
import com.zykj.xishuashua.view.XListView;
import com.zykj.xishuashua.view.XListView.IXListViewListener;

public class IndexNewDetailActivity extends BaseActivity implements IXListViewListener,OnItemClickListener{
	
	/**
	 * 新闻资讯详情
	 */
	private static final String NUM = "2";//每页显示条数
	private int page = 1;
	
	private MyCommonTitle myCommonTitle;
	private String newId;
	private TextView gift_message,message_title,msg_content;
	private CheckBox msg_dw_laud,msg_dw_star,msg_dw_comment,msg_dw_share,bottom_comment;
	private boolean check_laud,check_star;
	private ImageView bottom_comment_show,bottom_store,bottom_mobile;
	private LinearLayout msg_content_img;
	private RelativeLayout layout_laud;
	private XListView msg_listview;
	private CommonAdapter<Comment> commonAdapter;
	private View headView;
	private EditText text;
	private Handler mHandler = new Handler();
	private List<Comment> comments = new ArrayList<Comment>();
	private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	
	private JSONObject good;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_gift_detail);
		newId = getIntent().getStringExtra("newId");//新闻资讯Id
		
		initView();
		requestData();
	}

	/**
	 * 初始化页面
	 */
	private void initView() {
		myCommonTitle = (MyCommonTitle)findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("红包详情");
		myCommonTitle.setLisener(null, this);
		
		commonAdapter = new CommonAdapter<Comment>(this, R.layout.ui_item_comment, comments) {
			@Override
			public void convert(ViewHolder holder, Comment comment) {
				holder.setText(R.id.comment_name, comment.getMember_name())//
					.setImageUrl(R.id.comment_images, UrlContants.ABATARURL+comment.getMember_avatar(), 10f)//
					.setText(R.id.comment_content, comment.getComment_content())//
					.setText(R.id.comment_num1, "("+comment.getComment_subcommentnum()+")")//
					.setText(R.id.comment_num2, "("+comment.getComment_favoratenum()+")")//
					.setText(R.id.comment_time, Tools.getTimeStr(comment.getComment_commenttime()));
			}
		};
		gift_message = (TextView)findViewById(R.id.gift_message);//提示信息
		gift_message.setVisibility(View.GONE);
		msg_listview = (XListView)findViewById(R.id.msg_listview);//评论列表
		msg_listview.setVisibility(View.GONE);
		msg_listview.setAdapter(commonAdapter);
		msg_listview.setPullLoadEnable(true);
		msg_listview.setXListViewListener(this);
		msg_listview.setOnItemClickListener(this);
		
		headView = LayoutInflater.from(this).inflate(R.layout.ui_gift_header, null);
		msg_listview.addHeaderView(headView);
		
		message_title = (TextView)headView.findViewById(R.id.message_title);//标题
		layout_laud = (RelativeLayout)headView.findViewById(R.id.layout_laud);//图片展示
		msg_dw_laud = (CheckBox)headView.findViewById(R.id.msg_dw_laud);//喜欢
		msg_dw_star = (CheckBox)headView.findViewById(R.id.msg_dw_star);//收藏
		msg_dw_star.setClickable(false);//不能点击
		msg_dw_comment = (CheckBox)headView.findViewById(R.id.msg_dw_comment);//评论
		msg_dw_comment.setClickable(false);//不能点击
		msg_dw_share = (CheckBox)headView.findViewById(R.id.msg_dw_share);//分享
		msg_dw_share.setClickable(false);//不能点击
		msg_content = (TextView)headView.findViewById(R.id.msg_content);//内容
		msg_content_img = (LinearLayout)headView.findViewById(R.id.msg_content_img);//图片展示
		
		bottom_comment = (MyCheckBox)findViewById(R.id.bottom_comment);//添加评论
		bottom_comment_show = (ImageView)findViewById(R.id.bottom_comment_show);//查看评论
		bottom_store = (ImageView)findViewById(R.id.bottom_store);//收藏
		bottom_mobile = (ImageView)findViewById(R.id.bottom_mobile);//拨号
		
		setListener(layout_laud, bottom_comment, bottom_comment_show, bottom_store, bottom_mobile);
	}
	
	/**
	 * 请求红包详情和评论
	 */
	private void requestData() {
		RequestParams params = new RequestParams();
		params.put("goods_id", newId);
		MyRequestDailog.showDialog(this, "");
		HttpUtils.getGoodDetail(rel_getGoodDetail, params);
	}
	
	private void requestComments(){
		RequestParams params = new RequestParams();
		params.put("goods_id", newId);
		params.put("comment_supercommentid", "0");
    	params.put("page", String.valueOf(page));
    	params.put("per_page", NUM);
		HttpUtils.getcomment(rel_getcomment, params);
	}
	
	/**
	 * 请求红包详情
	 */
	private AsyncHttpResponseHandler rel_getGoodDetail = new HttpErrorHandler() {
		@Override
		public void onRecevieSuccess(JSONObject json) {
			msg_dw_laud.setChecked("1".equals(json.getString("favorate")));
			check_laud = "1".equals(json.getString("favorate"));
			msg_dw_star.setChecked("1".equals(json.getString("collect")));
			check_star = "1".equals(json.getString("collect"));
			good = json.getJSONObject("data");
			message_title.setText(good.getString("goods_name"));
			msg_content.setText(good.getString("goods_jingle"));
			msg_content_img.removeAllViews();
			Object jsonImg = json.get("images");
			if (jsonImg instanceof JSONArray){
				for (Object object : (JSONArray)jsonImg) {
					String img_url = ((JSONObject)object).getString("goods_image");
					ImageView imageView = new ImageView(IndexNewDetailActivity.this);
					imageView.setLayoutParams(params);
					params.setMargins(0, 20, 0, 0);
					ImageLoader.getInstance().displayImage(UrlContants.GIFTIMGURL+img_url, imageView);
					msg_content_img.addView(imageView);
				}
			}
			msg_dw_laud.setText(good.getString("goods_favoritenum"));
			msg_dw_star.setText(good.getString("goods_collectnum"));
			msg_dw_comment.setText(good.getString("goods_commentnum"));
			msg_dw_share.setText(good.getString("goods_sharenum"));
			requestComments();
		}
	};
	
	/**
	 * 请求评论
	 */
	private AsyncHttpResponseHandler rel_getcomment = new EntityHandler<Comment>(Comment.class) {
		@Override
		public void onReadSuccess(List<Comment> list) {
			MyRequestDailog.closeDialog();
			msg_listview.setVisibility(View.VISIBLE);
			if(page == 1){comments.clear();}
			comments.addAll(list);
			commonAdapter.notifyDataSetChanged();
		}
	};

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.aci_shared_btn:
			//分享
			break;
		case R.id.layout_laud:
			clickfavorite();//点赞
			break;
		case R.id.bottom_comment:
			text = UIDialog.commentLayout(this, this);//写评论
			break;
		case R.id.bottom_comment_show:
			msg_listview.setSelection(2);//看评论
			break;
		case R.id.bottom_store:
			clickCollect();//收藏
			break;
		case R.id.bottom_mobile:
			UIDialog.callTelephone(this, "联系电话：15006598533", this);//打电话
			break;
		case R.id.comment_cancel:
			UIDialog.closeDialog();//取消评论
			break;
		case R.id.comment_submit:
			submitcomment();//提交评论
			break;
		case R.id.dialog_modif_1:
			//传入服务， parse（）解析号码,打电话
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:15006598533")));
			break;
		case R.id.dialog_modif_2:
			UIDialog.closeDialog();//取消拨号
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> viewgGroup, View view, int position, long checkId) {
		Comment comment = comments.get(position-2);
		startActivity(new Intent(IndexNewDetailActivity.this, CommentDetailActivity.class)
			.putExtra("goodid", good.getString("goods_id"))
			.putExtra("imgUrl", good.getString("goods_image"))
			.putExtra("content", good.getString("goods_jingle"))
			.putExtra("comment", comment));
	}

	private void onLoad() {
		msg_listview.stopRefresh();
		msg_listview.stopLoadMore();
		msg_listview.setRefreshTime("刚刚");
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				page = 1;
				requestComments();
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
				requestComments();
				onLoad();
			}
		}, 1000);
	}

	/** 用户点赞 */
	private void clickfavorite(){
		HttpUtils.clickfavorite(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				msg_dw_laud.setChecked(!msg_dw_laud.isChecked());
				int laudNum = Integer.valueOf(good.getString("goods_favoritenum"));
				if(!check_laud){
					msg_dw_laud.setText((msg_dw_laud.isChecked()?laudNum+1:laudNum)+"");
				}else{
					msg_dw_laud.setText((msg_dw_laud.isChecked()?laudNum:laudNum-1)+"");
				}
				Tools.toast(IndexNewDetailActivity.this, msg_dw_laud.isChecked()?"点赞成功":"取消点赞");
			}
			@Override
			public void onRecevieFailed(String status, JSONObject json) {
				msg_dw_laud.setChecked(!msg_dw_laud.isChecked());
				Tools.toast(IndexNewDetailActivity.this, "点赞失败!");
			}
		}, newId);
	}

	/** 用户收藏  */
	private void clickCollect(){
		HttpUtils.clickCollect(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				msg_dw_star.setChecked(!msg_dw_star.isChecked());
				int collectNum = Integer.valueOf(good.getString("goods_collectnum"));
				if(!check_star){
					msg_dw_star.setText((msg_dw_star.isChecked()?collectNum+1:collectNum)+"");
				}else{
					msg_dw_star.setText((msg_dw_star.isChecked()?collectNum:collectNum-1)+"");
				}
				Tools.toast(IndexNewDetailActivity.this, msg_dw_star.isChecked()?"收藏成功":"取消收藏");
			}
			@Override
			public void onRecevieFailed(String status, JSONObject json) {
				msg_dw_star.setChecked(false);
				Tools.toast(IndexNewDetailActivity.this, "请求失败!");
			}
		}, newId);
	}
	
	/** 用户提交评论 */
	private void submitcomment(){
		RequestParams urlparams = new RequestParams();
		urlparams.put("comment_supercommentid", "0");
		urlparams.put("comment_content", text.getText().toString().trim());
		urlparams.put("goods_id", newId);
		HttpUtils.submitcomment(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				int commentnum = Integer.valueOf(good.getString("goods_commentnum"))+1;
				msg_dw_comment.setText(commentnum + "");
				msg_dw_comment.setChecked(true);
				Tools.toast(IndexNewDetailActivity.this, "评论成功!");
				UIDialog.closeDialog();
				page = 1;
				requestComments();
			}
			@Override
			public void onRecevieFailed(String status, JSONObject json) {
				Tools.toast(IndexNewDetailActivity.this, "评论失败!");
			}
		}, urlparams);
	}
}