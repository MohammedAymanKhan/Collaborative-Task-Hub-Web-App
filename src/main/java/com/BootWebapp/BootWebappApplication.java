package com.BootWebapp;

import com.BootWebapp.Controller.ProjectReportController;
import com.BootWebapp.Model.ProjectReport;
import com.BootWebapp.Services.ProjectReportServices;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootApplication
public class BootWebappApplication {



	public static void main(String[] args) {

		ConfigurableApplicationContext applicationContext=SpringApplication.run(BootWebappApplication.class, args);

	}


}
