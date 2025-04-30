package com.evaluacion.usuario_api.service;

import com.evaluacion.usuario_api.controller.UsuarioController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.evaluacion.usuario_api.dto.UserRequestDTO;
import com.evaluacion.usuario_api.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.context.annotation.Import;

@WebMvcTest(controllers = UsuarioController.class)
class UsuarioControllerTest {


}
