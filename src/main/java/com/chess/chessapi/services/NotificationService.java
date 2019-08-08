package com.chess.chessapi.services;

import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.constants.Common;
import com.chess.chessapi.constants.EntitiesFieldName;
import com.chess.chessapi.entities.Notification;
import com.chess.chessapi.entities.User;
import com.chess.chessapi.models.PagedList;
import com.chess.chessapi.repositories.NotificationRepository;
import com.chess.chessapi.utils.ManualCastUtils;
import com.chess.chessapi.utils.TimeUtils;
import com.chess.chessapi.viewmodels.CoursePaginationViewModel;
import com.chess.chessapi.viewmodels.NotificationPaginationsViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @PersistenceContext
    private EntityManager em;
    //public method
    public Notification create(Notification notification){
        return notificationRepository.save(notification);
    }

    public NotificationPaginationsViewModel getPagination(int pageIndex, int pageSize, long role_id, long userId, String sortBy, String sortDirection){
        StoredProcedureQuery storedProcedureQuery = this.em.createNamedStoredProcedureQuery("getNotificationPagination");
        storedProcedureQuery.setParameter("role",role_id);
        Common.storedProcedureQueryPaginationSetup(storedProcedureQuery,pageIndex,pageSize,sortBy,sortDirection);
        storedProcedureQuery.setParameter("userId",userId);
        storedProcedureQuery.setParameter("totalNotViewedElements",Long.parseLong("0"));

        storedProcedureQuery.execute();

        List<Object> rawData = storedProcedureQuery.getResultList();
        long totalNotViewedElements = Long.parseLong(storedProcedureQuery.getOutputParameterValue("totalNotViewedElements").toString());
        final long totalElements = Long.parseLong(storedProcedureQuery.getOutputParameterValue("totalElements").toString());
        return new NotificationPaginationsViewModel(this.fillDataToPaginationCustom(rawData,totalElements,pageSize),totalNotViewedElements);
    }

    public void sendNotificationToAdmin(String content,String objectName,String objectAvatar,int objectType,long objectId){
        Notification notification = new Notification();
        notification.setObjectTypeId(objectType);
        notification.setObjectName(objectName);
        notification.setObjectId(objectId);
        notification.setContent(content);
        notification.setCreateDate(TimeUtils.getCurrentTime());
        notification.setViewed(false);
        notification.setObjectAvatar(objectAvatar);
        notification.setRoleTarget(AppRole.ROLE_ADMIN);
        this.create(notification);
    }

    public void sendNotificationToUser(String content,String objectName,String objectAvatar,int objectType,long objectId
            ,long userId,long roleTarget){
        Notification notification = new Notification();
        notification.setObjectTypeId(objectType);
        notification.setCreateDate(TimeUtils.getCurrentTime());
        notification.setContent(content);
        notification.setViewed(false);
        notification.setObjectId(objectId);
        notification.setObjectAvatar(objectAvatar);

        User user = new User();
        user.setUserId(userId);
        notification.setUser(user);

        notification.setRoleTarget(roleTarget);
        notification.setObjectName(objectName);
        this.create(notification);
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateIsView(List<Long> notificationIds){
        this.notificationRepository.updateIsViewed(notificationIds);
    }
    //end public method

    //PRIVATE METHOD DEFINED
    private PagedList<Notification> fillDataToPaginationCustom(List<Object> rawData, long totalElements, int pageSize){
        long totalPages = (long) Math.ceil(totalElements / (double) pageSize);
        List<Notification> data = ManualCastUtils.castListObjectToNotification(rawData);
        return new PagedList<Notification>(Math.toIntExact(totalPages),totalElements,data);
    }
    //END PRIVATE METHOD DEFINED
}
