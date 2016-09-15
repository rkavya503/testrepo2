
<jsp:useBean id="pss2Config" class="com.akuacom.pss2.web.common.Config" scope="request"/>
<div class="akua">
     <p style="margin-top:0;width: 300px;float:left;text-align: left;">
        <a href="<jsp:getProperty name="pss2Config" property="link"/> " >
               <img src="images/logo/<jsp:getProperty name="pss2Config" property="logo"/>" 
               alt="<jsp:getProperty name="pss2Config" property="alt"/>" />
         </a>
    </p>
    <p align="right" >Powered by: <a href="http://www.akuacom.com" target="_blank">
        <img src="images/logo/Akuacom-logo.gif" alt="Akuacom"
             width="100" height="44" longdesc="http://www.akuacom.com" />
    </a></p>
    
    
    <p><jsp:getProperty name="pss2Config" property="copyright"/></p>
</div>

