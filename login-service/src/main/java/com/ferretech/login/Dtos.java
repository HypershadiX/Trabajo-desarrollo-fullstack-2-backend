package com.ferretech.login;

import jakarta.validation.constraints.NotBlank;

public final class Dtos {

    private Dtos() {
    }

    public record LoginRequest(
            @NotBlank(message = "Usuario o contraseña incorrectos.") String usuario,
            @NotBlank(message = "Usuario o contraseña incorrectos.") String contrasena) {
    }

    public record RegistroRequest(
            @NotBlank(message = "Completa usuario y contraseña.") String usuario,
            @NotBlank(message = "Completa usuario y contraseña.") String contrasena) {
    }

    public record RecuperarRequest(
            @NotBlank(message = "Ingresa tu nombre de usuario.") String usuario) {
    }

    public record RestablecerRequest(
            @NotBlank(message = "Ingresa tu nombre de usuario.") String usuario,
            @NotBlank(message = "Ingresa el código que enviamos a tu correo.") String codigo,
            @NotBlank(message = "Ingresa tu nueva contraseña.") String nuevaContrasena) {
    }

    /** Lo que el frontend guarda como sesión. No incluye la contraseña. */
    public record UsuarioRespuesta(String usuario, Rol rol) {
    }

    public record MensajeRespuesta(String mensaje) {
    }

    public record CodigoRespuesta(String mensaje, String codigo) {
    }
}
