/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.program.ProgramMatrixForm.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.program;

import org.apache.struts.action.ActionForm;

/**
 * The Class ProgramMatrixForm.
 */
public class ProgramMatrixForm extends ActionForm
{
	
	/** The matrix cells. */
	private String matrixCells[];

    /**
     * Gets the matrix cells.
     * 
     * @return the matrix cells
     */
    public String[] getMatrixCells()
    {
        return matrixCells;
    }

    /**
     * Sets the matrix cells.
     * 
     * @param matrixCells the new matrix cells
     */
    public void setMatrixCells(String[] matrixCells)
    {
        this.matrixCells = matrixCells;
    }
}