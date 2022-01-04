package com.ead.course.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ead.course.service.UtilsService;

@Service
public class UtilsServiceImpl implements UtilsService {

	public String createURLGetAllUsersByCourse(UUID courseId, Pageable pageable) {
		return "/users?courseId=" + courseId + "&page=" + pageable.getPageNumber() + "&size" 
				+ pageable.getPageSize() + "&sort" + pageable.getSort().toString().replaceAll(": ", ",");
	}

}
