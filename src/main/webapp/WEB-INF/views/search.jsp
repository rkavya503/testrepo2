<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
  
  		 <a href="home">Back to Home</a>  <br></br>
        <h1>Search by course name</h1>  
       <form:form method="post" action="searchcourse">    
        <table >  
         <tr>    
          <td>Name : </td>   
          <td><form:input path="name"  /></td>  
         </tr>    
         <tr>    
          <td> </td>    
          <td><input type="submit" value="search" /></td>    
         </tr>    
        </table>    
       </form:form>    