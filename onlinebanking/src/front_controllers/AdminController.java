package front_controllers;


import helpers.AuthenticationHelper;
import helpers.DBConnectionHelper;
import helpers.HttpHelper;
import helpers.ValidationHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Account;
import models.BankBranch;
import models.Customer;
import models.MapAccountCustomer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import utils.ChangesStatus;
import utils.Constants;
import utils.Message;
import utils.MessageQueue;
import utils.PageEnvironment;
import utils.TransactionHistoryOfAnAccount;
import business_logic.AccountService;
import business_logic.BankBranchService;
import business_logic.CustomerService;
import business_logic.TransactionService;
import dao.CustomerDAO;
import exceptions.NotFoundException;

@Controller
public class AdminController{
	
	
	private @Autowired HttpServletRequest request;
	private @Autowired CustomerService customerService;
	private @Autowired AccountService accountService;
	private @Autowired TransactionService transactionService;
	private @Autowired BankBranchService bankBranchService;
	private @Autowired CustomerDAO customerDAO;
	
	Logger logger = Logger.getLogger(AdminController.class.getName());
	
	@RequestMapping(value = {"test"})
	public String dashboard(HttpServletRequest request, HttpServletResponse response,
			Model model, final RedirectAttributes redirectAttributes
			) {
		
		logger.info("request -->" + request.getRequestURI());
		PageEnvironment page = new PageEnvironment();
		MessageQueue message_queue = new MessageQueue();
		HttpSession session = request.getSession();
		HashMap<String, String> uris = HttpHelper.analyseRequest(request);
		boolean isAdmin = Boolean.parseBoolean(session.getAttribute(Constants.IS_ADMIN) != null ? session.getAttribute(Constants.IS_ADMIN).toString(): "false");
		
		if ( isAdmin )
		{
			//Customer
			if(uris.get(Constants.URI_2).equals("customer")){
				
				//Listing Down the customer
				if(uris.get(Constants.URI_3).equals("list")){
					List<Customer> customerList = customerService.getCustomers();
					model.addAttribute("data", customerList);
					page.setPage_title("List Customer");
					page.setViewFile("admin/customer_list.jsp");
				}
				//New Customer
				if(uris.get(Constants.URI_3).equals("new")){
					Customer customer = new Customer();
					model.addAttribute("data", customer);
					page.setPage_title("New Customer");
					page.setViewFile("admin/customer_edit.jsp");
				}
				//Editing a new customer
				if(uris.get(Constants.URI_3).equals("edit")){
					try {
						int customer_id = Integer.parseInt(uris.get(Constants.URI_4));
						Customer data = customerService.getCustomer(customer_id);
						model.addAttribute("data", data);
						page.setPage_title("Edit Customer : " + data.getGivenname());
						page.setViewFile("admin/customer_edit.jsp");
					} catch (NotFoundException e) {
						e.printStackTrace();
						message_queue.add(new Message("Customer Not Found", "error"));
						redirectAttributes.addFlashAttribute("message_queue", message_queue);
						return "redirect:/admin/customer/list";
					} catch (SQLException e) {
						e.printStackTrace();
						message_queue.add(new Message("Sorry, Database has some problem. Try again soon.", "error"));
						redirectAttributes.addFlashAttribute("message_queue", message_queue);
						return "redirect:/admin/customer/list";
					}
				}
				//Customer search page
				if(uris.get(Constants.URI_3).equals("search")){
					page.setPage_title("Search Customer");
					page.setViewFile("admin/customer_search.jsp");
				}
				//Showing customer and his|her Acounts
				if(uris.get(Constants.URI_3).equals("details")){
					try {
						int customer_id = Integer.parseInt(uris.get(Constants.URI_4));
						Customer data = customerService.getCustomer(customer_id);
						
						model.addAttribute("data", data);
						page.setPage_title("Customer Detials : " + data.getGivenname());
						page.setViewFile("admin/customer_details.jsp");
					} catch (NumberFormatException e){
						message_queue.add(new Message("Customer ID is not valid.", "error"));
						redirectAttributes.addFlashAttribute("message_queue", message_queue);
						return "redirect:/admin/dashboard";
					} catch (NotFoundException e) {
						e.printStackTrace();
						message_queue.add(new Message("Customer Not Found", "error"));
						redirectAttributes.addFlashAttribute("message_queue", message_queue);
						return "redirect:/admin/customer/search";
					} catch (SQLException e) {
						e.printStackTrace();
						message_queue.add(new Message("Sorry, Database has some problem. Try again soon.", "error"));
						redirectAttributes.addFlashAttribute("message_queue", message_queue);
						return "redirect:/admin/customer/search";
					}
				}
			}
			//Account
			if(uris.get(Constants.URI_2).equals("account")){
				
				if(uris.get(Constants.URI_3).equals("list")){
					List<Account> AccountsList = accountService.getAccounts();
					model.addAttribute("data", AccountsList);
					page.setPage_title("List Accounts");
					page.setViewFile("admin/account_list.jsp");
				}
				if(uris.get(Constants.URI_3).equals("new")){
					Account account = new Account();
					model.addAttribute("data", account);
					
					page.setPage_title("New Account");
					page.setViewFile("admin/account_edit.jsp");
				}
				if(uris.get(Constants.URI_3).equals("edit")){
					
					try{
						int account_id = Integer.parseInt(uris.get(Constants.URI_4));
						Account account = accountService.getAccountDetails(account_id);
						model.addAttribute("data", account);
						page.setPage_title("Edit Account");
						page.setViewFile("admin/account_edit.jsp");
					} catch (NumberFormatException e){
						e.printStackTrace();
						message_queue.add(new Message("Account ID is not valid.", "error"));
						redirectAttributes.addFlashAttribute("message_queue", message_queue);
						return "redirect:/admin/account/search";
						
					} catch (NotFoundException e) {
						e.printStackTrace();
						message_queue.add(new Message("Account Not Found", "error"));
						redirectAttributes.addFlashAttribute("message_queue", message_queue);
						return "redirect:/admin/account/search";
					}
					
				}
				//Account search page
				if(uris.get(Constants.URI_3).equals("search")){
					page.setPage_title("Search Account");
					page.setViewFile("admin/account_search.jsp");
				}
				//Account search page
				if(uris.get(Constants.URI_3).equals("details")){
					int account_id = Integer.parseInt(uris.get(Constants.URI_4));
					Account data;
					try {
						data = accountService.getAccountDetails(account_id);
						model.addAttribute("data", data);
						page.setPage_title("Account Details");
						page.setViewFile("admin/account_details.jsp");
					} catch (NotFoundException e) {
						e.printStackTrace();
						message_queue.add(new Message("Account Not Exit", "error"));
						redirectAttributes.addFlashAttribute("message_queue", message_queue);
						return "redirect:/admin/dashboard";
					}
					
					
				}
				if(uris.get(Constants.URI_3).equals("transaction")){
					int account_id = Integer.parseInt(uris.get(Constants.URI_4));
					ArrayList<TransactionHistoryOfAnAccount> t_histories = (ArrayList<TransactionHistoryOfAnAccount>) transactionService.getAllTransactionsOfAAccount(account_id);
					model.addAttribute("data", t_histories);
					model.addAttribute("account_id", account_id);
					page.setPage_title("Transfer History");
					page.setView_file("admin/account_transaction.jsp");
				}
			}
			if(uris.get(Constants.URI_2).equals("bank_branch")){
				
				if(uris.get(Constants.URI_3).equals("list")){
					List<BankBranch> bankBranchList = bankBranchService.getAllBankBranches();
					model.addAttribute("data", bankBranchList);
					page.setPage_title("Bank Branches");
					page.setViewFile("admin/bank_branch_list.jsp");
				}
				if(uris.get(Constants.URI_3).equals("new")){
					BankBranch bankBranch = new BankBranch();
					model.addAttribute("data", bankBranch);
					page.setPage_title("New Bank Branch");
					page.setViewFile("admin/bank_branch_edit.jsp");
				}
				if(uris.get(Constants.URI_3).equals("edit")){
					int bank_branch_id = Integer.parseInt(uris.get(Constants.URI_4));
					BankBranch bankBranch = bankBranchService.getBankBranch(bank_branch_id);
					model.addAttribute("data", bankBranch);
					page.setPage_title("Edit Bank Branch");
					page.setViewFile("admin/bank_branch_edit.jsp");
				}
			}
			if(uris.get(Constants.URI_2).equals("dashboard") )
			{
				page.setPage_title("Dashboard");
				page.setViewFile("admin/dashboard.jsp");
			}
		}
		if(uris.get(Constants.URI_2).equals(""))
		{
			page.setPage_title("Admin Login");
			page.setViewFile("admin/login.jsp");
		}
		
		if(uris.get(Constants.URI_2).toString().equals("login"))
		{
			
			//New user is trying to login.
			if(HttpHelper.isPOST(request))
			{
				//Correct. Redirect to home
				int admin_id = AuthenticationHelper.authenticateAdmin(request);
				if(admin_id == 1)
				{
					isAdmin = true;
					session.setAttribute(Constants.IS_ADMIN, isAdmin);
					message_queue.add(new Message("Login Successful.", "success"));
//					redirectAttributes.addFlashAttribute("message_queue", message_queue);
//					return "redirect:admin/dashboard";
					page.setPage_title("Dashboard");
					page.setViewFile("admin/dashboard.jsp");
				} else { //Not correct. Show home screen with error message
					message_queue.add(new Message("Username or Password is Invalid! Please Try Again.","error"));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					return "redirect:/admin/login";
				}
				
			}
		}
		
		if(uris.get(Constants.URI_2).toString().equals("session"))
		{
			//Logout Request
			if(uris.get(Constants.URI_3).toString().equals("logout")){
				session.invalidate();
				message_queue.add(new Message("Successfully Logout.", "success"));
				redirectAttributes.addFlashAttribute("message_queue", message_queue);
				return "redirect:/admin/login";
			}
		}
			
		Map<String, MessageQueue> inputFlashMap = (Map<String, MessageQueue>) RequestContextUtils.getInputFlashMap(request);
		if (inputFlashMap != null) {
		    MessageQueue mq =  inputFlashMap.get("message_queue");
		    for (Message m : mq){
		    	message_queue.add(m);
		    }
		}
		
		model.addAttribute("message_queue", message_queue);
		model.addAttribute("page", page);
		
		if( !isAdmin)
			return "admin/login";
		else
			return "admin_template";
	}
	
	
	
