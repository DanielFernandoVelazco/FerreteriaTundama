package com.tundama.negociotundamabackend.service.contracts;

import com.tundama.negociotundamabackend.model.entity.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    /**
     * Registra un nuevo usuario en el sistema
     * @param usuario Datos del usuario a registrar
     * @return Usuario registrado con ID asignado
     */
    Usuario registrarUsuario(Usuario usuario);

    /**
     * Busca un usuario por su ID
     * @param id ID del usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> buscarPorId(Long id);

    /**
     * Busca un usuario por su email
     * @param email Email del usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> buscarPorEmail(String email);

    /**
     * Busca un usuario por su ID de usuario (formato NTU-...)
     * @param idUsuario ID de usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> buscarPorIdUsuario(String idUsuario);

    /**
     * Obtiene todos los usuarios activos
     * @return Lista de usuarios activos
     */
    List<Usuario> obtenerTodosActivos();

    /**
     * Actualiza los datos de un usuario existente
     * @param id ID del usuario a actualizar
     * @param usuario Datos actualizados
     * @return Usuario actualizado
     */
    Usuario actualizarUsuario(Long id, Usuario usuario);

    /**
     * Elimina lógicamente un usuario (cambia activo a false)
     * @param id ID del usuario a eliminar
     */
    void eliminarUsuario(Long id);

    /**
     * Valida las credenciales de un usuario para login
     * @param email Email del usuario
     * @param contrasena Contraseña
     * @return Optional con el usuario si credenciales válidas
     */
    Optional<Usuario> validarCredenciales(String email, String contrasena);

    /**
     * Cambia la contraseña de un usuario
     * @param id ID del usuario
     * @param contrasenaActual Contraseña actual
     * @param contrasenaNueva Nueva contraseña
     * @return true si se cambió exitosamente
     */
    boolean cambiarContrasena(Long id, String contrasenaActual, String contrasenaNueva);

    /**
     * Verifica si un email ya está registrado
     * @param email Email a verificar
     * @return true si ya existe
     */
    boolean existeEmail(String email);
}