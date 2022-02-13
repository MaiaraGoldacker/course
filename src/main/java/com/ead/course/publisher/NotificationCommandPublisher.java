package com.ead.course.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ead.course.dtos.NotificationCommandDto;

@Component
public class NotificationCommandPublisher {

	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Value(value = "${ead.broker.exchange.notificationCommandExchange}")
	private String notificationCommandExchange;
	
	@Value(value = "${ead.broker.key.notificationCommandKey}")
	private String notificationCommandKey;
	
	public void publishNotificationCommand(NotificationCommandDto notificationCommand) {
		rabbitTemplate.convertAndSend(notificationCommandExchange, notificationCommandKey,notificationCommand);
	}
	
}
