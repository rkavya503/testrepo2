/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.program.participant.ProgramParticipantEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.participant;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantRtpStrategy;
import com.akuacom.pss2.program.Program;

/**
 * Represents the customization of a program for a given participant.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "program_participant")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries( {
        @NamedQuery(name = "ProgramParticipant.findAll",
                        query = "select pp from ProgramParticipant pp",
                        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "ProgramParticipant.findByParticipant",
                        query = "select pp from ProgramParticipant pp where pp.participant.participantName = :participantName and pp.participant.client = :client and pp.state = 1 order by pp.program.priority",
                        hints={@QueryHint(name="org.hibernate.cacheable", value="true")} ),
        @NamedQuery(name = "ProgramParticipant.findAllByClient",
                        query = "select pp from ProgramParticipant pp where pp.participant.participantName = :participantName and pp.participant.client = :client order by pp.program.priority",
                        hints={@QueryHint(name="org.hibernate.cacheable", value="true")} ),
        @NamedQuery(name = "ProgramParticipant.findByProgram",
                        query = "select pp from ProgramParticipant pp where pp.programName = :programName and pp.state = 1",
                        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "ProgramParticipant.findAllByProgram",
                query = "select pp from ProgramParticipant pp where pp.programName = :programName",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
                
        @NamedQuery(name = "ProgramParticipant.findProgramParticipantAllByProgram",
                query = "select  distinct(pp) from ProgramParticipant pp left join fetch pp.bidEntries left join fetch pp.ppRules left join fetch pp.constraint left join fetch pp.participant where pp.programName = :programName",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
                        
        @NamedQuery(name = "ProgramParticipant.findByProgramConfig",
                query = "select distinct(pp) from ProgramParticipant pp left join fetch pp.bidEntries left join fetch pp.ppRules left join fetch pp.constraint left join fetch pp.participant where pp.participant.participantName = :participantName    ",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "ProgramParticipant.findByProgramAndType",
                query = "select distinct(pp) from ProgramParticipant pp where pp.programName = :programName and pp.participant.client = :client and pp.state = 1",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "ProgramParticipant.findByProgramAndParticipant",
                query = "select distinct(pp) from ProgramParticipant pp  left join fetch pp.bidEntries left join fetch pp.constraint  left join fetch pp.ppRules where pp.programName = :programName and pp.participant.participantName = :participantName and pp.participant.client = :client and pp.state = 1",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}) ,
        @NamedQuery(name = "ProgramParticipant.countByProgramAndParticipant",
                query = "select count(pp) from ProgramParticipant pp where pp.programName = :programName and pp.participant.participantName = :participantName and pp.participant.client = :client and pp.state = 1",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}) ,
        @NamedQuery(name = "ProgramParticipant.findByProgramAndParticipantForClient",
                query = "select distinct(pp) from ProgramParticipant pp left join fetch pp.bidEntries left join fetch pp.constraint left join fetch pp.ppRules left join fetch pp.program  left join fetch pp.participant where pp.programName = :programName and pp.participant.participantName = :participantName and pp.participant.client = :client",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}) ,
        @NamedQuery(name = "ProgramParticipant.findByProgramAndParentParticipantForClient",
                query = "select distinct(pp) from ProgramParticipant pp  left join fetch pp.bidEntries left join fetch pp.program left join fetch pp.participant left join fetch pp.constraint left join fetch pp.ppRules  join fetch pp.program  left join fetch pp.participant where pp.programName = :programName and pp.participant.parent = :parentParticipantName and pp.participant.client = :client ",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}) ,
        @NamedQuery(name = "ProgramParticipant.findAllByProgramAndParticipant",
                        query = "select distinct(pp) from ProgramParticipant pp left join fetch pp.program left join fetch pp.participant left join fetch pp.constraint left join fetch pp.ppRules left join fetch pp.bidEntries where pp.programName = :programName and pp.participant.participantName = :participantName and pp.state = :state and pp.participant.client = :client ",
                        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "ProgramParticipant.findAllAggregationDescendants.list",
                query = "select pp from ProgramParticipant pp where pp.programName = :programName and locate(:ancestor ,pp.ancestry) > 0"),
                
        @NamedQuery(name = "ProgramParticipant.findClientPrograms",
        		query = "select DISTINCT pp.program.utilityProgramName from ProgramParticipant pp " +
        				"where pp.participant.parent = :participantName and pp.participant.client='1' " +
        				"and pp.state='1' "),
        				
		@NamedQuery(name = "ProgramParticipant.findRtpStrategyByProgAndPartForClient",
	        query = "select distinct(pp) from ProgramParticipant pp  left join fetch pp.rtpStrateges left join fetch pp.program left join fetch pp.participant where pp.programName = :programName and pp.participant.participantName = :participantName and pp.participant.client = :client ",
	        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}) ,
	        
        @NamedQuery(name = "ProgramParticipant.findProgramClientByProgramAndParticipant",
            query = "select distinct(pp) from ProgramParticipant pp left join fetch pp.program left join fetch pp.participant left join fetch pp.constraint left join fetch pp.rtpStrateges left join fetch pp.ppRules left join fetch pp.bidEntries where pp.programName = :programName and pp.participant.participantName IN (:participantNames) and pp.participant.client = 1 ",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
           
        @NamedQuery(name = "ProgramParticipant.findProgramParticipantsByProgram",
            query = "select pp from ProgramParticipant pp where pp.programName = :programName and pp.state = 1 and pp.participant.client =0 ",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        		
        @NamedQuery(name = "ProgramParticipant.findDirectDescendants.list",
                query = "select pp from ProgramParticipant pp where pp.programName = :programName and pp.parent = :parent"),
                
        @NamedQuery(name = "ProgramParticipant.findOptedOutProgram",
        query = "select pp from ProgramParticipant pp where pp.optStatus = 1 and pp.optOutUntil < CURDATE()",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
      
        @NamedQuery(name = "ProgramParticipant.findAggregatorTestParticipantByProgram",
        query = "select pp from ProgramParticipant pp left join fetch pp.participant where pp.participant.testParticipant = 0  and pp.participant.aggregator = 1 and pp.programName = :programName and pp.state = 1"),
       
        @NamedQuery(name = "ProgramParticipant.findProgramClientByParticipantUUID",
        query = "select distinct(pp) from ProgramParticipant pp left join fetch pp.rtpStrateges left join fetch pp.ppRules left join fetch pp.constraint " +
        		" left join fetch pp.program prog left join fetch prog.bidConfig pb left join fetch pb.bidBlocks " +
        		" where pp.participant.participantName =:participantName",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")})

})
public class ProgramParticipant extends Node<ProgramParticipant>
{

    /**
	 * Serial Version UID
	 */
    private static final long serialVersionUID = -7800460850854682610L;

	/** The PROGRA m_ par t_ deleted. */
    public static int PROGRAM_PART_DELETED = 0;

    /** The PROGRA m_ par t_ active. */
    public static int PROGRAM_PART_ACTIVE = 1;

    /** The participant name. */
    @ManyToOne
    @JoinColumn(name = "participant_uuid")
    private Participant participant;

    /** The program name. */
    private String programName;

    /** The program . */
    @ManyToOne
    @JoinColumn(name = "program_uuid")
    private Program program;

    /** The bid entries. */
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "programParticipant" )
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
		org.hibernate.annotations.CascadeType.REMOVE,
        org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<ProgramParticipantBidEntry> bidEntries;

    /** The rules. */
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "programParticipant" )
        @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
                org.hibernate.annotations.CascadeType.REMOVE,
                org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
        @Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<ProgramParticipantRule> ppRules;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "programParticipant" )
        @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
                org.hibernate.annotations.CascadeType.REMOVE,
                org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
        @Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<CustomRule> customRules;

    /** The constraint. */
    //One-To-Many acting as one-to-one mapping to achieve lazy load
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "programParticipant" )
        @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
                org.hibernate.annotations.CascadeType.REMOVE,
                org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
        @Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<Constraint> constraint;
    
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "programParticipant")
	@Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.REMOVE })
	@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<ParticipantRtpStrategy> rtpStrateges = new HashSet<ParticipantRtpStrategy>();

	/** The state. */
	private Integer state;

	/** The clinet-config. */
	private Integer clientConfig = 0;

    private Integer optStatus = 0;
    
    private Date optOutUntil;
    
    

    /**
	 * @return the optOutUntil
	 */
	public Date getOptOutUntil() {
		return optOutUntil;
	}

	/**
	 * @param optOutUntil the optOutUntil to set
	 */
	public void setOptOutUntil(Date optOutUntil) {
		this.optOutUntil = optOutUntil;
	}

	@Column(name = "uisActive")
    private Boolean uisActive;

    private Integer applyDayOfBaselineAdjustment = 0;
    
    public Integer getOptStatus() {
       if(optStatus == null){
                 optStatus = Integer.valueOf(0);
         }
        return optStatus;
    }

    public void setOptStatus(Integer optStatus) {
        this.optStatus = optStatus;
    }
    
    public Integer getApplyDayOfBaselineAdjustment() {
		if(applyDayOfBaselineAdjustment == null){
			applyDayOfBaselineAdjustment = Integer.valueOf(0);
    }
		return applyDayOfBaselineAdjustment;
	}

	public void setApplyDayOfBaselineAdjustment(Integer applyDayOfBaselineAdjustment) {
		this.applyDayOfBaselineAdjustment = applyDayOfBaselineAdjustment;
	}
    
	/**
	 * Convience method
	 */
	public String getParticipantName() {
		if (participant == null)
			return null;
		else
			return participant.getParticipantName();
	}

	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

        /**
     * Gets the program name.
     *
     * @return the program name
     */
    public String getProgramName() {
        return programName;
    }

    /**
     * Sets the program name.
     *
     * @param programName the new program name
     */
    public void setProgramName(String programName) {
        this.programName = programName;
    }

    /**
     * Gets the program .
     *
     * @return the program attribute
     */
    public Program getProgram() {
        return program;
    }

    /**
     * Sets the program .
     *
     * @param program the new program
     */
    public void setProgram(Program program) {
        this.program = program;
    }

    /**
     * Gets the bid entries.
     *
     * @return the bid entries
     */
    public Set<ProgramParticipantBidEntry> getBidEntries() {
        return bidEntries;
    }

    /**
     * Sets the bid entries.
     *
     * @param bidEntries the new bid entries
     */
    public void setBidEntries(Set<ProgramParticipantBidEntry> bidEntries) {
        this.bidEntries = bidEntries;
    }

    /**
     * Gets the state.
     *
     * @return the state
     */
    public Integer getState() {
        return state;
    }

    /**
     * Sets the state.
     *
     * @param state the new state
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * Gets the ClientConfig.
     *
     * @return the ClientConfig
     */
    public Integer getClientConfig() {
         if(clientConfig == null){
                 clientConfig = Integer.valueOf(0);
         }
        return clientConfig;
    }

    /**
     * Sets the ClientConfig
     *
     * @param clientConfig the new ClientConfig
     */
    public void setClientConfig(Integer clientConfig) {
        this.clientConfig = clientConfig;
    }

	/**
	 * Gets the rules.
	 * 
	 * @return the rules
	 */
	public Set<ProgramParticipantRule> getProgramParticipantRules() {
		return ppRules;
	}

	/**
	 * Sets the rules.
	 * 
	 * @param rules
	 *            the new rules
	 */
	public void setProgramParticipantRules(Set<ProgramParticipantRule> rules) {
		this.ppRules = rules;
	}

	/**
	 * Gets the rules.
	 * 
	 * @return the rules
	 */
	public Set<CustomRule> getCustomRules() {
		return customRules;
	}

	/**
	 * Sets the rules.
	 * 
	 * @param customRules
	 *            the new rules
	 */
	public void setCustomRules(Set<CustomRule> customRules) {
		this.customRules = customRules;
	}

    /**
     * Gets the constraint.
     *
     * @return the constraint
     */
    public Constraint getConstraint() {
    	if(constraint!=null && !constraint.isEmpty()){
    		return constraint.iterator().next();
        }
    	return null;
    }
    	
	/**
	 * Sets the constraint.
	 * 
	 * @param constraint
	 *            the new constraint
	 */
	public void setConstraint(Constraint constraint) {
		if(this.constraint==null){
			this.constraint = new HashSet<Constraint>();
		}
		if(!this.constraint.isEmpty())
			this.constraint.remove(0);
		this.constraint.add(constraint);
	}
	
	public boolean equalsValue(ProgramParticipant that) {
		return this.getProgramName().equals(that.getProgramName())
				&& this.getParticipantName().equals(that.getParticipantName());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((program == null || program.getUUID() == null) ? 0 : program
						.getUUID().hashCode());
		result = prime
				* result
				+ ((participant == null || participant.getUUID() == null) ? 0
						: participant.getUUID().hashCode());
		return result;
	}
    /**
    @PreRemove
    public void prepareForDelete() {
         if (this.getProgram() != null) {
                 this.getProgram().getProgramParticipants().remove(this);
         }
         if (this.getParticipant() != null) {
                 this.getParticipant().getProgramParticipants().remove(this);
         }
    }
    */

	public void setUisActive(Boolean uisActive) {
		this.uisActive = uisActive;
	}

	public Boolean getUisActive() {
		return uisActive;
	}

	public Set<ParticipantRtpStrategy> getRtpStrateges() {
		return rtpStrateges;
	}

	public void setRtpStrateges(Set<ParticipantRtpStrategy> rtpStrateges) {
		this.rtpStrateges = rtpStrateges;
	}
	
	
}