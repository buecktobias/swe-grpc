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

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/// ValueObject für das Neuanlegen und Ändern eines neuen Kunden.
///
/// @author [Jürgen Zimmermann](mailto:Juergen.Zimmermann@h-ka.de)
/// @param plz Postleitzahl
/// @param ort Ort
record AdresseDTO(
    @NotNull
    @Pattern(regexp = PLZ_PATTERN)
    String plz,

    @NotBlank
    String ort
) {
    /// Konstante für den regulären Ausdruck einer Postleitzahl als 5-stellige Zahl mit führender Null.
    public static final String PLZ_PATTERN = "^\\d{5}$";
}
