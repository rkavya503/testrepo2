/**
 * 
 */
package com.akuacom.pss2.notifications.usecasetest;

import com.akuacom.ejb.jboss.test.JBossFixture;
import com.akuacom.pss2.contact.ContactManager;
import com.akuacom.pss2.contact.ContactManagerBean;
import com.akuacom.pss2.email.EmailManager;
import com.akuacom.pss2.email.EmailManagerBean;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.SystemManagerBean;
import com.akuacom.pss2.usecasetest.cases.AbstractUseCase;


/**
 * @author Linda
 *
 */
public abstract class AbstractNotificationUseCase extends AbstractUseCase {

    private static EmailManager mailManager = null;
    private static SystemManager systemManager = null;
    private static ContactManager contactManager = null;

    protected EmailManager getEmailManager() {
        if (mailManager == null) mailManager = JBossFixture.lookupSessionRemote(EmailManagerBean.class);
        return mailManager;
    }
    
    protected SystemManager getSystemManager() {
        if (systemManager == null) systemManager = JBossFixture.lookupSessionRemote(SystemManagerBean.class);
        return systemManager;
    }
    
    protected ContactManager getContactManager() {
        if (contactManager == null) contactManager = JBossFixture.lookupSessionRemote(ContactManagerBean.class);
        return contactManager;
    }
}
