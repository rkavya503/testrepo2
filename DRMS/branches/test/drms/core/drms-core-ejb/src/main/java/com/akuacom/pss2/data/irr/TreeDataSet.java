/**
 * 
 */
package com.akuacom.pss2.data.irr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class TreeDataSet implements Serializable{
	
	private static final long serialVersionUID = -2050535989978438445L;

	private String UUID;
	
	private String name;
	private String unit;
	private Long period;
	private String description;
	private Boolean sync;
	private Integer graphType;
	private Integer missingDataType;
	private Integer valuePrecision;
	private String colorHint;
	private Boolean interpolated;
	private Integer dataType;
    private String enumTitles;
    private String calculationImplClass;
    private Boolean disable;

    List<TreeDataSource> dataSources=new ArrayList<TreeDataSource>();
    
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public List<TreeDataSource> getDataSources() {
		return dataSources;
	}

	public void setDataSources(List<TreeDataSource> dataSources) {
		this.dataSources = dataSources;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uuid) {
		UUID = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPeriod() {
		return period;
	}

	public void setPeriod(Long period) {
		this.period = period;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getSync() {
		return sync;
	}

	public void setSync(Boolean sync) {
		this.sync = sync;
	}

	public Integer getGraphType() {
		return graphType;
	}

	public void setGraphType(Integer graphType) {
		this.graphType = graphType;
	}

	public Integer getValuePrecision() {
		return valuePrecision;
	}

	public void setValuePrecision(Integer valuePrecision) {
		this.valuePrecision = valuePrecision;
	}

	public String getColorHint() {
		return colorHint;
	}

	public void setColorHint(String colorHint) {
		this.colorHint = colorHint;
	}

	public Boolean getInterpolated() {
		return interpolated;
	}

	public void setInterpolated(Boolean interpolated) {
		this.interpolated = interpolated;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public String getEnumTitles() {
		return enumTitles;
	}

	public void setEnumTitles(String enumTitles) {
		this.enumTitles = enumTitles;
	}

	public String getCalculationImplClass() {
		return calculationImplClass;
	}

	public void setCalculationImplClass(String calculationImplClass) {
		this.calculationImplClass = calculationImplClass;
	}

	public Integer getMissingDataType() {
		return missingDataType;
	}

	public void setMissingDataType(Integer missingDataType) {
		this.missingDataType = missingDataType;
	}

	public Boolean getDisable() {
		return disable;
	}

	public void setDisable(Boolean disable) {
		this.disable = disable;
	}
	
}
