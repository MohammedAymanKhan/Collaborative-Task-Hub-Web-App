package com.BootWebapp.DAO;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.BootWebapp.Model.Project;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ProjectsRepository{

	private static final String NEW_PROJECT = " INSERT INTO projects(projName , createdBy) VALUES(?,?) ";
	private static final String DELETE_PROJECT = "Delete from projects where projId=?";
	private static final String UPDATE_PROJECT = "Update projects Set projName=? where projId=?";
	private static final String GET_PROJECTS = "SELECT p.projId, p.projName FROM projects AS p"
			+ " INNER JOIN workson AS w ON p.projId = w.projid"
			+ " WHERE  w.user_id =?"
			+ " UNION"
			+ " SELECT p.projId, p.projName"
			+ " FROM projects AS p"
			+ " WHERE p.createdBy=?";
	private static final String GET_ASSIGNS_OF_PROJECT = " SELECT DISTINCT u.user_id, " +
			"COALESCE(CONCAT(u.first_name, ' ', u.last_name), 'Pending Assignment') AS assign " +
			"FROM users u " +
			"JOIN workson w ON w.user_id = u.user_id " +
			"WHERE w.projId = ? " +
			"UNION " +
			"SELECT DISTINCT u.user_id, " +
			"COALESCE(CONCAT(u.first_name, ' ', u.last_name), 'Pending Assignment') AS assign " +
			"FROM users u " +
			"JOIN projects p ON p.createdBy = u.user_id " +
			"WHERE p.projID = ? ";

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public ProjectsRepository(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}

	@Transactional
	public boolean addProject(Project project) {

		KeyHolder keyHolder = new GeneratedKeyHolder();

		int row = jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(NEW_PROJECT, new String[]{"projId"});
			ps.setString(1, project.getProjName());
			ps.setInt(2, project.getCratedBy());
			return ps;
		}, keyHolder);

		project.setProjId(keyHolder.getKey().intValue());

		return row==1;

	}

	public Boolean removeProject(Integer projId) {

		int row=jdbcTemplate.update(DELETE_PROJECT,projId);
		return row==1;

	}

	public Boolean updateProject(Project project) {

		int row=jdbcTemplate.update(UPDATE_PROJECT,project.getProjName(),project.getProjId());
		return row==1;

	}

	public List<Project> getProjects(int user_id){

		return jdbcTemplate.query(GET_PROJECTS ,(rs, rowNum) ->
			new Project(
				rs.getInt("projId") ,
				rs.getString("projName")
			)
			,user_id ,user_id
		);

	}

	public List<Map<String,Object>> getAssigns(int projId){

		return jdbcTemplate.queryForList(GET_ASSIGNS_OF_PROJECT, projId, projId);

	}
	
}
