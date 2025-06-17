package com.quyet.superapp.service;

    import com.quyet.superapp.dto.AddressRequestDTO;
    import com.quyet.superapp.entity.address.Address;
    import com.quyet.superapp.entity.address.Ward;
    import com.quyet.superapp.mapper.AddressMapper;
    import com.quyet.superapp.repository.address.AddressRepository;
    import com.quyet.superapp.repository.address.WardRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;

    @Service
    @RequiredArgsConstructor
    public class AddressService {

        private final AddressRepository addressRepository;
        private final WardRepository wardRepository;

        public Address createAddressFromDTO(AddressRequestDTO dto) {
            Ward ward = wardRepository.findById(dto.getWardId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy phường/xã"));

            Address address = AddressMapper.toEntity(dto, ward);
            return addressRepository.save(address);
        }
    }
