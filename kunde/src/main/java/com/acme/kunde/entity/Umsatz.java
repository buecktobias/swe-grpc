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

import java.math.BigDecimal;
import java.util.Currency;

/// Geldbetrag und Währungseinheit für eine Umsatzangabe.
///
/// @author [Jürgen Zimmermann](mailto:Juergen.Zimmermann@h-ka.de)
public class Umsatz {
    private BigDecimal betrag;
    private Currency waehrung;

    /// Konstruktor mit allen notwendigen Argumenten.
    ///
    /// @param betrag Der Betrag.
    /// @param waehrung Die Währung.
    public Umsatz(final BigDecimal betrag, final Currency waehrung) {
        this.betrag = betrag;
        this.waehrung = waehrung;
    }

    /// Betrag ermitteln.
    ///
    /// @return Der Betrag.
    public BigDecimal getBetrag() {
        return betrag;
    }

    /// Betrag setzen.
    ///
    /// @param betrag Der Betrag.
    public void setBetrag(final BigDecimal betrag) {
        this.betrag = betrag;
    }

    /// Währung ermitteln.
    ///
    /// @return Die Währung.
    public Currency getWaehrung() {
        return waehrung;
    }

    /// Währung setzen.
    ///
    /// @param waehrung Die Währung
    public void setWaehrung(final Currency waehrung) {
        this.waehrung = waehrung;
    }

    @Override
    public String toString() {
        return "Umsatz{" + "betrag=" + betrag + ", waehrung=" + waehrung +  '}';
    }
}
