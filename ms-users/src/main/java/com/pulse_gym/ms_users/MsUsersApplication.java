package com.pulse_gym.ms_users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.pulse_gym.ms_users", "com.pulse_gym.lb_common"})
public class MsUsersApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsUsersApplication.class, args);
    }
}
