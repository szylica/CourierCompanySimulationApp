package org.szylica.data.validator.impl;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import org.szylica.data.model.UserData;
import org.szylica.data.validator.Validator;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDataValidator implements Validator<UserData> {

    Map<String, String> errors = new HashMap<>();

    //TODO finish validation
    @Override
    public Map<String, String> validate(UserData userData) {
        if (!EmailValidator.getInstance().isValid(userData.getEmail())){
            errors.put("email", "Email is not valid");
        };
        return errors;
    }
}
