<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ page import="com.akuacom.utils.config.RuntimeSwitches" %>
<f:loadBundle basename="com.akuacom.pss2.richsite.LogConfiguration_en"
    var="msg" />

<script language="JavaScript">
    //This is the function that will open the
    //new window when the mouse is moved over the link
    function open_new_window(title, message) {
        new_window = open("", "hoverwindow",
                "width=400,height=400,left=10,top=10,scrollbars=yes");

        // open new document 
        new_window.document.open();

        // Text of the new document
        // Replace your " with ' or \" or your document.write statements will fail
        new_window.document
                .write("<html><title>Logfile name time formats</title>");
        new_window.document.write("<body>");
        new_window.document
                .write('<TABLE BORDER=\"0\" WIDTH=\"100%\" VALIGN=\"TOP\"><TR><TD><B>Symbol</B></TD><TD><B>Meaning</B></TD><TD><B>Type</B></TD><TD><B>Example</B></TD></TR><TR BGCOLOR=\"lightgrey\"><TD>G</TD><TD>Era</TD><TD>Text</TD><TD>&#8220;GG&#8221; -&gt; &#8220;AD&#8221;</TD></TR><TR><TD>y</TD><TD>Year</TD><TD>Number</TD><TD>&#8220;yy&#8221; -&gt; &#8220;03&#8243;<BR>&#8220;yyyy&#8221; -&gt; &#8220;2003&#8243;</TD></TR><TR BGCOLOR=\"lightgrey\"><TD>M</TD><TD>Month</TD><TD>Text or Number</TD><TD>&#8220;M&#8221; -&gt; &#8220;7&#8243;<BR>&#8220;M&#8221; -&gt; &#8220;12&#8243;<BR>&#8220;MM&#8221; -&gt; &#8220;07&#8243;<BR>&#8220;MMM&#8221; -&gt; &#8220;Jul&#8221;<BR>&#8220;MMMM&#8221; -&gt; &#8220;December&#8221;</TD></TR><TR><TD>d</TD><TD>Day in month</TD><TD>Number</TD><TD>&#8220;d&#8221; -&gt; &#8220;3&#8243;<BR>&#8220;dd&#8221; -&gt; &#8220;03&#8243;</TD></TR><TR BGCOLOR=\"lightgrey\"><TD>h</TD><TD>Hour (1-12, AM/PM)</TD><TD>Number</TD><TD>&#8220;h&#8221; -&gt; &#8220;3&#8243;<BR>&#8220;hh&#8221; -&gt; &#8220;03&#8243;</TD></TR><TR><TD>H</TD><TD>Hour (0-23)</TD><TD>Number</TD><TD>&#8220;H&#8221; -&gt; &#8220;15&#8243;<BR>&#8220;HH&#8221; -&gt; &#8220;15&#8243;</TD></TR><TR BGCOLOR=\"lightgrey\"><TD>k</TD><TD>Hour (1-24)</TD><TD>Number</TD><TD>&#8220;k&#8221; -&gt; &#8220;3&#8243;<BR>&#8220;kk&#8221; -&gt; &#8220;03&#8243;</TD></TR><TR><TD>K</TD><TD>Hour (0-11 AM/PM)</TD><TD>Number</TD><TD>&#8220;K&#8221; -&gt; &#8220;15&#8243;<BR>&#8220;KK&#8221; -&gt; &#8220;15&#8243;</TD></TR><TR BGCOLOR=\"lightgrey\"><TD>m</TD><TD>Minute</TD><TD>Number</TD><TD>&#8220;m&#8221; -&gt; &#8220;7&#8243;<BR>&#8220;m&#8221; -&gt; &#8220;15&#8243;<BR>&#8220;mm&#8221; -&gt; &#8220;15&#8243;</TD></TR><TR><TD>s</TD><TD>Second</TD><TD>Number</TD><TD>&#8220;s&#8221; -&gt; &#8220;15&#8243;<BR>&#8220;ss&#8221; -&gt; &#8220;15&#8243;</TD></TR><TR BGCOLOR=\"lightgrey\"><TD>S</TD><TD>Millisecond (0-999)</TD><TD>Number</TD><TD>&#8220;SSS&#8221; -&gt; &#8220;007&#8243;</TD></TR><TR><TD>E</TD><TD>Day in week</TD><TD>Text</TD><TD>&#8220;EEE&#8221; -&gt; &#8220;Tue&#8221;<BR>&#8220;EEEE&#8221; -&gt; &#8220;Tuesday&#8221;</TD></TR><TR BGCOLOR=\"lightgrey\"><TD>D</TD><TD>Day in year (1-365 or 1-364)</TD><TD>Number</TD><TD>&#8220;D&#8221; -&gt; &#8220;65&#8243;<BR>&#8220;DDD&#8221; -&gt; &#8220;065&#8243;</TD></TR><TR><TD>F</TD><TD>Day of week in month (1-5)</TD><TD>Number</TD><TD>&#8220;F&#8221; -&gt; &#8220;1&#8243;</TD></TR><TR BGCOLOR=\"lightgrey\"><TD>w</TD><TD>Week in year (1-53)</TD><TD>Number</TD><TD>&#8220;w&#8221; -&gt; &#8220;7&#8243;</TD></TR><TR><TD>W</TD><TD>Week in month (1-5)</TD><TD>Number</TD><TD>&#8220;W&#8221; -&gt; &#8220;3&#8243;</TD></TR><TR BGCOLOR=\"lightgrey\"><TD>a</TD><TD>AM/PM</TD><TD>Text</TD><TD>&#8220;a&#8221; -&gt; &#8220;AM&#8221;<BR>&#8220;aa&#8221; -&gt; &#8220;AM&#8221;</TD></TR><TR><TD>z</TD><TD>Time zone</TD><TD>Text</TD><TD>&#8220;z&#8221; -&gt; &#8220;EST&#8221;<BR>&#8220;zzz&#8221; -&gt; &#8220;EST&#8221;<BR>&#8220;zzzz&#8221; -&gt; &#8220;Eastern Standard Time&#8221;</TD></TR><TR BGCOLOR=\"lightgrey\"><TD>&#8216;</TD><TD>Excape for text</TD><TD>Delimiter</TD><TD>&#8220;&#8216;hour&#8217; h&#8221; -&gt; &#8220;hour 9&#8243;</TD></TR><TR><TD>&#8221;</TD><TD>Single quote</TD><TD>Literal</TD><TD>&#8220;ss&#8221;SSS&#8221; -&gt; &#8220;45&#8242;876&#8243;</TD></TR></TABLE></p>');
        new_window.document.write("</body></html>");

        // close the document
        new_window.document.close();
    }

    function close_window() {
        new_window.close();
    }
