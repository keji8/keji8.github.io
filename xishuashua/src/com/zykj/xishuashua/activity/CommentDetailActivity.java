package com.zykj.xishuashua.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.zykj.xishuashua.utils.ImageUtil;
import com.zykj.xishuashua.utils.Tools;
import com.zykj.xishuashua.view.MyCommonTitle;
import com.zykj.xishuashua.view.UIDialog;
import com.zykj.xishuashua.view.XListView;
import com.zykj.xishuashua.view.XListView.IXListViewListener;

public class CommentDetailActivity extends BaseActivity implements IXListViewListener{

	/**
	 * 评论详情
	 */
	private static final String NUM = "2";//每页显示条数
	private int page = 1;

	private EditText text;
	private Comment comment;
	private MyCommonTitle myCommonTitle;
	private XListView msg_listview;
	private Handler mHandler = new Handler();
	private CommonAdapter<Comment> commonAdapter;
	private List<Comment> comments = new ArrayList<Comment>();
	private String goodid;
	private boolean check_laud;
	
	private ImageView super_image,good_image,comment_fav;
	private TextView comment_name,comment_time,comment_content,good_content;
	private TextView comment_num1,comment_num2;
	private TextView bottom_comment;
	
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
		comment_num1 = (TextView)findViewById(R.id.comment_num1);//评论
		comment_fav = (ImageView)findViewById(R.id.comment_fav);//点赞
		comment_num2 = (TextView)findViewById(R.id.comment_num2);//点赞
		bottom_comment = (TextView)findViewById(R.id.bottom_comment);//写评论
		setListener(comment_fav,comment_num2,bottom_comment);
		
		msg_listview = (XListView)findViewById(R.id.msg_listview);//评论列表
		commonAdapter = new CommonAdapter<Comment>(this, R.layout.ui_item_reply, comments) {
			@Override
			public void convert(ViewHolder holder, Comment comment) {
				holder.setText(R.id.comment_name, comment.getMember_name())//
					.setImageUrl(R.id.comment_images, UrlContants.ABATARURL+comment.getMember_avatar(), 10f)//
					.setText(R.id.comment_content, comment.getComment_content())//
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
		imgUrl = imgUrl.substring(0, imgUrl.indexOf("_"));
		String content = getIntent().getStringExtra("content");
		comment = (Comment)getIntent().getSerializableExtra("comment");

		ImageUtil.displayImage2Circle(super_image, UrlContants.ABATARURL+comment.getMember_avatar(), 10f, true);//父评论头像
		comment_name.setText(comment.getMember_name());//父评论昵称
		comment_time.setText(Tools.getTimeStr(comment.getComment_commenttime()));//父评论时间
		comment_content.setText(comment.getComment_content());//父评论内容
		ImageLoader.getInstance().displayImage(UrlContants.GIFTIMGURL+imgUrl+File.separator+imgUrl, good_image);//红包图片
		good_content.setText(content);//红包简介
		comment_num1.setText(comment.getComment_subcommentnum());;//评论
		check_laud = "1".equals(comment.getComment_favoratenum());
		comment_fav.setSelected(check_laud);///////////////////
		comment_num2.setText(comment.getComment_favoratenum());//点赞
		comment_num2.setSelected(check_laud);
		requestComments();
	}
	
	private void requestComments(){
		RequestParams params = new RequestParams();
		params.put("goods_id", goodid);
		params.put("comment_supercommentid", comment.getComment_id());
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
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.comment_fav:
			clickfavorite();//点赞
			break;
		case R.id.comment_num2:
			clickfavorite();//点赞
			break;
		case R.id.bottom_comment:
			text = UIDialog.commentLayout(this, this);//写评论
			break;
		case R.id.comment_cancel:
			UIDialog.closeDialog();//取消评论
			break;
		case R.id.comment_submit:
			submitcomment();//提交评论
			break;
		default:
			break;
		}
	}

	/** 用户点赞 */
	private void clickfavorite(){
		HttpUtils.clickCommentFavorite(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				comment_fav.setSelected(!comment_num2.isSelected());
				comment_num2.setSelected(!comment_num2.isSelected());
				int laudNum = Integer.valueOf(comment.getComment_favoratenum());
				if(!check_laud){
					comment_num2.setText((comment_fav.isSelected()?laudNum+1:laudNum)+"");
				}else{
					comment_num2.setText((comment_fav.isSelected()?laudNum:laudNum-1)+"");
				}
				Tools.toast(CommentDetailActivity.this, comment_fav.isSelected()?"点赞成功":"取消点赞");
			}
			@Override
			public void onRecevieFailed(String status, JSONObject json) {
				comment_fav.setSelected(!comment_num2.isSelected());
				comment_num2.setSelected(!comment_num2.isSelected());
				Tools.toast(CommentDetailActivity.this, "点赞失败!");
			}
		}, comment.getComment_id());
	}
	
	/** 用户提交评论 */
	private void submitcomment(){
		RequestParams urlparams = new RequestParams();
		urlparams.put("comment_supercommentid", comment.getComment_id());
		urlparams.put("comment_content", text.getText().toString().trim());
		urlparams.put("goods_id", goodid);
		HttpUtils.submitcomment(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				int commentnum = Integer.valueOf(comment.getComment_subcommentnum())+1;
				comment_num1.setText(commentnum+"");//评论
				Tools.toast(CommentDetailActivity.this, "评论成功!");
				UIDialog.closeDialog();
				page = 1;
				requestComments();
			}
			@Override
			public void onRecevieFailed(String status, JSONObject json) {
				Tools.toast(CommentDetailActivity.this, "评论失败!");
			}
		}, urlparams);
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
