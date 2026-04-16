package com.tundama.negociotundamabackend.controller;

import com.tundama.negociotundamabackend.model.dto.ApiResponse;
import com.tundama.negociotundamabackend.model.entity.Cliente;
import com.tundama.negociotundamabackend.service.contracts.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:5173")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    /**
     * Endpoint para registrar un nuevo cliente
     * Corresponde al formulario cliente.html
     */
    @PostMapping("/registrar")
    public ResponseEntity<ApiResponse<Cliente>> registrarCliente(@RequestBody Cliente cliente) {
        try {
            Cliente nuevoCliente = clienteService.registrarCliente(cliente);
            return ResponseEntity.ok(ApiResponse.success("Cliente registrado exitosamente", nuevoCliente));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error al registrar cliente: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para obtener todos los clientes activos
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Cliente>>> obtenerTodos() {
        List<Cliente> clientes = clienteService.obtenerTodosActivos();
        return ResponseEntity.ok(ApiResponse.success("Clientes obtenidos", clientes));
    }

    /**
     * Endpoint para buscar cliente por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Cliente>> buscarPorId(@PathVariable Long id) {
        Optional<Cliente> clienteOpt = clienteService.buscarPorId(id);

        if (clienteOpt.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("Cliente encontrado", clienteOpt.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Cliente no encontrado con ID: " + id));
        }
    }

    /**
     * Endpoint para buscar cliente por identificación (NIT o Cédula)
     */
    @GetMapping("/identificacion/{identificacion}")
    public ResponseEntity<ApiResponse<Cliente>> buscarPorIdentificacion(@PathVariable String identificacion) {
        Optional<Cliente> clienteOpt = clienteService.buscarPorIdentificacion(identificacion);

        if (clienteOpt.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("Cliente encontrado", clienteOpt.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Cliente no encontrado con identificación: " + identificacion));
        }
    }

    /**
     * Endpoint para buscar clientes por nombre
     */
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<Cliente>>> buscarPorNombre(@RequestParam String nombre) {
        List<Cliente> clientes = clienteService.buscarPorNombre(nombre);
        return ResponseEntity.ok(ApiResponse.success("Clientes encontrados", clientes));
    }

    /**
     * Endpoint para actualizar cliente
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Cliente>> actualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        try {
            Cliente clienteActualizado = clienteService.actualizarCliente(id, cliente);
            return ResponseEntity.ok(ApiResponse.success("Cliente actualizado", clienteActualizado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error al actualizar: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para eliminar cliente (lógico)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarCliente(@PathVariable Long id) {
        try {
            clienteService.eliminarCliente(id);
            return ResponseEntity.ok(ApiResponse.success("Cliente eliminado correctamente", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error al eliminar: " + e.getMessage()));
        }
    }
}