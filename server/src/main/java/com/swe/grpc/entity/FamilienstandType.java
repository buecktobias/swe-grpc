package com.swe.grpc.entity;
public enum FamilienstandType {
    /// _Ledig_ mit dem internen Wert `L` für z.B. das Mapping in einem JSON-Datensatz.
    LEDIG("L"),

    /// _Verheiratet_ mit dem internen Wert `VH` für z.B. das Mapping in einem JSON-Datensatz.
    VERHEIRATET("VH"),

    /// _Geschieden_ mit dem internen Wert `G` für z.B. das Mapping in einem JSON-Datensatz.
    GESCHIEDEN("G"),

    /// _Verwitwet_ mit dem internen Wert `VW` für z.B. das Mapping in einem JSON-Datensatz.
    VERWITWET("VW");

    private final String value;

    FamilienstandType(final String value) {
        this.value = value;
    }

    /// Einen enum-Wert als String mit dem internen Wert ausgeben.
    /// Dieser Wert wird durch Jackson in einem JSON-Datensatz verwendet.
    /// [Wiki-Seiten](https://github.com/FasterXML/jackson-databind/wiki)
    ///
    /// @return Der interne Wert.
    @JsonValue
    public String getValue() {
        return value;
    }

    /// Konvertierung eines Strings in einen Enum-Wert.
    ///
    /// @param value Der String, zu dem ein passender Enum-Wert ermittelt werden soll.
    /// @return Passender Enum-Wert oder null.
    @JsonCreator
    public static FamilienstandType of(final String value) {
        return Stream.of(values())
            .filter(familienstand -> familienstand.value.equalsIgnoreCase(value))
            .findFirst()
            .orElse(null);
    }
}

