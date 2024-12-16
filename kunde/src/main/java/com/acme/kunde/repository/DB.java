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
package com.acme.kunde.repository;

import com.acme.kunde.entity.Kunde;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static com.acme.kunde.entity.FamilienstandType.GESCHIEDEN;
import static com.acme.kunde.entity.FamilienstandType.LEDIG;
import static com.acme.kunde.entity.FamilienstandType.VERHEIRATET;
import static com.acme.kunde.entity.FamilienstandType.VERWITWET;
import static com.acme.kunde.entity.GeschlechtType.DIVERS;
import static com.acme.kunde.entity.GeschlechtType.MAENNLICH;
import static com.acme.kunde.entity.GeschlechtType.WEIBLICH;
import static com.acme.kunde.entity.InteresseType.LESEN;
import static com.acme.kunde.entity.InteresseType.REISEN;
import static com.acme.kunde.entity.InteresseType.SPORT;
import static java.math.BigDecimal.ZERO;
import static java.util.Collections.emptyList;
import static java.util.Locale.GERMANY;

/// Emulation der Datenbasis für persistente Kunden.
///
/// @author [Jürgen Zimmermann](mailto:Juergen.Zimmermann@h-ka.de)
@SuppressWarnings({"UtilityClassCanBeEnum", "UtilityClass", "MagicNumber", "RedundantSuppression", "java:S1192"})
final class DB {
    /// Liste der Kunden zur Emulation der DB.
    @SuppressWarnings("StaticCollection")
    static final List<Kunde> KUNDEN;

