package com.akuacom.pss2.drw.entry;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "com.akuacom.drweb")
public class CategotyList {

  // XmLElementWrapper generates a wrapper element around XML representation
  @XmlElementWrapper(name = "categorys")
  // XmlElement sets the name of the entities
  @XmlElement(name = "category")
  private ArrayList<Category> categoryList;

	public ArrayList<Category> getCategorysList() {
		return categoryList;
	}
	
	public void setCategoryList(ArrayList<Category> bookList) {
		this.categoryList = bookList;
	}
  
} 