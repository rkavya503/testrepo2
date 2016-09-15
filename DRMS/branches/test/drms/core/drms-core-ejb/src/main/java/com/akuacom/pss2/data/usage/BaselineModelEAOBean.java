package com.akuacom.pss2.data.usage;

import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.akuacom.ejb.BaseEAOBean;
import com.akuacom.pss2.util.LogUtils;

@Stateless
public class BaselineModelEAOBean extends BaseEAOBean<BaselineModel> implements
        BaselineModelEAO.R, BaselineModelEAO.L {

    private static final Logger log = Logger
            .getLogger(BaselineModelEAOBean.class);

    public BaselineModelEAOBean() {
        super(BaselineModel.class);
    }

    /**
     * Finds the only entity represented by the given name or null if not found.
     * 
     * @param name
     * @return
     */
    @Override
    public BaselineModel getBaselineModelByName(String name) {
        final Query namedQuery = em.createNamedQuery(
                BaselineModel.Details.FIND_BY_NAME_QUERY_NAME).setParameter(
                "name", name);

        try {
            return (BaselineModel) namedQuery.getSingleResult();
        } catch (Exception e) {
            // TODO 2992
            log.error(LogUtils.createExceptionLogEntry("", this.getClass()
                    .getName(), e));
        }
        return null;
    }

}
