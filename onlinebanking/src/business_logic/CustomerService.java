package business_logic;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import utils.ChangesStatus;
import exceptions.NotFoundException;
import models.Customer;




public interface CustomerService {

	List<Customer> getCustomers();
	List<Customer> searchCustomers(HttpServletRequest request) throws SQLException;
	Customer getCustomer(int customer_id) throws NotFoundException,SQLException;
	ChangesStatus saveCustomer(Customer customer);
	ChangesStatus deleteCustomer(int customer_id);
}
