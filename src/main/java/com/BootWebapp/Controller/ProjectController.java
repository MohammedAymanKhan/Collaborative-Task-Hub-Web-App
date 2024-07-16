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
	public Map<String,Object> getProjects(@SessionAttribute User user) {

		Map<String,Object> map = new HashMap<>();

		List<Project> projects = null;
		projects=projRep.getProjects(user.getEmail());

		if(projects!=null) {
			map.put("userName", user.getName());
			map.put("projects", projects);
			return map;
		}

		return null;
	}
	
	@PostMapping("/addProject")
	public  Project createNewProject(@RequestBody Project project,@SessionAttribute User user) {

		project.setCratedByEmail(user.getEmail());
		Boolean flag=projRep.addProject(project);

		if(flag) return project;
		return null;

	}

}
