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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.util.UriComponentsBuilder;
import static com.acme.kunde.controller.KundeGetController.API_PATH;
import static com.acme.kunde.controller.KundeGetController.ID_PATTERN;
import static com.acme.kunde.controller.KundeGetControllerTest.HOST;
import static com.acme.kunde.controller.KundeGetControllerTest.REQUEST_FACTORY;
import static com.acme.kunde.controller.KundeGetControllerTest.SCHEMA;
import static com.acme.kunde.dev.DevConfig.DEV;
import static com.acme.kunde.entity.GeschlechtType.WEIBLICH;
import static com.acme.kunde.entity.InteresseType.LESEN;
import static com.acme.kunde.entity.InteresseType.REISEN;
import static java.math.BigDecimal.ONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowableOfType;
import static org.junit.jupiter.api.condition.JRE.JAVA_23;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Tag("integration")
@Tag("rest")
@Tag("rest-write")
@DisplayName("REST-Schnittstelle fuer Schreiben testen")
@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles(DEV)
@EnabledForJreRange(min = JAVA_23, max = JAVA_23)
@SuppressWarnings("WriteTag")
class KundeWriteControllerTest {
    private static final String ID_UPDATE_PUT = "00000000-0000-0000-0000-000000000030";
    private static final String ID_DELETE = "00000000-0000-0000-0000-000000000050";

    private static final String NEUER_NACHNAME = "Neuernachname-Rest";
    private static final String NEUE_EMAIL = "email.rest@test.de";
    private static final String NEUES_GEBURTSDATUM = "2024-01-31";
    private static final String CURRENCY_CODE = "EUR";
    private static final String NEUE_HOMEPAGE = "https://test.de";

    private static final String NEUE_PLZ = "12345";
    private static final String NEUER_ORT = "Neuerortrest";

    private static final String NEUER_NACHNAME_INVALID = "?!$";
    private static final String NEUE_EMAIL_INVALID = "email@";
    private static final int NEUE_KATEGORIE_INVALID = 11;
    private static final String NEUES_GEBURTSDATUM_INVALID = "3000-01-31";
    private static final String NEUE_PLZ_INVALID = "1234";

    private final KundeRepository kundeRepo;

    @InjectSoftAssertions
    private SoftAssertions softly;

    @SuppressFBWarnings("CT")
    KundeWriteControllerTest(@LocalServerPort final int port, final ApplicationContext ctx) {
        final var writeController = ctx.getBean(KundeWriteController.class);
        assertThat(writeController).isNotNull();

        final var uriComponents = UriComponentsBuilder.newInstance()
            .scheme(SCHEMA)
            .host(HOST)
            .port(port)
            .path(API_PATH)
            .build();
        final var baseUrl = uriComponents.toUriString();
        final var restClient = RestClient
            .builder()
            .baseUrl(baseUrl)
            .requestFactory(REQUEST_FACTORY)
            .build();
        final var clientAdapter = RestClientAdapter.create(restClient);
        final var proxyFactory = HttpServiceProxyFactory.builderFor(clientAdapter).build();
        kundeRepo = proxyFactory.createClient(KundeRepository.class);
    }

    @SuppressWarnings("DataFlowIssue")
    @Nested
    @DisplayName("Erzeugen")
    class Erzeugen {
        @ParameterizedTest(name = "[{index}] Neuanlegen eines neuen Kunden: nachname={0}, email={1}")
        @CsvSource(
            NEUER_NACHNAME + "," + NEUE_EMAIL + "," + NEUES_GEBURTSDATUM + "," + NEUE_HOMEPAGE +
                "," + NEUE_PLZ + "," + NEUER_ORT + "," + CURRENCY_CODE
        )
        @DisplayName("Neuanlegen eines neuen Kunden")
        void post(final ArgumentsAccessor args) {
            // given
            final var adresse = new AdresseDTO(args.getString(4), args.getString(5));
            final var umsatz = new UmsatzDTO(ONE, Currency.getInstance(args.getString(6)));
            final var umsaetze = List.of(umsatz);
            final var kundeDTO = new KundeDTO(
                args.getString(0),
                args.getString(1),
                1,
                true,
                args.get(2, LocalDate.class),
                args.get(3, URL.class),
                WEIBLICH,
                null,
                adresse,
                umsaetze,
                List.of(LESEN, REISEN)
            );

            // when
            final var response = kundeRepo.post(kundeDTO);

            // then
            assertThat(response).isNotNull();
            softly.assertThat(response.getStatusCode()).isEqualTo(CREATED);
            final var location = response.getHeaders().getLocation();
            softly.assertThat(location)
                .isNotNull()
                .isInstanceOf(URI.class);
            softly.assertThat(location.toString()).matches(".*/" + ID_PATTERN + '$');
        }

