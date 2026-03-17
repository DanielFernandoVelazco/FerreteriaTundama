package com.tundama.negociotundamabackend.service.contracts;

import com.tundama.negociotundamabackend.model.dto.VentaDTO;
import com.tundama.negociotundamabackend.model.entity.Venta;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VentaService {

    /**
     * Registra una nueva venta con sus detalles
     * @param ventaDTO Datos de la venta y detalles
     * @return Venta registrada
     */
    Venta registrarVenta(VentaDTO ventaDTO);

    /**
     * Busca venta por ID
     * @param id ID de la venta
     * @return Optional con la venta
     */
    Optional<Venta> buscarPorId(Long id);

    /**
     * Busca venta por ID de venta (formato NTV-...)
     * @param idVenta ID de venta
     * @return Optional con la venta
     */
    Optional<Venta> buscarPorIdVenta(String idVenta);

    /**
     * Obtiene ventas por rango de fechas
     * @param fechaInicio Fecha inicio
     * @param fechaFin Fecha fin
     * @return Lista de ventas
     */
    List<Venta> obtenerVentasPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Obtiene ventas por cliente
     * @param clienteId ID del cliente
     * @return Lista de ventas del cliente
     */
    List<Venta> obtenerVentasPorCliente(Long clienteId);

    /**
     * Obtiene todas las ventas
     * @return Lista de todas las ventas
     */
    List<Venta> obtenerTodas();

    /**
     * Obtiene venta con todos sus detalles
     * @param id ID de la venta
     * @return Optional con la venta y sus detalles
     */
    Optional<Venta> obtenerVentaConDetalles(Long id);

    /**
     * Calcula el total de ventas en un período
     * @param fechaInicio Fecha inicio
     * @param fechaFin Fecha fin
     * @return Suma total de ventas
     */
    BigDecimal calcularTotalVentasPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Anula una venta (cambia estado a ANULADA)
     * @param id ID de la venta
     * @return Venta anulada
     */
    Venta anularVenta(Long id);

    /**
     * Obtiene los productos más vendidos en un período
     * @param fechaInicio Fecha inicio
     * @param fechaFin Fecha fin
     * @return Lista de objetos con [producto, cantidad]
     */
    List<Object[]> obtenerTopProductosVendidos(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Cuenta las ventas de un cliente
     * @param clienteId ID del cliente
     * @return Número de ventas
     */
    long contarVentasPorCliente(Long clienteId);
}