// $Revision: 1.1 $ $Date: 2010-03-02 04:18:16 $
package com.akuacom.pss2.facdash;

import java.util.Comparator;

public class JSFRuleComparator  implements Comparator<JSFRule>
{
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(JSFRule o1, JSFRule o2)
	{
		return o1.getRule().getSortOrder().compareTo(o2.getRule().getSortOrder());
	}	
}
