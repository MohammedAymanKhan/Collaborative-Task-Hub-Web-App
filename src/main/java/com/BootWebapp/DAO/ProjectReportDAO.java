package com.BootWebapp.DAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.BootWebapp.Model.ProjectReport;


@Repository
public class ProjectReportDAO{

	private static final String NEW_PROJECT_REPORET_ALL_VALUES = "INSERT INTO projectreport(projRepID, taskTittle, " +
			"assign, progress, dueDate, projId) VALUES(?,?,?,?,?,?)";
	private static final String NEW_PROJECT_REPORET_NO_ASSIGN_NO_PROGRESS = "INSERT INTO projectreport(projRepID, taskTittle, " +
			"dueDate, projId) VALUES(?,?,?,?)";
	private static final String NEW_PROJECT_REPORET_NO_ASSIGN = "INSERT INTO projectreport(projRepID, taskTittle, " +
			"progress, dueDate, projId) VALUES(?,?,?,?,?)";
	private static final String NEW_PROJECT_REPORET_NO_PROGRESS = "INSERT INTO projectreport(projRepID, taskTittle, " +
			"assign, dueDate, projId) VALUES(?,?,?,?,?)";
	private static final String DELETE_PROJECT_REPORET = "DELETE FROM projectreport WHERE projRepID=?";
	private static final String GET_PROJECT_REPORETS = "SELECT projRepID,taskTittle,assign,progress,dueDate FROM " +
			"projectreport WHERE projID=?";

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public ProjectReportDAO(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}

	public int addProjReportAllDetails(ProjectReport projRep,Integer pid) throws DataAccessException {

		return jdbcTemplate.update(NEW_PROJECT_REPORET_ALL_VALUES, projRep.getProjRepID(), projRep.getTaskTittle(),
				projRep.getAssign(), projRep.getProgress(), projRep.getDueDate(), pid);

	}

	public int addProjReportWithNoProgNoAssig(ProjectReport projRep,Integer pid) throws DataAccessException {

		return jdbcTemplate.update(NEW_PROJECT_REPORET_NO_ASSIGN_NO_PROGRESS, projRep.getProjRepID(),
				projRep.getTaskTittle(), projRep.getDueDate(), pid);

	}

	public int addProjReportWithNoAssig(ProjectReport projRep,Integer pid) throws DataAccessException {

		return jdbcTemplate.update(NEW_PROJECT_REPORET_NO_ASSIGN, projRep.getProjRepID(),
				projRep.getTaskTittle(), projRep.getProgress(), projRep.getDueDate(), pid);

	}

	public int addProjReportWithNoProg(ProjectReport projRep,Integer pid) throws DataAccessException {

		return jdbcTemplate.update(NEW_PROJECT_REPORET_NO_PROGRESS, projRep.getProjRepID(),
				projRep.getTaskTittle(), projRep.getAssign(), projRep.getDueDate(), pid);

	}

	public int updateProjReport(Float pRid,String column,String value) throws DataAccessException{

		String update = "UPDATE projectreport SET "+column+" = ? WHERE projRepID = ?";
		return jdbcTemplate.update(update, value, pRid);

	}

	public int removeProjReport(Float pRid) throws DataAccessException {

		return jdbcTemplate.update(DELETE_PROJECT_REPORET, pRid);

	}
	

	public List<ProjectReport> getProjReport(Integer pid) throws DataAccessException {

		return jdbcTemplate.query(GET_PROJECT_REPORETS, (rs, rowNum) ->
				new ProjectReport(
					rs.getString("projRepId"),
					rs.getString("tasktittle"),
					rs.getString("assign"),
					rs.getString("progress"),
					rs.getString("dueDate")),
				pid);
	}
	
	
}
