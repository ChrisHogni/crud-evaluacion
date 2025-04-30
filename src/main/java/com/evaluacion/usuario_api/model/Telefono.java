package com.evaluacion.usuario_api.model;


import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "telefonos")
public class Telefono {

    @Id
    @GeneratedValue
    private UUID id;

    private String numero;
    private String codigoCiudad;
    private String codigoPais;

    public Telefono(UUID id, String numero, String codigoCiudad, String codigoPais) {
        this.id = id;
        this.numero = numero;
        this.codigoCiudad = codigoCiudad;
        this.codigoPais = codigoPais;
    }
// Getters y Setters
public Telefono(){
}
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCodigoCiudad() {
        return codigoCiudad;
    }

    public void setCodigoCiudad(String codigoCiudad) {
        this.codigoCiudad = codigoCiudad;
    }

    public String getCodigoPais() {
        return codigoPais;
    }

    public void setCodigoPais(String codigoPais) {
        this.codigoPais = codigoPais;
    }
}