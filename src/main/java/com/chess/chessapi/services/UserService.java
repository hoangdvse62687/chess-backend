package com.chess.chessapi.services;

import com.chess.chessapi.constant.*;
import com.chess.chessapi.entities.Cetificates;
import com.chess.chessapi.entities.Notification;
import com.chess.chessapi.entities.User;
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

    public Page<UserPagination> getPagination(int page,int pageSize,String fullName,String sortRole,Boolean sortFullName,Boolean sortStatus){
        PageRequest pageable =  null;
        if(sortFullName){
            pageable = PageRequest.of(page - 1,pageSize, Sort.by(EntitiesFieldName.USER_FULL_NAME).ascending());
        }else if(sortStatus){
            pageable = PageRequest.of(page - 1,pageSize, Sort.by(EntitiesFieldName.USER_IS_ACTIVE).descending());
        }else {
            pageable = PageRequest.of(page - 1,pageSize, Sort.by(EntitiesFieldName.USER_CREATED_DATE).descending());
        }

        Page<Object> rawData = null;
        Page<UserPagination> data = null;
        if( sortRole != null && !sortRole.isEmpty()){
            rawData = userRepository.findAllByFullNameSortByRoleCustom(pageable,fullName,sortRole);
        }else{
            rawData = userRepository.findAllByFullNameCustom(pageable,fullName);
        }
        final List<UserPagination> content = ManualCastUtils.castPageObjectsoUser(rawData);
        final int totalPages = rawData.getTotalPages();
        final long totalElements = rawData.getTotalElements();
        data = new Page<UserPagination>() {
            @Override
            public int getTotalPages() {
                return totalPages;
            }

            @Override
            public long getTotalElements() {
                return totalElements;
            }

            @Override
            public <U> Page<U> map(Function<? super UserPagination, ? extends U> converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public int getNumberOfElements() {
                return 0;
            }

            @Override
            public List<UserPagination> getContent() {
                return content;
            }

            @Override
            public boolean hasContent() {
                return false;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator<UserPagination> iterator() {
                return null;
            }
        };
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
