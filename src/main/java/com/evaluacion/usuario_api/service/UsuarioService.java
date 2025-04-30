package com.evaluacion.usuario_api.service;

import com.evaluacion.usuario_api.dto.UserRequestDTO;
import com.evaluacion.usuario_api.dto.UserResponseDTO;
import com.evaluacion.usuario_api.model.Usuario;

import java.util.UUID;


public interface UsuarioService {
    UserResponseDTO crearUsuario(UserRequestDTO dto);
    UserResponseDTO iniciarSesion(UserRequestDTO dto);
    UserResponseDTO obtenerUsuario(UUID id);
    Usuario obtenerUsuarioData(UUID id);
    UserResponseDTO actualizarUsuario(UUID id, UserRequestDTO dto);
    UserResponseDTO actualizarParcial(UUID id, UserRequestDTO dto);
    void eliminarUsuario(UUID id);
}