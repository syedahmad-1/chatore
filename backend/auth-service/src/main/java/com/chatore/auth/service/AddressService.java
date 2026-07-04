package com.chatore.auth.service;


import com.chatore.auth.dto.request.CreateAddressRequest;
import com.chatore.auth.dto.request.UpdateAddressRequest;
import com.chatore.auth.dto.response.AddressResponse;

import java.util.List;
import java.util.UUID;

public interface AddressService {

    AddressResponse createAddress(
            CreateAddressRequest request
    );

    List<AddressResponse> getAddresses();

    AddressResponse getAddressById(UUID id);

    AddressResponse updateAddress(
            UUID id,
            UpdateAddressRequest request
    );

    void deleteAddress(UUID id);

    AddressResponse setDefaultAddress(UUID id);

}
