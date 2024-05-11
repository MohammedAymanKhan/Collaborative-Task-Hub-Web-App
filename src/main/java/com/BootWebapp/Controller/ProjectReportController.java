package com.BootWebapp.Controller;


import com.BootWebapp.Model.ProjectReport;
import com.BootWebapp.Services.ProjectReportServices;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path="/projectReport",produces = "application/json")
public class ProjectReportController {

    private ProjectReportServices projectReportServices;

    @Autowired
    public void setProjectReportServices(ProjectReportServices projectReportServices) {
        this.projectReportServices = projectReportServices;
    }

    @GetMapping("/projectDetails/{projectId}")
    public ResponseEntity<List<ProjectReport>> getProjectReport(@PathVariable("projectId") Integer pID, HttpSession session){

        try{

            List<ProjectReport> projectDetails;
            projectDetails=projectReportServices.getProjReportDetails(pID);
            if(projectDetails!=null){
                session.setAttribute("pID",pID);
                return ResponseEntity.ok(projectDetails);
            }else{
                return ResponseEntity.notFound().build();
            }


        }catch (DataAccessException e){

            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
    }

    @PatchMapping("/update")
    public ResponseEntity<String> updateProjRep(@RequestBody Map<String,String> update) {

        try {

            Boolean flag=projectReportServices.updateProjDetails(update.get("projRepId"), update.get("column"), update.get("upVlaue"));
            if(flag){
                return ResponseEntity.ok().build();
            }else{
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
            }

        }catch (DataAccessException e){

            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }

    }

    @PostMapping("/insert")
    public ResponseEntity<List<ProjectReport>> insertNewProjReport(@RequestBody ProjectReport  projReport,@SessionAttribute Integer pID) {

        try {

            ProjectReport succProjectReportList = projectReportServices.addProjectDetails(projReport, pID);
            if(succProjectReportList!=null){
                return ResponseEntity.status(HttpStatus.CREATED).body(List.of(succProjectReportList));
            }

        }catch (DataAccessException e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }

    @DeleteMapping("/deleteProjRep")
    public ResponseEntity<String> deleteProjReport(@RequestBody Float pRid) {

        try{

            Boolean flag=projectReportServices.removeProjDetails(pRid);
            if(flag){
                return ResponseEntity.ok().build();
            }else{
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

        }catch (DataAccessException e){

            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }

    }

}
