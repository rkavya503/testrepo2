/**
 * 
 */
package com.akuacom.pss2.drw.core;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * the entity Block
 */
@Entity
@Table(name = "block")
@NamedQueries( {
	@NamedQuery(name = "Block.getByProduct", 
			query = "select e.name from Block e where e.productName = :productName and e.status=:status")
	})
public class Block extends AbstractLibraryEntirty {

	private static final long serialVersionUID = 5968945073585195231L;

	private Integer number;
	private String name;
	private String productName;
	private Boolean status;
	
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
}
