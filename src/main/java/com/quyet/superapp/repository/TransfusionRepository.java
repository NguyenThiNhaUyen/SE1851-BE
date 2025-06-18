package com.quyet.superapp.repository;

import com.quyet.superapp.entity.Transfusion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransfusionRepository extends JpaRepository<Transfusion, Long> {
    
}
