package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.AddressDTO;
import com.quyet.superapp.dto.AddressRequestDTO;
import com.quyet.superapp.entity.address.Address;
import com.quyet.superapp.entity.address.Ward;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    // 1. Không static, dùng khi inject qua @Autowired hoặc @RequiredArgsConstructor
    public Address toEntity(AddressRequestDTO dto, Ward ward) {
        Address address = new Address();
        address.setAddressStreet(dto.getAddressStreet());
        address.setWard(ward);
        return address;
    }

    // 2. Static, dùng cho các chỗ gọi trực tiếp
    public static Address toEntity(AddressDTO dto) {
        Address address = new Address();
        address.setAddressStreet(dto.getAddressStreet());
        Ward ward = new Ward();
        ward.setWardId(dto.getWardId());
        address.setWard(ward);
        return address;
    }
