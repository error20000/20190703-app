package com.jian.system.entity;

import android.database.Cursor;

import java.util.Date;

public class Messages {


	private String sMsg_ID;
	private String sMsg_Type;
	private String sMsg_TypeName;
	private Date dMsg_CreateDate;
	private String sMsg_ToUserID;
	private String sMsg_ToUserName;
	private String sMsg_EquipID;
	private String sMsg_EquipName;
	private String sMsg_Describe;
	private String sMsg_Remarks;
	private Date dMsg_UpdateDate;
	private String sMsg_UserID;
	private String sMsg_UserName;
	private String sMsg_AidID;
	private String sMsg_AidName;
	private String sMsg_IP;
	private String sMsg_FromUserID;
	private String sMsg_FromUserName;
	private String sMsg_Label;
	private String sMsg_LabelName;
	private int lMsg_Level = Integer.MAX_VALUE;
	private String sMsg_Status;
	private String sMsg_StatusName;
	private String sMsg_Title;
	private String sMsg_StoreLv1;
	private String sMsg_StoreLv1Name;
	private String sMsg_StoreLv2;
	private String sMsg_StoreLv2Name;
	private String sMsg_StoreLv3;
	private String sMsg_StoreLv3Name;
	private String sMsg_StoreLv4;
	private String sMsg_StoreLv4Name;
	private int dMsg_StoreNum;
	private String sMsg_Reason;
	private String sMsg_ReasonName;
	

	public String getsMsg_ID() {
		return sMsg_ID;
	}
	public void setsMsg_ID(String sMsg_ID) {
		this.sMsg_ID = sMsg_ID;
	}
	public String getsMsg_Type() {
		return sMsg_Type;
	}
	public void setsMsg_Type(String sMsg_Type) {
		this.sMsg_Type = sMsg_Type;
	}
	public Date getdMsg_CreateDate() {
		return dMsg_CreateDate;
	}
	public void setdMsg_CreateDate(Date dMsg_CreateDate) {
		this.dMsg_CreateDate = dMsg_CreateDate;
	}
	public String getsMsg_ToUserID() {
		return sMsg_ToUserID;
	}
	public void setsMsg_ToUserID(String sMsg_ToUserID) {
		this.sMsg_ToUserID = sMsg_ToUserID;
	}
	public String getsMsg_EquipID() {
		return sMsg_EquipID;
	}
	public void setsMsg_EquipID(String sMsg_EquipID) {
		this.sMsg_EquipID = sMsg_EquipID;
	}
	public String getsMsg_Describe() {
		return sMsg_Describe;
	}
	public void setsMsg_Describe(String sMsg_Describe) {
		this.sMsg_Describe = sMsg_Describe;
	}
	public String getsMsg_Remarks() {
		return sMsg_Remarks;
	}
	public void setsMsg_Remarks(String sMsg_Remarks) {
		this.sMsg_Remarks = sMsg_Remarks;
	}
	public Date getdMsg_UpdateDate() {
		return dMsg_UpdateDate;
	}
	public void setdMsg_UpdateDate(Date dMsg_UpdateDate) {
		this.dMsg_UpdateDate = dMsg_UpdateDate;
	}
	public String getsMsg_UserID() {
		return sMsg_UserID;
	}
	public void setsMsg_UserID(String sMsg_UserID) {
		this.sMsg_UserID = sMsg_UserID;
	}
	public String getsMsg_AidID() {
		return sMsg_AidID;
	}
	public void setsMsg_AidID(String sMsg_AidID) {
		this.sMsg_AidID = sMsg_AidID;
	}
	public String getsMsg_IP() {
		return sMsg_IP;
	}
	public void setsMsg_IP(String sMsg_IP) {
		this.sMsg_IP = sMsg_IP;
	}
	public String getsMsg_FromUserID() {
		return sMsg_FromUserID;
	}
	public void setsMsg_FromUserID(String sMsg_FromUserID) {
		this.sMsg_FromUserID = sMsg_FromUserID;
	}
	public String getsMsg_Label() {
		return sMsg_Label;
	}
	public void setsMsg_Label(String sMsg_Label) {
		this.sMsg_Label = sMsg_Label;
	}
	public int getlMsg_Level() {
		return lMsg_Level;
	}
	public void setlMsg_Level(int lMsg_Level) {
		this.lMsg_Level = lMsg_Level;
	}
	public String getsMsg_Title() {
		return sMsg_Title;
	}
	public void setsMsg_Title(String sMsg_Title) {
		this.sMsg_Title = sMsg_Title;
	}
	public String getsMsg_Status() {
		return sMsg_Status;
	}
	public void setsMsg_Status(String sMsg_Status) {
		this.sMsg_Status = sMsg_Status;
	}

