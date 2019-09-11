package com.jian.system.entity;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

public class Aid {


	private String sAid_ID;
	private String sAid_Name;
	private String sAid_NO;
	private float lAid_Lat;
	private float lAid_Lng;
	private String sAid_Station;
	private String sAid_Type;
	private String sAid_Icon;
	private Date dAid_BuildDate;
	private Date dAid_DelDate;
	private String sAid_Lighting;
	private String sAid_Mark;
	private String sAid_NfcID;
	private Date dAid_CreateDate;
	private String sAid_Status;

	private float lAid_LatDu;
	private float lAid_LatFen;
	private float lAid_LatMiao;
	private float lAid_LngDu;
	private float lAid_LngFen;
	private float lAid_LngMiao;
	
	
	public String getsAid_ID() {
		return sAid_ID;
	}
	public void setsAid_ID(String sAid_ID) {
		this.sAid_ID = sAid_ID;
	}
	public String getsAid_Name() {
		return sAid_Name;
	}
	public void setsAid_Name(String sAid_Name) {
		this.sAid_Name = sAid_Name;
	}
	public String getsAid_NO() {
		return sAid_NO;
	}
	public void setsAid_NO(String sAid_NO) {
		this.sAid_NO = sAid_NO;
	}
	public float getlAid_Lat() {
		return lAid_Lat;
	}
	public void setlAid_Lat(float lAid_Lat) {
		this.lAid_Lat = lAid_Lat;
	}
	public float getlAid_Lng() {
		return lAid_Lng;
	}
	public void setlAid_Lng(float lAid_Lng) {
		this.lAid_Lng = lAid_Lng;
	}
	public String getsAid_Station() {
		return sAid_Station;
	}
	public void setsAid_Station(String sAid_Station) {
		this.sAid_Station = sAid_Station;
	}
	public String getsAid_Type() {
		return sAid_Type;
	}
	public void setsAid_Type(String sAid_Type) {
		this.sAid_Type = sAid_Type;
	}
	public String getsAid_Icon() {
		return sAid_Icon;
	}
	public void setsAid_Icon(String sAid_Icon) {
		this.sAid_Icon = sAid_Icon;
	}
	public Date getdAid_CreateDate() {
		return dAid_CreateDate;
	}
	public void setdAid_CreateDate(Date dAid_CreateDate) {
		this.dAid_CreateDate = dAid_CreateDate;
	}
	public Date getdAid_DelDate() {
		return dAid_DelDate;
	}
	public void setdAid_DelDate(Date dAid_DelDate) {
		this.dAid_DelDate = dAid_DelDate;
	}
	public String getsAid_Lighting() {
		return sAid_Lighting;
	}
	public void setsAid_Lighting(String sAid_Lighting) {
		this.sAid_Lighting = sAid_Lighting;
	}
	public String getsAid_Mark() {
		return sAid_Mark;
	}
	public void setsAid_Mark(String sAid_Mark) {
		this.sAid_Mark = sAid_Mark;
	}
	public String getsAid_NfcID() {
		return sAid_NfcID;
	}
	public void setsAid_NfcID(String sAid_NfcID) {
		this.sAid_NfcID = sAid_NfcID;
	}
	public Date getdAid_BuildDate() {
		return dAid_BuildDate;
	}
	public void setdAid_BuildDate(Date dAid_BuildDate) {
		this.dAid_BuildDate = dAid_BuildDate;
	}
	public String getsAid_Status() {
		return sAid_Status;
	}
	public void setsAid_Status(String sAid_Status) {
		this.sAid_Status = sAid_Status;
	}
	public float getlAid_LatDu() {
		return lAid_LatDu;
	}
	public void setlAid_LatDu(float lAid_LatDu) {
		this.lAid_LatDu = lAid_LatDu;
	}
	public float getlAid_LatFen() {
		return lAid_LatFen;
	}
	public void setlAid_LatFen(float lAid_LatFen) {
		this.lAid_LatFen = lAid_LatFen;
	}
	public float getlAid_LatMiao() {
		return lAid_LatMiao;
	}
	public void setlAid_LatMiao(float lAid_LatMiao) {
		this.lAid_LatMiao = lAid_LatMiao;
	}
	public float getlAid_LngDu() {
		return lAid_LngDu;
	}
	public void setlAid_LngDu(float lAid_LngDu) {
		this.lAid_LngDu = lAid_LngDu;
	}
	public float getlAid_LngFen() {
		return lAid_LngFen;
	}
	public void setlAid_LngFen(float lAid_LngFen) {
		this.lAid_LngFen = lAid_LngFen;
	}
	public float getlAid_LngMiao() {
		return lAid_LngMiao;
	}
	public void setlAid_LngMiao(float lAid_LngMiao) {
		this.lAid_LngMiao = lAid_LngMiao;
	}

