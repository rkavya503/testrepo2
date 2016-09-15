package com.akuacom.pss2.drw.entry;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.akuacom.pss2.drw.Event;
import com.akuacom.pss2.drw.value.EventValue;
import com.akuacom.utils.DateUtil;

@XmlRootElement(namespace = "com.akuacom.drweb")
public class ResultList {
	private String updateTime;
  // XmLElementWrapper generates a wrapper element around XML representation
  @XmlElementWrapper(name = "results")
  // XmlElement sets the name of the entities
  @XmlElement(name = "result")
  private List<HistoryResultEntry> categoryList = new ArrayList<HistoryResultEntry>();

	public List<HistoryResultEntry> getCategorysList() {
		
		return categoryList;
	}
	
	public void setCategoryList(List<HistoryResultEntry> bookList) {
		this.categoryList = bookList;
	}
	private int size;
	private int indexFrom;
	private int indexTo;
	private int offset;

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the indexFrom
	 */
	public int getIndexFrom() {
		return indexFrom;
	}

	/**
	 * @param indexFrom the indexFrom to set
	 */
	public void setIndexFrom(int indexFrom) {
		this.indexFrom = indexFrom;
	}

	/**
	 * @return the indexTo
	 */
	public int getIndexTo() {
		return indexTo;
	}

	/**
	 * @param indexTo the indexTo to set
	 */
	public void setIndexTo(int indexTo) {
		this.indexTo = indexTo;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
	@XmlElement(name="updateTime")
	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		if(updateTime!=null){
			this.updateTime = DateUtil.format(updateTime, "MM/dd/yyyy hh:mm a");
		}else{
			this.updateTime = DateUtil.format(new Date(), "MM/dd/yyyy hh:mm a");
		}
	}
	/**
	 * 
	 */
	public void build() {
		if(categoryList!=null){
			size = categoryList.size();
			if(indexFrom<size){
				if(indexFrom+offset>size){
					indexTo=size;
				}else{
					indexTo=indexFrom+offset;
				}
				categoryList = categoryList.subList(indexFrom, indexTo);
			}else{
				//wrong scope
			}
			
		}
	}
//	
//	
//	public static void main(String args[]){
//		List list = new ArrayList();
//		for(int i=0;i<100;i++){
//			list.add(i);
//		}
//		list = list.subList(0,10);
//		for(int i=0;i<list.size();i++){
//			System.out.println(list.get(i));
//		}
//	}
} 