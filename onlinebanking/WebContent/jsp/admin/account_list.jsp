<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="table table-hover" style="table-layout: fixed;">
	<thead>
		<th>Account ID</th>
		<th>Balance</th>
		<th>Account Type</th>
		<th>Branch Id</th>
		<th>Branch Name</th>
	</thead>
	<tbody>
		<c:forEach items="${data}" var="account">
			<tr>
				<td>${account.account_id}</td>
				<td style="width: 100px">${account.amount}</td>
				<td>${account.account_type.account_type}</td>
				<td>${account.bank_branch_id}</td>
				<td>${account.bank_branch.name}</td>
				<td><a class="btn btn-small"
					style="width: 50px; margin-bottom: 5px;"
					href="${page.url_host}${page.url_apppath}admin/account/edit/${account.account_id}">Edit</a> &nbsp;&nbsp;&nbsp;&nbsp;
					<a class="btn btn-small" style="width: 50px;margin-bottom: 5px;"
					href="${page.url_host}${page.url_apppath}admin/account/details/${account.account_id}">Details</a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>