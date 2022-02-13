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

import com.ead.course.dtos.LessonDto;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.service.LessonService;
import com.ead.course.service.ModuleService;
import com.ead.course.specification.SpecificationTemplate;

@RestController
@CrossOrigin(origins = "*", maxAge=3600)
public class LessonController {

	@Autowired
	LessonService lessonService;
	
	@Autowired
	ModuleService moduleService;
	
	@PreAuthorize("hasAnyRole('INSTRUCTOR')")
	@PostMapping("/modules/{moduleId}/lessons")
	public ResponseEntity<Object> saveLesson(@RequestBody @Valid LessonDto lessonDto,
											 @PathVariable UUID moduleId) {
		
		Optional<ModuleModel> moduleModelOptional= moduleService.findById(moduleId);
		
		if (moduleModelOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found.");
		}
		
		var lessonModel = new LessonModel();
		BeanUtils.copyProperties(lessonDto, lessonModel);
		lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
		lessonModel.setModule(moduleModelOptional.get());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.save(lessonModel));
	}
	
	@PreAuthorize("hasAnyRole('INSTRUCTOR')")
	@DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
	public ResponseEntity<Object> deleteLesson(@PathVariable(value="lessonId") UUID lessonId,
											   @PathVariable(value="moduleId") UUID moduleId) {
		Optional<LessonModel> lessonModelOptional= lessonService.findLessonIntomodule(moduleId, lessonId);
		
		if (lessonModelOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson Not Found.");
		}
	
		lessonService.delete(lessonModelOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body("Lesson Deleted Successfully.");
	}

	@PreAuthorize("hasAnyRole('INSTRUCTOR')")
	@PutMapping("/modules/{moduleId}/lessons/{lessonId}")
	public ResponseEntity<Object> updateModule(@PathVariable(value="lessonId") UUID lessonId,
			   								   @PathVariable(value="moduleId") UUID moduleId,
			   								   @RequestBody @Valid LessonDto lessonDto) {
		
		Optional<LessonModel> lessonModelOptional= lessonService.findLessonIntomodule(moduleId, lessonId);
		
		if (lessonModelOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson Not Found.");
		}
		
		var lessonModel = lessonModelOptional.get();
		lessonModel.setDescription(lessonDto.getDescription());
		lessonModel.setTitle(lessonDto.getTitle());
		lessonModel.setVideoUrl(lessonDto.getVideoUrl());
		return ResponseEntity.status(HttpStatus.OK).body(lessonService.save(lessonModel));
		
	}
	
	@PreAuthorize("hasAnyRole('STUDENT')")
	@GetMapping("/modules/{moduleId}/lessons/")
	public ResponseEntity<Page<LessonModel>> getAllLessonsByModules(@PathVariable(value="moduleId") UUID moduleId,
			  														SpecificationTemplate.LessonSpec spec, 
			  														@PageableDefault(page = 0, size = 10, sort = "lessonId", direction = Sort.Direction.ASC) Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(lessonService.findAllByModule(SpecificationTemplate.lessonModuleId(moduleId).and(spec), pageable));
	}
	
	@PreAuthorize("hasAnyRole('STUDENT')")
	@GetMapping("/modules/{moduleId}/lessons/{lessonId}")
	public ResponseEntity<Object> getOneLesson(@PathVariable(value="lessonId") UUID lessonId,
											   @PathVariable(value="moduleId") UUID moduleId){
		
		Optional<LessonModel> lessonModelOptional= lessonService.findLessonIntomodule(moduleId, lessonId);
		
		if (lessonModelOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson Not Found.");
		}
		return ResponseEntity.status(HttpStatus.OK).body(lessonModelOptional.get());
	}
}
