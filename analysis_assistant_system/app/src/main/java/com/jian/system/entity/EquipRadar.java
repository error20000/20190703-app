package com.jian.system.entity;


import android.database.Cursor;

public class EquipRadar {


	private String sEquip_ID;
	private String sRadar_NO;
	private String sRadar_Band;
	

	
	public String getsEquip_ID() {
		return sEquip_ID;
	}
	public void setsEquip_ID(String sEquip_ID) {
		this.sEquip_ID = sEquip_ID;
	}
	public String getsRadar_NO() {
		return sRadar_NO;
	}
	public void setsRadar_NO(String sRadar_NO) {
		this.sRadar_NO = sRadar_NO;
	}
	public String getsRadar_Band() {
		return sRadar_Band;
	}
	public void setsRadar_Band(String sRadar_Band) {
		this.sRadar_Band = sRadar_Band;
	}

	public EquipRadar cursorToBean(Cursor cursor){
		this.sEquip_ID = cursor.getString(cursor.getColumnIndex("sEquip_ID"));
		this.sRadar_NO = cursor.getString(cursor.getColumnIndex("sRadar_NO"));
		this.sRadar_Band = cursor.getString(cursor.getColumnIndex("sRadar_Band"));

		return this;
	}
}
