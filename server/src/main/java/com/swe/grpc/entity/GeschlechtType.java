package com.swe.grpc.entity;
public enum GeschlechtType {
    /// _Männlich_ mit dem internen Wert `M` für z.B. das Mapping in einem JSON-Datensatz.
    MAENNLICH("M"),

    /// _Weiblich_ mit dem internen Wert `W` für z.B. das Mapping in einem JSON-Datensatz.
    WEIBLICH("W"),

    /// _Divers_ mit dem internen Wert `D` für z.B. das Mapping in einem JSON-Datensatz.
    DIVERS("D");

    private final String value;

    GeschlechtType(final String value) {
        this.value = value;
    }

    /// Einen enum-Wert als String mit dem internen Wert ausgeben.
    /// Dieser Wert wird durch Jackson in einem JSON-Datensatz verwendet.
    /// [Wiki-Seiten](https://github.com/FasterXML/jackson-databind/wiki)
    ///
    /// @return Interner Wert
    @JsonValue
    public String getValue() {
        return value;
    }

    /// Konvertierung eines Strings in einen Enum-Wert.
    ///
    /// @param value Der String, zu dem ein passender Enum-Wert ermittelt werden soll.
    /// @return Passender Enum-Wert oder null.
    @JsonCreator
    public static GeschlechtType of(final String value) {
        return Stream.of(values())
            .filter(geschlecht -> geschlecht.value.equalsIgnoreCase(value))
            .findFirst()
            .orElse(null);
    }
}