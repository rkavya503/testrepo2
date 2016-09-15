package com.akuacom.pss2.drw.entry;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name = "category")
@XmlType(propOrder = { "name", "programName", "program", "products", "active","eventType" })
public class Category {

	private String name;
	private String program;
	private String programName;
	private ProductsList products;
	private boolean active;
	private String eventType;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProgram() {
		return program;
	}
	public void setProgram(String program) {
		this.program = program;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public ProductsList getProducts() {
		return products;
	}
	public void setProducts(ProductsList products) {
		this.products = products;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}

}
