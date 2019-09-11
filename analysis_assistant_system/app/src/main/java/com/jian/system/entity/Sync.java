package com.jian.system.entity;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

public class Sync {


	private String sSync_TableName;
	private Date dSync_UpdateDate;

	public String getsSync_TableName() {
		return sSync_TableName;
	}
	public void setsSync_TableName(String sSync_TableName) {
		this.sSync_TableName = sSync_TableName;
	}
	public Date getdSync_UpdateDate() {
		return dSync_UpdateDate;
	}
	public void setdSync_UpdateDate(Date dSync_UpdateDate) {
		this.dSync_UpdateDate = dSync_UpdateDate;
	}

	public Sync cursorToBean(Cursor cursor){
		this.sSync_TableName = cursor.getString(cursor.getColumnIndex("sSync_TableName"));
		this.dSync_UpdateDate = new Date(cursor.getLong(cursor.getColumnIndex("dSync_UpdateDate")));
		return this;
	}

	public ContentValues beanToValues(){
		ContentValues values = new ContentValues();
		values.put("sSync_TableName", sSync_TableName);
		values.put("dSync_UpdateDate", dSync_UpdateDate.getTime());
		return values;
	}
}
