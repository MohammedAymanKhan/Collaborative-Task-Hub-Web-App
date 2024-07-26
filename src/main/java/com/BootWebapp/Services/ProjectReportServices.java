package com.BootWebapp.Services;

import com.BootWebapp.DAO.ProjectReportDAO;
import com.BootWebapp.Model.ProjectReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ProjectReportServices {

    private final ProjectReportDAO projRepDao;
    private static final Map<String,String> currentUpdateOnColumn = new HashMap<>();

    @Autowired
    public ProjectReportServices(ProjectReportDAO projRepDao) {
        this.projRepDao = projRepDao;
    }

    public Boolean addProjectDetails(ProjectReport projRep, Integer pid) throws DataAccessException {

        int row;

        if(projRep.getProgress() != null && projRep.getAssign_id() != -1){
            row = projRepDao.addProjReportAllDetails(projRep,pid);
            System.out.println("Both assign and prog: "+projRep);
        } else if (projRep.getAssign_id() != -1) {
            row = projRepDao.addProjReportWithNoProg(projRep,pid);
            System.out.println("only assign: "+projRep);
        }else if (projRep.getProgress() != null) {
            row = projRepDao.addProjReportWithNoAssig(projRep,pid);
            System.out.println("only prog: "+projRep);
        }else{
            row = projRepDao.addProjReportWithNoProgNoAssig(projRep,pid);
        }

       if (row == 1){
           if(projRep.getProgress() == null) projRep.setProgress("In Progress");
           if(projRep.getAssign() == null) projRep.setAssign("Pending Assignment");
           return true;
       }else
           return false;
    }

    public Boolean updateProjDetails(String pRid,String column,Object value) throws DataAccessException {

        while (currentUpdateOnColumn.containsKey(pRid) && currentUpdateOnColumn.get(pRid).equals(column))

        currentUpdateOnColumn.put(pRid,column);

        if(column.equals("assign")){
            value = !value.equals("") ? Integer.parseInt((String)value) : -1;
            column = "assign_id";
        }
        int row = projRepDao.updateProjReport(Float.parseFloat(pRid),column,value);

        currentUpdateOnColumn.remove(pRid);

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
