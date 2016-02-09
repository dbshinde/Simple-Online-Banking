package business_logic;

import utils.ChangesStatus;
import exceptions.BelowMinimumBalanceException;
import exceptions.OverDraftLimitExceededException;



public interface CheckingAccountService {

	ChangesStatus withdrawMoney(int account_id, int customer_id_by, int amount, int pin) throws BelowMinimumBalanceException, OverDraftLimitExceededException;
	ChangesStatus transferMoney(int account_id_from, int account_id_to, int customer_id_by, int amount) throws OverDraftLimitExceededException;
}
