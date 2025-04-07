package com.example.hocspring.validator;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;


@Target({ FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = { DobValidator.class })// bỏ validator vừa khai báo vào
public @interface DobContraints {
	String message() default "Invalid date of birth";

    int min();

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
