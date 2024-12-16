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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/// Anwendungslogik für Kunden.
/// ![Klassendiagramm](../../../../../asciidoc/KundeReadService.svg)
///
/// @author [Jürgen Zimmermann](mailto:Juergen.Zimmermann@h-ka.de)
// Maven: ![Klassendiagramm](../../../../../../generated-docs/KundeReadService.svg)
@Service
public class KundeReadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KundeReadService.class);

    private final KundeRepository repo;

    /// Konstruktor mit _package private_ für _Spring_.
    ///
    /// @param repo Injiziertes Repository-Objekt.
    KundeReadService(final KundeRepository repo) {
        this.repo = repo;
    }

    /// Einen Kunden anhand seiner ID suchen.
    ///
    /// @param id Die Id des gesuchten Kunden
    /// @return Der gefundene Kunde
    /// @throws NotFoundException Falls kein Kunde gefunden wurde
    public @NonNull Kunde findById(final UUID id) {
        LOGGER.debug("findById: id={}", id);
        final var kunde = repo.findById(id)
            .orElseThrow(() -> new NotFoundException(id));
        LOGGER.debug("findById: {}", kunde);
        return kunde;
    }

    /// Kunden anhand von Suchkriterien als Collection suchen.
    ///
    /// @param suchkriterien Die Suchkriterien
    /// @return Die gefundenen Kunden oder eine leere Liste
    /// @throws NotFoundException Falls keine Kunden gefunden wurden
    @SuppressWarnings({"ReturnCount", "NestedIfDepth"})
    public @NonNull Collection<Kunde> find(@NonNull final Map<String, List<String>> suchkriterien) {
        LOGGER.debug("find: suchkriterien={}", suchkriterien);

        if (suchkriterien.isEmpty()) {
            return repo.findAll();
        }

        if (suchkriterien.size() == 1) {
            final var nachnamen = suchkriterien.get("nachname");
            if (nachnamen != null && nachnamen.size() == 1) {
                final var kunden = repo.findByNachname(nachnamen.getFirst());
                if (kunden.isEmpty()) {
                    throw new NotFoundException(suchkriterien);
                }
                LOGGER.debug("find (nachname): {}", kunden);
                return kunden;
            }

            final var emails = suchkriterien.get("email");
            if (emails != null && emails.size() == 1) {
                final var kunde = repo
                    .findByEmail(emails.getFirst())
                    .orElseThrow(() -> new NotFoundException(suchkriterien));
                final var kunden = List.of(kunde);
                LOGGER.debug("find (email): {}", kunden);
                return kunden;
            }
        }

        final var kunden = repo.find(suchkriterien);
        if (kunden.isEmpty()) {
            throw new NotFoundException(suchkriterien);
        }
        LOGGER.debug("find: {}", kunden);
        return kunden;
    }

    /// Abfrage, welche Nachnamen es zu einem Präfix gibt.
    ///
    /// @param prefix Nachname-Präfix.
    /// @return Die passenden Nachnamen.
    /// @throws NotFoundException Falls keine Nachnamen gefunden wurden.
    public Collection<String> findNachnamenByPrefix(final String prefix) {
        final var nachnamen = repo.findNachnamenByPrefix(prefix);
        if (nachnamen.isEmpty()) {
            //noinspection NewExceptionWithoutArguments
            throw new NotFoundException();
        }
        return nachnamen;
    }
}
