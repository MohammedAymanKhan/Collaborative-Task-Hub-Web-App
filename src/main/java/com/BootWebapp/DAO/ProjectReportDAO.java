package com.BootWebapp.DAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.BootWebapp.Model.ProjectReport;


@Repository
public class ProjectReportDAO{

	private static final String NEWPROJECTREPORET = "INSERT INTO projectreport VALUES(?,?,?,?,?,?)";
	private static final String DELETEPROJECTREPORET = "DELETE FROM projectreport WHERE projRepID=?";
	private static final String GETPROJECTREPORETS = "SELECT projRepID,taskTittle,assign,progress,dueDate FROM " +
			"projectreport WHERE projID=?";

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public ProjectReportDAO(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}

	public int addProjReport(ProjectReport projRep,Integer pid) throws DataAccessException {

		return jdbcTemplate.update(NEWPROJECTREPORET, projRep.getProjRepID(), projRep.getTaskTittle(),
				projRep.getAssign(), projRep.getProgress(), projRep.getDueDate(), pid);

	}

	public int updateProjReport(Float pRid,String column,String value) throws DataAccessException{

		String update = "UPDATE projectreport SET "+column+" = ? WHERE projRepID = ?";
		return jdbcTemplate.update(update, value, pRid);

	}

	public int removeProjReport(Float pRid) throws DataAccessException {

		return jdbcTemplate.update(DELETEPROJECTREPORET, pRid);

	}
	

	public List<ProjectReport> getProjReport(Integer pid) throws DataAccessException {

		return jdbcTemplate.query(GETPROJECTREPORETS, (rs, rowNum) ->
				new ProjectReport(
					rs.getString("projRepId"),
					rs.getString("tasktittle"),
					rs.getString("assign"),
					rs.getString("progress"),
					rs.getString("dueDate")),
				pid);
	}
	
	
}
