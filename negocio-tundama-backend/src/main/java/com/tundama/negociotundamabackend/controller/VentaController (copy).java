package com.tundama.negociotundamabackend.controller;

import com.tundama.negociotundamabackend.model.dto.ApiResponse;
import com.tundama.negociotundamabackend.model.dto.VentaDTO;
import com.tundama.negociotundamabackend.model.entity.Venta;
import com.tundama.negociotundamabackend.service.contracts.VentaService;
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
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "http://localhost:5173")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    /**
     * Endpoint para registrar una nueva venta
     * Corresponde al formulario ventas.html
     */
    @PostMapping("/registrar")
    public ResponseEntity<ApiResponse<Venta>> registrarVenta(@RequestBody VentaDTO ventaDTO) {
        try {
            Venta nuevaVenta = ventaService.registrarVenta(ventaDTO);
            return ResponseEntity.ok(ApiResponse.success("Venta registrada exitosamente", nuevaVenta));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error al registrar venta: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para obtener todas las ventas
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Venta>>> obtenerTodas() {
        List<Venta> ventas = ventaService.obtenerTodas();
        return ResponseEntity.ok(ApiResponse.success("Ventas obtenidas", ventas));
    }

    /**
     * Endpoint para buscar venta por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Venta>> buscarPorId(@PathVariable Long id) {
        Optional<Venta> ventaOpt = ventaService.buscarPorId(id);

        if (ventaOpt.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("Venta encontrada", ventaOpt.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Venta no encontrada con ID: " + id));
        }
    }

    /**
     * Endpoint para buscar venta por ID de venta (formato NTV-...)
     */
    @GetMapping("/codigo/{idVenta}")
    public ResponseEntity<ApiResponse<Venta>> buscarPorIdVenta(@PathVariable String idVenta) {
        Optional<Venta> ventaOpt = ventaService.buscarPorIdVenta(idVenta);

        if (ventaOpt.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("Venta encontrada", ventaOpt.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Venta no encontrada con código: " + idVenta));
        }
    }

    /**
     * Endpoint para obtener ventas por rango de fechas
     */
    @GetMapping("/por-fechas")
    public ResponseEntity<ApiResponse<List<Venta>>> obtenerPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<Venta> ventas = ventaService.obtenerVentasPorFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(ApiResponse.success("Ventas en el período", ventas));
    }

    /**
     * Endpoint para obtener ventas por cliente
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<ApiResponse<List<Venta>>> obtenerPorCliente(@PathVariable Long clienteId) {
        try {
            List<Venta> ventas = ventaService.obtenerVentasPorCliente(clienteId);
            return ResponseEntity.ok(ApiResponse.success("Ventas del cliente", ventas));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para obtener venta con todos sus detalles
     */
    @GetMapping("/{id}/detalles")
    public ResponseEntity<ApiResponse<Venta>> obtenerConDetalles(@PathVariable Long id) {
        Optional<Venta> ventaOpt = ventaService.obtenerVentaConDetalles(id);

        if (ventaOpt.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("Venta con detalles", ventaOpt.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Venta no encontrada con ID: " + id));
        }
    }

    /**
     * Endpoint para calcular total de ventas en un período
     */
    @GetMapping("/total-periodo")
    public ResponseEntity<ApiResponse<BigDecimal>> calcularTotalPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        BigDecimal total = ventaService.calcularTotalVentasPorPeriodo(fechaInicio, fechaFin);
        return ResponseEntity.ok(ApiResponse.success("Total de ventas en el período", total));
    }

    /**
     * Endpoint para anular una venta
     */
    @PutMapping("/{id}/anular")
    public ResponseEntity<ApiResponse<Venta>> anularVenta(@PathVariable Long id) {
        try {
            Venta ventaAnulada = ventaService.anularVenta(id);
            return ResponseEntity.ok(ApiResponse.success("Venta anulada correctamente", ventaAnulada));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error al anular venta: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para obtener top productos vendidos
     */
    @GetMapping("/top-productos")
    public ResponseEntity<ApiResponse<List<Object[]>>> obtenerTopProductos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<Object[]> topProductos = ventaService.obtenerTopProductosVendidos(fechaInicio, fechaFin);
        return ResponseEntity.ok(ApiResponse.success("Top productos vendidos", topProductos));
    }

    /**
     * Endpoint para contar ventas de un cliente
     */
    @GetMapping("/cliente/{clienteId}/contar")
    public ResponseEntity<ApiResponse<Long>> contarVentasPorCliente(@PathVariable Long clienteId) {
        long cantidad = ventaService.contarVentasPorCliente(clienteId);
        return ResponseEntity.ok(ApiResponse.success("Cantidad de ventas del cliente", cantidad));
    }
}