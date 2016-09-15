package com.honeywell.dras.ssm.api.response;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GetProgramResponse {
	private List<String> programList;

	public List<String> getProgramList() {
		return programList;
	}

	public void setProgramList(List<String> programList) {
		this.programList = programList;
	}

}
