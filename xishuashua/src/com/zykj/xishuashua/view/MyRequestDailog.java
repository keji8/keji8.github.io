package com.zykj.xishuashua.view;

import java.util.Timer;

import android.app.Dialog;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.zykj.xishuashua.R;

public class MyRequestDailog extends Dialog {

	public static MyRequestDailog m_dialog;
	public static Timer m_timer;
	private String msg;

	public MyRequestDailog(Context context) {
		this(context, "");
	}

	public MyRequestDailog(Context context, String msg) {
		super(context, R.style.RequestDialog);
		this.msg = msg;
		this.setContentView(R.layout.dialog_request);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (m_dialog == null) {
			return;
		}
		final ImageView image = (ImageView) findViewById(R.id.dialog_load);
		Animation anim = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		anim.setInterpolator(new LinearInterpolator());// 设置播放之间无停顿
		anim.setDuration(1500);// 设置播放速度
		anim.setRepeatCount(-1);// 设置循环播放
		image.startAnimation(anim);

		if (msg.length() > 0) {
			final TextView show_msg = (TextView) findViewById(R.id.dialog_text);
			show_msg.setText(msg);
		}

	}

	public static void showDialog(Context context, String msg) {
		if (m_dialog == null) {
			m_dialog = new MyRequestDailog(context, msg);
			m_dialog.setCancelable(false);
			m_dialog.show();
		}else{
			m_dialog.dismiss();
			m_dialog = new MyRequestDailog(context, msg);
			m_dialog.setCancelable(false);
			m_dialog.show();
		}
	}

	public static void closeDialog() {
		if (m_dialog == null) {
			return;
		}
		m_dialog.dismiss();
	}
}
