
import com.quyet.superapp.mapper.BloodBagMapper;
import com.quyet.superapp.repository.BloodBagRepository;
import com.quyet.superapp.repository.BloodTypeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BloodBagService {

    private final BloodBagRepository bloodBagRepository;
    private final DonationRepository donationRepository;


    public List<BloodBagDTO> getAll() {
        return bloodBagRepository.findAll()
                .stream()
                .map(BloodBagMapper::toDTO)
                .collect(Collectors.toList());
    }
