package com.akuacom.pss2.program.dl;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.akuacom.pss2.program.Program;

@Entity
@DiscriminatorValue("DemandLimitingProgram")
public class DemandLimitingProgram extends Program implements Serializable {

	private static final long serialVersionUID = 2770012249070731507L;
	public static final String PROGRAM_NAME = "Demand Limiting Program";

    @Override
    public Program getNewInstance() {
        return new DemandLimitingProgram();
    }

    /* (non-Javadoc)
     * @see com.akuacom.pss2.core.model.Program#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder rv = new StringBuilder("DemandLimitingProgram: ");
        rv.append(super.toString());
        return rv.toString();
   }

}
