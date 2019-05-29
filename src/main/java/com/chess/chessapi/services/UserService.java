package com.chess.chessapi.services;

import com.chess.chessapi.constant.*;
import com.chess.chessapi.entities.Cetificates;
import com.chess.chessapi.entities.Notification;
import com.chess.chessapi.entities.User;
import com.chess.chessapi.model.PaginationCustom;
import com.chess.chessapi.repositories.CetificatesRepository;
import com.chess.chessapi.repositories.NotificationRepository;
import com.chess.chessapi.repositories.UserRepository;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.util.ManualCastUtils;
import com.chess.chessapi.viewmodel.UserPagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private CetificatesRepository cetificatesRepository;

    public UserPrincipal getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        return user;
    }

    public Optional<User> getUserById(long id){
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email){return userRepository.findByEmail(email);}

    public String register(User user,String redirectClient){
        String redirectUri = "";
        switch (user.getRole()){
            case AppRole
                    .ROLE_INSTRUCTOR:
                this.registerInstructor(user);
                break;
            default:
                this.registerLearner(user);
        }

        redirectUri = redirectClient != null ? redirectClient : "/";

        this.setUserRoleAuthentication(user);
        this.userRepository.save(user);
        return redirectUri;
    }

    public void updateProfile(User user){
        this.userRepository.updateProfile(user.getId(),user.getFullName(),user.getAchievement());

        //handle cetificate update
        List<Cetificates> oldCetificates = this.cetificatesRepository.findAllByUserId(user.getId());
        if(oldCetificates.isEmpty()){
            //add all
            for (Cetificates newCetificate:
                    user.getCetificates()) {
                this.cetificatesRepository.save(newCetificate);
            }
        }else if(user.getCetificates() != null && !user.getCetificates().isEmpty()){
            //check if new cetificate has already or not, if it not yet c=> create
            for (Cetificates newCetificate:
                 user.getCetificates()) {
                boolean isExist = false;
                for (Cetificates oldCetificate:
                     oldCetificates) {
                    if(newCetificate.getCetificateLink().equals(oldCetificate.getCetificateLink())){
                       isExist = true;
                        break;
                    }
                }
                if(!isExist){
                    this.cetificatesRepository.save(newCetificate);
                }
            }
            //check old records should be deleted
            for (Cetificates oldCetificate:
                 oldCetificates) {
                boolean isUpdatedRecord = false;
                for (Cetificates newCetificate:
                     user.getCetificates()) {
                    if(oldCetificate.getCetificateLink().equals(newCetificate.getCetificateLink())){
                        isUpdatedRecord = true;
                        user.getCetificates().remove(newCetificate);
                        break;
                    }
                }

                if(!isUpdatedRecord){
                    this.cetificatesRepository.delete(oldCetificate);
                }
            }
        }
        else{
            //delete all
            for (Cetificates cetificate:
                 oldCetificates) {
                this.cetificatesRepository.delete(cetificate);
            }
        }
    }

    public PaginationCustom<UserPagination> getPagination(int page,int pageSize,String fullName,String sortRole,Boolean sortFullName
            ,String sortStatus){
        PageRequest pageable =  null;
        if(sortFullName){
            pageable = PageRequest.of(page - 1,pageSize, Sort.by(EntitiesFieldName.USER_FULL_NAME).ascending()
                    .by(EntitiesFieldName.USER_CREATED_DATE).descending());
        }else {
            pageable = PageRequest.of(page - 1,pageSize, Sort.by(EntitiesFieldName.USER_CREATED_DATE).descending());
        }

        Page<Object> rawData = null;
        PaginationCustom<UserPagination> data = null;
        if( sortRole != null && !sortRole.isEmpty()){
            rawData = userRepository.findAllByFullNameSortByRoleCustom(pageable,fullName,sortRole);
        }else if(sortStatus != null && !sortStatus.isEmpty()){
            if(sortStatus.equals(Integer.toString(Status.INACTIVE))){
                rawData = userRepository.findAllByStatus(pageable,Status.INACTIVE);
            }else{
                rawData = userRepository.findAllByStatus(pageable,Status.ACTIVE);
            }
        }else{
            rawData = userRepository.findAllByFullNameCustom(pageable,fullName);
        }
        final List<UserPagination> content = ManualCastUtils.castPageObjectsoUser(rawData);
        final int totalPages = rawData.getTotalPages();
        final long totalElements = rawData.getTotalElements();
        data = new PaginationCustom<UserPagination>(content,totalPages,totalElements);

        return data;
    }

    public void updateStatus(long id,int isActive){
        userRepository.updateStatus(id,isActive);
    }

    private void setUserRoleAuthentication(User user){
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority(user.getRole()));
        UserPrincipal userDetails = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        authentication.setDetails(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void registerLearner(User user){
        user.setIs_active(Status.ACTIVE);
        user.setRole(AppRole.ROLE_LEARNER);
    }

    private void registerInstructor(User user){
        user.setIs_active(Status.INACTIVE);
        user.setRole(AppRole.ROLE_INSTRUCTOR);

        // create notification for admin
        Notification notification = new Notification();
        notification.setObjectId(ObjectType.USER);
        notification.setObjectName(user.getFullName());
        notification.setObjectId(user.getId());
        notification.setContent(NotificationMessage.CREATE_NEW_USER_AS_INSTRUCTOR);
        notification.setCreateDate(new Timestamp(new Date().getTime()));
        notification.setViewed(false);
        notification.setRoleTarget(AppRole.ROLE_ADMIN);
        notificationRepository.save(notification);
    }

}
