package edu.flightregistration.backendapplication.config;

import edu.flightregistration.rest.controllers.TerminalController;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"edu.flightregistration.rest"})
public class PresentationConfig {
}
