package business_logic;

import java.util.ArrayList;
import java.util.List;

import utils.ChangesStatus;
import exceptions.BelowMinimumBalanceException;
import exceptions.NotFoundException;
import exceptions.OverDraftLimitExceededException;
import models.Account;
import models.MapAccountCustomer;



public interface AccountService {

	List<Account> getAccounts();
	Account getAccount(int account_id) throws NotFoundException;
	Account getAccountDetails(int account_id) throws NotFoundException;
	ChangesStatus depositMoney(int account_id, int customer_id_by,double amount, int pin);
	ChangesStatus withdrawMoney(int account_id, int customer_id_by,int amount, int pin) throws BelowMinimumBalanceException,OverDraftLimitExceededException;
	ChangesStatus transferMoney(int account_id_from, int account_id_to,int customer_id_by, int amount, int pin)throws OverDraftLimitExceededException,BelowMinimumBalanceException;
	ChangesStatus saveAccount(Account account,ArrayList<MapAccountCustomer> mapAccountCustomer);
	ChangesStatus deleteAccount(int account_id);
}
