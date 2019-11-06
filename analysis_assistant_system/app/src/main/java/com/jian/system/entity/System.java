package com.jian.system.entity;


import android.content.ContentValues;
import android.database.Cursor;

public class System {


	private String sSys_ID;
	private String sSys_MapService;
	private float lSys_MapLng;
	private float lSys_MapLat;
	private int lSys_MapLevel;
	private int lSys_MapIconWidth;
	private int lSys_MapIconHeight;

	//库存盘点
	private int lSys_StoreMode;
	private int lSys_StoreValue;
	private int lSys_StoreMsg;

	//器材寿命盘点
	private int lSys_EquipMode;
	private int lSys_EquipValue;
	private int lSys_EquipMsg;
	private int lSys_EquipRate;

	//图标
	private int lSys_MapLevelPoint;
	private int lSys_MapIconWidthPoint;
	private int lSys_MapIconHeightPoint;
	private int lSys_MapLevelDef;
	private int lSys_MapIconWidthDef;
	private int lSys_MapIconHeightDef;

	private int lSys_StoreIconWidth;
	private int lSys_StoreIconHeight;
	private int lSys_StoreIconWidthPoint;
	private int lSys_StoreIconHeightPoint;
	private int lSys_StoreIconWidthDef;
	private int lSys_StoreIconHeightDef;



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
	public int getlSys_StoreMode() {
		return lSys_StoreMode;
	}
	public void setlSys_StoreMode(int lSys_StoreMode) {
		this.lSys_StoreMode = lSys_StoreMode;
	}
	public int getlSys_StoreValue() {
		return lSys_StoreValue;
	}
	public void setlSys_StoreValue(int lSys_StoreValue) {
		this.lSys_StoreValue = lSys_StoreValue;
	}
	public int getlSys_StoreMsg() {
		return lSys_StoreMsg;
	}
	public void setlSys_StoreMsg(int lSys_StoreMsg) {
		this.lSys_StoreMsg = lSys_StoreMsg;
	}
	public int getlSys_EquipMode() {
		return lSys_EquipMode;
	}
	public void setlSys_EquipMode(int lSys_EquipMode) {
		this.lSys_EquipMode = lSys_EquipMode;
	}
	public int getlSys_EquipValue() {
		return lSys_EquipValue;
	}
	public void setlSys_EquipValue(int lSys_EquipValue) {
		this.lSys_EquipValue = lSys_EquipValue;
	}
	public int getlSys_EquipMsg() {
		return lSys_EquipMsg;
	}
	public void setlSys_EquipMsg(int lSys_EquipMsg) {
		this.lSys_EquipMsg = lSys_EquipMsg;
	}
	public int getlSys_EquipRate() {
		return lSys_EquipRate;
	}
	public void setlSys_EquipRate(int lSys_EquipRate) {
		this.lSys_EquipRate = lSys_EquipRate;
	}
	public int getlSys_MapLevelPoint() {
		return lSys_MapLevelPoint;
	}
	public void setlSys_MapLevelPoint(int lSys_MapLevelPoint) {
		this.lSys_MapLevelPoint = lSys_MapLevelPoint;
	}
	public int getlSys_MapIconWidthPoint() {
		return lSys_MapIconWidthPoint;
	}
	public void setlSys_MapIconWidthPoint(int lSys_MapIconWidthPoint) {
		this.lSys_MapIconWidthPoint = lSys_MapIconWidthPoint;
	}
	public int getlSys_MapIconHeightPoint() {
		return lSys_MapIconHeightPoint;
	}
	public void setlSys_MapIconHeightPoint(int lSys_MapIconHeightPoint) {
		this.lSys_MapIconHeightPoint = lSys_MapIconHeightPoint;
	}
	public int getlSys_MapLevelDef() {
		return lSys_MapLevelDef;
	}
	public void setlSys_MapLevelDef(int lSys_MapLevelDef) {
		this.lSys_MapLevelDef = lSys_MapLevelDef;
	}
	public int getlSys_MapIconWidthDef() {
		return lSys_MapIconWidthDef;
	}
	public void setlSys_MapIconWidthDef(int lSys_MapIconWidthDef) {
		this.lSys_MapIconWidthDef = lSys_MapIconWidthDef;
	}
	public int getlSys_MapIconHeightDef() {
		return lSys_MapIconHeightDef;
	}
	public void setlSys_MapIconHeightDef(int lSys_MapIconHeightDef) {
		this.lSys_MapIconHeightDef = lSys_MapIconHeightDef;
	}
	public int getlSys_StoreIconWidth() {
		return lSys_StoreIconWidth;
	}
	public void setlSys_StoreIconWidth(int lSys_StoreIconWidth) {
		this.lSys_StoreIconWidth = lSys_StoreIconWidth;
	}
	public int getlSys_StoreIconHeight() {
		return lSys_StoreIconHeight;
	}
	public void setlSys_StoreIconHeight(int lSys_StoreIconHeight) {
		this.lSys_StoreIconHeight = lSys_StoreIconHeight;
	}
	public int getlSys_StoreIconWidthPoint() {
		return lSys_StoreIconWidthPoint;
	}
	public void setlSys_StoreIconWidthPoint(int lSys_StoreIconWidthPoint) {
		this.lSys_StoreIconWidthPoint = lSys_StoreIconWidthPoint;
	}
	public int getlSys_StoreIconHeightPoint() {
		return lSys_StoreIconHeightPoint;
	}
	public void setlSys_StoreIconHeightPoint(int lSys_StoreIconHeightPoint) {
		this.lSys_StoreIconHeightPoint = lSys_StoreIconHeightPoint;
	}
	public int getlSys_StoreIconWidthDef() {
		return lSys_StoreIconWidthDef;
	}
	public void setlSys_StoreIconWidthDef(int lSys_StoreIconWidthDef) {
		this.lSys_StoreIconWidthDef = lSys_StoreIconWidthDef;
	}
	public int getlSys_StoreIconHeightDef() {
		return lSys_StoreIconHeightDef;
	}
	public void setlSys_StoreIconHeightDef(int lSys_StoreIconHeightDef) {
		this.lSys_StoreIconHeightDef = lSys_StoreIconHeightDef;
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

	public ContentValues beanToValues(){
		ContentValues values = new ContentValues();
		values.put("sSys_ID", sSys_ID);
		values.put("sSys_MapService", sSys_MapService);
		values.put("lSys_MapLng", lSys_MapLng);
		values.put("lSys_MapLat", lSys_MapLat);
		values.put("lSys_MapLevel", lSys_MapLevel);
		values.put("lSys_MapIconWidth", lSys_MapIconWidth);
		values.put("lSys_MapIconHeight", lSys_MapIconHeight);
		return values;
	}
}
