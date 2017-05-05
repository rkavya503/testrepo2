package com.bindu.courses.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.bindu.courses.entity.Course;

@Repository
public class CourseDAO  {
	
	@Autowired 
	private HibernateTemplate hibernateTemplate;

	public void saveCourse(Course course){
		hibernateTemplate.saveOrUpdate(course);
		System.out.println("Course added sucessfully");
	}
	
	public List<?> getCourseByName(String courseName){  
		
			return hibernateTemplate.find("from Course where name in (?)",courseName );
	}
	
	public List<?> getCourseById(int id){  
			
			return hibernateTemplate.find("from Course where id in (?)",id );
	}

	public void delete(int id) {
		
		Course course = new Course();
		course.setId(id);
		hibernateTemplate.delete(course);
		System.out.println("Course deleted sucessfully");
	}

	public Course edit(int id) {
		return hibernateTemplate.get(Course.class, id);
	}

	/*public void update(Course course) {
		// TODO Auto-generated method stub
		
	}  */
}
