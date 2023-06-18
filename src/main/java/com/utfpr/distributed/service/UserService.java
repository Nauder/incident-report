package com.utfpr.distributed.service;

import com.utfpr.distributed.database.UserDataAccess;
import com.utfpr.distributed.model.User;
import com.utfpr.distributed.util.ClientSession;
import com.utfpr.distributed.util.EncryptionHelper;
import com.utfpr.distributed.util.InputManager;
import com.utfpr.distributed.util.token.ServerTokenManager;
import com.utfpr.distributed.util.token.TokenManager;
import com.utfpr.distributed.validation.UserValidator;
import org.json.JSONObject;

import java.util.Map;

public final class UserService {
    private static final String[] KEYS = { "operacao", "status" };

    private UserService() {
    }

    public static String login(JSONObject json) {

        final JSONObject responseObject = new JSONObject();
        final String email = (String) json.query("/email");
        responseObject.put(KEYS[0], 2);

        if (!UserValidator.checkEmail(email)) {
            responseObject.put(KEYS[1], "formato de email invalido");
        } else if (!UserValidator.checkPassword(EncryptionHelper.decrypt((String) json.query("/senha")))) {
            responseObject.put(KEYS[1], "formato de senha invalido");
        } else {
            final User user = UserDataAccess.getLogin(email, (String) json.query("/senha"));
            if (user != null) {

                responseObject.put(KEYS[1], "OK");
                responseObject.put("token", ServerTokenManager.generateToken(user.getId()));
                responseObject.put("nome", user.getNome());
                responseObject.put("id", user.getId());
            } else {

                responseObject.put(KEYS[1], "email ou senha incorretos");
            }
        }

        return responseObject.toString();
    }

    public static String delete(JSONObject json) {

        final JSONObject responseObject = new JSONObject();
        final Integer id = (Integer) json.query("/id");
        responseObject.put(KEYS[0], 8);

        if (TokenManager.getToken(id) == null) {
            responseObject.put(KEYS[1], "id incorreto");
        } else if (!TokenManager.getToken(id).equals(json.query("/token"))) {
            responseObject.put(KEYS[1], "token incorreto");
        } else if (!UserDataAccess.validatePassword(id, (String) json.query("/senha"))) {
            responseObject.put(KEYS[1], "id ou senha incorretos");
        } else {
            UserDataAccess.delete(id);
            TokenManager.deleteToken(id);
            responseObject.put(KEYS[1], "OK");
        }

        return responseObject.toString();
    }

    public static String logout(JSONObject json) {

        final JSONObject responseObject = new JSONObject();
        responseObject.put(KEYS[0], 9);

        if (TokenManager.getToken((Integer) json.query("/id")) == null) {
            responseObject.put(KEYS[1], "id incorreto ou token não gerado");
        } else if (!TokenManager.getToken((Integer) json.query("/id")).equals(json.query("/token"))) {
            responseObject.put(KEYS[1], "token incorreto");
        } else {
            TokenManager.deleteToken(((Integer) json.query("/id")));
            responseObject.put(KEYS[1], "OK");
        }

        return responseObject.toString();
    }

    public static String create(JSONObject json) {

        final JSONObject responseObject = new JSONObject();
        responseObject.put(KEYS[0], 1);

        if (!UserValidator.checkEmail((String) json.query("/email"))) {
            responseObject.put(KEYS[1], "formato de email invalido");
        } else if (!UserValidator.checkPassword(EncryptionHelper.decrypt((String) json.query("/senha")))) {
            responseObject.put(KEYS[1], "formato de senha invalido");
        } else if (!UserValidator.checkName((String) json.query("/nome"))) {
            responseObject.put(KEYS[1], "formato de nome invalido");
        } else if (UserDataAccess.findEmail((String) json.query("/email"))) {
            responseObject.put(KEYS[1], "email já existe");
        } else {
            UserDataAccess.create(json);
            responseObject.put(KEYS[1], "OK");
        }

        return responseObject.toString();
    }

    public static String update(JSONObject json) {

        final JSONObject responseObject = new JSONObject();
        responseObject.put(KEYS[0], 3);

        if(!UserValidator.checkFields(json)) {
            responseObject.put(KEYS[1], "campos obrigatórios faltantes");
        } else if (!UserValidator.checkEmail((String) json.query("/email"))) {
            responseObject.put(KEYS[1], "formato de email invalido");
        } else if (!UserValidator.checkPassword(EncryptionHelper.decrypt((String) json.query("/senha")))) {
            responseObject.put(KEYS[1], "formato de senha invalido");
        } else if (!UserValidator.checkName((String) json.query("/nome"))) {
            responseObject.put(KEYS[1], "formato de nome invalido");
        } else if (UserDataAccess.findEmailBesidesCurrent((String) json.query("/email"), (Integer) json.query("/id"))) {
            responseObject.put(KEYS[1], "email já existe");
        } else if (TokenManager.getToken((Integer) json.query("/id")) == null) {
            responseObject.put(KEYS[1], "id incorreto ou token não gerado");
        } else if (!TokenManager.getToken((Integer) json.query("/id")).equals(json.query("/token"))) {
            responseObject.put(KEYS[1], "token incorreto");
        } else {
            TokenManager.deleteToken(((Integer) json.query("/id")));
            UserDataAccess.update(json);
            responseObject.put(KEYS[1], "OK");
        }

        return responseObject.toString();
    }

    public static String requestLogin(Map<String, Object> input) {

        final JSONObject requestObject = new JSONObject();

        requestObject.put("operacao", 2);
        requestObject.put("email", input.get("email"));
        requestObject.put("senha", EncryptionHelper.encrypt((String) input.get("senha")));

        return requestObject.toString();
    }

    public static String requestCreate(Map<String, Object> input) {

        final JSONObject requestObject = new JSONObject();

        requestObject.put(KEYS[0], 1);
        requestObject.put("nome", input.get("nome"));
        requestObject.put("email", input.get("email"));
        requestObject.put("senha", EncryptionHelper.encrypt((String) input.get("senha")));

        return requestObject.toString();
    }

    public static String requestDelete() {

        final JSONObject requestObject = new JSONObject();

        requestObject.put(KEYS[0], 8);
        requestObject.put("id", ClientSession.getId());
        requestObject.put("token", ClientSession.getToken());
        requestObject.put("senha", EncryptionHelper.encrypt(ClientSession.getSenha()));

        return requestObject.toString();
    }

    public static String requestLogout() {

        final JSONObject requestObject = new JSONObject();

        requestObject.put(KEYS[0], 9);
        requestObject.put("id", ClientSession.getId());
        requestObject.put("token", ClientSession.getToken());

        return requestObject.toString();
    }

    public static String requestUpdate(Map<String, Object> input) {

        final JSONObject requestObject = new JSONObject();

        requestObject.put(KEYS[0], 3);
        requestObject.put("nome", input.get("nome"));
        requestObject.put("email", input.get("email"));
        requestObject.put("senha", EncryptionHelper.encrypt((String) input.get("senha")));
        requestObject.put("id", ClientSession.getId());
        requestObject.put("token", ClientSession.getToken());

        return requestObject.toString();
    }

}
