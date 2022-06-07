package cz.muni.ics.services.validation;


import cz.muni.ics.services.implementations.web.dto.UserDataDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {}

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        UserDataDto user = (UserDataDto) obj;
        return user.getPassword().equals(user.getMatchingPassword());
    }
}
