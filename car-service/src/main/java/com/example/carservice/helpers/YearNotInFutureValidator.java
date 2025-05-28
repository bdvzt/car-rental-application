package com.example.carservice.helpers;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Year;

public class YearNotInFutureValidator implements ConstraintValidator<YearNotInFuture, Integer> {

    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext context) {
        if (year == null) return true;
        int currentYear = Year.now().getValue();
        return year <= currentYear;
    }
}

