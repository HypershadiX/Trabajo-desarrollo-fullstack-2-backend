package com.ferretech.login;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Duration VIGENCIA_CODIGO = Duration.ofMinutes(10);

    private record CodigoRecuperacion(String codigo, Instant expira) {
    }

    private final UsuarioRepository repositorio;
    private final PasswordEncoder encoder;
    private final SecureRandom azar = new SecureRandom();
    private final Map<String, CodigoRecuperacion> codigos = new ConcurrentHashMap<>();

    public AuthService(UsuarioRepository repositorio, PasswordEncoder encoder) {
        this.repositorio = repositorio;
        this.encoder = encoder;
    }

    public Dtos.UsuarioRespuesta login(String usuario, String contrasena, Rol rolRequerido) {
        Usuario encontrado = repositorio.findByUsuario(usuario.trim())
                .filter(u -> encoder.matches(contrasena.trim(), u.getContrasena()))
                .filter(u -> rolRequerido == null || u.getRol() == rolRequerido)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED,
                        "Usuario o contraseña incorrectos."));
        return new Dtos.UsuarioRespuesta(encontrado.getUsuario(), encontrado.getRol());
    }

    public Dtos.MensajeRespuesta registrar(String usuario, String contrasena) {
        String nombre = usuario.trim();
        if (repositorio.existsByUsuario(nombre)) {
            throw new ApiException(HttpStatus.CONFLICT, "Ese usuario ya existe, elige otro.");
        }
        repositorio.save(new Usuario(nombre, encoder.encode(contrasena.trim()), Rol.CLIENTE));
        return new Dtos.MensajeRespuesta("Cuenta creada. Ya puedes iniciar sesión.");
    }

    /**
     * Simula el envío del correo: el código viaja en la respuesta para poder
     * mostrarlo en el frontend (igual que la simulación original).
     */
    public Dtos.CodigoRespuesta solicitarCodigo(String usuario) {
        String nombre = usuario.trim();
        // Los administradores no pueden recuperar contraseña por este flujo.
        repositorio.findByUsuario(nombre)
                .filter(u -> u.getRol() == Rol.CLIENTE)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Ese usuario no existe."));

        String codigo = String.valueOf(100000 + azar.nextInt(900000));
        codigos.put(nombre, new CodigoRecuperacion(codigo, Instant.now().plus(VIGENCIA_CODIGO)));

        String mensaje = "[Simulación de correo]\n\nHemos enviado un código de verificación al correo "
                + "asociado a \"" + nombre + "\".\n\nTu código es: " + codigo;
        return new Dtos.CodigoRespuesta(mensaje, codigo);
    }

    public Dtos.MensajeRespuesta restablecer(String usuario, String codigoIngresado, String nuevaContrasena) {
        String nombre = usuario.trim();
        CodigoRecuperacion guardado = codigos.get(nombre);

        if (guardado == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Primero solicita un código de verificación.");
        }
        if (Instant.now().isAfter(guardado.expira())) {
            codigos.remove(nombre);
            throw new ApiException(HttpStatus.BAD_REQUEST, "El código expiró, solicita uno nuevo.");
        }
        if (!guardado.codigo().equals(codigoIngresado.trim())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El código ingresado no es correcto.");
        }

        Usuario encontrado = repositorio.findByUsuario(nombre)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Ese usuario ya no existe."));
        encontrado.setContrasena(encoder.encode(nuevaContrasena.trim()));
        repositorio.save(encontrado);
        codigos.remove(nombre);

        return new Dtos.MensajeRespuesta("Contraseña actualizada. Ya puedes iniciar sesión.");
    }
}
