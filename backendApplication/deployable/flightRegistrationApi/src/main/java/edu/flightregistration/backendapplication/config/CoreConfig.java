package edu.flightregistration.backendapplication.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan(basePackages = {"edu.flightregistration.core"})
@EntityScan(basePackages = "edu.flightregistration.core.jpa.entity")
@EnableJpaRepositories("edu.flightregistration.core.jpa.repository")
@EnableScheduling
public class CoreConfig {
}
