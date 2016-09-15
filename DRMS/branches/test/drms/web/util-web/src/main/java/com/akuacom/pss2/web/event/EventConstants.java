package com.akuacom.pss2.web.event;

public interface EventConstants {
	
	public static final String XML_SCHEMA_VERSION="1.0";
	
	public enum MODEL_LEVELS {
		
		NORMAL(1.0,"Normal"),
		MODERATE(2.0,"Moderate"),
		HIGH(3.0,"High");
		
		private final double value;
		private final String label;
		private MODEL_LEVELS(double value,String label){
			this.value = value;
			this.label = label;
		}
		public double getValue() {
			return value;
		}
		public String getLabel() {
			return label;
		}
	}
	
	public static final String SIGNAL_MODE="mode";
	public static final String SIGNAL_MODE_OPENADR="OperationModeValue";
	public static final String SIGNAL_PENDDING="pending";
}
