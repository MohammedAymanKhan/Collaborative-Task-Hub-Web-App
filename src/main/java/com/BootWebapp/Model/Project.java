package com.BootWebapp.Model;



public class Project {

	private Integer projId;
	private String projName;
	private Integer cratedBy;
	
	public Project(){}
	
	public Project(Integer projId, String projName) {
		super();
		this.projId = projId;
		this.projName = projName;
	}

	public Project(String projName , Integer cratedBy){
		this.projName = projName;
		this.cratedBy = cratedBy;
	}

	public Integer getProjId() {
		return projId;
	}

	public String getProjName() {
		return projName;
	}

	public Integer getCratedBy() {
		return cratedBy;
	}

	public void setProjName(String projName) {
		this.projName = projName;
	}

	public void setCratedBy(Integer cratedBy) {
		this.cratedBy = cratedBy;
	}

	public void setProjId(Integer projId) {
		this.projId = projId;
	}

	@Override
	public String toString() {
		return "Project{" +
				"projId=" + projId +
				", projName='" + projName + '\'' +
				", cratedBy=" + cratedBy +
				'}';
	}
}
