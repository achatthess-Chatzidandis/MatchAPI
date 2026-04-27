package chatzidandis.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MatchValidator.class)
@Documented
public @interface ValidMatch {

    String message() default "Invalid match data";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
