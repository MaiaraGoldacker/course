package com.ead.course.dtos;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationCommandDto {

	private String title;
	private String message;
	private UUID userId;
}
