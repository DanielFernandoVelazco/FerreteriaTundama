package com.tundama.negociotundamabackend.repository;

import com.tundama.negociotundamabackend.model.entity.DetalleVenta;
import com.tundama.negociotundamabackend.model.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

    // Buscar detalles por venta
    List<DetalleVenta> findByVenta(Venta venta);

    // Buscar detalles por producto
    List<DetalleVenta> findByProductoId(Long productoId);

    // Eliminar detalles de una venta
    void deleteByVenta(Venta venta);
}