package com.akuacom.jsf.model;

import java.util.List;

import org.ajax4jsf.model.Range;
import org.richfaces.model.FilterField;
import org.richfaces.model.Modifiable;
import org.richfaces.model.SortField2;

public interface ExtendedModifiable extends Modifiable{
	
	void modify(List<FilterField> filterFields, List<SortField2> sortFields,Range range);
}
