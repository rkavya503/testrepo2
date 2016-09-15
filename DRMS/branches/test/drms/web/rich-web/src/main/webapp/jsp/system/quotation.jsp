<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<html>
<f:view>
    <head>
    <title>Quotation o' the Day</title>
    <jsp:include page="../head.jsp" />
    </head>
    <body>
	<jsp:include page="../header.jsp" />
    <h:form>
        
        <rich:panel styleClass="about-content-panel" style="align: left">
            <h:panelGrid columns="1" styleClass="about-panel-style">
            <h:panelGrid columns="2">

                <h:outputLabel value="author" />
                <h:inputText title="author" id="authorId"
                    value="#{qBrider.quotation.author}" />

                <h:outputLabel value="author" />
                <h:inputText title="author" id="authorId"
                    value="#{qBrider.quotation.author}" />

                <h:outputLabel value="author" />
                <h:inputText title="author" id="authorId"
                    value="#{qBrider.quotation.author}" />

                <h:outputLabel value="#{msg.includeDate}" />
                <h:selectBooleanCheckbox title="#{msg.includeDate}"
                    value="#{logConfiguration.includeDate}"
                    onclick="document.getElementById('j_id_jsp_2033881974_1:timestampFormat').disabled = !document.getElementById('j_id_jsp_2033881974_1:timestampFormat').disabled">
                </h:selectBooleanCheckbox>
                <h:outputText style="font-weight:bold" value="#{qodServer.nextQodHtml}" />
            </h:panelGrid>
        </rich:panel>
    </h:form>
    <jsp:include page="../footer.jsp" />
    </body>
</f:view>
</html>
