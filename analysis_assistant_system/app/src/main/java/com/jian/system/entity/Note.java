package com.jian.system.entity;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

public class Note {


	private String sNote_ID;
	private String sNote_Content;
	private String sNote_UserID;
	private Date dNote_CreateDate;
	private Date dNote_UpdateDate;


	public String getsNote_ID() {
		return sNote_ID;
	}
	public void setsNote_ID(String sNote_ID) {
		this.sNote_ID = sNote_ID;
	}
	public String getsNote_Content() {
		return sNote_Content;
	}
	public void setsNote_Content(String sNote_Content) {
		this.sNote_Content = sNote_Content;
	}
	public String getsNote_UserID() {
		return sNote_UserID;
	}
	public void setsNote_UserID(String sNote_UserID) {
		this.sNote_UserID = sNote_UserID;
	}
	public Date getdNote_CreateDate() {
		return dNote_CreateDate;
	}
	public void setdNote_CreateDate(Date dNote_CreateDate) {
		this.dNote_CreateDate = dNote_CreateDate;
	}
	public Date getdNote_UpdateDate() {
		return dNote_UpdateDate;
	}
	public void setdNote_UpdateDate(Date dNote_UpdateDate) {
		this.dNote_UpdateDate = dNote_UpdateDate;
	}

	public Note cursorToBean(Cursor cursor){
		this.sNote_ID = cursor.getString(cursor.getColumnIndex("sNote_ID"));
		this.sNote_Content = cursor.getString(cursor.getColumnIndex("sNote_Content"));
		this.sNote_UserID = cursor.getString(cursor.getColumnIndex("sNote_UserID"));
		this.dNote_CreateDate = new Date(cursor.getLong(cursor.getColumnIndex("dNote_CreateDate")));
		this.dNote_UpdateDate = new Date(cursor.getLong(cursor.getColumnIndex("dNote_UpdateDate")));
		return this;
	}

	public ContentValues beanToValues(){
		ContentValues values = new ContentValues();
		values.put("sNote_ID", sNote_ID);
		values.put("sNote_Content", sNote_Content);
		values.put("sNote_UserID", sNote_UserID);
		values.put("dNote_CreateDate", dNote_CreateDate.getTime());
		values.put("dNote_UpdateDate", dNote_UpdateDate.getTime());
		return values;
	}
}
