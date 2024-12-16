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

package com.acme.kunde.dev;

import org.springframework.context.annotation.Profile;
import static com.acme.kunde.dev.DevConfig.DEV;

/// Konfigurationsklasse für die Anwendung bzw. den Microservice, falls das Profile `dev` aktiviert ist.
///
/// @author [Jürgen Zimmermann](mailto:Juergen.Zimmermann@h-ka.de)
@Profile(DEV)
@SuppressWarnings({"ClassNamePrefixedWithPackageName", "HideUtilityClassConstructor"})
public final class DevConfig implements LogRequestHeaders, K8s {
    /// Konstante für das Spring-Profile `dev`.
    public static final String DEV = "dev";

    /// Konstruktor mit _package private_ für _Spring_.
    DevConfig() {
    }
}
