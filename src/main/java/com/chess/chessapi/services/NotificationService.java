package com.chess.chessapi.services;

import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.constants.Common;
import com.chess.chessapi.entities.Notification;
import com.chess.chessapi.entities.User;
import com.chess.chessapi.models.PagedList;
import com.chess.chessapi.repositories.NotificationRepository;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.utils.ManualCastUtils;
import com.chess.chessapi.utils.TimeUtils;
import com.chess.chessapi.viewmodels.NotificationPaginationsViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserService userService;

    private final String targetURL = "https://fcm.googleapis.com/fcm/send";
    private final String SERVER_KEY = "AAAA_Ah8T8Y:APA91bG1UrCHo6_fYUf_Mj4WLDNkzgsbp7PEHliJ3-5zIbeen00KwuhCJY4EM6aTI3x26Fx8PZyPHIiZBkKswz5ctRlGthd8Ug_gqT3xo0H-0EyKZnEZZqwSHOuP5dExyuSEuMxpadfS";
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

        List<String> listAppIds = this.userService.findListAppIdByRoleId(AppRole.ROLE_ADMIN);
        for (String appIds:
                listAppIds) {
            if(appIds != null && !appIds.isEmpty()){
                String jsonInput = ManualCastUtils.castToNotificationJson(notification,appIds);
                this.sendPost(jsonInput);
            }
        }
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

        String appId = this.userService.findAppIdByUserId(userId);
        if(appId != null && !appId.isEmpty()){
            String jsonInput = ManualCastUtils.castToNotificationJson(notification,appId);
            this.sendPost(jsonInput);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateIsView(List<Long> notificationIds){
        this.notificationRepository.updateIsViewed(notificationIds);
    }

    public void updateNotificationTokenId(long userId,String token){
        this.userService.updateAppIdByUserId(token,userId);
    }
    //end public method

    //PRIVATE METHOD DEFINED
    private PagedList<Notification> fillDataToPaginationCustom(List<Object> rawData, long totalElements, int pageSize){
        long totalPages = (long) Math.ceil(totalElements / (double) pageSize);
        List<Notification> data = ManualCastUtils.castListObjectToNotification(rawData);
        return new PagedList<Notification>(Math.toIntExact(totalPages),totalElements,data);
    }

    private void sendPost(String jsonInputString){
        URL url;
        HttpURLConnection connection = null;
        try{
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/json");
            connection.setRequestProperty("Authorization",
                    "key=" + SERVER_KEY);
            connection.setDoOutput(true);

            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                System.out.println(br.readLine());
            }
        }catch (Exception ex){
           ex.printStackTrace();
        }finally {
            if(connection != null){
                connection.disconnect();
            }
        }
    }
    //END PRIVATE METHOD DEFINED
}
