package com.chatore.auth.service.Impl;

import com.chatore.auth.dto.request.CreateAddressRequest;
import com.chatore.auth.dto.request.UpdateAddressRequest;
import com.chatore.auth.dto.response.AddressResponse;
import com.chatore.auth.entity.User;
import com.chatore.auth.entity.UserAddress;
import com.chatore.auth.exception.custom.ResourceNotFoundException;
import com.chatore.auth.repository.UserAddressRepository;
import com.chatore.auth.repository.UserRepository;
import com.chatore.auth.security.AuthenticationUtil;
import com.chatore.auth.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;
    private final AuthenticationUtil authenticationUtil;

    @Override
    @Transactional
    public AddressResponse createAddress(CreateAddressRequest request) {

        User user = getCurrentUser();

        boolean isFirstAddress =
                userAddressRepository.countByUser(user) == 0;

        boolean shouldBeDefault =
                isFirstAddress ||
                        Boolean.TRUE.equals(request.getIsDefault());

        if (shouldBeDefault) {

            userAddressRepository
                    .findByUserAndIsDefaultTrue(user)
                    .ifPresent(address ->
                            address.setIsDefault(false)
                    );
        }

        UserAddress address = UserAddress.builder()
                .user(user)
                .label(request.getLabel())
                .addressLine1(request.getAddressLine1())
                .addressLine2(request.getAddressLine2())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .isDefault(shouldBeDefault)
                .build();

        return mapToResponse(
                userAddressRepository.save(address)
        );
    }

    @Override
    public List<AddressResponse> getAddresses() {

        User user = getCurrentUser();

        return userAddressRepository
                .findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AddressResponse getAddressById(UUID id) {

        User user = getCurrentUser();

        UserAddress address =
                userAddressRepository
                        .findByIdAndUser(id, user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Address not found"
                                ));

        return mapToResponse(address);
    }

    @Override
    @Transactional
    public AddressResponse updateAddress(UUID id, UpdateAddressRequest request) {
        User user = getCurrentUser();

        UserAddress userAddress= userAddressRepository.findByIdAndUser(id, user)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Address not found"
                ));

        userAddress.setAddressLine1(request.getAddressLine1());
        userAddress.setAddressLine2(request.getAddressLine2());
        userAddress.setCity(request.getCity());
        userAddress.setState(request.getState());
        userAddress.setPincode(request.getPincode());
        userAddress.setLatitude(request.getLatitude());
        userAddress.setLongitude(request.getLongitude());
        userAddress.setLabel(request.getLabel());


        return mapToResponse(userAddress);
    }

    @Override
    @Transactional
    public void deleteAddress(UUID id) {

        User user = getCurrentUser();

        UserAddress address = userAddressRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found"
                        )
                );

        boolean wasDefault =
                Boolean.TRUE.equals(address.getIsDefault());

        userAddressRepository.delete(address);

        if (!wasDefault) {
            return;
        }

        userAddressRepository
                .findTopByUserOrderByUpdatedAtDesc(user)
                .ifPresent(remainingAddress ->
                        remainingAddress.setIsDefault(true)
                );
    }

    @Override
    @Transactional
    public AddressResponse setDefaultAddress(UUID id) {

        User user = getCurrentUser();

        UserAddress newDefault = userAddressRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found"
                        )
                );

        if (Boolean.TRUE.equals(newDefault.getIsDefault())) {
            return mapToResponse(newDefault);
        }

        userAddressRepository
                .findByUserAndIsDefaultTrue(user)
                .ifPresent(currentDefault ->
                        currentDefault.setIsDefault(false)
                );

        newDefault.setIsDefault(true);

        return mapToResponse(newDefault);
    }

    private User getCurrentUser() {

        UUID userId =
                authenticationUtil.getLoggedInUserId();

        return userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );
    }

    private AddressResponse mapToResponse(
            UserAddress address
    ) {

        return AddressResponse.builder()
                .id(address.getId())
                .label(address.getLabel().name())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .city(address.getCity())
                .state(address.getState())
                .pincode(address.getPincode())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .isDefault(address.getIsDefault())
                .build();
    }
}
