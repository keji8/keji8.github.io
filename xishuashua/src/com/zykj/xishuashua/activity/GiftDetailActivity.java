package com.zykj.xishuashua.activity;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zykj.xishuashua.BaseActivity;
import com.zykj.xishuashua.R;
import com.zykj.xishuashua.adapter.CommonAdapter;
import com.zykj.xishuashua.adapter.ViewHolder;
import com.zykj.xishuashua.http.EntityHandler;
import com.zykj.xishuashua.http.HttpErrorHandler;
import com.zykj.xishuashua.http.HttpUtils;
import com.zykj.xishuashua.http.UrlContants;
import com.zykj.xishuashua.model.Comment;
import com.zykj.xishuashua.utils.CommonUtils;
import com.zykj.xishuashua.utils.StringUtil;
import com.zykj.xishuashua.utils.Tools;
import com.zykj.xishuashua.view.MyCheckBox;
import com.zykj.xishuashua.view.MyCommonTitle;
import com.zykj.xishuashua.view.MyRequestDailog;
import com.zykj.xishuashua.view.UIDialog;
import com.zykj.xishuashua.view.XListView;
import com.zykj.xishuashua.view.XListView.IXListViewListener;

public class GiftDetailActivity extends BaseActivity implements
		IXListViewListener, OnItemClickListener {

	/**
	 * 红包详情
	 */
	private static final String NUM = "10";// 每页显示条数
	private int page = 1;

	private MyCommonTitle myCommonTitle;
	private String goods_id;
	private TextView gift_message, message_title;
	private TextView msg_content;
	private CheckBox msg_dw_laud, msg_dw_star, msg_dw_comment, msg_dw_share, bottom_comment;
	private boolean check_laud, check_star;
	private ImageView bottom_comment_show, bottom_store, bottom_mobile;
	private RelativeLayout layout_laud;
	private XListView msg_listview;
	private CommonAdapter<Comment> commonAdapter;
	private View headView;
	private boolean flag = false;
	private EditText text;
	private Handler mHandler = new Handler();
	private List<Comment> comments = new ArrayList<Comment>();
	private TimeCount timer;

	private JSONObject good;
	private String html;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_gift_detail);
		goods_id = getIntent().getStringExtra("goods_id");// 红包Id
		// saw = getIntent().getStringExtra("saw");//红包Id

		initView();
		requestData();
	}

	/**
	 * 初始化页面
	 */
	private void initView() {
		myCommonTitle = (MyCommonTitle) findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("红包详情");
		myCommonTitle.setLisener(this, this);

		commonAdapter = new CommonAdapter<Comment>(this, R.layout.ui_item_comment, comments) {
			@Override
			public void convert(ViewHolder holder, Comment comment) {
				holder.setText(R.id.comment_name, comment.getMember_name())//
						.setImageUrl(R.id.comment_images,UrlContants.ABATARURL+ comment.getMember_avatar(), 10f)//
						.setText(R.id.comment_content,comment.getComment_content())//
						.setText(R.id.comment_num1,"(" + comment.getComment_subcommentnum() + ")")//
						.setText(R.id.comment_num2,"(" + comment.getComment_favoratenum() + ")")//
						.setText(R.id.comment_time,Tools.getTimeStr(comment.getComment_commenttime()));
			}
		};
		gift_message = (TextView) findViewById(R.id.gift_message);// 提示信息
		msg_listview = (XListView) findViewById(R.id.msg_listview);// 评论列表
		msg_listview.setAdapter(commonAdapter);
		msg_listview.setPullLoadEnable(true);
		msg_listview.setXListViewListener(this);
		msg_listview.setOnItemClickListener(this);

		headView = LayoutInflater.from(this).inflate(R.layout.ui_gift_header, null);
		msg_listview.addHeaderView(headView);

		message_title = (TextView) headView.findViewById(R.id.message_title);// 标题
		layout_laud = (RelativeLayout) headView.findViewById(R.id.layout_laud);// 图片展示
		msg_dw_laud = (CheckBox) headView.findViewById(R.id.msg_dw_laud);// 喜欢
		msg_dw_star = (CheckBox) headView.findViewById(R.id.msg_dw_star);// 收藏
		msg_dw_star.setClickable(false);// 不能点击
		msg_dw_comment = (CheckBox) headView.findViewById(R.id.msg_dw_comment);// 评论
		msg_dw_comment.setClickable(false);// 不能点击
		msg_dw_share = (CheckBox) headView.findViewById(R.id.msg_dw_share);// 分享
		msg_dw_share.setClickable(false);// 不能点击
		msg_content = (TextView) headView.findViewById(R.id.msg_content);// 内容

		bottom_comment = (MyCheckBox) findViewById(R.id.bottom_comment);// 添加评论
		bottom_comment_show = (ImageView) findViewById(R.id.bottom_comment_show);// 查看评论
		bottom_store = (ImageView) findViewById(R.id.bottom_store);// 收藏
		bottom_mobile = (ImageView) findViewById(R.id.bottom_mobile);// 拨号

		setListener(layout_laud, bottom_comment, bottom_comment_show, bottom_store, bottom_mobile);
	}

	// 因为从网上下载图片是耗时操作 所以要开启新线程
	Thread t = new Thread(new Runnable() {
		Message msg = Message.obtain();
		@Override
		public void run() {
			/**
			 * 要实现图片的显示需要使用Html.fromHtml的一个重构方法：public static Spanned fromHtml
			 * (String source, Html.ImageGetterimageGetter, Html.TagHandler
			 * tagHandler)其中Html.ImageGetter是一个接口，我们要实现此接口，在它的getDrawable
			 * (String source)方法中返回图片的Drawable对象才可以。
			 */
			ImageGetter imageGetter = new ImageGetter() {
				@Override
				public Drawable getDrawable(String source) {
					URL url;
					Drawable drawable = null;
					try {
						url = new URL(source);
						drawable = Drawable.createFromStream(url.openStream(), null);
						drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
					} catch (Exception e) {
						e.printStackTrace();
					}
					return drawable;
				}
			};
			CharSequence test = Html.fromHtml(html, imageGetter, null);
			msg.what = 0x101;
			msg.obj = test;
			handler.sendMessage(msg);
		}
	});

	/**异步加载红包体*/
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x101) {
				msg_content.setText((CharSequence) msg.obj);
				MyRequestDailog.closeDialog();
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 请求红包详情和评论
	 */
	private void requestData() {
		RequestParams params = new RequestParams();
		params.put("goods_id", goods_id);
		HttpUtils.getGoodDetail(rel_getGoodDetail, params);
		requestComments();
	}

	private void requestComments() {
		RequestParams params = new RequestParams();
		params.put("goods_id", goods_id);
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
			// gift_message.setText("恭喜你，浏览15之后将会获得商家红包2元！");
			long continueTime = Long.parseLong(StringUtil.toString(good.getString("goods_marketprice"), "0"));
			long startTime = Long.parseLong(StringUtil.toString(good.getString("goods_selltime"), "0"));
			long seconds = startTime + continueTime - System.currentTimeMillis() / 1000;
			if ("1".equals(good.getString("has_rabbed"))) {
				gift_message.setText("红包已抢过，分享也可以拿红包！");
			} else {
				if (continueTime == 0 && !"news".equals(good.getString("store_name"))) {
					timer = new TimeCount(15000, 1000);// 构造CountDownTimer对象
					timer.start();
				}
				if (continueTime > 0 && !"news".equals(good.getString("store_name"))) {
					if (seconds > 1) {
						timer = new TimeCount(15000, 1000);// 构造CountDownTimer对象
						timer.start();
					} else {
						gift_message.setText("红包已过期，分享也可以拿红包！");
					}
				}
			}
			message_title.setText(good.getString("goods_name"));
			html = good.getString("goods_body");
			MyRequestDailog.showDialog(GiftDetailActivity.this, "");
			t.start();
			msg_dw_laud.setText(good.getString("goods_favoritenum"));
			msg_dw_star.setText(good.getString("goods_collectnum"));
			msg_dw_comment.setText(good.getString("goods_commentnum"));
			msg_dw_share.setText(good.getString("goods_sharenum"));
		}
	};

	/**
	 * 请求评论
	 */
	private AsyncHttpResponseHandler rel_getcomment = new EntityHandler<Comment>(
			Comment.class) {
		@Override
		public void onReadSuccess(List<Comment> list) {
			if (page == 1) {
				comments.clear();
			}
			comments.addAll(list);
			commonAdapter.notifyDataSetChanged();
		}
	};

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.aci_back_btn:
			long continueTime = Long.parseLong(StringUtil.toString(
					good.getString("goods_marketprice"), "0"));
			long startTime = Long.parseLong(StringUtil.toString(
					good.getString("goods_selltime"), "0"));
			long seconds = startTime + continueTime
					- System.currentTimeMillis() / 1000;
			if ("1".equals(good.getString("has_rabbed"))) {
				flag = true;
			} else {
				if (continueTime > 0
						&& !"news".equals(good.getString("store_name"))) {
					if (seconds < 1) {
						flag = true;
					}
				}
			}
			if (!flag) {
				CommonUtils.exitGift(4, this);
			} else {
				finish();
			}
			break;
		case R.id.aci_shared_btn:
			// 分享
			CommonUtils
					.showShare(
							this,
							getString(R.string.app_name),
							"我从" + good.getString("goods_name") + "那里获得了"
									+ good.getString("goods_price") + "元的红包",
							"http://dashboard.mob.com/Uploads/1b692f6c9fceaf93c407afd889c36090.png",
							null);
			break;
		case R.id.layout_laud:
			clickfavorite();// 点赞
			break;
		case R.id.bottom_comment:
			text = UIDialog.commentLayout(this, this);// 写评论
			break;
		case R.id.bottom_comment_show:
			msg_listview.setSelection(2);// 看评论
			break;
		case R.id.bottom_store:
			clickCollect();// 收藏
			break;
		case R.id.bottom_mobile:
			UIDialog.callTelephone(this, "联系电话：15006598533", this);// 打电话
			break;
		case R.id.comment_cancel:
			UIDialog.closeDialog();// 取消评论
			break;
		case R.id.comment_submit:
			submitcomment();// 提交评论
			break;
		case R.id.dialog_modif_1:
			// 传入服务， parse（）解析号码,打电话
			startActivity(new Intent(Intent.ACTION_CALL,
					Uri.parse("tel:15006598533")));
			break;
		case R.id.dialog_modif_2:
			UIDialog.closeDialog();// 取消拨号
			break;
		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// gift_message.setText("红包获取失败!");
		flag = true;
		if (timer != null)
			timer.cancel();
	}

	@Override
	public void onItemClick(AdapterView<?> viewgGroup, View view, int position,
			long checkId) {
		Comment comment = comments.get(position - 2);
		startActivity(new Intent(GiftDetailActivity.this,
				CommentDetailActivity.class)
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
	private void clickfavorite() {
		HttpUtils.clickfavorite(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				msg_dw_laud.setChecked(!msg_dw_laud.isChecked());
				int laudNum = Integer.valueOf(good
						.getString("goods_favoritenum"));
				if (!check_laud) {
					msg_dw_laud.setText((msg_dw_laud.isChecked() ? laudNum + 1
							: laudNum) + "");
				} else {
					msg_dw_laud.setText((msg_dw_laud.isChecked() ? laudNum
							: laudNum - 1) + "");
				}
				Tools.toast(GiftDetailActivity.this,
						msg_dw_laud.isChecked() ? "点赞成功" : "取消点赞");
			}

			@Override
			public void onRecevieFailed(String status, JSONObject json) {
				msg_dw_laud.setChecked(!msg_dw_laud.isChecked());
				Tools.toast(GiftDetailActivity.this, "点赞失败!");
			}
		}, goods_id);
	}

	/** 用户收藏 */
	private void clickCollect() {
		HttpUtils.clickCollect(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				msg_dw_star.setChecked(!msg_dw_star.isChecked());
				int collectNum = Integer.valueOf(good
						.getString("goods_collectnum"));
				if (!check_star) {
					msg_dw_star.setText((msg_dw_star.isChecked() ? collectNum + 1
							: collectNum)
							+ "");
				} else {
					msg_dw_star.setText((msg_dw_star.isChecked() ? collectNum
							: collectNum - 1) + "");
				}
				Tools.toast(GiftDetailActivity.this,
						msg_dw_star.isChecked() ? "收藏成功" : "取消收藏");
			}

			@Override
			public void onRecevieFailed(String status, JSONObject json) {
				msg_dw_star.setChecked(false);
				Tools.toast(GiftDetailActivity.this, "请求失败!");
			}
		}, goods_id);
	}

	/** 用户提交评论 */
	private void submitcomment() {
		RequestParams urlparams = new RequestParams();
		urlparams.put("comment_supercommentid", "0");
		urlparams.put("comment_content", text.getText().toString().trim());
		urlparams.put("goods_id", goods_id);
		HttpUtils.submitcomment(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				int commentnum = Integer.valueOf(good
						.getString("goods_commentnum")) + 1;
				msg_dw_comment.setText(commentnum + "");
				msg_dw_comment.setChecked(true);
				Tools.toast(GiftDetailActivity.this, "评论成功!");
				UIDialog.closeDialog();
				page = 1;
				requestComments();
			}

			@Override
			public void onRecevieFailed(String status, JSONObject json) {
				Tools.toast(GiftDetailActivity.this, "评论失败!");
			}
		}, urlparams);
	}

	// 退出操作
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		long continueTime = Long.parseLong(StringUtil.toString(
				good.getString("goods_marketprice"), "0"));
		long startTime = Long.parseLong(StringUtil.toString(
				good.getString("goods_selltime"), "0"));
		long seconds = startTime + continueTime - System.currentTimeMillis()
				/ 1000;
		if ("1".equals(good.getString("has_rabbed"))) {
			flag = true;
		} else {
			if (continueTime > 0
					&& !"news".equals(good.getString("store_name"))) {
				if (seconds < 1) {
					flag = true;
				}
			}
		}
		if (!flag) {
			CommonUtils.exitGift(keyCode, this);
		}
		return super.onKeyDown(keyCode, event);
	}

	/** 定义一个倒计时的内部类 */
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			RequestParams enveparams = new RequestParams();
			enveparams.put("goods_id", goods_id);
			enveparams.put("store_id", good.getString("store_id"));
			enveparams.put("points", good.getString("goods_price"));
			HttpUtils.getenvePoints(new HttpErrorHandler() {
				@Override
				public void onRecevieSuccess(JSONObject json) {
					gift_message.setText("您获得了" + good.getString("goods_price")
							+ "元红包，分享也可以拿红包！");
					flag = true;
				}

				@Override
				public void onRecevieFailed(String status, JSONObject json) {
					gift_message.setText("红包获取失败,请稍后再试");
					flag = true;
				}
			}, enveparams);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			long seconds = millisUntilFinished / 1000;
			gift_message.setText("恭喜你，浏览" + seconds + "秒之后将会获得商家红包"
					+ good.getString("goods_price") + "元！");
		}
	}
}
