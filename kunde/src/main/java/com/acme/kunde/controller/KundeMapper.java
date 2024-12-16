/*
 * Copyright (C) 2023 - present Juergen Zimmermann, Hochschule Karlsruhe
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

import com.acme.kunde.entity.Adresse;
import com.acme.kunde.entity.Kunde;
import com.acme.kunde.entity.Umsatz;
import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

/// Mapper zwischen Entity-Klassen.
/// Siehe `build\generated\sources\annotationProcessor\java\main\...\KundeMapperImpl.java`.
///
/// @author [Jürgen Zimmermann](mailto:Juergen.Zimmermann@h-ka.de)
@Mapper(nullValueIterableMappingStrategy = RETURN_DEFAULT, componentModel = "spring")
@AnnotateWith(ExcludeFromJacocoGeneratedReport.class)
interface KundeMapper {
    /// Ein DTO-Objekt von [KundeDTO] in ein Objekt für [Kunde] konvertieren.
    ///
    /// @param dto DTO-Objekt für `KundeDTO` ohne ID
    /// @return Konvertiertes `Kunde-Objekt` mit null als ID
    @Mapping(target = "id", ignore = true)
    Kunde toKunde(KundeDTO dto);

    /// Ein DTO-Objekt von [AdresseDTO] in ein Objekt für [Adresse] konvertieren.
    ///
    /// @param dto DTO-Objekt für `AdresseDTO`
    /// @return Konvertiertes `Adresse`-Objekt
    Adresse toAdresse(AdresseDTO dto);

    /// Ein DTO-Objekt von [UmsatzDTO] in ein Objekt für [Umsatz] konvertieren.
    ///
    /// @param dto DTO-Objekt für `UmsatzDTO`
    /// @return Konvertiertes `Umsatz`-Objekt
    Umsatz toUmsatz(UmsatzDTO dto);
}
