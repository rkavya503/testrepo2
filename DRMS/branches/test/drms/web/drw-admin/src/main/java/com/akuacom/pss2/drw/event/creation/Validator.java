//package com.akuacom.pss2.drw.event.creation;
//
//import java.io.Serializable;
//
//public abstract class Validator implements Serializable {
//	
//	public static final int MSG_INFO  = 0;
//	public static final int MSG_WARN  = 1;
//	public static final int MSG_ERROR = 2;
//	
//	private static final long serialVersionUID = -5531328058885073020L;
//
//	public static class MSG {
//		public int type;
//		public String body;
//		public MSG(int type, String body) {
//			super();
//			this.type = type;
//			this.body = body;
//		}
//	}
//	
//	public abstract MSG validate(AbstractEventCreation model);
//}
