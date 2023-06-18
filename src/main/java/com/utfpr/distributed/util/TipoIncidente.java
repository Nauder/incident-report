package com.utfpr.distributed.util;

import java.util.Objects;

public enum TipoIncidente {

    ALAGAMENTO(1, "Alagamento"),
    DESLIZAMENTO(2, "Deslizamento"),
    ACIDENTE_DE_CARRO(3, "Acidente de Carro"),
    OBSTRUCAO_DA_VIA(4, "Obstrução da Via"),
    FISSURA_DA_VIA(5, "Fissura da Via"),
    PISTA_EM_OBRAS(6, "Pista em Obras"),
    LENTIDAO_NA_PISTA(7, "Lentidão na Pista"),
    ANIMAIS_NA_PISTA(8, "Animais na Pista"),
    NEVOEIRO(9, "Nevoeiro"),
    TROMBA_DE_AGUA(10, "Tromba d'água");

    private int codigo;
    private String texto;

    TipoIncidente(int codigo, String texto) {
        this.codigo = codigo;
        this.texto = texto;
    }

    public static TipoIncidente getFromCodigo(int c) {
        for(TipoIncidente tipo : TipoIncidente.values()) {
            if(tipo.codigo == c) {
                return tipo;
            }
        }

        return null;
    }

    public static String getTextoFromCodigo(int c) {
        for(TipoIncidente tipo : TipoIncidente.values()) {
            if(tipo.codigo == c) {
                return tipo.texto;
            }
        }

        return null;
    }

    public static int getCodigoFromTexto(String t) {
        for(TipoIncidente tipo : TipoIncidente.values()) {
            if(tipo.texto.equals(t)) {
                return tipo.codigo;
            }
        }

        return 0;
    }

    public int getCodigo() {
        return codigo;
    }

    @Override
    public String toString() {
        return this.texto;
    }
}
