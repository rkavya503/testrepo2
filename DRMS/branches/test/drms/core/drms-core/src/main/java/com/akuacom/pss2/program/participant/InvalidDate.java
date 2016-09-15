/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.InvalidDateEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.participant;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.VersionedEntity;


/**
 * The Class InvalidDate.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "constraint_invalid_date")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
public class InvalidDate extends VersionedEntity {
	
	private static final long serialVersionUID = -3601894989088072564L;

	/** The invalid date. */
	private Date invalidDate;
	
	/** The constraint. */
	@ManyToOne
    @JoinColumn(name = "program_participant_constraint_uuid")
	private Constraint constraint;

	/**
	 * Gets the invalid date.
	 * 
	 * @return the invalid date
	 */
	public Date getInvalidDate() {
		return invalidDate;
	}
	
	/**
	 * Sets the invalid date.
	 * 
	 * @param invalidDate the new invalid date
	 */
	public void setInvalidDate(Date invalidDate) {
		this.invalidDate = invalidDate;
	}
	
	/**
	 * Gets the constraint.
	 * 
	 * @return the constraint
	 */
	public Constraint getConstraint() {
		return constraint;
	}
	
	/**
	 * Sets the constraint.
	 * 
	 * @param constraint the new constraint
	 */
	public void setConstraint(Constraint constraint) {
		this.constraint = constraint;
	}
}
