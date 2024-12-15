package com.swe.grpc.entity;

public enum InteresseType {
    /// _Sport_ mit dem internen Wert `S` für z.B. das Mapping in einem JSON-Datensatz.
    SPORT("S"),

    /// _Lesen_ mit dem internen Wert `L` für z.B. das Mapping in einem JSON-Datensatz.
    LESEN("L"),

    /// _Reisen_ mit dem internen Wert `R` für z.B. das Mapping in einem JSON-Datensatz.
    REISEN("R");

    private final String value;

    InteresseType(final String value) {
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
    public static InteresseType of(final String value) {
        return Stream.of(values())
            .filter(interesse -> interesse.value.equalsIgnoreCase(value))
            .findFirst()
            .orElse(null);
    }
}
