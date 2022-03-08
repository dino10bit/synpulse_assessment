package com.synpulse.banking.payload;

public class TransactionPayLoad {

	private String key;
	private String accountIBAN;
	private float amount;
	private String description;
	private String valueDate;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getAccountIBAN() {
		return accountIBAN;
	}
	public void setAccountIBAN(String accountIBAN) {
		this.accountIBAN = accountIBAN;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getValueDate() {
		return valueDate;
	}
	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	} 
}
