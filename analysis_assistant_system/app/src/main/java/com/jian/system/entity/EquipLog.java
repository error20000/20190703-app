package com.jian.system.entity;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;


public class EquipLog  {


	private String sELog_ID;
	private String sELog_Type;
	private Date dELog_CreateDate;
	private String sELog_UserID;
	private String sELog_EquipID;
	private String sELog_Describe;
	private String sELog_Remarks;
	private String sELog_IP;
	private String sELog_StoreLv1;
	private String sELog_StoreLv2;
	private String sELog_StoreLv3;
	private String sELog_StoreLv4;
	private String sELog_AidID;
	private String sELog_StoreLv1Name;
	private String sELog_StoreLv2Name;
	private String sELog_StoreLv3Name;
	private String sELog_StoreLv4Name;
	private String sELog_AidName;
	
	
	
	public String getsELog_ID() {
		return sELog_ID;
	}
	public void setsELog_ID(String sELog_ID) {
		this.sELog_ID = sELog_ID;
	}
	public String getsELog_Type() {
		return sELog_Type;
	}
	public void setsELog_Type(String sELog_Type) {
		this.sELog_Type = sELog_Type;
	}
	public Date getdELog_CreateDate() {
		return dELog_CreateDate;
	}
	public void setdELog_CreateDate(Date dELog_CreateDate) {
		this.dELog_CreateDate = dELog_CreateDate;
	}
	public String getsELog_UserID() {
		return sELog_UserID;
	}
	public void setsELog_UserID(String sELog_UserID) {
		this.sELog_UserID = sELog_UserID;
	}
	public String getsELog_EquipID() {
		return sELog_EquipID;
	}
	public void setsELog_EquipID(String sELog_EquipID) {
		this.sELog_EquipID = sELog_EquipID;
	}
	public String getsELog_Describe() {
		return sELog_Describe;
	}
	public void setsELog_Describe(String sELog_Describe) {
		this.sELog_Describe = sELog_Describe;
	}
	public String getsELog_Remarks() {
		return sELog_Remarks;
	}
	public void setsELog_Remarks(String sELog_Remarks) {
		this.sELog_Remarks = sELog_Remarks;
	}
	public String getsELog_IP() {
		return sELog_IP;
	}
	public void setsELog_IP(String sELog_IP) {
		this.sELog_IP = sELog_IP;
	}

	public String getsELog_StoreLv1() {
		return sELog_StoreLv1;
	}

	public void setsELog_StoreLv1(String sELog_StoreLv1) {
		this.sELog_StoreLv1 = sELog_StoreLv1;
	}

	public String getsELog_StoreLv2() {
		return sELog_StoreLv2;
	}

	public void setsELog_StoreLv2(String sELog_StoreLv2) {
		this.sELog_StoreLv2 = sELog_StoreLv2;
	}

	public String getsELog_StoreLv3() {
		return sELog_StoreLv3;
	}

	public void setsELog_StoreLv3(String sELog_StoreLv3) {
		this.sELog_StoreLv3 = sELog_StoreLv3;
	}

	public String getsELog_StoreLv4() {
		return sELog_StoreLv4;
	}

	public void setsELog_StoreLv4(String sELog_StoreLv4) {
		this.sELog_StoreLv4 = sELog_StoreLv4;
	}

	public String getsELog_AidID() {
		return sELog_AidID;
	}

	public void setsELog_AidID(String sELog_AidID) {
		this.sELog_AidID = sELog_AidID;
	}

	public String getsELog_StoreLv1Name() {
		return sELog_StoreLv1Name;
	}

	public void setsELog_StoreLv1Name(String sELog_StoreLv1Name) {
		this.sELog_StoreLv1Name = sELog_StoreLv1Name;
	}

	public String getsELog_StoreLv2Name() {
		return sELog_StoreLv2Name;
	}

	public void setsELog_StoreLv2Name(String sELog_StoreLv2Name) {
		this.sELog_StoreLv2Name = sELog_StoreLv2Name;
	}

	public String getsELog_StoreLv3Name() {
		return sELog_StoreLv3Name;
	}

	public void setsELog_StoreLv3Name(String sELog_StoreLv3Name) {
		this.sELog_StoreLv3Name = sELog_StoreLv3Name;
	}

	public String getsELog_StoreLv4Name() {
		return sELog_StoreLv4Name;
	}

	public void setsELog_StoreLv4Name(String sELog_StoreLv4Name) {
		this.sELog_StoreLv4Name = sELog_StoreLv4Name;
	}

	public String getsELog_AidName() {
		return sELog_AidName;
	}

	public void setsELog_AidName(String sELog_AidName) {
		this.sELog_AidName = sELog_AidName;
	}

	public EquipLog cursorToBean(Cursor cursor){
		this.sELog_ID = cursor.getString(cursor.getColumnIndex("sELog_ID"));
		this.sELog_Type = cursor.getString(cursor.getColumnIndex("sELog_Type"));
		this.dELog_CreateDate = new Date(cursor.getLong(cursor.getColumnIndex("dELog_CreateDate")));
		this.sELog_UserID = cursor.getString(cursor.getColumnIndex("sELog_UserID"));
		this.sELog_EquipID = cursor.getString(cursor.getColumnIndex("sELog_EquipID"));
		this.sELog_Describe = cursor.getString(cursor.getColumnIndex("sELog_Describe"));
		this.sELog_Remarks = cursor.getString(cursor.getColumnIndex("sELog_Remarks"));
		this.sELog_IP = cursor.getString(cursor.getColumnIndex("sELog_IP"));
		this.sELog_StoreLv1 = cursor.getString(cursor.getColumnIndex("sELog_StoreLv1"));
		this.sELog_StoreLv2 = cursor.getString(cursor.getColumnIndex("sELog_StoreLv2"));
		this.sELog_StoreLv3 = cursor.getString(cursor.getColumnIndex("sELog_StoreLv3"));
		this.sELog_StoreLv4 = cursor.getString(cursor.getColumnIndex("sELog_StoreLv4"));
		this.sELog_AidID = cursor.getString(cursor.getColumnIndex("sELog_AidID"));

		return this;
	}

	public ContentValues beanToValues(){
		ContentValues values = new ContentValues();
		values.put("sELog_ID", sELog_ID);
		values.put("sELog_Type", sELog_Type);
		values.put("dELog_CreateDate", dELog_CreateDate.getTime());
		values.put("sELog_UserID", sELog_UserID);
		values.put("sELog_EquipID", sELog_EquipID);
		values.put("sELog_Describe", sELog_Describe);
		values.put("sELog_Remarks", sELog_Remarks);
		values.put("sELog_IP", sELog_IP);
		values.put("sELog_StoreLv1", sELog_StoreLv1);
		values.put("sELog_StoreLv2", sELog_StoreLv2);
		values.put("sELog_StoreLv3", sELog_StoreLv3);
		values.put("sELog_StoreLv4", sELog_StoreLv4);
		values.put("sELog_AidID", sELog_AidID);
		return values;
	}
}
