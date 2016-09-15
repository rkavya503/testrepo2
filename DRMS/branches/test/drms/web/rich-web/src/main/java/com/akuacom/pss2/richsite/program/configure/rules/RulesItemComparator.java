// $Revision: 1.1 $ $Date: 2010-03-02 04:18:16 $
package com.akuacom.pss2.richsite.program.configure.rules;

import java.util.Comparator;

public class RulesItemComparator  implements Comparator<RulesViewItem>
{
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(RulesViewItem o1, RulesViewItem o2)
	{
		return o1.getRule().getSortOrder().compareTo(o2.getRule().getSortOrder());
	}	
}
