package com.BootWebapp.DAO;

import com.BootWebapp.Model.Project;
import com.BootWebapp.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsService {

    private final UserRepository repository;

    @Autowired
    public UserDetailsService(UserRepository repository){
        this.repository = repository;
    }

    public User existsUser(String email){
        return repository.userExistsByEmail(email);
    }

    public boolean inviteUserToProject(int from_Id, int to_Id, int projId){
        return repository.sendInviteToProject(from_Id, to_Id, projId) == 1;
    }

    public List<Project> getNotifications(int user_id){
        return repository.getUserInvite(user_id);
    }

    public boolean updateStatusInvite(int toId, int projId, boolean status){
        String strStatus = status ? "accepted" : "rejected";
        boolean flag = repository.updateStatus(toId, projId, strStatus) == 1;
        return flag && status ? repository.addtoProjectWorksOn(toId,projId) == 1 : flag;
    }

}
