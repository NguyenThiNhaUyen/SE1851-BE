package com.quyet.superapp.repository;

<<<<<<< HEAD
import com.quyet.superapp.entity.BloodRequest;
=======
>>>>>>> origin/main
import com.quyet.superapp.entity.VnPayment;
import org.springframework.data.jpa.repository.JpaRepository;



import org.springframework.stereotype.Repository;

<<<<<<< HEAD
import java.util.List;
import java.util.Optional;

=======
>>>>>>> origin/main
@Repository

public interface VnPaymentRepository extends JpaRepository<VnPayment, Long> {
    long countByStatus(String status);
<<<<<<< HEAD

    List<VnPayment> findAllByBloodRequest_Id(Long requestId);


=======
>>>>>>> origin/main
}