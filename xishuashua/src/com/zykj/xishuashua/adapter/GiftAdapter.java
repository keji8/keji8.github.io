package com.zykj.xishuashua.adapter;

import java.text.ParseException;
import java.util.List;

import android.content.Context;

import com.zykj.xishuashua.BaseApp;
import com.zykj.xishuashua.R;
import com.zykj.xishuashua.http.UrlContants;
import com.zykj.xishuashua.model.Gift;
import com.zykj.xishuashua.utils.CommonUtils;
import com.zykj.xishuashua.utils.DateUtil;
import com.zykj.xishuashua.utils.StringUtil;

public class GiftAdapter extends CommonAdapter<Gift> {
	
	private String status;////0即时红包, 1永久红包, 2收藏红包

	public GiftAdapter(Context context, int resource, List<Gift> datas, String status) {
		super(context, resource, datas);
		this.status = status;
	}

	@Override
	public void convert(final ViewHolder holder, Gift gift) {
		long continueTime = Long.parseLong(StringUtil.toString(gift.getGoods_marketprice(), "0"));
		long startTime = Long.parseLong(StringUtil.toString(gift.getGoods_selltime(), "0"))/1000;
		long seconds = startTime + continueTime - System.currentTimeMillis();
		try {
			String sellTime = DateUtil.longToString(Long.parseLong(gift.getGoods_selltime()+"000"), "yyyy-MM-dd");
			holder.setText(R.id.gift_name, gift.getGoods_name())
				.setText(R.id.gift_content, gift.getGoods_jingle())
				.setText(R.id.gift_btn, "1".equals(status)?"抢红包"+gift.getGoods_price()+"元":seconds>1?"抢红包"+gift.getGoods_price()+"元":"已过期")
				.setText(R.id.gift_date1, sellTime+"发布").setText(R.id.gift_date2, sellTime+"发布")
				.setVisibility(R.id.gift_type, !"2".equals(status))
				.setVisibility(R.id.gift_date1, "1".equals(status))
				.setVisibility(R.id.gift_date2, "2".equals(status))
				.setVisibility(R.id.gift_time, !"1".equals(status))
				.setImageUrl(R.id.gift_image, UrlContants.GIFTIMGURL+gift.getGoods_image(), 10f)
				.setText(R.id.gift_time, seconds>1?"倒计时"+seconds/60+"分"+seconds%60+"秒":"")
				.setText(R.id.gift_distance, CommonUtils.GetDistance(Double.valueOf(gift.getGoods_lati()), Double.valueOf(gift.getGoods_longi()), 
						Double.valueOf(BaseApp.getModel().getLatitude()), Double.valueOf(BaseApp.getModel().getLongitude()))+"m");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