	@RequestMapping(value = "/admin/test/{action}", method = RequestMethod.POST)
	public String customer_list(HttpServletRequest request, HttpServletResponse response
			,Model model, final RedirectAttributes redirectAttributes
			,@PathVariable String action
			){
		
		PageEnvironment page = new PageEnvironment();
		MessageQueue message_queue = new MessageQueue();
		HttpSession session = request.getSession();
		boolean isAdmin = Boolean.parseBoolean(session.getAttribute(Constants.IS_ADMIN) != null ? session.getAttribute(Constants.IS_ADMIN).toString(): "false");
		
		if ( isAdmin )
		{
			if(action.equals("save_customer")){
				Customer customer = HttpHelper.sanatizeRequestedCustomerModel(request);
				ChangesStatus status = customerService.saveCustomer(customer);
				message_queue.add(new Message(status.getMessage(),((status.getStatus()) ? "success" : "error")));
				redirectAttributes.addFlashAttribute("message_queue", message_queue);
				
				return "redirect:/admin/customer/list";
			}
			
			if(action.equals("save_account")){
				ChangesStatus validationToPin = ValidationHelper.isValidSecurityPin(request.getParameter("pin"));
				Account account = HttpHelper.sanatizeRequestedAccountModel(request);
				ArrayList<MapAccountCustomer> mapAccountCustomer = HttpHelper.sanatizeRequestedMapAccountCustomerModel(request);
				if(!validationToPin.getStatus())
				{
					message_queue.add(new Message(validationToPin.getMessage(), "error"));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					
					return "redirect:/admin/account/new";
				}
				else if( !(account.getAccount_id() >0) && (mapAccountCustomer.size() <1)){
					message_queue.add(new Message("Please add at least 1 customer.","error"));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					
					return "redirect:/admin/account/new";
				} else{
					ChangesStatus status = accountService.saveAccount(account, mapAccountCustomer);
					message_queue.add(new Message(status.getMessage(),((status.getStatus()) ? "success" : "error")));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					
					return "redirect:/admin/dashboard";
				}
				
				
			}
			
			if(action.equals("search_customer")){
				try 
				{
					Connection connection = DBConnectionHelper.getConnection();
					Customer customer = new Customer();
					//customer.setCustomer_id(Integer.parseInt(request.getParameter("customer_id")));
					SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("mm/dd/yyyy");
					try {
						Date lFromDate1 = datetimeFormatter1.parse(request
								.getParameter("date_of_birth"));
						Timestamp fromTS1 = new Timestamp(lFromDate1.getTime());
						customer.setDate_of_birth(fromTS1);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					List<Customer> customerList = customerDAO.searchMatching(connection,customer);
					model.addAttribute("data", customerList);
					page.setPage_title("List Customer");
					page.setViewFile("admin/customer_list.jsp");
					model.addAttribute("message_queue", message_queue);
					model.addAttribute("page", page);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				return "admin_template";
			}
			
			if(action.equals("search_account")){
				return "redirect:/admin/account/edit/" + request.getParameter("account_id");
			}
			
			if(action.equals("save_bank_branch")){
				BankBranch bankBranch = HttpHelper.sanatizeRequestedBankBranchModel(request);
				ChangesStatus status = bankBranchService.saveBankBranch(bankBranch);
				message_queue.add(new Message(status.getMessage(),((status.getStatus()) ? "success" : "error")));
				redirectAttributes.addFlashAttribute("message_queue", message_queue);
				
				return "redirect:/admin/bank_branch/list";
			}
		}
		else
			return "admin/login";
		
		return "";
	}
 
}
