package com.akuacom.pss2.task;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.utils.DateUtil;

@Stateless
public class RoutineStatusEAOBean extends RoutineStatusGenEAOBean implements RoutineStatusEAO.L,RoutineStatusEAO.R {

	@Override
	public RoutineStatus getRoutineStatus(String routine, Date date) {
		List<RoutineStatus> statuses =findByNameAndDate(routine,DateUtil.stripTime(date));
		RoutineStatus status = new RoutineStatus();
		if(statuses.size()!=0){
			status = statuses.get(0);
		}else{
			status.setName(routine);
			status.setDate(date);
		}
		return status;
	}
	
	@Override
	public RoutineStatus insertOrUpdate(RoutineStatus status) {
		if(status.getUUID()==null){
			try {
				return this.create(status);
			} catch (DuplicateKeyException e) {}
		}else{
			try {
				return update(status);
			} catch (EntityNotFoundException e) {
			}
		}
		return null;
	}
	
	
}
