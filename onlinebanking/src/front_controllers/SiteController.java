package front_controllers;

import helpers.AuthenticationHelper;
import helpers.HttpHelper;
import helpers.PermissionHelper;
import helpers.ValidationHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Account;
import models.Customer;

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
import business_logic.CustomerService;
import business_logic.TransactionService;
import exceptions.BelowMinimumBalanceException;
import exceptions.NotFoundException;
import exceptions.OverDraftLimitExceededException;


@Controller
public class SiteController {

	Logger logger = Logger.getLogger(SiteController.class.getName());
	
	private @Autowired HttpServletRequest request;
	private @Autowired CustomerService customerService;
	private @Autowired AccountService accountService;
	private @Autowired TransactionService transactionService;
	
	
	@RequestMapping(value = {"/*","/*/*","/*/*/*"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String sitePages(Model model, HttpServletRequest request, HttpServletResponse response,final RedirectAttributes redirectAttributes){
		
		logger.info("request -->" + request.getRequestURI());
		//Splitting the URI
		HashMap<String, String> uris = HttpHelper.analyseRequest(request);
		
		//Initialize essential objects
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(15*60);
		PageEnvironment page = new PageEnvironment();
		MessageQueue message_queue = new MessageQueue();
		
		//New user ?
		if(session.isNew() || session.getAttribute("customer_id") == null)
		{
			//New user is trying to login.
			if(HttpHelper.isPOST(request) && uris.get(Constants.URI_1).toString().equals("login"))
			{
				//Correct. Redirect to home
				int customer_id = AuthenticationHelper.isValidLogin(request);
				if(customer_id > 0)
				{
					session.setAttribute("customer_id", customer_id);
					message_queue.add(new Message("Login Successful.", "success"));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					return "redirect:/dashboard";
				} 
				else 
				{ //Not correct. Show home screen with error message
					message_queue.add(new Message("Username or Password is Invalid! Please Try Again.","error"));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					return "redirect:/";
				}
			}
			//Show the login screen
			page.setPage_title("Login");
			redirectAttributes.addFlashAttribute("message_queue", message_queue);
			model.addAttribute("page", page);
			return "site/login";
		} 
		
		if(uris.get(Constants.URI_1).toString().equals("session"))
		{
			//Logout Request
			if(uris.get(Constants.URI_2).toString().equals("logout"))
			{
				session.invalidate();
				message_queue.add(new Message("Successfully Logout.", "success"));
				redirectAttributes.addFlashAttribute("message_queue", message_queue);
				return "redirect:/";
			}
			//Account Session Request
			if(uris.get(Constants.URI_2).toString().equals("account"))
			{
				int account_id = Integer.parseInt(uris.get(Constants.URI_3));
				int customer_id = Integer.parseInt(session.getAttribute("customer_id").toString());
				if(PermissionHelper.isThisAccountOwnByThisCustomer(account_id, customer_id))
				{
					message_queue.add(new Message("Account Selected.", "success"));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					session.setAttribute("account_id", account_id);	
				} 
				else 
				{
					message_queue.add(new Message("You don't have permission to access this account.", "error"));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
				}
				
				return "redirect:/dashboard";
			}
		} 
		else if(uris.get(Constants.URI_1).toString().equals("post") && HttpHelper.isPOST(request) )
		{
			
			//Deposit POST Request
			if(uris.get(Constants.URI_2).toString().equals("deposit"))
			{
				int account_id_to = Integer.parseInt(session.getAttribute("account_id").toString());
				int customer_id_by = Integer.parseInt(session.getAttribute("customer_id").toString());

				//Validate Input Fields
				ChangesStatus validationToAmount  = ValidationHelper.isValidAmount(request.getParameter("deposit_amount").toString());
				ChangesStatus validationToPin     = ValidationHelper.isValidSecurityPin(request.getParameter("pin").toString());
				
				if(!validationToAmount.getStatus())
				{
					message_queue.add(new Message(validationToAmount.getMessage(), "error"));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					return "redirect:/bank_account/deposit_fund";
				}
				if(!validationToPin.getStatus())
				{
					message_queue.add(new Message(validationToPin.getMessage(), "error"));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					return "redirect:/bank_account/deposit_fund";
				}
				
				ChangesStatus queryCondition = accountService.depositMoney(account_id_to, customer_id_by, Float.parseFloat(request.getParameter("deposit_amount").toString()),
						Integer.parseInt(request.getParameter("pin").toString()));
				
				message_queue.add(new Message( queryCondition.getMessage(), ((queryCondition.getStatus()) ? "success" : "error")));
				redirectAttributes.addFlashAttribute("message_queue", message_queue);
				return ((queryCondition.getStatus()) ? "redirect:/dashboard" : "redirect:/bank_account/deposit_fund");
			}
			//Withdraw POST Request
			else if (uris.get(Constants.URI_2).toString().equals("withdraw"))
			{
				int account_id = Integer.parseInt(session.getAttribute("account_id").toString());
				int customer_id_by = Integer.parseInt(session.getAttribute("customer_id").toString());
				ChangesStatus queryCondition;
				
				//Validate Input Fields
				ChangesStatus validationToAmount  = ValidationHelper.isValidAmount(request.getParameter("withdraw_amount").toString());
				ChangesStatus validationToPin     = ValidationHelper.isValidSecurityPin(request.getParameter("pin").toString());
				if(!validationToAmount.getStatus()){
					message_queue.add(new Message(validationToAmount.getMessage(), "error"));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					return "redirect:/bank_account/withdraw_fund";
				}
				if(!validationToPin.getStatus()){
					message_queue.add(new Message(validationToPin.getMessage(), "error"));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					return "redirect:/bank_account/withdraw_fund";
				}
				
				try 
				{
					queryCondition = accountService.withdrawMoney(account_id, customer_id_by, Integer.parseInt(request.getParameter("withdraw_amount")), Integer.parseInt(request.getParameter("pin")));
					message_queue.add(new Message( queryCondition.getMessage(), ((queryCondition.getStatus()) ? "success" : "error")));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					return ((queryCondition.getStatus()) ? "redirect:/dashboard" : "redirect:/bank_account/withdraw_fund");
				} 
				catch (BelowMinimumBalanceException e) {
					e.printStackTrace();
					message_queue.add(new Message( "Your Balanced reached to below minimum range", "error"));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					return "redirect:/dashboard";
				} 
				catch (NumberFormatException e) {
					e.printStackTrace();
					return "redirect:/dashboard";
				} 
				catch (OverDraftLimitExceededException e) {
					e.printStackTrace();
					message_queue.add(new Message( "Your Balanced reached to overdraft limit", "error"));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					return "redirect:/dashboard";
				}
			}
			//Transfer POST Request
			else if (uris.get(Constants.URI_2).toString().equals("transfer")){
				
				//Validate Input Fields
				ChangesStatus validationToAmount  = ValidationHelper.isValidAmount(request.getParameter("transfer_amount").toString());
				ChangesStatus validationToPin     = ValidationHelper.isValidSecurityPin(request.getParameter("pin").toString());
				ChangesStatus validationToAccount = ValidationHelper.isValidAccountId(request.getParameter("account_id_to"));
				if(!validationToAmount.getStatus()){
					message_queue.add(new Message(validationToAmount.getMessage(), "error"));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					return "redirect:/bank_account/transfer_fund";
				}
				if(!validationToPin.getStatus()){
					message_queue.add(new Message(validationToPin.getMessage(), "error"));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					return "redirect:/bank_account/transfer_fund";
				}
				if(!validationToAccount.getStatus()){
					message_queue.add(new Message(validationToAccount.getMessage(), "error"));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					return "redirect:/bank_account/transfer_fund";
				} 
				
				
				int account_id_from = Integer.parseInt(session.getAttribute("account_id").toString());
				int account_id_to   = Integer.parseInt(request.getParameter("account_id_to"));
				int customer_id_by  = Integer.parseInt(session.getAttribute("customer_id").toString());
				ChangesStatus queryCondition;
				try 
				{
					queryCondition = accountService.transferMoney(account_id_from, account_id_to, customer_id_by, Integer.parseInt(request.getParameter("transfer_amount")), Integer.parseInt(request.getParameter("pin")));
					message_queue.add(new Message( queryCondition.getMessage(), ((queryCondition.getStatus()) ? "success" : "error")));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					return ((queryCondition.getStatus()) ? "redirect:/dashboard" : "redirect:/bank_account/transfer_fund");
				} 
				catch (OverDraftLimitExceededException e) {
					e.printStackTrace();
					message_queue.add(new Message( "Overdraft", "error"));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					return "redirect:/bank_account/transfer_fund";
				} 
				catch (NumberFormatException e) {
					e.printStackTrace();
					message_queue.add(new Message( "Number format error", "error"));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					return "redirect:/bank_account/transfer_fund";
				} 
				catch (BelowMinimumBalanceException e) {
					e.printStackTrace();
					message_queue.add(new Message( "Your reached below minimum balance", "error"));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					return "redirect:/bank_account/transfer_fund";
				}
			}
			return "redirect:/dashboard";
		}
		
		
		//Loading Customer and Account Profile
		int customer_id = Integer.parseInt(session.getAttribute("customer_id").toString());
		
		try 
		{
			Customer customer = customerService.getCustomer(customer_id);
			model.addAttribute("customer", customer);
			
			ArrayList<Account> accounts = (ArrayList<Account>) customer.getAccounts();
			
			if(customer.getAccounts().size() == 1) //Customer has only one account
			{
				Account account = accounts.get(0);
				session.setAttribute("account_id",account.getAccount_id());
			}
			
			//Customer has more than one account, but havn't choose one |or| no account 
			else if(session.getAttribute("account_id") == null)
			{
				//Except direct access to choose account page everything will be redirect
				if( !((uris.get(Constants.URI_1).toString().equals("bank_account")) && (uris.get(Constants.URI_2).toString().equals("account_list"))) ) {
					
					if (customer.getAccounts().size() >1) //Customer has more than one account
					{ 
						message_queue.add(new Message("Please choose an account first.", "info"));
					} 
					else //Customer has no account
					{ 
						message_queue.add(new Message("You don't have any accounts! Please open an account at nearest bank branch. Thanks.", "info"));
					}
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					return "redirect:/bank_account/account_list";
				}
			} 
			
			//account_id in session and continue
			if( !(session.getAttribute("account_id") == null))
			{
				int account_id = Integer.parseInt(session.getAttribute("account_id").toString());
				Account account = accountService.getAccount(account_id);
				model.addAttribute("account", account);
			}
		} 
		catch (NotFoundException e) {
			e.printStackTrace();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		//GET Requests Routing
		if(HttpHelper.isGET(request))
		{
			if( uris.get(Constants.URI_1).toString().equals("dashboard") || uris.get(Constants.URI_1).toString().equals("") )
			{
				page.setPage_title("Dashboard");
				page.setView_file("site/dashboard");
				
			} 
			else if ( uris.get(Constants.URI_1).toString().equals("bank_account") && uris.get(Constants.URI_2).toString().equals("account_list") )
			{
				page.setPage_title("Choose Account");
				page.setView_file("site/bank_account_list");
				
			} 
			else if ( uris.get(Constants.URI_1).toString().equals("bank_account") && uris.get(Constants.URI_2).toString().equals("show_balance") )
			{
				
				Account account = new Account();
				int account_id = Integer.parseInt(session.getAttribute("account_id").toString());
				try 
				{
					account = accountService.getAccountDetails(account_id);
					model.addAttribute("account", account);
					page.setPage_title("Bank Accout Information");
					page.setView_file("site/show_balance");
				} 
				catch (NotFoundException e) {
					e.printStackTrace();
				}
			} 
			else if ( uris.get(Constants.URI_1).toString().equals("bank_account") && uris.get(Constants.URI_2).toString().equals("deposit_fund") )
			{
				page.setPage_title("Deposit");
				page.setView_file("site/deposit_fund");
			} 
			else if ( uris.get(Constants.URI_1).toString().equals("bank_account") && uris.get(Constants.URI_2).toString().equals("withdraw_fund") )
			{
				page.setPage_title("Withdraw");
				page.setView_file("site/withdraw_fund");
			} 
			else if ( uris.get(Constants.URI_1).toString().equals("bank_account") && uris.get(Constants.URI_2).toString().equals("transfer_fund") )
			{
				page.setPage_title("Transfer Money");
				page.setView_file("site/transfer_fund");
			} 
			else if ( uris.get(Constants.URI_1).toString().equals("bank_account") && uris.get(Constants.URI_2).toString().equals("transfer_history") )
			{
				int account_id = Integer.parseInt(session.getAttribute("account_id").toString());
				ArrayList<TransactionHistoryOfAnAccount> t_histories = (ArrayList<TransactionHistoryOfAnAccount>) transactionService.getAllTransactionsOfAAccount(account_id);
				model.addAttribute("t_histories", t_histories);
				
				page.setPage_title("Transfer History");
				page.setView_file("site/history");
						
			} 
			else if (uris.get(Constants.URI_1).toString().equals("profile"))
			{
				page.setPage_title("My Information");
				page.setView_file("site/profile");
			} 
			else 
			{
				return "error_404";
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
		//redirectAttributes.addFlashAttribute("message_queue", message_queue);
		model.addAttribute("page", page);
		return "site_template";
	}
	
	@RequestMapping(value = {"/ajax/{to_get}/{id}"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String ajax(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable String to_get, @PathVariable String id){
		
		if(to_get.equals("get_owners_by_account_id")){
			int account_id = Integer.parseInt(id);
			Account account;
			try {
				account = accountService.getAccountDetails(account_id);
				ArrayList<Customer> customers = account.getCustomers();
				
				String str = "<div style='font:10px;'><u>Owner(s)</u><br />";
				for(Customer c : customers){
					str += c.getGivenname()+"<br />";
				}
				str += "</div>";
				request.setAttribute("out_string", str);
			} catch (NotFoundException e) {
				e.printStackTrace();
			}
		}
		
		return "ajax";
	}
	
}
