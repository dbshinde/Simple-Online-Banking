<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<table class="table table-hover" style="table-layout: fixed;">
	<thead>
		<th>Customer ID</th>
		<th>Name</th>
		<th>Gender</th>
		<th>Date of Birth</th>
		<th>NRIC</th>
		<th></th>
	</thead>
	<tbody>
		<c:forEach items="${data}" var="customer">
			<tr>
				<td>${customer.customer_id}</td>
				<td><b style="font-size: 16px;">${customer.givenname}</b><br/></td>
				<td>${customer.gender}</td>
				<td><fmt:formatDate value="${customer.date_of_birth}" pattern="MM/d/yyyy" /></td>
				<td>${customer.nric}</td>
				<td><a class="btn btn-small"
					style="width: 50px; margin-bottom: 5px;"
					href="${page.url_host}${page.url_apppath}admin/customer/edit/${customer.customer_id}">Edit</a>&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="btn btn-small" style="width: 50px;margin-bottom: 5px;"
					href="${page.url_host}${page.url_apppath}admin/customer/details/${customer.customer_id}">Details</a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>