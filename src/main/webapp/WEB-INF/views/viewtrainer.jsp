 <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>    
   <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
  
<h1>Course List</h1>  
<table border="2" width="70%" cellpadding="2">  
<tr><th>Mobile Number</th><th>Trainer's Name</th><th>Subject</th><th>Delete</th><th>Download</th></tr> 
 <c:forEach var="trainer" items="${trainers}">  
   <tr>  
   <td>${trainer.mobilenumber}</td>  
   <td>${trainer.name}</td>  
   <td>${trainer.subject}</td>  
   <td><a href="delettrainer/${trainer.mobilenumber}">Delete</a></td> 
   <td><a href="downloadresume/${trainer.subject}">Download</a></td>  
   </tr> 
   </c:forEach> 
   </table>  
   <br/>  
   <a href="add">Add New Trainer</a>  