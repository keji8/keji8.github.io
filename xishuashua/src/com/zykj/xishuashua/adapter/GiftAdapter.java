package com.zykj.xishuashua.adapter;

import java.util.List;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.zykj.xishuashua.BaseApp;
import com.zykj.xishuashua.R;
import com.zykj.xishuashua.http.UrlContants;
import com.zykj.xishuashua.model.Gift;
import com.zykj.xishuashua.utils.CommonUtils;
import com.zykj.xishuashua.utils.StringUtil;

public class GiftAdapter extends CommonAdapter<Gift> {
	
	private String status;//0普通红包, 1收藏红包, 2新闻资讯

	public GiftAdapter(Context context, int resource, List<Gift> datas, String status) {
		super(context, resource, datas);
		this.status = status;
	}

	@Override
	public void convert(final ViewHolder holder, Gift gift) {
		int seconds = Integer.parseInt(StringUtil.isEmpty(gift.getGoods_marketprice())?"0":gift.getGoods_marketprice());
		holder.setText(R.id.gift_name, gift.getGoods_name())
			.setText(R.id.gift_content, gift.getGoods_jingle())
			.setText(R.id.gift_btn, "抢红包"+gift.getGoods_price()+"元")
			.setVisibility(R.id.gift_type, !"1".equals(status))
			.setVisibility(R.id.gift_date, !"0".equals(status))
			.setImageUrl(R.id.gift_image, UrlContants.GIFTIMGURL+gift.getGoods_image(), 10f)
			//.setText(R.id.gift_time, "倒计时"+seconds/60+"分"+seconds%60+"秒")
			.setText(R.id.gift_distance, CommonUtils.GetDistance(Double.valueOf(gift.getGoods_lati()), Double.valueOf(gift.getGoods_longi()), 
					Double.valueOf(BaseApp.getModel().getLatitude()), Double.valueOf(BaseApp.getModel().getLongitude()))+"m");
//		long startGunTime = Long.valueOf(gift.getGoods_selltime());
//		long continuousTime = Long.valueOf(gift.getGoods_marketprice());
//		long currentTime = System.currentTimeMillis()/1000L;
//		if (startGunTime - currentTime > 0){
//			
//		} else if (currentTime - startGunTime + continuousTime > 0){
//			
//		}
//		new TimeCount(seconds * 1000, 1000, (TextView)holder.getView(R.id.gift_time));
	}
	
	/* 定义一个倒计时的内部类 */
	class TimeCount extends CountDownTimer {
		private TextView goodsTime;
		public TimeCount(long millisInFuture, long countDownInterval, TextView goodsTime) {
			super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
			this.goodsTime = goodsTime;
		}
		@Override
		public void onFinish() {//计时完毕时触发
			goodsTime.setText("已过期");
		}
		@Override
		public void onTick(long millisUntilFinished){//计时过程显示
			long seconds = millisUntilFinished / 1000;
			goodsTime.setText("倒计时"+seconds/60+"分"+seconds%60+"秒");
		}
	}
}
