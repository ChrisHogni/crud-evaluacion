package com.evaluacion.usuario_api.service;

import com.evaluacion.usuario_api.config.PasswordEncoder;
import com.evaluacion.usuario_api.dto.TelefonoDTO;
import com.evaluacion.usuario_api.dto.UserRequestDTO;
import com.evaluacion.usuario_api.dto.UserResponseDTO;
import com.evaluacion.usuario_api.exception.CustomException;
import com.evaluacion.usuario_api.model.Telefono;
import com.evaluacion.usuario_api.model.Usuario;
import com.evaluacion.usuario_api.repository.UsuarioRepository;
import com.evaluacion.usuario_api.security.JwtUtil;
import com.evaluacion.usuario_api.service.impl.UsuarioServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class UsuarioServiceTest {

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Value("${regex.password}")
    private String passwordRegex;

    private final String validEmail = "usuario@correo.com";
    private final String validPassword = "Password123213.,2312";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioService = new UsuarioServiceImpl(
                usuarioRepository,
                jwtUtil,
                passwordRegex,
                passwordEncoder
        );
    }

    @Test
    void crearUsuario_DeberiaCrearUsuarioCorrectamente() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setNombre("Juan");
        dto.setCorreo(validEmail);
        dto.setContrasena(validPassword);
        TelefonoDTO telefonoDTO = new TelefonoDTO();
        telefonoDTO.setCodigoCiudad("56");
        telefonoDTO.setNumero("39232942");
        telefonoDTO.setCodigoPais("56");
        dto.setTelefonos(List.of(telefonoDTO));

        when(usuarioRepository.findByCorreo(validEmail)).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArguments()[0]);

        UserResponseDTO response = usuarioService.crearUsuario(dto);

        assertNotNull(response);
        assertTrue(response.isActivo());
    }

    @Test
    void crearUsuario_DeberiaFallarPorCorreoExistente() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setNombre("Juan");
        dto.setCorreo(validEmail);
        dto.setContrasena(validPassword);

        when(usuarioRepository.findByCorreo(validEmail)).thenReturn(Optional.of(new Usuario()));

        CustomException ex = assertThrows(CustomException.class, () -> usuarioService.crearUsuario(dto));
        assertEquals("El correo ya está registrado", ex.getMessage());
    }

    @Test
    void crearUsuario_DeberiaFallarPorPasswordInvalido() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setNombre("Juan");
        dto.setCorreo(validEmail);
        dto.setContrasena("123");

        CustomException ex = assertThrows(CustomException.class, () -> usuarioService.crearUsuario(dto));
        assertEquals("La contraseña no cumple el formato", ex.getMessage());
    }

    @Test
    void actualizarUsuario_DeberiaActualizarCampos() {
        UUID id = UUID.randomUUID();
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setCorreo("original@correo.com");
        usuario.setContrasena("hashed1231231231.");

        UserRequestDTO dto = new UserRequestDTO();
        dto.setNombre("Nuevo Nombre");
        dto.setCorreo("nuevo@correo.com");
        dto.setContrasena("Password1233123123.");
        dto.setTelefonos(Collections.emptyList());

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findByCorreo(dto.getCorreo())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.getContrasena())).thenReturn("hashed-pass");
        when(usuarioRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        UserResponseDTO response = usuarioService.actualizarUsuario(id, dto);

        assertNotNull(response);
        assertEquals(id, response.getId());
    }

    @Test
    void actualizarParcial_DeberiaActualizarSoloCamposDiferentes() {
        UUID id = UUID.randomUUID();
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre("Antiguo");
        usuario.setCorreo(validEmail);

        UserRequestDTO dto = new UserRequestDTO();
        dto.setNombre("Nuevo Nombre");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        UserResponseDTO response = usuarioService.actualizarParcial(id, dto);

        assertNotNull(response);
        assertEquals(id, response.getId());
    }
}
