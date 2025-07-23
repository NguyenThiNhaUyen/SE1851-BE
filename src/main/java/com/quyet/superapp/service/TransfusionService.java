package com.quyet.superapp.service;

import com.quyet.superapp.entity.Transfusion;
import com.quyet.superapp.repository.TransfusionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

<<<<<<< HEAD
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
=======
import java.util.List;
>>>>>>> origin/main

@Service
@RequiredArgsConstructor
public class TransfusionService {
    private final TransfusionRepository repo;
<<<<<<< HEAD

    public List<Transfusion> findAll() {
        return repo.findAll();
    }

    public List<Transfusion> filterTransfusions(Long recipientId, LocalDateTime fromDate, LocalDateTime toDate) {
        return repo.filterTransfusions(recipientId, fromDate, toDate);
    }

    public Transfusion findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản ghi truyền máu với ID: " + id));
    }
}
=======
    public List<Transfusion> findAll() {
        return repo.findAll();
    }
}

>>>>>>> origin/main
