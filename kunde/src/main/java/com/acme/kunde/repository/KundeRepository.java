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

import com.acme.kunde.entity.InteresseType;
import com.acme.kunde.entity.Kunde;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import static com.acme.kunde.repository.DB.KUNDEN;
import static java.util.Collections.emptyList;
import static java.util.UUID.randomUUID;

/// Repository für den DB-Zugriff bei Kunden.
///
/// @author [Jürgen Zimmermann](mailto:Juergen.Zimmermann@h-ka.de)
@Repository
@SuppressWarnings("PublicConstructor")
public class KundeRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(KundeRepository.class);

    /// Einen Kunden anhand seiner ID suchen.
    ///
    /// @param id Die Id des gesuchten Kunden
    /// @return Optional mit dem gefundenen Kunden oder leeres Optional
    public Optional<Kunde> findById(final UUID id) {
        LOGGER.debug("findById: id={}", id);
        final var result = KUNDEN.stream()
            .filter(kunde -> Objects.equals(kunde.getId(), id))
            .findFirst();
        LOGGER.debug("findById: {}", result);
        return result;
    }

    /// Kunden anhand von Suchkriterien ermitteln.
    /// Z.B. mit `GET https://localhost:8080/api?nachname=A&plz=7`
    ///
    /// @param suchkriterien Suchkriterien.
    /// @return Gefundene Kunden oder leere Collection.
    @SuppressWarnings({"ReturnCount", "JavadocLinkAsPlainText"})
    public @NonNull Collection<Kunde> find(final Map<String, ? extends List<String>> suchkriterien) {
        LOGGER.debug("find: suchkriterien={}", suchkriterien);

        if (suchkriterien.isEmpty()) {
            return findAll();
        }

        // for-Schleife statt "forEach" wegen return
        for (final var entry : suchkriterien.entrySet()) {
            switch (entry.getKey()) {
                case "email" -> {
                    final var kunde = findByEmail(entry.getValue().getFirst()).orElse(null);
                    return kunde == null ? emptyList() : List.of(kunde);
                }
                case "nachname" -> {
                    return findByNachname(entry.getValue().getFirst());
                }
                case "interesse" -> {
                    return findByInteresse(entry.getValue());
                }
                default -> {
                    LOGGER.debug("find: ungueltiges Suchkriterium={}", entry.getKey());
                    return emptyList();
                }
            }
        }

        return emptyList();
    }

    /// Alle Kunden als Collection ermitteln, wie sie später auch von der DB kommen.
    ///
    /// @return Alle Kunden
    public @NonNull Collection<Kunde> findAll() {
        return KUNDEN;
    }

    /// Kunde zu gegebener Emailadresse aus der DB ermitteln.
    ///
    /// @param email Emailadresse für die Suche
    /// @return Gefundener Kunde oder leeres Optional
    public Optional<Kunde> findByEmail(final String email) {
        LOGGER.debug("findByEmail: {}", email);
        final var result = KUNDEN.stream()
            .filter(kunde -> kunde.getEmail().contentEquals(email))
            .findFirst();
        LOGGER.debug("findByEmail: {}", result);
        return result;
    }

    /// Abfrage, ob es einen Kunden mit gegebener Emailadresse gibt.
    ///
    /// @param email Emailadresse für die Suche
    /// @return true, falls es einen solchen Kunden gibt, sonst false
    public boolean isEmailExisting(final String email) {
        LOGGER.debug("isEmailExisting: email={}", email);
        final var count = KUNDEN.stream()
            .filter(kunde -> kunde.getEmail().contentEquals(email))
            .count();
        LOGGER.debug("isEmailExisting: count={}", count);
        return count > 0L;
    }

    /// Kunden anhand des Nachnamens suchen.
    ///
    /// @param nachname Der (Teil-) Nachname der gesuchten Kunden
    /// @return Die gefundenen Kunden oder eine leere Collection
    public @NonNull Collection<Kunde> findByNachname(final CharSequence nachname) {
        LOGGER.debug("findByNachname: nachname={}", nachname);
        final var kunden = KUNDEN.stream()
            .filter(kunde -> kunde.getNachname().contains(nachname))
            .toList();
        LOGGER.debug("findByNachname: kunden={}", kunden);
        return kunden;
    }

    /// Kunden anhand von Interessen suchen.
    ///
    /// @param interessenStr Die Interessen der gesuchten Kunden
    /// @return Die gefundenen Kunden oder eine leere Collection
    private @NonNull Collection<Kunde> findByInteresse(final Collection<String> interessenStr) {
        LOGGER.debug("findByInteressen: interessenStr={}", interessenStr);
        final var interessen = interessenStr
            .stream()
            .map(InteresseType::of)
            .toList();
        if (interessen.contains(null)) {
            LOGGER.debug("findByInteressen: keine Kunden");
            return emptyList();
        }

        LOGGER.trace("findByInteressen: interessen={}", interessen);
        final var kunden = KUNDEN.stream()
            .filter(kunde -> {
                @SuppressWarnings("SetReplaceableByEnumSet")
                final Collection<InteresseType> kundeInteressen = new HashSet<>(kunde.getInteressen());
                return kundeInteressen.containsAll(interessen);
            })
            .toList();
        LOGGER.debug("findByInteressen: kunden={}", kunden);
        return kunden;
    }

    /// Abfrage, welche Nachnamen es zu einem Präfix gibt.
    ///
    /// @param prefix Nachname-Präfix.
    /// @return Die passenden Nachnamen oder eine leere Collection.
    public @NonNull Collection<String> findNachnamenByPrefix(final @NonNull String prefix) {
        LOGGER.debug("findByNachname: prefix={}", prefix);
        final var nachnamen = KUNDEN.stream()
            .map(Kunde::getNachname)
            .filter(nachname -> nachname.startsWith(prefix))
            .distinct()
            .toList();
        LOGGER.debug("findByNachname: nachnamen={}", nachnamen);
        return nachnamen;
    }

    /// Einen neuen Kunden anlegen.
    ///
    /// @param kunde Das Objekt des neu anzulegenden Kunden.
    /// @return Der neu angelegte Kunde mit generierter ID
    public @NonNull Kunde create(final @NonNull Kunde kunde) {
        LOGGER.debug("create: {}", kunde);
        kunde.setId(randomUUID());
        KUNDEN.add(kunde);
        LOGGER.debug("create: {}", kunde);
        return kunde;
    }

    /// Einen vorhandenen Kunden aktualisieren.
    ///
    /// @param kunde Das Objekt mit den neuen Daten
    public void update(final @NonNull Kunde kunde) {
        LOGGER.debug("update: {}", kunde);
        final OptionalInt index = IntStream
            .range(0, KUNDEN.size())
            .filter(i -> Objects.equals(KUNDEN.get(i).getId(), kunde.getId()))
            .findFirst();
        LOGGER.trace("update: index={}", index);
        if (index.isEmpty()) {
            return;
        }
        KUNDEN.set(index.getAsInt(), kunde);
        LOGGER.debug("update: {}", kunde);
    }

    /// Einen vorhandenen Kunden löschen.
    ///
    /// @param id Die ID des zu löschenden Kunden.
    public void deleteById(final UUID id) {
        LOGGER.debug("deleteById: id={}", id);
        final OptionalInt index = IntStream
            .range(0, KUNDEN.size())
            .filter(i -> Objects.equals(KUNDEN.get(i).getId(), id))
            .findFirst();
        LOGGER.trace("deleteById: index={}", index);
        index.ifPresent(KUNDEN::remove);
        LOGGER.debug("deleteById: #KUNDEN={}", KUNDEN.size());
    }
}