</script>
<html>
<f:view>
    <head>
    <title>LogWhacker</title>
    <jsp:include page="../head.jsp" />
    </head>
    <body>
	<jsp:include page="../header_options.jsp" />
    <h:form>
        
        <rich:panel
            styleClass="about-content-panel; font:normal 9px arial"
            style="align: left">
            <p><h:messages style="color:darkred" /></p>
            <f:facet name="header">
                <h:outputText value="Configuration Parameters" />
            </f:facet>

            <h:panelGrid columns="4" width="75%" border="0">

<%if(RuntimeSwitches.EXTRA_UI) {%>
                <h:outputLabel value="#{msg.logWhacker}" />
                <h:graphicImage value="/images/wave.gif" />
<%}%>

                <h:outputLabel value="#{msg.available}" />
                <h:outputText
                    value="#{logWatcherBridge.logWatcherServlet.headroomStr}" />
                <h:outputLabel value="#{msg.currentLength}" />
                <h:outputText
                    value="#{logWatcherBridge.logWatcherServlet.logLength}" />


                <h:outputLabel value="#{msg.dispatchDirectory}" />
                <h:inputText title="#{msg.dispatchDirectory}"
                    id="dispatchDirectory"
                    value="#{logConfiguration.dispatchDirectory}" />


                <h:outputLabel value="#{msg.logFilePrefix}" />
                <h:inputText title="#{msg.logFilePrefix}" id="prefix"
                    value="#{logConfiguration.prefix}" />

                <h:outputLabel value="#{msg.includeDate}" />
                <h:selectBooleanCheckbox title="#{msg.includeDate}"
                    value="#{logConfiguration.includeDate}"
                    onclick="document.getElementById('j_id_jsp_2033881974_1:timestampFormat').disabled = !document.getElementById('j_id_jsp_2033881974_1:timestampFormat').disabled">
                </h:selectBooleanCheckbox>

                <h:outputLabel value="#{msg.timeFormat}" />
                <h:panelGroup>
                    <h:inputText title="#{msg.timeFormat}"
                        id="timestampFormat" disabled="false"
                        value="#{logConfiguration.dateFormat}" />
                    <p onMouseOver="open_new_window()">?</p>
                </h:panelGroup>

                <h:outputLabel value="#{msg.frequency}" />
                <h:selectOneMenu
                    value="#{logConfiguration.currentFrequency}">
                    <f:selectItems
                        value="#{logConfiguration.frequencies}" />
                </h:selectOneMenu>

                <h:outputLabel value="#{msg.dispatch}" />
                <h:selectOneMenu
                    value="#{logConfiguration.currentDispatch}">
                    <f:selectItems
                        value="#{logConfiguration.dispatches}" />
                </h:selectOneMenu>

                <h:outputLabel value="#{msg.headroom}" />
                <h:panelGroup>
                    <h:selectOneMenu
                        value="#{logConfiguration.currentHeadroomPct}">
                        <f:selectItems
                            value="#{logConfiguration.headroomRange}" />
                    </h:selectOneMenu>%
                </h:panelGroup>

                <h:outputLabel value="#{msg.trackMemory}" />
                <h:selectBooleanCheckbox title="#{msg.trackMemory}"
                    value="#{logConfiguration.trackingMemory}">
                </h:selectBooleanCheckbox>

                <h:outputLabel value="#{msg.aggressivePruning}" />
                <h:selectBooleanCheckbox
                    title="#{msg.aggressivePruning}"
                    value="#{logConfiguration.aggressivePruning}">
                </h:selectBooleanCheckbox>


                <h:outputLabel value="" />
                <h:outputLabel value="" />
                <h:commandButton action="#{logConfiguration.update}"
                    value="#{msg.configButton}" />
                <h:commandButton
                    action="#{logWatcherBridge.logWatcherServlet.whackLog}"
                    value="#{msg.whackLog}" />
            </h:panelGrid>
        </rich:panel>
        <br />
        <br />
        <br />
        <jsp:include page="../footer.jsp" />
    </h:form>
    </body>
</f:view>
</html>
