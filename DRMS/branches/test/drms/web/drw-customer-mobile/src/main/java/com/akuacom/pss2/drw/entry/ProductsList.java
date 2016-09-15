package com.akuacom.pss2.drw.entry;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class ProductsList {

  @XmlElement(name = "product")
  private List<String> products;

	public List<String> getProductsList() {
		return products;
	}
	
	public void setProducts(List<String> products) {
		this.products = products;
	}

	
  
} 