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
package com.acme.kunde.service;

import com.acme.kunde.entity.Kunde;
import com.acme.kunde.repository.KundeRepository;
import jakarta.validation.Valid;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/// Anwendungslogik für Kunden auch mit Bean Validation.
/// ![Klassendiagramm](../../../../../asciidoc/KundeWriteService.svg)
///
/// @author [Jürgen Zimmermann](mailto:Juergen.Zimmermann@h-ka.de)
// Maven: ![Klassendiagramm](../../../../../../generated-docs/KundeWriteService.svg)
@Service
public class KundeWriteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KundeWriteService.class);

    private final KundeRepository repo;

    /// Konstruktor mit _package private_ für _Spring_.
    ///
    /// @param repo Injiziertes Repository-Objekt.
    KundeWriteService(final KundeRepository repo) {
        this.repo = repo;
    }

    /// Einen neuen Kunden anlegen.
    ///
    /// @param kunde Das Objekt des neu anzulegenden Kunden.
    /// @return Der neu angelegte Kunden mit generierter ID
    /// @throws EmailExistsException Es gibt bereits einen Kunden mit der Emailadresse.
    public Kunde create(final Kunde kunde) {
        LOGGER.debug("create: {}", kunde);

        if (repo.isEmailExisting(kunde.getEmail())) {
            throw new EmailExistsException(kunde.getEmail());
        }

        final var kundeDB = repo.create(kunde);
        LOGGER.debug("create: {}", kundeDB);
        return kundeDB;
    }

    /// Einen vorhandenen Kunden aktualisieren.
    ///
    /// @param kunde Das Objekt mit den neuen Daten (ohne ID)
    /// @param id ID des zu aktualisierenden Kunden
    /// @throws NotFoundException Kein Kunde zur ID vorhanden.
    /// @throws EmailExistsException Es gibt bereits einen Kunden mit der Emailadresse.
    public void update(@Valid final Kunde kunde, final UUID id) {
        LOGGER.debug("update: {}", kunde);
        LOGGER.debug("update: id={}", id);

        // Ist die neue Email bei einem///ANDEREN* Kunden vorhanden?
        final var email = kunde.getEmail();
        final var kundeDb = repo
            .findById(id)
            .orElseThrow(() -> new NotFoundException(id));
        if (!Objects.equals(email, kundeDb.getEmail()) && repo.isEmailExisting(email)) {
            LOGGER.debug("update: email {} existiert", email);
            throw new EmailExistsException(email);
        }

        kunde.setId(id);
        repo.update(kunde);
    }

    /// Einen vorhandenen Kunden löschen.
    ///
    /// @param id Die ID des zu löschenden Kunden.
    public void deleteById(final UUID id) {
        LOGGER.debug("deleteById: id={}", id);
        repo.deleteById(id);
    }
}
