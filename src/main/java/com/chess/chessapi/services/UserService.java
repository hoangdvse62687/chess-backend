package com.chess.chessapi.services;

import com.chess.chessapi.constant.AppRole;
import com.chess.chessapi.constant.Status;
import com.chess.chessapi.entities.User;
import com.chess.chessapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public UserDetails getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();
        return user;
    }
    public User getUserById(long id){
        return userRepository.findById(id).get();
    }

    public User getUserByEmmail(String email){
        return userRepository.findByEmail(email).get();
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public String register(User user){
        String redirectUri = "";
        switch (user.getRole()){
            case AppRole
                    .ROLE_INSTRUCTOR:
                user.setIsActive(Status.INACTIVE);
                user.setRole(AppRole.ROLE_INSTRUCTOR);
                redirectUri = "/instructor/home";
                break;
            default:
                user.setIsActive(Status.ACTIVE);
                user.setRole(AppRole.ROLE_LEARNER);
                redirectUri = "/learner/home";
        }
        this.setUserRoleAuthentication(user);
        userRepository.save(user);
        return redirectUri;
    }

    public void setUserRoleAuthentication(User user){
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority(user.getRole()));
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        authentication.setDetails(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
