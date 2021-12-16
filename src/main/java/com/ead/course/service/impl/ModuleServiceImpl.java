package com.ead.course.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.service.ModuleService;

@Service
public class ModuleServiceImpl implements ModuleService{

	@Autowired
	ModuleRepository moduleRepository;
}
