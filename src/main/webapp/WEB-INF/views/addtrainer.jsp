<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<a href="home">Back to Home</a>
<br></br>
<h1>Add New Trainer</h1>
	<form action="upload">
		<input type="submit" value="Upload Resume of Trainers">
	</form>
<form:form method="post" action="savetrainer">
	<table>
		<tr>
			<td>Mobile Number :</td>
			<td><form:input path="mobilenumber" /></td>
		</tr>
		<tr>
			<td>Name :</td>
			<td><form:input path="name" /></td>
		</tr>
		<tr>
			<td>Subject :</td>
			<td><form:input path="subject" /></td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="save" /></td>
		</tr>
	</table>
</form:form>

