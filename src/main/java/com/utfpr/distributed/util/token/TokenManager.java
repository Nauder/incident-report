package com.utfpr.distributed.util.token;

import com.utfpr.distributed.controller.ServerController;

import java.util.HashMap;
import java.util.Map;

public abstract class TokenManager {

    private static final Map<Integer, String> TOKENS = new HashMap<>();

    protected TokenManager() {
    }

    public static String getToken(Integer id) {

        return TOKENS.containsKey(id) ? TOKENS.get(id) : null;
    }

    public static void deleteToken(Integer id) {

        TOKENS.remove(id);
        ServerController.refreshLogins();
    }

    public static Map<Integer, String> getTokens() {

        return TOKENS;
    }
}
