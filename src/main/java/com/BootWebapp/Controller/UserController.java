package com.BootWebapp.Controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.BootWebapp.Model.User;
import com.BootWebapp.DAO.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	
	private final UserRepository userRep;

	@Autowired
	public UserController(UserRepository userRep) {
		this.userRep = userRep;
	}
	
	@PostMapping(path = "/login")
	public String login(@ModelAttribute User user, BindingResult bindingResult, HttpSession session, HttpServletResponse response) {


		if(!bindingResult.hasErrors())
			user = userRep.userExistsByEmail(user.getEmail());

		if(user != null) {

			session.setAttribute("user", user);

			Cookie userCookie = new Cookie("user_id", user.getUser_id().toString());
			userCookie.setPath("/");
			userCookie.setSecure(true);
			response.addCookie(userCookie);

			return "redirect:/TaskHub.html";

		}

		return "redirect:/index.html";
	}

	@RequestMapping(path="/invalidLogin")
	private ResponseEntity<String> invalidUser(){
		String msg="invalid User";
		return ResponseEntity.badRequest().body(msg);
	}

}










