package com.ead.course.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ead.course.dtos.ModuleDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.service.CourseService;
import com.ead.course.service.ModuleService;
import com.ead.course.specification.SpecificationTemplate;

@RestController
@CrossOrigin(origins = "*", maxAge=3600)
public class ModuleController {
	
	@Autowired
	ModuleService moduleService;
	
	@Autowired
	CourseService courseService;
	
	@PreAuthorize("hasAnyRole('INSTRUCTOR')")
	@PostMapping("/courses/{courseId}/modules")
	public ResponseEntity<Object> saveModule(@RequestBody @Valid ModuleDto moduleDto,
											 @PathVariable UUID courseId){
		
		Optional<CourseModel> courseModelOptional= courseService.findById(courseId);
		
		if (courseModelOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found.");
		}
		
		var moduleModel = new ModuleModel();
		BeanUtils.copyProperties(moduleDto, moduleModel);
		moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
		moduleModel.setCourse(courseModelOptional.get());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.save(moduleModel));
	}
	
	@PreAuthorize("hasAnyRole('INSTRUCTOR')")
	@DeleteMapping("/courses/{courseId}/modules/{moduleId}")
	public ResponseEntity<Object> deleteCourse(@PathVariable(value="courseId") UUID courseId,
											   @PathVariable(value="moduleId") UUID moduleId) {
		Optional<ModuleModel> moduleModelOptional= moduleService.findModuleIntoCourse(courseId, moduleId);
		
		if (moduleModelOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found.");
		}
		
		
		moduleService.delete(moduleModelOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body("Module Deleted Successfully.");
	}

	@PreAuthorize("hasAnyRole('INSTRUCTOR')")
	@PutMapping("/courses/{courseId}/modules/{moduleId}")
	public ResponseEntity<Object> updateModule(@PathVariable(value="courseId") UUID courseId,
											   @PathVariable(value="moduleId") UUID moduleId,
											   @RequestBody @Valid ModuleDto moduleDto) {
		
		Optional<ModuleModel> moduleModelOptional= moduleService.findModuleIntoCourse(courseId, moduleId);
		
		if (moduleModelOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found.");
		}
		
		var moduleModel = moduleModelOptional.get();
		moduleModel.setDescription(moduleDto.getDescription());
		moduleModel.setTitle(moduleDto.getTitle());
		
		return ResponseEntity.status(HttpStatus.OK).body(moduleService.save(moduleModel));
		
	}
	
	@PreAuthorize("hasAnyRole('STUDENT')")
	@GetMapping("/courses/{courseId}/modules/")
	public ResponseEntity<Page<ModuleModel>> getAllModulesByCourse(@PathVariable(value="courseId") UUID courseId,
																   SpecificationTemplate.ModuleSpec spec, 
																   @PageableDefault(page = 0, size = 10, sort = "moduleId", direction = Sort.Direction.ASC) Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(moduleService.findAllByCourse(SpecificationTemplate.moduleCourseId(courseId).and(spec), pageable));
	}
	
	@PreAuthorize("hasAnyRole('STUDENT')")
	@GetMapping("/courses/{courseId}/modules/{moduleId}")
	public ResponseEntity<Object> getOneModule(@PathVariable(value="courseId") UUID courseId,
											   @PathVariable(value="moduleId") UUID moduleId){
		
		Optional<ModuleModel> moduleModelOptional= moduleService.findModuleIntoCourse(courseId, moduleId);
		
		if (moduleModelOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found.");
		}
		return ResponseEntity.status(HttpStatus.OK).body(moduleModelOptional.get());
	}
}
