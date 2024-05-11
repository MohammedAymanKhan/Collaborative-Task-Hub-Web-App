package com.BootWebapp.DAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.BootWebapp.Model.Project;

@Repository
public class ProjectsRepository{

	private static final String insert="insert into projects(projName,createdBy) values(?,?)";
	private static final String delete="Delete from projects where projId=?";
	private static final String update="Update projects Set projName=? where projId=?";
	private static final String selectProjects="SELECT p.projId, p.projName FROM projects AS p"
			+ " INNER JOIN workson AS w ON p.projId = w.projid"
			+ " WHERE  w.email =?"
			+ " UNION"
			+ " SELECT p.projId, p.projName"
			+ " FROM projects AS p"
			+ " WHERE p.createdBy=?;";

	private JdbcTemplate projConn;

	@Autowired
	public void setProjConn(JdbcTemplate projConn) {
		this.projConn = projConn;
	}

	public Boolean addProject(Project project) {

		int row=projConn.update(insert,project.getProjName(),project.getCratedByEmail());
		return row==1;

	}

	public Boolean removeProject(Integer projId) {

		int row=projConn.update(delete,projId);
		return row==1;

	}

	public Boolean updateProject(Project project) {

		int row=projConn.update(update,project.getProjName(),project.getProjId());
		return row==1;

	}

	public List<Project> getProjects(String email){

		return projConn.query(selectProjects,(rs, rowNum) ->
				new Project(rs.getInt("projId"),rs.getString("projName")),email,email);

	}	
	
}
