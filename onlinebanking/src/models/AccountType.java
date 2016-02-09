package models;

import java.io.Serializable;
import java.util.ArrayList;

public class AccountType implements Cloneable, Serializable {

    private int account_typeId;
    private String account_type;
    private String status;
	public int getAccount_typeId() {
		return account_typeId;
	}
	public void setAccount_typeId(int account_typeId) {
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
