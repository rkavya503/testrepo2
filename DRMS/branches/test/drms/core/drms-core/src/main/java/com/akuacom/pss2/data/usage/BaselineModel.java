package com.akuacom.pss2.data.usage;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.akuacom.ejb.VersionedEntity;

/**
 * The Class BaselineModel.
 */
@Entity
@Table(name = BaselineModel.Details.NAME)
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
	@NamedQuery(name = BaselineModel.Details.FIND_BY_NAME_QUERY_NAME,
            query = BaselineModel.Details.FIND_BY_NAME_QUERY)
        })
public class BaselineModel extends VersionedEntity {
		
	private static final long serialVersionUID = -3804626418461022627L;
	
	/** The Constant CURRENT_USAGE. */
    private String name;
    
    private String implClass;
    
    private String dayPickerImplClass;
    
    private String calcImplClass;
    
    private String description;
    
    /** The baselineConfigs. */
    @OneToMany(mappedBy = BaselineModel.Details.MAPPEDBY_COLUMN_NAME, cascade = {CascadeType.ALL}, fetch=FetchType.EAGER)
    @Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
    @NotFound(action=NotFoundAction.IGNORE)
    @OnDelete(action=OnDeleteAction.CASCADE)
    private Set<BaselineConfig> baselineConfigs;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImplClass() {
		return implClass;
	}

	public void setImplClass(String implClass) {
		this.implClass = implClass;
	}

	public String getDayPickerImplClass() {
		return dayPickerImplClass;
	}

	public void setDayPickerImplClass(String dayPickerImplClass) {
		this.dayPickerImplClass = dayPickerImplClass;
	}

	public String getCalcImplClass() {
		return calcImplClass;
	}

	public void setCalcImplClass(String calcImplClass) {
		this.calcImplClass = calcImplClass;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<BaselineConfig> getBaselineConfigs() {
		return baselineConfigs;
	}

	public void setBaselineConfigs(Set<BaselineConfig> baselineConfigs) {
		this.baselineConfigs = baselineConfigs;
	}
	
	static class Details{
		static final String NAME = "baseline_model";
		static final int BATCH_SIZE = 50;
		static final String NAME_COLUMN_NAME = "name";
		static final String IMPLCLASS_COLUMN_NAME = "implClass";
		static final String DAYPICKERIMPLCLASS_COLUMN_NAME = "dayPickerImplClass";
		static final String CALCIMPLCLASS_COLUMN_NAME = "calcImplClass";
		static final String DESCRIPTION_COLUMN_NAME = "description";
		static final String MAPPEDBY_COLUMN_NAME = "baselineModel";
		static final String FIND_BY_NAME_QUERY_NAME = "BaselineModel.findByName";
		static final String FIND_BY_NAME_QUERY = "select lineModel from BaselineModel lineModel where lineModel.name = :name";
	}

}
