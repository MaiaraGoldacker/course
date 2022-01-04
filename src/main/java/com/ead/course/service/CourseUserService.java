package com.ead.course.service;

import java.util.UUID;

import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;

public interface CourseUserService {

	boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId);

	CourseUserModel save(CourseUserModel courseUserModel);

	CourseUserModel saveAndSendSubsctiptionUserInCourse(CourseUserModel courseUserModel);

}
