package com.akuacom.pss2.facdash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.data.DataManager;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.PDataSource;

public class DemandLimitingMockMeter implements
		Serializable {
	private static final long serialVersionUID = -7508131690275978093L;
	private String participantName;
	private Double intervalLoad = 0.0;

	public DemandLimitingMockMeter() {
		super();
		loadParticipant();
	}

	private void loadParticipant() {
		participantName = FDUtils.getParticipantName();
		
	}
	
	
	public String sendIntervalLoad() {
		DataManager dataManager = EJB3Factory
				.getBean(DataManager.class);
        PDataSource source = dataManager.getDataSourceByNameAndOwner("meter1", participantName);
        if (source == null) {
            PDataSource dataSet = new PDataSource();
            dataSet.setName("meter1");
            dataSet.setOwnerID(participantName);
            source = dataManager.createPDataSource(dataSet);
        }
        PDataSet dataSet = dataManager.getDataSetByName("Usage");
        Set<PDataEntry> dataEntrySet = new HashSet<PDataEntry>();

        PDataEntry dataEntry = new PDataEntry();
        dataEntry.setDatasource(source);
        dataEntry.setDataSet(dataSet);
        dataEntry.setTime((new GregorianCalendar()).getTime());
        dataEntry.setValue(this.getIntervalLoad());
        dataEntry.setActual(true);
        dataEntrySet.add(dataEntry);

        dataManager.createDataEntries(dataEntrySet);

		return "success";
	}


	public String cancel() {
		return "cancel";
	}

	public String getParticipantName() {
		return participantName;
	}


	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}


	public Double getIntervalLoad() {
		return intervalLoad;
	}


	public void setIntervalLoad(Double intervalLoad) {
		this.intervalLoad = intervalLoad;
	}


}
