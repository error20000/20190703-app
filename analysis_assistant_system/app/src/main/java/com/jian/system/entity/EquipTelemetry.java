package com.jian.system.entity;

import android.database.Cursor;

public class EquipTelemetry {


	private String sEquip_ID;
	private String sTelemetry_Mode;
	private float lTelemetry_Watt;
	private String sTelemetry_NO;
	private float lTelemetry_Volt;
	private String sTelemetry_SIM;


	
	public String getsEquip_ID() {
		return sEquip_ID;
	}
	public void setsEquip_ID(String sEquip_ID) {
		this.sEquip_ID = sEquip_ID;
	}
	public String getsTelemetry_Mode() {
		return sTelemetry_Mode;
	}
	public void setsTelemetry_Mode(String sTelemetry_Mode) {
		this.sTelemetry_Mode = sTelemetry_Mode;
	}
	public float getlTelemetry_Watt() {
		return lTelemetry_Watt;
	}
	public void setlTelemetry_Watt(float lTelemetry_Watt) {
		this.lTelemetry_Watt = lTelemetry_Watt;
	}
	public String getsTelemetry_NO() {
		return sTelemetry_NO;
	}
	public void setsTelemetry_NO(String sTelemetry_NO) {
		this.sTelemetry_NO = sTelemetry_NO;
	}
	public float getlTelemetry_Volt() {
		return lTelemetry_Volt;
	}
	public void setlTelemetry_Volt(float lTelemetry_Volt) {
		this.lTelemetry_Volt = lTelemetry_Volt;
	}
	public String getsTelemetry_SIM() {
		return sTelemetry_SIM;
	}
	public void setsTelemetry_SIM(String sTelemetry_SIM) {
		this.sTelemetry_SIM = sTelemetry_SIM;
	}

	public EquipTelemetry cursorToBean(Cursor cursor){
		this.sEquip_ID = cursor.getString(cursor.getColumnIndex("sEquip_ID"));
		this.sTelemetry_Mode = cursor.getString(cursor.getColumnIndex("sTelemetry_Mode"));
		this.lTelemetry_Watt = cursor.getFloat(cursor.getColumnIndex("lTelemetry_Watt"));
		this.sTelemetry_NO = cursor.getString(cursor.getColumnIndex("sTelemetry_NO"));
		this.lTelemetry_Volt = cursor.getFloat(cursor.getColumnIndex("lTelemetry_Volt"));
		this.sTelemetry_SIM = cursor.getString(cursor.getColumnIndex("sTelemetry_SIM"));

		return this;
	}
}
