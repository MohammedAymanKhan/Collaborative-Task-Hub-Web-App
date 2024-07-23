package com.BootWebapp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class BootWebappApplication {



	public static void main(String[] args) {

		ConfigurableApplicationContext applicationContext=SpringApplication.run(BootWebappApplication.class, args);

//		ProjectController object = applicationContext.getBean(ProjectController.class);
//
//		List<Project> project = object.createNewProject(new Project("xyz",1),new User(1,"a","b"));
//		System.out.println(project.get(0));


	}


}
