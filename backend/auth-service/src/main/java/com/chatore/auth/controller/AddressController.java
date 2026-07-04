package com.chatore.auth.controller;

import com.chatore.auth.dto.request.CreateAddressRequest;
import com.chatore.auth.dto.request.UpdateAddressRequest;
import com.chatore.auth.dto.response.AddressResponse;
import com.chatore.auth.dto.response.ApiResponse;
import com.chatore.auth.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponse>> createAddress(
            @Valid @RequestBody CreateAddressRequest request
    ) {

        AddressResponse response = addressService.createAddress(request);

        return ResponseEntity.ok(
                ApiResponse.<AddressResponse>builder()
                        .success(true)
                        .message("Address created successfully")
                        .data(response)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAddresses() {

        return ResponseEntity.ok(
                ApiResponse.<List<AddressResponse>>builder()
                        .success(true)
                        .message("Addresses fetched successfully")
                        .data(addressService.getAddresses())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> getAddressById(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                ApiResponse.<AddressResponse>builder()
                        .success(true)
                        .message("Address fetched successfully")
                        .data(addressService.getAddressById(id))
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAddressRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.<AddressResponse>builder()
                        .success(true)
                        .message("Address updated successfully")
                        .data(addressService.updateAddress(id, request))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @PathVariable UUID id
    ) {

        addressService.deleteAddress(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Address deleted successfully")
                        .build()
        );
    }

    @PatchMapping("/{id}/default")
    public ResponseEntity<ApiResponse<AddressResponse>> setDefaultAddress(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                ApiResponse.<AddressResponse>builder()
                        .success(true)
                        .message("Default address updated successfully")
                        .data(addressService.setDefaultAddress(id))
                        .build()
        );
    }
}