package com.tundama.negociotundamabackend.controller;

import com.tundama.negociotundamabackend.model.dto.ApiResponse;
import com.tundama.negociotundamabackend.model.entity.Proveedor;
import com.tundama.negociotundamabackend.service.contracts.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "http://localhost:5173")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    /**
     * Endpoint para registrar un nuevo proveedor
     * Corresponde al formulario proveedor.html
     */
    @PostMapping("/registrar")
    public ResponseEntity<ApiResponse<Proveedor>> registrarProveedor(@RequestBody Proveedor proveedor) {
        try {
            Proveedor nuevoProveedor = proveedorService.registrarProveedor(proveedor);
            return ResponseEntity.ok(ApiResponse.success("Proveedor registrado exitosamente", nuevoProveedor));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error al registrar proveedor: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para obtener todos los proveedores activos
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Proveedor>>> obtenerTodos() {
        List<Proveedor> proveedores = proveedorService.obtenerTodosActivos();
        return ResponseEntity.ok(ApiResponse.success("Proveedores obtenidos", proveedores));
    }

    /**
     * Endpoint para buscar proveedor por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Proveedor>> buscarPorId(@PathVariable Long id) {
        Optional<Proveedor> proveedorOpt = proveedorService.buscarPorId(id);

        if (proveedorOpt.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("Proveedor encontrado", proveedorOpt.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Proveedor no encontrado con ID: " + id));
        }
    }

    /**
     * Endpoint para buscar proveedor por NIT
     */
    @GetMapping("/identificacion/{identificacion}")
    public ResponseEntity<ApiResponse<Proveedor>> buscarPorIdentificacion(@PathVariable String identificacion) {
        Optional<Proveedor> proveedorOpt = proveedorService.buscarPorIdentificacion(identificacion);

        if (proveedorOpt.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("Proveedor encontrado", proveedorOpt.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Proveedor no encontrado con NIT: " + identificacion));
        }
    }

    /**
     * Endpoint para buscar proveedores por nombre de empresa
     */
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<Proveedor>>> buscarPorNombreEmpresa(@RequestParam String nombre) {
        List<Proveedor> proveedores = proveedorService.buscarPorNombreEmpresa(nombre);
        return ResponseEntity.ok(ApiResponse.success("Proveedores encontrados", proveedores));
    }

    /**
     * Endpoint para obtener proveedores con productos activos
     */
    @GetMapping("/con-productos")
    public ResponseEntity<ApiResponse<List<Proveedor>>> obtenerConProductos() {
        List<Proveedor> proveedores = proveedorService.obtenerProveedoresConProductosActivos();
        return ResponseEntity.ok(ApiResponse.success("Proveedores con productos", proveedores));
    }

    /**
     * Endpoint para actualizar proveedor
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Proveedor>> actualizarProveedor(@PathVariable Long id, @RequestBody Proveedor proveedor) {
        try {
            Proveedor proveedorActualizado = proveedorService.actualizarProveedor(id, proveedor);
            return ResponseEntity.ok(ApiResponse.success("Proveedor actualizado", proveedorActualizado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error al actualizar: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para eliminar proveedor (lógico)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarProveedor(@PathVariable Long id) {
        try {
            proveedorService.eliminarProveedor(id);
            return ResponseEntity.ok(ApiResponse.success("Proveedor eliminado correctamente", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error al eliminar: " + e.getMessage()));
        }
    }
}