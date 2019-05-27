package com.chess.chessapi.services;

import com.chess.chessapi.constant.AppRole;
import com.chess.chessapi.constant.NotificationMessage;
import com.chess.chessapi.constant.ObjectType;
import com.chess.chessapi.constant.Status;
import com.chess.chessapi.entities.Notification;
import com.chess.chessapi.entities.User;
import com.chess.chessapi.repositories.NotificationRepository;
import com.chess.chessapi.repositories.UserRepository;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.util.ManualCastUtils;
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

    public UserPrincipal getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        return user;
    }

    public Optional<User> getUserById(long id){
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email){return userRepository.findByEmail(email);}

    public String register(User user){
        String redirectUri = "";
        switch (user.getRole()){
            case AppRole
                    .ROLE_INSTRUCTOR:
                this.registerInstructor(user);
                redirectUri = "/instructor/home";
                break;
            default:
                this.registerLearner(user);
                redirectUri = "/learner/home";
        }
        this.setUserRoleAuthentication(user);
        userRepository.save(user);
        return redirectUri;
    }

    public Page<User> getPagination(int page,int pageSize,String fullName,String roleSort,Boolean sortFullName){
        PageRequest pageable =  null;
        if(sortFullName){
            pageable = PageRequest.of(page - 1,pageSize, Sort.by("full_name").ascending());
        }else {
            pageable = PageRequest.of(page - 1,pageSize, Sort.by("created_date").descending());
        }

        Page<Object> rawData = null;
        Page<User> data = null;
        if( roleSort != null && !roleSort.isEmpty()){
            rawData = userRepository.findAllByFullNameSortByRoleCustom(pageable,fullName,roleSort);
        }else{
            rawData = userRepository.findAllByFullNameCustom(pageable,fullName);
        }
        final List<User> content = ManualCastUtils.castPageObjectoUser(rawData);
        final int totalPages = rawData.getTotalPages();
        final long totalElements = rawData.getTotalElements();
        data = new Page<User>() {
            @Override
            public int getTotalPages() {
                return totalPages;
            }

            @Override
            public long getTotalElements() {
                return totalElements;
            }

            @Override
            public <U> Page<U> map(Function<? super User, ? extends U> converter) {
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
            public List<User> getContent() {
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
            public Iterator<User> iterator() {
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