	public Aid cursorToBean(Cursor cursor){
		this.sAid_ID = cursor.getString(cursor.getColumnIndex("sAid_ID"));
		this.sAid_Name = cursor.getString(cursor.getColumnIndex("sAid_Name"));
		this.sAid_NO = cursor.getString(cursor.getColumnIndex("sAid_NO"));
		this.lAid_Lat = cursor.getFloat(cursor.getColumnIndex("lAid_Lat"));
		this.lAid_Lng = cursor.getFloat(cursor.getColumnIndex("lAid_Lng"));
		this.sAid_Station = cursor.getString(cursor.getColumnIndex("sAid_Station"));
		this.sAid_Type = cursor.getString(cursor.getColumnIndex("sAid_Type"));
		this.sAid_Icon = cursor.getString(cursor.getColumnIndex("sAid_Icon"));
		this.dAid_BuildDate = new Date(cursor.getLong(cursor.getColumnIndex("dAid_BuildDate")));
		this.dAid_DelDate =  new Date(cursor.getLong(cursor.getColumnIndex("dAid_DelDate")));
		this.sAid_Lighting = cursor.getString(cursor.getColumnIndex("sAid_Lighting"));
		this.sAid_Mark = cursor.getString(cursor.getColumnIndex("sAid_Mark"));
		this.sAid_NfcID = cursor.getString(cursor.getColumnIndex("sAid_NfcID"));
		this.dAid_CreateDate =  new Date(cursor.getLong(cursor.getColumnIndex("dAid_CreateDate")));
		this.sAid_Status = cursor.getString(cursor.getColumnIndex("sAid_Status"));

		this.lAid_LatDu = cursor.getFloat(cursor.getColumnIndex("lAid_LatDu"));
		this.lAid_LatFen = cursor.getFloat(cursor.getColumnIndex("lAid_LatFen"));
		this.lAid_LatMiao = cursor.getFloat(cursor.getColumnIndex("lAid_LatMiao"));
		this.lAid_LngDu = cursor.getFloat(cursor.getColumnIndex("lAid_LngDu"));
		this.lAid_LngFen = cursor.getFloat(cursor.getColumnIndex("lAid_LngFen"));
		this.lAid_LngMiao = cursor.getFloat(cursor.getColumnIndex("lAid_LngMiao"));

		return this;
	}

	public ContentValues beanToValues(){
		ContentValues values = new ContentValues();
		values.put("sAid_ID", sAid_ID);
		values.put("sAid_Name", sAid_Name);
		values.put("sAid_NO", sAid_NO);
		values.put("lAid_Lat", lAid_Lat);
		values.put("lAid_Lng", lAid_Lng);
		values.put("sAid_Station", sAid_Station);
		values.put("sAid_Type", sAid_Type);
		values.put("sAid_Icon", sAid_Icon);
		values.put("dAid_BuildDate", dAid_BuildDate.getTime());
		values.put("dAid_DelDate", dAid_DelDate.getTime());
		values.put("sAid_Lighting", sAid_Lighting);
		values.put("sAid_Mark", sAid_Mark);
		values.put("sAid_NfcID", sAid_NfcID);
		values.put("dAid_CreateDate", dAid_CreateDate.getTime());
		values.put("sAid_Status", sAid_Status);
		values.put("lAid_LatDu", lAid_LatDu);
		values.put("lAid_LatFen", lAid_LatFen);
		values.put("lAid_LatMiao", lAid_LatMiao);
		values.put("lAid_LngDu", lAid_LngDu);
		values.put("lAid_LngFen", lAid_LngFen);
		values.put("lAid_LngMiao", lAid_LngMiao);
		return values;
	}

}
