package com.ead.course.dtos;

import java.util.UUID;

import lombok.Data;

@Data
public class CourseUserDto {
	
	private UUID courseId;
	private UUID userId;

}
