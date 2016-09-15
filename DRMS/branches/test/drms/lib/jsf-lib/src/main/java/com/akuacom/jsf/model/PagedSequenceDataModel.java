package com.akuacom.jsf.model;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceDataModel;
import org.ajax4jsf.model.SequenceRange;

public class PagedSequenceDataModel extends SequenceDataModel {

	private DataModel wrappedModel;
	
	public PagedSequenceDataModel(DataModel wrapped) {
		super(wrapped);
		this.wrappedModel= wrapped;
	}
	
	public void walk(FacesContext context, DataVisitor visitor, Range range, Object argument) throws IOException {
		final SequenceRange seqRange = (SequenceRange) range;
		int rows = seqRange.getRows();
		int rowCount = wrappedModel.getRowCount();
		//int currentRow = seqRange.getFirstRow();
		int currentRow = 0;
		if(rows > 0){
			rows += currentRow;
			if(rowCount >=0){
				rows = Math.min(rows, rowCount);
			}
		} else if(rowCount >=0 ){
			rows = rowCount;
		} else {
			rows = -1;
		}
		while (rows < 0 || currentRow < rows) {
			wrappedModel.setRowIndex(currentRow);
			if(wrappedModel.isRowAvailable()){
				visitor.process(context, currentRow, argument);
			} else {
				break;
			}
			currentRow++;
		}
	}
}
