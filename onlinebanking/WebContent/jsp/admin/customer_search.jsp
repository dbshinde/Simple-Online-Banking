<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h3>Search Customer</h3>
<hr />
<form
	action="${page.url_host}${page.url_apppath}admin/post/search_customer"
	method="post" name="form-customer" class="form-search">
	<input type="text" name="customer_id" class="input-medium search-query" autocomplete="off"
		placeholder="Customer ID" style="width: 150px" /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="text" autocomplete="off" name="date_of_birth" class="input-medium search-query"
		placeholder="Date of Birth   (MM/DD/YYYY)" style="width: 250px" /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	
	<button type="submit" class="btn btn-primary">Search</button>
</form>