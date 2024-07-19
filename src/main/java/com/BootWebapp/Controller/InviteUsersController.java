package com.BootWebapp.Controller;

import com.BootWebapp.Model.User;
import com.BootWebapp.Services.SearchUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(method = RequestMethod.GET , path = "/getUsers" , produces = "application/json")
public class InviteUsersController {


    private final SearchUsers searchUsers;

    @Autowired
    public InviteUsersController(SearchUsers searchUsers){
        this.searchUsers = searchUsers;
    }


    @RequestMapping("/byName/{name}")
    public List<User> byName(@PathVariable("name") String byName){
        System.out.println("called by name");
        return searchUsers.getUserByName(byName);
    }

    @RequestMapping("/byEmail/{email}")
    public ResponseEntity<Object> byEmail(@PathVariable("email") String byEmail){

        User user = searchUsers.getUSerByEmail(byEmail);

        return user != null ? ResponseEntity.ok(user) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Such User");

    }

    @RequestMapping("/byTechStack/{techstack}")
    public List<User> byTechStack(@PathVariable("techstack") String byTechStack){

        return searchUsers.getUserByTechStack(byTechStack);

    }

    @RequestMapping("/searchUser/{parameter}")
    public ResponseEntity<Object> getUser(@PathVariable("parameter") String searchUser){

        List<User> userList = new ArrayList<>();
        List<User> tempUsers;

        tempUsers = searchUsers.getUserByName(searchUser);
        if(!tempUsers.isEmpty())  userList.addAll(tempUsers);

        User user = searchUsers.getUSerByEmail(searchUser);
        if(user != null) userList.add(user);

        tempUsers = searchUsers.getUserByTechStack(searchUser);
        if(!tempUsers.isEmpty())  userList.addAll(tempUsers);

        return userList.isEmpty() ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user exists by this parameter") :
                ResponseEntity.ok(userList);

    }
}
