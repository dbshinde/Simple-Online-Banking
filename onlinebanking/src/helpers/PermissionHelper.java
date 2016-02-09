package helpers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import models.MapAccountCustomer;

import org.springframework.stereotype.Component;

import dao.DAOFactory;
import dao.MapAccountCustomerDAO;

@Component
public class PermissionHelper {

	
	public static boolean isThisAccountOwnByThisCustomer(int account_id, int customer_id) {

		Connection connection = DBConnectionHelper.getConnection();
		MapAccountCustomerDAO mapAccountCustomerDAO = DAOFactory.getMapAccountCustomerDAO();

		MapAccountCustomer mapAccountCustomer = new MapAccountCustomer();
		mapAccountCustomer.setAccount_id(account_id);
		mapAccountCustomer.setCustomer_id(customer_id);

		try 
		{
			List<MapAccountCustomer> mapAccountCustomerList = mapAccountCustomerDAO.searchMatching(connection, mapAccountCustomer);
			if (mapAccountCustomerList.size() > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
