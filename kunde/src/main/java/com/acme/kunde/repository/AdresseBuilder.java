/*
 * Copyright (C) 2024 - present Juergen Zimmermann, Hochschule Karlsruhe
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
package com.acme.kunde.repository;

import com.acme.kunde.entity.Adresse;

/// Builder-Klasse für die Klasse [Adresse].
///
/// @author ![Jürgen Zimmermann](mailto:Juergen.Zimmermann@h-ka.de)
public class AdresseBuilder {
    private String plz;
    private String ort;

    /// Ein Builder-Objekt für die Klasse [Adresse] bauen.
    ///
    /// @return Das Builder-Objekt.
    public static AdresseBuilder getBuilder() {
        return new AdresseBuilder();
    }

    /// Postleitzahl setzen.
    /// @param plz Die Postleitzahl
    /// @return Builder-Objekt.
    public AdresseBuilder setPlz(final String plz) {
        this.plz = plz;
        return this;
    }

    /// Ort setzen.
    /// @param ort Der Ort.
    /// @return Builder-Objekt.
    public AdresseBuilder setOrt(final String ort) {
        this.ort = ort;
        return this;
    }

    /// Adresse bauen.
    /// @return Adresse-Objekt.
    public Adresse build() {
        return new Adresse(plz, ort);
    }
}
