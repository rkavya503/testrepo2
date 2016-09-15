function _nf(){
}
	
function _frow(ele){
	if(ele.tagName.toUpperCase() == 'BODY') return null;
	if(Element.hasClassName(ele, "rich-table-row")) return ele;
	return _frow(ele.parentNode);
}	

function _d_click(ele){
	if(ele.checked){
              Element.addClassName(ele,'checked');
              var allSel = document.getElementsByClassName('checkforsel'); // [START -JIRA # 8615 & 8616] - when uncheck one A-bank, system should deselect the ‘All’ button
              for(var i=0;i<allSel.length;i++ )
           {
                     if(!allSel[i].checked){
                           document.getElementsByClassName('check-select-all')[0].checked=false;
                           break;
                     }else{
                           document.getElementsByClassName('check-select-all')[0].checked=true;
                     }
           }
       }
       else{
              Element.removeClassName(ele,'checked');
              document.getElementsByClassName('check-select-all')[0].checked=false; // [START -JIRA # 8615 & 8616]- when check all A-bank, select all to be selected
       }
       
    //for nested selection
     var row = _frow(ele);

	if(row){
		var rid = row.getAttribute("id");
		var next=Selector.handlers.nextElementSibling(row);
		while(next && next.getAttribute("id").startsWith(rid+":") ){
			var rowCheckBox = $(next).select("input.checkforsel");
			if(rowCheckBox.length==1){
				rowCheckBox[0].checked=ele.checked;
			}
			if(ele.checked) Element.addClassName(next,'checked'); 
			else Element.removeClassName(next,'checked');
			next=Selector.handlers.nextElementSibling(next);
		}
		
		if(row.parentNode){
		    var eles = $(row.parentNode).select("input.checked");
		 
		    if(!eles.length || eles.length==0){
			    var id = row.parentNode.id; 
			    var temp =id.substring(0,id.lastIndexOf(":")); 
			    temp = temp+ "_all_selected";
			    var all = document.getElementsByName(temp); 
			  
			    for(var i=0;i<all.length;i++ )
			    {
				    all[i].checked=false;
			    };
		    }
		}
	}
}
	
function _findClickHandler(ele){
	if(!ele) return _nf;
	if(ele.onclick) { return ele.onclick;}
	if(ele.parentNode!=null){
		return _findClickHandler(ele.parentNode);
	}
	return _nf;
}

function _findUpClickHandler(ele){
	return _findClickHandler(ele.parentNode);
}

function _getParentRowId(rowId){
	if(rowId){
		var i = rowId.lastIndexOf(":");
		if(i>0)
		  return rowId.substring(0,i);
	}	
}


function toggleTreeNode(rowId,imageId){
	//toggle status of tree Node 
	var hiddenField =$(rowId+":sts");
	if(hiddenField){
		if(hiddenField.value == 'KIDS_LOADED_AND_EXPANDED')
			hiddenField.value ='KIDS_LOADED_AND_COLLAPSED';
		else if(hiddenField.value =='KIDS_LOADED_AND_COLLAPSED')
			hiddenField.value = 'KIDS_LOADED_AND_EXPANDED';
	}
	
	//hidden or visualize children	
	var row = $(rowId);
	if(row){
		var	next= row.nextSibling;
		while(next){
			var id = next.id;
			var descendant =id.length > rowId.length;
			if(descendant){
				if(Element.hasClassName(next,"treetable-rowhidden")){
					//should consult it's parent status to decide whether to expand this node
					var parentRow =_getParentRowId(id);
					var hiddenField =$(parentRow+":sts");
					if(hiddenField){
						if(hiddenField.value == 'KIDS_LOADED_AND_EXPANDED')
							Element.removeClassName(next,"treetable-rowhidden");
						//else if(hiddenField.value =='KIDS_LOADED_AND_COLLAPSED')
					}
				}else{
					Element.addClassName(next,"treetable-rowhidden");
				}		
				next = next.nextSibling;	
			}else
				break;
		} 
	}
	
	//update image	
	var img= $(imageId);
	if(img){
		var src = img.getAttribute("src");
		if(!src) return;
		if(src.indexOf('collapsed')>0){
			src=src.replace(/collapsed/mi,'expanded');
		}else{
			src=src.replace(/expanded/mi,'collapsed');
		}
		img.setAttribute("src", src);
		
		//update alt
		if(img.getAttribute("alt")=="Expand"){
			img.setAttribute("alt", "Collapse");
		}
		else{
			img.setAttribute("alt", "Expand");
		}
		img.focus();
	}
}

