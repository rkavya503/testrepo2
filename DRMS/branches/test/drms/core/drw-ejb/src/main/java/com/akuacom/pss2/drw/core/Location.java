/**
 * 
 */
package com.akuacom.pss2.drw.core;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * the entity Location
 */
@Entity
@Table(name = "location")
@NamedQueries( {
	@NamedQuery(name = "Location.findByType", query = "select e from Location e where e.type = :type"),
	@NamedQuery(name = "Location.findByTypeAndNumber", query = "select e from Location e where e.type = :type and e.number= :number")
	})
public class Location extends AbstractLibraryEntirty {

	private static final long serialVersionUID = -4992490646086325399L;

	private String type;
	private String number;
	private String name;
	private Long parentID;
	private String block;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getParentID() {
		return parentID;
	}
	public void setParentID(Long parentID) {
		this.parentID = parentID;
	}
	public String getBlock() {
		return block;
	}
	public void setBlock(String block) {
		this.block = block;
	}
	
}
