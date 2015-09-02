package com.zykj.xishuashua.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zykj.xishuashua.BaseActivity;
import com.zykj.xishuashua.BaseApp;
import com.zykj.xishuashua.R;
import com.zykj.xishuashua.http.AbstractHttpHandler;
import com.zykj.xishuashua.http.HttpErrorHandler;
import com.zykj.xishuashua.http.HttpUtils;
import com.zykj.xishuashua.utils.StringUtil;
import com.zykj.xishuashua.utils.Tools;

@SuppressLint("HandlerLeak")
public class LoginActivity extends BaseActivity{
	
	//private static final String TAG = LoginActivity.class.getName();
	
	private ImageView back;
	private EditText uu_username,uu_password;

	public static String mAppid;
	private Tencent mTencent;
	private final String APP_ID = "1104789886";//测试时使用，真正发布的时候要换成自己的APP_ID()
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_user_login);
        //device_token = UmengRegistrar.getRegistrationId(this);
		
		initView();
	}

	@Override
	protected void onStart() {
		//final Context context = LoginActivity.this;
		//final Context ctxContext = context.getApplicationContext();
        //这里的APP_ID请换成你应用申请的APP_ID，我这里使用的是DEMO中官方提供的测试APP_ID 222222
		//mQQAuth = QQAuth.createInstance(APP_ID, this.getApplicationContext());
        //第一个参数就是上面所说的申请的APPID，第二个是全局的Context上下文，这句话实现了调用QQ登录
		mTencent = Tencent.createInstance(APP_ID, this);
		super.onStart();
	}

	private void initView() {
		back = (ImageView)findViewById(R.id.aci_back_btn);

		uu_username = (EditText)findViewById(R.id.uu_username);//用户名
		uu_password = (EditText)findViewById(R.id.uu_password);//密码
		Button app_league_in = (Button)findViewById(R.id.app_league_in);//商家加盟
		Button login_in = (Button)findViewById(R.id.app_login_in);//登录

		TextView forgetpwd = (TextView)findViewById(R.id.forgetpwd);//忘记密码
		TextView login_register = (TextView)findViewById(R.id.login_register);//没有账号?
		TextView login_quickly = (TextView)findViewById(R.id.login_quickly);//手机号快速注册
		
		TextView login_weibo = (TextView)findViewById(R.id.login_weibo);//微博
		TextView login_qq = (TextView)findViewById(R.id.login_qq);//qq
		TextView login_weixin = (TextView)findViewById(R.id.login_weixin);//微信
		
		setListener(back,app_league_in,forgetpwd,login_register,login_quickly,login_weibo,login_qq,login_weixin,app_league_in,login_in);//点击事件
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.aci_back_btn:
			startActivity(new Intent(this, MainActivity.class));
			MainActivity.switchTabActivity(2);
			break;
		case R.id.app_league_in://商家加盟
			break;
		case R.id.app_login_in://登录
	        String username=uu_username.getText().toString().trim();
	        final String password=uu_password.getText().toString().trim();
	        if(StringUtil.isEmpty(username) || StringUtil.isEmpty(password)){
	            Tools.toast(LoginActivity.this, "请先填写账号和密码");
	            return;
	        }
			RequestParams params = new RequestParams();
			params.put("login_name", username);
			params.put("login_password", password);
			params.put("lati", BaseApp.getModel().getLatitude());
			params.put("longi", BaseApp.getModel().getLongitude());
	        HttpUtils.login(new AbstractHttpHandler() {
				@Override
				public void onJsonSuccess(JSONObject json) {
			       String status= json.getString("result");
			        if(TextUtils.isEmpty(status) || !status.equals("1")){
			            Tools.toast(LoginActivity.this, "账号密码错误");
			        }else{
						JSONObject data = json.getJSONObject("data");
						BaseApp.getModel().setAvatar(data.getString("member_avatar"));//头像
						BaseApp.getModel().setMobile(data.getString("member_mobile"));//手机号
						BaseApp.getModel().setPassword(password);//登录密码
						BaseApp.getModel().setUserid(data.getString("member_id"));//用户Id
						BaseApp.getModel().setUsername(data.getString("member_name"));//登录账号
				        submitDeviceToken("device_token", data.getString("member_id"));
			        }
				}
			}, params);
			break;
		case R.id.forgetpwd://忘记密码
			startActivity(new Intent(LoginActivity.this,UserRegisterActivity.class).putExtra("type", "forget"));
			break;
		case R.id.login_register://没有账号?
			startActivity(new Intent(LoginActivity.this,UserRegisterActivity.class).putExtra("type", "register"));
			break;
		case R.id.login_quickly://手机号快速注册
			startActivity(new Intent(LoginActivity.this,UserRegisterActivity.class).putExtra("type", "register"));
			break;
		case R.id.login_weibo://微博
			break;
		case R.id.login_qq://qq
			/**通过这句代码，SDK实现了QQ的登录，这个方法有三个参数，第一个参数是context上下文，第二个参数SCOPO 是一个String类型的字符串，表示一些权限 
			官方文档中的说明：应用需要获得哪些API的权限，由“，”分隔。例如：SCOPE = “get_user_info,add_t”；所有权限用“all”  
			第三个参数，是一个事件监听器，IUiListener接口的实例，这里用的是该接口的实现类 */
			mTencent.login(this, "all", iuiListener);
			break;
		case R.id.login_weixin://微信
			break;
		}
	}

    /**当自定义的监听器实现IUiListener接口后，必须要实现接口的三个方法，
     * onComplete  onCancel onError 
     *分别表示第三方登录成功，取消 ，错误。*/
	private IUiListener iuiListener = new IUiListener() {
		@Override
		public void onComplete(Object response) {
			//Tools.showResultDialog(LoginActivity.this, response.toString(), "登录成功");
			//updateUserInfo();
			//获得的数据是JSON格式的，获得你想获得的内容
            //如果你不知道你能获得什么，看一下下面的LOG
            //String openidString = JSONObject.parseObject(response.toString()).getString("openid");
            //Log.e(TAG, "-------------"+openidString);
			/**获取一些QQ的基本信息，比如昵称，头像等  */
            QQToken qqToken = mTencent.getQQToken();
            UserInfo info = new UserInfo(LoginActivity.this, qqToken);
            updateUserInfo(info);
            //这样我们就拿到这个类了，之后的操作就跟上面的一样了，同样是解析JSON 
		}

		@Override
		public void onError(UiError e) {
			Tools.toastMessage(LoginActivity.this, "onError: " + e.errorDetail);
		}

		@Override
		public void onCancel() {
			Tools.toastMessage(LoginActivity.this, "onCancel: ");
		}
	};

	private void updateUserInfo(UserInfo info) {
		info.getUserInfo(new IUiListener(){
			@Override
			public void onCancel() {}
			@Override
			public void onComplete(final Object response) {
				Message msg = new Message();
				msg.obj = response;
				msg.what = 0;
				mHandler.sendMessage(msg);
                /**
                 * 由于图片需要下载所以这里使用了线程，如果是想获得其他文字信息直接
                 * 在mHandler里进行操作
                 */
//				new Thread() {
//					@Override
//					public void run() {
//						JSONObject json = (JSONObject) response;
//						if (json.getString("figureurl_qq_2") != null) {
//							Bitmap bitmap = null;
//							bitmap = ImageUtil.getbitmap(json.getString("figureurl_qq_2"));
//							Message msg = new Message();
//							msg.obj = bitmap;
//							msg.what = 1;
//							mHandler.sendMessage(msg);
//						}
//					}
//				}.start();
			}
			@Override
			public void onError(UiError uiError) {
				Log.e("onError", uiError.errorMessage);
				Tools.toastMessage(LoginActivity.this, uiError.errorMessage);
			}
		});
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				JSONObject response = JSONObject.parseObject(msg.obj.toString());
				RequestParams params = new RequestParams();
				params.put("openid", mTencent.getOpenId());
				params.put("member_name", response.getString("nickname"));
				params.put("member_sex", "男".equals(response.getString("gender"))?"1":"2");
				params.put("member_avatar", response.getString("figureurl_qq_2"));
				HttpUtils.getAllInterests(new HttpErrorHandler() {
					@Override
					public void onRecevieSuccess(JSONObject json) {
						JSONObject data = json.getJSONObject("data");
						BaseApp.getModel().setAvatar(data.getString("member_avatar"));
						BaseApp.getModel().setMobile(data.getString("member_mobile"));//手机号
						BaseApp.getModel().setMoney(data.getString("member_id"));//红包金额
						BaseApp.getModel().setNumber(data.getString("member_id"));//红包个数
//						BaseApp.getModel().setPassword(password);//登录密码
						BaseApp.getModel().setUserid(data.getString("member_id"));//用户Id
						BaseApp.getModel().setUsername(data.getString("member_name"));//登录账号
				        submitDeviceToken("device_token", data.getString("member_id"));
					}
				});
				//Log.e("response", msg.obj.toString());
//				if (response.getString("nickname") != null) {
//					BaseApp.getModel().setMobile(response.getString("nickname"));
//				}
//				if (response.getString("figureurl_qq_2") != null) {
//					BaseApp.getModel().setAvatar(response.getString("figureurl_qq_2"));
//				}
			} else if (msg.what == 1) {
//                Bitmap bitmap = (Bitmap) msg.obj;
//                mUserLogo.setImageBitmap(bitmap);
//                mUserLogo.setVisibility(View.VISIBLE);
            }  
		}
	};
	
	private void submitDeviceToken(String userid, String member_id){
		//Tools.toast(this, device_token);
		setResult(Activity.RESULT_OK, getIntent().putExtra("member_id", member_id));
		finish();
	}
}
