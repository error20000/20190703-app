package com.jian.system.entity;

import android.content.ContentValues;
import android.database.Cursor;

public class StoreType {


	private String sStoreType_ID;
	private String sStoreType_Name;
	private String sStoreType_Address;
	private float lStoreType_Lat;
	private float lStoreType_Lng;
	private String sStoreType_Station;
	private int lStoreType_Limit;
	private String sStoreType_MapIcon;

	private float lStoreType_LatDu;
	private float lStoreType_LatFen;
	private float lStoreType_LatMiao;
	private float lStoreType_LngDu;
	private float lStoreType_LngFen;
	private float lStoreType_LngMiao;
	
	
	
	public String getsStoreType_ID() {
		return sStoreType_ID;
	}
	public void setsStoreType_ID(String sStoreType_ID) {
		this.sStoreType_ID = sStoreType_ID;
	}
	public String getsStoreType_Name() {
		return sStoreType_Name;
	}
	public void setsStoreType_Name(String sStoreType_Name) {
		this.sStoreType_Name = sStoreType_Name;
	}
	public String getsStoreType_Address() {
		return sStoreType_Address;
	}
	public void setsStoreType_Address(String sStoreType_Address) {
		this.sStoreType_Address = sStoreType_Address;
	}
	public float getlStoreType_Lat() {
		return lStoreType_Lat;
	}
	public void setlStoreType_Lat(float lStoreType_Lat) {
		this.lStoreType_Lat = lStoreType_Lat;
	}
	public float getlStoreType_Lng() {
		return lStoreType_Lng;
	}
	public void setlStoreType_Lng(float lStoreType_Lng) {
		this.lStoreType_Lng = lStoreType_Lng;
	}
	public String getsStoreType_Station() {
		return sStoreType_Station;
	}
	public void setsStoreType_Station(String sStoreType_Station) {
		this.sStoreType_Station = sStoreType_Station;
	}

	public int getlStoreType_Limit() {
		return lStoreType_Limit;
	}

	public void setlStoreType_Limit(int lStoreType_Limit) {
		this.lStoreType_Limit = lStoreType_Limit;
	}

	public String getsStoreType_MapIcon() {
		return sStoreType_MapIcon;
	}

	public void setsStoreType_MapIcon(String sStoreType_MapIcon) {
		this.sStoreType_MapIcon = sStoreType_MapIcon;
	}

	public float getlStoreType_LatDu() {
		return lStoreType_LatDu;
	}

	public void setlStoreType_LatDu(float lStoreType_LatDu) {
		this.lStoreType_LatDu = lStoreType_LatDu;
	}

	public float getlStoreType_LatFen() {
		return lStoreType_LatFen;
	}

	public void setlStoreType_LatFen(float lStoreType_LatFen) {
		this.lStoreType_LatFen = lStoreType_LatFen;
	}

	public float getlStoreType_LatMiao() {
		return lStoreType_LatMiao;
	}

	public void setlStoreType_LatMiao(float lStoreType_LatMiao) {
		this.lStoreType_LatMiao = lStoreType_LatMiao;
	}

	public float getlStoreType_LngDu() {
		return lStoreType_LngDu;
	}

	public void setlStoreType_LngDu(float lStoreType_LngDu) {
		this.lStoreType_LngDu = lStoreType_LngDu;
	}

	public float getlStoreType_LngFen() {
		return lStoreType_LngFen;
	}

	public void setlStoreType_LngFen(float lStoreType_LngFen) {
		this.lStoreType_LngFen = lStoreType_LngFen;
	}

	public float getlStoreType_LngMiao() {
		return lStoreType_LngMiao;
	}

	public void setlStoreType_LngMiao(float lStoreType_LngMiao) {
		this.lStoreType_LngMiao = lStoreType_LngMiao;
	}


	public StoreType cursorToBean(Cursor cursor){
		this.sStoreType_ID = cursor.getString(cursor.getColumnIndex("sStoreType_ID"));
		this.sStoreType_Name = cursor.getString(cursor.getColumnIndex("sStoreType_Name"));
		this.sStoreType_Address = cursor.getString(cursor.getColumnIndex("sStoreType_Address"));
		this.lStoreType_Lat = cursor.getFloat(cursor.getColumnIndex("lStoreType_Lat"));
		this.lStoreType_Lng = cursor.getFloat(cursor.getColumnIndex("lStoreType_Lng"));
		this.sStoreType_Station = cursor.getString(cursor.getColumnIndex("sStoreType_Station"));
		this.lStoreType_Limit = cursor.getInt(cursor.getColumnIndex("lStoreType_Limit"));
		this.sStoreType_MapIcon = cursor.getString(cursor.getColumnIndex("sStoreType_MapIcon"));

		this.lStoreType_LatDu = cursor.getFloat(cursor.getColumnIndex("lStoreType_LatDu"));
		this.lStoreType_LatFen = cursor.getFloat(cursor.getColumnIndex("lStoreType_LatFen"));
		this.lStoreType_LatMiao = cursor.getFloat(cursor.getColumnIndex("lStoreType_LatMiao"));
		this.lStoreType_LngDu = cursor.getFloat(cursor.getColumnIndex("lStoreType_LngDu"));
		this.lStoreType_LngFen = cursor.getFloat(cursor.getColumnIndex("lStoreType_LngFen"));
		this.lStoreType_LngMiao = cursor.getFloat(cursor.getColumnIndex("lStoreType_LngMiao"));

		return this;
	}

	public ContentValues beanToValues(){
		ContentValues values = new ContentValues();
		values.put("sStoreType_ID", sStoreType_ID);
		values.put("sStoreType_Name", sStoreType_Name);
		values.put("sStoreType_Address", sStoreType_Address);
		values.put("lStoreType_Lat", lStoreType_Lat);
		values.put("lStoreType_Lng", lStoreType_Lng);
		values.put("sStoreType_Station", sStoreType_Station);
		values.put("lStoreType_Limit", lStoreType_Limit);
		values.put("sStoreType_MapIcon", sStoreType_MapIcon);
		values.put("lStoreType_LatDu", lStoreType_LatDu);
		values.put("lStoreType_LatFen", lStoreType_LatFen);
		values.put("lStoreType_LatMiao", lStoreType_LatMiao);
		values.put("lStoreType_LngDu", lStoreType_LngDu);
		values.put("lStoreType_LngFen", lStoreType_LngFen);
		values.put("lStoreType_LngMiao", lStoreType_LngMiao);
		return values;
	}
}