    static {
        final var currencyGermany = Currency.getInstance(GERMANY);
        // Helper-Methoden ab Java 9: List.of(), Set.of, Map.of, Stream.of
        // List.of() baut eine unveraenderliche Liste: kein Einfuegen, Aendern, Loeschen
        KUNDEN = Stream.of(
            // admin
            KundeBuilder.getBuilder()
                .setId(UUID.fromString("00000000-0000-0000-0000-000000000000"))
                .setNachname("Admin")
                .setEmail("admin@acme.com")
                .setKategorie(0)
                .setHasNewsletter(true)
                .setGeburtsdatum(LocalDate.parse("2024-01-31"))
                .setHomepage(buildURL("https://www.acme.com"))
                .setUmsaetze(Stream.of(
                    UmsatzBuilder.getBuilder().setBetrag(ZERO).setWaehrung(currencyGermany).build()
                ).collect(Collectors.toList())) // NOSONAR
                .setGeschlecht(WEIBLICH)
                .setFamilienstand(VERHEIRATET)
                .setInteressen(List.of(LESEN))
                .setAdresse(AdresseBuilder.getBuilder().setPlz("00000").setOrt("Aachen").build())
                .build(),
            // HTTP GET
            KundeBuilder.getBuilder()
                .setId(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .setNachname("Alpha")
                .setEmail("alpha@acme.de")
                .setKategorie(1)
                .setHasNewsletter(true)
                .setGeburtsdatum(LocalDate.parse("2024-01-01"))
                .setHomepage(buildURL("https://www.acme.de"))
                .setUmsaetze(Stream.of(
                    UmsatzBuilder.getBuilder().setBetrag(new BigDecimal("10")).setWaehrung(currencyGermany).build(),
                    UmsatzBuilder.getBuilder().setBetrag(new BigDecimal("11")).setWaehrung(currencyGermany).build(),
                    UmsatzBuilder.getBuilder().setBetrag(new BigDecimal("12")).setWaehrung(currencyGermany).build()
                ).collect(Collectors.toList())) // NOSONAR
                .setGeschlecht(MAENNLICH)
                .setFamilienstand(LEDIG)
                .setInteressen(List.of(SPORT, LESEN))
                .setAdresse(AdresseBuilder.getBuilder().setPlz("11111").setOrt("Augsburg").build())
                .build(),
            KundeBuilder.getBuilder()
                .setId(UUID.fromString("00000000-0000-0000-0000-000000000020"))
                .setNachname("Alpha")
                .setEmail("alpha@acme.edu")
                .setKategorie(2)
                .setHasNewsletter(true)
                .setGeburtsdatum(LocalDate.parse("2024-01-02"))
                .setHomepage(buildURL("https://www.acme.edu"))
                .setUmsaetze(Stream.of(
                    UmsatzBuilder.getBuilder().setBetrag(new BigDecimal("20")).setWaehrung(currencyGermany).build()
                ).collect(Collectors.toList())) // NOSONAR
                .setGeschlecht(WEIBLICH)
                .setFamilienstand(GESCHIEDEN)
                .setInteressen(emptyList())
                .setAdresse(AdresseBuilder.getBuilder().setPlz("22222").setOrt("Aalen").build())
                .build(),
            // HTTP PUT
            KundeBuilder.getBuilder()
                .setId(UUID.fromString("00000000-0000-0000-0000-000000000030"))
                .setNachname("Alpha")
                .setEmail("alpha@acme.ch")
                .setKategorie(3)
                .setHasNewsletter(true)
                .setGeburtsdatum(LocalDate.parse("2024-01-03"))
                .setHomepage(buildURL("https://www.acme.ch"))
                .setUmsaetze(Stream.of(
                    UmsatzBuilder.getBuilder().setBetrag(new BigDecimal("30")).setWaehrung(currencyGermany).build()
                ).collect(Collectors.toList())) // NOSONAR
                .setGeschlecht(MAENNLICH)
                .setFamilienstand(VERWITWET)
                .setInteressen(List.of(SPORT, REISEN))
                .setAdresse(AdresseBuilder.getBuilder().setPlz("33333").setOrt("Ahlen").build())
                .build(),
            // HTTP PATCH
            KundeBuilder.getBuilder()
                .setId(UUID.fromString("00000000-0000-0000-0000-000000000040"))
                .setNachname("Delta")
                .setEmail("delta@acme.uk")
                .setKategorie(4)
                .setHasNewsletter(true)
                .setGeburtsdatum(LocalDate.parse("2024-01-04"))
                .setHomepage(buildURL("https://www.acme.uk"))
                .setUmsaetze(Stream.of(
                    UmsatzBuilder.getBuilder().setBetrag(new BigDecimal("40")).setWaehrung(currencyGermany).build()
                ).collect(Collectors.toList())) // NOSONAR
                .setGeschlecht(WEIBLICH)
                .setFamilienstand(VERHEIRATET)
                .setInteressen(List.of(LESEN, REISEN))
                .setAdresse(AdresseBuilder.getBuilder().setPlz("44444").setOrt("Dortmund").build())
                .build(),
            // HTTP DELETE
            KundeBuilder.getBuilder()
                .setId(UUID.fromString("00000000-0000-0000-0000-000000000050"))
                .setNachname("Epsilon")
                .setEmail("epsilon@acme.jp")
                .setKategorie(5)
                .setHasNewsletter(true)
                .setGeburtsdatum(LocalDate.parse("2024-01-05"))
                .setHomepage(buildURL("https://www.acme.jp"))
                .setUmsaetze(Stream.of(
                    UmsatzBuilder.getBuilder().setBetrag(new BigDecimal("50")).setWaehrung(currencyGermany).build()
                ).collect(Collectors.toList())) // NOSONAR
                .setGeschlecht(MAENNLICH)
                .setFamilienstand(LEDIG)
                .setInteressen(emptyList())
                .setAdresse(AdresseBuilder.getBuilder().setPlz("55555").setOrt("Essen").build())
                .build(),
            // zur freien Verfuegung
            KundeBuilder.getBuilder()
                .setId(UUID.fromString("00000000-0000-0000-0000-000000000060"))
                .setNachname("Phi")
                .setEmail("phi@acme.cn")
                .setKategorie(6)
                .setHasNewsletter(true)
                .setGeburtsdatum(LocalDate.parse("2024-01-06"))
                .setHomepage(buildURL("https://www.acme.cn"))
                .setUmsaetze(Stream.of(
                    UmsatzBuilder.getBuilder().setBetrag(new BigDecimal("60")).setWaehrung(currencyGermany).build()
                ).collect(Collectors.toList())) // NOSONAR
                .setGeschlecht(DIVERS)
                .setFamilienstand(LEDIG)
                .setInteressen(emptyList())
                .setAdresse(AdresseBuilder.getBuilder().setPlz("66666").setOrt("Freiburg").build())
                .build()
        )
        // CAVEAT Stream.toList() erstellt eine "immutable" List
        .collect(Collectors.toList()); // NOSONAR
    }

    private DB() {
    }

    private static URL buildURL(final String url) {
        try {
            return URI.create(url).toURL();
        } catch (final MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
