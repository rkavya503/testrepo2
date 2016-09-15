<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<html>
<f:view>
	<head>
<title>Prototype:  Shed graph with Google visualization</title>

<script src="http://www.google.com/jsapi"></script>
<script src="http://api.timepedia.org/gviz/"></script>

<jsp:include page="../../head.jsp" />


<script>
	var c, table, gauge, gaugeData;
	var gaugeOptions = {
		width : 100,
		height : 100,
		redFrom : 90,
		redTo : 100,
		yellowFrom : 75,
		yellowTo : 90,
		minorTicks : 5
	};
	var shedData;
	function drawVisualization() {
		shedData = new google.visualization.DataTable();
		shedData.addColumn('date', 'Date');
		shedData.addColumn('number', 'SCE Shed (KW)');
        shedData.addColumn('number', 'Target-East Shed (KW)');
        shedData.addColumn('number', 'Oakland Muni Shed (KW)');
		shedData.addColumn('string', 'Marker');
		shedData.addRows(7);
		shedData.setValue(0, 0, new Date(2011, 4, 1, 8, 0, 0, 0));
		shedData.setValue(0, 1, 30.000);
        shedData.setValue(0, 2, 40.645);
        shedData.setValue(0, 3, 20.645);
		shedData.setValue(1, 0, new Date(2011, 4, 1, 10, 0, 0, 0));
		shedData.setValue(1, 1, 14.045);
        shedData.setValue(1, 2, 20.374);
        shedData.setValue(1, 3, 25.374);
		shedData.setValue(2, 0, new Date(2011, 4, 1, 12, 0, 0, 0));
		shedData.setValue(2, 1, 55.022);
        shedData.setValue(2, 2, 50.766);
        shedData.setValue(2, 3, 40.766);
		shedData.setValue(3, 0, new Date(2011, 4, 1, 14, 0, 0, 0));
		shedData.setValue(3, 1, 75.284);
        shedData.setValue(3, 2, 64.334);
        shedData.setValue(3, 3, 55.334);
		shedData.setValue(3, 4, 'TGZ turbine kicks in');
		shedData.setValue(4, 0, new Date(2011, 4, 1, 16, 0, 0, 0));
		shedData.setValue(4, 1, 46.476);
        shedData.setValue(4, 2, 56.467);
        shedData.setValue(4, 3, 50.467);
		shedData.setValue(4, 4, '3 5Kw Refers');
        shedData.setValue(5, 0, new Date(2011, 4, 1, 18, 0, 0, 0));
        shedData.setValue(5, 1, 33.322);
        shedData.setValue(5, 2, 39.463);
        shedData.setValue(5, 3, 38.463);
        shedData.setValue(6, 0, new Date(2011, 4, 1, 20, 0, 0, 0));
        shedData.setValue(6, 1, 23.322);
        shedData.setValue(6, 2, 33.463);
        shedData.setValue(6, 3, 34.463);
		c = new chronoscope.ChronoscopeVisualization(document
				.getElementById('shedDiv'));
		google.visualization.events.addListener(c, 'select', function(f) {
			var sel = c.getSelection();
			table.setSelection(sel);
			tweek(shedData.getValue(sel[0].row, sel[0].col));
		});

		c.draw(shedData, {
			legend : "false",
			overview : "false"
		});
		table = new google.visualization.Table(document
				.getElementById('table_div'));
		table.draw(shedData, {
			showRowNumber : true
		});
		google.visualization.events.addListener(table, 'select', function() {
			var sel = table.getSelection();
			c.setSelection([ {
				row : sel[0].row,
				col : 1
			} ]);
		});
	}
	function onChronoscopeLoaded() {
		google.load("visualization", "1", {
			packages : [ "table" ],
			callback : drawVisualization
		});
	}

	google.load('visualization', '1', {
		packages : [ 'gauge' ]
	});
	google.setOnLoadCallback(drawGauge);

	var val = 5;

	function tweek(newVal) {
		gaugeData.setValue(0, 1, newVal);
		gauge.draw(gaugeData, gaugeOptions);
	}

	function drawGauge() {
		gaugeData = new google.visualization.DataTable();
		gaugeData.addColumn('string', 'Label');
		gaugeData.addColumn('number', 'Value');
		gaugeData.addRows(1);
		gaugeData.setValue(0, 0, 'ShedKW');
		gaugeData.setValue(0, 1, 0);

		gauge = new google.visualization.Gauge(document
				.getElementById('guageDiv'));

		gauge.draw(gaugeData, gaugeOptions);
	}
</script>

	</head>

	<body>
       <div id="sheet">
        <table><tr><td><div id="shedDiv" style="width: 600px; height: 400px"></div></td><td>
        <div id="guageDiv" style="x: 600px; width: 100px; height: 100px"></div></td></tr></table>
        <div id="table_div"></div>
        </div>          
	</body>
</f:view>
</html>
