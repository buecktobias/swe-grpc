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
package com.acme.kunde.entity;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/// Daten eines Kunden. In DDD ist Kunde ist ein Aggregate Root.
/// ![Klassendiagramm](../../../../../asciidoc/Kunde.svg)
///
/// @author ![Jürgen Zimmermann](mailto:Juergen.Zimmermann@h-ka.de)
// Maven: [Klassendiagramm](../../../../../../generated-docs/Kunde.svg)
// https://thorben-janssen.com/java-records-hibernate-jpa
public class Kunde {
    /// Muster für einen gültigen Nachnamen.
    public static final String NACHNAME_PATTERN =
        "(o'|von|von der|von und zu|van)?[A-ZÄÖÜ][a-zäöüß]+(-[A-ZÄÖÜ][a-zäöüß]+)?";

    /// Kleinster Wert für eine Kategorie.
    public static final long MIN_KATEGORIE = 0L;

    /// Maximaler Wert für eine Kategorie.
    public static final long MAX_KATEGORIE = 9L;

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
    private List<Umsatz> umsaetze;
    private List<InteresseType> interessen;

    /// Konstruktor mit den Argumenten, u.a. für eine Builder-Klasse.
    ///
    /// @param id Die id.
    /// @param nachname Der Nachname.
    /// @param email Die Email.
    /// @param kategorie Die Kategorie.
    /// @param hasNewsletter Flag für das Newsletter-Abonnement.
    /// @param geburtsdatum Das Geburtsdatum.
    /// @param homepage Die Homepage.
    /// @param geschlecht Das Geschlecht.
    /// @param familienstand Der Familienstand.
    /// @param adresse Die Adresse.
    /// @param umsaetze Die Umsätze.
    /// @param interessen Die Interessen.
    @SuppressWarnings("ParameterNumber")
    public Kunde(final UUID id, final String nachname, final String email, final int kategorie, // NOSONAR
                 final boolean hasNewsletter, final LocalDate geburtsdatum, final URL homepage,
                 final GeschlechtType geschlecht, final FamilienstandType familienstand, final Adresse adresse,
                 final List<Umsatz> umsaetze, final List<InteresseType> interessen) {
        this.id = id;
        this.nachname = nachname;
        this.email = email;
        this.kategorie = kategorie;
        this.hasNewsletter = hasNewsletter;
        this.geburtsdatum = geburtsdatum;
        this.homepage = homepage;
        this.geschlecht = geschlecht;
        this.familienstand = familienstand;
        this.adresse = adresse;
        this.umsaetze = umsaetze;
        this.interessen = interessen;
    }

    @Override
    public final boolean equals(final Object other) {
        return other instanceof Kunde kunde && Objects.equals(id, kunde.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /// id ermitteln.
    ///
    /// @return Die id.
    public UUID getId() {
        return id;
    }

    /// id setzen.
    ///
    /// @param id Die id.
    public void setId(final UUID id) {
        this.id = id;
    }

    /// Nachname ermitteln.
    ///
    /// @return Der Nachname.
    public String getNachname() {
        return nachname;
    }

    /// Nachname setzen.
    ///
    /// @param nachname Der Nachname.
    public void setNachname(final String nachname) {
        this.nachname = nachname;
    }

    /// Email ermitteln.
    ///
    /// @return Die Email.
    public String getEmail() {
        return email;
    }

    /// Email setzen.
    ///
    /// @param email Die Email.
    public void setEmail(final String email) {
        this.email = email;
    }

    /// Kategorie ermitteln.
    ///
    /// @return Die Kategorie.
    public int getKategorie() {
        return kategorie;
    }

    /// Kategorie setzen.
    ///
    /// @param kategorie Die Kategorie.
    public void setKategorie(final int kategorie) {
        this.kategorie = kategorie;
    }

    /// Abfrage, ob der Newsletter abonniert ist.
    ///
    /// @return true, falls der Newsletter abonniert ist.
    public boolean isHasNewsletter() {
        return hasNewsletter;
    }

    /// Flag, ob der Newsletter abonniert ist, setzen.
    ///
    /// @param hasNewsletter Das Flag für das Newsletter-Abo.
    public void setHasNewsletter(final boolean hasNewsletter) {
        this.hasNewsletter = hasNewsletter;
    }

    /// Geburtsdatum ermitteln.
    ///
    /// @return Das Geburtsdatum.
    public LocalDate getGeburtsdatum() {
        return geburtsdatum;
    }

    /// Geburtsdatum setzen.
    ///
    /// @param geburtsdatum Das Geburtsdatum.
    public void setGeburtsdatum(final LocalDate geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    /// Homepage ermitteln.
    ///
    /// @return Die Homepage.
    public URL getHomepage() {
        return homepage;
    }

    /// Homepage setzen.
    ///
    /// @param homepage Die Homepage.
    public void setHomepage(final URL homepage) {
        this.homepage = homepage;
    }

    /// Geschlecht ermitteln.
    ///
    /// @return Das Geschlecht.
    public GeschlechtType getGeschlecht() {
        return geschlecht;
    }

    /// Geschlecht setzen.
    ///
    /// @param geschlecht Das Geschlecht.
    public void setGeschlecht(final GeschlechtType geschlecht) {
        this.geschlecht = geschlecht;
    }

    /// Familienstand ermitteln.
    ///
    /// @return Der Familienstand.
    public FamilienstandType getFamilienstand() {
        return familienstand;
    }

    /// Familienstand setzen.
    ///
    /// @param familienstand Der Familienstand.
    public void setFamilienstand(final FamilienstandType familienstand) {
        this.familienstand = familienstand;
    }

    /// Adresse ermitteln.
    ///
    /// @return Die Adresse.
    public Adresse getAdresse() {
        return adresse;
    }

    /// Adresse setzen.
    ///
    /// @param adresse Die Adresse.
    public void setAdresse(final Adresse adresse) {
        this.adresse = adresse;
    }

    /// Umsätze ermitteln.
    ///
    /// @return Die Umsätze.
    public List<Umsatz> getUmsaetze() {
        return umsaetze;
    }

    /// Umsätze setzen.
    ///
    /// @param umsaetze Die Umsätze.
    public void setUmsaetze(final List<Umsatz> umsaetze) {
        this.umsaetze = umsaetze;
    }

    /// Interessen ermitteln.
    ///
    /// @return Die Interessen.
    public List<InteresseType> getInteressen() {
        return interessen;
    }

    /// Interessen setzen.
    ///
    /// @param interessen Die Interessen.
    public void setInteressen(final List<InteresseType> interessen) {
        this.interessen = interessen;
    }

    @Override
    public String toString() {
        return "Kunde{" +
            "id=" + id +
            ", nachname='" + nachname + '\'' +
            ", email='" + email + '\'' +
            ", kategorie=" + kategorie +
            ", hasNewsletter=" + hasNewsletter +
            ", geburtsdatum=" + geburtsdatum +
            ", homepage=" + homepage +
            ", geschlecht=" + geschlecht +
            ", familienstand=" + familienstand +
            '}';
    }
}
