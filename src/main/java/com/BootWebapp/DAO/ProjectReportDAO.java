package com.BootWebapp.DAO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.BootWebapp.Model.ProjectReport;

@Repository
public class ProjectReportDAO{

	private static final String insert="INSERT INTO projectreport VALUES(?,?,?,?,?,?)";
	private static final String delete="DELETE FROM projectreport WHERE projRepID=?";
	private static final String selectProjReports="Select projRepID,taskTittle,assign,progress,dueDate FROM projectreport WHERE projID=?";

	private JdbcTemplate projConn;

	@Autowired
	public void setProjConn(JdbcTemplate projConn) {
		this.projConn = projConn;
	}

	//created
	public int addProjReport(ProjectReport projRep,Integer pid) throws DataAccessException {

		return projConn.update(insert, projRep.getProjRepID(), projRep.getTaskTittle(),
				projRep.getAssign(), projRep.getProgress(), projRep.getDueDate(), pid);

	}
	
	//update
	public int updateProjReport(Float pRid,String column,String value) throws DataAccessException{

		String update = "UPDATE projectreport SET "+column+" = ? WHERE projRepID = ?";
		return projConn.update(update, value, pRid);

	}
	
	//Delete
	public int removeProjReport(Float pRid) throws DataAccessException {

		return projConn.update(delete, pRid);

	}
	
	//Retrieve
	public List<ProjectReport> getProjReport(Integer pid) throws DataAccessException {

		return projConn.query(selectProjReports, (rs, rowNum) ->
				new ProjectReport(
					rs.getString("projRepId"),
					rs.getString("tasktittle"),
					rs.getString("assign"),
					rs.getString("progress"),
					rs.getString("dueDate")),
				pid);

	}
	
	
}
