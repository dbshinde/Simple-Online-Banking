<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<table class="table table-hover" style="table-layout: fixed;">
	<thead>
		<th style="text-align: center; width: 100px;">Customer Id</th>
		<th style="text-align: center; width: 100px;">Name</th>
		<th style="text-align: center; width: 100px;">Gender</th>
		<th style="text-align: center; width: 100px;">Birth Date</th>
		<th style="text-align: center; width: 100px;">NRIC</th>
		<th style="text-align: center; width: 100px;"></th>
		<th style="text-align: center; width: 100px;"></th>
	</thead>
	<tbody>
		<c:forEach items="${data}" var="customer">
			<tr>
				<td style="text-align: center;">${customer.customer_id}</td>
				<td style="text-align: center; width: 100px;"><b style="font-size: 16px;">${customer.givenname}</b><br/></td>
				<td style="text-align: center; width: 100px;">${customer.gender}</td>
				<td style="text-align: center; width: 100px;"><fmt:formatDate value="${customer.date_of_birth}" pattern="MM/d/yyyy" /></td>
				<td style="text-align: center; width: 100px;">${customer.nric}</td>
				<td style="text-align: right; width: 100px;"><a class="btn btn-small" style="width: 50px; margin-bottom: 3px;"
					href="${page.url_host}${page.url_apppath}admin/customer/edit/${customer.customer_id}">Edit</a>&nbsp;&nbsp;&nbsp;
				</td>
				<td style="text-align: left; width: 100px;"><a class="btn btn-small" style="width: 50px;margin-bottom: 3px;"
					href="${page.url_host}${page.url_apppath}admin/customer/details/${customer.customer_id}">Details</a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>