package com.bindu.courses.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bindu.courses.dao.CourseDAO;
import com.bindu.courses.entity.Course;

@Controller
public class courseController {
	

	 @Autowired  
	    CourseDAO courseDao;
	 
		
		@RequestMapping(value = { "/search"}, method = RequestMethod.GET)
		public ModelAndView searchPage() {
	       
			return new ModelAndView("search","command",new Course());

		}
		
		@RequestMapping(value = { "/add"}, method = RequestMethod.GET)
		public ModelAndView addPage() {
			
			return new ModelAndView("add","command",new Course());

		}
		
		 @RequestMapping(value="/savecourse",method = RequestMethod.POST)  
		    public ModelAndView save(@ModelAttribute("course") Course course){  
			 	courseDao.saveCourse(course);  
			 	
			 	ModelAndView model = new ModelAndView();		
				model.setViewName("sucess");
				return model;
		    }  
		 

		 @RequestMapping(value="/searchcourse",method = RequestMethod.POST)  
		    public ModelAndView searchCourse(@ModelAttribute("course") Course course){  
			 	List<?> courses = courseDao.getCourseByName(course.getName());  
			 	 return new ModelAndView("viewcourse","courses",courses); 
		    } 
		 
		 @RequestMapping(value="/deletcourse/{id}",method = RequestMethod.GET)  
		    public ModelAndView delete(@PathVariable int id){  
			 courseDao.delete(id);  
//			 List<?> courses = courseDao.getCourseById(id);  
//			  return new ModelAndView("viewcourse","courses",courses); 
			 return new ModelAndView("redirect:/add"); 
		    } 
		 
		 @RequestMapping(value="/editcourse/{name}",method = RequestMethod.GET)  
		    public ModelAndView edit(@PathVariable String name){  
			 Course course=(Course) courseDao.getCourseByName(name).get(0);  
		        return new ModelAndView("editcourse","command",course);  
		    }  
		 
		  @RequestMapping(value="/editsave",method = RequestMethod.POST)  
		    public ModelAndView editsave(@ModelAttribute("course") Course course){  
			  courseDao.saveCourse(course);
			  List<?> courses = courseDao.getCourseByName(course.getName());  
			  return new ModelAndView("viewcourse","courses",courses); 
		    } 
}
