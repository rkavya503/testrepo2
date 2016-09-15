/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.akuacom.pss2.usecasetest.tests;

import com.akuacom.pss2.usecasetest.cases.AbstractUseCase;

/**
 *
 * @author spierson
 */
public class UsecaseTestBase {

    public Object runCase(AbstractUseCase usecase) throws Exception {
        return usecase.runCase();
    }
}
