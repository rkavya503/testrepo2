package com.honeywell.dras.ssm.api.request;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GetProgramsRequest {

		private String utilityName;

		public String getUtilityName() {
			return utilityName;
		}

		public void setUtilityName(String utilityName) {
			this.utilityName = utilityName;
		}
		
		
}
