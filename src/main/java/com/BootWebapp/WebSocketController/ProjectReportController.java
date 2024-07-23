package com.BootWebapp.WebSocketController;

import com.BootWebapp.Model.ProjectReport;
import com.BootWebapp.Services.ProjectReportServices;
import com.BootWebapp.WebSocketConnection.WebSocketMessageHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

@Component
public class ProjectReportController {

    private final ProjectReportServices projectReportServices;
    private final ObjectMapper converter;

    @Autowired
    public ProjectReportController(ProjectReportServices projectReportServices ,ObjectMapper converter){
        this.projectReportServices = projectReportServices;
        this.converter = converter;
    }


    public void getProjectReport(WebSocketMessageHandler webSocketHandler,WebSocketSession session,Integer pID) throws JsonProcessingException, IOException {

        try {

            List<ProjectReport> projectDetails = projectReportServices.getProjReportDetails(pID);

            if(projectDetails!=null) {
                String body = converter.writeValueAsString(projectDetails);

                String message = "{ \"header\": \"received\", \"body\": " + body + "}";

                webSocketHandler.sendToUser(message, session);
            }

        } catch (DataAccessException exc) {

            String errorMsg = "\"Error not able to get Details\"";
            String message = "{ \"header\": \"error\", \"body\": " + errorMsg + "}";
            webSocketHandler.sendToUser(message,session);

        }


    }

    public void insertNewProjReport(String body, Integer pID,
                                    WebSocketMessageHandler webSocketHandler, WebSocketSession session) throws JsonProcessingException, IOException {

        try {

            ProjectReport newProjectReport = converter.readValue(body, ProjectReport.class);

            boolean flag=projectReportServices.addProjectDetails(newProjectReport, pID);

            if(flag){
                String message = "{ \"header\": \"inserted\", \"body\": " + body + "}";
                webSocketHandler.forwardToSubscriber(message);
            }

        } catch (DataAccessException exc) {
            String errorMsg = "\"Error not able to insert\"";
            webSocketHandler.sendToUser("{ \"header\": \"error\", \"body\": " + errorMsg + "}",session);
        }

    }

    public void updateProjRep(String body,WebSocketMessageHandler webSocketHandler, WebSocketSession session) throws JsonProcessingException, IOException {

        try {

            HashMap<String, String> update = converter.readValue(body, HashMap.class);

            boolean flag = projectReportServices.updateProjDetails(update.get("projRepId"), update.get("column"),
                    update.get("upValue"));

            if(flag) {
                String message = "{ \"header\": \"updated\", \"body\": " + body + "}";
                webSocketHandler.forwardToSubscriber(message);
            }

        } catch (DataAccessException exc) {
            String errorMsg = "\"Error not able update\"";
            webSocketHandler.sendToUser("{ \"header\": \"error\", \"body\": " + errorMsg + "}",session);
        }

    }

    public void deleteProjReport(String body,WebSocketMessageHandler webScoketHandler, WebSocketSession session) throws JsonProcessingException, IOException {

        try {

            Float pRid = Float.parseFloat(body.substring(1, body.length() - 1));

            boolean flag = projectReportServices.removeProjDetails(pRid);

            if(flag) {
                String message = "{ \"header\": \"deleted\", \"body\": " + body + "}";
                webScoketHandler.forwardToSubscriber(message);
            }

        } catch (DataAccessException exc) {

            String errorMsg = "\"Error not able to get Details\"";
            webScoketHandler.sendToUser("{ \"header\": \"error\", \"body\": " + errorMsg + "}",session);

        }

    }

}
