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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;

/// Enum für Geschlecht. Dazu können auf der Clientseite z.B. Radiobuttons realisiert werden.
///
/// @author [Jürgen Zimmermann](mailto:Juergen.Zimmermann@h-ka.de)
public enum GeschlechtType {
    /// _Männlich_ mit dem internen Wert `M` für z.B. das Mapping in einem JSON-Datensatz.
    MAENNLICH("M"),

    /// _Weiblich_ mit dem internen Wert `W` für z.B. das Mapping in einem JSON-Datensatz.
    WEIBLICH("W"),

    /// _Divers_ mit dem internen Wert `D` für z.B. das Mapping in einem JSON-Datensatz.
    DIVERS("D");

    private final String value;

    GeschlechtType(final String value) {
        this.value = value;
    }

    /// Einen enum-Wert als String mit dem internen Wert ausgeben.
    /// Dieser Wert wird durch Jackson in einem JSON-Datensatz verwendet.
    /// [Wiki-Seiten](https://github.com/FasterXML/jackson-databind/wiki)
    ///
    /// @return Interner Wert
    @JsonValue
    public String getValue() {
        return value;
    }

    /// Konvertierung eines Strings in einen Enum-Wert.
    ///
    /// @param value Der String, zu dem ein passender Enum-Wert ermittelt werden soll.
    /// @return Passender Enum-Wert oder null.
    @JsonCreator
    public static GeschlechtType of(final String value) {
        return Stream.of(values())
            .filter(geschlecht -> geschlecht.value.equalsIgnoreCase(value))
            .findFirst()
            .orElse(null);
    }
}
