package com.jian.system.entity;


import android.database.Cursor;

public class EquipBattery  {


	private String sEquip_ID;
	private String sBattery_NO;
	private String sBattery_Type;
	private float lBattery_Volt;
	private float lBattery_Watt;
	private String sBattery_Connect;



	
	public String getsEquip_ID() {
		return sEquip_ID;
	}
	public void setsEquip_ID(String sEquip_ID) {
		this.sEquip_ID = sEquip_ID;
	}
	public String getsBattery_NO() {
		return sBattery_NO;
	}
	public void setsBattery_NO(String sBattery_NO) {
		this.sBattery_NO = sBattery_NO;
	}
	public String getsBattery_Type() {
		return sBattery_Type;
	}
	public void setsBattery_Type(String sBattery_Type) {
		this.sBattery_Type = sBattery_Type;
	}
	public float getlBattery_Volt() {
		return lBattery_Volt;
	}
	public void setlBattery_Volt(float lBattery_Volt) {
		this.lBattery_Volt = lBattery_Volt;
	}
	public float getlBattery_Watt() {
		return lBattery_Watt;
	}
	public void setlBattery_Watt(float lBattery_Watt) {
		this.lBattery_Watt = lBattery_Watt;
	}
	public String getsBattery_Connect() {
		return sBattery_Connect;
	}
	public void setsBattery_Connect(String sBattery_Connect) {
		this.sBattery_Connect = sBattery_Connect;
	}

	public EquipBattery cursorToBean(Cursor cursor){
		this.sEquip_ID = cursor.getString(cursor.getColumnIndex("sEquip_ID"));
		this.sBattery_NO = cursor.getString(cursor.getColumnIndex("sBattery_NO"));
		this.sBattery_Type = cursor.getString(cursor.getColumnIndex("sBattery_Type"));
		this.lBattery_Volt = cursor.getFloat(cursor.getColumnIndex("lBattery_Volt"));
		this.lBattery_Watt = cursor.getFloat(cursor.getColumnIndex("lBattery_Watt"));
		this.sBattery_Connect = cursor.getString(cursor.getColumnIndex("sBattery_Connect"));

		return this;
	}
}
