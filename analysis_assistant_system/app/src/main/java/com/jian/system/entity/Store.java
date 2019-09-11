package com.jian.system.entity;

import android.content.ContentValues;
import android.database.Cursor;

public class Store {


	private String sStore_ID;
	private String sStore_Name;
	private String sStore_Level1;
	private String sStore_Level2;
	private String sStore_Level3;
	private String sStore_Parent;
	private int lStore_Limit;
	

	
	public String getsStore_ID() {
		return sStore_ID;
	}
	public void setsStore_ID(String sStore_ID) {
		this.sStore_ID = sStore_ID;
	}
	public String getsStore_Name() {
		return sStore_Name;
	}
	public void setsStore_Name(String sStore_Name) {
		this.sStore_Name = sStore_Name;
	}
	public String getsStore_Level1() {
		return sStore_Level1;
	}
	public void setsStore_Level1(String sStore_Level1) {
		this.sStore_Level1 = sStore_Level1;
	}
	public String getsStore_Level2() {
		return sStore_Level2;
	}
	public void setsStore_Level2(String sStore_Level2) {
		this.sStore_Level2 = sStore_Level2;
	}
	public String getsStore_Level3() {
		return sStore_Level3;
	}
	public void setsStore_Level3(String sStore_Level3) {
		this.sStore_Level3 = sStore_Level3;
	}
	public String getsStore_Parent() {
		return sStore_Parent;
	}
	public void setsStore_Parent(String sStore_Parent) {
		this.sStore_Parent = sStore_Parent;
	}
	public int getlStore_Limit() {
		return lStore_Limit;
	}
	public void setlStore_Limit(int lStore_Limit) {
		this.lStore_Limit = lStore_Limit;
	}

	public Store cursorToBean(Cursor cursor){
		this.sStore_ID = cursor.getString(cursor.getColumnIndex("sStore_ID"));
		this.sStore_Name = cursor.getString(cursor.getColumnIndex("sStore_Name"));
		this.sStore_Level1 = cursor.getString(cursor.getColumnIndex("sStore_Level1"));
		this.sStore_Level2 = cursor.getString(cursor.getColumnIndex("sStore_Level2"));
		this.sStore_Level3 = cursor.getString(cursor.getColumnIndex("sStore_Level3"));
		this.sStore_Parent = cursor.getString(cursor.getColumnIndex("sStore_Parent"));
		this.lStore_Limit = cursor.getInt(cursor.getColumnIndex("lStore_Limit"));
		return this;
	}

	public ContentValues beanToValues(){
		ContentValues values = new ContentValues();
		values.put("sStore_ID", sStore_ID);
		values.put("sStore_Name", sStore_Name);
		values.put("sStore_Level1", sStore_Level1);
		values.put("sStore_Level2", sStore_Level2);
		values.put("sStore_Level3", sStore_Level3);
		values.put("sStore_Parent", sStore_Parent);
		values.put("lStore_Limit", lStore_Limit);
		return values;
	}
}
