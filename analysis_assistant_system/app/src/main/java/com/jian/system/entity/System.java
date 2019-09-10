package com.jian.system.entity;


import android.database.Cursor;

public class System {


	private String sSys_ID;
	private String sSys_MapService;
	private float lSys_MapLng;
	private float lSys_MapLat;
	private int lSys_MapLevel;
	private int lSys_MapIconWidth;
	private int lSys_MapIconHeight;


	public String getsSys_ID() {
		return sSys_ID;
	}
	public void setsSys_ID(String sSys_ID) {
		this.sSys_ID = sSys_ID;
	}
	public String getsSys_MapService() {
		return sSys_MapService;
	}
	public void setsSys_MapService(String sSys_MapService) {
		this.sSys_MapService = sSys_MapService;
	}
	public float getlSys_MapLng() {
		return lSys_MapLng;
	}
	public void setlSys_MapLng(float lSys_MapLng) {
		this.lSys_MapLng = lSys_MapLng;
	}
	public float getlSys_MapLat() {
		return lSys_MapLat;
	}
	public void setlSys_MapLat(float lSys_MapLat) {
		this.lSys_MapLat = lSys_MapLat;
	}
	public int getlSys_MapLevel() {
		return lSys_MapLevel;
	}
	public void setlSys_MapLevel(int lSys_MapLevel) {
		this.lSys_MapLevel = lSys_MapLevel;
	}
	public int getlSys_MapIconWidth() {
		return lSys_MapIconWidth;
	}
	public void setlSys_MapIconWidth(int lSys_MapIconWidth) {
		this.lSys_MapIconWidth = lSys_MapIconWidth;
	}
	public int getlSys_MapIconHeight() {
		return lSys_MapIconHeight;
	}
	public void setlSys_MapIconHeight(int lSys_MapIconHeight) {
		this.lSys_MapIconHeight = lSys_MapIconHeight;
	}


	public System cursorToBean(Cursor cursor){
		this.sSys_ID = cursor.getString(cursor.getColumnIndex("sSys_ID"));
		this.sSys_MapService = cursor.getString(cursor.getColumnIndex("sSys_MapService"));
		this.lSys_MapLng = cursor.getFloat(cursor.getColumnIndex("lSys_MapLng"));
		this.lSys_MapLat = cursor.getFloat(cursor.getColumnIndex("lSys_MapLat"));
		this.lSys_MapLevel = cursor.getInt(cursor.getColumnIndex("lSys_MapLevel"));
		this.lSys_MapIconWidth = cursor.getInt(cursor.getColumnIndex("lSys_MapIconWidth"));
		this.lSys_MapIconHeight = cursor.getInt(cursor.getColumnIndex("lSys_MapIconHeight"));

		return this;
	}
}
