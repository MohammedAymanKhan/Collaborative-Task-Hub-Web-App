package com.BootWebapp.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.BootWebapp.Model.User;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class UserRepository {

	private static final String NEW_USER = "INSERT INTO users('email', 'password', 'first_name', 'last_name') VALUES(?,?,?,?)";
	private static final String USER_BY_EMAIL = "SELECT user_id, first_name, last_name, email FROM users WHERE email = ?";

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public UserRepository(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}

	public boolean addUser(User user) {

		int row=jdbcTemplate.update(NEW_USER,user.getEmail(),
											user.getPassword(),
											user.getFirst_name(),
											user.getLast_name());
		return row==1;

	}
	
	public User userExistsByEmail(String email) {

		User user=jdbcTemplate.query(USER_BY_EMAIL,(res)->{
			if(res.next()){
				return new User(res.getInt("user_id"),
								res.getString("first_name"),
								res.getString("last_name"),
								res.getString("email"));
			}else{
				return null;
			}
		},email);

		return user;
	}

}
