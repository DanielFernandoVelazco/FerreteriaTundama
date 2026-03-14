package com.tundama.negociotundamabackend.repository;

import com.tundama.negociotundamabackend.model.entity.Compra;
import com.tundama.negociotundamabackend.model.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    // Buscar compra por ID de compra (formato NTCO-...)
    Optional<Compra> findByIdCompra(String idCompra);

    // Buscar compras por proveedor
    List<Compra> findByProveedor(Proveedor proveedor);

    // Buscar compras por rango de fechas
    List<Compra> findByFechaCompraBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // Buscar compras por estado
    List<Compra> findByEstado(String estado);

    // Calcular total de compras en un período
    @Query("SELECT COALESCE(SUM(c.total), 0) FROM Compra c WHERE c.fechaCompra BETWEEN :fechaInicio AND :fechaFin AND c.estado = 'COMPLETADA'")
    BigDecimal sumTotalComprasPorPeriodo(@Param("fechaInicio") LocalDateTime fechaInicio,
                                         @Param("fechaFin") LocalDateTime fechaFin);

    // Obtener compra con detalles
    @Query("SELECT c FROM Compra c LEFT JOIN FETCH c.detalles WHERE c.id = :compraId")
    Optional<Compra> findCompraWithDetalles(@Param("compraId") Long compraId);

    // Buscar por ID de comprador (empleado)
    List<Compra> findByIdComprador(String idComprador);
}