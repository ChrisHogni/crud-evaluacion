package com.evaluacion.usuario_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public class UserRequestDTO {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;

    @NotEmpty
    private List<TelefonoDTO> telefonos;

    public @NotBlank(message = "El nombre es obligatorio") String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank(message = "El nombre es obligatorio") String nombre) {
        this.nombre = nombre;
    }

    public @NotBlank(message = "El correo es obligatorio") @Email(message = "El correo no tiene un formato válido") String getCorreo() {
        return correo;
    }

    public void setCorreo(@NotBlank(message = "El correo es obligatorio") @Email(message = "El correo no tiene un formato válido") String correo) {
        this.correo = correo;
    }

    public @NotBlank(message = "La contraseña es obligatoria") String getContrasena() {
        return contrasena;
    }

    public void setContrasena(@NotBlank(message = "La contraseña es obligatoria") String contrasena) {
        this.contrasena = contrasena;
    }

    public @NotEmpty List<TelefonoDTO> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(@NotEmpty List<TelefonoDTO> telefonos) {
        this.telefonos = telefonos;
    }
}