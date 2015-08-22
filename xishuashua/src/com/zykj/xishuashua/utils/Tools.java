package com.zykj.xishuashua.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.text.ParseException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.Toast;

/**
 * 工具类
 * 
 * @author bin
 * 
 */
public class Tools {
	public static boolean DEBUG = true;

	public static ProgressDialog m_dialog;
	
	static private Toast mToast = null;

	public static int M_SCREEN_WIDTH;

	public static int M_SCREEN_HEIGHT;

	/**
	 * log输出
	 * @param msg
	 */
	public static void Log(String msg) {
		if (DEBUG) {
			android.util.Log.d("debug输出：", msg);
		}
	}
	
	/**
	 * Toast提醒
	 * @param msg
	 */
	public static void toast (Context context,String msg) {
		// 防止Toast重复显示
		if (mToast == null) {  
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);  
        } else {  
            mToast.setText(msg);  
            mToast.setDuration(Toast.LENGTH_SHORT);  
        }  
        mToast.show(); 
	}

	/**
	 * 打印消息并且用Toast显示消息
	 * 
	 * @param activity
	 * @param message
	 * @param logLevel
	 *            填d, w, e分别代表debug, warn, error; 默认是debug
	 */
	public static final void toastMessage(final Activity activity,final String message) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mToast != null) {
					mToast.cancel();
					mToast = null;
				}
				mToast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
				mToast.show();
			}
		});
	}

	public static final void showResultDialog(Context context, String msg,String title) {
		if (msg == null)
			return;
		String rmsg = msg.replace(",", "\n");
		Log.d("Util", rmsg);
		new AlertDialog.Builder(context).setTitle(title).setMessage(rmsg).setNegativeButton("知道了", null).create().show();
	}

	/**
	 * Dialog显示
	 */
	public static void showDialog(Context context) {
		m_dialog = new ProgressDialog(context);
		m_dialog.setCanceledOnTouchOutside(false);
		m_dialog.setMessage("正在加载");
		m_dialog.show();
	}

	/** 图片无损压缩并存储 */
	public static void compressBitmap(Bitmap image, File file) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baos.toByteArray());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** onCreate方法中获取view的高度 */
	public static int getTargetHeight(View v) {

		try {
			Method m = v.getClass().getDeclaredMethod("onMeasure", int.class,
					int.class);
			m.setAccessible(true);
			m.invoke(v, MeasureSpec.makeMeasureSpec(
					((View) v.getParent()).getMeasuredWidth(),
					MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED));
		} catch (Exception e) {

		}
		return v.getMeasuredHeight();
	}

	/** onCreate方法中获取view的宽度 */
	public static int getTargetWidth(View v) {

		try {
			Method m = v.getClass().getDeclaredMethod("onMeasure", int.class,
					int.class);
			m.setAccessible(true);
			m.invoke(v, MeasureSpec.makeMeasureSpec(
					((View) v.getParent()).getMeasuredWidth(),
					MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED));
		} catch (Exception e) {

		}
		return v.getMeasuredWidth();
	}

	/**
	 * 将dp转化为像素值
	 */

	public static int dp2px(Context context, int dp) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public static int Px2Dp(Context context, float px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	/**
	 * 获取当前应用的版本号
	 */

	public static int getAppVersion(Context context) {
		int version = 0;
		try {
			PackageInfo packinfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			version = packinfo.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
			return version;
		}

		return version;
	}

	/**
	 * 判断SD卡是否存在
	 */

	public static boolean existSDCard() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 判断sd卡的剩余空间
	 */

	@SuppressWarnings("deprecation")
	public static long getSDFreeSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		// return freeBlocks * blockSize; //单位Byte
		// return (freeBlocks * blockSize)/1024; //单位KB
		return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
	}
	
	/**
	 * @param dateStr
	 * @return
	 */
	public static String getTimeStr(String dateStr) {
		long currentTime = System.currentTimeMillis()/1000L;
		long commentTime = Long.valueOf(dateStr);
		long distanceTime = currentTime - commentTime;
		try {
			String udpatetime = DateUtil.longToString(Long.parseLong(dateStr+"000"), "yy-MM-dd");
			if (distanceTime/60 <= 0){
				return "刚刚";
			} else if (distanceTime/600 <= 0){
				return distanceTime/60 + "分钟前";
			} else if (distanceTime/3600 <= 0){
				return (distanceTime/600)*10 + "分钟前";
			} else if (distanceTime/43200 <= 0){
				return distanceTime/3600 + "小时前";
			} else if (distanceTime/86400 <= 0){
				return (distanceTime/43200)*12 + "小时前";
			} else if (distanceTime/259200 <= 0){
				return distanceTime/86400 + "天前";
			} else {
				return udpatetime;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}
}
