package com.jian.system.entity;

import android.content.ContentValues;
import android.database.Cursor;

public class EquipViceLamp {


	private String sEquip_ID;
	private float lVLamp_Watt;


	
	public String getsEquip_ID() {
		return sEquip_ID;
	}
	public void setsEquip_ID(String sEquip_ID) {
		this.sEquip_ID = sEquip_ID;
	}
	public float getlVLamp_Watt() {
		return lVLamp_Watt;
	}
	public void setlVLamp_Watt(float lVLamp_Watt) {
		this.lVLamp_Watt = lVLamp_Watt;
	}

	public EquipViceLamp cursorToBean(Cursor cursor){
		this.sEquip_ID = cursor.getString(cursor.getColumnIndex("sEquip_ID"));
		this.lVLamp_Watt = cursor.getFloat(cursor.getColumnIndex("lVLamp_Watt"));

		return this;
	}

	public ContentValues beanToValues(){
		ContentValues values = new ContentValues();
		values.put("sEquip_ID", sEquip_ID);
		values.put("lVLamp_Watt", lVLamp_Watt);
		return values;
	}
	
}
