package com.ead.course.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.service.CourseUserService;

@Service
public class CourseUserServiceImpl implements CourseUserService {

	@Autowired
	CourseUserRepository courseUserRepository;
}
