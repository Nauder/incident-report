package com.utfpr.distributed.util;

import com.utfpr.distributed.model.Incidente;

public final class ClientSession {

    private static int id;
    private static String token;
    private static String nome;
    private static String senha;
    private static String email;
    private static String endereco;
    private static int porta;

    private ClientSession() {
    }

    public static int getId() {

        return id;
    }

    public static void setId(int newId) {

        id = newId;
    }

    public static String getToken() {

        return token;
    }

    public static void setToken(String newToken) {

        token = newToken;
    }

    public static String getNome() {

        return nome;
    }

    public static void setNome(String newNome) {

        nome = newNome;
    }

    public static String getEmail() {

        return email;
    }

    public static void setEmail(String newEmail) {

        email = newEmail;
    }

    public static String getSenha() {

        return senha;
    }

    public static void setSenha(String newSenha) {

        senha = newSenha;
    }

    public static String getEndereco() {

        return endereco;
    }

    public static void setEndereco(String newEndereco) {

        endereco = newEndereco;
    }

    public static int getPorta() {

        return porta;
    }

    public static void setPorta(int newPorta) {

        porta = newPorta;
    }
}
