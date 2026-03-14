package com.tundama.negociotundamabackend.repository;

import com.tundama.negociotundamabackend.model.entity.Cliente;
import com.tundama.negociotundamabackend.model.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    // Buscar venta por ID de venta (formato NTV-...)
    Optional<Venta> findByIdVenta(String idVenta);

    // Buscar ventas por cliente
    List<Venta> findByCliente(Cliente cliente);

    // Buscar ventas por rango de fechas
    List<Venta> findByFechaVentaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // Buscar ventas por estado
    List<Venta> findByEstado(String estado);

    // Calcular total de ventas en un período
    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE v.fechaVenta BETWEEN :fechaInicio AND :fechaFin AND v.estado = 'COMPLETADA'")
    BigDecimal sumTotalVentasPorPeriodo(@Param("fechaInicio") LocalDateTime fechaInicio,
                                        @Param("fechaFin") LocalDateTime fechaFin);

    // Obtener ventas con detalles (para reports)
    @Query("SELECT v FROM Venta v LEFT JOIN FETCH v.detalles WHERE v.id = :ventaId")
    Optional<Venta> findVentaWithDetalles(@Param("ventaId") Long ventaId);

    // Contar ventas por cliente
    @Query("SELECT COUNT(v) FROM Venta v WHERE v.cliente.id = :clienteId")
    long countVentasByCliente(@Param("clienteId") Long clienteId);

    // Top 5 productos más vendidos (requiere join con detalles)
    @Query("SELECT d.producto.nombre, SUM(d.cantidad) as totalVendido " +
            "FROM DetalleVenta d " +
            "WHERE d.venta.fechaVenta BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY d.producto.id, d.producto.nombre " +
            "ORDER BY totalVendido DESC")
    List<Object[]> findTopProductosVendidos(@Param("fechaInicio") LocalDateTime fechaInicio,
                                            @Param("fechaFin") LocalDateTime fechaFin);
}