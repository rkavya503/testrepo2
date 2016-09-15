package com.akuacom.pss2.data.usage;

import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.ejb.BaseEAOBean;
import com.akuacom.pss2.util.LogUtils;

@Stateless
public class BaselineConfigEAOBean extends BaseEAOBean<BaselineConfig> implements BaselineConfigEAO.R, BaselineConfigEAO.L {

	private static final Logger log = Logger.getLogger(BaselineConfigEAOBean.class);

	public BaselineConfigEAOBean() {
		super(BaselineConfig.class);
	}

	/**
	 * Finds the only entity represented by the given name or null
	 * if not found.
	 * @param name
	 * @return
	 */
	@Override
	public BaselineConfig getBaselineConfigByName(String name) {
		  final Query namedQuery = em.createNamedQuery(
				  BaselineConfig.Details.FIND_BY_NAME_QUERY_NAME).setParameter("name",
			name);

		  try {
			  return (BaselineConfig) namedQuery.getSingleResult();
		  } catch (Exception e) {
		      // TODO 2992
		      log.error(LogUtils.createExceptionLogEntry("", this.getClass().getName(), e));
		  }
		  return null;
	}

	/**
	 * Finds the only entity represented by the given owner_uuid 
	 * return null if not found or more than 1 result.
	 * @param uuid
	 * @return
	 */
	@Override
	public BaselineConfig getBaselineConfigByOwnerUUID(String uuid) {
		  final Query namedQuery = em.createNamedQuery(
				  BaselineConfig.Details.FIND_BY_OWNERUUID_QUERY_NAME).setParameter("ownerUuid",
						  uuid);

		  try {
			  return (BaselineConfig) namedQuery.getSingleResult();
		  } catch (Exception e) {
		      // TODO 2992
		      log.error(LogUtils.createExceptionLogEntry("", this.getClass().getName(), e));
		  }
		  return null;
	}

	/**
	 * Finds all the Entities represented by the given baselineModel 
	 * @param baselineModel
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BaselineConfig> getBaselineConfigByBaselineModel(BaselineModel baselineModel) {
		  final Query namedQuery = em.createNamedQuery(
				  BaselineConfig.Details.FIND_BY_BASELINEMODEL_QUERY_NAME).setParameter("baselineModel",
						  baselineModel);

		  try {
			  return namedQuery.getResultList();
		  } catch (Exception e) {
		      log.debug(LogUtils.createExceptionLogEntry("", this.getClass().getName(), e));
		  }
		  return Collections.<BaselineConfig>emptyList();
	}
	
	@Override
	public void delete(BaselineConfig entity) throws EntityNotFoundException {
		if(!em.contains(entity)) {
			log.debug(entity + " is not bound");
			entity = em.find(BaselineConfig.class, entity.getUUID());
			if(entity == null) {
				throw new EntityNotFoundException(entity + " not in db");
			}
			if(entity.getBaselineModel() != null)  {
				entity.getBaselineModel().getBaselineConfigs().remove(entity);
			}
			entity.setBaselineModel(null);
			em.remove(entity);
			log.debug("delete(ent) is bound " + em.contains( entity ));
			entity=null;
			em.flush();
		} else {
			em.remove(entity);
		}
	}


}
