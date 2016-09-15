<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<!--

//
// ****************************************************************************
// *	ViewLogsHelper.js
// *	Javascript code that is imported into ViewLogsHelpsers.jsp
// *	NOTE: All the code may not be portable since control names are specific
// *			to the JSP.  Note the JSTL code as well.
// ****************************************************************************
//

 -->
<script type="text/javascript">
    function onCalStartDate(event)
    {
        var strStartDate = document.getElementById('startDate').value;
        cal1.popup(strStartDate, event.screenX, event.screenY);
    };

    function onCalEndDate(event)
    {
        var strEndDate = document.getElementById('endDate').value;
        cal2.popup(strEndDate, event.screenX, event.screenY);
    }

    function onChangeStartTime(event)
    {
        var ctrlHour = document.getElementById('startHour');
        var startHour = ctrlHour.options[ ctrlHour.selectedIndex ].text;

        var ctrlMinute = document.getElementById('startMinute');
        var startMinute = ctrlMinute.options[ ctrlMinute.selectedIndex ].text;

        var ctrlSecond = document.getElementById('startSecond');
        var startSecond = ctrlSecond.options[ ctrlSecond.selectedIndex ].text;

        var startTimeEdit = document.getElementById( 'startTime' );
        startTimeEdit.value = startHour + ':' + startMinute + ':' + startSecond;
    }

    function onChangeEndTime(event)
    {
        var ctrlHour = document.getElementById('endHour');
        var endHour = ctrlHour.options[ ctrlHour.selectedIndex ].text;

        var ctrlMinute = document.getElementById('endMinute');
        var endMinute = ctrlMinute.options[ ctrlMinute.selectedIndex ].text;

        var ctrlSecond = document.getElementById('endSecond');
        var endSecond = ctrlSecond.options[ ctrlSecond.selectedIndex ].text;

        var endTimeEdit = document.getElementById( 'endTime' );
        endTimeEdit.value = endHour + ':' + endMinute + ':' + endSecond;
    }

    function onClickNowEndTime(event)
    {
        var date = new Date();
        date.setTime(document.getElementById('serverTime'));
        var ctrlHour = document.getElementById('endHour');
        ctrlHour.options[ date.getHours() ].selected = true;

        var ctrlMinute = document.getElementById('endMinute');
        ctrlMinute.options[ date.getMinutes() ].selected = true;

        var ctrlSecond = document.getElementById('endSecond');
        ctrlSecond.options[ date.getSeconds() ].selected = true;

        onChangeEndTime();
    }

    function onClickNowStartTime(event)
    {
        var date = new Date();

        var ctrlHour = document.getElementById('startHour');
        ctrlHour.options[ date.getHours() ].selected = true;

        var ctrlMinute = document.getElementById('startMinute');
        ctrlMinute.options[ date.getMinutes() ].selected = true;

        var ctrlSecond = document.getElementById('startSecond');
        ctrlSecond.options[ date.getSeconds() ].selected = true;

        onChangeStartTime();
    }

    function onPreviousRecords()
    {
    	var startRecord = parseInt(document.getElementById ( 'startRecord' ).value);
    	if ( startRecord == 1 )
    	{
    		alert ( 'No previous records' );
    		return;
    	}
    	var numRecords = parseInt(document.getElementById ( 'objectsPerPage' ).value);
    	startRecord -= numRecords;
    	if ( startRecord < 1 )
    		startRecord = 1;
    	document.getElementById ( 'startRecord' ).value = startRecord;
    	var theForm = document.getElementById ( 'submitQuery' );
    	theForm.submit();
    }

    function onNextRecords()
    {
    	var startRecordCtrl = document.getElementById ( 'startRecord' );
    	var startRecord = parseInt( startRecordCtrl.value);
    	var numRecordsCtrl = document.getElementById ( 'objectsPerPage' );
    	var numRecords = parseInt(numRecordsCtrl.value);

    	if ( startRecord < 1 )
    	{
    		startRecord = 1;
    	}

    	startRecord += numRecords;

    	startRecordCtrl.value = startRecord;
    	var theForm = document.getElementById ( 'submitQuery' );
    	theForm.submit();
    }

    function onSortByDateTime()
    {
        onSortBy(0);
    }

    function onSortByLevel()
    {
        onSortBy(1);
    }

    function onSortByUser()
    {
        onSortBy(2);
    }

    function onSortByCategory()
    {
        onSortBy(3);
    }

    function onSortByDescription()
    {
        onSortBy(4);
    }

    function onSortBy(argIndex)
    {
        var sortByCtrl = document.getElementById('sortBy');
        var sortOrderCtrl = document.getElementById('sortOrder');

        if ( sortByCtrl.options [ argIndex ].selected )
        {
            if ( sortOrderCtrl.selectedIndex == 0 )
                sortOrderCtrl.options[1].selected = true;
            else
                sortOrderCtrl.options[0].selected = true;
        }
        else
        {
            sortByCtrl.options [ argIndex ].selected = true;
        }
        var queryForm = document.getElementById ( 'submitQuery' );
        queryForm.submit();
    }

    function onEnsureNumeric(event)
    {
        var keyChar;
        var numCheck;

        if (window.event)
        {
            keyChar = event.keyCode;
        }
        else
        {
            keyChar = event.which;
        }

        keyChar = String.fromCharCode(keyChar);
        numCheck = /\d/;
        return numCheck.test(keyChar);
    }

    var cal1 = new calendar2(document.getElementById('startDate'));
    cal1.year_scroll = true;
    cal1.time_comp = false;
    var cal2 = new calendar2(document.getElementById('endDate'));
    cal2.year_scroll = true;
    cal2.time_comp = false;
