/**
 * 
 */
package com.akuacom.pss2.data.gridpoint;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.akuacom.ejb.BaseEntity;

/**
 * the class GridPointConfiguration
 *
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "grid_point_configuration")
public class GridPointConfiguration extends BaseEntity {

	private static final long serialVersionUID = 3047974480646393914L;
	
	private String authenticationURL;
	private String username;
	private String password;
	private String retrieveDataURL;
	private Integer timeInterval;
	private Boolean fixScopeEnabled;
	private Integer fixScopeValue;
	private Integer dateBackScope;
	
	public String getAuthenticationURL() {
		return authenticationURL;
	}
	public void setAuthenticationURL(String authenticationURL) {
		this.authenticationURL = authenticationURL;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRetrieveDataURL() {
		return retrieveDataURL;
	}
	public void setRetrieveDataURL(String retrieveDataURL) {
		this.retrieveDataURL = retrieveDataURL;
	}
	public Integer getTimeInterval() {
		return timeInterval;
	}
	public void setTimeInterval(Integer timeInterval) {
		this.timeInterval = timeInterval;
	}
	public Boolean getFixScopeEnabled() {
		return fixScopeEnabled;
	}
	public void setFixScopeEnabled(Boolean fixScopeEnabled) {
		this.fixScopeEnabled = fixScopeEnabled;
	}
	public Integer getFixScopeValue() {
		return fixScopeValue;
	}
	public void setFixScopeValue(Integer fixScopeValue) {
		this.fixScopeValue = fixScopeValue;
	}
	public Integer getDateBackScope() {
		return dateBackScope;
	}
	public void setDateBackScope(Integer dateBackScope) {
		this.dateBackScope = dateBackScope;
	}
}
