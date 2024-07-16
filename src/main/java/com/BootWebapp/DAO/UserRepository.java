package com.BootWebapp.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.BootWebapp.Model.User;

import java.util.List;

@Repository
public class UserRepository {

	private static final String NEWUSER = "INSERT INTO User VALUES(?,?,?);";
	private static final String USERBYNAME = "SELECT * FROM USER WHERE name = ?";
	private static final String USERBYEMAIL = "SELECT name,email FROM user WHERE email = ?";

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public UserRepository(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}

	public boolean addUser(User user) {

		int row=jdbcTemplate.update(NEWUSER,user.getName(),user.getEmail(),user.getPassword());
		return row==1;

	}
	
	public boolean userExists(String email) {

		User user=jdbcTemplate.query(USERBYEMAIL,(res)->{
			if(res.next()){
				return new User(res.getString("name"),res.getString("email"));
			}else{
				return null;
			}
		},email);

		return user!=null;
	}

	public User getUserByEmail(String email){

		try {

            return jdbcTemplate.queryForObject(USERBYEMAIL, (res, rowNum)->
					new User(res.getString("name"),res.getString("email")), email);

		}catch(DataAccessException e){
			return null;
		}

    }

	public List<User> getUserByName(String name){

		try{

			return jdbcTemplate.query(USERBYNAME, (rs,rowNum)->
				 new User(rs.getString("name"), rs.getString("email")), name);

		} catch (DataAccessException e) {
			System.out.println("Error executing query: " + e.getMessage());
			return null;
		}

	}

}
