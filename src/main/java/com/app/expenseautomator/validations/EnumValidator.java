package com.app.expenseautomator.validations;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    private Set<String> allowedValues;
    
    @Override
    public void initialize(ValidEnum annotation) {
        allowedValues = Stream.of(annotation.enumClass().getEnumConstants())
        .map(Enum::toString)
        .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(value)) {
            return false;
        }

        return allowedValues.contains(value);
    }
}
