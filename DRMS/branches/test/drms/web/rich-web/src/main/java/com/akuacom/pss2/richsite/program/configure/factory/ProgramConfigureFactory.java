package com.akuacom.pss2.richsite.program.configure.factory;

import com.akuacom.pss2.richsite.program.configure.ProgramConfigureDataModelManager;
import com.akuacom.pss2.richsite.program.configure.ProgramConfigureDataModelManagerImpl;
import com.akuacom.pss2.richsite.program.configure.bid.BidConfigureDataModelManager;
import com.akuacom.pss2.richsite.program.configure.bid.BidConfigureDataModelManagerImpl;
import com.akuacom.pss2.richsite.program.configure.mode.ModeConfigureDataModelManager;
import com.akuacom.pss2.richsite.program.configure.mode.ModeConfigureDataModelManagerImpl;
import com.akuacom.pss2.richsite.program.configure.rtp.RTPConfigureDataModelManager;
import com.akuacom.pss2.richsite.program.configure.rtp.RTPConfigureDataModelManagerImpl;
import com.akuacom.pss2.richsite.program.configure.season.SeasonConfigureDataModelManager;
import com.akuacom.pss2.richsite.program.configure.season.SeasonConfigureDataModelManagerImpl;
import com.akuacom.pss2.richsite.program.configure.signal.SignalConfigureDataModelManager;
import com.akuacom.pss2.richsite.program.configure.signal.SignalConfigureDataModelManagerImpl;

public class ProgramConfigureFactory {

	/** ProgramConfigureFactory instance */
	private static ProgramConfigureFactory instance;
	
	/**
	 * Private constructor to prevent multiple instance to be create
	 */
	private ProgramConfigureFactory(){
		
	}
	
	/**
	 * Singleton function for get self instance
	 * @return
	 */
	public synchronized static ProgramConfigureFactory getInstance(){
		if(instance==null){
			instance = new ProgramConfigureFactory();
		}
		return instance;
	}
	
	/**
	 * Get an instance of  ProgramConfigureDataModelManager
	 * @return
	 */
	public ProgramConfigureDataModelManager getProgramConfigureDataModelManager(){
		return new ProgramConfigureDataModelManagerImpl();
	}
	
	/**
	 * Get an instance of  BidConfigureDataModelManager
	 * @return
	 */
	public BidConfigureDataModelManager getBidConfigureDataModelManager(){
		return new BidConfigureDataModelManagerImpl();
	}
	
	/**
	 * Get an instance of  ModeConfigureDataModelManager
	 * @return
	 */
	public ModeConfigureDataModelManager getModeConfigureDataModelManager(){
		return new ModeConfigureDataModelManagerImpl();
	}
	
	/**
	 * Get an instance of  SignalConfigureDataModelManager
	 * @return
	 */
	public SignalConfigureDataModelManager getSignalConfigureDataModelManager(){
		return new SignalConfigureDataModelManagerImpl();
	}
	/**
	 * Get an instance of  SeasonConfigureDataModelManager
	 * @return
	 */
	public SeasonConfigureDataModelManager getSeasonConfigureDataModelManager(){
		return new SeasonConfigureDataModelManagerImpl();
	}
	
	/**
	 * Get an instance of  RTPConfigureDataModelManager
	 * @return
	 */
	public RTPConfigureDataModelManager getRTPConfigureDataModelManager(){
		return new RTPConfigureDataModelManagerImpl();
	}
}
