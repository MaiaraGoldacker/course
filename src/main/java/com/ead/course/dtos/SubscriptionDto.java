package com.ead.course.dtos;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptionDto {

	@NotNull
	private UUID userId;	
}
