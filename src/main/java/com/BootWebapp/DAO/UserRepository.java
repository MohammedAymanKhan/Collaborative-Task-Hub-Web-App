package com.BootWebapp.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.BootWebapp.Model.User;
import com.BootWebapp.Model.Project;

import java.util.List;

@Repository
public class UserRepository {

	private static final String NEW_USER = "INSERT INTO users('email', 'password', 'first_name', 'last_name') VALUES(?,?,?,?)";
	private static final String USER_BY_EMAIL = "SELECT user_id, first_name, last_name, email FROM users WHERE email = ?";
	private static final String INVITE_USER = "INSERT INTO inivitetoprojects(from_id, to_id, projId) VALUES(?,?,?)";
	private static final String GET_USER_INVITE_MSG = "SELECT p.projId, p.projName FROM projects p, inivitetoprojects i " +
			"WHERE p.projId = i.projId and i.to_id = ? and i.status = 'pending' ";
	private static final String PROJECT_INVITE_STATUS_UPDATE = "UPDATE inivitetoprojects " +
			"SET status = ? " +
			"WHERE to_id = ? and projId  = ?";
	private static final String ADD_TO_PROJECT_WORKSON = "INSERT INTO workson VALUES(?, ?)";

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

	public int sendInviteToProject(int from_id,int to_id,int projId){
		return jdbcTemplate.update(INVITE_USER, from_id, to_id, projId);
	}

	public List<Project> getUserInvite(int user_id){

		return jdbcTemplate.query(GET_USER_INVITE_MSG, (rs, row) -> {
			return new Project(rs.getInt("projId"),rs.getString("projName"));
		}, user_id);

	}

	public int updateStatus(int to_id, int projId,String status){
		return jdbcTemplate.update(PROJECT_INVITE_STATUS_UPDATE, status, to_id, projId);
	}

	public int addtoProjectWorksOn(int user_id, int proj_id){
		return jdbcTemplate.update(ADD_TO_PROJECT_WORKSON, user_id, proj_id);
	}
}
