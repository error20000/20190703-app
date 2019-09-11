package com.jian.system.entity;

import android.content.ContentValues;
import android.database.Cursor;

public class UserAid {


	private String sUserAid_ID;
	private String sUserAid_UserID;
	private String sUserAid_AidID;


	public String getsUserAid_ID() {
		return sUserAid_ID;
	}
	public void setsUserAid_ID(String sUserAid_ID) {
		this.sUserAid_ID = sUserAid_ID;
	}
	public String getsUserAid_UserID() {
		return sUserAid_UserID;
	}
	public void setsUserAid_UserID(String sUserAid_UserID) {
		this.sUserAid_UserID = sUserAid_UserID;
	}
	public String getsUserAid_AidID() {
		return sUserAid_AidID;
	}
	public void setsUserAid_AidID(String sUserAid_AidID) {
		this.sUserAid_AidID = sUserAid_AidID;
	}

	public UserAid cursorToBean(Cursor cursor){
		this.sUserAid_ID = cursor.getString(cursor.getColumnIndex("sUserAid_ID"));
		this.sUserAid_UserID = cursor.getString(cursor.getColumnIndex("sUserAid_UserID"));
		this.sUserAid_AidID = cursor.getString(cursor.getColumnIndex("sUserAid_AidID"));
		return this;
	}

	public ContentValues beanToValues(){
		ContentValues values = new ContentValues();
		values.put("sUserAid_ID", sUserAid_ID);
		values.put("sUserAid_UserID", sUserAid_UserID);
		values.put("sUserAid_AidID", sUserAid_AidID);
		return values;
	}
}
