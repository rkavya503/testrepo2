package com.akuacom.pss2.query;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;

import com.akuacom.jdbc.BeanFactory;
import com.akuacom.jdbc.CellConverter;
import com.akuacom.jdbc.ColumnAsFeatureFactory;
import com.akuacom.jdbc.ColumnAsObjectFactory;
import com.akuacom.jdbc.EmptyWord;
import com.akuacom.jdbc.HierarchyFactory;
import com.akuacom.jdbc.ListConverter;
import com.akuacom.jdbc.MasterDetailFactory;
import com.akuacom.jdbc.SQLBuilder;
import com.akuacom.jdbc.SQLBuilderException;
import com.akuacom.jdbc.SQLWord;
import com.akuacom.jdbc.SearchConstraint;
import com.akuacom.pss2.Pss2SQLExecutor;
import com.akuacom.pss2.program.ProgramManagerBean;
import com.akuacom.pss2.program.dbp.BidEntry;
import com.akuacom.pss2.program.participant.Constraint;
import com.akuacom.pss2.program.participant.InvalidDate;
import com.akuacom.pss2.program.testProgram.TestProgram;
import com.akuacom.pss2.query.ClientSummary.ProgramParticipation;
import com.akuacom.pss2.report.entities.RTPShedStrategyReportSummary;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class NativeQueryManagerBean implements NativeQueryManager.L,NativeQueryManager.R {

	private static final Logger log = Logger.getLogger(NativeQueryManagerBean.class);

	private static final String SQL_EVT_PARTICIPANT_CANDIDATE;
	private static final String SQL_EVT_PP_CANDIDATE_CONSTRAINT;
	private static final String SQL_PARTICIPANT_SUMMARY;
	private static final String SQL_PARTICIPANT_SUMMARY_COUNT;
	private static final String SQL_PROGRAM_SUMMARY;
	private static final String SQL_CLIENT_SUMMARY;
	private static final String SQL_CLIENT_SUMMARY_COUNT;
	private static final String SQL_UPLOAD_EVT_PARTICIPANT_CANDIDATE;
    private static final String SQL_EVENT_CLIENT_SUMMARY;
    private static final String SQL_CLIENT_CONTACTS;
    private static final String SQL_EVENT_PARTICIPANT_SUMMARY;
    private static final String SQL_ENROLL_EVENT_PARTICIPANT_SUMMARY;
    private static final String SQL_EVT_PART_BID_SUMMARY;
    private static final String SQL_RTP_SHED_STRATEGY_REPORT;
    private static final String SQL_SCE_EVENT_LOCATIONS;
	private static final String SQL_UPLOAD_EVT_PARTICIPANT_OPTOUT;  
	
	static {
		SQL_EVT_PARTICIPANT_CANDIDATE = getSQLFromFile("EventParticipantCandidate.sql");
		SQL_EVT_PP_CANDIDATE_CONSTRAINT =getSQLFromFile("EvtPPCandidateWithConstraint.sql");
		SQL_PARTICIPANT_SUMMARY =getSQLFromFile("ParticipantSummary.sql");
		SQL_PARTICIPANT_SUMMARY_COUNT= getSQLFromFile("ParticipantSummaryCount.sql");
		SQL_PROGRAM_SUMMARY= getSQLFromFile("ProgramSummary.sql");
		SQL_UPLOAD_EVT_PARTICIPANT_CANDIDATE = getSQLFromFile("UploadEventParticipantCandidate.sql");
		SQL_CLIENT_SUMMARY = getSQLFromFile("ClientSummary.sql");
		SQL_CLIENT_SUMMARY_COUNT = getSQLFromFile("ClientSummaryCount.sql");
        SQL_EVENT_CLIENT_SUMMARY = getSQLFromFile("EventClientSummary.sql");
        SQL_CLIENT_CONTACTS = getSQLFromFile("ClientContacts.sql");
        SQL_EVENT_PARTICIPANT_SUMMARY = getSQLFromFile("EventParticipantSummary.sql");
        SQL_ENROLL_EVENT_PARTICIPANT_SUMMARY = getSQLFromFile("EventEnrollParticipantSummary.sql");
        SQL_EVT_PART_BID_SUMMARY= getSQLFromFile("EventBidSummary.sql");
        SQL_RTP_SHED_STRATEGY_REPORT= getSQLFromFile("RTPShedStrategyReport.sql");
        SQL_SCE_EVENT_LOCATIONS = getSQLFromFile("sceLocations.sql");
		SQL_UPLOAD_EVT_PARTICIPANT_OPTOUT = getSQLFromFile("OptOutClientsList.sql");  
	}

	@EJB
	Pss2SQLExecutor.L sqlExecutor;

	
	public List<ProgramSummary> getProgramSummary(SearchConstraint sc) throws  SQLException{
		String sqltemplate =SQL_PROGRAM_SUMMARY;
		List<ProgramSummary> results = null;
		try {
			Map<String,Object> params = new HashMap<String,Object>();
			if(sc!=null){
				SQLWord word = SQLBuilder.getOrderBy(sc);
				if(word!=null) 	params.put("orderBy", word);
			}

			params.put("testProgram", TestProgram.PROGRAM_NAME);
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			results = sqlExecutor.doNativeQuery(sql,
						params,
						ListConverter.make(ColumnAsFeatureFactory.make(ProgramSummary.class)));
			return results;
		} catch (Exception e) {
			log.error(e);
			throw new SQLException(e);
		}
	}

	@Override
	public List<EvtParticipantCandidate> getEvtParticipantCandidate(String programName)
													throws SQLException{
		String sqltemplate =SQL_EVT_PARTICIPANT_CANDIDATE;
		if(sqltemplate==null ||sqltemplate.trim().length()==0 ){
			throw new SQLException("SQL File not loaded");
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programName", programName);
		params.put("orderBy", new SQLWord("order by participantName"));
		try{
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			MasterDetailFactory<EvtParticipantCandidate,EvtClientCandidate> factory
				= new MasterDetailFactory<EvtParticipantCandidate,EvtClientCandidate>(
			          new ColumnAsFeatureFactory<EvtParticipantCandidate>(EvtParticipantCandidate.class,"participantName"),
			          new ColumnAsFeatureFactory<EvtClientCandidate>(EvtClientCandidate.class,"clientName"),true){
				private static final long serialVersionUID = 1L;
				@Override
				protected void buildUp(EvtParticipantCandidate master,
						EvtClientCandidate detail) {
					if(detail!=null){
						detail.setParent(master.getParticipantName());
						detail.setAccount(detail.getClientName());
						master.getClients().add(detail);
					}
				}
			};
			List<EvtParticipantCandidate> results = sqlExecutor.doNativeQuery(sql,params,
						new ListConverter<EvtParticipantCandidate>(factory));
			return results;
		}catch(SQLBuilderException e){
			log.error(e.getMessage(),e);
			throw new SQLException(e);
		}catch(SQLException e){
			log.error(e.getMessage(),e);
			throw e;
		}
	}
	
	public List<EventEnrollingGroup> getEventParticipantsByLocation(String programName,SCELocationType locType)
			throws SQLException{
			String sqltemplate =SQL_EVT_PARTICIPANT_CANDIDATE;
			if(sqltemplate==null ||sqltemplate.trim().length()==0 ){
				throw new SQLException("SQL File not loaded");
			}
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("programName", programName);
			params.put("byLocation",EmptyWord.getInstance()); //just to enable fetching location related columns
			params.put("orderBy", new SQLWord("order by groupId,participantName"));
			
			switch(locType){
			case ABank:
				params.put("byABank", EmptyWord.getInstance());
				break;
			case Slap:
				params.put("bySlap", EmptyWord.getInstance());
				break;
			case Substation:
				params.put("bySubstation", EmptyWord.getInstance());
				break;
			}
			
			try{
				String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
				
				Map<String,BeanFactory<?>> factories = new HashMap<String,BeanFactory<?>>();
				factories.put("/", ColumnAsFeatureFactory.make(EventEnrollingGroup.class,"groupId"));
				factories.put("/participantName",ColumnAsFeatureFactory.make(EvtParticipantCandidate.class, 
						"groupId","participantName"));
				factories.put("/participantName/clientName",ColumnAsFeatureFactory.make(EvtClientCandidate.class,"groupId","participantName", "clientName"));
				HierarchyFactory<EventEnrollingGroup> factory= new HierarchyFactory<EventEnrollingGroup>(factories,true){
					private static final long serialVersionUID = 1L;

					@Override
					public void buildUp(Object parent, Object child, String path) {
						if(path.equals("/participantName")){
							if(child!=null){
								EventEnrollingGroup p =(EventEnrollingGroup)parent;
								EvtParticipantCandidate c = (EvtParticipantCandidate) child;
								p.getEventParticipants().add(c);
							}
						}
						else if(path.equals("/participantName/clientName")){
							if(child!=null && parent!=null){
								EvtParticipantCandidate p =(EvtParticipantCandidate)parent;
								EvtClientCandidate c = (EvtClientCandidate) child;
								c.setParent(p.getParticipantName());
								c.setAccount(c.getClientName());
								p.getClients().add(c);
							}
						}
					}
				};
				
				List<EventEnrollingGroup> results = sqlExecutor.doNativeQuery(sql,params,
						new ListConverter<EventEnrollingGroup>(factory));
				return results;
			}catch(SQLBuilderException e){
				log.error(e.getMessage(),e);
				throw new SQLException(e);
			}catch(SQLException e){
				log.error(e.getMessage(),e);
				throw e;
			}
	}
	
	protected ListConverter<ParticipantSummary> getParticipantSummaryConverter(){
		Map<String,BeanFactory<?>> factories = new HashMap<String,BeanFactory<?>>();
		factories.put("/", ColumnAsFeatureFactory.make(ParticipantSummary.class,"uuid"));
		factories.put("/program",ColumnAsFeatureFactory.make(ParticipantSummary.ProgramParticipation.class, 
						"uuid","programName"));
		factories.put("/event",ColumnAsObjectFactory.make(String.class, "eventName","uuid","eventName"));
		factories.put("/program/aggregation",ColumnAsObjectFactory.make(String.class, 
				"childParticipant","uuid","programName","childParticipant"));
		HierarchyFactory<ParticipantSummary> factory= new HierarchyFactory<ParticipantSummary>(factories,true){
			private static final long serialVersionUID = -582937705196076890L;
			@Override
			public void buildUp(Object parent, Object child, String path) {
				if(path.equals("/event")){
					ParticipantSummary p =(ParticipantSummary)parent;
					String eventName = (String) child;
					p.addActiveEvent(eventName);
				}
				if(path.equals("/program")){
					ParticipantSummary p =(ParticipantSummary)parent;
					ParticipantSummary.ProgramParticipation pp = (ParticipantSummary.ProgramParticipation) child;
					if(pp!=null)
						p.addProgramParticipation(pp);
				}
				else if(path.equals("/program/aggregation")){
					ParticipantSummary.ProgramParticipation pp = (ParticipantSummary.ProgramParticipation) parent;
					String childParticipant =(String)child;
					if(childParticipant!=null){
						pp.addChildParticipant(childParticipant);
					}
				}
			}
		};
		return  ListConverter.make(factory);
	}


	protected Map<String,Object> pscConvertToParams(ParticipantSearchCriteria psc,SearchConstraint sc){
		Map<String,Object> params = new HashMap<String,Object>();
		
		
		if(psc.getNameList()!=null&&psc.getNameList().size()>0){
			List<String> temp = psc.getNameList();
			String subSQL = "";
			for(String name:temp){
				if(name.contains("*")){
					name=name.replaceAll("\\*", "%");	
					String nameSubSQL=" \nAND p.participantName LIKE '"+name+"'";
					subSQL+=nameSubSQL;
				}else{
					String nameSubSQL=" \nAND p.participantName LIKE '"+name+"%'";
					subSQL+=nameSubSQL;
				}
			}
			SQLWord subsqlWord  = new SQLWord(subSQL);
			params.put("multiParticipantNames", subsqlWord);
			
		}else if(psc.getNameLeadingStr()!=null){
			String participantName = psc.getNameLeadingStr();
			if(participantName.contains("*")){
				participantName=participantName.replaceAll("\\*", "%");	
				params.put("participantName", participantName);
			}else{
				params.put("participantName", psc.getNameLeadingStr()+"%");	
			}
		}
		
		if(psc.getAccountList()!=null&&psc.getAccountList().size()>0){
			List<String> temp = psc.getAccountList();
			String subSQL = "";
			for(String account:temp){
				if(account.contains("*")){
					account=account.replaceAll("\\*", "%");	
					String accountSubSQL=" \nAND p.account LIKE '"+account+"'";
					subSQL+=accountSubSQL;
				}else{
					String accountSubSQL=" \nAND p.account LIKE '"+account+"%'";
					subSQL+=accountSubSQL;
				}
			}
			SQLWord subsqlWord  = new SQLWord(subSQL);
			params.put("multiAccounts", subsqlWord);
			
		}else if(psc.getAccountLeadingStr()!=null){
			String account = psc.getAccountLeadingStr();
			if(account.contains("*")){
				account=account.replaceAll("\\*", "%");	
				params.put("account", account);
			}else{
				params.put("account", psc.getAccountLeadingStr()+"%");
			}
		}
			
		
		
		int s1 = psc.getProgramLeadingStr()==null?  0: psc.getProgramLeadingStr().size();
		int s2 = psc.getExactProgramNames()==null?  0: psc.getExactProgramNames().size();
		
		if(s1+s2==1){
			//one program 
			if(s1==1){
				String programName = psc.getProgramLeadingStr().get(0);
				if(programName.contains("*")){
					programName=programName.replaceAll("\\*", "%");	
					params.put("programName", programName);
				}else{
					params.put("programName", psc.getProgramLeadingStr().get(0)+"%");
				}
				
			//one exact program 
			}else{
				params.put("exactProgramName", psc.getExactProgramNames().get(0));
			}
		//multiple programs 
		}else if(s1+s2>1){
			String subSQL = "";
			if(s1>=1){
				List<String> temp = psc.getProgramLeadingStr();
				for(String prog:temp){
					if(prog.contains("*")){
						prog=prog.replaceAll("\\*", "%");
					}
					String programSubSQL=" \nAND EXISTS (SELECT pp.UUID FROM program_participant pp " +
						" WHERE  p.UUID=pp.participant_uuid AND pp.programName LIKE '"+prog+"%')";
					subSQL+=programSubSQL;
				}
			}
			if(s2>=1){
				List<String> temp = psc.getExactProgramNames();
				for(String prog:temp){
					if(prog.contains("*")){
						prog=prog.replaceAll("\\*", "%");
					}
					String programSubSQL=" \nAND EXISTS (SELECT pp.UUID FROM program_participant pp " +
						" WHERE  p.UUID=pp.participant_uuid AND pp.programName= '"+prog+"')";
					subSQL+=programSubSQL;
				}
			}
			SQLWord subsqlWord  = new SQLWord(subSQL);
			params.put("multiprograms", subsqlWord);
		}
		
		if(sc!=null){
			SQLWord word = SQLBuilder.getOrderBy(sc);
			if(word!=null) 	params.put("orderBy", word);
			
			word = SQLBuilder.getMySqlLimit(sc);
			if(word!=null)	params.put("range", word);
		}
		return params;
	}
	
	
	protected Map<String,Object> cscConvertToParams(ClientSearchCriteria csc,SearchConstraint sc){
		Map<String,Object> params = new HashMap<String,Object>();
		
		if(csc.getParticipantNameList()!=null&&csc.getParticipantNameList().size()>0){
			List<String> temp = csc.getParticipantNameList();
			String subSQL = "";
			for(String participantName:temp){
				if(participantName.contains("*")){
					participantName=participantName.replaceAll("\\*", "%");	
					String nameSubSQL=" \nAND p.parent LIKE '"+participantName+"'";
					subSQL+=nameSubSQL;
				}else{
					String nameSubSQL=" \nAND p.parent LIKE '"+participantName+"%'";
					subSQL+=nameSubSQL;
				}
			}
			SQLWord subsqlWord  = new SQLWord(subSQL);
			params.put("multiParticipantNames", subsqlWord);
			
		}else if(csc.getParenLeadingStr()!=null){
			String parent = csc.getParenLeadingStr();
			if(parent.contains("*")){
				parent=parent.replaceAll("\\*", "%");	
				params.put("parent", parent);
			}else{
				params.put("parent", csc.getParenLeadingStr()+"%");
			}
		}
		
		
		if(csc.getClientNameList()!=null&&csc.getClientNameList().size()>0){
			List<String> temp = csc.getClientNameList();
			String subSQL = "";
			for(String clientName:temp){
				if(clientName.contains("*")){
					clientName=clientName.replaceAll("\\*", "%");	
					String nameSubSQL=" \nAND p.participantName LIKE '"+clientName+"'";
					subSQL+=nameSubSQL;
				}else{
					String nameSubSQL=" \nAND p.participantName LIKE '"+clientName+"%'";
					subSQL+=nameSubSQL;
				}
			}
			SQLWord subsqlWord  = new SQLWord(subSQL);
			params.put("multiClientNames", subsqlWord);
			
		}else if(csc.getClientNameLeadingStr()!=null){
			String clientName = csc.getClientNameLeadingStr();
			if(clientName.contains("*")){
				clientName=clientName.replaceAll("\\*", "%");	
				params.put("clientName", clientName);
			}else{
				params.put("clientName", csc.getClientNameLeadingStr()+"%");
			}
		}
		
		if(csc.getCommsStatus()!=null){
			params.put("commsStatus", csc.getCommsStatus());
		}
		if(csc.getClientType()!=null){
			params.put("type", csc.getClientType());
		}
		
		if(csc.getEventStatus()!=null)
			params.put("eventStatus", csc.getEventStatus().name());
		
		int s1 = csc.getProgramLeadingStr()==null?0:csc.getProgramLeadingStr().size();
		int s2 = csc.getExactProgramName()==null? 0: csc.getExactProgramName().size();
		
		if(s1+s2==1){
			//one program 
			if(s1==1){
				String programName = csc.getProgramLeadingStr().get(0);
				if(programName.contains("*")){
					programName=programName.replaceAll("\\*", "%");	
					params.put("programName", programName);
				}else{
					params.put("programName", csc.getProgramLeadingStr().get(0)+"%");
				}
			//one exact program 
			}else{
				params.put("exactProgramName", csc.getExactProgramName().get(0));
			}
		//multiple programs 
		}else if(s1+s2>1){
			String subSQL = "";
			if(s1>=1){
				List<String> temp = csc.getProgramLeadingStr();
				for(String prog:temp){
					if(prog.contains("*")){
						prog=prog.replaceAll("\\*", "%");
					}
					String programSubSQL=" \nAND EXISTS (SELECT pp.UUID FROM program_participant pp " +
						" WHERE  p.UUID=pp.participant_uuid AND pp.programName LIKE '"+prog+"%')";
					subSQL+=programSubSQL;
				}
			}
			if(s2>=1){
				List<String> temp = csc.getExactProgramName();
				for(String prog:temp){
					if(prog.contains("*")){
						prog=prog.replaceAll("\\*", "%");
					}
					String programSubSQL=" \nAND EXISTS (SELECT pp.UUID FROM program_participant pp " +
						" WHERE  p.UUID=pp.participant_uuid AND pp.programName= '"+prog+"')";
					subSQL+=programSubSQL;
				}
			}
			SQLWord subsqlWord  = new SQLWord(subSQL);
			params.put("multiprograms", subsqlWord);
		}
		
		if(csc.isFetchContacts()){
			params.put("withcontacts",  EmptyWord.getInstance());
		}
		
		if(sc!=null){
			SQLWord word = SQLBuilder.getOrderBy(sc);
			if(word!=null) 	params.put("orderBy", word);
			
			word = SQLBuilder.getMySqlLimit(sc);
			if(word!=null)	params.put("range", word);
		}
		return params;
	}
	
	@Override
	public List<ParticipantSummary> getParticipantSummary(ParticipantSearchCriteria psc, SearchConstraint sc)
			throws SQLException {
		String sqltemplate =SQL_PARTICIPANT_SUMMARY;
		Map<String,Object> params = pscConvertToParams(psc, sc);
		
		String sql;
		try {
			sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
		} catch (SQLBuilderException e) {
			throw new SQLException(e);
		}
		List<ParticipantSummary> result = sqlExecutor.doNativeQuery(sql, params, getParticipantSummaryConverter());
		return result;
	}
	
	@Override
	public int getParticipantSummaryCount(ParticipantSearchCriteria psc)
		throws SQLException {
		String sqltemplate =SQL_PARTICIPANT_SUMMARY_COUNT;
		Map<String,Object> params = pscConvertToParams(psc, null);
		
		String sql;
		try {
			sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
		} catch (SQLBuilderException e) {
			throw new SQLException(e);
		}
		int count = sqlExecutor.doNativeQuery(sql,params, CellConverter.make(Integer.class));
		return count;
	}
	
	@Override
	public List<ClientSummary> getClientSummary(ClientSearchCriteria csc,
												SearchConstraint sc) throws SQLException{
		String sqltemplate =SQL_CLIENT_SUMMARY;
		Map<String,Object> params = cscConvertToParams(csc,sc);
		try{
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			List<ClientSummary> results = sqlExecutor.doNativeQuery(sql,params,
					getClientSummaryConverter(csc.isFetchContacts()));
			return results;
		}catch(Exception e){
			log.error(e);
			throw new SQLException(e);
		}
	}
	
	@Override
	public int getClientSummaryCount(ClientSearchCriteria csc) throws SQLException {
		String sqltemplate =SQL_CLIENT_SUMMARY_COUNT;
		Map<String,Object> params = cscConvertToParams(csc,null);
		String sql;
		try {
			sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
		} catch (SQLBuilderException e) {
			throw new SQLException(e);
		}
		int count = sqlExecutor.doNativeQuery(sql, params,CellConverter.make(Integer.class));
		return count;
	}
	
	protected ListConverter<ClientSummary> getClientSummaryConverter(boolean withContacts){
		Map<String,BeanFactory<?>> factories = new HashMap<String,BeanFactory<?>>();
		factories.put("/",
					ColumnAsFeatureFactory.make(ClientSummary.class,"uuid"));
		factories.put("/program",
					ColumnAsFeatureFactory.make(ClientSummary.ProgramParticipation.class,"uuid","programName"));
		factories.put("/program/event",
					ColumnAsFeatureFactory.make(ClientSummary.Evt.class, "uuid","programName","eventName"));
		factories.put("/program/event/signal",
					ColumnAsFeatureFactory.make(ClientSummary.EvtSignal.class, "uuid","programName","eventName","signalName"));
		factories.put("/manualSignal",
				ColumnAsFeatureFactory.make(ClientSummary.ManualSignal.class, "uuid","manualSignalName"));

		if(withContacts)
			factories.put("/email",
				ColumnAsObjectFactory.make(String.class, "address","uuid","address"));

		HierarchyFactory<ClientSummary> factory= new HierarchyFactory<ClientSummary>(factories,true){
			private static final long serialVersionUID = -582937705196076890L;
			@Override
			public void buildUp(Object parent, Object child, String path) {
				if(path.equals("/program")){
					ClientSummary c =(ClientSummary)parent;
					ClientSummary.ProgramParticipation cp = (ProgramParticipation) child;
					if(cp!=null){
						if(!c.getPrograms().contains(cp)){
							c.getPrograms().add(cp);
						}
					}
				}
				else if(path.equals("/program/event")){
					ClientSummary.ProgramParticipation cp  =(ProgramParticipation)parent;
					ClientSummary.Evt evt = (ClientSummary.Evt) child;
					if(!cp.getEvents().contains(evt)){
						cp.getEvents().add(evt);
					}
				}
				else if(path.equals("/program/event/signal")){
					ClientSummary.Evt evt  =(ClientSummary.Evt)parent;
					ClientSummary.EvtSignal s = (ClientSummary.EvtSignal) child;
					if(!evt.getEvtSignals().contains(s)){
						evt.getEvtSignals().add(s);
					}
				}
				else if(path.equals("/manualSignal")){
					ClientSummary c =(ClientSummary)parent;
					ClientSummary.ManualSignal ms = (ClientSummary.ManualSignal) child;
					if(!c.getManualSignals().contains(ms)){
						c.getManualSignals().add(ms);
					}
				}else if(path.equals("/email")){
					ClientSummary c =(ClientSummary)parent;
					String add = (String) child;
					if(add!=null && !c.getEmailAddress().contains(add)){
						c.getEmailAddress().add(add);
					}
				}
			}
		};
		return  ListConverter.make(factory);
	}

	@Override
	public List<EvtPPWithConstraint> getEvtPPCandidateWithConstraint(String programName)
												throws SQLException {
		String sqltemplate =SQL_EVT_PP_CANDIDATE_CONSTRAINT;
		if(sqltemplate==null ||sqltemplate.trim().length()==0 ){
			throw new SQLException("SQL File not loaded");
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programName", programName);
		try{
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);

			Map<String,BeanFactory<?>> factories = new HashMap<String,BeanFactory<?>>();
			factories.put("/",new ColumnAsFeatureFactory<EvtPPWithConstraint>(EvtPPWithConstraint.class,"participantName"));
			factories.put("/client",new ColumnAsFeatureFactory<EvtClientCandidate>(EvtClientCandidate.class,"clientName"));
			Map<String,String> mapping = new HashMap<String,String>();
			mapping.put("constraintUUID", "UUID");
			factories.put("/constraint",new ColumnAsFeatureFactory<Constraint>(Constraint.class,mapping,"constraintUUID"));

			mapping = new HashMap<String,String>();
			mapping.put("dateUUID", "UUID");
			factories.put("/constraint/date",new ColumnAsFeatureFactory<InvalidDate>(InvalidDate.class,mapping,"dateUUID"));

			HierarchyFactory<EvtPPWithConstraint> factory= new HierarchyFactory<EvtPPWithConstraint>(factories,true){
				private static final long serialVersionUID = 2468387495125385719L;
				@Override
				public void buildUp(Object parent, Object child, String path) {
					if(path.equals("/client")){
						EvtPPWithConstraint p = (EvtPPWithConstraint) parent;
						EvtClientCandidate  c =  (EvtClientCandidate) child;
						if(c!=null){
							p.getClients().add(c);
						}
					}else if(path.equals("/constraint")){
						EvtPPWithConstraint p = (EvtPPWithConstraint) parent;
						Constraint constraint = (Constraint) child;
						if(constraint!=null){
							p.setConstraint(constraint);
						}
					}else if(path.equals("/constraint/date")){
						Constraint constraint = (Constraint) parent;
						InvalidDate date = (InvalidDate) child;
						if(constraint.getInvalidDates()==null && date!=null)
							constraint.setInvalidDates(new HashSet<InvalidDate>());
						constraint.getInvalidDates().add(date);
					}
				}
			};
			List<EvtPPWithConstraint> results = sqlExecutor.doNativeQuery(sql,params,
						new ListConverter<EvtPPWithConstraint>(factory));
			return results;
		}catch(SQLBuilderException e){
			log.error(e.getMessage(),e);
			throw new SQLException(e);
		}catch(SQLException e){
			log.error(e.getMessage(),e);
			throw e;
		}
	}

	@Override
	public int validateAccount(String programName, String account) throws SQLException {
		String sqltemplate=" SELECT COUNT(*)" +
				" FROM participant p, program_participant pp " +
				" WHERE p.account=${account} AND p.client=b'0' AND p.uuid=pp.participant_uuid " +
				" AND pp.programName=${programName} " +
				" AND pp.optStatus=b'0' AND pp.state=b'1' ";

		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programName", programName);
		params.put("account", account);

		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);

			Integer result = sqlExecutor.doNativeQuery(sql, params, new CellConverter<Integer>(Integer.class));
			return result;
		} catch (SQLException e) {
			log.error(e.getMessage(),e);
			throw e;
		} catch (SQLBuilderException e) {
			log.error(e.getMessage(),e);
			throw new SQLException(e);
		}
	}

	@Override
	public List<EvtParticipantCandidate> getUploadEvtParticipantCandidate(String programName, List<String> accounts)
													throws SQLException{
		String sqltemplate =SQL_UPLOAD_EVT_PARTICIPANT_CANDIDATE;
		if(sqltemplate==null ||sqltemplate.trim().length()==0 ){
			throw new SQLException("SQL File not loaded");
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programName", programName);
		if (accounts==null || accounts.size()==0)
			params.put("accounts", null);
		else
			params.put("accounts", accounts);

		try{
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			MasterDetailFactory<EvtParticipantCandidate,EvtClientCandidate> factory
				= new MasterDetailFactory<EvtParticipantCandidate,EvtClientCandidate>(
			          new ColumnAsFeatureFactory<EvtParticipantCandidate>(EvtParticipantCandidate.class,"participantName"),
			          new ColumnAsFeatureFactory<EvtClientCandidate>(EvtClientCandidate.class,"clientName"),true){
				private static final long serialVersionUID = 1L;
				@Override
				protected void buildUp(EvtParticipantCandidate master,
						EvtClientCandidate detail) {
					if(detail!=null){
						detail.setParent(master.getParticipantName());
						detail.setAccount(detail.getClientName());
						master.getClients().add(detail);
					}
				}
			};
			List<EvtParticipantCandidate> results = sqlExecutor.doNativeQuery(sql,params,
						new ListConverter<EvtParticipantCandidate>(factory));
			return results;
		}catch(SQLBuilderException e){
			log.error(e.getMessage(),e);
			throw new SQLException(e);
		}catch(SQLException e){
			log.error(e.getMessage(),e);
			throw e;
		}
	}

	
	@Override
	public List<OptedOutClientList> getOptOutClients(String programName)
			throws SQLException {
		String sqltemplate =SQL_UPLOAD_EVT_PARTICIPANT_OPTOUT;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programName", programName);

		try{
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			
			List<OptedOutClientList> results = sqlExecutor.doNativeQuery(sql,
						params,
						ListConverter.make(ColumnAsFeatureFactory.make(OptedOutClientList.class)));
						
			//ListConverter.make(ColumnAsFeatureFactory.make(PartContact.class,"name","address")));
			return results;
		}catch(Exception e){
			log.error(e);
			throw new SQLException(e);
		}
	}
	
    @Override
    public List<EventClientSummary> getEventClientSummary(String eventName) throws SQLException {
        String sqlTemplate = SQL_EVENT_CLIENT_SUMMARY;
        if (sqlTemplate == null || sqlTemplate.trim().length() == 0) {
            throw new SQLException("SQL File not loaded");
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("eventName", eventName);

        try{
      			String sql = SQLBuilder.buildNamedParameterSQL(sqlTemplate, params);
      			List<EventClientSummary> results = sqlExecutor.doNativeQuery(sql, params,
      						getEventClientSummaryConverter());
      			return results;
      		}catch(Exception e){
      			log.error(e);
      			throw new SQLException(e);
      		}
    }

    private ListConverter<EventClientSummary> getEventClientSummaryConverter() {
        Map<String,BeanFactory<?>> factories = new HashMap<String,BeanFactory<?>>();
        factories.put("/", ColumnAsFeatureFactory.make(EventClientSummary.class, "uuid"));
        factories.put("/signal", ColumnAsFeatureFactory.make(EventClientSummary.EventClientSignal.class,
                "uuid", "signalName", "signalStartTime"));
        factories.put("/manualSignal", ColumnAsFeatureFactory.make(EventClientSummary.ManualSignal.class,
                "uuid","manualSignalName"));

        HierarchyFactory<EventClientSummary> factory= new HierarchyFactory<EventClientSummary>(factories,true){
            private static final long serialVersionUID = -6172046070722198896L;

            @Override
            public void buildUp(Object parent, Object child, String path) {
                if(path.equals("/signal")) {
                    EventClientSummary summary = (EventClientSummary) parent;
                    EventClientSummary.EventClientSignal s = (EventClientSummary.EventClientSignal) child;
                    if (!summary.getSignals().contains(s)) {
                        summary.getSignals().add(s);
                    }
                } else if(path.equals("/manualSignal")) {
                    EventClientSummary c = (EventClientSummary) parent;
                    EventClientSummary.ManualSignal ms = (EventClientSummary.ManualSignal) child;
                    if (!c.getManualSignals().contains(ms)) {
                        c.getManualSignals().add(ms);
                    }
                }
            }
        };
        return  ListConverter.make(factory);
    }


    private static String getSQLFromFile(String sqlFileName){
		String sql = "";
		InputStream is = null;
		try{  
			is = ProgramManagerBean.class.getResourceAsStream("/com/akuacom/pss2/query/" + sqlFileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String s = "";
			while ((s = br.readLine()) != null) {
				sql = sql + s + " \n ";
			}
		}
		catch (Exception e) {
			log.error("Unable to load SQL file. " +sqlFileName);
			log.debug(e.getStackTrace());
		}
		finally{
			if(is != null){ try {is.close();}catch(Exception e){};}
		}
		return sql;
	}
    
	@Override
	public List<PartContact> getClientContacts(ClientSearchCriteria csc,SearchConstraint sc)
			throws SQLException {
		String sqltemplate =SQL_CLIENT_CONTACTS;
		Map<String,Object> params = this.cscConvertToParams(csc, sc);
		try{
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			List<PartContact> results = sqlExecutor.doNativeQuery(sql,
						params,
						ListConverter.make(ColumnAsFeatureFactory.make(PartContact.class,"name","address")));
			return results;
		}catch(Exception e){
			log.error(e);
			throw new SQLException(e);
		}
	}
	
	@Override
	public List<EventParticipantSummary> getEnrollParticipantSummary(String programName,
			String eventName)  throws SQLException {
		String sqltemplate =SQL_ENROLL_EVENT_PARTICIPANT_SUMMARY;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programName", programName);
		params.put("eventName", eventName);
		
		try{
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			List<EventParticipantSummary> results = sqlExecutor.doNativeQuery(sql,
						params,
						ListConverter.make(ColumnAsFeatureFactory.make(EventParticipantSummary.class,"participantName")));
			return results;
		}catch(Exception e){
			log.error(e);
			throw new SQLException(e);
		}
	}
	
	@Override
	public List<EventParticipantSummary> getEventParticipantSummary(
			String eventName)  throws SQLException {
		String sqltemplate =SQL_EVENT_PARTICIPANT_SUMMARY;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("eventName", eventName);
		
		try{
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			List<EventParticipantSummary> results = sqlExecutor.doNativeQuery(sql,
						params,
						ListConverter.make(ColumnAsFeatureFactory.make(EventParticipantSummary.class,"participantName")));
			return results;
		}catch(Exception e){
			log.error(e);
			throw new SQLException(e);
		}
	}

	@Override
	public List<EventBidSummary> getEventBidSummary(String eventName) throws SQLException {
		String sqltemplate =SQL_EVT_PART_BID_SUMMARY;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("eventName", eventName);
		try{
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			MasterDetailFactory<EventBidSummary,BidEntry> factory= 
				new MasterDetailFactory<EventBidSummary,BidEntry>(
			          ColumnAsFeatureFactory.make(EventBidSummary.class,"participantName"),
			          ColumnAsFeatureFactory.make(BidEntry.class,"blockStart"),true){
				private static final long serialVersionUID = 1L;
				@Override
				protected void buildUp(EventBidSummary master,
						BidEntry detail) {
					if(detail!=null){
						if(!master.getBids().contains(detail))
							master.getBids().add(detail);
					}
				}
			};
			List<EventBidSummary> results = sqlExecutor.doNativeQuery(sql,params,ListConverter.make(factory));
			return results;
		}catch(Exception e){
			log.error(e);
			throw new SQLException(e);
		}
	}

	@Override
	public List<RTPShedStrategyReportSummary> getRTPShedStrategyReport(String programName) throws SQLException{
		String sqltemplate =SQL_RTP_SHED_STRATEGY_REPORT;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programName", programName);
		try{
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			List<RTPShedStrategyReportSummary> results = sqlExecutor.doNativeQuery(sql, params,
						ListConverter.make(ColumnAsFeatureFactory.make(RTPShedStrategyReportSummary.class)));
			return results;
		}catch(Exception e){
			log.error(e);
			throw new SQLException(e);
		}
	}

	@Override
	public List<Location> getEventLocations(String locType) throws SQLException {
		String sqltemplate =SQL_SCE_EVENT_LOCATIONS;
		Map<String,Object> params = new HashMap<String,Object>();
		if(locType!=null){
			params.put("locType", locType);
		}
		try{
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			List<Location> results = sqlExecutor.doNativeQuery(sql, params,
						ListConverter.make(ColumnAsFeatureFactory.make(Location.class)));
			return results;
		}catch(Exception e){
			log.error(e);
			throw new SQLException(e);
		}
	}
}
