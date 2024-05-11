package com.BootWebapp.Services;

import com.BootWebapp.DAO.ProjectReportDAO;
import com.BootWebapp.Model.ProjectReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProjectReportServices {

    private ProjectReportDAO projRepDao;
    private Set<String> updateOpProjDetailID=new HashSet<>();

    @Autowired
    public void setProjRepDao(ProjectReportDAO projRepDao) {
        this.projRepDao = projRepDao;
    }

    public ProjectReport addProjectDetails(ProjectReport projRep, Integer pid) throws DataAccessException {

        int row=projRepDao.addProjReport(projRep,pid);
        if(row==1) return projRep;
        else return null;

    }

    public Boolean updateProjDetails(String pRid,String column,String value) throws DataAccessException {

        while(updateOpProjDetailID.contains(pRid));

        updateOpProjDetailID.add(pRid);
        int row=projRepDao.updateProjReport(Float.parseFloat(pRid),column,value);
        updateOpProjDetailID.remove(pRid);

        return row==1;

    }

    public Boolean removeProjDetails(Float pRid) throws DataAccessException{

        int row=projRepDao.removeProjReport(pRid);
        return row==1;

    }

    public List<ProjectReport> getProjReportDetails(Integer pid) throws DataAccessException{

        return projRepDao.getProjReport(pid);

    }

}