	public String getsMsg_ToUserName() {
		return sMsg_ToUserName;
	}

	public void setsMsg_ToUserName(String sMsg_ToUserName) {
		this.sMsg_ToUserName = sMsg_ToUserName;
	}

	public String getsMsg_EquipName() {
		return sMsg_EquipName;
	}

	public void setsMsg_EquipName(String sMsg_EquipName) {
		this.sMsg_EquipName = sMsg_EquipName;
	}

	public String getsMsg_UserName() {
		return sMsg_UserName;
	}

	public void setsMsg_UserName(String sMsg_UserName) {
		this.sMsg_UserName = sMsg_UserName;
	}

	public String getsMsg_AidName() {
		return sMsg_AidName;
	}

	public void setsMsg_AidName(String sMsg_AidName) {
		this.sMsg_AidName = sMsg_AidName;
	}

	public String getsMsg_FromUserName() {
		return sMsg_FromUserName;
	}

	public void setsMsg_FromUserName(String sMsg_FromUserName) {
		this.sMsg_FromUserName = sMsg_FromUserName;
	}

	public String getsMsg_TypeName() {
		return sMsg_TypeName;
	}

	public void setsMsg_TypeName(String sMsg_TypeName) {
		this.sMsg_TypeName = sMsg_TypeName;
	}

	public String getsMsg_LabelName() {
		return sMsg_LabelName;
	}

	public void setsMsg_LabelName(String sMsg_LabelName) {
		this.sMsg_LabelName = sMsg_LabelName;
	}

	public String getsMsg_StatusName() {
		return sMsg_StatusName;
	}

	public void setsMsg_StatusName(String sMsg_StatusName) {
		this.sMsg_StatusName = sMsg_StatusName;
	}

	public String getsMsg_StoreLv1() {
		return sMsg_StoreLv1;
	}

	public void setsMsg_StoreLv1(String sMsg_StoreLv1) {
		this.sMsg_StoreLv1 = sMsg_StoreLv1;
	}

	public String getsMsg_StoreLv1Name() {
		return sMsg_StoreLv1Name;
	}

	public void setsMsg_StoreLv1Name(String sMsg_StoreLv1Name) {
		this.sMsg_StoreLv1Name = sMsg_StoreLv1Name;
	}

	public String getsMsg_StoreLv2() {
		return sMsg_StoreLv2;
	}

	public void setsMsg_StoreLv2(String sMsg_StoreLv2) {
		this.sMsg_StoreLv2 = sMsg_StoreLv2;
	}

	public String getsMsg_StoreLv2Name() {
		return sMsg_StoreLv2Name;
	}

	public void setsMsg_StoreLv2Name(String sMsg_StoreLv2Name) {
		this.sMsg_StoreLv2Name = sMsg_StoreLv2Name;
	}

	public String getsMsg_StoreLv3() {
		return sMsg_StoreLv3;
	}

	public void setsMsg_StoreLv3(String sMsg_StoreLv3) {
		this.sMsg_StoreLv3 = sMsg_StoreLv3;
	}

	public String getsMsg_StoreLv3Name() {
		return sMsg_StoreLv3Name;
	}

	public void setsMsg_StoreLv3Name(String sMsg_StoreLv3Name) {
		this.sMsg_StoreLv3Name = sMsg_StoreLv3Name;
	}

	public String getsMsg_StoreLv4() {
		return sMsg_StoreLv4;
	}

	public void setsMsg_StoreLv4(String sMsg_StoreLv4) {
		this.sMsg_StoreLv4 = sMsg_StoreLv4;
	}

	public String getsMsg_StoreLv4Name() {
		return sMsg_StoreLv4Name;
	}

	public void setsMsg_StoreLv4Name(String sMsg_StoreLv4Name) {
		this.sMsg_StoreLv4Name = sMsg_StoreLv4Name;
	}

	public int getdMsg_StoreNum() {
		return dMsg_StoreNum;
	}

	public void setdMsg_StoreNum(int dMsg_StoreNum) {
		this.dMsg_StoreNum = dMsg_StoreNum;
	}

	public String getsMsg_Reason() {
		return sMsg_Reason;
	}