if (!window.RichfacesExt) {
	window.RichfacesExt = {
		SELECTION_SINGLE:"single",
		SELECTION_MULTIPLE:"multiple",
		SELECTION_NONE:"none"
	};
}

RichfacesExt.TreeNodeExpandEvent = "richext:treeTable:onExpand";
RichfacesExt.TreeTable = Class.create({
	initialize: function(clientId, selectionMode,submitFunction) {
		this.tableId = clientId;
		this.element = $(clientId);
		this.selectionField = $(this.tableId+"_selected");
		this.element.component = this;
		this.selectionMode = selectionMode;
		
		this["rich:destructor"] = "destroy";
		
		Event.observe(this.element, RichfacesExt.TreeNodeExpandEvent, submitFunction);
	    if(this.selectionMode ==RichfacesExt.SELECTION_SINGLE) {
			Event.observe(this.element,"click",this.tableClick.bind(this));
	    }
	},
	
	tableClick:function(event){
		var event =event || window.event
		var target=event.target || event.srcElement;
		var row=this.getSelectedRow(target);
		
		this.selectedRow= row.getAttribute("rowkey");
		if(this.selectionField){
			this.selectionField.setAttribute("value",this.selectedRow);
		}		
				
		if(this.lastSelectRow)
			Element.toggleClassName(this.lastSelectRow,"row-selected");
		Element.toggleClassName(row,"row-selected");
		this.lastSelectRow = row;
	},
	
	getSelectedRow:function(element){
		if(Element.hasClassName(element,"rich-table-row")){
			return element;							
		}
		return this.getSelectedRow(element.parentNode);
	},
	destroy: function() {
		this.element.component = undefined;
		this.element = undefined;
	},
	
	
	init:function(){
		this.frozenTables();
	},
	

	getHiddenElement:function(element){
		if(!Element.visible(element)){
			return element;
		}
		return this.getHiddenElement(element.parentNode);
	},
	
	hideElement:function(element){
		$(element).style.display="none";
	},
	
	/* 
	 * To add a scrollbar for table content but not to table header,e.g. the table header will always 
	 * be visible, two html tables, one for table header ,and the other for the body,will be created 
	 * and all their columns should be aligned. This method is to do such a job
	 */
	frozenTables:function(){
		if(this.frozen) return;
		if(this.timeout) clearTimeout(this.timeout);
		this.timeout= null;
				
		var tableId = this.tableId;
		var wholeTable =$(tableId);
		var container = $(tableId+"_outer");
		
		var height = Element.getHeight(wholeTable);
		if(height==0){
			this.timeout=setTimeout(this.frozenTables.bind(this),100);
			return;
		}
								
		var bodyTable = $(tableId+"_body");
		var headTable = $(tableId+"_head");
		var scrollDiv = $(tableId+"_scroll_div");
		var fakeheader = $(tableId+"_fake_header");
		var widthForm = $(tableId+"_widths");
		
		var headColumns=headTable.getElementsByTagName('th');
		var bodyColumns = new Array(headColumns.length);
		var fakeColumns = new Array(headColumns.length);
					
		var td = bodyTable.select("td.rich-table-cell");
		var th = bodyTable.getElementsByTagName('th');
		
		//no rows
		if(td.length<headColumns.length) {
			// show no data message
			var headerHeight = Element.getHeight(headColumns[0]);
			scrollDiv.style['display']="block";
			scrollDiv.style['height']=(height-headerHeight);
			scrollDiv.style['overflow-y']='scroll';
			fakeheader.style.display="none";
			
			return;
		}
		
		for(var i =0; i<headColumns.length; i++){
			bodyColumns[i] = td[i];
			fakeColumns[i] = th[i];
		}
		
		this.columnWidths = [];
		var headerHeight = Element.getHeight(headColumns[0]);
		
		scrollDiv.style['display']="block";
		scrollDiv.style['height']=(height-headerHeight);
		scrollDiv.style['overflow-y']='scroll';
		
		var scrollbarWidth= Prototype.Browser.IE? 17:21;
	    for (var i = 0,element; element = bodyColumns[i]; i++){
			this.columnWidths.push(Element.getWidth(element));
		}
			
		this.colCount = this.columnWidths.length;
		this.originalWidths=[];
		
		//reset width
		var width = Element.getWidth(bodyTable);
		
		headTable.style['tableLayout']='fixed';
		bodyTable.style['tableLayout']='fixed';
		headTable.style['width']=(width+scrollbarWidth)+"px";
		bodyTable.style['width']=width+"px";
		
		for(var i = 0;i<fakeColumns.length; i++){
			fakeColumns[i].removeAttribute('width');
			bodyColumns[i].removeAttribute('width');
		}
		
		for(var i = 0;i<bodyColumns.length; i++){
			if(i < this.columnWidths.length){
				if(!this.originalWidths[i]){
					this.originalWidths[i]= bodyColumns[i].getAttribute('width') || bodyColumns[i].style['width'];
				}
			}
		}
		
		var widthStr="";
		for (var i = 0,width; width = this.columnWidths[i]; i++){
			this.originalWidths[i] = bodyColumns[i].getAttribute('width') || bodyColumns[i].style['width'];
			if(i<this.columnWidths.length-1){
				//TODO calcualte paddings and border
				width = Prototype.Browser.IE? width-9: width;
				if(i==0)
					widthStr=width+"";
				else
					widthStr=widthStr.concat(',',width+"");
				headColumns[i].setAttribute("width",width+"px");
				fakeColumns[i].setAttribute("width",width+"px");
				bodyColumns[i].setAttribute("width",width+"px");
			}
		}
				
		
		widthForm.setAttribute("value",widthStr);
		fakeheader.style.display="none";
		
		headColumns[headColumns.length-1].style['border-right']= "none";
		this.forzen = true;
	},
	
	afterNodeExpand:function(){
		var widthForm = $(this.tableId+"_widths");
		var widthStr="";
		for (var i = 0,width; width = this.columnWidths[i]; i++){
			if(i<this.columnWidths.length-1){
				width = Prototype.Browser.IE? width-9: width;
				if(i==0)
					widthStr=width+"";
				else
					widthStr=widthStr.concat(',',width+"");
			}
		}
		widthForm.setAttribute("value",widthStr);
	},
	
	unfrozenTables:function(){
		var tableId = this.tableId;
		var wholeTable =$(tableId);
		var bodyTable = $(tableId+"_body");
		var headTable = $(tableId+"_head");
		var scrollDiv = $(tableId+"_scroll_div");
		var fakeheader = $(tableId+"_fake_header");
		
		headTable.style['tableLayout']='auto';
		bodyTable.style['tableLayout']='auto';
		headTable.style['width']="100%";
		bodyTable.style['width']="100%";
		fakeheader.style.display="";
		
		scrollDiv.style['display']="none";
		scrollDiv.style['height']="";
		
		
		var headColumns=headTable.getElementsByTagName('th');
		var bodyColumns = new Array(headColumns.length);
		var fakeColumns = new Array(headColumns.length);
		
		var td = bodyTable.select("td.rich-table-cell");
		var th = bodyTable.getElementsByTagName('th');
			
		//no rows
		if(td.length<headColumns.length) return;	
		
		for(var i =0; i<headColumns.length; i++){
			bodyColumns[i] = td[i];
			fakeColumns[i] = th[i];
		}
				
		if(td.length==0) return;
		
		for(var i =0; i<headColumns.length; i++){
			if(i<td.length)
				bodyColumns[i] = td[i];
			fakeColumns[i] = th[i];
		}
		
		for (var i = 0;i<this.colCount; i++){
			if(this.originalWidths[i] && cells[i]){
				if(i<td.length)
					bodyColumns[i].setAttribute('width',this.originalWidths[i]);
				
			}
		}
		this.frozen = false;
	}
	,
	selectOrDeselectAll:function(checkAll,event){
		var evt =event|| window.event;
		Event.extend(evt);
		evt.stopPropagation();
      	evt.stopped = true;
		var check=$(checkAll).checked;
		var wholeTable =$(this.tableId);
		var eles =wholeTable.getElementsByClassName('checkforsel');
		for(var i=0;i<eles.length;i++){
			eles[i].checked=check;
			if(check) Element.addClassName(eles[i],'checked'); 
			else Element.removeClassName(eles[i],'checked');
		}
	}
});