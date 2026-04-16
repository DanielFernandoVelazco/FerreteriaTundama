package com.tundama.negociotundamabackend.controller;

import com.tundama.negociotundamabackend.model.dto.ApiResponse;
import com.tundama.negociotundamabackend.model.dto.LoginDTO;
import com.tundama.negociotundamabackend.model.entity.Usuario;
import com.tundama.negociotundamabackend.service.contracts.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:5173") // Para React
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Endpoint para registrar un nuevo usuario
     * Corresponde al formulario signup.html
     */
    @PostMapping("/registrar")
    public ResponseEntity<ApiResponse<Usuario>> registrarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.registrarUsuario(usuario);
            return ResponseEntity.ok(ApiResponse.success("Usuario registrado exitosamente", nuevoUsuario));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error al registrar usuario: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para login de usuario
     * Corresponde al formulario index.html
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Usuario>> login(@RequestBody LoginDTO loginDTO) {
        Optional<Usuario> usuarioOpt = usuarioService.validarCredenciales(loginDTO.getEmail(), loginDTO.getContrasena());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setContrasena(null); // No enviar contraseña en la respuesta
            return ResponseEntity.ok(ApiResponse.success("Login exitoso", usuario));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Usuario o contraseña incorrectos"));
        }
    }

    /**
     * Endpoint para obtener todos los usuarios activos
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Usuario>>> obtenerTodos() {
        List<Usuario> usuarios = usuarioService.obtenerTodosActivos();
        // Ocultar contraseñas
        usuarios.forEach(u -> u.setContrasena(null));
        return ResponseEntity.ok(ApiResponse.success("Usuarios obtenidos", usuarios));
    }

    /**
     * Endpoint para buscar usuario por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Usuario>> buscarPorId(@PathVariable Long id) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setContrasena(null);
            return ResponseEntity.ok(ApiResponse.success("Usuario encontrado", usuario));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Usuario no encontrado con ID: " + id));
        }
    }

    /**
     * Endpoint para actualizar usuario
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Usuario>> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuario);
            usuarioActualizado.setContrasena(null);
            return ResponseEntity.ok(ApiResponse.success("Usuario actualizado", usuarioActualizado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error al actualizar: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para eliminar usuario (lógico)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarUsuario(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.ok(ApiResponse.success("Usuario eliminado correctamente", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error al eliminar: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para cambiar contraseña
     */
    @PostMapping("/{id}/cambiar-contrasena")
    public ResponseEntity<ApiResponse<Boolean>> cambiarContrasena(
            @PathVariable Long id,
            @RequestParam String contrasenaActual,
            @RequestParam String contrasenaNueva) {

        boolean cambiada = usuarioService.cambiarContrasena(id, contrasenaActual, contrasenaNueva);

        if (cambiada) {
            return ResponseEntity.ok(ApiResponse.success("Contraseña cambiada exitosamente", true));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error("Contraseña actual incorrecta", false));
        }
    }
}