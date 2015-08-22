package com.zykj.xishuashua;

import java.util.Stack;

import android.app.Activity;
import android.app.Application;
import android.os.Vibrator;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.zykj.xishuashua.model.AppModel;
import com.zykj.xishuashua.utils.StringUtil;

public class BaseApp extends Application {
	
	/**
	 * 存储的sharePerfence
	 */
	public static final String config = "config";//存储的sharePerfence
	public static final String IS_INTRO = "is_intro";//当前的是否已经进行过指引
	public static final String VERSION = "version";//当前应用中存储的版本号
	// ===========常量==========
	private static final String TAG = "BaseApp";
	public static final String FILE_DIR = "heer_dir";

	private static Stack<Activity> activityStack;
	private static BaseApp instance;
    private static AppModel model;
	public static String address;
	public Vibrator mVibrator;
	public BaseApp() {
	}

	public synchronized static BaseApp getInstance() {
		if (null == instance) {
			instance = new BaseApp();
		}
		return instance;
	}

    private void initModel() {
    	/*初始化用户Model*/
        model=AppModel.init(this);
    }

	/**
	 * 获取用户信息
	 */
    public static AppModel getModel(){
        if(model == null){
            Log.e("application","appmodel is null");
        }
        return model;
    }

	/**
	 * 验证用户是否登录
	 */
    public static boolean validateUserLogin(){
        if(StringUtil.isEmpty(model.getUserid())){
            return false;
        }else{
            return true;
        }
    }

	/**
	 * 打开Activity
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);

		Log.d(TAG, "-----------------------------------");
		for (Activity temp : activityStack) {
			Log.d(TAG, "类名:" + temp.toString() + "地址：" + temp);
		}
		Log.d(TAG, "===================================");
	}

	/**
	 * 获取当前Activity
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 关闭Activity
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 关闭指定Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 关闭所有的Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 当前界面恢复时的操作
	 */
	public void resumeActivity(Activity activity) {
		if (activityStack.lastElement() == activity) {
			return;
		}
		activityStack.remove(activity);
		activityStack.push(activity);

		Log.d(TAG, "最后一个参数:" + activityStack.lastElement());
	}

	public void exit() {
		finishAllActivity();
		System.exit(0);
	}

	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}

	@Override
	public void onCreate() {
		initImageLoader();
		
        initModel();//初始化 数据
	}

	/**
	 * 初始化ImageLoader
	 */
	protected void initImageLoader() {
		//初始化ImageLoader
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024);
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs();

        ImageLoader.getInstance().init(config.build());
	}
}
