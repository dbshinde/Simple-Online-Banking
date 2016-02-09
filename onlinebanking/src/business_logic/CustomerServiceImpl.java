package business_logic;

import helpers.DBConnectionHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import models.Customer;
import models.MapAccountCustomer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import utils.ChangesStatus;
import dao.CustomerDAO;
import dao.MapAccountCustomerDAO;
import exceptions.NotFoundException;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService{

	private @Autowired MapAccountCustomerDAO mapAccountCustomerDAO;
	private @Autowired CustomerDAO customerDAO;
	private @Autowired AccountService accountService;
	public CustomerServiceImpl() {

	}

	public List<Customer> getCustomers() {
		Connection connection = DBConnectionHelper.getConnection();
		try {
			return customerDAO.loadAll(connection);
		} catch (SQLException e) {
			
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		return null;
	}

	public Customer getCustomer(int customer_id) throws NotFoundException,
			SQLException {
		
		Customer customer = new Customer();
		Connection connection = DBConnectionHelper.getConnection();

		try 
		{
			customer = customerDAO.getObject(connection, customer_id);

			MapAccountCustomer map = new MapAccountCustomer();
			map.setCustomer_id(customer.getCustomer_id());

			List<MapAccountCustomer> relation = mapAccountCustomerDAO.searchMatching(connection, map);

			if (relation.size() < 1) {
				return customer;
			}

			for (MapAccountCustomer r : relation) {
				customer.addAccount(accountService.getAccountDetails(r.getAccount_id()));
			}

			return customer;
		} catch (NotFoundException e) {
			throw new NotFoundException("Custommer do not found");
		} catch (SQLException e) {
			throw new SQLException("SQL Query Exception");
		} finally {
			connection.close();
		}

	}

	/*
	 * public Customer getCustomerDetails(int customer_id) throws
	 * NotFoundException, SQLException{
	 * 
	 * Customer customer = getCustomer(customer_id);
	 * 
	 * Connection connection = DBConnectionHelper.getConnection();
	 * 
	 * //Finding the accounts MapAccountCustomerDAOImpl mapAccountCustomerDAO =
	 * new MapAccountCustomerDAOImpl(); AccountDAOImpl accountDAO = new
	 * AccountDAOImpl();
	 * 
	 * MapAccountCustomer mapAccountCustomer = new MapAccountCustomer();
	 * mapAccountCustomer.setCustomer_id(customer.getCustomer_id());
	 * List<MapAccountCustomer> relation; try { relation =
	 * mapAccountCustomerDAO.searchMatching(connection, mapAccountCustomer);
	 * 
	 * AccountController accountController = new AccountController();
	 * 
	 * //Adding the account(s) CustomerDAOImpl customerDAOImpl = new
	 * CustomerDAOImpl(); for(MapAccountCustomer r : relation){ Account a =
	 * accountController.getAccountDetails(r.getAccount_id());
	 * customer.addAccount(a); }
	 * 
	 * 
	 * 
	 * return customer; } catch (SQLException e) { // TODO Auto-generated catch
	 * block e.printStackTrace(); } finally { connection.close(); }
	 * 
	 * return null; }
	 */

	public ChangesStatus saveCustomer(Customer customer) 
	{

		Connection connection = DBConnectionHelper.getConnection();
		try 
		{
			if (customer.getCustomer_id() > 0) {
				customerDAO.save(connection, customer);
				connection.commit();
				return new ChangesStatus(true, "Customer successfully saved.");
			} else {
				customerDAO.create(connection, customer);
				connection.commit();
				return new ChangesStatus(true, "Customer successfully created.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new ChangesStatus(false, "Customer can not be save.");
		} catch (NotFoundException e) {
			e.printStackTrace();
			return new ChangesStatus(false, "Customer can not be save.");
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public ChangesStatus deleteCustomer(int customer_id) {

		return null;
	}

}
