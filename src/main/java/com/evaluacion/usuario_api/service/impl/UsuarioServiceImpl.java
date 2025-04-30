package com.evaluacion.usuario_api.service.impl;


import com.evaluacion.usuario_api.config.PasswordEncoder;
import com.evaluacion.usuario_api.dto.TelefonoDTO;
import com.evaluacion.usuario_api.dto.UserRequestDTO;
import com.evaluacion.usuario_api.dto.UserResponseDTO;
import com.evaluacion.usuario_api.exception.CustomException;
import com.evaluacion.usuario_api.model.Telefono;
import com.evaluacion.usuario_api.model.Usuario;
import com.evaluacion.usuario_api.repository.UsuarioRepository;
import com.evaluacion.usuario_api.security.JwtUtil;
import com.evaluacion.usuario_api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repo;
    private final JwtUtil jwtUtil;

    @Value("${regex.password}")
    private String passwordRegex;

    @Value("${regex.email}")
    private String correo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository repo, JwtUtil jwtUtil) {
        this.repo = repo;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserResponseDTO crearUsuario(UserRequestDTO dto) {
        if (repo.findByCorreo(dto.getCorreo()).isPresent()) {
            throw new CustomException("El correo ya está registrado");
        }
        if (!dto.getContrasena().matches(passwordRegex)) {
            throw new CustomException("La contraseña no cumple el formato");
        }
        if (!dto.getCorreo().matches(correo)) {
            throw new CustomException("El correo no cumple el formato");
        }
        Usuario u = new Usuario();
        u.setNombre(dto.getNombre());
        u.setCorreo(dto.getCorreo());
        u.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        u.setCreado(LocalDateTime.now());
        u.setModificado(u.getCreado());
        u.setUltimoLogin(u.getCreado());
        u.setActivo(true);
        List<Telefono> telefonos = dto.getTelefonos()
                .stream()
                .map(t -> {
                    Telefono tel = new Telefono();
                    tel.setNumero(t.getNumero());
                    tel.setCodigoCiudad(t.getCodigoCiudad());
                    tel.setCodigoPais(t.getCodigoPais());
                    return tel;
                })
                .collect(Collectors.toList());
        u.setTelefonos(telefonos);
        u.setToken(jwtUtil.generateToken(u.getCorreo()));
        Usuario saved = repo.save(u);
        return new UserResponseDTO(saved.getId(), saved.getCreado(), saved.getModificado(),
                saved.getUltimoLogin(), saved.getToken(), saved.isActivo());
    }

    @Override
    public UserResponseDTO iniciarSesion(UserRequestDTO id) {
        return new UserResponseDTO(null,null,null,null,null,false);
    }

    @Override
    public UserResponseDTO obtenerUsuario(UUID id) {
        Usuario u = repo.findById(id)
                .orElseThrow(() -> new CustomException("Usuario no encontrado"));
        return new UserResponseDTO(u.getId(), u.getCreado(), u.getModificado(),
                u.getUltimoLogin(), u.getToken(), u.isActivo());
    }

    @Override
    public UserResponseDTO actualizarUsuario(UUID id, UserRequestDTO dto) {
        // implementación similar a crear, asignando todos los campos
        return obtenerUsuario(id);
    }

    @Override
    public UserResponseDTO actualizarParcial(UUID id, UserRequestDTO dto) {
        // implementar actualizaciones parciales
        return obtenerUsuario(id);
    }

    @Override
    public void eliminarUsuario(UUID id) {
        if (!repo.existsById(id)) {
            throw new CustomException("Usuario no encontrado");
        }
        repo.deleteById(id);
    }
}