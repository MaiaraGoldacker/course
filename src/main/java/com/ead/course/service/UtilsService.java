package com.ead.course.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

public interface UtilsService {
	
	 String createURLGetAllUsersByCourse(UUID userId, Pageable pageable);

}
