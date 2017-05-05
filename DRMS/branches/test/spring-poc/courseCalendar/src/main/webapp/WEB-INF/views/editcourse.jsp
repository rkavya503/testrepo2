<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
  
        <h1>Edit Course</h1>  
       <form:form method="POST" action="/courses/editsave">    
        <table >  
         <tr>    
          <td>Course Id : </td>   
          <td><form:input path="id"  /></td>  
         </tr>   
         <tr>    
          <td>Name : </td>   
          <td><form:input path="name"  /></td>  
         </tr>    
         <tr>    
          <td>Cost :</td>    
          <td><form:input path="cost" /></td>  
         </tr>   
         <tr>    
          <td>Duration :</td>    
          <td><form:input path="duration" /></td>  
         </tr>   
         <tr>    
          <td> </td>    
          <td><input type="submit" value="Edit Save" /></td>    
         </tr>    
        </table>    
       </form:form>    