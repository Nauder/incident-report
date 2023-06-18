package com.utfpr.distributed.model;

public final class Incidente {
    private int id_incidente;
    private String data;
    private String hora;
    private String estado;
    private String cidade;
    private String bairro;
    private String rua;
    private int tipo_incidente;

    public Incidente() {
    }

    public Incidente(int id_incidente, String data, String hora, String estado, String cidade, String bairro, String rua, int tipo_incidente) {
        this.id_incidente = id_incidente;
        this.data = data;
        this.hora = hora;
        this.estado = estado;
        this.cidade = cidade;
        this.bairro = bairro;
        this.rua = rua;
        this.tipo_incidente = tipo_incidente;
    }

    public int getId_incidente() {
        return id_incidente;
    }

    public void setId_incidente(int id_incidente) {
        this.id_incidente = id_incidente;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public int getTipo_incidente() {
        return tipo_incidente;
    }

    public void setTipo_incidente(int tipo_incidente) {
        this.tipo_incidente = tipo_incidente;
    }
}
