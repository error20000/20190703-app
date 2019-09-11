package com.jian.system.entity;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

public class AidEquip {


	private String sAidEquip_ID;
	private String sAidEquip_AidID;
	private String sAidEquip_EquipID;
	private Date dAidEquip_CreateDate;

	private String sAidEquip_EquipName;
	private String sAidEquip_EquipNO;
	private String sAidEquip_EquipType;
	private String sAidEquip_EquipTypeName;

	
	public String getsAidEquip_ID() {
		return sAidEquip_ID;
	}
	public void setsAidEquip_ID(String sAidEquip_ID) {
		this.sAidEquip_ID = sAidEquip_ID;
	}
	public String getsAidEquip_AidID() {
		return sAidEquip_AidID;
	}
	public void setsAidEquip_AidID(String sAidEquip_AidID) {
		this.sAidEquip_AidID = sAidEquip_AidID;
	}
	public String getsAidEquip_EquipID() {
		return sAidEquip_EquipID;
	}
	public void setsAidEquip_EquipID(String sAidEquip_EquipID) {
		this.sAidEquip_EquipID = sAidEquip_EquipID;
	}
	public Date getdAidEquip_CreateDate() {
		return dAidEquip_CreateDate;
	}
	public void setdAidEquip_CreateDate(Date dAidEquip_CreateDate) {
		this.dAidEquip_CreateDate = dAidEquip_CreateDate;
	}
	public String getsAidEquip_EquipName() {
		return sAidEquip_EquipName;
	}
	public void setsAidEquip_EquipName(String sAidEquip_EquipName) {
		this.sAidEquip_EquipName = sAidEquip_EquipName;
	}
	public String getsAidEquip_EquipNO() {
		return sAidEquip_EquipNO;
	}
	public void setsAidEquip_EquipNO(String sAidEquip_EquipNO) {
		this.sAidEquip_EquipNO = sAidEquip_EquipNO;
	}
	public String getsAidEquip_EquipType() {
		return sAidEquip_EquipType;
	}
	public void setsAidEquip_EquipType(String sAidEquip_EquipType) {
		this.sAidEquip_EquipType = sAidEquip_EquipType;
	}
	public String getsAidEquip_EquipTypeName() {
		return sAidEquip_EquipTypeName;
	}
	public void setsAidEquip_EquipTypeName(String sAidEquip_EquipTypeName) {
		this.sAidEquip_EquipTypeName = sAidEquip_EquipTypeName;
	}

	public AidEquip cursorToBean(Cursor cursor){
		this.sAidEquip_ID = cursor.getString(cursor.getColumnIndex("sAidEquip_ID"));
		this.sAidEquip_AidID = cursor.getString(cursor.getColumnIndex("sAidEquip_AidID"));
		this.sAidEquip_EquipID = cursor.getString(cursor.getColumnIndex("sAidEquip_EquipID"));
		this.dAidEquip_CreateDate = new Date(cursor.getLong(cursor.getColumnIndex("dAidEquip_CreateDate")));
		return this;
	}

	public ContentValues beanToValues(){
		ContentValues values = new ContentValues();
		values.put("sAidEquip_ID", sAidEquip_ID);
		values.put("sAidEquip_AidID", sAidEquip_AidID);
		values.put("sAidEquip_EquipID", sAidEquip_EquipID);
		values.put("dAidEquip_CreateDate", dAidEquip_CreateDate.getTime());
		return values;
	}
}
