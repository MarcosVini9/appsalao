package com.example.appsalao.modelo;

public class Cliente {

    private int id;
    private String nomecliente;
    private String horariocliente;
    private String cabeleireirocliente;
    private String datacliente;

    public Cliente(int id, String nomecliente, String horariocliente, String cabeleireirocliente, String datacliente) {
        this.id = id;
        this.nomecliente = nomecliente;
        this.horariocliente = horariocliente;
        this.cabeleireirocliente = cabeleireirocliente;
        this.datacliente = datacliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomecliente() {
        return nomecliente;
    }

    public void setNomecliente(String nomecliente) {
        this.nomecliente = nomecliente;
    }

    public String getHorariocliente() {
        return horariocliente;
    }

    public void setHorariocliente(String horariocliente) {
        this.horariocliente = horariocliente;
    }

    public String getCabeleireirocliente() {
        return cabeleireirocliente;
    }

    public void setCabeleireirocliente(String cabeleireirocliente) {
        this.cabeleireirocliente = cabeleireirocliente;
    }

    public String getDatacliente() {
        return datacliente;
    }

    public void setDatacliente(String datacliente) {
        this.datacliente = datacliente;
    }
}
