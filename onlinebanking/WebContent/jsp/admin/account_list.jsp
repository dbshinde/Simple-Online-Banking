<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="table table-hover">
	<thead>
		<th>Account ID</th>
		<th>Balance</th>
		<th>Account Type</th>
		<th>Branch</th>
		<th></th>
	</thead>
	<tbody>
		<c:forEach items="${data}" var="account">
			<tr>
				<td>${account.account_id}</td>
				<td>${account.amount}</td>
				<td>${account.account_type}</td>
				<td>${account.bank_branch_id}</td>
				<td><a class="btn btn-small"
					style="width: 50px; margin-bottom: 5px;"
					href="${page.url_host}${page.url_apppath}admin/account/edit/${account.account_id}">Edit</a><br />
					<a class="btn btn-small" style="width: 50px;"
					href="${page.url_host}${page.url_apppath}admin/account/details/${account.account_id}">Details</a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>