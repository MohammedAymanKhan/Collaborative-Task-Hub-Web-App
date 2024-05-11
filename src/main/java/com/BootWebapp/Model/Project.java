package com.BootWebapp.Model;



public class Project {

	private Integer projId;
	private String projName;
	private String cratedByEmail;
	
	public Project(){}
	
	public Project(Integer projId, String projName) {
		super();
		this.projId = projId;
		this.projName = projName;
	}

	public Integer getProjId() {
		return projId;
	}
	public String getProjName() {
		return projName;
	}
	public void setProjName(String projName) {
		this.projName = projName;
	}
	public String getCratedByEmail() {
		return cratedByEmail;
	}
	public void setCratedByEmail(String cratedByEmail) {
		this.cratedByEmail = cratedByEmail;
	}

	@Override
	public String toString() {
		return "Project [projId=" + projId + ", projName=" + projName + ", cratedByEmail=" + cratedByEmail + "]";
	}
	
	
	
}
