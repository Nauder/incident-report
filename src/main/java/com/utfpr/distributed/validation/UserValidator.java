package com.utfpr.distributed.validation;

import org.json.JSONObject;

public interface UserValidator {

    static boolean checkEmail(String email) {

        return email.trim().matches(
                "([a-zA-Z0-9]*[_$&+,:;=?#|'<>.^*()%!-]*){3,50}@([a-zA-Z0-9]*[$&+,:;=?@#|'<>.^*()%!-]*){3,10}");
    }

    static boolean checkPassword(String password) {

        return password.length() >= 5 && password.length() <= 10 && password.matches("[a-zA-Z0-9]*");
    }

    static boolean checkName(String name) {

        return name.length() >= 3 && name.length() <= 50;
    }

    static boolean checkFields(JSONObject fields) {

        return fields.has("id") && fields.has("nome") && fields.has("email") && fields.has("senha");
    }
}
