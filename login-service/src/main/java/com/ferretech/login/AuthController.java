package com.ferretech.login;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService servicio;

    public AuthController(AuthService servicio) {
        this.servicio = servicio;
    }

    @PostMapping("/login")
    public Dtos.UsuarioRespuesta login(@Valid @RequestBody Dtos.LoginRequest datos) {
        return servicio.login(datos.usuario(), datos.contrasena(), Rol.CLIENTE);
    }

    @PostMapping("/login-admin")
    public Dtos.UsuarioRespuesta loginAdmin(@Valid @RequestBody Dtos.LoginRequest datos) {
        return servicio.login(datos.usuario(), datos.contrasena(), Rol.ADMIN);
    }

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public Dtos.MensajeRespuesta registro(@Valid @RequestBody Dtos.RegistroRequest datos) {
        return servicio.registrar(datos.usuario(), datos.contrasena());
    }

    @PostMapping("/recuperar")
    public Dtos.CodigoRespuesta recuperar(@Valid @RequestBody Dtos.RecuperarRequest datos) {
        return servicio.solicitarCodigo(datos.usuario());
    }

    @PostMapping("/restablecer")
    public Dtos.MensajeRespuesta restablecer(@Valid @RequestBody Dtos.RestablecerRequest datos) {
        return servicio.restablecer(datos.usuario(), datos.codigo(), datos.nuevaContrasena());
    }
}
