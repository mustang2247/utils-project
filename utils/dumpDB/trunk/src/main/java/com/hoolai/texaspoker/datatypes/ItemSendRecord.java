package com.hoolai.texaspoker.datatypes;


/**
 * 用户道具的赠送记录。用于查询接受者和赠送者的需求。
 */
public final class ItemSendRecord {
	private String formId;//赠送者的用户ID
	private String targetId;//接受者的用户ID
	private String itemId;//赠送道具id
	private String count;//赠送数量
	private String status;//赠送状态
	private String date;//时间
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	public String toString() {
		return "formId:[" + formId + "],targetId:[" + targetId + "],itemId:["
				+ itemId + "],count:[" + count + "],status:[" + status
				+ "],date:[" + date + "]";
	}
}