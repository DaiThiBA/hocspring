package com.example.hocspring.validator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DobValidator implements ConstraintValidator<DobContraints, LocalDate> {
    private int min;

    @Override
    public void initialize(DobContraints constraintAnnotation) {
        this.min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if(Objects.isNull(value)) {
            return true; // null is handled by @NotNull
        }

        //Độ tuổi user tối thiểu = min

        long years = ChronoUnit.YEARS.between(value, LocalDate.now()) ;

        return years >= min;
    }

}
