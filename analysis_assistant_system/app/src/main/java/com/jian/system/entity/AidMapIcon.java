package com.jian.system.entity;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

public class AidMapIcon {


	private String sAidIcon_ID;
	private String sAidIcon_Status;
	private String sAidIcon_StatusIcon;
	private String sAidIcon_AidID;



	public String getsAidIcon_ID() {
		return sAidIcon_ID;
	}
	public void setsAidIcon_ID(String sAidIcon_ID) {
		this.sAidIcon_ID = sAidIcon_ID;
	}
	public String getsAidIcon_Status() {
		return sAidIcon_Status;
	}
	public void setsAidIcon_Status(String sAidIcon_Status) {
		this.sAidIcon_Status = sAidIcon_Status;
	}
	public String getsAidIcon_StatusIcon() {
		return sAidIcon_StatusIcon;
	}
	public void setsAidIcon_StatusIcon(String sAidIcon_StatusIcon) {
		this.sAidIcon_StatusIcon = sAidIcon_StatusIcon;
	}
	public String getsAidIcon_AidID() {
		return sAidIcon_AidID;
	}
	public void setsAidIcon_AidID(String sAidIcon_AidID) {
		this.sAidIcon_AidID = sAidIcon_AidID;
	}

	public AidMapIcon cursorToBean(Cursor cursor){
		this.sAidIcon_ID = cursor.getString(cursor.getColumnIndex("sAidIcon_ID"));
		this.sAidIcon_Status = cursor.getString(cursor.getColumnIndex("sAidIcon_Status"));
		this.sAidIcon_StatusIcon = cursor.getString(cursor.getColumnIndex("sAidIcon_StatusIcon"));
		this.sAidIcon_AidID = cursor.getString(cursor.getColumnIndex("sAidIcon_AidID"));
		return this;
	}

	public ContentValues beanToValues(){
		ContentValues values = new ContentValues();
		values.put("sAidIcon_ID", sAidIcon_ID);
		values.put("sAidIcon_Status", sAidIcon_Status);
		values.put("sAidIcon_StatusIcon", sAidIcon_StatusIcon);
		values.put("sAidIcon_AidID", sAidIcon_AidID);
		return values;
	}
}
