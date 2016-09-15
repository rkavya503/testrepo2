/***************************************************************************************

DataTableUtil.js   utility methods for google.visualization.DataTable  

www.akuacom.com - Automating Demand Response
 com.akuacom.utils.StackTraceUtil.java - Copyright(c)1994 to 2011 by Akuacom . All rights reserved. 
 Redistribution and use in source and binary forms, with or without modification, is prohibited.

 ***************************************************************************************/

/**
 * @param table
 *            google.visualization.DataTable whose cells are tuple4(member,
 *            parent, tooltip,visible)
 * @param sel
 */
function parentOf(table, sel) {
	return sel[0].parent;
}

/**
 * @param table DataTable to add 4th column 'visiblity'
 */
function addVisibility(table) {
	var rows = table.getNumberOfRows();
	var cols = table.getNumberOfColumns();
	table.addColumn('boolean', 'visibilty');
	for(var i = 0; i < rows;i++) {
		table.setValue(i, cols, true);
	}
}

function dump(obj) {
	var output = '';
	for (property in obj) {
		output += property + ': ' + obj[property] + '; ';
	}
	alert(output);
}

function rootQ(table, sel) {
	var col = sel[0].col == null ? 0 : sel[0].col;
	var row = sel[0].row;
	return table.getValue(row, 1) == null;
}

function orgSel() {
	var sel = viz.getSelection();
	alert(sel == null + " "+ sel);
	   
	try {
	   if(parentQ(data,sel)) {
	      updateDescendants(viz, data,sel[0].row);
	   }
	} catch( e) {
	      alert("ex " + e);
	}
}

function parentQ(table, sel) {

	var col = sel[0].col == null ? 0 : sel[0].col;
	var row = sel[0].row;
	var rows = table.getNumberOfRows();
	var member = table.getValue(row, col);
	for(var i = row+1; i < rows; i++ ){
		if(table.getValue(i,1) == member) {
			return true;
		}
	}
	return false;
}

function inQ(val, arry) {
	return arry.indexOf(val) > -1;
}

function descendantNames(table, sel) {
    var rows = new Array();
    var row = sel[0].row;
    var rowCount = table.getNumberOfRows();
    alert("row " + row);
    var names = new Array();
    names.push(table.getValue(row,0));
    alert("names  " + names);
    var curName;
    for(var i = row+1; i < rowCount; i++ ){
        curName = table.getValue(i,1) ;
        alert("curName "  + curName + " in (" +names + ") -> "  + inQ(curName, names));
        if(inQ( curName , names)) {
            names.push(table.getValue(i,0));
          alert(i + ":names  " + names);
        }
    }
    names.reverse().pop();
    alert("names  " + names);
    return names;
}

function toggleDescendants(table, row) {
    var rows = new Array();
    var rowCount = table.getNumberOfRows();
    var names = new Array();
    names.push(table.getValue(row,0));
    var rows = new Array();
    var curName;
    for(var i = row+1; i < rowCount; i++ ){
        curName = table.getValue(i,1) ;
        if(inQ( curName , names)) {
        	table.setValue(i, 3, !table.getValue(i,3));
            names.push(table.getValue(i,0));
        }
    }
    return rows;
}

function updateDescendants(viz, table, row) {
     var view = new google.visualization.DataView(table);
     var name = table.getValue(row,0);
     toggleDescendants(table, row);
     view.setRows(view.getFilteredRows([{column: 3, value: true}]));
     viz.setSelection();
     viz.draw(view);
 }
