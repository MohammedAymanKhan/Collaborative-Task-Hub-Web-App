package com.BootWebapp.Services;

import com.BootWebapp.DAO.ProjectReportDAO;
import com.BootWebapp.Model.ProjectReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Service
public class ProjectReportServices {

    private final ProjectReportDAO projRepDao;
    private static final Map<String,String> currentUpdateOnColumn = new HashMap<>();

    @Autowired
    public ProjectReportServices(ProjectReportDAO projRepDao) {
        this.projRepDao = projRepDao;
    }

    public Boolean addProjectDetails(ProjectReport projRep, Integer pid) throws DataAccessException {

        int row = projRepDao.addProjReport(projRep,pid);
        return row == 1;

    }

    public Boolean updateProjDetails(String pRid,String column,String value) throws DataAccessException {

        while (currentUpdateOnColumn.containsKey(pRid) && currentUpdateOnColumn.get(pRid).equals(column))

        currentUpdateOnColumn.put(pRid,column);

        int row=projRepDao.updateProjReport(Float.parseFloat(pRid),column,value);

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
