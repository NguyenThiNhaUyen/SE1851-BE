package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.AddressDTO;
import com.quyet.superapp.dto.AddressRequestDTO;
import com.quyet.superapp.entity.address.Address;
import com.quyet.superapp.entity.address.Ward;

public class AddressMapper {

    // Chuyển từ Entity sang DTO để trả về client
    public static AddressDTO toDTO(Address address) {
        AddressDTO dto = new AddressDTO();
        dto.setAddressStreet(address.getAddressStreet());
        dto.setWardId(address.getWard().getWardId());
        dto.setWard(address.getWard().getWardName());
        dto.setDistrict(address.getWard().getDistrict().getDistrictName());
        dto.setCity(address.getWard().getDistrict().getCity().getNameCity());
        return dto;
    }

    // Dùng khi chỉ có AddressRequestDTO + Ward (đăng ký user)
    public static Address toEntity(AddressRequestDTO dto, Ward ward) {
        Address address = new Address();
        address.setAddressStreet(dto.getAddressStreet());
        address.setWard(ward);
        return address;
    }

    public static Address toEntity(AddressDTO dto, Ward ward) {
        Address address = new Address();
        address.setAddressStreet(dto.getAddressStreet());
        address.setWard(ward);
        return address;
    }
}
