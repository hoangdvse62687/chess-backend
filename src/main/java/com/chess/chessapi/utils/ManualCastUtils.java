package com.chess.chessapi.utils;

import com.chess.chessapi.entities.User;
import com.chess.chessapi.viewmodels.UserPaginationViewModel;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ManualCastUtils implements Serializable {

    private static final int USER_ID_INDEX = 0;
    private static  final int USER_EMAIL_INDEX = 1;
    private static final int USER_ROLE_INDEX = 2;
    private static final int USER_ISACTIVE_INDEX = 3;
    private static final int USER_AVATAR_INDEX = 4;
    private static final int USER_FULLNAME_INDEX = 5;
    private static final int USER_CREATEDDATE_INDEX = 6;

    public static User castObjectToUserByFindCustom(Object object){
        if(object == null){
            return null;
        }
        User user = new User();
        Object[] data = (Object[]) object;
        user.setId(Long.parseLong(data[USER_ID_INDEX].toString()));
        user.setEmail(data[USER_EMAIL_INDEX].toString());
        user.setRoleId(Long.parseLong(data[USER_ROLE_INDEX].toString()));
        user.setActive(Boolean.parseBoolean(data[USER_ISACTIVE_INDEX].toString()));
        return user;
    }

    public static List<UserPaginationViewModel> castPageObjectsoUser(Page<Object> objects){
        List<UserPaginationViewModel> users = new ArrayList<>();
        for (Object object:
             objects.getContent()) {
            UserPaginationViewModel user = new UserPaginationViewModel();
            Object[] data = (Object[]) object;
            user.setId(Long.parseLong(data[USER_ID_INDEX].toString()));
            user.setEmail(data[USER_EMAIL_INDEX].toString());
            user.setRole_id(Integer.parseInt(data[USER_ROLE_INDEX].toString()));
            user.setIs_active(Integer.parseInt(data[USER_ISACTIVE_INDEX].toString()));
            user.setAvatar(data[USER_AVATAR_INDEX].toString());
            user.setFull_name(data[USER_FULLNAME_INDEX].toString());
            user.setCreated_date(Timestamp.valueOf(data[USER_CREATEDDATE_INDEX].toString()));
            users.add(user);
        }
        return users;
    }
}
