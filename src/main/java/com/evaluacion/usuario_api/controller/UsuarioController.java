package com.evaluacion.usuario_api.controller;

import com.evaluacion.usuario_api.dto.UserRequestDTO;
import com.evaluacion.usuario_api.dto.UserResponseDTO;
import com.evaluacion.usuario_api.repository.UsuarioRepository;
import com.evaluacion.usuario_api.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "usuarios")
@Tag(name = "CRUD Evaluación Tag")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @Operation(summary = "Crear un usuario", description = "Registra un nuevo usuario en el sistema")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente")
    @PostMapping
    public ResponseEntity<UserResponseDTO> crear(@Valid @RequestBody UserRequestDTO dto) {
        log.info("Crear usuario: {}", dto);
        return ResponseEntity.ok(service.crearUsuario(dto));
    }

    @Operation(summary = "Obtener información de un usuario", description = "Busca información de un usuario en el sistema")
    @ApiResponse(responseCode = "201", description = "Usuario encontrado exitosamente")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.obtenerUsuario(id));
    }

    @Operation(summary = "Modificación un usuario", description = "Modifica un usuario en el sistema")
    @ApiResponse(responseCode = "201", description = "Usuario modificado exitosamente")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> actualizar(@PathVariable UUID id,
                                                      @Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(service.actualizarUsuario(id, dto));
    }

    @Operation(summary = "Modificación parcial de un usuario", description = "Modifica parcialmente un usuario en el sistema")
    @ApiResponse(responseCode = "201", description = "Usuario modificado exitosamente")
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> patch(@PathVariable UUID id,
                                                 @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(service.actualizarParcial(id, dto));
    }

    @Operation(summary = "Eliminación de un usuario", description = "Elimina un usuario en el sistema")
    @ApiResponse(responseCode = "201", description = "Usuario eliminado exitosamente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        service.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}