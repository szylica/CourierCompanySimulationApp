package org.szylica.files.validator.impl;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import org.szylica.files.model.UserData;
import org.szylica.files.validator.Validator;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDataValidator implements Validator<UserData> {


    //TODO finish validation
    @Override
    public Map<String, String> validate(UserData userData) {
        Map<String, String> errors = new HashMap<>();

        if (!EmailValidator.getInstance().isValid(userData.getEmail())){
            errors.put("email", "Email is not valid");
        };
        return errors;
    }
}
