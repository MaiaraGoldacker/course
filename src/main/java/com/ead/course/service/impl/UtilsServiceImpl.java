package com.ead.course.service.impl;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ead.course.service.UtilsService;

@Service
public class UtilsServiceImpl implements UtilsService {
	
	String REQUEST_URI = "http://localhost:8087";
	
	public String createURL(UUID courseId, Pageable pageable) {
		return REQUEST_URI + "/users?courseId=" + courseId + "&page=" + pageable.getPageNumber() + "&size" 
				+ pageable.getPageSize() + "&sort" + pageable.getSort().toString().replaceAll(": ", ",");
	}

}
