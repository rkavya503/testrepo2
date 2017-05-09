package com.bindu.courses.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bindu.courses.dao.TrainerDAO;
import com.bindu.courses.entity.Trainer;

@Controller
public class TrainerController {
	

	 @Autowired  
	    TrainerDAO trainerDao;
	 
		
		/*@RequestMapping(value = { "/searchtrainer"}, method = RequestMethod.GET)
		public ModelAndView searchPage() {
	       
			return new ModelAndView("searchtrainer","command",new Trainer());

		}
		*/
		@RequestMapping(value = { "/addtrainer"}, method = RequestMethod.GET)
		public ModelAndView addPage() {
			
			return new ModelAndView("addtrainer","command",new Trainer());

		}
		
		 @RequestMapping(value="/savetrainer",method = RequestMethod.POST)  
		    public ModelAndView save(@ModelAttribute("trainer") Trainer trainer){  
			 trainerDao.saveTrainer(trainer);  
			 	
			 	ModelAndView model = new ModelAndView();		
				model.setViewName("sucess");
				return model;
		    }  
		 

		 @RequestMapping(value="/searchtrainer",method = RequestMethod.POST)  
		    public ModelAndView searchTrainer(@ModelAttribute("trainer") Trainer trainer){  
			 	List<?> trainers = trainerDao.gettrainerByCourseName(trainer.getSubject());  
			 	 return new ModelAndView("viewtrainer","trainers",trainers); 
		    } 
		 
		 @RequestMapping(value="/delettrainer/{mobilenumber}",method = RequestMethod.GET)  
		    public ModelAndView delete(@PathVariable int mobilenumber){  
			 trainerDao.delete(mobilenumber);  
			 return new ModelAndView("redirect:/addtrainer"); 
		    } 
		 @RequestMapping(value="/downloadresume/{subject}",method = RequestMethod.GET)  
		    public void downloadResume(@PathVariable int subject){  
			 //trainerDao.delete(subject);  
			// return new ModelAndView("redirect:/addtrainer"); 
		    } 
		 
}
