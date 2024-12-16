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

import java.util.List;
import java.util.Map;
import java.util.UUID;

/// [RuntimeException], falls kein Kunde gefunden wurde.
public final class NotFoundException extends RuntimeException {
    /// Fehlerhafte ID
    private final UUID id;

    ///  Fehlerhafte Suchkriterien
    private final Map<String, List<String>> suchkriterien;

    /// Standardkonstruktor für den [KundeReadService], wenn alle Kunden gesucht werden, aber keine existieren.
    NotFoundException() {
        super("Keine Kunden gefunden.");
        id = null;
        suchkriterien = null;
    }

    /// Konstruktor für den [KundeReadService] bei fehlerhafter ID.
    ///
    /// @param id Die fehlerhafte ID
    NotFoundException(final UUID id) {
        super("Kein Kunde mit der ID " + id + " gefunden.");
        this.id = id;
        suchkriterien = null;
    }

    /// Konstruktor für den [KundeReadService] bei fehlerhaften Suchkriterien.
    ///
    /// @param suchkriterien Die fehlerhaften Suchkriterien
    NotFoundException(final Map<String, List<String>> suchkriterien) {
        super("Keine Kunden gefunden.");
        id = null;
        this.suchkriterien = suchkriterien;
    }

    /// id ermitteln.
    ///
    /// @return Die fehlerhafte id.
    public UUID getId() {
        return id;
    }

    /// Suchkriterien ermitteln.
    ///
    /// @return Die fehlerhaften Suchkriterien.
    public Map<String, List<String>> getSuchkriterien() {
        return suchkriterien;
    }
}
