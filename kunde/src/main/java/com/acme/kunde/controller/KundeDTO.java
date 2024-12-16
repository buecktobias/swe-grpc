/*
 * Copyright (C) 2022 - present Juergen Zimmermann, Hochschule Karlsruhe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.acme.kunde.controller;

import com.acme.kunde.entity.FamilienstandType;
import com.acme.kunde.entity.GeschlechtType;
import com.acme.kunde.entity.InteresseType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import org.hibernate.validator.constraints.UniqueElements;

/// ValueObject für das Neuanlegen und Ändern eines neuen Kunden. Beim Lesen wird die Klasse KundeModel für die Ausgabe
/// verwendet.
///
/// @author [Jürgen Zimmermann](mailto:Juergen.Zimmermann@h-ka.de)
/// @param nachname Gültiger Nachname eines Kunden, d.h. mit einem geeigneten Muster.
/// @param email Email eines Kunden.
/// @param kategorie Kategorie eines Kunden mit eingeschränkten Werten.
/// @param hasNewsletter Flag, ob es ein Newsletter-Abo gibt.
/// @param geburtsdatum Das Geburtsdatum eines Kunden.
/// @param homepage Die Homepage eines Kunden.
/// @param geschlecht Das Geschlecht eines Kunden.
/// @param familienstand Der Familienstand eines Kunden.
/// @param adresse Die Adresse eines Kunden.
/// @param umsaetze Die Umsaetze eines Kunden.
/// @param interessen Die Interessen eines Kunden.
@SuppressWarnings("RecordComponentNumber")
record KundeDTO(
    @NotNull
    @Pattern(regexp = NACHNAME_PATTERN)
    String nachname,

    @Email
    @NotNull
    String email,

    @Min(MIN_KATEGORIE)
    @Max(MAX_KATEGORIE)
    int kategorie,

    boolean hasNewsletter,

    @Past
    LocalDate geburtsdatum,

    URL homepage,
    GeschlechtType geschlecht,
    FamilienstandType familienstand,

    @Valid
    @NotNull(groups = OnCreate.class)
    AdresseDTO adresse,

    List<UmsatzDTO> umsaetze,

    @UniqueElements
    List<InteresseType> interessen
) {
    /// Muster für einen gültigen Nachnamen.
    public static final String NACHNAME_PATTERN =
        "(o'|von|von der|von und zu|van)?[A-ZÄÖÜ][a-zäöüß]+(-[A-ZÄÖÜ][a-zäöüß]+)?";

    /// Kleinster Wert für eine Kategorie.
    public static final long MIN_KATEGORIE = 0L;

    /// Maximaler Wert für eine Kategorie.
    public static final long MAX_KATEGORIE = 9L;

    /// Marker-Interface für _Jakarta Validation_: zusätzliche Validierung beim Neuanlegen.
    interface OnCreate { }
}
