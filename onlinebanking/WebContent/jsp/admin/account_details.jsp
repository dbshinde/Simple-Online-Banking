<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<h3>
	Details of Account ID ${data.account_id}
	<a href="${page.url_host}${page.url_apppath}admin/account/edit/${data.account_id}"
		style="margin-top: 15px;" class="pull-right btn btn-small btn-info">Edit
		Account</a>
	<a href="${page.url_host}${page.url_apppath}admin/account/transaction/${data.account_id}"
		style="margin-top: 15px;margin-right:20px;" class="pull-right btn btn-small">View Transactions</a>
</h3>

<table class="table" style="table-layout: fixed;">
	<tr>
		<td>Account ID</td>
		<td>${data.account_id}</td>
	</tr>
	<tr>
		<td>Balance</td>
		<td><fmt:formatNumber value="${data.amount}" type="currency"/></td>
	</tr>
	<tr>
		<td>Account Type</td>
		<td>${data.account_type.account_type}</td>
	</tr>
	<tr>
		<td>Branch Name</td>
		<td>${data.bank_branch.name}</td>
	</tr>
	<tr>
		<td>Branch Id</td>
		<td>${data.bank_branch_id}</td>
	</tr>
</table>

<h3>Customers own to this Account</h3>
<table class="table table-hover" style="table-layout: fixed;">
	<thead>
		<tr>
			<td>Customer ID</td>
			<td>Name</td>
			<td></td>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${data.customers}" var="_customer">
			<tr>
				<td>${_customer.customer_id}</td>
				<td><b style="font-size: 15px;">${_customer.givenname}</b>
				</td>
				<td><a
					href="${page.url_host}${page.url_apppath}admin/customer/details/${_customer.customer_id}"
					class="btn btn-small">View Details</a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
