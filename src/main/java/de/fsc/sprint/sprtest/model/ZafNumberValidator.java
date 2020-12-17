package de.fsc.sprint.sprtest.model;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ZafNumberValidator implements ConstraintValidator<ZafNumberConstraint, String> {


    @Override
    public boolean isValid(String zafNr, ConstraintValidatorContext context) {
        //TODO
        return true;
    }


}
