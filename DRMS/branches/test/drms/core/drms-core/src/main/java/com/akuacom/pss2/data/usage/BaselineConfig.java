package com.akuacom.pss2.data.usage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.akuacom.ejb.VersionedEntity;

/**
 * The Class BaselineConfig.
 */
@Entity
@Table(name = BaselineConfig.Details.NAME)
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
	@NamedQuery(name = BaselineConfig.Details.FIND_BY_NAME_QUERY_NAME,
	    	query = BaselineConfig.Details.FIND_BY_NAME_QUERY),
	@NamedQuery(name = BaselineConfig.Details.FIND_BY_OWNERUUID_QUERY_NAME,
            query = BaselineConfig.Details.FIND_BY_OWNERUUID_QUERY),
    @NamedQuery(name = BaselineConfig.Details.FIND_BY_BASELINEMODEL_QUERY_NAME,
            query = BaselineConfig.Details.FIND_BY_BASELINEMODEL_QUERY)
        })
public class BaselineConfig extends VersionedEntity {

	private static final long serialVersionUID = 2562890435979121779L;
	
	@Column(name=BaselineConfig.Details.OWNER_UUID_COLUMN_NAME)
	private String ownerUuid;
	
	private String name;
	
	private boolean excludeHolidayFromCalc;
	
	private int excludedDaysOfWeekFromCalc;
	
	private boolean excludeHoliday;
	
	private int excludedDaysOfWeek;
	
	private String excludeAbnormalDayImplClass;
	
	private String description;
	
	private int eventStartTime;
	
	private int eventEndTime;
	
	private int maStartTime;
	
	private int maEndTime;
	
	private float minMARate;
	
	private float maxMARate;
	
	private boolean excludeEventDay;
	
	private boolean maByEvent;
	
	private int maBackOffset;
	
	private int maDuration;
	
	public boolean isMaByEvent() {
		return maByEvent;
	}

	public void setMaByEvent(boolean maByEvent) {
		this.maByEvent = maByEvent;
	}

	public int getMaBackOffset() {
		return maBackOffset;
	}

	public void setMaBackOffset(int maBackOffset) {
		this.maBackOffset = maBackOffset;
	}

	public int getMaDuration() {
		return maDuration;
	}

	public void setMaDuration(int maDuration) {
		this.maDuration = maDuration;
	}

	@ManyToOne
    @JoinColumn(name = BaselineConfig.Details.BASELINE_MODEL_UUID_COLUMN_NAME)
    @NotFound(action=NotFoundAction.IGNORE)
	private BaselineModel baselineModel;

	public String getOwnerUuid() {
		return ownerUuid;
	}

