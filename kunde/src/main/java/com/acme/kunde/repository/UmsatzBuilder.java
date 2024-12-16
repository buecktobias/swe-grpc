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

import com.acme.kunde.entity.Umsatz;
import java.math.BigDecimal;
import java.util.Currency;

/// Builder-Klasse für die Klasse [Umsatz].
///
/// @author ![Jürgen Zimmermann](mailto:Juergen.Zimmermann@h-ka.de)
public class UmsatzBuilder {
    private BigDecimal betrag;
    private Currency waehrung;

    /// Ein Builder-Objekt für die Klasse [Umsatz] bauen.
    ///
    /// @return Das Builder-Objekt.
    public static UmsatzBuilder getBuilder() {
        return new UmsatzBuilder();
    }

    /// Betrag setzen.
    ///
    /// @param betrag Der Betrag.
    /// @return Das Builder-Objekt.
    public UmsatzBuilder setBetrag(final BigDecimal betrag) {
        this.betrag = betrag;
        return this;
    }

    /// Währung setzen.
    ///
    /// @param waehrung Die Währung.
    /// @return Das Builder-Objekt.
    public UmsatzBuilder setWaehrung(final Currency waehrung) {
        this.waehrung = waehrung;
        return this;
    }

    /// [Umsatz]-Objekt bauen.
    ///
    /// @return Das gebaute Umsatz-Objekt.
    public Umsatz build() {
        return new Umsatz(betrag, waehrung);
    }

}
