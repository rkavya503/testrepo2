<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:h="http://java.sun.com/jsf/html" version="2.1">	
	<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
<f:subview id="com_cal"> 
  
	<h:inputHidden id="currentSystemTimeMilliseSecondsComponent" value="#{drFrame.currentSystemTimeMilliseSeconds}"/>
    
	<script type="text/javascript">
		
		
		var curDt = new Date();
		
		var relativedComponent = document.getElementById("currentSystemTimeMilliseSecondsComponent");
		
		if(relativedComponent){
			var currentSystemTimeMilliseSeconds = Number(relativedComponent.getAttribute("value"));
			//alert(currentSystemTimeMilliseSeconds);
			if(currentSystemTimeMilliseSeconds){
				curDt = new Date(currentSystemTimeMilliseSeconds);
				//alert(curDt);
			}else{
				
			}
		}

        function disablementFunction(day){
            var flag = true;
            if (curDt==undefined){
				curDt = day.date.getDate;
            }
			if(curDt.getTime()-day.date.getTime() &lt; 0 ){
				flag = false;
			//	//alert(day.date);
			} 
			//if(curDt.getTime()-day.date.getTime() &lt; 1000*60*60*24 ){
			//	flag = false;
				//alert(day.date);
			//} 
			//if(curDt.getTime()-day.date.getTime() &gt; 1000*60*60*24*365*3 ){
			//	flag = false;
			//	//alert(day.date);
			//}
			
			
			var t = new Date();
			t.setFullYear(1998);
			t.setMonth(6);
			t.setDate(26);
			t.setHours(0);
			t.setMinutes(0);
			t.setSeconds(0);
			//t.setFullYear(t.getFullYear()-3);
		
			
			if(  t.getTime() &gt; day.date.getTime()){
				flag = false;
				//alert(day.date);
			}
			
			return flag;
        }
        //function disabledClassesProv(day){
        //    if (curDt.getTime() - day.date.getTime() &gt;= 1000*60*60*24*365*3) return 'rich-calendar-boundary-dates';
		//	if(curDt.getTime()-day.date.getTime() &lt; 0 )return 'rich-calendar-boundary-dates';
        //}
		function disabledClassesProv(day){
			if(curDt.getTime()-day.date.getTime() &lt; 1000*60*60*24 )return 'rich-calendar-boundary-dates';
			var t = new Date();
			t.setFullYear(1998);
			t.setMonth(6);
			t.setDate(26);
			t.setHours(0);
			t.setMinutes(0);
			t.setSeconds(0);
			//t.setFullYear(t.getFullYear()-3);
			if(  t.getTime() &gt; day.date.getTime())return 'rich-calendar-boundary-dates';
        }
		
		function validateCalendarEnd(){	
			
			v1=$('eventHistoryForm:com_cal:calendarStart').component.getSelectedDate();
			
			v2=$('eventHistoryForm:com_cal:calendarEnd').component.getSelectedDate();
			
			var relativedComponent = document.getElementById('eventHistoryForm:com_cal:calendarEndInputDate');
			if(v2==null||relativedComponent.value==""){
				$('eventHistoryForm:com_cal:calendarEnd').component.selectDate(new Date());
			}
			
		}
		function validateCalendarStart(){
			v1=$('eventHistoryForm:com_cal:calendarStart').component.getSelectedDate();
			v2=$('eventHistoryForm:com_cal:calendarEnd').component.getSelectedDate();
			
			if(v1==null){
				$('eventHistoryForm:com_cal:calendarStart').component.selectDate(v2);
			}
		}
		
		
		
    </script>
	
				<label for="calendarStart" class="obscure">start date</label>
				<rich:calendar 	id="calendarStart"
						label="Start Time"
						isDayEnabled="disablementFunction" 
						dayStyleClass="disabledClassesProv"
						validator="#{drFrame.historyUIBackingBean.validateInputCalendar}"
						popup="true" 
						showApplyButton="false"
						datePattern="#{drFrame.dateFomat}"
						cellWidth="19px"
						cellHeight="15px"
						styleClass="rounded-corners"
						showWeeksBar="false"
						showFooter="true"
						enableManualInput="true"
						buttonIconDisabled="true"
						todayControlMode="hidden"
						ondateselected="validateCalendarEnd()"
						value="#{drFrame.historyUIBackingBean.startDate}">
						<f:converter converterId="drEventStartTimeCalendarConverter"/>
				</rich:calendar>
				- To -
				<label for="calendarEnd" class="obscure">end date</label>
				<rich:calendar 	 id="calendarEnd"	
						label="End Time"
						isDayEnabled="disablementFunction" 
						dayStyleClass="disabledClassesProv"
						validator="#{drFrame.historyUIBackingBean.validateInputCalendar}"
						popup="true" 
						showApplyButton="false"
						datePattern="#{drFrame.dateFomat}"
						cellWidth="19px"
						cellHeight="15px"
						styleClass="rounded-corners"
						showWeeksBar="false"
						showFooter="true"
						enableManualInput="true"
						buttonIconDisabled="true"
						todayControlMode="hidden"
						ondateselected="validateCalendarStart()"
						value="#{drFrame.historyUIBackingBean.endDate}">
						<f:converter converterId="drEventEndTimeCalendarConverter"/>
				</rich:calendar>
				
</f:subview>
</jsp:root> 
 