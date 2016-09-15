package com.akuacom.pss2.data.usage;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.BaseEntity;

@Entity
@Table(name = "datasource_usage")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
        @NamedQuery(name = "DataSourceUsage.findBysourceIdAndDate", query = "select ds from DataSourceUsage ds where ds.datasource_uuid = :datasourceuuid and ds.date = :date", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
        @NamedQuery(name = "DataSourceUsage.findBysourceId",
                query = "select ds from DataSourceUsage ds where ds.datasource_uuid in (:datasourceuuids) order by ds.date")
})
public class DataSourceUsage extends BaseEntity {

	private static final long serialVersionUID = 7777441191028205178L;

	private String datasource_uuid;
	private String eventName;
	private Date date;
	private Float maxgap;
	private Date lastactual;
	private Boolean baseline_state;
	private Boolean adjusted;
	
	public String getDatasource_uuid() {
		return datasource_uuid;
	}
	public Date getDate() {
		return date;
	}
	public Float getMaxgap() {
		return maxgap;
	}
	public Date getLastactual() {
		return lastactual;
	}
	public Boolean getBaseline_state() {
		return baseline_state;
	}
	public void setDatasource_uuid(String datasource_uuid) {
		this.datasource_uuid = datasource_uuid;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public void setMaxgap(Float maxgap) {
		this.maxgap = maxgap;
	}
	public void setLastactual(Date lastactual) {
		this.lastactual = lastactual;
	}
	public void setBaseline_state(Boolean baseline_state) {
		this.baseline_state = baseline_state;
	}
	public Boolean getAdjusted() {
		return adjusted;
	}
	public void setAdjusted(Boolean adjusted) {
		this.adjusted = adjusted;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

}
