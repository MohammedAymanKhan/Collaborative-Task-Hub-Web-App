package com.BootWebapp.Model;


public class ProjectReport {
	
	private String projRepID;
	private String taskTittle;
	private String assign;
	private String progress;
	private String dueDate;
	
	public ProjectReport() {}

	public ProjectReport(String projRepID, String taskTittle, String assign, String progress, String dueDate) {
		this.projRepID = projRepID;
		this.taskTittle = taskTittle;
		this.assign = assign;
		this.progress = progress;
		this.dueDate = dueDate;
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
