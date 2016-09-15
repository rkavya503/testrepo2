<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<html>
<f:view>
	<head>
<title>Prototype: Aggregation with Google VizAPI</title>

<script src="http://www.google.com/jsapi"></script>
<!-- <script src="http://api.timepedia.org/gviz/"></script> -->
<script src="<%=request.getContextPath()%>/js/DataTableUtil.js"></script>

<jsp:include page="../../head.jsp" />


<script>
	google.load('visualization', '1', {packages:['orgchart']});
	google.setOnLoadCallback(drawOrgChart);
    var data, viz;
    
	function drawOrgChart() {
		// Create and populate the data table.
		data = new google.visualization.DataTable();
		data.addColumn('string', 'member');
		data.addColumn('string', 'parent');
		data.addColumn('string', 'tooltip');
		data.addRows(8);
		data.setCell(0, 0, 'All American');
		data.setCell(0, 2, 'Aggregate');
		
		data.setCell(1, 0, 'Asphalt East');
        data.setCell(1, 2, 'Aggregate');
        data.setCell(2, 0, 'AA 2');
        data.setCell(2, 2, 'Asphalt Central');
        data.setCell(3, 0, 'Asphalt West');
		data.setCell(1, 1, 'All American');
		data.setCell(2, 1, 'All American');
		data.setCell(3, 1, 'All American');
		
        data.setCell(4, 0, 'AA 1');
        data.setCell(5, 0, 'AA 3');
        data.setCell(6, 0, 'AA 4');
        data.setCell(7, 0, 'AA 5');
        
        data.setCell(4, 1, 'Asphalt East');
        data.setCell(5, 1, 'Asphalt East');
        data.setCell(6, 1, 'Asphalt West');
        data.setCell(7, 1, 'Asphalt West');

        addVisibility(data);
        var view = new google.visualization.DataView(data);
		
		// Create and draw the visualization.
		var viz = new google.visualization.OrgChart(document
				.getElementById('aggDiv'));
		google.visualization.events.addListener(viz, 'select', function() {
			var sel = viz.getSelection();
			if(parentQ(data,sel)) {
			    updateDescendants(viz,data,sel[0].row);				
			}else {
				viz.setSelection();
			}
		});

		viz.draw(data, {
			allowHtml : true
		});
	}
</script>

	</head>

	<body>
		<div id="aggDiv" style="width: 600px; height: 400px"></div>
	</body>
</f:view>
</html>
