package com.akuacom.utils.aspects;

public abstract aspect Common {
	public pointcut akuacomScope() :
	    within(com.akuacom..*)
	    && !within(Common+);
	public pointcut akuacomScopeWithAspects() :
	    within(com.akuacom..*);
}