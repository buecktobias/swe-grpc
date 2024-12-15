package com.swe.grpc.entity;

import java.time.LocalDate;
import java.util.UUID;
import java.util.List;
import java.util.Objects;
import java.net.URL;


/* public record Kunde(int id, String vorname, String nachname, String email, LocalDate geburtsdatum) {
} */

public record Kunde(UUID id, String nachname, String email, int kategorie,
    boolean hasNewsletter, LocalDate geburtsdatum,URL homepage, GeschlechtType geschlecht,
    FamilienstandType familienstand,Adresse adresse, List<Umsatz> umsaetze, List<InteresseType> interessen) {
}
