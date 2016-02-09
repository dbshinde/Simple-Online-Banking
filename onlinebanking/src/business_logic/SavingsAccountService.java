package business_logic;

import utils.ChangesStatus;
import exceptions.BelowMinimumBalanceException;



public interface SavingsAccountService {

	ChangesStatus withdrawMoney(int account_id, int customer_id_by, int amount, int pin) throws BelowMinimumBalanceException;
	ChangesStatus transferMoney(int account_id_from, int account_id_to, int customer_id_by, int amount) throws BelowMinimumBalanceException;
}
