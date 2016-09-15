package com.akuacom.pss2.timer;

import java.util.Collection;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Timer;

import org.apache.log4j.Logger;

public abstract class TimerManagerBean {

	public String getTimersInfo(Collection timers) {
		StringBuffer sb = new StringBuffer();
		// Timers that this bean manages
		for (Object timer : timers) {
			if (timer instanceof Timer) {

				sb.append(((Timer) timer).getClass() + " Timer ["
						+ ((Timer) timer).getInfo().toString() + "] next run ["
						+ ((Timer) timer).getNextTimeout() + "]\n");

			}
		}
		return sb.toString();
	}

	public void cancelTimers(Collection timers) {

		for (Object timer : timers) {
			if (timer instanceof Timer) {
				Logger.getLogger(this.getClass()).debug(
						"" + ((Timer) timer).getInfo() + " was [Stopped].");
				((Timer) timer).cancel();

			}
		}
	}

}
