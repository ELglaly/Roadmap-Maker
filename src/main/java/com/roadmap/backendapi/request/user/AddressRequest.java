package com.roadmap.backendapi.request.user;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddressRequest {

    private String street;

    private String city;

    private String zip;

    private String country;
}
