package com.BootWebapp.Services;

import com.BootWebapp.DAO.UserSearchRepository;
import com.BootWebapp.Model.User;
import com.BootWebapp.DAO.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchUsers {

    private final UserSearchRepository userSearchRepository;

    @Autowired
    public SearchUsers(UserSearchRepository userSearchRepository){
        this.userSearchRepository = userSearchRepository;
    }

    public List<User> getUserByName(String name){

        String[] names = name.split(" ");

        Set<Integer> usersSet = new HashSet<>();
        List<User> userList = new ArrayList<>();

        for(String tempUser : names) {

            List<User> tempUsers = userSearchRepository.byName(tempUser);

            for (User user : tempUsers){
                if(!usersSet.contains(user.getUser_id())){
                    usersSet.add(user.getUser_id());
                    userList.add(user);
                }
            }

        }

        return userList;
    }

    public User getUSerByEmail(String email){

        if(email.contains("gmail"))
            return userSearchRepository.byEmail(email);
        else
            return null;

    }

    public List<User> getUserByTechStack(String techStacks){

        String[] techStackArr = techStacks.split(" ");

        Set<Integer> usersSet = new HashSet<>();
        List<User> userList = new ArrayList<>();

        for(String tempTechStack : techStackArr) {

            List<User> tempUsers = userSearchRepository.byTechStack(tempTechStack);

            for (User user : tempUsers){
                if(!usersSet.contains(user.getUser_id())){
                    usersSet.add(user.getUser_id());
                    userList.add(user);
                }
            }

        }

        return userList;
    }
}
