package com.swe.grpc.repository;

import com.swe.grpc.entity.Kunde;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class KundeRepository {

    private final List<Kunde> kunden = List.of(
            new Kunde(UUID.of(1),
                    "Mustermann",
                    "max.mustermann@web.de",
                    3,
                    false,
                    LocalDate.of(1990, 1, 1),
                    URL.of("https://www.mustermann.de"),
                    GeschlechtType.of("M"),
                    FamilienstandType.of("L"),
                    Adresse.of("76131", "Karlsruhe"),
                    Umsatz.of(BigDecimal.valueOf(200), Currency.of("EURO").toList()),
                    InteresseType.of("S").toList()
                    ),
            /* new Kunde(2,
                    "Erika",
                    "Musterfrau",
                    "erika@email.com",
                    LocalDate.of(1991, 2, 2))
    ); */
            new Kunde(UUID.of(2),
                "Musterfrau",
                "erika.musterfrau@web.de",
                2,
                false,
                LocalDate.of(1991, 2, 2),
                URL.of("https://www.musterfrau.de"),
                GeschlechtType.of("W"),
                FamilienstandType.of("VH"),
                Adresse.of("76131", "Karlsruhe"),
                Umsatz.of(BigDecimal.valueOf(300), Currency.of("EURO").toList()),
                InteresseType.of("R").toList()
            )

            
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
