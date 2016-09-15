package com.akuacom.pss2.openadr2.report;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.akuacom.ejb.VersionedEntity;

@Entity
@Table(name="itembase")
@NamedQueries({})
@XmlRootElement
public class ItemBase extends VersionedEntity {
	
	private static final long serialVersionUID = 1L;
	private String itemDescription;
	private String itemUnits;
	private String siScaleCode;
	
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	public String getItemUnits() {
		return itemUnits;
	}
	public void setItemUnits(String itemUnits) {
		this.itemUnits = itemUnits;
	}
	public String getSiScaleCode() {
		return siScaleCode;
	}
	public void setSiScaleCode(String siScaleCode) {
		this.siScaleCode = siScaleCode;
	}	
}
