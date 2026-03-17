package com.tundama.negociotundamabackend.service.impl;

import com.tundama.negociotundamabackend.model.entity.Usuario;
import com.tundama.negociotundamabackend.repository.UsuarioRepository;
import com.tundama.negociotundamabackend.service.contracts.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public Usuario registrarUsuario(Usuario usuario) {
        // Generar ID de usuario en formato NTU-YYYY-MMM-XXXX
        if (usuario.getIdUsuario() == null || usuario.getIdUsuario().isEmpty()) {
            usuario.setIdUsuario(generarIdUsuario());
        }

        // Validar que el email no exista
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está registrado: " + usuario.getEmail());
        }

        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public Optional<Usuario> buscarPorIdUsuario(String idUsuario) {
        return usuarioRepository.findByIdUsuario(idUsuario);
    }

    @Override
    public List<Usuario> obtenerTodosActivos() {
        return usuarioRepository.findByActivoTrue();
    }

    @Override
    public Usuario actualizarUsuario(Long id, Usuario usuario) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        // Actualizar campos permitidos
        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setDireccion(usuario.getDireccion());
        usuarioExistente.setEmail(usuario.getEmail());
        usuarioExistente.setNotas(usuario.getNotas());

        return usuarioRepository.save(usuarioExistente);
    }

    @Override
    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        usuario.setActivo(false);  // Eliminación lógica
        usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> validarCredenciales(String email, String contrasena) {
        return usuarioRepository.validarCredenciales(email, contrasena);
    }

    @Override
    public boolean cambiarContrasena(Long id, String contrasenaActual, String contrasenaNueva) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        // Validar contraseña actual
        Optional<Usuario> validacion = usuarioRepository.validarCredenciales(usuario.getEmail(), contrasenaActual);

        if (validacion.isPresent()) {
            usuario.setContrasena(contrasenaNueva);
            usuarioRepository.save(usuario);
            return true;
        }

        return false;
    }

    @Override
    public boolean existeEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    /**
     * Genera un ID de usuario en formato: NTU-YYYY-MMM-XXXX
     * Ejemplo: NTU-2023-ABR-0074
     */
    private String generarIdUsuario() {
        LocalDateTime ahora = LocalDateTime.now();
        String año = String.valueOf(ahora.getYear());
        String mes = ahora.format(DateTimeFormatter.ofPattern("MMM")).toUpperCase();
        String numero = String.format("%04d", (int)(Math.random() * 10000));

        return "NTU-" + año + "-" + mes + "-" + numero;
    }
}