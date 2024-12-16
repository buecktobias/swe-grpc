/*
 * Copyright (C) 2024 - present Juergen Zimmermann, Hochschule Karlsruhe
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

import com.acme.kunde.entity.Adresse;
import com.acme.kunde.entity.FamilienstandType;
import com.acme.kunde.entity.GeschlechtType;
import com.acme.kunde.entity.Kunde;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/// Builder-Klasse für die Klasse [Kunde].
///
/// @author ![Jürgen Zimmermann](mailto:Juergen.Zimmermann@h-ka.de)
public class KundeBuilder {
    private UUID id;
    private String nachname;
    private String email;
    private int kategorie;
    private boolean hasNewsletter;
    private LocalDate geburtsdatum;
    private URL homepage;
    private GeschlechtType geschlecht;
    private FamilienstandType familienstand;
    private Adresse adresse;
    private List<com.acme.kunde.entity.Umsatz> umsaetze;
    private List<com.acme.kunde.entity.InteresseType> interessen;

    /// Ein Builder-Objekt für die Klasse [Kunde] bauen.
    ///
    /// @return Das Builder-Objekt.
    public static KundeBuilder getBuilder() {
        return new KundeBuilder();
    }

    /// id setzen.
    ///
    /// @param id Die id.
    /// @return Das Builder-Objekt.
    public KundeBuilder setId(final UUID id) {
        this.id = id;
        return this;
    }

    /// Nachname setzen.
    ///
    /// @param nachname Der Nachname.
    /// @return Das Builder-Objekt.
    public KundeBuilder setNachname(final String nachname) {
        this.nachname = nachname;
        return this;
    }

    /// Email setzen.
    ///
    /// @param email Die Email.
    /// @return Das Builder-Objekt.
    public KundeBuilder setEmail(final String email) {
        this.email = email;
        return this;
    }

    /// Kategorie setzen.
    ///
    /// @param kategorie Die Kategorie.
    /// @return Das Builder-Objekt.
    public KundeBuilder setKategorie(final int kategorie) {
        this.kategorie = kategorie;
        return this;
    }

    /// Flag für das Newsletter-Abonnement setzen.
    ///
    /// @param hasNewsletter Flag für das Newaletter-Abonnement.
    /// @return Das Builder-Objekt.
    public KundeBuilder setHasNewsletter(final boolean hasNewsletter) {
        this.hasNewsletter = hasNewsletter;
        return this;
    }

    /// Geburtsdatum setzen.
    ///
    /// @param geburtsdatum Das Geburtsdatum.
    /// @return Das Builder-Objekt.
    public KundeBuilder setGeburtsdatum(final LocalDate geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
        return this;
    }

    /// Homepage setzen.
    ///
    /// @param homepage Die Homepage.
    /// @return Das Builder-Objekt.
    public KundeBuilder setHomepage(final URL homepage) {
        this.homepage = homepage;
        return this;
    }

    /// Geschlecht setzen.
    ///
    /// @param geschlecht Das Geschlecht.
    /// @return Das Builder-Objekt.
    public KundeBuilder setGeschlecht(final GeschlechtType geschlecht) {
        this.geschlecht = geschlecht;
        return this;
    }

    /// Familienstand setzen.
    ///
    /// @param familienstand Der Familienstand.
    /// @return Das Builder-Objekt.
    public KundeBuilder setFamilienstand(final FamilienstandType familienstand) {
        this.familienstand = familienstand;
        return this;
    }

    /// Adresse setzen.
    ///
    /// @param adresse Die Adresse.
    /// @return Das Builder-Objekt.
    public KundeBuilder setAdresse(final Adresse adresse) {
        this.adresse = adresse;
        return this;
    }

    /// Umsätze setzen.
    ///
    /// @param umsaetze Die Umsätze.
    /// @return Das Builder-Objekt.
    public KundeBuilder setUmsaetze(final List<com.acme.kunde.entity.Umsatz> umsaetze) {
        this.umsaetze = umsaetze;
        return this;
    }

    /// Interessen setzen.
    ///
    /// @param interessen Die Interessen.
    /// @return Das Builder-Objekt.
    public KundeBuilder setInteressen(final List<com.acme.kunde.entity.InteresseType> interessen) {
        this.interessen = interessen;
        return this;
    }

    /// Das [Kunde]-Objekt bauen.
    ///
    /// @return Das gebaute Kunde-Objekt.
    public Kunde build() {
        return new Kunde(id, nachname, email, kategorie, hasNewsletter, geburtsdatum, homepage, geschlecht,
            familienstand, adresse, umsaetze, interessen);
    }
}
