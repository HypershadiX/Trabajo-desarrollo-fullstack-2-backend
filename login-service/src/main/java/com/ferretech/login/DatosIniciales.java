package com.ferretech.login;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/** Crea las mismas cuentas por defecto que tenía el frontend (solo si la tabla está vacía). */
@Component
public class DatosIniciales implements CommandLineRunner {

    private final UsuarioRepository repositorio;
    private final PasswordEncoder encoder;

    public DatosIniciales(UsuarioRepository repositorio, PasswordEncoder encoder) {
        this.repositorio = repositorio;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (repositorio.count() > 0) {
            return;
        }
        repositorio.save(new Usuario("Mauricio", encoder.encode("1234"), Rol.CLIENTE));
        repositorio.save(new Usuario("Admin", encoder.encode("1234"), Rol.ADMIN));
    }
}
