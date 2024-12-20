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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.acme.kunde.controller;

import com.acme.kunde.entity.FamilienstandType;
import com.acme.kunde.entity.GeschlechtType;
import com.acme.kunde.entity.InteresseType;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

@SuppressWarnings({"RecordComponentNumber", "WriteTag"})
record KundeDownload(
    String nachname,
    String email,
    int kategorie,
    boolean hasNewsletter,
    LocalDate geburtsdatum,
    URL homepage,
    GeschlechtType geschlecht,
    FamilienstandType familienstand,
    AdresseDTO adresse,
    List<UmsatzDTO> umsaetze,
    List<InteresseType> interessen,
    @SuppressWarnings("RecordComponentName")
    HateoasLinks _links
) {
}
