package com.tundama.negociotundamabackend.controller;

import com.tundama.negociotundamabackend.model.dto.ApiResponse;
import com.tundama.negociotundamabackend.model.dto.CompraDTO;
import com.tundama.negociotundamabackend.model.entity.Compra;
import com.tundama.negociotundamabackend.service.contracts.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "http://localhost:5173")
public class CompraController {

    @Autowired
    private CompraService compraService;

    /**
     * Endpoint para registrar una nueva compra
     * Corresponde al formulario compras.html
     */
    @PostMapping("/registrar")
    public ResponseEntity<ApiResponse<Compra>> registrarCompra(@RequestBody CompraDTO compraDTO) {
        try {
            Compra nuevaCompra = compraService.registrarCompra(compraDTO);
            return ResponseEntity.ok(ApiResponse.success("Compra registrada exitosamente", nuevaCompra));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error al registrar compra: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para obtener todas las compras
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Compra>>> obtenerTodas() {
        List<Compra> compras = compraService.obtenerTodas();
        return ResponseEntity.ok(ApiResponse.success("Compras obtenidas", compras));
    }

    /**
     * Endpoint para buscar compra por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Compra>> buscarPorId(@PathVariable Long id) {
        Optional<Compra> compraOpt = compraService.buscarPorId(id);

        if (compraOpt.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("Compra encontrada", compraOpt.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Compra no encontrada con ID: " + id));
        }
    }

    /**
     * Endpoint para buscar compra por ID de compra (formato NTCO-...)
     */
    @GetMapping("/codigo/{idCompra}")
    public ResponseEntity<ApiResponse<Compra>> buscarPorIdCompra(@PathVariable String idCompra) {
        Optional<Compra> compraOpt = compraService.buscarPorIdCompra(idCompra);

        if (compraOpt.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("Compra encontrada", compraOpt.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Compra no encontrada con código: " + idCompra));
        }
    }

    /**
     * Endpoint para obtener compras por rango de fechas
     */
    @GetMapping("/por-fechas")
    public ResponseEntity<ApiResponse<List<Compra>>> obtenerPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<Compra> compras = compraService.obtenerComprasPorFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(ApiResponse.success("Compras en el período", compras));
    }

    /**
     * Endpoint para obtener compras por proveedor
     */
    @GetMapping("/proveedor/{proveedorId}")
    public ResponseEntity<ApiResponse<List<Compra>>> obtenerPorProveedor(@PathVariable Long proveedorId) {
        try {
            List<Compra> compras = compraService.obtenerComprasPorProveedor(proveedorId);
            return ResponseEntity.ok(ApiResponse.success("Compras del proveedor", compras));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para obtener compra con todos sus detalles
     */
    @GetMapping("/{id}/detalles")
    public ResponseEntity<ApiResponse<Compra>> obtenerConDetalles(@PathVariable Long id) {
        Optional<Compra> compraOpt = compraService.obtenerCompraConDetalles(id);

        if (compraOpt.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("Compra con detalles", compraOpt.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Compra no encontrada con ID: " + id));
        }
    }

    /**
     * Endpoint para calcular total de compras en un período
     */
    @GetMapping("/total-periodo")
    public ResponseEntity<ApiResponse<BigDecimal>> calcularTotalPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        BigDecimal total = compraService.calcularTotalComprasPorPeriodo(fechaInicio, fechaFin);
        return ResponseEntity.ok(ApiResponse.success("Total de compras en el período", total));
    }

    /**
     * Endpoint para anular una compra
     */
    @PutMapping("/{id}/anular")
    public ResponseEntity<ApiResponse<Compra>> anularCompra(@PathVariable Long id) {
        try {
            Compra compraAnulada = compraService.anularCompra(id);
            return ResponseEntity.ok(ApiResponse.success("Compra anulada correctamente", compraAnulada));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error al anular compra: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para buscar compras por ID de comprador (empleado)
     */
    @GetMapping("/comprador/{idComprador}")
    public ResponseEntity<ApiResponse<List<Compra>>> buscarPorIdComprador(@PathVariable String idComprador) {
        List<Compra> compras = compraService.buscarPorIdComprador(idComprador);
        return ResponseEntity.ok(ApiResponse.success("Compras del empleado", compras));
    }
}