	public void setOwnerUuid(String ownerUuid) {
		this.ownerUuid = ownerUuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isExcludeHolidayFromCalc() {
		return excludeHolidayFromCalc;
	}

	public void setExcludeHolidayFromCalc(boolean excludeHolidayFromCalc) {
		this.excludeHolidayFromCalc = excludeHolidayFromCalc;
	}

	public int getExcludedDaysOfWeekFromCalc() {
		return excludedDaysOfWeekFromCalc;
	}

	public void setExcludedDaysOfWeekFromCalc(int excludedDaysOfWeekFromCalc) {
		this.excludedDaysOfWeekFromCalc = excludedDaysOfWeekFromCalc;
	}

	public boolean isExcludeHoliday() {
		return excludeHoliday;
	}

	public void setExcludeHoliday(boolean excludeHoliday) {
		this.excludeHoliday = excludeHoliday;
	}

	public int getExcludedDaysOfWeek() {
		return excludedDaysOfWeek;
	}

	public void setExcludedDaysOfWeek(int excludedDaysOfWeek) {
		this.excludedDaysOfWeek = excludedDaysOfWeek;
	}

	public String getExcludeAbnormalDayImplClass() {
		return excludeAbnormalDayImplClass;
	}

	public void setExcludeAbnormalDayImplClass(String excludeAbnormalDayImplClass) {
		this.excludeAbnormalDayImplClass = excludeAbnormalDayImplClass;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BaselineModel getBaselineModel() {
		return baselineModel;
	}

	public void setBaselineModel(BaselineModel baselineModel) {
		this.baselineModel = baselineModel;
	}

    public int getEventStartTime() {
		return eventStartTime;
	}

	public int getEventEndTime() {
		return eventEndTime;
	}

	public int getMaStartTime() {
		return maStartTime;
	}

	public int getMaEndTime() {
		return maEndTime;
	}

	public float getMinMARate() {
		return minMARate;
	}

	public float getMaxMARate() {
		return maxMARate;
	}

	public void setEventStartTime(int eventStartTime) {
		this.eventStartTime = eventStartTime;
	}

	public void setEventEndTime(int eventEndTime) {
		this.eventEndTime = eventEndTime;
	}

	public void setMaStartTime(int maStartTime) {
		this.maStartTime = maStartTime;
	}

	public void setMaEndTime(int maEndTime) {
		this.maEndTime = maEndTime;
	}

	public void setMinMARate(float minMARate) {
		this.minMARate = minMARate;
	}

	public void setMaxMARate(float maxMARate) {
		this.maxMARate = maxMARate;
	}
	@Transient
	public boolean isExcludeEventDay() {
		return excludeEventDay;
	}

	public void setExcludeEventDay(boolean excludeEventDay) {
		this.excludeEventDay = excludeEventDay;
	}

	@Transient
    public boolean isWeekendExcluded()
    {
        return (this.getExcludedDaysOfWeekFromCalc() == 1000001);   
    }

    @Transient
    public int getNumberOfExcludedDays()
    {
        int num = 0;
        String weekString = String.valueOf(this.getExcludedDaysOfWeekFromCalc());
        for(int i=0; i<7; i++)
        {
            String cur = weekString.substring(i, i+1);
            if(cur.equals("1"))
            {
                num++;
            }
        }
        return num;
    }

    @Transient
    public int getMaxNumberOfDays(int baseNumOfDays)
    {
        int maxDays = baseNumOfDays;

        int excDays = this.getNumberOfExcludedDays();
        maxDays = baseNumOfDays + baseNumOfDays / (baseNumOfDays-excDays) * excDays + excDays;
        return maxDays;
    }

    static class Details{
		static final String NAME = "baseline_config";
		static final int BATCH_SIZE = 50;
		static final String BASELINE_MODEL_UUID_COLUMN_NAME = "baseline_model_uuid";
		static final String OWNER_UUID_COLUMN_NAME = "owner_uuid";
		static final String NAME_COLUMN_NAME = "name";
		static final String EXCLUDEHOLIDAYFROMCALC_COLUMN_NAME = "excludeHolidayFromCalc";
		static final String EXCLUDEDDAYSOFWEEKFROMCALC_COLUMN_NAME = "excludedDaysOfWeekFromCalc";
		static final String EXCLUDEHOLIDAY_COLUMN_NAME = "excludeHoliday";
		static final String EXCLUDEDDAYSOFWEEK_COLUMN_NAME = "excludedDaysOfWeek";
		static final String EXCLUDEABNORMALDAYIMPLCLASS_COLUMN_NAME = "excludeAbnormalDayImplClass";
		static final String DESCRIPTION_COLUMN_NAME = "description";
		
		static final String FIND_BY_NAME_QUERY_NAME = "BaselineConfig.findByName";
		static final String FIND_BY_NAME_QUERY = "select config from BaselineConfig config where config.name = :name";
		
		static final String FIND_BY_OWNERUUID_QUERY_NAME = "BaselineConfig.findByOwnerUuid";
		static final String FIND_BY_OWNERUUID_QUERY = "select config from BaselineConfig config where config.ownerUuid = :ownerUuid";
		
		static final String FIND_BY_BASELINEMODEL_QUERY_NAME = "BaselineConfig.findByBaselineModelUuid";
		static final String FIND_BY_BASELINEMODEL_QUERY = "select config from BaselineConfig config where config.baselineModel = :baselineModel";
	}

//	public boolean isMaByEvent() {
//		return maByEvent;
//	}
//
//	public void setMaByEvent(boolean maByEvent) {
//		this.maByEvent = maByEvent;
//	}
//
//	public int getMaBackOffset() {
//		return maBackOffset;
//	}
//
//	public void setMaBackOffset(int maBackOffset) {
//		this.maBackOffset = maBackOffset;
//	}
//
//	public int getMaDuration() {
//		return maDuration;
//	}
//
//	public void setMaDuration(int maDuration) {
//		this.maDuration = maDuration;
//	}

}
