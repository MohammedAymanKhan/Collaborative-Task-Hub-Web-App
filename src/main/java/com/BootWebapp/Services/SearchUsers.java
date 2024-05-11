package com.BootWebapp.Services;

import com.BootWebapp.Model.User;
import com.BootWebapp.DAO.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchUsers {

    @Autowired
    UserRepository userRepo;

    public List<User> searchContributor(String contributor){
        List<User> userList=null;

        String[] searchBy=contributor.split(":");
        System.out.println(searchBy[0]+searchBy[1]);

        if(searchBy[0].equals("email")) {
            userList=new ArrayList<>();
            userList.add(userRepo.getUserByEmail(searchBy[1]));
        }else {
            userList = userRepo.getUserByName(searchBy[1]);
        }

        return userList;
    }

}
