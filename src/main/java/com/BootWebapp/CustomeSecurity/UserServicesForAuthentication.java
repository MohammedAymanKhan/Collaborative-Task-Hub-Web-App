package com.BootWebapp.CustomeSecurity;

import com.BootWebapp.Model.User;
import com.BootWebapp.DAO.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Component
public class UserServicesForAuthentication implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserServicesForAuthentication(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.userExistsByEmail(username);

        if (user!=null){
            return user;
        }else {
            throw new UsernameNotFoundException("user not exists");
        }

    }
}