//    onInitForm();

    function onInitForm()
    {
        var ctrlStartHour = document.getElementById('startHour');
        ctrlStartHour.options[ Number ( <c:out value="${LogFilterForm.startHour}" /> ) ].selected = true;
        var ctrlStartMinute = document.getElementById('startMinute');
        ctrlStartMinute.options[ Number ( <c:out value="${LogFilterForm.startMinute}" /> ) ].selected = true;
        var ctrlStartSecond = document.getElementById('startSecond');
        ctrlStartSecond.options[ Number ( <c:out value="${LogFilterForm.startSecond}" /> ) ].selected = true;
        onChangeStartTime();
        var startDateEdit = document.getElementById( 'startDate' );
        startDateEdit.value = "<c:out value="${LogFilterForm.startDate}" />";

        var ctrlEndHour = document.getElementById('endHour');
        ctrlEndHour.options[ Number ( <c:out value="${LogFilterForm.endHour}" /> ) ].selected = true;
        var ctrlEndMinute = document.getElementById('endMinute');
        ctrlEndMinute.options[ Number ( <c:out value="${LogFilterForm.endMinute}" /> ) ].selected = true;
        var ctrlEndSecond = document.getElementById('endSecond');
        ctrlEndSecond.options[ Number ( <c:out value="${LogFilterForm.endSecond}" /> ) ].selected = true;
        onChangeEndTime();
        var endDateEdit = document.getElementById( 'endDate' );
        endDateEdit.value = "<c:out value="${LogFilterForm.endDate}" />";

        var sortByCtrl = document.getElementById('sortBy');

        for ( var i=0; i<sortByCtrl.length; i++ )
        {
            if ( sortByCtrl.options [ i ].text == '<c:out value="${LogFilterForm.sortBy}" />' )
            {
                sortByCtrl.options [ i ].selected = true;
                break;
            }
        }

        var logLevelCtrl = document.getElementById('logLevel');
        for ( var i=0; i<logLevelCtrl.length; i++ )
        {
            if ( logLevelCtrl.options [ i ].text == '<c:out value="${LogFilterForm.logLevel}" />' )
            {
                logLevelCtrl.options [ i ].selected = true;
                break;
            }
        }

        var sortOrderCtrl = document.getElementById('sortOrder');
        var j = 0;
        if ( sortOrderCtrl.options [ 0 ].text == '<c:out value="${LogFilterForm.sortOrder}" />' )
        {
            sortOrderCtrl.options [ 0 ].selected = true;
        }
        else
        {
            sortOrderCtrl.options [ 1 ].selected = true;
        }
    }
</script>

