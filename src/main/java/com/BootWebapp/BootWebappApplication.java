package com.BootWebapp;

import com.BootWebapp.Controller.InviteUsersController;
import com.BootWebapp.Controller.ProjectController;
import com.BootWebapp.DAO.ProjectsRepository;
import com.BootWebapp.DAO.UserRepository;
import com.BootWebapp.DAO.UserSearchRepository;
import com.BootWebapp.Model.Project;
import com.BootWebapp.Model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;


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
