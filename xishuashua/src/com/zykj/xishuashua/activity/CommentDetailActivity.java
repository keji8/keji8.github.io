package com.zykj.xishuashua.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.xishuashua.BaseActivity;
import com.zykj.xishuashua.R;
import com.zykj.xishuashua.adapter.CommonAdapter;
import com.zykj.xishuashua.adapter.ViewHolder;
import com.zykj.xishuashua.http.EntityHandler;
import com.zykj.xishuashua.http.HttpUtils;
import com.zykj.xishuashua.http.UrlContants;
import com.zykj.xishuashua.model.Comment;
import com.zykj.xishuashua.utils.ImageUtil;
import com.zykj.xishuashua.utils.Tools;
import com.zykj.xishuashua.view.MyCheckBox;
import com.zykj.xishuashua.view.MyCommonTitle;
import com.zykj.xishuashua.view.XListView;
import com.zykj.xishuashua.view.XListView.IXListViewListener;

public class CommentDetailActivity extends BaseActivity implements IXListViewListener{

	/**
	 * 评论详情
	 */
	private static final String NUM = "2";//每页显示条数
	private int page = 1;
	
	private MyCommonTitle myCommonTitle;
	private XListView msg_listview;
	private Handler mHandler = new Handler();
	private CommonAdapter<Comment> commonAdapter;
	private List<Comment> comments = new ArrayList<Comment>();
	private String goodid;
	
	private ImageView super_image,good_image;
	private TextView comment_name,comment_time,comment_content,good_content;
	private MyCheckBox button1,button2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_comment_detail);
		
		initView();
		requestData();
	}

	private void initView() {
		myCommonTitle = (MyCommonTitle)findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("评论");

		super_image = (ImageView)findViewById(R.id.super_image);//父评论头像
		comment_name = (TextView)findViewById(R.id.comment_name);//父评论昵称
		comment_time = (TextView)findViewById(R.id.comment_time);//父评论时间
		comment_content = (TextView)findViewById(R.id.comment_content);//父评论内容
		good_image = (ImageView)findViewById(R.id.good_image);//红包图片
		good_content = (TextView)findViewById(R.id.good_content);//红包简介
		button1 = (MyCheckBox)findViewById(R.id.button1);//评论
		button2 = (MyCheckBox)findViewById(R.id.button2);//点赞
		
		msg_listview = (XListView)findViewById(R.id.msg_listview);//评论列表
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
		msg_listview.setAdapter(commonAdapter);
		msg_listview.setDividerHeight(0);
		msg_listview.setPullLoadEnable(true);
		msg_listview.setXListViewListener(this);
	}
	
	private void requestData(){
		goodid = getIntent().getStringExtra("goodid");
		String imgUrl = getIntent().getStringExtra("imgUrl");
		String content = getIntent().getStringExtra("content");
		Comment comment = (Comment)getIntent().getSerializableExtra("comment");

		ImageUtil.displayImage2Circle(super_image, UrlContants.ABATARURL+comment.getMember_avatar(), 10f, true);//父评论头像
		comment_name.setText(comment.getMember_name());//父评论昵称
		comment_time.setText(Tools.getTimeStr(comment.getComment_commenttime()));//父评论时间
		comment_content.setText(comment.getComment_content());//父评论内容
		ImageLoader.getInstance().displayImage(UrlContants.GIFTIMGURL+imgUrl, good_image);//红包图片
		good_content.setText(content);//红包简介
		button1.setText(comment.getComment_subcommentnum());;//评论
		button2.setText(comment.getComment_favoratenum());//点赞
		requestComments();
	}
	
	private void requestComments(){
		RequestParams params = new RequestParams();
		params.put("goods_id", goodid);
		params.put("comment_supercommentid", "0");
    	params.put("page", String.valueOf(page));
    	params.put("per_page", NUM);
		HttpUtils.getcomment(rel_getcomment, params);
	}
	
	/**
	 * 请求评论
	 */
	private AsyncHttpResponseHandler rel_getcomment = new EntityHandler<Comment>(Comment.class) {
		@Override
		public void onReadSuccess(List<Comment> list) {
			if(page == 1){comments.clear();}
			comments.addAll(list);
			commonAdapter.notifyDataSetChanged();
		}
	};

	@Override
	public void onClick(View v) {
		
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
}
