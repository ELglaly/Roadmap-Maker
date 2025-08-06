package com.roadmap.backendapi.entity.enums;

public enum RoadmapVisibility {
    PUBLIC,
    PRIVATE,
    UNLISTED;

    /**
     * Returns the next visibility in the sequence.
     * If the current visibility is the last one, it returns the last visibility.
     *
     * @return The next visibility or the last visibility if already at the end.
     */
    public RoadmapVisibility next() {
        return this.ordinal() < RoadmapVisibility.values().length - 1
            ? RoadmapVisibility.values()[this.ordinal() + 1]
            : this;
    }
}
