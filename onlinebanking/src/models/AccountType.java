package models;

import java.io.Serializable;

public class AccountType implements Cloneable, Serializable {

    private Integer account_typeId;
    private String account_type;
    private String status;
	public Integer getAccount_typeId() {
		return account_typeId;
	}
	public void setAccount_typeId(Integer account_typeId) {
		this.account_typeId = account_typeId;
	}
	public String getAccount_type() {
		return account_type;
	}
	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
