/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.akuacom.pss2.facdash;

import java.io.Serializable;

/**
 *
 * @author ahmed
 */
public class JSFProgram implements Serializable {

    public JSFProgram(String name){
		this.name = name;
	}


    	/** The name. */
	private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}
