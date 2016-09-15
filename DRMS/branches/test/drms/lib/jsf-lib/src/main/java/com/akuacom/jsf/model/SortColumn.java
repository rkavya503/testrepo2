package com.akuacom.jsf.model;

import java.io.Serializable;

public class SortColumn implements Serializable{
	
	private static final long serialVersionUID = -6079940148388415593L;
	
	private String name;
	private boolean ascendent;
	
	public SortColumn(String name,boolean ascendent){
		this.name =name;
		this.ascendent= ascendent;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAscendent() {
		return ascendent;
	}

	public void setAscendent(boolean ascendent) {
		this.ascendent = ascendent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (ascendent ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	@Override	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SortColumn other = (SortColumn) obj;
		if (ascendent != other.ascendent)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
