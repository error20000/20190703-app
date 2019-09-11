package com.jian.system.entity;

import android.content.ContentValues;
import android.database.Cursor;

public class AidTypeMapIcon {


	private String sAidTypeIcon_ID;
	private String sAidTypeIcon_Status;
	private String sAidTypeIcon_StatusIcon;
	private String sAidTypeIcon_Type;


	public String getsAidTypeIcon_ID() {
		return sAidTypeIcon_ID;
	}
	public void setsAidTypeIcon_ID(String sAidTypeIcon_ID) {
		this.sAidTypeIcon_ID = sAidTypeIcon_ID;
	}
	public String getsAidTypeIcon_Status() {
		return sAidTypeIcon_Status;
	}
	public void setsAidTypeIcon_Status(String sAidTypeIcon_Status) {
		this.sAidTypeIcon_Status = sAidTypeIcon_Status;
	}
	public String getsAidTypeIcon_StatusIcon() {
		return sAidTypeIcon_StatusIcon;
	}
	public void setsAidTypeIcon_StatusIcon(String sAidTypeIcon_StatusIcon) {
		this.sAidTypeIcon_StatusIcon = sAidTypeIcon_StatusIcon;
	}
	public String getsAidTypeIcon_Type() {
		return sAidTypeIcon_Type;
	}
	public void setsAidTypeIcon_Type(String sAidTypeIcon_Type) {
		this.sAidTypeIcon_Type = sAidTypeIcon_Type;
	}

	public AidTypeMapIcon cursorToBean(Cursor cursor){
		this.sAidTypeIcon_ID = cursor.getString(cursor.getColumnIndex("sAidTypeIcon_ID"));
		this.sAidTypeIcon_Status = cursor.getString(cursor.getColumnIndex("sAidTypeIcon_Status"));
		this.sAidTypeIcon_StatusIcon = cursor.getString(cursor.getColumnIndex("sAidTypeIcon_StatusIcon"));
		this.sAidTypeIcon_Type = cursor.getString(cursor.getColumnIndex("sAidTypeIcon_Type"));
		return this;
	}

	public ContentValues beanToValues(){
		ContentValues values = new ContentValues();
		values.put("sAidTypeIcon_ID", sAidTypeIcon_ID);
		values.put("sAidTypeIcon_Status", sAidTypeIcon_Status);
		values.put("sAidTypeIcon_StatusIcon", sAidTypeIcon_StatusIcon);
		values.put("sAidTypeIcon_Type", sAidTypeIcon_Type);
		return values;
	}
}
