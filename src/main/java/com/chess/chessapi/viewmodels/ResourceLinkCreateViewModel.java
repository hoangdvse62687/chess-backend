package com.chess.chessapi.viewmodels;

import org.hibernate.validator.constraints.Length;

public class ResourceLinkCreateViewModel {
    @Length(max = 255,message = "Link must not larger than 255 characters")
    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
