package com.jian.system.entity;

import android.database.Cursor;

public class EquipSpareLamp {


	private String sEquip_ID;
	private float lSLamp_Watt;


	
	public String getsEquip_ID() {
		return sEquip_ID;
	}
	public void setsEquip_ID(String sEquip_ID) {
		this.sEquip_ID = sEquip_ID;
	}
	public float getlSLamp_Watt() {
		return lSLamp_Watt;
	}
	public void setlSLamp_Watt(float lSLamp_Watt) {
		this.lSLamp_Watt = lSLamp_Watt;
	}

	public EquipSpareLamp cursorToBean(Cursor cursor){
		this.sEquip_ID = cursor.getString(cursor.getColumnIndex("sEquip_ID"));
		this.lSLamp_Watt = cursor.getFloat(cursor.getColumnIndex("lSLamp_Watt"));

		return this;
	}
}
