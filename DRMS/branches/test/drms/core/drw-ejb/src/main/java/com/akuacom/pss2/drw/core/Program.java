/**
 * 
 */
package com.akuacom.pss2.drw.core;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * the entity Program
 */
@XmlRootElement
@Entity
@Table(name = "program")
@NamedQueries( {
	@NamedQuery(name = "Program.findByUtilityName", query = "select p from Program p where p.utilityName = :name")
	})
public class Program extends AbstractApplicationEntity {

	private static final long serialVersionUID = 8319021851478020306L;

	private String name;
	private String utilityName;
	private String programClass;
	private String longProgramName;
	
	private String productDisplayName;
	private boolean irProgram;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUtilityName() {
		return utilityName;
	}
	public void setUtilityName(String utilityName) {
		this.utilityName = utilityName;
	}
	public String getProgramClass() {
		return programClass;
	}
	public void setProgramClass(String programClass) {
		this.programClass = programClass;
	}
	public String getLongProgramName() {
		return longProgramName;
	}
	public void setLongProgramName(String longProgramName) {
		this.longProgramName = longProgramName;
	}
	/**
	 * @return the productDisplayName
	 */
	public String getProductDisplayName() {
		return productDisplayName;
	}
	/**
	 * @param productDisplayName the productDisplayName to set
	 */
	public void setProductDisplayName(String productDisplayName) {
		this.productDisplayName = productDisplayName;
	}
	/**
	 * @return the irProgram
	 */
	public boolean isIrProgram() {
		return irProgram;
	}
	/**
	 * @param irProgram the irProgram to set
	 */
	public void setIrProgram(boolean irProgram) {
		this.irProgram = irProgram;
	}
	
	
}
