package chatzidandis.validation;

import chatzidandis.dto.MatchDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MatchValidator implements ConstraintValidator<ValidMatch, MatchDTO> {

    @Override
    public boolean isValid(MatchDTO dto, ConstraintValidatorContext context) {

        if (dto == null) {
            return true;
        }

        boolean valid = true;

        // Disable default message
        context.disableDefaultConstraintViolation();

        // 1. teamA must be different from teamB
        if (dto.getTeamA() != null && dto.getTeamA().equalsIgnoreCase(dto.getTeamB())) {

            context.buildConstraintViolationWithTemplate("teamA and teamB must be different")
                            .addPropertyNode("teamA")
                            .addConstraintViolation();

            valid = false;
        }

        // 2. description must be "teamA-teamB"
        if (dto.getTeamA() != null && dto.getTeamB() != null && dto.getDescription() != null) {

            String expected = dto.getTeamA().trim() + "-" + dto.getTeamB().trim();

            if (!dto.getDescription().equals(expected)) {

                context.buildConstraintViolationWithTemplate(
                                                "description must be in format: " + expected)
                                .addPropertyNode("description")
                                .addConstraintViolation();

                valid = false;
            }
        }

        return valid;
    }
}
