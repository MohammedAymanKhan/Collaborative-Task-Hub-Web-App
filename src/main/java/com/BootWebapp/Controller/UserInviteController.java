package com.BootWebapp.Controller;

import com.BootWebapp.Model.User;
import com.BootWebapp.Services.SearchUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserInviteController{

    @Autowired
    SearchUsers searchUser;

    @GetMapping("/searchContributor/{contributor}")
    public ResponseEntity<List<User>> searchContributor(@PathVariable  String contributor){
        List<User> userList=searchUser.searchContributor(contributor);
        return userList==null||userList.isEmpty() ? ResponseEntity.badRequest().body(null) : ResponseEntity.ok(userList);
    }



}
