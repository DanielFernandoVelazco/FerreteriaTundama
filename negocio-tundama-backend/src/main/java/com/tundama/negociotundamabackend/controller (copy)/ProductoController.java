package com.tundama.negociotundamabackend.controller;

import com.tundama.negociotundamabackend.model.dto.ApiResponse;
import com.tundama.negociotundamabackend.model.entity.Producto;
import com.tundama.negociotundamabackend.service.contracts.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    /**
     * Endpoint para registrar un nuevo producto
     */
    @PostMapping("/registrar")
    public ResponseEntity<ApiResponse<Producto>> registrarProducto(@RequestBody Producto producto) {
        try {
            Producto nuevoProducto = productoService.registrarProducto(producto);
            return ResponseEntity.ok(ApiResponse.success("Producto registrado exitosamente", nuevoProducto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error al registrar producto: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para obtener todos los productos activos
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Producto>>> obtenerTodos() {
        List<Producto> productos = productoService.obtenerTodosActivos();
        return ResponseEntity.ok(ApiResponse.success("Productos obtenidos", productos));
    }

    /**
     * Endpoint para buscar producto por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Producto>> buscarPorId(@PathVariable Long id) {
        Optional<Producto> productoOpt = productoService.buscarPorId(id);

        if (productoOpt.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("Producto encontrado", productoOpt.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Producto no encontrado con ID: " + id));
        }
    }

    /**
     * Endpoint para buscar producto por código
     */
    @GetMapping("/codigo/{idProducto}")
    public ResponseEntity<ApiResponse<Producto>> buscarPorIdProducto(@PathVariable String idProducto) {
        Optional<Producto> productoOpt = productoService.buscarPorIdProducto(idProducto);

        if (productoOpt.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("Producto encontrado", productoOpt.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Producto no encontrado con código: " + idProducto));
        }
    }

    /**
     * Endpoint para buscar productos por nombre
     */
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<Producto>>> buscarPorNombre(@RequestParam String nombre) {
        List<Producto> productos = productoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(ApiResponse.success("Productos encontrados", productos));
    }

    /**
     * Endpoint para obtener productos con stock bajo
     */
    @GetMapping("/stock-bajo")
    public ResponseEntity<ApiResponse<List<Producto>>> obtenerStockBajo() {
        List<Producto> productos = productoService.obtenerProductosConStockBajo();
        return ResponseEntity.ok(ApiResponse.success("Productos con stock bajo", productos));
    }

    /**
     * Endpoint para buscar por rango de precio
     */
    @GetMapping("/rango-precio")
    public ResponseEntity<ApiResponse<List<Producto>>> buscarPorRangoPrecio(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        List<Producto> productos = productoService.buscarPorRangoPrecio(min, max);
        return ResponseEntity.ok(ApiResponse.success("Productos en rango de precio", productos));
    }

    /**
     * Endpoint para asignar proveedor a producto
     */
    @PostMapping("/{productoId}/asignar-proveedor/{proveedorId}")
    public ResponseEntity<ApiResponse<Producto>> asignarProveedor(
            @PathVariable Long productoId,
            @PathVariable Long proveedorId) {
        try {
            Producto producto = productoService.asignarProveedor(productoId, proveedorId);
            return ResponseEntity.ok(ApiResponse.success("Proveedor asignado correctamente", producto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error al asignar proveedor: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para actualizar producto
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Producto>> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            Producto productoActualizado = productoService.actualizarProducto(id, producto);
            return ResponseEntity.ok(ApiResponse.success("Producto actualizado", productoActualizado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error al actualizar: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para eliminar producto (lógico)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarProducto(@PathVariable Long id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.ok(ApiResponse.success("Producto eliminado correctamente", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error al eliminar: " + e.getMessage()));
        }
    }
}