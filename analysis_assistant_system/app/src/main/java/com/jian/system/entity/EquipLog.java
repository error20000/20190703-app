package com.jian.system.entity;

import java.util.Date;


public class EquipLog  {


	private String sELog_ID;
	private String sELog_Type;
	private Date dELog_CreateDate;
	private String sELog_UserID;
	private String sELog_EquipID;
	private String sELog_Describe;
	private String sELog_Remarks;
	private String sELog_IP;
	
	
	
	public String getsELog_ID() {
		return sELog_ID;
	}
	public void setsELog_ID(String sELog_ID) {
		this.sELog_ID = sELog_ID;
	}
	public String getsELog_Type() {
		return sELog_Type;
	}
	public void setsELog_Type(String sELog_Type) {
		this.sELog_Type = sELog_Type;
	}
	public Date getdELog_CreateDate() {
		return dELog_CreateDate;
	}
	public void setdELog_CreateDate(Date dELog_CreateDate) {
		this.dELog_CreateDate = dELog_CreateDate;
	}
	public String getsELog_UserID() {
		return sELog_UserID;
	}
	public void setsELog_UserID(String sELog_UserID) {
		this.sELog_UserID = sELog_UserID;
	}
	public String getsELog_EquipID() {
		return sELog_EquipID;
	}
	public void setsELog_EquipID(String sELog_EquipID) {
		this.sELog_EquipID = sELog_EquipID;
	}
	public String getsELog_Describe() {
		return sELog_Describe;
	}
	public void setsELog_Describe(String sELog_Describe) {
		this.sELog_Describe = sELog_Describe;
	}
	public String getsELog_Remarks() {
		return sELog_Remarks;
	}
	public void setsELog_Remarks(String sELog_Remarks) {
		this.sELog_Remarks = sELog_Remarks;
	}
	public String getsELog_IP() {
		return sELog_IP;
	}
	public void setsELog_IP(String sELog_IP) {
		this.sELog_IP = sELog_IP;
	}
	
}
