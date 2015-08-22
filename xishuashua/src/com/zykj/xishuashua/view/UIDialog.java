package com.zykj.xishuashua.view;



import android.app.AlertDialog;
import android.content.Context;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.zykj.xishuashua.R;

/**
 * 一些ui中Dialog中的使用
 * 
 * @author bin
 * 
 */
public class UIDialog {
	public static AlertDialog dialog;

	/** 3按键按钮dialog */
	public static void ForThreeBtn(Context context, String[] showtxt,
			OnClickListener lisener) {
		dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		Window window = dialog.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		window.setContentView(R.layout.dialog_picture);

		Button m_btn_1 = (Button) window.findViewById(R.id.dialog_modif_1);
		Button m_btn_2 = (Button) window.findViewById(R.id.dialog_modif_2);
		Button m_btn_3 = (Button) window.findViewById(R.id.dialog_modif_3);

		m_btn_1.setText(showtxt[0]);
		m_btn_2.setText(showtxt[1]);
		m_btn_3.setText(showtxt[2]);

		m_btn_1.setOnClickListener(lisener);
		m_btn_2.setOnClickListener(lisener);
		m_btn_3.setOnClickListener(lisener);
	}

	/** 拨号dialog */
	public static void callTelephone(Context context, String telephone, OnClickListener lisener) {
		dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		Window window = dialog.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		window.setContentView(R.layout.dialog_telephone);

		Button m_btn_1 = (Button) window.findViewById(R.id.dialog_modif_1);
		Button m_btn_2 = (Button) window.findViewById(R.id.dialog_modif_2);

		m_btn_1.setText(telephone);
		m_btn_2.setText("取消");

		m_btn_1.setOnClickListener(lisener);
		m_btn_2.setOnClickListener(lisener);
	}
	
	/** 评论dialog */
	public static EditText commentLayout(Context context, OnClickListener lisener) {
		dialog = new AlertDialog.Builder(context).create();
		//dialog.setView(((Activity)context).getLayoutInflater().inflate(R.layout.dialog_comment, null));
		dialog.show();
		Window window = dialog.getWindow();
		window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		window.setContentView(R.layout.dialog_comment);

		EditText comment_content = (EditText) window.findViewById(R.id.comment_content);
		Button comment_cancel = (Button) window.findViewById(R.id.comment_cancel);
		Button comment_submit = (Button) window.findViewById(R.id.comment_submit);

		comment_cancel.setOnClickListener(lisener);
		comment_submit.setOnClickListener(lisener);
		return comment_content;
	}
	
	/**
	 * 关闭dialog
	 */
	public static void closeDialog() {
		if (dialog == null || !dialog.isShowing()) {
			return;
		}
		dialog.dismiss();

	}
}
