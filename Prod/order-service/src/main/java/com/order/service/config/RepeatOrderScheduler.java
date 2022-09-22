package com.order.service.config;

import java.time.LocalDate;

import com.order.service.service.RepeatOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name="scheduler.enabled",matchIfMissing=true)
public class RepeatOrderScheduler {
	
	@Autowired
	RepeatOrderService repeatOrderService;
	
	@Scheduled(cron = "0 0 13 * * *", zone = "GMT+5:30")
	public void sendNotification() {
		LocalDate date=LocalDate.now();
		repeatOrderService.getOptInToSendNotification(date);

	}

}
