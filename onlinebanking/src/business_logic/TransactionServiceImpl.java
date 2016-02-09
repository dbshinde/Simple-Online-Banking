package business_logic;

import helpers.DBConnectionHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import utils.TransactionHistoryOfAnAccount;
import dao.TransactionDAO;


@Service("transactionService")
public class TransactionServiceImpl implements TransactionService{

	private @Autowired TransactionDAO transactionDAO;
	
	public TransactionServiceImpl() {
	}

	public List<TransactionHistoryOfAnAccount> getAllTransactionsOfAAccount( int account_id) {

		Connection connection = DBConnectionHelper.getConnection();
		List<TransactionHistoryOfAnAccount> histories = new ArrayList<TransactionHistoryOfAnAccount>();
		try 
		{
			Transaction transaction1 = new Transaction();
			// Retrieving Deposit and Withdraw and Transaction(SENT)
			transaction1.setAccount_id(account_id);
			List<Transaction> transactionList = transactionDAO.searchMatching(connection, transaction1);
			for (Transaction transac : transactionList) {
				TransactionHistoryOfAnAccount tH = new TransactionHistoryOfAnAccount();
				tH.setAccount_id(account_id);
				tH.setTransaction_type(transac.getTransaction_type());
				tH.setTransaction_amount(transac.getTransaction_amount());
				tH.setTransaction_time(transac.getTransaction_time());
				// For Transaction(SENT)
				if (transac.getTransaction_type() == 3) {
					tH.setTransaction_relation_type(1);
					tH.setTransaction_relation_account_id(transac.getAccount_id_to());
				}
				// add to list
				histories.add(tH);
			}

			Transaction transaction2 = new Transaction();
			// Transaction(RECEIVE)
			transaction2.setAccount_id_to(account_id);
			List<Transaction> transactionListTo = transactionDAO.searchMatching(connection, transaction2);
			for (Transaction t : transactionListTo) {
				TransactionHistoryOfAnAccount tH = new TransactionHistoryOfAnAccount();
				tH.setAccount_id(account_id);
				tH.setTransaction_type(t.getTransaction_type());
				tH.setTransaction_amount(t.getTransaction_amount());
				tH.setTransaction_time(t.getTransaction_time());
				tH.setTransaction_relation_type(2);
				tH.setTransaction_relation_account_id(t.getAccount_id());

				// add to list
				histories.add(tH);
			}
			return histories;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

}
