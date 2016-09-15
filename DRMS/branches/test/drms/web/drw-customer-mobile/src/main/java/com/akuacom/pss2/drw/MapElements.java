package com.akuacom.pss2.drw;

import javax.xml.bind.annotation.XmlElement;

public class MapElements {

	@XmlElement
	public String key;
	@XmlElement
	public Long value;

	private MapElements() {
	} // Required by JAXB

	public MapElements(String key, Long value) {
		this.key = key;
		this.value = value;
	}

}
