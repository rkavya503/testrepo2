<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<html>
<head>
<title>Login Page</title>
<style>
.error {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #0F13F5;
	background-color: #f2dede;
	border-color: #ebccd1;
}

#login-box {
	width: 300px;
	padding: 20px;
	margin: 100px auto;
	background: #fff;
	-webkit-border-radius: 2px;
	-moz-border-radius: 2px;
	border: 1px solid #000;
}
</style>
</head>
<body onload='document.loginForm.username.focus();'>

	

	<div id="login-box">

		<h3>Spring Security Authentication</h3>

		<c:if test="${not empty error}">
			<div class="error">${error}</div>
		</c:if>
		

		<form 
			action="<c:url value='/j_spring_security_check' />" method='POST'>

			<table>
				<tr>
					<td>Username:</td>
					<td><input type='text' name='username'></td>
				</tr>
				<tr>
					<td>Password:</td>
					<td><input type='password' name='password' /></td>
				</tr>
				<tr>
					<td colspan='2'><input name="submit" type="submit"
						value="Login" /></td>
				</tr>
			</table>


		</form>
	</div>

</body>
</html>