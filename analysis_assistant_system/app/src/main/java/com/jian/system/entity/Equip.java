package com.jian.system.entity;

import android.database.Cursor;

import java.util.Date;

public class Equip {

	private String sEquip_ID;
	private String sEquip_NO;
	private String sEquip_Name;
	private String sEquip_StoreLv1;
	private String sEquip_StoreLv2;
	private String sEquip_StoreLv3;
	private String sEquip_StoreLv4;
	private String sEquip_Type;
	private String sEquip_Status;
	private String sEquip_NfcID;
	private String sEquip_AidID;
	private Date dEquip_CreateDate;
	private String sEquip_Icon;
	private String sEquip_Manufacturer;
	private String sEquip_MModel;
	private Date dEquip_ArrivalDate;
	private Date dEquip_UseDate;
	private Date dEquip_StoreDate;
	private String sEquip_MBrand;
	private Date dEquip_DumpDate;
	
	
	public String getsEquip_ID() {
		return sEquip_ID;
	}
	public void setsEquip_ID(String sEquip_ID) {
		this.sEquip_ID = sEquip_ID;
	}
	public String getsEquip_Name() {
		return sEquip_Name;
	}
	public void setsEquip_Name(String sEquip_Name) {
		this.sEquip_Name = sEquip_Name;
	}
	public String getsEquip_StoreLv1() {
		return sEquip_StoreLv1;
	}
	public void setsEquip_StoreLv1(String sEquip_StoreLv1) {
		this.sEquip_StoreLv1 = sEquip_StoreLv1;
	}
	public String getsEquip_StoreLv2() {
		return sEquip_StoreLv2;
	}
	public void setsEquip_StoreLv2(String sEquip_StoreLv2) {
		this.sEquip_StoreLv2 = sEquip_StoreLv2;
	}
	public String getsEquip_StoreLv3() {
		return sEquip_StoreLv3;
	}
	public void setsEquip_StoreLv3(String sEquip_StoreLv3) {
		this.sEquip_StoreLv3 = sEquip_StoreLv3;
	}
	public String getsEquip_StoreLv4() {
		return sEquip_StoreLv4;
	}
	public void setsEquip_StoreLv4(String sEquip_StoreLv4) {
		this.sEquip_StoreLv4 = sEquip_StoreLv4;
	}
	public String getsEquip_Type() {
		return sEquip_Type;
	}
	public void setsEquip_Type(String sEquip_Type) {
		this.sEquip_Type = sEquip_Type;
	}
	public String getsEquip_NfcID() {
		return sEquip_NfcID;
	}
	public void setsEquip_NfcID(String sEquip_NfcID) {
		this.sEquip_NfcID = sEquip_NfcID;
	}
	public String getsEquip_AidID() {
		return sEquip_AidID;
	}
	public void setsEquip_AidID(String sEquip_AidID) {
		this.sEquip_AidID = sEquip_AidID;
	}
	public Date getdEquip_CreateDate() {
		return dEquip_CreateDate;
	}
	public void setdEquip_CreateDate(Date dEquip_CreateDate) {
		this.dEquip_CreateDate = dEquip_CreateDate;
	}
	public String getsEquip_NO() {
		return sEquip_NO;
	}
	public void setsEquip_NO(String sEquip_NO) {
		this.sEquip_NO = sEquip_NO;
	}
	public String getsEquip_Status() {
		return sEquip_Status;
	}
	public void setsEquip_Status(String sEquip_Status) {
		this.sEquip_Status = sEquip_Status;
	}
	public String getsEquip_Icon() {
		return sEquip_Icon;
	}
	public void setsEquip_Icon(String sEquip_Icon) {
		this.sEquip_Icon = sEquip_Icon;
	}
	public String getsEquip_Manufacturer() {
		return sEquip_Manufacturer;
	}
	public void setsEquip_Manufacturer(String sEquip_Manufacturer) {
		this.sEquip_Manufacturer = sEquip_Manufacturer;
	}
	public String getsEquip_MModel() {
		return sEquip_MModel;
	}
	public void setsEquip_MModel(String sEquip_MModel) {
		this.sEquip_MModel = sEquip_MModel;
	}
	public Date getdEquip_ArrivalDate() {
		return dEquip_ArrivalDate;
	}
	public void setdEquip_ArrivalDate(Date dEquip_ArrivalDate) {
		this.dEquip_ArrivalDate = dEquip_ArrivalDate;
	}
	public Date getdEquip_UseDate() {
		return dEquip_UseDate;
	}
	public void setdEquip_UseDate(Date dEquip_UseDate) {
		this.dEquip_UseDate = dEquip_UseDate;
	}
	public Date getdEquip_StoreDate() {
		return dEquip_StoreDate;
	}
	public void setdEquip_StoreDate(Date dEquip_StoreDate) {
		this.dEquip_StoreDate = dEquip_StoreDate;
	}
	public String getsEquip_MBrand() {
		return sEquip_MBrand;
	}
	public void setsEquip_MBrand(String sEquip_MBrand) {
		this.sEquip_MBrand = sEquip_MBrand;
	}
	public Date getdEquip_DumpDate() {
		return dEquip_DumpDate;
	}
	public void setdEquip_DumpDate(Date dEquip_DumpDate) {
		this.dEquip_DumpDate = dEquip_DumpDate;
	}


	public Equip cursorToBean(Cursor cursor){
		this.sEquip_ID = cursor.getString(cursor.getColumnIndex("sEquip_ID"));
		this.sEquip_NO = cursor.getString(cursor.getColumnIndex("sEquip_NO"));
		this.sEquip_Name = cursor.getString(cursor.getColumnIndex("sEquip_Name"));
		this.sEquip_StoreLv1 = cursor.getString(cursor.getColumnIndex("sEquip_StoreLv1"));
		this.sEquip_StoreLv2 = cursor.getString(cursor.getColumnIndex("sEquip_StoreLv2"));
		this.sEquip_StoreLv3 = cursor.getString(cursor.getColumnIndex("sEquip_StoreLv3"));
		this.sEquip_StoreLv4 = cursor.getString(cursor.getColumnIndex("sEquip_StoreLv4"));
		this.sEquip_Type = cursor.getString(cursor.getColumnIndex("sEquip_Type"));
		this.sEquip_Status = cursor.getString(cursor.getColumnIndex("sEquip_Status"));
		this.sEquip_NfcID = cursor.getString(cursor.getColumnIndex("sEquip_NfcID"));
		this.sEquip_AidID = cursor.getString(cursor.getColumnIndex("sEquip_AidID"));
		this.dEquip_CreateDate = new Date(cursor.getLong(cursor.getColumnIndex("dEquip_CreateDate")));
		this.sEquip_Icon = cursor.getString(cursor.getColumnIndex("sEquip_Icon"));
		this.sEquip_Manufacturer = cursor.getString(cursor.getColumnIndex("sEquip_Manufacturer"));
		this.sEquip_MModel = cursor.getString(cursor.getColumnIndex("sEquip_MModel"));
		this.dEquip_ArrivalDate = new Date(cursor.getLong(cursor.getColumnIndex("dEquip_ArrivalDate")));
		this.dEquip_UseDate = new Date(cursor.getLong(cursor.getColumnIndex("dEquip_UseDate")));
		this.dEquip_StoreDate = new Date(cursor.getLong(cursor.getColumnIndex("dEquip_StoreDate")));
		this.sEquip_MBrand = cursor.getString(cursor.getColumnIndex("sEquip_MBrand"));
		this.dEquip_DumpDate = new Date(cursor.getLong(cursor.getColumnIndex("dEquip_DumpDate")));

		return this;
	}

}
