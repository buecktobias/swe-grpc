package com.swe.grpc.entity;

import java.time.LocalDate;

public record Kunde(int id, String vorname, String nachname, String email, LocalDate geburtsdatum) {
}
