package com.evaluacion.usuario_api.service.impl;


import com.evaluacion.usuario_api.config.PasswordEncoder;
import com.evaluacion.usuario_api.dto.UserRequestDTO;
import com.evaluacion.usuario_api.dto.UserResponseDTO;
import com.evaluacion.usuario_api.exception.CustomException;
import com.evaluacion.usuario_api.model.Telefono;
import com.evaluacion.usuario_api.model.Usuario;
import com.evaluacion.usuario_api.repository.UsuarioRepository;
import com.evaluacion.usuario_api.security.JwtUtil;
import com.evaluacion.usuario_api.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


    private static final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);
    private final UsuarioRepository repo;
    private final JwtUtil jwtUtil;

    @Value("${regex.password}")
    private String passwordRegex;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, JwtUtil jwtUtil, @Value("${regex.password}") String passwordRegex, PasswordEncoder passwordEncoder) {
        this.repo = usuarioRepository;
        this.jwtUtil = jwtUtil;
        this.passwordRegex = passwordRegex;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO crearUsuario(UserRequestDTO dto) {
        if (repo.findByCorreo(dto.getCorreo()).isPresent()) {
            throw new CustomException("El correo ya está registrado");
        }
        if (!dto.getContrasena().matches(passwordRegex)) {
            log.info("contraseña: {}", dto.getContrasena());
            throw new CustomException("La contraseña no cumple el formato");
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
    public Usuario obtenerUsuarioData(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new CustomException("Usuario no encontrado"));
    }

    @Override
    public UserResponseDTO actualizarUsuario(UUID id, UserRequestDTO dto) {
        Usuario existente = repo.findById(id)
                .orElseThrow(() -> new CustomException("Usuario no encontrado"));

        // Validar nuevo correo si cambió
        if (!existente.getCorreo().equals(dto.getCorreo())) {
            repo.findByCorreo(dto.getCorreo()).ifPresent(u -> {
                throw new CustomException("El correo ya está registrado");
            });
        }

        if (!dto.getContrasena().matches(passwordRegex)) {
            throw new CustomException("La contraseña no cumple el formato");
        }

        existente.setNombre(dto.getNombre());
        existente.setCorreo(dto.getCorreo());
        existente.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        existente.setModificado(LocalDateTime.now());

        if (dto.getTelefonos() != null) {
            List<Telefono> telefonos = dto.getTelefonos().stream()
                    .map(t -> new Telefono(null, t.getNumero(), t.getCodigoCiudad(), t.getCodigoPais()))
                    .collect(Collectors.toList());
            existente.setTelefonos(telefonos);
        }

        Usuario actualizado = repo.save(existente);
        return new UserResponseDTO(actualizado.getId(), actualizado.getCreado(), actualizado.getModificado(),
                actualizado.getUltimoLogin(), actualizado.getToken(), actualizado.isActivo());
    }

    @Override
    public UserResponseDTO actualizarParcial(UUID id, UserRequestDTO dto) {
        Usuario existente = repo.findById(id)
                .orElseThrow(() -> new CustomException("Usuario no encontrado"));

        if (dto.getNombre() != null) {
            existente.setNombre(dto.getNombre());
        }

        if (dto.getCorreo() != null && !dto.getCorreo().equals(existente.getCorreo())) {
            repo.findByCorreo(dto.getCorreo()).ifPresent(u -> {
                throw new CustomException("El correo ya está registrado");
            });
            existente.setCorreo(dto.getCorreo());
        }

        if (dto.getContrasena() != null) {
            if (!dto.getContrasena().matches(passwordRegex)) {
                throw new CustomException("La contraseña no cumple el formato");
            }
            existente.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        }

        if (dto.getTelefonos() != null) {
            List<Telefono> telefonos = dto.getTelefonos().stream()
                    .map(t -> new Telefono(null, t.getNumero(), t.getCodigoCiudad(), t.getCodigoPais()))
                    .collect(Collectors.toList());
            existente.setTelefonos(telefonos);
        }

        existente.setModificado(LocalDateTime.now());

        Usuario actualizado = repo.save(existente);
        return new UserResponseDTO(actualizado.getId(), actualizado.getCreado(), actualizado.getModificado(),
                actualizado.getUltimoLogin(), actualizado.getToken(), actualizado.isActivo());
    }


    @Override
    public void eliminarUsuario(UUID id) {
        if (!repo.existsById(id)) {
            throw new CustomException("Usuario no encontrado");
        }
        repo.deleteById(id);
    }
}