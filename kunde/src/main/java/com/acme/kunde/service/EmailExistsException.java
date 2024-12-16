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

/// Exception, falls die Emailadresse bereits existiert.
///
/// @author [Jürgen Zimmermann](mailto:Juergen.Zimmermann@h-ka.de)
public class EmailExistsException extends RuntimeException {
    /// Bereits vorhandene Emailadresse.
    private final String email;

    /// Konstruktor, falls Kunden mit einer fehlerhaften Email angelegt oder bearbeitet werden sollen.
    ///
    /// @param email Die fehlerhafte Email.
    EmailExistsException(@SuppressWarnings("ParameterHidesMemberVariable") final String email) {
        super("Die Emailadresse " + email + " existiert bereits");
        this.email = email;
    }

    /// Fehlerhafte Email ermitteln.
    ///
    /// @return Die fehlerhafte Email.
    public String getEmail() {
        return email;
    }
}
