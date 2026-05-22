package com.pulse_gym.ms_operation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@EnableJpaRepositories(basePackages = {"com.pulse_gym.lb_common.repository"})
@EntityScan(basePackages = {"com.pulse_gym.lb_common.entity"})

@SpringBootApplication
public class MsOperationApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsOperationApplication.class, args);
	}

}
