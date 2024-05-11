package com.BootWebapp.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.BootWebapp.Model.User;
import com.BootWebapp.DAO.ProjectsRepository;

import jakarta.servlet.http.HttpSession;

import com.BootWebapp.Model.Project;
import com.BootWebapp.Model.ProjectReport;


@Controller
@RequestMapping(path="/project",produces = "application/json")
@ResponseBody
public class ProjectController {

	ProjectsRepository projRep;

	@Autowired
	public void setProjRep(ProjectsRepository projRep) {
		this.projRep = projRep;
	}

	@GetMapping("/projects")
	public Map<String,Object> getProjects(@SessionAttribute User user) {

		Map<String,Object> map=new HashMap<>();

		List<Project> projects=null;
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
