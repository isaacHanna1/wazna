package com.watad.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class MinimumAgeValidator implements ConstraintValidator<MinimumAge, LocalDate> {


    private int minAge;

    @Override
    public void initialize(MinimumAge theMinimumAge) {
        this.minAge = theMinimumAge.value();
    }

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext constraintValidatorContext) {
        if (birthDate == null) {
            return true; // Let @NotNull handle null checks
        }

        LocalDate today = LocalDate.now();

        if (birthDate.isAfter(today)) {
            return false;
        }

        int age = Period.between(birthDate, today).getYears();
        return age >= minAge;
    }

}
