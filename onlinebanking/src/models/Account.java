package models;

import java.io.Serializable;
import java.util.ArrayList;

public class Account implements Cloneable, Serializable {

    private Integer account_id;
    private Integer pin;
    private double amount;
    private Integer account_typeId; //1 checking //2 saving
    private Integer bank_branch_id;
    private AccountType account_type; //1 checking //2 saving
   	private ArrayList<Customer> customers;
    private BankBranch bank_branch;
    private boolean joint_account;

	public void addCustomer(Customer customer){
    	this.customers.add(customer);
    }
    
    public ArrayList<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(ArrayList<Customer> customers) {
		this.customers = customers;
	}

    public BankBranch getBank_branch() {
		return bank_branch;
	}

	public void setBank_branch(BankBranch bank_branch) {
		this.bank_branch = bank_branch;
	}

	public boolean isJoint_account() {
		return joint_account;
	}


	public void setJoint_account(boolean joint_account) {
		this.joint_account = joint_account;
	}

    public Account () {
    	customers = new ArrayList<Customer>();
    }

    public Account (Integer account_idIn) {
    	this.account_id = account_idIn;
    }

    public Integer getAccount_id() {
          return this.account_id;
    }
    public void setAccount_id(Integer account_idIn) {
          this.account_id = account_idIn;
    }

    public Integer getPin() {
          return this.pin;
    }
    public void setPin(Integer pinIn) {
          this.pin = pinIn;
    }

    public double getAmount() {
          return this.amount;
    }
    public void setAmount(double f) {
          this.amount = f;
    }

    public Integer getAccount_typeId() {
          return this.account_typeId;
    }
    public void setAccount_typeId(Integer account_typeIn) {
          this.account_typeId = account_typeIn;
    }

    public AccountType getAccount_type() {
		return account_type;
	}

	public void setAccount_type(AccountType account_type) {
		this.account_type = account_type;
	}

	public Integer getBank_branch_id() {
          return this.bank_branch_id;
    }
    public void setBank_branch_id(Integer bank_branch_idIn) {
          this.bank_branch_id = bank_branch_idIn;
    }

    public void setAll(Integer account_idIn,
          Integer pinIn,
          long amountIn,
          Integer account_typeIn,
          Integer bank_branch_idIn) {
          this.account_id = account_idIn;
          this.pin = pinIn;
          this.amount = amountIn;
          this.account_typeId = account_typeIn;
          this.bank_branch_id = bank_branch_idIn;
    }

    public boolean hasEqualMapping(Account valueObject) {

          if (valueObject.getAccount_id() != this.account_id) {
                    return(false);
          }
          if (valueObject.getPin() != this.pin) {
                    return(false);
          }
          if (valueObject.getAmount() != this.amount) {
                    return(false);
          }
          if (valueObject.getAccount_typeId() != this.account_typeId) {
                    return(false);
          }
          if (valueObject.getBank_branch_id() != this.bank_branch_id) {
                    return(false);
          }

          return true;
    }

    public String toString() {
        StringBuffer out = new StringBuffer(this.getDaogenVersion());
        out.append("\nclass Account, mapping to table account\n");
        out.append("Persistent attributes: \n"); 
        out.append("account_id = " + this.account_id + "\n"); 
        out.append("pin = " + this.pin + "\n"); 
        out.append("amount = " + this.amount + "\n"); 
        out.append("account_type = " + this.account_typeId + "\n"); 
        out.append("bank_branch_id = " + this.bank_branch_id + "\n"); 
        return out.toString();
    }

    public Object clone() {
        Account cloned = new Account();

        cloned.setAccount_id(this.account_id); 
        cloned.setPin(this.pin); 
        cloned.setAmount(this.amount); 
        cloned.setAccount_typeId(this.account_typeId); 
        cloned.setBank_branch_id(this.bank_branch_id); 
        return cloned;
    }

    public String getDaogenVersion() {
        return "DaoGen version 2.4.1";
    }

}
