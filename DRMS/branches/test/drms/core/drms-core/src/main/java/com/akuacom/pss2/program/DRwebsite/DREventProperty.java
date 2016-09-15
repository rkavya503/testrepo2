
package com.akuacom.pss2.program.DRwebsite;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.akuacom.ejb.VersionedEntity;
   
@Entity
//@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name ="drwebsite_property")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
        @NamedQuery(name = DREventProperty.Details.FIND_BY_PROPERTY_NAME,
                query =DREventProperty.Details.FIND_BY_PROPERTY_NAME_QUERY,
                hints={@QueryHint(name="org.hibernate.cacheable", value="false")})
})        
public class DREventProperty extends VersionedEntity {
	static class Details{
		static final String FIND_BY_PROPERTY_NAME = "DREventProperty.findByPropertyName";
		static final String FIND_BY_PROPERTY_NAME_QUERY = "select entity from DREventProperty entity where entity.propertyName = :propertyName";
	}
	private static final long serialVersionUID = 1068098071654273906L;

	/** The property name. */
	private String propertyName;

	/** The string value. */
	private String stringValue;	

	@Column(columnDefinition="text")
	private String textValue;

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public String getTextValue() {
		return textValue;
	}

	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}
	
	
}