        @ParameterizedTest(name = "[{index}] Neuanlegen mit ungueltigen Werten: nachname={0}, email={1}")
        @CsvSource(
            NEUER_NACHNAME_INVALID + "," + NEUE_EMAIL_INVALID + "," + NEUE_KATEGORIE_INVALID + "," +
                NEUES_GEBURTSDATUM_INVALID + "," + NEUE_PLZ_INVALID + "," + NEUER_ORT
        )
        @DisplayName("Neuanlegen mit ungueltigen Werten")
        @SuppressWarnings("DynamicRegexReplaceableByCompiledPattern")
        void postInvalid(final ArgumentsAccessor args) {
            // given
            final var adresse = new AdresseDTO(args.getString(4), args.getString(5));
            final var kundeDTO = new KundeDTO(
                args.getString(0),
                args.getString(1),
                args.getInteger(2),
                true,
                args.get(3, LocalDate.class),
                null,
                WEIBLICH,
                null,
                adresse,
                null,
                List.of(LESEN, REISEN, REISEN)
            );
            final var violationKeys = List.of(
                "nachname",
                "email",
                "kategorie",
                "geburtsdatum",
                "adresse.plz",
                "interessen"
            );

            // when
            final var exc = catchThrowableOfType(
                HttpClientErrorException.UnprocessableEntity.class,
                () -> kundeRepo.post(kundeDTO)
            );

            // then
            assertThat(exc.getStatusCode()).isEqualTo(UNPROCESSABLE_ENTITY);
            final var body = exc.getResponseBodyAs(ProblemDetail.class);
            assertThat(body).isNotNull();
            final var detail = body.getDetail();
            assertThat(detail).isNotNull();
            final var violations = detail.split(", ");
            final var actualViolationKeys = Arrays.stream(violations)
                .map(violation -> violation.substring(0, violation.indexOf(": ")))
                .toList();
            assertThat(actualViolationKeys).containsExactlyInAnyOrderElementsOf(violationKeys);
        }
    }

    @Nested
    @DisplayName("Aendern")
    class Aendern {
        @ParameterizedTest(name = "[{index}] Aendern eines vorhandenen Kunden durch PUT: id={0}")
        @ValueSource(strings = ID_UPDATE_PUT)
        @DisplayName("Aendern eines vorhandenen Kunden durch PUT")
        void put(final String id) {
            // given
            final var kundeOrig = kundeRepo.getById(id).getBody();
            assertThat(kundeOrig).isNotNull();
            final var umsaetzeOrig = kundeOrig.umsaetze();
            final List<UmsatzDTO> umsaetze;
            if (umsaetzeOrig == null) {
                umsaetze = null;
            } else {
                umsaetze = umsaetzeOrig.stream()
                    .map(umsatzOrig -> new UmsatzDTO(ONE, umsatzOrig.waehrung()))
                    .toList();
            }
            final var adresseOrig = kundeOrig.adresse();
            final var adresse = new AdresseDTO(adresseOrig.plz(), adresseOrig.ort());

            final var kunde = new KundeDTO(
                kundeOrig.nachname(),
                kundeOrig.email() + "put",
                kundeOrig.kategorie(),
                kundeOrig.hasNewsletter(),
                kundeOrig.geburtsdatum(),
                kundeOrig.homepage(),
                kundeOrig.geschlecht(),
                kundeOrig.familienstand(),
                adresse,
                umsaetze,
                kundeOrig.interessen()
            );

            // when
            final var response = kundeRepo.put(id, kunde);

            // then
            assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
        }
    }

    @Nested
    @DisplayName("Loeschen")
    class Loeschen {
        @ParameterizedTest(name = "[{index}] Loeschen eines vorhandenen Kunden: id={0}")
        @ValueSource(strings = ID_DELETE)
        @DisplayName("Loeschen eines vorhandenen Kunden")
        void deleteById(final String id) {
            // when
            final var response = kundeRepo.deleteById(id);

            // then
            assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
        }
    }
}
