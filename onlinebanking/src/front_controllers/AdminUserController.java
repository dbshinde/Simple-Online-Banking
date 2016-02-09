package front_controllers;


import static helpers.Utils.isNotNullAndEmpty;
import helpers.AuthenticationHelper;
import helpers.DBConnectionHelper;
import helpers.HttpHelper;
import helpers.Utils;
import helpers.ValidationHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
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
import dao.AccountDAO;
import dao.CustomerDAO;
import enums.ActionEnum;
import exceptions.NotFoundException;

@Controller
public class AdminUserController{
	
	
	private @Autowired HttpServletRequest request;
	private @Autowired CustomerService customerService;
	private @Autowired AccountService accountService;
	private @Autowired TransactionService transactionService;
	private @Autowired BankBranchService bankBranchService;
	private @Autowired CustomerDAO customerDAO;
	private @Autowired AccountDAO accountDAO;
	
	Logger logger = Logger.getLogger(AdminUserController.class.getName());
	
	@RequestMapping(value = {"/admin" , "/admin/*","/admin/*/*","/admin/*/*/*"})
	public String dashboard(HttpServletRequest request, HttpServletResponse response, Model model, final RedirectAttributes redirectAttributes) 
	{
		logger.info("request -->" + request.getRequestURI());
		PageEnvironment page = new PageEnvironment();
		MessageQueue message_queue = new MessageQueue();
		HttpSession session = request.getSession();
		HashMap<String, String> uris = HttpHelper.analyseRequest(request);
		boolean isAdmin = Boolean.parseBoolean(session.getAttribute(Constants.IS_ADMIN) != null ? session.getAttribute(Constants.IS_ADMIN).toString(): "false");
		
		if ( isAdmin )
		{
			switch (ActionEnum.getValue(uris.get(Constants.URI_2)))
			{
				case CUSTOMER : //Customer
				{
					switch (ActionEnum.getValue(uris.get(Constants.URI_3)))
					{
						//Listing Down the customer
						case LIST : {
							List<Customer> customerList = customerService.getCustomers();
							model.addAttribute("data", customerList);
							page.setPage_title("List Customer");
							page.setViewFile("admin/customer_list.jsp");
							break;
						}
						
						//New Customer
						case NEW : {
							model.addAttribute("data", new Customer());
							page.setPage_title("New Customer");
							page.setViewFile("admin/customer_edit.jsp");
							break;
						}
						
						//Editing a new customer
						case EDIT : {
							try 
							{
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
						break;
						//Customer search page
						case SEARCH : {
							page.setPage_title("Search Customer");
							page.setViewFile("admin/customer_search.jsp");
							break;
						}
						
						//Showing customer and his|her Acounts
						case DETAILS : {
							try 
							{
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
						
						break;
					}
					
					break;
				}
				
				case ACCOUNT : //Account
				{
					switch (ActionEnum.getValue(uris.get(Constants.URI_3)))
					{
						case LIST : {
							List<Account> AccountsList = accountService.getAccounts();
							model.addAttribute("data", AccountsList);
							page.setPage_title("List Accounts");
							page.setViewFile("admin/account_list.jsp");
							break;
						}
						
						case NEW :{
							model.addAttribute("data", new Account());
							page.setPage_title("New Account");
							page.setViewFile("admin/account_edit.jsp");
							break;
						}
						
						case EDIT:{
							try
							{
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
							break;
						}
						
						//Account search page
						case SEARCH: {
							page.setPage_title("Search Account");
							page.setViewFile("admin/account_search.jsp");
							break;
						}
						
						case DETAILS:{
							int account_id = Integer.parseInt(uris.get(Constants.URI_4));
							Account data;
							try 
							{
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
							break;
						}
						
						case TRANSACTION:{
							int account_id = Integer.parseInt(uris.get(Constants.URI_4));
							ArrayList<TransactionHistoryOfAnAccount> t_histories = (ArrayList<TransactionHistoryOfAnAccount>) transactionService.getAllTransactionsOfAAccount(account_id);
							model.addAttribute("data", t_histories);
							model.addAttribute("account_id", account_id);
							page.setPage_title("Transfer History");
							page.setView_file("admin/account_transaction.jsp");
							break;
						}
					}
					
					break;
				}
				
				case BANK_BRANCH:
				{
					switch (ActionEnum.getValue(uris.get(Constants.URI_3)))
					{
						case LIST :{
							List<BankBranch> bankBranchList = bankBranchService.getAllBankBranches();
							model.addAttribute("data", bankBranchList);
							page.setPage_title("Bank Branches");
							page.setViewFile("admin/bank_branch_list.jsp");
							break;
						}
						
						case NEW:{
							BankBranch bankBranch = new BankBranch();
							model.addAttribute("data", bankBranch);
							page.setPage_title("New Bank Branch");
							page.setViewFile("admin/bank_branch_edit.jsp");
							break;
						}
						
						case EDIT:{
							int bank_branch_id = Integer.parseInt(uris.get(Constants.URI_4));
							BankBranch bankBranch = bankBranchService.getBankBranch(bank_branch_id);
							model.addAttribute("data", bankBranch);
							page.setPage_title("Edit Bank Branch");
							page.setViewFile("admin/bank_branch_edit.jsp");
							break;
						}
					}
					
					break;
				}
				
				
				case DASHBOARD:{
					page.setPage_title("Dashboard");
					page.setViewFile("admin/dashboard.jsp");
					break;
				}
				
				case SESSION :{
					//Logout Request
					if(uris.get(Constants.URI_3).toString().equals("logout")){
						session.invalidate();
						message_queue.add(new Message("Successfully Logout.", "success"));
						redirectAttributes.addFlashAttribute("message_queue", message_queue);
						return "redirect:/admin/login";
					}
					
					break;
				}
				
				default : // uris.get(Constants.URI_2).equals("")
				{
					page.setPage_title("Dashboard");
					page.setViewFile("admin/dashboard.jsp");
					break;
				}
			}
		}
		else
		{
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
						//redirectAttributes.addFlashAttribute("message_queue", message_queue);
						//return "redirect:admin/dashboard";
						page.setPage_title("Dashboard");
						page.setViewFile("admin/dashboard.jsp");
					} else { //Not correct. Show home screen with error message
						message_queue.add(new Message("Username or Password is Invalid! Please Try Again.","error"));
						redirectAttributes.addFlashAttribute("message_queue", message_queue);
						return "redirect:/admin/login";
					}
					
				}
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
	
	
	
	@RequestMapping(value = "/admin/post/{action}", method = RequestMethod.POST)
	public String customer_list(HttpServletRequest request, HttpServletResponse response ,Model model, final RedirectAttributes redirectAttributes ,@PathVariable String action)
	{
		PageEnvironment page = new PageEnvironment();
		MessageQueue message_queue = new MessageQueue();
		HttpSession session = request.getSession();
		boolean isAdmin = Boolean.parseBoolean(session.getAttribute(Constants.IS_ADMIN) != null ? session.getAttribute(Constants.IS_ADMIN).toString(): "false");
		
		if(isAdmin)
		{
			switch (ActionEnum.getValue(action))
			{
				case SAVE_CUSTOMER : {
					Customer customer = HttpHelper.sanatizeRequestedCustomerModel(request);
					ChangesStatus status = customerService.saveCustomer(customer);
					message_queue.add(new Message(status.getMessage(),((status.getStatus()) ? "success" : "error")));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					
					return "redirect:/admin/customer/list";
				}
				
				case SAVE_ACCOUNT : {
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
				
				case SAVE_BANK_BRANCH : {
					BankBranch bankBranch = HttpHelper.sanatizeRequestedBankBranchModel(request);
					ChangesStatus status = bankBranchService.saveBankBranch(bankBranch);
					message_queue.add(new Message(status.getMessage(),((status.getStatus()) ? "success" : "error")));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					
					return "redirect:/admin/bank_branch/list";
				}
				
				case SEARCH_CUSTOMER : {
					try 
					{
						Connection connection = DBConnectionHelper.getConnection();
						Customer customer = new Customer();
						customer.setCustomer_id(isNotNullAndEmpty(request.getParameter("customer_id")) ? Integer.parseInt(request.getParameter("customer_id")) : null);
						if(isNotNullAndEmpty(request.getParameter("date_of_birth")))
						{
							try 
							{
								customer.setDate_of_birth(Utils.getDateTimeStamp(request.getParameter("date_of_birth")));
							} catch (ParseException e) {
								e.printStackTrace();
							}
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
				
				case SEARCH_ACCOUNT : {
					try 
					{
						Connection connection = DBConnectionHelper.getConnection();
						Account account = new Account();
						account.setAccount_id(isNotNullAndEmpty(request.getParameter("account_id")) ?Integer.parseInt(request.getParameter("account_id")) : null);
						account.setAccount_typeId(isNotNullAndEmpty(request.getParameter("account_typeId")) ? Integer.parseInt(request.getParameter("account_typeId")) : null);
						List<Account> accountList = accountDAO.searchMatching(connection,account);
						model.addAttribute("data", accountList);
						page.setPage_title("List Accounts");
						page.setViewFile("admin/account_list.jsp");
						model.addAttribute("message_queue", message_queue);
						model.addAttribute("page", page);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					return "admin_template";
					//return "redirect:/admin/account/edit/" + request.getParameter("account_id");
				}
			}
		}
		else
			return "admin/login";
		
		return "";
	}
 
}
