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
package com.acme.kunde.controller;

import com.acme.kunde.entity.Adresse;
import com.acme.kunde.entity.FamilienstandType;
import com.acme.kunde.entity.GeschlechtType;
import com.acme.kunde.entity.InteresseType;
import com.acme.kunde.entity.Kunde;
import com.acme.kunde.entity.Umsatz;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

/// Model-Klasse für _Spring HATEOAS_
/// ![Klassendiagramm](../../../../../asciidoc/KundeModel.svg)
///
/// @author [Jürgen Zimmermann](mailto:Juergen.Zimmermann@h-ka.de)
// Maven: ![Klassendiagramm](../../../../../../generated-docs/KundeModel.svg)
@JsonPropertyOrder({
    "nachname", "email", "kategorie", "hasNewsletter", "geburtsdatum", "homepage", "geschlecht", "familienstand",
    "adresse", "umsaetze", "interessen"
})
@Relation(collectionRelation = "kunden", itemRelation = "kunde")
class KundeModel extends RepresentationModel<KundeModel> {
    private final String nachname;
    private final String email;
    private final int kategorie;
    private final boolean hasNewsletter;
    private final LocalDate geburtsdatum;
    private final URL homepage;
    private final GeschlechtType geschlecht;
    private final FamilienstandType familienstand;
    private final Adresse adresse;
    private final List<Umsatz> umsaetze;
    private final List<InteresseType> interessen;

    /// Konstruktor für ein [RepresentationModel] gemäß _Spring HATEOAS_.
    ///
    /// @param kunde Entity-Objekt für einen Kunden.
    KundeModel(final Kunde kunde) {
        nachname = kunde.getNachname();
        email = kunde.getEmail();
        kategorie = kunde.getKategorie();
        hasNewsletter = kunde.isHasNewsletter();
        geburtsdatum = kunde.getGeburtsdatum();
        homepage = kunde.getHomepage();
        geschlecht = kunde.getGeschlecht();
        familienstand = kunde.getFamilienstand();
        adresse = kunde.getAdresse();
        umsaetze = kunde.getUmsaetze();
        interessen = kunde.getInteressen();
    }

    @Override
    public final boolean equals(final Object other) {
        if (!(other instanceof KundeModel kundeModel)) {
            return false;
        }
        return Objects.equals(email, kundeModel.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }

    /// Nachname ermitteln.
    ///
    /// @return Der Nachname.
    public String getNachname() {
        return nachname;
    }

    /// Email ermitteln.
    ///
    /// @return Die Email.
    public String getEmail() {
        return email;
    }

    /// Kategorie ermitteln.
    ///
    /// @return Die Kategorie.
    public int getKategorie() {
        return kategorie;
    }

    /// Flag für das Newsletter-Abo ermitteln.
    ///
    /// @return Das Flag für das Newsletter-Abo.
    public boolean isHasNewsletter() {
        return hasNewsletter;
    }

    /// Geburtsdatum ermitteln.
    ///
    /// @return Das Geburtsdatum.
    public LocalDate getGeburtsdatum() {
        return geburtsdatum;
    }

    /// URI für die Homepage ermitteln.
    ///
    /// @return Die URI der Homepage.
    public URL getHomepage() {
        return homepage;
    }

    /// Geschlecht ermitteln.
    ///
    /// @return Das Geschlecht.
    public GeschlechtType getGeschlecht() {
        return geschlecht;
    }

    /// Familienstand ermitteln.
    ///
    /// @return Der Familienstand.
    public FamilienstandType getFamilienstand() {
        return familienstand;
    }

    /// Adresse ermitteln.
    ///
    /// @return Die Adresse.
    public Adresse getAdresse() {
        return adresse;
    }

    /// Umsätze ermitteln.
    ///
    /// @return Liste der `Umsatz`-Objekte.
    public List<Umsatz> getUmsaetze() {
        return umsaetze;
    }

    /// Interessen ermitteln
    ///
    /// @return Liste der Enum-Werte für die einzelnen Interessen.
    public List<InteresseType> getInteressen() {
        return interessen;
    }

    @Override
    public String toString() {
        return "KundeModel{" +
            "nachname='" + nachname + '\'' +
            ", email='" + email + '\'' +
            ", kategorie=" + kategorie +
            ", hasNewsletter=" + hasNewsletter +
            ", geburtsdatum=" + geburtsdatum +
            ", homepage=" + homepage +
            ", geschlecht=" + geschlecht +
            ", familienstand=" + familienstand +
            ", adresse=" + adresse +
            ", umsaetze=" + umsaetze +
            ", interessen=" + interessen +
            '}';
    }
}
