package com.jian.system.entity;

import android.content.ContentValues;
import android.database.Cursor;

public class EquipSolarEnergy {


	private String sEquip_ID;
	private String sSolar_NO;
	private String sSolar_Type;
	private float lSolar_Volt;
	private float lSolar_Watt;
	private String sSolar_Connect;
	

	
	public String getsEquip_ID() {
		return sEquip_ID;
	}
	public void setsEquip_ID(String sEquip_ID) {
		this.sEquip_ID = sEquip_ID;
	}
	public String getsSolar_NO() {
		return sSolar_NO;
	}
	public void setsSolar_NO(String sSolar_NO) {
		this.sSolar_NO = sSolar_NO;
	}
	public String getsSolar_Type() {
		return sSolar_Type;
	}
	public void setsSolar_Type(String sSolar_Type) {
		this.sSolar_Type = sSolar_Type;
	}
	public float getlSolar_Volt() {
		return lSolar_Volt;
	}
	public void setlSolar_Volt(float lSolar_Volt) {
		this.lSolar_Volt = lSolar_Volt;
	}
	public float getlSolar_Watt() {
		return lSolar_Watt;
	}
	public void setlSolar_Watt(float lSolar_Watt) {
		this.lSolar_Watt = lSolar_Watt;
	}
	public String getsSolar_Connect() {
		return sSolar_Connect;
	}
	public void setsSolar_Connect(String sSolar_Connect) {
		this.sSolar_Connect = sSolar_Connect;
	}

	public EquipSolarEnergy cursorToBean(Cursor cursor){
		this.sEquip_ID = cursor.getString(cursor.getColumnIndex("sEquip_ID"));
		this.sSolar_NO = cursor.getString(cursor.getColumnIndex("sSolar_NO"));
		this.sSolar_Type = cursor.getString(cursor.getColumnIndex("sSolar_Type"));
		this.lSolar_Volt = cursor.getFloat(cursor.getColumnIndex("lSolar_Volt"));
		this.lSolar_Watt = cursor.getFloat(cursor.getColumnIndex("lSolar_Watt"));
		this.sSolar_Connect = cursor.getString(cursor.getColumnIndex("sSolar_Connect"));

		return this;
	}

	public ContentValues beanToValues(){
		ContentValues values = new ContentValues();
		values.put("sEquip_ID", sEquip_ID);
		values.put("sSolar_NO", sSolar_NO);
		values.put("sSolar_Type", sSolar_Type);
		values.put("lSolar_Volt", lSolar_Volt);
		values.put("lSolar_Watt", lSolar_Watt);
		values.put("sSolar_Connect", sSolar_Connect);
		return values;
	}
}
