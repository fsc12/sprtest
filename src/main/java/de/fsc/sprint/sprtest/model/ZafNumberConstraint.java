package de.fsc.sprint.sprtest.model;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ZafNumberValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ZafNumberConstraint {
    String message() default "ZAF-Nummer ist ungültig";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}