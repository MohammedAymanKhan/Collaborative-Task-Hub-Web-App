package com.BootWebapp.Controller;

import com.BootWebapp.DAO.UserDetailsService;
import com.BootWebapp.Model.Project;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
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
	private final CsrfTokenRepository csrfTokenRepository;

	@Autowired
	public UserDetailsController(UserDetailsService service,CsrfTokenRepository csrfTokenRepository) {
		this.service = service;
		this.csrfTokenRepository = csrfTokenRepository;
	}
	@GetMapping("/CSRF/getToken")
	public @ResponseBody String getCsrfToken(HttpServletRequest request, HttpServletResponse response){
		CsrfToken token = csrfTokenRepository.generateToken(request);
		csrfTokenRepository.saveToken(token,request,response);
		System.out.println("called csrf token:");
		return token.getToken();
	}

	@GetMapping("/myLogin")
	public String getLoginPage(){
		return "redirect:/login.html";
	}

	@GetMapping(path = "/taskHub")
	public String redirectTaskHub(HttpSession session){
		Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		session.setAttribute("user", user);
		return "redirect:/TaskHub.html";
	}

	@PostMapping(path = "/failed/login")
	public @ResponseBody String falidToLogin(){
		return "<h1>Falid to login</h1>";
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










