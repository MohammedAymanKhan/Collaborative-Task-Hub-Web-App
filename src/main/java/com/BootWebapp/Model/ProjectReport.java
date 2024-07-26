package com.BootWebapp.Model;


import org.jetbrains.annotations.Contract;

public class ProjectReport {
	
	private String projRepID;
	private String taskTittle;//taskTitle
	private String assign;
	private Integer assign_id;
	private String progress;
	private String dueDate;
	
	public ProjectReport() {}

	public ProjectReport(String projRepID, String taskTittle,int assign_id, String assign, String progress, String dueDate) {
		this.projRepID = projRepID;
		this.taskTittle = taskTittle;
		this.assign = assign;
		this.assign_id = assign_id;
		this.progress = progress;
		this.dueDate = dueDate;
	}

	public ProjectReport(String projRepID, String taskTittle, String assign, String progress, String dueDate) {
		this.projRepID = projRepID;
		this.taskTittle = taskTittle;
		this.assign = assign;
		this.progress = progress;
		this.dueDate = dueDate;
	}

	public ProjectReport(String projRepID, String taskTittle, String dueDate) {
		this.projRepID = projRepID;
		this.taskTittle = taskTittle;
		this.dueDate = dueDate;
		this.progress = null;
		this.assign = null;
	}

	public String getProjRepID() {
		return projRepID;
	}

	public void setProjRepID(String projRepID) {
		this.projRepID = projRepID;
	}

	public String getTaskTittle() {
		return taskTittle;
	}

	public void setTaskTittle(String taskTittle) {
		this.taskTittle = taskTittle;
	}

	public String getAssign() {
		return assign;
	}

	public void setAssign(String assign) {
		this.assign = assign;
	}

	public Integer getAssign_id() {
		return assign_id;
	}

	public void setAssign_id(Integer assign_id) {
		this.assign_id = assign_id;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	@Override
	public String toString() {
		return "ProjectReport [projRepID=" + projRepID + ", taskTittle=" + taskTittle + ", assign=" + assign
				+ ", progress=" + progress + ", dueDate=" + dueDate + "]";
	}
	
	
	
}
