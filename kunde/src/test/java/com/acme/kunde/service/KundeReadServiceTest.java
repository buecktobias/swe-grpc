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
package com.acme.kunde.service;

import com.acme.kunde.entity.Kunde;
import com.acme.kunde.repository.KundeRepository;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.junit.jupiter.api.condition.JRE.JAVA_23;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

@Tag("unit")
@Tag("service-read")
@DisplayName("Anwendungskern fuer Lesen testen")
@Execution(CONCURRENT)
@EnabledForJreRange(min = JAVA_23, max = JAVA_23)
@ExtendWith(SoftAssertionsExtension.class)
@SuppressWarnings("WriteTag")
class KundeReadServiceTest {
    private static final String ID_VORHANDEN = "00000000-0000-0000-0000-000000000001";
    private static final String ID_NICHT_VORHANDEN = "ffffffff-ffff-ffff-ffff-ffffffffffff";
    private static final String NACHNAME = "Alpha";

    private final KundeRepository repo = new KundeRepository();
    private final KundeReadService service = new KundeReadService(repo);

    @InjectSoftAssertions
    private SoftAssertions softly;

    @Test
    @DisplayName("Immer erfolgreich")
    @SuppressWarnings("java:S3415")
    void immerErfolgreich() {
        assertThat(true).isTrue();
    }

    @Test
    @DisplayName("Noch nicht fertig")
    @Disabled("Nur zur Veranschaulichung von @Disabled")
    @SuppressWarnings("java:S3415")
    void nochNichtFertig() {
        //noinspection DataFlowIssue
        assertThat(false).isTrue();
    }

    @Test
    @DisplayName("Suche nach allen Kunden")
    void findAll() {
        // when
        final var kunden = service.find(Collections.emptyMap());

        // then
        assertThat(kunden).isNotEmpty();
    }

    @ParameterizedTest(name = "[{index}] Suche mit vorhandenem Nachnamen: nachname={0}")
    @ValueSource(strings = NACHNAME)
    @DisplayName("Suche mit vorhandenem Nachnamen")
    void findByNachname(final String nachname) {
        // given
        final var params = Map.of("nachname", List.of(nachname));

        // when
        final var kunden = service.find(params);

        // then
        softly.assertThat(kunden).isNotEmpty();
        kunden.stream()
            .map(Kunde::getNachname)
            .forEach(nachnameTmp -> softly.assertThat(nachnameTmp).isEqualTo(nachname));
    }

    @Nested
    @DisplayName("Suche anhand der ID")
    class FindById {
        @ParameterizedTest(name = "[{index}] Suche mit vorhandener ID: id={0}")
        @ValueSource(strings = ID_VORHANDEN)
        @DisplayName("Suche mit vorhandener ID")
        void findById(final String id) {
            // given
            final var kundeId = UUID.fromString(id);

            // when
            final var kunde = service.findById(kundeId);

            // then
            assertThat(kunde)
                .isNotNull()
                .extracting(Kunde::getId)
                .isEqualTo(kundeId);
        }

        @ParameterizedTest(name = "[{index}] Suche mit nicht-vorhandener ID: id={0}")
        @ValueSource(strings = ID_NICHT_VORHANDEN)
        @DisplayName("Suche mit nicht-vorhandener ID")
        void findByIdNichtVorhanden(final String id) {
            // given
            final var kundeId = UUID.fromString(id);

            // when
            final var notFoundException = catchThrowableOfType(
                NotFoundException.class,
                () -> service.findById(kundeId)
            );

            // then
            assertThat(notFoundException)
                .isNotNull()
                .extracting(NotFoundException::getId)
                .isEqualTo(kundeId);
        }
    }
}
