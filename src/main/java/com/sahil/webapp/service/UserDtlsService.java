package com.sahil.webapp.service;

import com.sahil.webapp.model.User;
import com.sahil.webapp.repository.UserRepository;
import com.sahil.webapp.security.UserDtlsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDtlsService  implements UserDetailsService {


    @Autowired
    private UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= this.repo.findUserByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException("No user with the provided username");
        }
        return new UserDtlsImpl(user);
    }
}
