/**
 * 
 */
package com.akuacom.pss2.system.property;

import javax.ejb.Stateless;

import com.akuacom.ejb.BaseEAOBean;
import com.akuacom.ejb.EntityNotFoundException;

/**
 * @see CorePropertyEAO
 * @author roller
 * 
 */
@Stateless
public class CorePropertyEAOBean extends BaseEAOBean<CoreProperty> implements
        CorePropertyEAO.R, CorePropertyEAO.L {

    public CorePropertyEAOBean() {
        super(CoreProperty.class);
    }

    @Override
    public CoreProperty getByPropertyName(String propertyName)
            throws EntityNotFoundException {

        return super.getByKey(
                CoreProperty.Details.FIND_BY_PROPERTY_NAME_QUERY_NAME,
                CoreProperty.Details.PROPERTY_NAME_PARAMETER, propertyName);
    }

    /**
     * Searches for others of this same entity using whatever other finders may
     * be possible given the unique identifiers.
     *
     * @param property
     * @return
     * @throws EntityNotFoundException
     */
    @Override
    protected CoreProperty getByOtherIdentifiers(CoreProperty property)
            throws EntityNotFoundException {

        // property name is the only other identifier...so that's all they get
        return getByPropertyName(property.getPropertyName());
    }
}
