package com.zykj.xishuashua.model;

import java.io.Serializable;
import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint("ParcelCreator")
public class MyEnvelope implements Serializable,Parcelable{
	/**
	 * 我的红包
	 */
	private static final long serialVersionUID = 1L;
	private String membersawgoods_id;
	private String member_id;
	private String goods_id;
	private String membersawgoods_lastsawtime;
	private String membersawgoods_sawtimes;
	private String membersawgoods_gotpoint;
	private String store_name;
	private String goods_serial;
	private String enve_serial;
	
	public String getMembersawgoods_id() {
		return membersawgoods_id;
	}
	public void setMembersawgoods_id(String membersawgoods_id) {
		this.membersawgoods_id = membersawgoods_id;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}
	public String getMembersawgoods_lastsawtime() {
		return membersawgoods_lastsawtime;
	}
	public void setMembersawgoods_lastsawtime(String membersawgoods_lastsawtime) {
		this.membersawgoods_lastsawtime = membersawgoods_lastsawtime;
	}
	public String getMembersawgoods_sawtimes() {
		return membersawgoods_sawtimes;
	}
	public void setMembersawgoods_sawtimes(String membersawgoods_sawtimes) {
		this.membersawgoods_sawtimes = membersawgoods_sawtimes;
	}
	public String getMembersawgoods_gotpoint() {
		return membersawgoods_gotpoint;
	}
	public void setMembersawgoods_gotpoint(String membersawgoods_gotpoint) {
		this.membersawgoods_gotpoint = membersawgoods_gotpoint;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public String getGoods_serial() {
		return goods_serial;
	}
	public void setGoods_serial(String goods_serial) {
		this.goods_serial = goods_serial;
	}
	public String getEnve_serial() {
		return enve_serial;
	}
	public void setEnve_serial(String enve_serial) {
		this.enve_serial = enve_serial;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel parcel, int position) {
		parcel.writeString(membersawgoods_id);
		parcel.writeString(member_id);
		parcel.writeString(goods_id);
		parcel.writeString(membersawgoods_lastsawtime);
		parcel.writeString(membersawgoods_sawtimes);
		parcel.writeString(membersawgoods_gotpoint);
		parcel.writeString(store_name);
		parcel.writeString(goods_serial);
		parcel.writeString(enve_serial);
	}
	public static final Parcelable.Creator<MyEnvelope> CREATOR = new Parcelable.Creator<MyEnvelope>() {
		@Override
		public MyEnvelope createFromParcel(Parcel parcel) {
			MyEnvelope envelope = new MyEnvelope();
			envelope.membersawgoods_id = parcel.readString();
			envelope.member_id = parcel.readString();
			envelope.goods_id = parcel.readString();
			envelope.membersawgoods_lastsawtime = parcel.readString();
			envelope.membersawgoods_sawtimes = parcel.readString();
			envelope.membersawgoods_gotpoint = parcel.readString();
			envelope.store_name = parcel.readString();
			envelope.goods_serial = parcel.readString();
			envelope.enve_serial = parcel.readString();
			return envelope;
		}

		@Override
		public MyEnvelope[] newArray(int size) {
			return new MyEnvelope[size];
		}
	};
}
