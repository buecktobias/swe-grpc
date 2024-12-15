package com.swe.grpc.entity;

public class Adresse {
    private String plz;
    private String ort;

    /// Konstruktor mit allen notwendigen Argumenten.
    /// @param plz Die Postleitzahl.
    /// @param ort Der Ort.
    public Adresse(final String plz, final String ort) {
        this.plz = plz;
        this.ort = ort;
    }

    /// Postleitzahl ermitteln.
    /// @return Die Postleitzahl.
    public String getPlz() {
        return plz;
    }

    /// Die Postleitzahl setzen.
    /// @param plz Die Postleitzahl.
    public void setPlz(final String plz) {
        this.plz = plz;
    }

    /// Den Ort ermitteln.
    /// @return Der Ort.
    public String getOrt() {
        return ort;
    }

    /// Den Ort setzen.
    /// @param ort Der Ort.
    public void setOrt(final String ort) {
        this.ort = ort;
    }

    @Override
    public String toString() {
        return "Adresse{" + "plz='" + plz + '\'' + ", ort='" + ort + '\'' + '}';
    }
}