	public void setsMsg_Reason(String sMsg_Reason) {
		this.sMsg_Reason = sMsg_Reason;
	}

	public String getsMsg_ReasonName() {
		return sMsg_ReasonName;
	}

	public void setsMsg_ReasonName(String sMsg_ReasonName) {
		this.sMsg_ReasonName = sMsg_ReasonName;
	}


	public Messages cursorToBean(Cursor cursor){
		this.sMsg_ID = cursor.getString(cursor.getColumnIndex("sMsg_ID"));
		this.sMsg_Type = cursor.getString(cursor.getColumnIndex("sMsg_Type"));
		this.sMsg_TypeName = cursor.getString(cursor.getColumnIndex("sMsg_TypeName"));
		this.dMsg_CreateDate = new Date(cursor.getLong(cursor.getColumnIndex("dMsg_CreateDate")));
		this.sMsg_ToUserID = cursor.getString(cursor.getColumnIndex("sMsg_ToUserID"));
		this.sMsg_ToUserName = cursor.getString(cursor.getColumnIndex("sMsg_ToUserName"));
		this.sMsg_EquipID = cursor.getString(cursor.getColumnIndex("sMsg_EquipID"));
		this.sMsg_EquipName = cursor.getString(cursor.getColumnIndex("sMsg_EquipName"));
		this.sMsg_Describe = cursor.getString(cursor.getColumnIndex("sMsg_Describe"));
		this.sMsg_Remarks =  cursor.getString(cursor.getColumnIndex("sMsg_Remarks"));
		this.dMsg_UpdateDate = new Date(cursor.getLong(cursor.getColumnIndex("dMsg_UpdateDate")));
		this.sMsg_UserID = cursor.getString(cursor.getColumnIndex("sMsg_UserID"));
		this.sMsg_UserName = cursor.getString(cursor.getColumnIndex("sMsg_UserName"));
		this.sMsg_AidID =  cursor.getString(cursor.getColumnIndex("sMsg_AidID"));
		this.sMsg_AidName = cursor.getString(cursor.getColumnIndex("sMsg_AidName"));
		this.sMsg_IP = cursor.getString(cursor.getColumnIndex("sMsg_IP"));
		this.sMsg_FromUserID = cursor.getString(cursor.getColumnIndex("sMsg_FromUserID"));
		this.sMsg_FromUserName = cursor.getString(cursor.getColumnIndex("sMsg_FromUserName"));
		this.sMsg_Label = cursor.getString(cursor.getColumnIndex("sMsg_Label"));
		this.sMsg_LabelName = cursor.getString(cursor.getColumnIndex("sMsg_LabelName"));
		this.lMsg_Level = cursor.getInt(cursor.getColumnIndex("lMsg_Level"));
		this.sMsg_Status = cursor.getString(cursor.getColumnIndex("sMsg_Status"));
		this.sMsg_StatusName = cursor.getString(cursor.getColumnIndex("sMsg_StatusName"));
		this.sMsg_Title = cursor.getString(cursor.getColumnIndex("sMsg_Title"));
		this.sMsg_StoreLv1 = cursor.getString(cursor.getColumnIndex("sMsg_StoreLv1"));
		this.sMsg_StoreLv1Name = cursor.getString(cursor.getColumnIndex("sMsg_StoreLv1Name"));
		this.sMsg_StoreLv2 = cursor.getString(cursor.getColumnIndex("sMsg_StoreLv2"));
		this.sMsg_StoreLv2Name = cursor.getString(cursor.getColumnIndex("sMsg_StoreLv2Name"));
		this.sMsg_StoreLv3 = cursor.getString(cursor.getColumnIndex("sMsg_StoreLv3"));
		this.sMsg_StoreLv3Name = cursor.getString(cursor.getColumnIndex("sMsg_StoreLv3Name"));
		this.sMsg_StoreLv4 = cursor.getString(cursor.getColumnIndex("sMsg_StoreLv4"));
		this.sMsg_StoreLv4Name = cursor.getString(cursor.getColumnIndex("sMsg_StoreLv4Name"));
		this.dMsg_StoreNum = cursor.getInt(cursor.getColumnIndex("dMsg_StoreNum"));
		this.sMsg_Reason = cursor.getString(cursor.getColumnIndex("sMsg_Reason"));
		this.sMsg_ReasonName = cursor.getString(cursor.getColumnIndex("sMsg_ReasonName"));

		return this;
	}
}
