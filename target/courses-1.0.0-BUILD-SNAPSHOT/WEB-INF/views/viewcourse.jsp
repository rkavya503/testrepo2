 <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>    
   <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
  
<h1>Course List</h1>  
<table border="2" width="70%" cellpadding="2">  
<tr><th>Id</th><th>Name</th><th>Duration</th><th>Cost</th><th>Delete</th><th>Edit</th></tr> 
 <c:forEach var="course" items="${courses}">  
   <tr>  
   <td>${course.id}</td>  
   <td>${course.name}</td>  
   <td>${course.duration}</td>  
   <td>${course.cost}</td>  
   <td><a href="deletcourse/${course.id}">Delete</a></td>  
   <td><a href="editcourse/${course.name}">Edit</a></td>  
   </tr> 
   </c:forEach> 
   </table>  
   <br/>  
   <a href="add">Add New Course</a>  