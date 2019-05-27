package com.chess.chessapi.services;

import com.chess.chessapi.constant.AppRole;
import com.chess.chessapi.entities.Notification;
import com.chess.chessapi.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public Notification create(Notification notification){
        return notificationRepository.save(notification);
    }

    public Page<Notification> getPagination(int page,int pageSize,String role,String userId,boolean sortIsViewed){
        PageRequest pageable =  null;

        if(sortIsViewed){
            pageable = PageRequest.of(page - 1,pageSize, Sort.by("is_viewed").ascending());
        }else {
            pageable = PageRequest.of(page - 1,pageSize, Sort.by("created_date").descending());
        }

        Page<Notification> notificationPage;
        switch (role){
            case AppRole.ROLE_ADMIN:
                notificationPage = notificationRepository.findAllByRoleWithPagination(pageable,AppRole.ROLE_ADMIN);
                break;
            case AppRole.ROLE_INSTRUCTOR:
                notificationPage = notificationRepository.findAllByRoleAndObjectIdWithPagination(pageable,AppRole.ROLE_INSTRUCTOR,userId);
                break;
            default:
                notificationPage = notificationRepository.findAllByRoleAndObjectIdWithPagination(pageable,AppRole.ROLE_LEARNER,userId);

        }
        return notificationPage;
    }
}
