/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicerBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.participant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.event.ListSelectionEvent;

import org.apache.log4j.Logger;

import com.akuacom.jdbc.ColumnAsFeatureFactory;
import com.akuacom.jdbc.ListConverter;
import com.akuacom.jdbc.SQLBuilder;
import com.akuacom.pss2.Pss2SQLExecutor;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.util.LogUtils;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */
@Stateless
public class ProgramParticipantEAOBean extends ProgramUserBaseEAOBean implements
		ProgramParticipantEAO.R, ProgramParticipantEAO.L {
	/** The Constant log. */
	private static final Logger log = Logger
			.getLogger(ProgramParticipantEAOBean.class);
	
	@EJB
	Pss2SQLExecutor.L sqlExecutor;

	public ProgramParticipantEAOBean() {
		super();
	}

    @Override
    public void addChild(ProgramParticipant parent, ProgramParticipant child) {
        checkProgram(parent, child);
        checkRecursiveAdd(parent, child);

        EntityManager entityManager = getEm();
        // update child's parent and ancestry fields
        String oldAncestry = child.getAncestry();
        String parentAncestry = parent.getAncestry();
        if (parentAncestry == null) {
            parentAncestry = "";
        }
        child.setAncestry(parentAncestry + parent.getUUID());
        child.setParent(parent);
        entityManager.merge(child);

        int start = 0;
        if (oldAncestry != null) {
            start = oldAncestry.length();
        }
        Query query = entityManager.createQuery("update ProgramParticipant pp " +
                "set pp.ancestry = concat(:ancestry, substring(pp.ancestry, :start + 1, length(pp.ancestry))) " +
                "where locate(:uuid, pp.ancestry) > 0");
        query.setParameter("ancestry", child.getAncestry());
        query.setParameter("start", start);
        query.setParameter("uuid", child.getUUID());
        query.executeUpdate();
    }

    private void checkRecursiveAdd(ProgramParticipant parent, ProgramParticipant child) {
        String ancestry = parent.getAncestry();
        if (ancestry != null && ancestry.contains(child.getUUID())) {
            throw new EJBException("can't added " + child.getUUID() + " to its child " + parent.getUUID());
        }
    }

    private void checkProgram(ProgramParticipant parent, ProgramParticipant child) {
        Program parentProgram = parent.getProgram();
        String parentProgramProgramName = parentProgram.getProgramName();

        Program childProgram = child.getProgram();
        String childProgramProgramName = childProgram.getProgramName();

        if (!parentProgramProgramName.equals(childProgramProgramName)) {
            throw new EJBException("can't operate aggregation across different program. " +
                    "parent program name: " + parentProgramProgramName +
                    ", child program name: " + childProgramProgramName);
        }
    }

    @Override
    public Set<ProgramParticipant> getFlatDescendants(ProgramParticipant parent) {
        List<ProgramParticipant> list = findAllAggregationDescendants(parent.getProgramName(),
                parent.getAncestry() == null ? parent.getUUID() : parent.getAncestry() + parent.getUUID());
        HashSet<ProgramParticipant> set = new HashSet<ProgramParticipant>();
        for (ProgramParticipant pp : list) {
            set.add(pp);
        }
        return set;
    }

    @SuppressWarnings({"unchecked"})
    public List<ProgramParticipant> findAllAggregationDescendants(java.lang.String programName, Object ancestor) {
        Query q = em.createNamedQuery( "ProgramParticipant.findAllAggregationDescendants.list" );
        q.setParameter("programName", programName);
        q.setParameter("ancestor", ancestor);
        return q.getResultList();
    }

    @Override
    public void removeChildren(ProgramParticipant parent, Set<ProgramParticipant> children) {
        String programName = parent.getProgramName();
        String parentAncestry = parent.getAncestry() + parent.getUUID();
        for (ProgramParticipant child : children) {
            if (!programName.equals(child.getProgramName())) {
                throw new EJBException("child has different program name than parent");
                // todo add detail info
            }
            if (!isAncestor(parent, child)) {
//            if (!parentAncestry.equals(child.getAncestry())) {
                throw new EJBException("child is not direct descent of parent");
            }
        }

        for (ProgramParticipant child : children) {
            removeParent(child);
        }
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public List<String> findClientProgram(String parentPartName){
        Query q = em.createNamedQuery( "ProgramParticipant.findClientPrograms" );
        q.setParameter("participantName", parentPartName);
        return q.getResultList();
    }

    @Override
    public void removeParent(ProgramParticipant child) {
        EntityManager entityManager = getEm();
        String oldAncestry = child.getAncestry();
        child.setParent(null);
        child.setAncestry(null);
        entityManager.merge(child);

        int start = 0;
        if (oldAncestry != null) {
            start = oldAncestry.length();
        }
        Query query = entityManager.createQuery("update ProgramParticipant pp " +
                "set pp.ancestry = concat(:ancestry, substring(pp.ancestry, :start + 1, length(pp.ancestry))) " +
                "where locate(:uuid, pp.ancestry) > 0");
        query.setParameter("ancestry", oldAncestry);
        query.setParameter("start", start);
        query.setParameter("uuid", child.getUUID());
        query.executeUpdate();
    }

    @Override
    public Set<ProgramParticipant> getDescendants(ProgramParticipant parent) {
        List<ProgramParticipant> descendants = findAllAggregationDescendants(parent.getProgramName(),
                parent.getAncestry() == null ? parent.getUUID() : parent.getAncestry() + parent.getUUID());
        HashSet<ProgramParticipant> set = new HashSet<ProgramParticipant>();
        for (ProgramParticipant child : descendants) {
            if (child.getAncestry().endsWith(parent.getUUID())) {
                set.add(child);
            }
        }
        return set;
    }

    @Override
    public Set<ProgramParticipant> getDescendantsForSpecifiedProgram(ProgramParticipant parent,String programName) {
        List<ProgramParticipant> descendants = findAllAggregationDescendants(programName,
                parent.getAncestry() == null ? parent.getUUID() : parent.getAncestry() + parent.getUUID());
        HashSet<ProgramParticipant> set = new HashSet<ProgramParticipant>();
        for (ProgramParticipant child : descendants) {
            if (child.getAncestry().endsWith(parent.getUUID())) {
                set.add(child);
            }
        }
        return set;
    }

    @Override
    public ProgramParticipant getAncestor(ProgramParticipant child) {
        String ancestry = child.getAncestry();
        if (ancestry == null) {
            return null;
        } else {
            String uuid = ancestry.substring(ancestry.length() - 32);
            return getEm().find(ProgramParticipant.class, uuid);
        }
    }

    @Override
    public ProgramParticipant getRoot(ProgramParticipant child) {
        String ancestry = child.getAncestry();
        if (ancestry == null) {
            return null;
        } else {
            String uuid = ancestry.substring(0, 32);
            return getEm().find(ProgramParticipant.class, uuid);
        }
    }

    @Override
    public void mergeAll(List<ProgramParticipant> ppList){
    	for(ProgramParticipant pp : ppList){
    		em.merge(pp);
    	}
    }
    
    @Override
    public ProgramParticipant getCommonAncestor(ProgramParticipant pp1, ProgramParticipant pp2) {
        String a1 = pp1.getAncestry();
        String a2 = pp2.getAncestry();

        if (a1 == null || a2 == null) {
            return null;
        } else {
            int minLength = Math.min(a1.length(), a2.length());
            int limit = minLength / 32;
            String common = null;
            for (int i = 0; i < limit; i++) {
                String s1 = a1.substring(i * 32, (i + 1) * 32);
                String s2 = a2.substring(i * 32, (i + 1) * 32);
                if (s1.equals(s2)) {
                    common = s1;
                } else {
                    break;
                }
            }
            if (common != null) {
                return getEm().find(ProgramParticipant.class, common);
            } else {
                return null;
            }
        }
    }

    @Override
    public boolean isDescendant(ProgramParticipant parent, ProgramParticipant child) {
        String ancestry = child.getAncestry();
        return ancestry != null && ancestry.contains(parent.getUUID());
    }

    @Override
    public boolean isAncestor(ProgramParticipant parent, ProgramParticipant child) {
        return isDescendant(parent, child);
    }

    @Override
    public void deleteProgramParticipant(ProgramParticipant pp) {
        Set<ProgramParticipant> set = getFlatDescendants(pp);
        if (set.size() > 0) {
            for (ProgramParticipant next : set) {
                String ancestry = next.getAncestry();
                // if direct descendant, set to null
                if (next.getParent().getUUID().equals(pp.getUUID())) {
                    next.setAncestry(null);
                    next.setParent(null);
                } else { // if not, just set ancestry
                    String replace = ancestry.replace(pp.getAncestry() + pp.getUUID(), "");
                    next.setAncestry(replace);
                }
                getEm().merge(next);
            }
        }
        super.deleteProgramParticipant(pp);
    }

	@Override
	public List<TreeNodeVo> getChildren(String participantName,
			String programName) {
		StringBuffer sqltemplate = new StringBuffer(); 
		sqltemplate.append(" SELECT child.participantName AS participantName, child.account as accountNumber, child.secondary_account as secondaryAccountNo, ppchild.programName AS programName,       \n");
		sqltemplate.append(" 	(SELECT participantName FROM participant WHERE UUID=             														\n");
		sqltemplate.append(" 	(SELECT participant_uuid FROM program_participant WHERE UUID=ppchild.parent_uuid )) AS parentName,          			\n");
		sqltemplate.append("  	LENGTH(ppchild.ancestry) treelevel 					        	   														\n");
		sqltemplate.append("  	FROM   program_participant ppchild, program_participant ppparent,participant parent,participant child 					\n");
		sqltemplate.append("  	WHERE  parent.participantName =${participantName}  AND ppparent.participant_uuid = parent.uuid 							\n");		
		sqltemplate.append("  	AND ppchild.programName =${programName}   					         			  										\n");
		//sqltemplate.append("  	AND ( LOCATE(ppparent.uuid,ppchild.ancestry)>0 ) 																    \n");
		sqltemplate.append("    AND ppparent.uuid = ppchild.parent_uuid																			    	\n");
		sqltemplate.append("  	AND child.uuid = ppchild.participant_uuid  					         			  										\n");
		sqltemplate.append("  	ORDER BY treelevel,participantName ,participantName					         			  								\n");
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("participantName", participantName);
		params.put("programName", programName);
		
		String sql = null;
		try {
			sql = SQLBuilder.buildNamedParameterSQL(sqltemplate.toString(), params);
			List<TreeNodeVo> results = null;
			results = sqlExecutor.doNativeQuery(sql,params,
					new ListConverter<TreeNodeVo>(new ColumnAsFeatureFactory<TreeNodeVo>(TreeNodeVo.class)));
			return results;
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	@Override
	public List<Participant> getDescendantClients(String participantName,
			String programName) {
		StringBuffer sqltemplate = new StringBuffer(); 
		sqltemplate.append(" SELECT UUID,participantName FROM participant WHERE CLIENT=1  AND parent IN   (     			    \n");
		sqltemplate.append(" 	SELECT participantName FROM (     																			\n");
		sqltemplate.append("  	SELECT child.participantName AS participantName, ppchild.programName AS programName,          			  	\n");
		sqltemplate.append("  	(SELECT participantName FROM participant WHERE UUID=               			  								\n");
		sqltemplate.append("  	(SELECT participant_uuid FROM program_participant WHERE UUID=ppchild.parent_uuid )) AS parentName,          \n");
		sqltemplate.append("  	LENGTH(ppchild.ancestry) treelevel 	         			  													\n");
		sqltemplate.append("  	FROM   program_participant ppchild, program_participant ppparent,participant parent,participant child 	    \n");
		sqltemplate.append("  	WHERE  parent.participantName =${participantName}  AND ppparent.participant_uuid = parent.uuid  and child.retained=0         	\n");
		
		if(programName!=null && programName.trim().length()>0){
			sqltemplate.append("  	AND ppchild.programName like ${programName} 																\n");
		}
		         			  										
		//sqltemplate.append("  	AND ( LOCATE(ppparent.uuid,ppchild.ancestry)>0) 	         			  		\n");
		sqltemplate.append("    AND ppparent.uuid = ppchild.parent_uuid																				\n");
		sqltemplate.append("  	AND child.uuid = ppchild.participant_uuid           			  											\n");
		sqltemplate.append("  	ORDER BY treelevel,participantName ,participantName	         			  									\n");
		sqltemplate.append("  	) AS temp )         			  																			\n");
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("participantName", participantName);
		if(programName!=null && programName.trim().length()>0){
			params.put("programName", programName+"%");
		}
		
		String sql = null;
		try {
			sql = SQLBuilder.buildNamedParameterSQL(sqltemplate.toString(), params);
			List<Participant> results = null;
			results = sqlExecutor.doNativeQuery(sql,params,
					new ListConverter<Participant>(new ColumnAsFeatureFactory<Participant>(Participant.class)));
			return results;
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	@Override
	public List<Participant> getSiblingClients(String participantName,
			String programName) {
		StringBuffer sqltemplate = new StringBuffer(); 
		Map<String,Object> params = new HashMap<String,Object>();
		if(programName==null){
			sqltemplate.append(" SELECT UUID as UUID,participantName FROM participant WHERE CLIENT=1 AND retained=0 AND parent=${participantName}  		\n");
			params.put("participantName", participantName);
		}else{
			sqltemplate.append(" SELECT p.UUID AS UUID, p.participantName AS participantName 	\n");
			sqltemplate.append(" FROM participant p, program_participant pp 					\n");
			sqltemplate.append(" WHERE pp.participant_uuid=p.uuid AND CLIENT=1 AND retained=0 	\n");
			sqltemplate.append(" AND parent=${participantName}									\n");
			sqltemplate.append(" AND pp.programName=${programName} 								\n");
			
			params.put("participantName", participantName);
			params.put("programName", programName);
		}
		
		String sql = null;
		try {
			sql = SQLBuilder.buildNamedParameterSQL(sqltemplate.toString(), params);
			List<Participant> results = null;
			results = sqlExecutor.doNativeQuery(sql,params,
					new ListConverter<Participant>(new ColumnAsFeatureFactory<Participant>(Participant.class)));
			return results;
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	@Override
	public List<ProgramParticipant> getProgramParticipantsByProgram(
			String programName) {
		  List<ProgramParticipant> pps=null;
		  try {
	            Query query = em
	                    .createNamedQuery(
	                            "ProgramParticipant.findProgramParticipantsByProgram")
	                    .setParameter("programName", programName);

	            pps = query.getResultList();
	        } catch (Exception e) {
	            log.debug(LogUtils.createExceptionLogEntry(programName,null,e));
	        }

		return pps!=null?pps:new ArrayList<ProgramParticipant>();
	}

	@Override
	public List<ProgramParticipant>  findProgramClientByParticipantName(String participantName) {
		  List<ProgramParticipant> programParticipantList=null;
		  try {
	            Query query = em
	                    .createNamedQuery(
	                            "ProgramParticipant.findProgramClientByParticipantUUID")	                    
	            .setParameter("participantName", participantName);

	            programParticipantList = query.getResultList();
	        } catch (Exception e) {
	            log.debug(LogUtils.createExceptionLogEntry(participantName,null,e));
	        }

		return programParticipantList;
	}

}