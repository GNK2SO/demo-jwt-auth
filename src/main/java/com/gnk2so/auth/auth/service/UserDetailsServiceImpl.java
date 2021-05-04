package com.gnk2so.auth.auth.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gnk2so.auth.user.exception.UserNotFoundException;
import com.gnk2so.auth.user.model.User;
import com.gnk2so.auth.user.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService service;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = service.findByEmail(email);
            return userDetailsFrom(user);
        } catch ( UserNotFoundException e ) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
    
    private UserDetails userDetailsFrom(User user) {
        String username = user.getEmail();
        String password = user.getPassword();
        List<GrantedAuthority> authorities = authorities();
        return new org.springframework.security.core.userdetails.User(username, password, authorities);
    }
    
    private List<GrantedAuthority> authorities() {
        return new ArrayList<>(Arrays.asList(new SimpleGrantedAuthority("AUTH")));
    }

}
