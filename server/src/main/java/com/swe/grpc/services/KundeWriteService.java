package com.swe.grpc.services;


import com.swe.grpc.entity.Kunde;
import org.springframework.stereotype.Service;

@Service
public class KundeWriteService {

    private final KundeRepository kundeRepository;

    public KundeWriteService(KundeRepository kundeRepository) {
        this.kundeRepository = kundeRepository;
    }

    public Kunde createKunde(Kunde kunde) {
        return kundeRepository.save(kunde); // Save the Kunde in the database
    }

    public void deleteKunde(UUID id) {
        if (!kundeRepository.existsById(id)) {
            throw new IllegalArgumentException("Kunde with ID " + id + " does not exist.");
        }
        kundeRepository.deleteById(id); // Delete the Kunde
    }
}

