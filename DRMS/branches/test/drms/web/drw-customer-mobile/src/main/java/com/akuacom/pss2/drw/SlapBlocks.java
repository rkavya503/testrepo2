package com.akuacom.pss2.drw;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SlapBlocks implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<String> scecBlocks = new ArrayList<String>();
	private List<String> scenBlocks = new ArrayList<String>();
	private List<String> scnwBlocks = new ArrayList<String>();
	private List<String> scewBlocks = new ArrayList<String>();
	private List<String> scldBlocks = new ArrayList<String>();
	private List<String> schdBlocks = new ArrayList<String>();
	public List<String> getScecBlocks() {
		return scecBlocks;
	}
	public void setScecBlocks(List<String> scecBlocks) {
		this.scecBlocks = scecBlocks;
	}
	public List<String> getScenBlocks() {
		return scenBlocks;
	}
	public void setScenBlocks(List<String> scenBlocks) {
		this.scenBlocks = scenBlocks;
	}
	public List<String> getScnwBlocks() {
		return scnwBlocks;
	}
	public void setScnwBlocks(List<String> scnwBlocks) {
		this.scnwBlocks = scnwBlocks;
	}
	public List<String> getScewBlocks() {
		return scewBlocks;
	}
	public void setScewBlocks(List<String> scewBlocks) {
		this.scewBlocks = scewBlocks;
	}
	public List<String> getScldBlocks() {
		return scldBlocks;
	}
	public void setScldBlocks(List<String> scldBlocks) {
		this.scldBlocks = scldBlocks;
	}
	public List<String> getSchdBlocks() {
		return schdBlocks;
	}
	public void setSchdBlocks(List<String> schdBlocks) {
		this.schdBlocks = schdBlocks;
	}
	

}
