package com.zykj.xishuashua.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.xishuashua.BaseActivity;
import com.zykj.xishuashua.BaseApp;
import com.zykj.xishuashua.R;
import com.zykj.xishuashua.http.AbstractHttpHandler;
import com.zykj.xishuashua.http.HttpErrorHandler;
import com.zykj.xishuashua.http.HttpUtils;
import com.zykj.xishuashua.http.UrlContants;
import com.zykj.xishuashua.utils.Tools;
import com.zykj.xishuashua.view.MyAlertView;
import com.zykj.xishuashua.view.MyCommonTitle;
import com.zykj.xishuashua.view.NumericWheelAdapter;
import com.zykj.xishuashua.view.OnWheelChangedListener;
import com.zykj.xishuashua.view.RoundImageView;
import com.zykj.xishuashua.view.UIDialog;
import com.zykj.xishuashua.view.WheelView;

public class UserInfoActivity extends BaseActivity{

	private String timeString;//上传头像的字段
	private MyCommonTitle myCommonTitle;
	private RoundImageView rv_me_avatar;
//	private ImageView titleBack;
	private LinearLayout layout_age,layout_sex,layout_address,layout_updpwd;
	private TextView user_age,user_sex,user_address;
	private boolean flag = false;
	private Button app_login_out;
	private int selectval;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_user_info);
		
		initView();
		requestData();
	}

	/**
	 * 初始化页面
	 */
	private void initView() {
		myCommonTitle = (MyCommonTitle)findViewById(R.id.aci_mytitle);
		myCommonTitle.setTitle("用户");
//		titleBack = (ImageView)findViewById(R.id.aci_back_btn);
//		
//		titleBack.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				setResult(Activity.RESULT_OK, getIntent().putExtra("login_out", "clear"));
//				finish();
//			}
//		});

		rv_me_avatar = (RoundImageView)findViewById(R.id.rv_me_avatar);//头像
		layout_age = (LinearLayout)findViewById(R.id.layout_age);//年龄
		user_age = (TextView)findViewById(R.id.user_age);//年龄
		layout_sex = (LinearLayout)findViewById(R.id.layout_sex);//性别
		user_sex = (TextView)findViewById(R.id.user_sex);//性别
		layout_address = (LinearLayout)findViewById(R.id.layout_address);//常驻地
		user_address = (TextView)findViewById(R.id.user_address);//常驻地
		layout_updpwd = (LinearLayout)findViewById(R.id.layout_updpwd);//修改密码
		app_login_out = (Button)findViewById(R.id.app_login_out);//退出
		
		setListener(rv_me_avatar, layout_age, layout_sex, layout_address, layout_updpwd, app_login_out);
	}

	/**
	 * 请求服务器数据---首页
	 */
	private void requestData(){
		RequestParams params = new RequestParams();
		params.put("login_name", BaseApp.getModel().getUsername());
		params.put("login_password", BaseApp.getModel().getPassword());
		params.put("lati", BaseApp.getModel().getLatitude());
		params.put("longi", BaseApp.getModel().getLongitude());
        HttpUtils.login(new AbstractHttpHandler() {
			@Override
			public void onJsonSuccess(JSONObject json) {
		       String status= json.getString("result");
		        if(TextUtils.isEmpty(status) || !status.equals("1")){
		            Tools.toast(UserInfoActivity.this, "账号密码错误");
		        }else{
					JSONObject data = json.getJSONObject("data");
					String sex = data.getString("member_sex");
					user_age.setText(data.getString("member_areaid"));//年龄
					user_sex.setText("1".equals(sex)?"男":"2".equals(sex)?"女":"选择");//性别
					user_address.setText(data.getString("member_areainfo"));//常驻地
					ImageLoader.getInstance().displayImage(UrlContants.ABATARURL+BaseApp.getModel().getAvatar(), rv_me_avatar);//用户头像
		        }
			}
		}, params);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.rv_me_avatar:
			flag = true;
			UIDialog.ForThreeBtn(this, new String[]{"相册", "拍照", "取消"}, this);
			break;
		case R.id.layout_age:
			showDateTimePicker();
			break;
		case R.id.layout_sex:
			flag = false;
			UIDialog.ForThreeBtn(this, new String[]{"男", "女", "取消"}, this);
			//showDateTimePicker("请选择性别", "sex");
			break;
		case R.id.layout_address:
			break;
		case R.id.layout_updpwd:
			startActivity(new Intent(UserInfoActivity.this, UserUpdPwdActivity.class)
				.putExtra("username", BaseApp.getModel().getUsername()).putExtra("type", "forget"));
			break;
		case R.id.app_login_out:
			BaseApp.getModel().clear();
			setResult(Activity.RESULT_OK, getIntent().putExtra("login_out", "clear"));
			finish();
			break;
		case R.id.dialog_modif_1:
			if(flag){
				/*选择相册*/
				UIDialog.closeDialog();
				/**
				 * 刚开始，我自己也不知道ACTION_PICK是干嘛的，后来直接看Intent源码，
				 * 可以发现里面很多东西，Intent是个很强大的东西，大家一定仔细阅读下
				 */
				Intent photoIntent = new Intent(Intent.ACTION_PICK, null);
				/**
				 * 下面这句话，与其它方式写是一样的效果，如果：
				 * intent.setData(MediaStore.Images
				 * .Media.EXTERNAL_CONTENT_URI);
				 * intent.setType(""image/*");设置数据类型
				 * 如果朋友们要限制上传到服务器的图片类型时可以直接写如
				 * ："image/jpeg 、 image/png等的类型"
				 * 这个地方小马有个疑问，希望高手解答下：就是这个数据URI与类型为什么要分两种形式来写呀？有什么区别？
				 */
				photoIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(photoIntent, 1);
			}else{
				updateCurrentUserSex("1");
			}
			break;
		case R.id.dialog_modif_2:
			if(flag){
				/*点击拍照*/
				UIDialog.closeDialog();
				/**
				 * 下面这句还是老样子，调用快速拍照功能，至于为什么叫快速拍照，大家可以参考如下官方
				 * 文档，you_sdk_path/docs/guide/topics/media/camera.html
				 * 我刚看的时候因为太长就认真看，其实是错的，这个里面有用的太多了，所以大家不要认为
				 * 官方文档太长了就不看了，其实是错的，这个地方小马也错了，必须改正
				 */
				Date date = new Date(System.currentTimeMillis());
				SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMddHHmmss", new Locale("zh", "CN"));
				timeString = dateFormat.format(date);
				createSDCardDir();
				Intent shootIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				shootIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory()
								+ "/DCIM/Camera", timeString + ".jpg")));
				startActivityForResult(shootIntent, 2);
			}else{
				updateCurrentUserSex("2");
			}
			break;
		case R.id.dialog_modif_3:
			UIDialog.closeDialog();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			/*如果是直接从相册获取*/
			try {
				startPhotoZoom(data.getData());
			} catch (Exception e) {
				Toast.makeText(this, "您没有选择任何照片", Toast.LENGTH_LONG).show();
			}
			break;
		case 2:
			/*如果是调用相机拍照时
			File temp = new File(Environment.getExternalStorageDirectory() + "/xiaoma.jpg");
			给图片设置名字和路径*/
			File temp = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/DCIM/Camera/" + timeString + ".jpg");
			startPhotoZoom(Uri.fromFile(temp));
			break;
		case 3:
			/**
			 * 取得裁剪后的图片
			 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃
			 * 当前功能时，会报NullException，小马只 在这个地方加下，大家可以根据不同情况在合适的 地方做判断处理类似情况
			 */
			if (data != null) {
				setPicToView(data);
			}
			break;
		case 11:
			/*成功登陆之后*/
			if (data != null) {requestData();}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void createSDCardDir() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			// 创建一个文件夹对象，赋值为外部存储器的目录
			File sdcardDir = Environment.getExternalStorageDirectory();
			// 得到一个路径，内容是sdcard的文件夹路径和名字
			String path = sdcardDir.getPath() + "/DCIM/Camera";
			File path1 = new File(path);
			if (!path1.exists()) {
				// 若不存在，创建目录，可以在应用启动的时候创建
				path1.mkdirs();

			}
		}
	}

	/**
	 * 裁剪图片方法实现
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		/**
		 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的，小马不懂C C++
		 * 这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么 制做的了...吼吼
		 */
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			//Drawable drawable = new BitmapDrawable(photo);
			/*下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上 传到服务器，QQ头像上传采用的方法跟这个类似*/
			savaBitmap(photo);
			// avatar_head_image.setBackgroundDrawable(drawable);
		}
	}

	/**
	 * 将剪切后的图片保存到本地图片上！
	 */
	public void savaBitmap(Bitmap bitmap) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMddHHmmss", new Locale("zh", "CN"));
		String cutnameString = dateFormat.format(date);
		String filename = Environment.getExternalStorageDirectory().getPath() + "/" + cutnameString + ".jpg";
		File f = new File(filename);
		FileOutputStream fOut = null;
		try {
			f.createNewFile();
			fOut = new FileOutputStream(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);// 把Bitmap对象解析成流
		try {
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		rv_me_avatar.setImageBitmap(bitmap);
		uploadAvatar(f);
	}

	/**
	 * 更新服务器头像
	 */
	private void uploadAvatar(File file) {
		try {
			RequestParams params = new RequestParams();
			params.put("member_avatar", file);
			HttpUtils.uploadAvatar(new HttpErrorHandler() {
				@Override
				public void onRecevieSuccess(JSONObject json) {
					BaseApp.getModel().setAvatar(json.getString("member_avatar"));
				}
			}, params);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Description: 弹出日期时间选择器
	 */
	private void showDateTimePicker() {
		// 找到dialog的布局文件
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view2 = inflater.inflate(R.layout.dialog_wheelview_bg, null);
		// 年
		final WheelView wv_wheel = (WheelView) view2.findViewById(R.id.wheel);
		wv_wheel.setAdapter(new NumericWheelAdapter(6, 80));// 设置"年龄"的显示数据
		wv_wheel.setCyclic(true);// 可循环滚动
		wv_wheel.setCurrentItem(3);// 初始化时显示的数据
		int textSize = 50;

		wv_wheel.TEXT_SIZE = textSize;
		wv_wheel.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				selectval=wv_wheel.getCurrentItem()+6;//年龄
			}
		});
		onCreateDialog("请选择年龄", null, view2).show();
	}
	/**
	 * 启动dialog的方法
	 * 
	 * @return Dialog
	 */
	public Dialog onCreateDialog(String title, String body, View view) {
		Dialog dialog = null;
		MyAlertView.Builder customBuilder = new MyAlertView.Builder(this);
		customBuilder.setTitle(title).setMessage(body).setContentView(view)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if(selectval != 0){
							updateCurrentUserAge();
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		dialog = customBuilder.create();
		return dialog;
	}
	
	/**
	 * 更新用户年龄
	 */
	private void updateCurrentUserAge(){
		RequestParams params = new RequestParams();
		params.put("member_id", BaseApp.getModel().getUserid());
		params.put("member_age", selectval);
		HttpUtils.uploadMemberInfo(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				user_age.setText(""+selectval);//年龄
			}
			@Override
			public void onRecevieFailed(String status, JSONObject json) {
				Tools.toast(UserInfoActivity.this, json.getString("message"));
			}
		},params);
	}
	
	/**
	 * 更新用户性别
	 */
	private void updateCurrentUserSex(final String sex){
		RequestParams params = new RequestParams();
		params.put("member_id", BaseApp.getModel().getUserid());
		params.put("member_sex", sex);
		HttpUtils.uploadMemberInfo(new HttpErrorHandler() {
			@Override
			public void onRecevieSuccess(JSONObject json) {
				user_sex.setText("1".equals(sex)?"男":"2".equals(sex)?"女":"选择");//年龄
			}
			@Override
			public void onRecevieFailed(String status, JSONObject json) {
				Tools.toast(UserInfoActivity.this, json.getString("message"));
			}
		},params);
	}
}
