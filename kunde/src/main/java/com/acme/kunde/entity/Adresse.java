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
package com.acme.kunde.entity;

/// Adressdaten für die Anwendungslogik und zum Abspeichern in der DB.
///
///  @author [Jürgen Zimmermann](mailto:Juergen.Zimmermann@h-ka.de)
@SuppressWarnings("RequireEmptyLineBeforeBlockTagGroup")
public class Adresse {
    private String plz;
    private String ort;

    /// Konstruktor mit allen notwendigen Argumenten.
    /// @param plz Die Postleitzahl.
    /// @param ort Der Ort.
    public Adresse(final String plz, final String ort) {
        this.plz = plz;
        this.ort = ort;
    }

    /// Postleitzahl ermitteln.
    /// @return Die Postleitzahl.
    public String getPlz() {
        return plz;
    }

    /// Die Postleitzahl setzen.
    /// @param plz Die Postleitzahl.
    public void setPlz(final String plz) {
        this.plz = plz;
    }

    /// Den Ort ermitteln.
    /// @return Der Ort.
    public String getOrt() {
        return ort;
    }

    /// Den Ort setzen.
    /// @param ort Der Ort.
    public void setOrt(final String ort) {
        this.ort = ort;
    }

    @Override
    public String toString() {
        return "Adresse{" + "plz='" + plz + '\'' + ", ort='" + ort + '\'' + '}';
    }
}
