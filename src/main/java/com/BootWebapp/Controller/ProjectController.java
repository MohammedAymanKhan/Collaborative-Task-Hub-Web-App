package com.BootWebapp.Controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.BootWebapp.Model.User;
import com.BootWebapp.Model.Project;

import com.BootWebapp.DAO.ProjectsRepository;


@Controller
@RequestMapping(path="/project",produces = "application/json")
@ResponseBody
public class ProjectController {

	private final ProjectsRepository projRep;

	@Autowired
	public ProjectController(ProjectsRepository projRep) {
		this.projRep = projRep;
	}

	@GetMapping("/projects")
	public List<Project> getProjects(@SessionAttribute User user) {

		List<Project> projects = projRep.getProjects(user.getUser_id());
		return projects!=null ? projects : null;

	}
	
	@PostMapping("/addProject")
	public  List<Project> createNewProject(@RequestBody Project project, @SessionAttribute User user) {

		project.setCratedBy(user.getUser_id());

		boolean flag = projRep.addProject(project);
		project.setCratedBy(null);

		return flag ? List.of(project) : null;

	}

	

}
