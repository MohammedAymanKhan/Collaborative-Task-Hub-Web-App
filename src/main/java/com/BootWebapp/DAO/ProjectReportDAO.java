package com.BootWebapp.DAO;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.BootWebapp.Model.ProjectReport;


@Repository
public class ProjectReportDAO{

	private static final String NEW_PROJECT_REPORET_ALL_VALUES = "INSERT INTO projectreport(projRepID, taskTittle, " +
			"assign_id, progress, dueDate, projId) VALUES(?,?,?,?,?,?)";
	private static final String NEW_PROJECT_REPORET_NO_ASSIGN_NO_PROGRESS = "INSERT INTO projectreport(projRepID, taskTittle, " +
			"dueDate, projId) VALUES(?,?,?,?)";
	private static final String NEW_PROJECT_REPORET_NO_ASSIGN = "INSERT INTO projectreport(projRepID, taskTittle, " +
			"progress, dueDate, projId) VALUES(?,?,?,?,?)";
	private static final String NEW_PROJECT_REPORET_NO_PROGRESS = "INSERT INTO projectreport(projRepID, taskTittle, " +
			"assign_id, dueDate, projId) VALUES(?,?,?,?,?)";
	private static final String DELETE_PROJECT_REPORET = "DELETE FROM projectreport WHERE projRepID=?";
	private static final String GET_PROJECT_REPORETS = "SELECT p.projRepID, p.taskTittle, " +
			"COALESCE(CONCAT(u.first_name, ' ', u.last_name), 'Pending Assignment') AS assign, p.progress, p.dueDate " +
			"FROM projectreport p " +
			"LEFT JOIN users u ON p.assign_id = u.user_id " +
			"WHERE p.projID = ?";

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public ProjectReportDAO(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}

	public int addProjReportAllDetails(ProjectReport projRep,Integer pid) throws DataAccessException {

		return jdbcTemplate.update(NEW_PROJECT_REPORET_ALL_VALUES, projRep.getProjRepID(), projRep.getTaskTittle(),
				projRep.getAssign_id(), projRep.getProgress(), projRep.getDueDate(), pid);

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
				projRep.getTaskTittle(), projRep.getAssign_id(), projRep.getDueDate(), pid);

	}

	public int updateProjReport(Float pRid,String column,Object value) throws DataAccessException{

		String update = "UPDATE projectreport SET "+column+" = ? WHERE projRepID = ?";
		return jdbcTemplate.update(update, value, pRid);

	}

	public int removeProjReport(Float pRid) throws DataAccessException {

		return jdbcTemplate.update(DELETE_PROJECT_REPORET, pRid);

	}

	private static final String dateFormatted(String date){

		String[] arrDate = date.split("-");

		switch(arrDate[1]){
			case "01" : return arrDate[2] + "-Jan-" + arrDate[0];
			case "02" : return arrDate[2] + "-Feb-" + arrDate[0];
			case "03" : return arrDate[2] + "-Mar-" + arrDate[0];
			case "12" : return arrDate[2] + "-Dec-" + arrDate[0];
			case "04" : return arrDate[2] + "-Apr-" + arrDate[0];
			case "05" : return arrDate[2] + "-May-" + arrDate[0];
			case "06" : return arrDate[2] + "-Jun-" + arrDate[0];
			case "07" : return arrDate[2] + "-July-" + arrDate[0];
			case "08" : return arrDate[2] + "-Aug-" + arrDate[0];
			case "09" : return arrDate[2] + "-Sept-" + arrDate[0];
			case "10" : return arrDate[2] + "-Oct-" + arrDate[0];
			case "11" : return arrDate[2] + "-Nov-" + arrDate[0];
			default : return null;
		}

	}

	public List<ProjectReport> getProjReport(Integer pid) throws DataAccessException {

		return jdbcTemplate.query(GET_PROJECT_REPORETS, (rs, rowNum) ->
				new ProjectReport(
					rs.getString("projRepId"),
					rs.getString("tasktittle"),
					rs.getString("assign"),
					rs.getString("progress"),
					dateFormatted(rs.getString("dueDate"))),
				pid);
	}
	
	
}
