package com.jian.system.entity;

import android.content.ContentValues;
import android.database.Cursor;

public class EquipLamp {


	private String sEquip_ID;
	private String sLamp_NO;
	private String sLamp_Brand;
	private String sLamp_Type;
	private float lLamp_InputVolt;
	private float lLamp_Watt;
	private String sLamp_Lens;
	private int lLamp_TelemetryFlag;
	private String sLamp_Telemetry;
	

	
	public String getsEquip_ID() {
		return sEquip_ID;
	}
	public void setsEquip_ID(String sEquip_ID) {
		this.sEquip_ID = sEquip_ID;
	}
	public String getsLamp_NO() {
		return sLamp_NO;
	}
	public void setsLamp_NO(String sLamp_NO) {
		this.sLamp_NO = sLamp_NO;
	}
	public String getsLamp_Brand() {
		return sLamp_Brand;
	}
	public void setsLamp_Brand(String sLamp_Brand) {
		this.sLamp_Brand = sLamp_Brand;
	}
	public String getsLamp_Type() {
		return sLamp_Type;
	}
	public void setsLamp_Type(String sLamp_Type) {
		this.sLamp_Type = sLamp_Type;
	}
	public float getlLamp_InputVolt() {
		return lLamp_InputVolt;
	}
	public void setlLamp_InputVolt(float lLamp_InputVolt) {
		this.lLamp_InputVolt = lLamp_InputVolt;
	}
	public float getlLamp_Watt() {
		return lLamp_Watt;
	}
	public void setlLamp_Watt(float lLamp_Watt) {
		this.lLamp_Watt = lLamp_Watt;
	}
	public String getsLamp_Lens() {
		return sLamp_Lens;
	}
	public void setsLamp_Lens(String sLamp_Lens) {
		this.sLamp_Lens = sLamp_Lens;
	}
	public int getlLamp_TelemetryFlag() {
		return lLamp_TelemetryFlag;
	}
	public void setlLamp_TelemetryFlag(int lLamp_TelemetryFlag) {
		this.lLamp_TelemetryFlag = lLamp_TelemetryFlag;
	}
	public String getsLamp_Telemetry() {
		return sLamp_Telemetry;
	}
	public void setsLamp_Telemetry(String sLamp_Telemetry) {
		this.sLamp_Telemetry = sLamp_Telemetry;
	}

	public EquipLamp cursorToBean(Cursor cursor){
		this.sEquip_ID = cursor.getString(cursor.getColumnIndex("sEquip_ID"));
		this.sLamp_NO = cursor.getString(cursor.getColumnIndex("sLamp_NO"));
		this.sLamp_Brand = cursor.getString(cursor.getColumnIndex("sLamp_Brand"));
		this.sLamp_Type = cursor.getString(cursor.getColumnIndex("sLamp_Type"));
		this.lLamp_InputVolt = cursor.getFloat(cursor.getColumnIndex("lLamp_InputVolt"));
		this.lLamp_Watt = cursor.getFloat(cursor.getColumnIndex("lLamp_Watt"));
		this.sLamp_Lens = cursor.getString(cursor.getColumnIndex("sLamp_Lens"));
		this.lLamp_TelemetryFlag = cursor.getInt(cursor.getColumnIndex("lLamp_TelemetryFlag"));
		this.sLamp_Telemetry = cursor.getString(cursor.getColumnIndex("sLamp_Telemetry"));

		return this;
	}

	public ContentValues beanToValues(){
		ContentValues values = new ContentValues();
		values.put("sEquip_ID", sEquip_ID);
		values.put("sLamp_NO", sLamp_NO);
		values.put("sLamp_Brand", sLamp_Brand);
		values.put("sLamp_Type", sLamp_Type);
		values.put("lLamp_InputVolt", lLamp_InputVolt);
		values.put("lLamp_Watt", lLamp_Watt);
		values.put("sLamp_Lens", sLamp_Lens);
		values.put("lLamp_TelemetryFlag", lLamp_TelemetryFlag);
		values.put("sLamp_Telemetry", sLamp_Telemetry);
		return values;
	}
}
