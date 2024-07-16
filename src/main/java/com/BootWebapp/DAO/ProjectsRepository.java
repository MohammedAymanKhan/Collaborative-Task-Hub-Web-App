package com.BootWebapp.DAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.BootWebapp.Model.Project;

@Repository
public class ProjectsRepository{

	private static final String NEWPROJECT = "insert into projects(projName,createdBy) values(?,?)";
	private static final String DELETEPROJECT = "Delete from projects where projId=?";
	private static final String UPDATEPROJECT = "Update projects Set projName=? where projId=?";
	private static final String GETPROJECTS = "SELECT p.projId, p.projName FROM projects AS p"
			+ " INNER JOIN workson AS w ON p.projId = w.projid"
			+ " WHERE  w.email =?"
			+ " UNION"
			+ " SELECT p.projId, p.projName"
			+ " FROM projects AS p"
			+ " WHERE p.createdBy=?;";

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public ProjectsRepository(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}

	public Boolean addProject(Project project) {

		int row=jdbcTemplate.update(NEWPROJECT,project.getProjName(),project.getCratedByEmail());
		return row==1;

	}

	public Boolean removeProject(Integer projId) {

		int row=jdbcTemplate.update(DELETEPROJECT,projId);
		return row==1;

	}

	public Boolean updateProject(Project project) {

		int row=jdbcTemplate.update(UPDATEPROJECT,project.getProjName(),project.getProjId());
		return row==1;

	}

	public List<Project> getProjects(String email){

		return jdbcTemplate.query(GETPROJECTS ,(rs, rowNum) ->
			new Project(
				rs.getInt("projId") ,
				rs.getString("projName")
			)
			,email ,email
		);

	}	
	
}
