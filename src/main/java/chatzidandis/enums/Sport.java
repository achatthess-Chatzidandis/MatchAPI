package chatzidandis.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Sport {

    FOOTBALL(1),
    BASKETBALL(2);

    private final int value;

    Sport(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Sport fromValue(int value) {
        for (Sport s : Sport.values()) {
            if (s.value == value) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown sport value: " + value);
    }

    @JsonCreator
    public static Sport fromJson(Object value) {

        if (value instanceof Integer intValue) {
            return fromValue(intValue);
        }

        if (value instanceof String strValue) {

            // match enum name
            for (Sport s : values()) {
                if (s.name().equalsIgnoreCase(strValue)) {
                    return s;
                }
            }

            // match numeric string ("1", "2")
            try {
                int intValue = Integer.parseInt(strValue);
                return fromValue(intValue);
            } catch (NumberFormatException ignored) {
            }
        }

        throw new IllegalArgumentException("Unknown sport: " + value);
    }

    @JsonValue
    public String toJson() {
        return this.name(); // returns FOOTBALL, BASKETBALL
    }
}
