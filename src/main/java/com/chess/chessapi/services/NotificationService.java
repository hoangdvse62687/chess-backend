package com.chess.chessapi.services;

import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.constants.EntitiesFieldName;
import com.chess.chessapi.entities.Notification;
import com.chess.chessapi.entities.User;
import com.chess.chessapi.repositories.NotificationRepository;
import com.chess.chessapi.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    //public method
    public Notification create(Notification notification){
        return notificationRepository.save(notification);
    }

    public Page<Notification> getPagination(int page,int pageSize,long role_id,String userId,boolean sortIsViewed){
        PageRequest pageable =  null;

        if(sortIsViewed){
            pageable = PageRequest.of(page - 1,pageSize, Sort.by(EntitiesFieldName.NOTIFICATION_IS_VIEWED).ascending());
        }else {
            pageable = PageRequest.of(page - 1,pageSize, Sort.by(EntitiesFieldName.NOTIFICATION_CREATED_DATE).descending());
        }

        Page<Notification> notificationPage;
        if(role_id == AppRole.ROLE_ADMIN){
            notificationPage = this.notificationRepository.findAllByRoleWithPagination(pageable,AppRole.ROLE_ADMIN);
        }else if(role_id == AppRole.ROLE_INSTRUCTOR){
            notificationPage = this.notificationRepository.findAllByRoleAndObjectIdWithPagination(pageable,AppRole.ROLE_INSTRUCTOR,userId);
        }else{
            notificationPage = this.notificationRepository.findAllByRoleAndObjectIdWithPagination(pageable,AppRole.ROLE_LEARNER,userId);
        }

        return notificationPage;
    }

    public void sendNotificationToAdmin(String content,String objectName,int objectType,long objectId){
        Notification notification = new Notification();
        notification.setObjectTypeId(objectType);
        notification.setObjectName(objectName);
        notification.setObjectId(objectId);
        notification.setContent(content);
        notification.setCreateDate(TimeUtils.getCurrentTime());
        notification.setViewed(false);
        notification.setRoleTarget(AppRole.ROLE_ADMIN);
        this.create(notification);
    }

    public void sendNotificationToUser(String content,String objectName,int objectType,long objectId
            ,long userId,long roleTarget){
        Notification notification = new Notification();
        notification.setObjectTypeId(objectType);
        notification.setCreateDate(TimeUtils.getCurrentTime());
        notification.setContent(content);
        notification.setViewed(false);
        notification.setObjectId(objectId);

        User user = new User();
        user.setUserId(userId);
        notification.setUser(user);

        notification.setRoleTarget(roleTarget);
        notification.setObjectName(objectName);
        this.create(notification);
    }

    public void updateIsView(List<Long> notificationIds){
        this.notificationRepository.updateIsViewed(notificationIds);
    }
    //end public method
}
