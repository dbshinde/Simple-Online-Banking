package business_logic;

import java.util.List;
import utils.TransactionHistoryOfAnAccount;



public interface TransactionService {

	List<TransactionHistoryOfAnAccount> getAllTransactionsOfAAccount( int account_id);
}
