package com.swe.grpc.repository;

import com.swe.grpc.entity.Kunde;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class KundeRepository {

    private final List<Kunde> kunden = List.of(
            new Kunde(1,
                    "Max",
                    "Mustermann",
                    "max@email.com",
                    LocalDate.of(1990, 1, 1)),
            new Kunde(2,
                    "Erika",
                    "Musterfrau",
                    "erika@email.com",
                    LocalDate.of(1991, 2, 2))
    );

    public List<Kunde> findAll() {
        return kunden;
    }

    public Optional<Kunde> findById(final int id) {
        return kunden.stream()
                .filter(kunde -> kunde.id() == id)
                .findFirst();
    }

    public List<Kunde> findByNachname(final String nachname) {
        return kunden.stream()
                .filter(kunde -> kunde.nachname().equals(nachname))
                .toList();
    }
}
