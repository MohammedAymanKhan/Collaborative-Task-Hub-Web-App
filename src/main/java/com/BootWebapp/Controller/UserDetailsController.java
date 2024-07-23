package com.BootWebapp.Controller;

import com.BootWebapp.DAO.UserDetailsService;
import com.BootWebapp.Model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.BootWebapp.Model.User;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/user")
public class UserDetailsController {
	
	private final UserDetailsService service;

	@Autowired
	public UserDetailsController(UserDetailsService service) {
		this.service = service;
	}
	
	@PostMapping(path = "/login")
	public String login(@ModelAttribute User user, BindingResult bindingResult, HttpSession session) {


		if(!bindingResult.hasErrors())
			user = service.existsUser(user.getEmail());

		if(user != null) {
			session.setAttribute("user", user);
			return "redirect:/TaskHub.html";
		}

		return "redirect:/index.html";
	}

	@PostMapping("/invite/{toUserId}/{projectId}")
	public ResponseEntity<String> inviteUser(@PathVariable("toUserId") Integer to_Id,
			@PathVariable("projectId") Integer projId, @SessionAttribute("user") User user){

		boolean flag = service.inviteUserToProject(user.getUser_id(), to_Id, projId);
		return flag ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

	}

	@GetMapping("/notification")
	public ResponseEntity<Object> getUserInviteNotification(@SessionAttribute("user") User user){

		List<Project> projectList = service.getNotifications(user.getUser_id());
		return projectList != null ? ResponseEntity.ok(projectList) : ResponseEntity.notFound().build();

	}

	@PatchMapping("/updateInviteStatus/{projId}/{status}")
	public ResponseEntity<Object> updateStatusInviteNotification(@PathVariable("projId") int projId,
											@PathVariable("status") boolean status,@SessionAttribute("user") User user){
		return service.updateStatusInvite(user.getUser_id(), projId, status) ?
		ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

}










