package com.ferretech.login;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 60)
    private String usuario;

    /** Hash BCrypt, nunca la contraseña en texto plano. */
    @Column(nullable = false)
    private String contrasena;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Rol rol;

    protected Usuario() {
    }

    public Usuario(String usuario, String contrasena, Rol rol) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public Long getId() { return id; }
    public String getUsuario() { return usuario; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public Rol getRol() { return rol; }
}
