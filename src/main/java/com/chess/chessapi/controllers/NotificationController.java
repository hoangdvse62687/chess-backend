package com.chess.chessapi.controllers;

import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.entities.Notification;
import com.chess.chessapi.exceptions.ResourceNotFoundException;
import com.chess.chessapi.models.JsonResult;
import com.chess.chessapi.models.PagedList;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.services.NotificationService;
import com.chess.chessapi.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/notification")
@Api(value = "Notification Management")
public class NotificationController {
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;

    @ApiOperation(value = "Get notification pagings")
    @GetMapping("/get-notifications-pagination")
    @PreAuthorize("hasAnyAuthority("+AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+","+AppRole.ROLE_LEARNER_AUTHENTICATIION+"" +
            ","+AppRole.ROLE_ADMIN_AUTHENTICATIION+")")
    public JsonResult getNotifications(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize,
                                      boolean sortIsViewed){
        UserPrincipal currentUser = userService.getCurrentUser();

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.addAll(currentUser.getAuthorities());
        long role_id = AppRole.ROLE_LEARNER;
        for (GrantedAuthority authority:
             authorities) {
            role_id = Integer.parseInt(authority.toString());
        }
        Page<Notification> listNofication = null;
        try{
            listNofication = notificationService
                    .getPagination(page,pageSize, role_id,currentUser.getId().toString(),sortIsViewed);
        }catch (IllegalArgumentException ex){
            throw new ResourceNotFoundException("Page","number",page);
        }
        PagedList<Notification> data = new PagedList<>(listNofication.getTotalPages()
                ,listNofication.getTotalElements(),listNofication.getContent());
        return new JsonResult("",data);
    }
}
