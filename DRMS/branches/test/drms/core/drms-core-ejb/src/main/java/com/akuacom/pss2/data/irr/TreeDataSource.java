/**
 * 
 */
package com.akuacom.pss2.data.irr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class TreeDataSource implements Serializable {

	private static final long serialVersionUID = -2144726872632509106L;

	private String UUID;
	private String ownerID;

    private String datasource_name;

    private boolean enabled;

    private int ownerType;
    
    List<TreeDataEntry> dataEntries=new ArrayList<TreeDataEntry>();
    
	public String getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}

	public String getName() {
		return datasource_name;
	}

	public void setName(String name) {
		this.datasource_name = name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(int ownerType) {
		this.ownerType = ownerType;
	}

	public List<TreeDataEntry> getDataEntries() {
		return dataEntries;
	}

	public void setDataEntries(List<TreeDataEntry> dataEntries) {
		this.dataEntries = dataEntries;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uuid) {
		UUID = uuid;
	}
	
}
