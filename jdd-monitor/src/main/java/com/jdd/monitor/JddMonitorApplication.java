package com.jdd.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;


@SpringBootApplication
@Configuration
@EnableAdminServer
@EnableDiscoveryClient
public class JddMonitorApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(JddMonitorApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(JddMonitorApplication.class);
    }
}
