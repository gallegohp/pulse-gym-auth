package com.pulse_gym.ms_notifications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = "com.pulse_gym")

@EntityScan("com.pulse_gym.lb_common.entity.notification")

public class MsNotificationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsNotificationsApplication.class, args);
	}

}
