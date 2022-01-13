package com.ead.course.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.ead.course.dtos.CourseDto;
import com.ead.course.enums.UserType;
import com.ead.course.service.UserService;
import com.ead.course.models.UserModel;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CourseValidator implements Validator {

	@Autowired
	@Qualifier("defaultValidator")
	private Validator validator;
	
	@Autowired
	UserService userService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return false;
	}

	@Override
	public void validate(Object o, Errors errors) {
		CourseDto courseDto = (CourseDto) o; 
		validator.validate(courseDto, errors); //isso é necessário, pq precisamos validar as propriedades que eram validadas pelo @Valid que foi retirado do método
		
		if (!errors.hasErrors()) { //entra quando não houver erros.
			validateUserInstructor(courseDto.getUserInstructor(), errors);
		}
	}
	
	private void validateUserInstructor(UUID userInstructor, Errors errors) {
		Optional<UserModel> userModelOptional = userService.findById(userInstructor);
		
		if (userModelOptional.isEmpty()) {
			errors.rejectValue("userInstructor", "UserInstructorError", "Instructor Not Found.");
		}
		
		if (userModelOptional.get().getUserType().equals(UserType.STUDENT.toString())) {
			errors.rejectValue("userInstructor", "UserInstructorError", "User must be INSTRUCTOR or ADMIN.");
		}	
	}

}
