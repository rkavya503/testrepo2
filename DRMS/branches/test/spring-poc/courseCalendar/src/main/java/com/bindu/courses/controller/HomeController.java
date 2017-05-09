package com.bindu.courses.controller;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.bindu.courses.dao.CourseDAO;
import com.bindu.courses.entity.Course;

/**
 * Handles requests for the application home page.
 */

@Controller
public class HomeController {
	
	 @Autowired  
	    CourseDAO courseDao;

	@RequestMapping(value = { "/home"}, method = RequestMethod.GET)
	public ModelAndView homePage() {
       
		ModelAndView model = new ModelAndView();		
		model.setViewName("home");
		return model;

	}
	
	@RequestMapping(value = { "/upload"}, method = RequestMethod.GET)
	public ModelAndView uploadPage() {
       
		ModelAndView model = new ModelAndView();		
		model.setViewName("upload");
		return model;

	}
	
	@RequestMapping(value="/savefile",method=RequestMethod.POST)  
	public ModelAndView upload(@RequestParam CommonsMultipartFile file,HttpSession session){  
	        String path=session.getServletContext().getRealPath("/");  
	        String filename=file.getOriginalFilename();  
	          
	        System.out.println(path+" "+filename);  
	        try{  
	        byte barr[]=file.getBytes();  
	          
	        BufferedOutputStream bout=new BufferedOutputStream(  
	                 new FileOutputStream(path+"/"+filename));  
	        bout.write(barr);  
	        bout.flush();  
	        bout.close();  
	          
	        }catch(Exception e){
	        	System.out.println(e);
	        	 return new ModelAndView("failure","filename",path+"/"+filename); 
	        	}  
	       // return new ModelAndView("addtrainer","filename",path+"/"+filename);  
	        return new ModelAndView("redirect:/addtrainer");
	    }  
	
}