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
package com.acme.kunde.config;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

/// Konfigurationsklasse für die Anwendung bzw. den Microservice.
///
/// @author [Jürgen Zimmermann](mailto:Juergen.Zimmermann@h-ka.de)
public final class ApplicationConfig {
    /// Konstruktor mit _package private_ für _Spring_.
    ApplicationConfig() {
    }

    // https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#native-image.advanced.custom-hints
    // https://stackoverflow.com/questions/76287163/...
    // ...how-to-specify-the-location-of-a-keystore-file-with-spring-aot-processing
    /// Registrierung für _GraalVM_:
    /// - `PEM`- und `CRT`-Dateien für TLS
    /// - SQL-Skripte für _Flyway_
    public static class GraalVmHints implements RuntimeHintsRegistrar {
        /// Konstruktor mit _package private_ für _Spring_.
        GraalVmHints() {
        }

        @Override
        public void registerHints(final RuntimeHints hints, final ClassLoader classLoader) {
            hints.resources()
                .registerPattern("*.pem")
                .registerPattern("*.crt")
                // https://github.com/spring-projects/spring-boot/issues/31999
                // https://github.com/flyway/flyway/issues/2927
                .registerPattern("*.sql");
        }
    }
}
