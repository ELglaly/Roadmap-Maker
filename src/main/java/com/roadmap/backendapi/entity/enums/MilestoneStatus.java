package com.roadmap.backendapi.entity.enums;

/**
 * Enum representing the status of a milestone.
 * This enum is used to indicate the current status of a milestone in the application.
 *
 * @see com.roadmap.backendapi.entity.Milestone
 */
public enum MilestoneStatus {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED,
        ARCHIVED;

    /**
     * Returns the next status in the sequence.
     * If the current status is the last one, it returns the last status.
     * * @return The next status or the last status if already at the end.
     */
    public MilestoneStatus next() {
        return this.ordinal() < MilestoneStatus.values().length - 1
            ? MilestoneStatus.values()[this.ordinal() + 1]
            : this;
    }
}
