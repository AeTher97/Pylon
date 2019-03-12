package org.urzednicza.pylon.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
public class UserDetails implements UserDetailsService {

    @Override
    public User loadUserByUsername(String username){
        return null;
    }
}
