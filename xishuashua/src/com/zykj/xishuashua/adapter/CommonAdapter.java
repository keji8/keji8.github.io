package com.zykj.xishuashua.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CommonAdapter<T> extends BaseAdapter{
	
	protected Context mContext;
	public List<T> mDatas;
	private int resource;
	protected LayoutInflater mInflater;
	
	public CommonAdapter(Context context, int resource, List<T> datas){
		this.mContext = context;
		this.resource = resource;
		this.mInflater = LayoutInflater.from(context);
		this.mDatas = datas;
	}

	@Override
	public int getCount() {
		return mDatas == null?0:mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder holder = ViewHolder.get(mContext, convertView, parent, resource, position);
		
		convert(holder, getItem(position));
		
		return holder.getConvertView();
	}

	public abstract void convert(ViewHolder holder, T t);

}
