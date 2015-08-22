package com.zykj.xishuashua.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RadioButton;

import com.zykj.xishuashua.R;

/**
 * @author Administrator
 * 自定义菜单
 */
public class MyRadioButton extends RadioButton {
	
    private int mDrawableSize;// xml文件中设置图片的大小
    
    public MyRadioButton(Context context) {
        super(context);
        initView(context,null);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context,attrs);
    }

    private void initView(Context context, AttributeSet attrs){
        Drawable drawableLeft = null, drawableTop = null, drawableRight = null, drawableBottom = null;
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MyRadioButton);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            Log.i("MyRadioButton", "attr:" + attr);
            switch (attr) {
                case R.styleable.MyRadioButton_drawable_size:
                    mDrawableSize = a.getDimensionPixelSize(R.styleable.MyRadioButton_drawable_size, 20);
                    Log.i("MyRadioButton", "mDrawableSize:" + mDrawableSize);
                    break;
                case R.styleable.MyRadioButton_drawableTop:
                    drawableTop = a.getDrawable(attr);
                    break;
                case R.styleable.MyRadioButton_drawableBottom:
                	drawableBottom = a.getDrawable(attr);
                    break;
                case R.styleable.MyRadioButton_drawableRight:
                	drawableRight = a.getDrawable(attr);
                    break;
                case R.styleable.MyRadioButton_drawableLeft:
                    drawableLeft = a.getDrawable(attr);
                    break;
                default :
                    break;
            }
        }
        a.recycle();

        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }

    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {

        if (left != null) {
            left.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (right != null) {
            right.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (top != null) {
            top.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        setCompoundDrawables(left, top, right, bottom);
    }
}
