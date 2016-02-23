<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="table table-hover" style="table-layout: fixed;">
	<thead>
		<th style="text-align: center; width: 100px;">Account ID</th>
		<th style="text-align: center; width: 100px;">Balance</th>
		<th style="text-align: center; width: 100px;">Account Type</th>
		<th style="text-align: center; width: 100px;">Branch Id</th>
		<th style="text-align: center; width: 100px;">Branch Name</th>
		<th style="text-align: center; width: 100px;"></th>
		<th style="text-align: center; width: 100px;"></th>
	</thead>
	<tbody>
		<c:forEach items="${data}" var="account">
			<tr>
				<td style="text-align: center; width: 100px;">${account.account_id}</td>
				<td style="text-align: center; width: 100px;">${account.amount}</td>
				<td style="text-align: center; width: 100px;">${account.account_type.account_type}</td>
				<td style="text-align: center; width: 100px;">${account.bank_branch_id}</td>
				<td style="text-align: center; width: 100px;">${account.bank_branch.name}</td>
				<td style="text-align: right; width: 100px;"><a class="btn btn-small" style="width: 50px; margin-bottom: 5px;"
					href="${page.url_host}${page.url_apppath}admin/account/edit/${account.account_id}">Edit</a> &nbsp;&nbsp;&nbsp;&nbsp;
				</td>
				<td style="text-align: center; width: 100px;"><a class="btn btn-small" style="width: 50px;margin-bottom: 5px;"
					href="${page.url_host}${page.url_apppath}admin/account/details/${account.account_id}">Details</a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>