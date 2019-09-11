package com.jian.system.entity;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

public class Dict {


	private String sDict_ID;
	private String sDict_NO;
	private String sDict_Name;
	private Date dDict_CreateDate;
	private String sDict_UserID;
	private String sDict_DictTypeNO;
	private Date dDict_UpdateDate;
	private String sDict_UpdateUserID;
	private int lDict_SysFlag;
	private String sDict_Describe;
	private String sDict_Picture;
	private String sDict_Link;
	private String sDict_Color;
	


	public String getsDict_ID() {
		return sDict_ID;
	}
	public void setsDict_ID(String sDict_ID) {
		this.sDict_ID = sDict_ID;
	}
	public String getsDict_NO() {
		return sDict_NO;
	}
	public void setsDict_NO(String sDict_NO) {
		this.sDict_NO = sDict_NO;
	}
	public String getsDict_Name() {
		return sDict_Name;
	}
	public void setsDict_Name(String sDict_Name) {
		this.sDict_Name = sDict_Name;
	}
	public Date getdDict_CreateDate() {
		return dDict_CreateDate;
	}
	public void setdDict_CreateDate(Date dDict_CreateDate) {
		this.dDict_CreateDate = dDict_CreateDate;
	}
	public String getsDict_UserID() {
		return sDict_UserID;
	}
	public void setsDict_UserID(String sDict_UserID) {
		this.sDict_UserID = sDict_UserID;
	}
	public String getsDict_DictTypeNO() {
		return sDict_DictTypeNO;
	}
	public void setsDict_DictTypeNO(String sDict_DictTypeNO) {
		this.sDict_DictTypeNO = sDict_DictTypeNO;
	}
	public Date getdDict_UpdateDate() {
		return dDict_UpdateDate;
	}
	public void setdDict_UpdateDate(Date dDict_UpdateDate) {
		this.dDict_UpdateDate = dDict_UpdateDate;
	}
	public String getsDict_UpdateUserID() {
		return sDict_UpdateUserID;
	}
	public void setsDict_UpdateUserID(String sDict_UpdateUserID) {
		this.sDict_UpdateUserID = sDict_UpdateUserID;
	}
	public int getlDict_SysFlag() {
		return lDict_SysFlag;
	}
	public void setlDict_SysFlag(int lDict_SysFlag) {
		this.lDict_SysFlag = lDict_SysFlag;
	}
	public String getsDict_Describe() {
		return sDict_Describe;
	}
	public void setsDict_Describe(String sDict_Describe) {
		this.sDict_Describe = sDict_Describe;
	}
	public String getsDict_Picture() {
		return sDict_Picture;
	}
	public void setsDict_Picture(String sDict_Picture) {
		this.sDict_Picture = sDict_Picture;
	}
	public String getsDict_Link() {
		return sDict_Link;
	}
	public void setsDict_Link(String sDict_Link) {
		this.sDict_Link = sDict_Link;
	}
	public String getsDict_Color() {
		return sDict_Color;
	}
	public void setsDict_Color(String sDict_Color) {
		this.sDict_Color = sDict_Color;
	}



	public Dict cursorToBean(Cursor cursor){
		this.sDict_ID = cursor.getString(cursor.getColumnIndex("sDict_ID"));
		this.sDict_NO = cursor.getString(cursor.getColumnIndex("sDict_NO"));
		this.sDict_Name = cursor.getString(cursor.getColumnIndex("sDict_Name"));
		this.dDict_CreateDate = new Date(cursor.getLong(cursor.getColumnIndex("dDict_CreateDate")));
		this.sDict_UserID = cursor.getString(cursor.getColumnIndex("sDict_UserID"));
		this.sDict_DictTypeNO = cursor.getString(cursor.getColumnIndex("sDict_DictTypeNO"));
		this.dDict_UpdateDate = new Date(cursor.getLong(cursor.getColumnIndex("dDict_UpdateDate")));
		this.sDict_UpdateUserID = cursor.getString(cursor.getColumnIndex("sDict_UpdateUserID"));
		this.lDict_SysFlag = cursor.getInt(cursor.getColumnIndex("lDict_SysFlag"));
		this.sDict_Describe = cursor.getString(cursor.getColumnIndex("sDict_Describe"));
		this.sDict_Picture = cursor.getString(cursor.getColumnIndex("sDict_Picture"));
		this.sDict_Link = cursor.getString(cursor.getColumnIndex("sDict_Link"));
		this.sDict_Color = cursor.getString(cursor.getColumnIndex("sDict_Color"));
		return this;
	}

	public ContentValues beanToValues(){
		ContentValues values = new ContentValues();
		values.put("sDict_ID", sDict_ID);
		values.put("sDict_NO", sDict_NO);
		values.put("sDict_Name", sDict_Name);
		values.put("dDict_CreateDate", dDict_CreateDate.getTime());
		values.put("sDict_UserID", sDict_UserID);
		values.put("sDict_DictTypeNO", sDict_DictTypeNO);
		values.put("dDict_UpdateDate", dDict_UpdateDate.getTime());
		values.put("sDict_UpdateUserID", sDict_UpdateUserID);
		values.put("lDict_SysFlag", lDict_SysFlag);
		values.put("sDict_Describe", sDict_Describe);
		values.put("sDict_Picture", sDict_Picture);
		values.put("sDict_Link", sDict_Link);
		values.put("sDict_Color", sDict_Color);
		return values;
	}
}
