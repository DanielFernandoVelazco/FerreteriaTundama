package com.tundama.negociotundamabackend.service.contracts;

import com.tundama.negociotundamabackend.model.dto.CompraDTO;
import com.tundama.negociotundamabackend.model.entity.Compra;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CompraService {

    /**
     * Registra una nueva compra con sus detalles
     * @param compraDTO Datos de la compra y detalles
     * @return Compra registrada
     */
    Compra registrarCompra(CompraDTO compraDTO);

    /**
     * Busca compra por ID
     * @param id ID de la compra
     * @return Optional con la compra
     */
    Optional<Compra> buscarPorId(Long id);

    /**
     * Busca compra por ID de compra (formato NTCO-...)
     * @param idCompra ID de compra
     * @return Optional con la compra
     */
    Optional<Compra> buscarPorIdCompra(String idCompra);

    /**
     * Obtiene compras por rango de fechas
     * @param fechaInicio Fecha inicio
     * @param fechaFin Fecha fin
     * @return Lista de compras
     */
    List<Compra> obtenerComprasPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Obtiene compras por proveedor
     * @param proveedorId ID del proveedor
     * @return Lista de compras del proveedor
     */
    List<Compra> obtenerComprasPorProveedor(Long proveedorId);

    /**
     * Obtiene todas las compras
     * @return Lista de todas las compras
     */
    List<Compra> obtenerTodas();

    /**
     * Obtiene compra con todos sus detalles
     * @param id ID de la compra
     * @return Optional con la compra y sus detalles
     */
    Optional<Compra> obtenerCompraConDetalles(Long id);

    /**
     * Calcula el total de compras en un período
     * @param fechaInicio Fecha inicio
     * @param fechaFin Fecha fin
     * @return Suma total de compras
     */
    BigDecimal calcularTotalComprasPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Anula una compra (cambia estado a ANULADA)
     * @param id ID de la compra
     * @return Compra anulada
     */
    Compra anularCompra(Long id);

    /**
     * Busca compras por ID de comprador (empleado)
     * @param idComprador ID del comprador
     * @return Lista de compras
     */
    List<Compra> buscarPorIdComprador(String idComprador);
}