package com.jian.system.entity;

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
	
	
}
