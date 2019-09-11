package com.jian.system.entity;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

public class EquipAis {


	private String sEquip_ID;
	private String sAis_MMSIX;

	

	
	public String getsEquip_ID() {
		return sEquip_ID;
	}
	public void setsEquip_ID(String sEquip_ID) {
		this.sEquip_ID = sEquip_ID;
	}
	public String getsAis_MMSIX() {
		return sAis_MMSIX;
	}
	public void setsAis_MMSIX(String sAis_MMSIX) {
		this.sAis_MMSIX = sAis_MMSIX;
	}


	public EquipAis cursorToBean(Cursor cursor){
		this.sEquip_ID = cursor.getString(cursor.getColumnIndex("sEquip_ID"));
		this.sAis_MMSIX = cursor.getString(cursor.getColumnIndex("sAis_MMSIX"));
		return this;
	}

	public ContentValues beanToValues(){
		ContentValues values = new ContentValues();
		values.put("sEquip_ID", sEquip_ID);
		values.put("sAis_MMSIX", sAis_MMSIX);
		return values;
	}
}
