<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h3>Bank Branch List</h3>

<table class="table">
	<thead>
		<th style="text-align: center; width: 100px;">ID</th>
		<th style="text-align: center; width: 100px;">Name</th>
		<th style="text-align: center; width: 100px;">Location</th>
		<th style="text-align: center; width: 100px;">Description</th>
		<th></th>
	</thead>
	<tbody>
		<c:forEach items="${data}" var="bank_branch">
			<tr>
				<td style="text-align: center; width: 100px;">${bank_branch.bank_branch_id}</td>
				<td style="text-align: center; width: 100px;">${bank_branch.name}</td>
				<td style="text-align: center; width: 100px;">${bank_branch.location}</td>
				<td style="text-align: center; width: 100px;">${bank_branch.description}</td>
				<td style="text-align: right; width: 100px;"><a class="btn btn-small" style="width: 50px; margin-bottom: 5px;"
					href="${page.url_host}${page.url_apppath}admin/bank_branch/edit/${bank_branch.bank_branch_id}">Edit</a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>