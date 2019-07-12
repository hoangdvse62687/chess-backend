package com.chess.chessapi.viewmodels;

import javax.validation.constraints.NotNull;

public class ResourceLinkRemoveViewModel {
    @NotNull(message = "Resource link id must not be null")
    private long resourcelinkId;

    public long getResourcelinkId() {
        return resourcelinkId;
    }

    public void setResourcelinkId(long resourcelinkId) {
        this.resourcelinkId = resourcelinkId;
    }
}
