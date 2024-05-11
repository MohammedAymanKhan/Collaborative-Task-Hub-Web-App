package com.BootWebapp.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.BootWebapp.Model.User;

import java.util.List;

@Repository
public class UserRepository {

	private static final String insert="INSERT INTO User VALUES(?,?,?);";
	private static final String selectByName = "SELECT * FROM USER WHERE name = ?";
	private static final String selectByEmail = "SELECT name,email FROM user WHERE email = ?";

	private JdbcTemplate userConn;

	@Autowired
	public void setUserConn(JdbcTemplate userConn) {
		this.userConn = userConn;
	}

	public boolean addUser(User user) {

		int row=userConn.update(insert,user.getName(),user.getEmail(),user.getPassword());
		return row==1;

	}
	
	public boolean userExists(String email) {

		User user=userConn.query(selectByEmail,(res)->{
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

            return userConn.queryForObject(selectByEmail, (res, rowNum)->
					new User(res.getString("name"),res.getString("email")), email);

		}catch(DataAccessException e){
			return null;
		}

    }

	public List<User> getUserByName(String name){

		try{

			return userConn.query(selectByName, (rs,rowNum)->
				 new User(rs.getString("name"), rs.getString("email")), name);

		} catch (DataAccessException e) {
			System.out.println("Error executing query: " + e.getMessage());
			return null;
		}

	}

}
