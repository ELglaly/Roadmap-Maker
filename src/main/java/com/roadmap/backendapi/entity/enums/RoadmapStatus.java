package com.roadmap.backendapi.entity.enums;

public enum RoadmapStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
    ARCHIVED,
    DRAFT;

    /**
     * Returns the next status in the sequence.
     * If the current status is the last one, it returns the last status.
     *
     * @return The next status or the last status if already at the end.
     */
    public RoadmapStatus next() {
        return this.ordinal() < RoadmapStatus.values().length - 1 ? RoadmapStatus.values()[this.ordinal() + 1] : this;
    }
}
