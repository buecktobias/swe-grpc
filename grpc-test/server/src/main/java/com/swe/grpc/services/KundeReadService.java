package com.swe.grpc.services;

import com.swe.grpc.entity.Kunde;
import com.swe.grpc.repository.KundeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KundeReadService {
    private final KundeRepository kundenRepository;
    public KundeReadService(
            final KundeRepository kundenRepository
    ) {
        this.kundenRepository = kundenRepository;
    }
    public List<Kunde> findAll() {
        return kundenRepository.findAll();
    }

    public Optional<Kunde> findById(final int id) {
        return kundenRepository.findById(id);
    }

    public List<Kunde> findByNachname(final String nachname) {
        return kundenRepository.findByNachname(nachname);
    }
}
