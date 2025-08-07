package com.roadmap.backendapi.entity.enums;

public enum AddressType {
    HOME,
    WORK,
    OTHER;

    public static AddressType fromString(String type) {
        for (AddressType addressType : AddressType.values()) {
            if (addressType.name().equalsIgnoreCase(type)) {
                return addressType;
            }
        }
        throw new IllegalArgumentException("Unknown address type: " + type);
    }
}
