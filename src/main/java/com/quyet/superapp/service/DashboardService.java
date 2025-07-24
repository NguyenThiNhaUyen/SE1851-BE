package com.quyet.superapp.service;

import com.quyet.superapp.dto.BloodGroupDistributionDTO;
import com.quyet.superapp.dto.DashboardOverviewDTO;
import com.quyet.superapp.dto.DashboardResponseDTO;
import com.quyet.superapp.dto.GroupStat;
import com.quyet.superapp.enums.BloodRequestStatus;
import com.quyet.superapp.enums.UrgencyLevel;
import com.quyet.superapp.repository.BloodInventoryRepository;
import com.quyet.superapp.repository.BloodRequestRepository;
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DonationRepository donationRepository;
    private final BloodInventoryRepository bloodInventoryRepository;
    private final BloodRequestRepository bloodRequestRepository;

    public DashboardOverviewDTO getDashboardOverview() {
        LocalDate today = LocalDate.now();

        long donorsToday = donationRepository.countByCreatedAtBetween(
                today.atStartOfDay(),
                today.plusDays(1).atStartOfDay()
        );

        long bloodUnitsAvailable = bloodInventoryRepository.countByTotalQuantityMlGreaterThan(0);

        long urgentRequests = bloodRequestRepository.countByUrgencyLevelAndStatusIn(
                UrgencyLevel.KHAN_CAP,
                List.of(BloodRequestStatus.PENDING, BloodRequestStatus.WAITING_DONOR)
        );


        List<BloodGroupDistributionDTO> distribution = bloodInventoryRepository
                .calculateBloodGroupDistribution();

        return DashboardOverviewDTO.builder()
                .donorsToday(donorsToday)
                .bloodUnitsAvailable(bloodUnitsAvailable)
                .urgentRequestsCount(urgentRequests)
                .bloodDistribution(distribution)
                .build();
    }
}