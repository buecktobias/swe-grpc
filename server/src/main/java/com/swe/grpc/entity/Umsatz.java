package com.swe.grpc.entity;
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
