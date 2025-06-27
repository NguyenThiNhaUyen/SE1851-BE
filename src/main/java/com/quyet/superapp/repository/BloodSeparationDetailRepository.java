package com.quyet.superapp.repository;


import com.quyet.superapp.entity.BloodSeparationDetail;
import com.quyet.superapp.enums.BloodComponentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodSeparationDetailRepository extends JpaRepository<BloodSeparationDetail,Long> { //chưa sử dụng
    List<BloodSeparationDetail> findByResult_SeparationResultId(Long resultId);
    List<BloodSeparationDetail> findByComponentType(BloodComponentType componentType);

}
