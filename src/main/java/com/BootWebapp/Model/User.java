package com.BootWebapp.Model;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class User implements UserDetails{

	private Integer user_id;
	private String first_name;
	private String last_name;
	private String email;
	private String password;
	private UserDetails userDetails;

	private class UserDetails {

		private List<String> techStacks;
		private HashMap<String,String> gitHubProjects;

		private UserDetails(List<String> techStacks, HashMap<String,String> gitHubProjects){
			this.techStacks = techStacks;
			this.gitHubProjects = gitHubProjects;
		}

		public List<String> getTechStacks() {
			return techStacks;
		}

		public void setTechStacks(List<String> techStacks) {
			this.techStacks = techStacks;
		}

		public HashMap<String, String> getGitHubProjects() {
			return gitHubProjects;
		}

		public void setGitHubProjects(HashMap<String, String> gitHubProjects) {
			this.gitHubProjects = gitHubProjects;
		}

		@Override
		public String toString() {
			return "UserDetails{" +
					"techStacks=" + techStacks +
					", gitHubProjects=" + gitHubProjects +
					'}';
		}
	}

	public User() {}

	public User(Integer user_id, String first_name, String last_name, String email, String password) {
		this.user_id = user_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.password = password;
	}

	public User(Integer user_id , String first_name , String last_name){
		this.user_id = user_id;
		this.first_name = first_name;
		this.last_name = last_name;
	}

	public User(int user_id,String first_name, String last_name, String email) {
		this.user_id = user_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority("USER"));
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(List<String> techStacks, HashMap<String,String> gitProjects) {
		this.userDetails = new UserDetails(techStacks,gitProjects);
	}

	@Override
	public String toString() {
		return "User{" +
				"user_id=" + user_id +
				", first_name='" + first_name + '\'' +
				", last_name='" + last_name + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", userDetails=" + userDetails +
				'}';
	}
}
