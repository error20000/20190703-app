package com.jian.system.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.jian.system.utils.Utils;

import java.util.Date;


public class User  {


	private String sUser_ID;
	private String sUser_UserName;
	private String sUser_PassWord; //密码（md5）
	private String sUser_Nick;
	private int lUser_StatusFlag;
	private String sUser_GroupID;
	private String sUser_QQ;
	private String sUser_Email;
	private String sUser_Phone;
	private String sUser_ThirdID;
	private Date dUser_CreateDate;
	private String sUser_UserID;
	

	
	public String getsUser_ID() {
		return sUser_ID;
	}
	public void setsUser_ID(String sUser_ID) {
		this.sUser_ID = sUser_ID;
	}
	public String getsUser_UserName() {
		return sUser_UserName;
	}
	public void setsUser_UserName(String sUser_UserName) {
		this.sUser_UserName = sUser_UserName;
	}
	public String getsUser_PassWord() {
		return sUser_PassWord;
	}
	public void setsUser_PassWord(String sUser_PassWord) {
		this.sUser_PassWord = sUser_PassWord;
	}
	public String getsUser_Nick() {
		return sUser_Nick;
	}
	public void setsUser_Nick(String sUser_Nick) {
		this.sUser_Nick = sUser_Nick;
	}
	public int getlUser_StatusFlag() {
		return lUser_StatusFlag;
	}
	public void setlUser_StatusFlag(int lUser_StatusFlag) {
		this.lUser_StatusFlag = lUser_StatusFlag;
	}
	public String getsUser_GroupID() {
		return sUser_GroupID;
	}
	public void setsUser_GroupID(String sUser_GroupID) {
		this.sUser_GroupID = sUser_GroupID;
	}
	public String getsUser_QQ() {
		return sUser_QQ;
	}
	public void setsUser_QQ(String sUser_QQ) {
		this.sUser_QQ = sUser_QQ;
	}
	public String getsUser_Email() {
		return sUser_Email;
	}
	public void setsUser_Email(String sUser_Email) {
		this.sUser_Email = sUser_Email;
	}
	public String getsUser_Phone() {
		return sUser_Phone;
	}
	public void setsUser_Phone(String sUser_Phone) {
		this.sUser_Phone = sUser_Phone;
	}
	public String getsUser_ThirdID() {
		return sUser_ThirdID;
	}
	public void setsUser_ThirdID(String sUser_ThirdID) {
		this.sUser_ThirdID = sUser_ThirdID;
	}
	public Date getdUser_CreateDate() {
		return dUser_CreateDate;
	}
	public void setdUser_CreateDate(Date dUser_CreateDate) {
		this.dUser_CreateDate = dUser_CreateDate;
	}
	public String getsUser_UserID() {
		return sUser_UserID;
	}
	public void setsUser_UserID(String sUser_UserID) {
		this.sUser_UserID = sUser_UserID;
	}
	

	public  User cursorToBean(Cursor cursor){
		this.sUser_ID = cursor.getString(cursor.getColumnIndex("sUser_ID"));
		this.sUser_UserName = cursor.getString(cursor.getColumnIndex("sUser_UserName"));
		this.sUser_PassWord = cursor.getString(cursor.getColumnIndex("sUser_PassWord"));
		this.sUser_Nick = cursor.getString(cursor.getColumnIndex("sUser_Nick"));
		this.lUser_StatusFlag = cursor.getInt(cursor.getColumnIndex("lUser_StatusFlag"));
		this.sUser_GroupID = cursor.getString(cursor.getColumnIndex("sUser_GroupID"));
		this.sUser_QQ = cursor.getString(cursor.getColumnIndex("sUser_QQ"));
		this.sUser_Email = cursor.getString(cursor.getColumnIndex("sUser_Email"));
		this.sUser_Phone = cursor.getString(cursor.getColumnIndex("sUser_Phone"));
		this.sUser_ThirdID = cursor.getString(cursor.getColumnIndex("sUser_ThirdID"));
		this.dUser_CreateDate = new Date(cursor.getLong(cursor.getColumnIndex("dUser_CreateDate")));
		this.sUser_UserID = cursor.getString(cursor.getColumnIndex("sUser_UserID"));
		return this;
	}

	public ContentValues beanToValues(){
		ContentValues values = new ContentValues();
		if (!Utils.isNullOrEmpty(sUser_ID)) {
			values.put("sUser_ID", sUser_ID);
		}
		if (!Utils.isNullOrEmpty(sUser_UserName)) {
			values.put("sUser_UserName", sUser_UserName);
		}
		if (!Utils.isNullOrEmpty(sUser_PassWord)) {
			values.put("sUser_PassWord", sUser_PassWord);
		}
		if (!Utils.isNullOrEmpty(sUser_Nick)) {
			values.put("sUser_Nick", sUser_Nick);
		}
		if (!Utils.isNullOrEmpty(lUser_StatusFlag)) {
			values.put("lUser_StatusFlag", lUser_StatusFlag);
		}
		if (!Utils.isNullOrEmpty(sUser_GroupID)) {
			values.put("sUser_GroupID", sUser_GroupID);
		}
		if (!Utils.isNullOrEmpty(sUser_QQ)) {
			values.put("sUser_QQ", sUser_QQ);
		}
		if (!Utils.isNullOrEmpty(sUser_Email)) {
			values.put("sUser_Email", sUser_Email);
		}
		if (!Utils.isNullOrEmpty(sUser_Phone)) {
			values.put("sUser_Phone", sUser_Phone);
		}
		if (!Utils.isNullOrEmpty(sUser_ThirdID)) {
			values.put("sUser_ThirdID", sUser_ThirdID);
		}
		if (!Utils.isNullOrEmpty(dUser_CreateDate)) {
			values.put("dUser_CreateDate", dUser_CreateDate.getTime());
		}
		if (!Utils.isNullOrEmpty(sUser_UserID)) {
			values.put("sUser_UserID", sUser_UserID);
		}
		return values;
	}
}
