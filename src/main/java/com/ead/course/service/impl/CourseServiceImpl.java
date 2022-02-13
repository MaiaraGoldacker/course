package com.ead.course.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ead.course.dtos.NotificationCommandDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.models.UserModel;
import com.ead.course.publisher.NotificationCommandPublisher;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.UserRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.service.CourseService;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

@Log4j2
@Service
public class CourseServiceImpl implements CourseService {

	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	ModuleRepository moduleRepository;
	
	@Autowired
	LessonRepository lessonRepository;
	
	@Autowired
	UserRepository courseUserRepository;
	
	@Autowired
	NotificationCommandPublisher notificationCommandPublisher;
	
	@Transactional	
	@Override
	public void delete(CourseModel courseModel) {
	
		List<ModuleModel> moduleModelList = moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());
		
		if (!moduleModelList.isEmpty()) {
			for (ModuleModel module : moduleModelList) {
				List<LessonModel> lessonModelList = lessonRepository.findAllLessonsIntoModule(module.getModuleId());
				if (!lessonModelList.isEmpty()) {
					lessonRepository.deleteAll(lessonModelList);
				}
			}	
			moduleRepository.deleteAll(moduleModelList);
		}
		courseRepository.deleteCourseUserByCourse(courseModel.getCourseId());
		courseRepository.delete(courseModel);
	}

	@Override
	public CourseModel save(CourseModel courseModel) {
		return courseRepository.save(courseModel);
	}

	@Override
	public Optional<CourseModel> findById(UUID courseId) {
		return courseRepository.findById(courseId);
	}

	@Override
	public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {
		return courseRepository.findAll(spec, pageable);
	}

	@Override
	public boolean existsByCourseAndUser(UUID courseId, UUID userId) {
		return courseRepository.existsByCourseAndUser(courseId, userId);
	}

	@Transactional
	@Override
	public void saveSubscriptionUserInCourse(UUID courseId, UUID userId) {
		courseRepository.saveCourseUser(courseId, userId);		
	}
	
	@Transactional
	@Override
	public void saveSubscriptionUserInCourseAndSendNotification(CourseModel course, UserModel user) {
		courseRepository.saveCourseUser(course.getCourseId(), user.getUserId());
		
		try {
			var notificationCommandDto = new NotificationCommandDto();
			notificationCommandDto.setTitle("Bem vindo ao curso!");
			notificationCommandDto.setMessage("Inscrição realizada com sucesso!");
			notificationCommandDto.setUserId(user.getUserId());
			notificationCommandPublisher.publishNotificationCommand(notificationCommandDto);
		} catch(Exception e) {
			log.warn("Error sanding notification!");
		}
	}
}
