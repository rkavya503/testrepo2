package com.bindu.courses.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.bindu.courses.entity.Trainer;

@Repository
public class TrainerDAO  {
	
	@Autowired 
	private HibernateTemplate hibernateTemplate;

	public void saveTrainer(Trainer trainer){
		hibernateTemplate.saveOrUpdate(trainer);
		System.out.println("Course added sucessfully");
	}
	
	public List<?> gettrainerByCourseName(String courseName){  
		return hibernateTemplate.loadAll(Trainer.class);
	}
	
	public List<?> getTrainerByMobileNumber(int mobileNumber){  
			
			return hibernateTemplate.find("from trainer where mobilenumber in (?)",mobileNumber );
	}

	public void delete(int mobilenumber) {
		
		Trainer trainer = new Trainer();
		trainer.setMobilenumber(mobilenumber);
		hibernateTemplate.delete(trainer);
		System.out.println("Trainer deleted sucessfully");
	}

}
