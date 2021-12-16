package com.ead.course.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ead.course.repositories.LessonRepository;
import com.ead.course.service.LessonService;

@Service
public class LessonServiceImpl implements LessonService{

	@Autowired
	LessonRepository lessonRepository;
}
