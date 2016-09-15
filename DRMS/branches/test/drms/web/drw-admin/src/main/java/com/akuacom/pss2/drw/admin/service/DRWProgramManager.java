package com.akuacom.pss2.drw.admin.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;

import com.akuacom.pss2.drw.admin.ProgramBackingBean;

public class DRWProgramManager {
	/** singleton DRWProgramManager instance*/
	private static DRWProgramManager instance = new DRWProgramManager();
	
	/**
	 * private constructor 
	 */
	private DRWProgramManager(){
		super();
	}
	/**
	 * Function for get singleton DRWProgramManager instance 
	 * @return
	 */
	public static DRWProgramManager getInstance(){
		return instance;
	}
	
	/**
	 * Function for retrieve programs data from system 
	 * @return
	 */
	public List<ProgramBackingBean> retrieveData(){
		List<ProgramBackingBean> programList = new ArrayList<ProgramBackingBean>();
		// to do 
		// invoke the manage bean API
		return programList;
	}
	
}
