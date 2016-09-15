// $Revision: 1.1 $ $Date: 2000/01/01 00:00:00 $
package com.akuacom.pss2.richsite.event;

import java.io.Serializable;

public class BidModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String timeBlock;
	private double reduction;
	private boolean editable;
	
	public String getTimeBlock()
	{
		return timeBlock;
	}
	public void setTimeBlock(String timeBlock)
	{
		this.timeBlock = timeBlock;
	}
	public double getReduction()
	{
		return reduction;
	}
	public void setReduction(double reduction)
	{
		this.reduction = reduction;
	}
	public boolean isEditable()
	{
		return editable;
	}
	public void setEditable(boolean editable)
	{
		this.editable = editable;
	}
	
	
}
