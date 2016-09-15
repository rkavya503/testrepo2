package com.akuacom.pss2.richsite.event.creation;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Wizard implements Serializable{
	
	private static final long serialVersionUID = 8652930772808750770L;
	
	private String activePage;
	
	private List<String> pages;
	
	private Stack<String> comeFrom = new Stack<String>();
	
	public Wizard(String... pages){
		this.pages = Arrays.asList(pages);
	}
	
	public String getActivePage() {
		if(this.activePage==null){
			setActivePage(getPages().get(0));
		}
		return activePage;
	}
	
	public List<String> getPages(){
		return pages;
	}
	
	protected void setActivePage(String page) {
		this.activePage = page;
	}
	
	public void goToPage(String page){
		if(!this.pages.contains(page)) return;
		if(comeFrom.contains(page)){
			String p =null;
			do {
				p = comeFrom.pop();
			}while(!page.equals(p));
		}else{
			if(this.activePage!=null && !comeFrom.contains(this.activePage))
				comeFrom.push(this.activePage);
		}
		this.setActivePage(page);
	}
	
	public void nextPage(){
		String page = this.getPages().get(0);
		if(activePage!=null){
			int idx= getPages().indexOf(activePage);
			if(idx>=0 && idx<getPages().size()-1){
				page = getPages().get(idx+1);
			}
		}
		if(page!=null){
			if(this.activePage!=null)
				comeFrom.push(this.activePage);
			setActivePage(page);
		}
	}
	
	public void backPage(){
		if(comeFrom.isEmpty()) return;
		String page = comeFrom.pop();
		if(page!=null){
			this.setActivePage(page);
		}
	}
	
}
