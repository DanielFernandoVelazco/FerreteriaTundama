package com.tundama.negociotundamabackend.repository;

import com.tundama.negociotundamabackend.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar usuario por email (para login)
    Optional<Usuario> findByEmail(String email);

    // Buscar usuario por ID de usuario (formato NTU-...)
    Optional<Usuario> findByIdUsuario(String idUsuario);

    // Verificar si existe un email
    boolean existsByEmail(String email);

    // Buscar usuarios activos
    List<Usuario> findByActivoTrue();

    // Buscar por nombre (búsqueda parcial, sin distinción de mayúsculas)
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);

    // Query personalizada para validar credenciales
    @Query("SELECT u FROM Usuario u WHERE u.email = :email AND u.contrasena = :contrasena AND u.activo = true")
    Optional<Usuario> validarCredenciales(@Param("email") String email, @Param("contrasena") String contrasena);
}