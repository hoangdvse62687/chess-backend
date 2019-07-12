package com.chess.chessapi.services;

import com.chess.chessapi.entities.ResourceLink;
import com.chess.chessapi.entities.User;
import com.chess.chessapi.repositories.ResourceLinkRepository;
import com.chess.chessapi.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class ResourceLinkService {
    @Autowired
    private ResourceLinkRepository resourceLinkRepository;

    //PUBLIC METHOD
    public long create(String link, long userId){
        ResourceLink resourceLink = new ResourceLink();
        resourceLink.setLink(link);
        User user = new User();
        user.setUserId(userId);
        resourceLink.setUser(user);
        resourceLink.setCreatedDate(TimeUtils.getCurrentTime());

        ResourceLink savedResourceLink = this.resourceLinkRepository.save(resourceLink);
        return savedResourceLink.getResourceLinkId();
    }

    public List<String> getAllLinkByUserId(long userId){
        return  this.resourceLinkRepository.findAllByUserId(userId);
    }

    public void remove(long id){
        this.resourceLinkRepository.remove(id);
    }

    public boolean isExist(long id){
        return this.resourceLinkRepository.existsById(id);
    }
    //END PUBLIC METHOD
}
