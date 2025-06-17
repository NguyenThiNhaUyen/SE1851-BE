package com.quyet.superapp.repository.address;

import com.quyet.superapp.entity.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
