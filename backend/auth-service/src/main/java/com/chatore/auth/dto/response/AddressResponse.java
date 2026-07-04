package com.chatore.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@Builder
public class AddressResponse {

    private UUID id;

    private String label;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private String pincode;

    private Double latitude;

    private Double longitude;

    private Boolean isDefault;
}
