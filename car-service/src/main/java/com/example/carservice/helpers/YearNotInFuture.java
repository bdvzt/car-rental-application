package com.example.carservice.helpers;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = YearNotInFutureValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface YearNotInFuture {
    String message() default "Год выпуска не может быть в будущем";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
