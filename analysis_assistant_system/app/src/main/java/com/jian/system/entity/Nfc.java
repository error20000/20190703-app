package com.jian.system.entity;

import android.database.Cursor;

import java.util.Date;

public class Nfc {


	private String sNfc_ID;
	private String sNfc_Name;
	private String sNfc_NO;
	private int lNfc_StatusFlag;
	private Date dNfc_CreateDate;


	
	public String getsNfc_ID() {
		return sNfc_ID;
	}
	public void setsNfc_ID(String sNfc_ID) {
		this.sNfc_ID = sNfc_ID;
	}
	public String getsNfc_Name() {
		return sNfc_Name;
	}
	public void setsNfc_Name(String sNfc_Name) {
		this.sNfc_Name = sNfc_Name;
	}
	public String getsNfc_NO() {
		return sNfc_NO;
	}
	public void setsNfc_NO(String sNfc_NO) {
		this.sNfc_NO = sNfc_NO;
	}
	public int getlNfc_StatusFlag() {
		return lNfc_StatusFlag;
	}
	public void setlNfc_StatusFlag(int lNfc_StatusFlag) {
		this.lNfc_StatusFlag = lNfc_StatusFlag;
	}
	public Date getdNfc_CreateDate() {
		return dNfc_CreateDate;
	}
	public void setdNfc_CreateDate(Date dNfc_CreateDate) {
		this.dNfc_CreateDate = dNfc_CreateDate;
	}


	public Nfc cursorToBean(Cursor cursor){
		this.sNfc_ID = cursor.getString(cursor.getColumnIndex("sNfc_ID"));
		this.sNfc_Name = cursor.getString(cursor.getColumnIndex("sNfc_Name"));
		this.sNfc_NO = cursor.getString(cursor.getColumnIndex("sNfc_NO"));
		this.lNfc_StatusFlag = cursor.getInt(cursor.getColumnIndex("lNfc_StatusFlag"));
		this.dNfc_CreateDate = new Date(cursor.getLong(cursor.getColumnIndex("dNfc_CreateDate")));
		return this;
	}
}
