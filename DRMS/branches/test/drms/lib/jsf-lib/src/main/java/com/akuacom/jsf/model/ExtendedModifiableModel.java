package com.akuacom.jsf.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.richfaces.model.FilterField;
import org.richfaces.model.ModifiableModel;
import org.richfaces.model.SortField2;

/**
 * Extended this data model to allow the programmer to provide a call back to retrieve 
 * data in order of sortField.
 */

public class ExtendedModifiableModel extends ModifiableModel 
		implements ExtendedModifiable {

	private static final Log log = LogFactory.getLog(ExtendedModifiableModel.class);
	
	public ExtendedModifiableModel(ExtendedDataModel originalModel, String var) {
		super(originalModel, var);
	}
	
	@Override
	public void setRowKey(Object key) {
		originalModel.setRowKey(key);
	}
	
	public void modify(List<FilterField> filterFields, List<SortField2> sortFields,
				Range range){
		if(range instanceof SequenceRange){
			SequenceRange seq = (SequenceRange) range;
			if(seq.getRows()>0){
				rowKeys = new ArrayList<Object>(seq.getRows());
			}
		}else{
			rowKeys = new ArrayList<Object>();
		}
		
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			
			ArrayList<String> sortAttributes = new ArrayList<String>();
			
			if(sortFields instanceof List){
				if(((List) sortFields).get(0) instanceof SortField2){
					SortField2 sf2 = (SortField2)((List) sortFields).get(0);
					
					sf2.getExpression().getExpressionString();
					sortAttributes.add(sf2.getExpression().getExpressionString());

					sf2.getOrdering().name().toString();
					sortAttributes.add(sf2.getOrdering().name().toString());
				}		
			}
			originalModel.walk(context, new DataVisitor() {
				public void process(FacesContext context, Object rowKey,
						Object argument) throws IOException {
					originalModel.setRowKey(rowKey);
					if (originalModel.isRowAvailable()) {
						rowKeys.add(rowKey);
					}
				}
			}, range,
			//sortFields);
			sortAttributes);
			//pass sortFields as an argument so that the data model can be retrieved
			//by the order of sortFields
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		
		filter(filterFields);
		//No need to do sort locally. 
		//sort(sortFields);
	}
	
	@Override
	public void walk(FacesContext context, DataVisitor visitor, Range range,
			Object argument) throws IOException {
		final SequenceRange seqRange = (SequenceRange) range;
		int rows = seqRange.getRows();
		
		//row count is row count in current page, not all rows in all pages
		int rowCount = getRowCount();
		//int currentRow = seqRange.getFirstRow();
		//int currentRow = 0 ;
		if(rows > 0){
			rows =Math.min(rows,rowCount);
		} else {
			rows = rowCount;
		}
		//render phase
		originalModel.walk(context,visitor,range,argument);
	}
	
	public void modify(List<FilterField> filterFields, List<SortField2> sortFields) {
		modify(filterFields,sortFields, new SequenceRange(0,-1));
	}
}
