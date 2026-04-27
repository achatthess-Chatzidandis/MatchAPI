package chatzidandis.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Specifier {

    HOME("1"),
    DRAW("X"),
    AWAY("2");

    private final String value;

    Specifier(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Specifier fromValue(String value) {
        for (Specifier s : Specifier.values()) {
            if (s.value.equals(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown specifier: " + value);
    }

    @JsonCreator
    public static Specifier fromJson(String value) {

        // match enum name (HOME, DRAW, AWAY)
        for (Specifier s : values()) {
            if (s.name().equalsIgnoreCase(value)) {
                return s;
            }
        }

        // match DB value (1, X, 2)
        for (Specifier s : values()) {
            if (s.value.equalsIgnoreCase(value)) {
                return s;
            }
        }

        throw new IllegalArgumentException("Unknown specifier: " + value);
    }


    @JsonValue
    public String toJson() {
        return this.name(); // returns HOME, DRAW, AWAY
    }
}
