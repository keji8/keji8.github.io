package com.zykj.xishuashua.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;

import com.zykj.xishuashua.BaseActivity;
import com.zykj.xishuashua.R;
import com.zykj.xishuashua.adapter.CommonAdapter;
import com.zykj.xishuashua.adapter.ViewHolder;
import com.zykj.xishuashua.model.Comment;
import com.zykj.xishuashua.utils.Tools;
import com.zykj.xishuashua.view.MyCommonTitle;
import com.zykj.xishuashua.view.XListView;

public class CommentDetailActivity extends BaseActivity{
	
	private MyCommonTitle myCommonTitle;
	private XListView msg_listview;
	
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

		msg_listview = (XListView)findViewById(R.id.msg_listview);//评论列表
	}
	
	private void requestData(){
		//String imgUrl = getIntent().getStringExtra("imgUrl");
		Comment comment = (Comment)getIntent().getSerializableExtra("comment");
		Tools.toast(CommentDetailActivity.this, comment.getMember_name());
		List<Comment> comments = new ArrayList<Comment>();
//		comments.add(new Comment("张飞", "这个红包抢的好多啊", "12", "33"));
//		comments.add(new Comment("刘备", "我每天都能抢到", "66", "56"));
//		comments.add(new Comment("刘亦菲", "没事的时候可以抢着玩", "22", "8"));
//		comments.add(new Comment("戚薇", "我今天抢了好多，赶紧来抢吧", "37", "18"));
//		comments.add(new Comment("范冰冰", "每天抢得红包够吃饭的，真省钱", "79", "58"));
//		comments.add(new Comment("刘涛", "这个红包是抢得最快的一个", "41", "20"));
//		comments.add(new Comment("谭静", "好长时间没抢了，感觉美美哒", "102", "178"));
//		comments.add(new Comment("孙杨", "抢红包也能赚钱，虽然不多，但是轻而易举就能得到，大家快来抢", "2", "6"));
		msg_listview.setAdapter(new CommonAdapter<Comment>(this, R.layout.ui_item_reply, comments) {
			@Override
			public void convert(ViewHolder holder, Comment comment) {
				holder.setText(R.id.comment_name, comment.getMember_name())//
					.setText(R.id.comment_content, comment.getComment_content());
			}
		});
	}

	@Override
	public void onClick(View v) {
		
	}
}
