package com.tundama.negociotundamabackend.repository;

import com.tundama.negociotundamabackend.model.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Buscar producto por ID de producto
    Optional<Producto> findByIdProducto(String idProducto);

    // Buscar por nombre
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // Buscar productos activos
    List<Producto> findByActivoTrue();

    // Buscar productos con stock bajo (menor al mínimo)
    @Query("SELECT p FROM Producto p WHERE p.stockActual <= p.stockMinimo AND p.activo = true")
    List<Producto> findProductosConStockBajo();

    // Buscar productos por rango de precio
    List<Producto> findByPrecioUnitarioBetween(BigDecimal precioMin, BigDecimal precioMax);

    // Buscar productos por unidad de medida
    List<Producto> findByUnidadMedida(String unidadMedida);

    // Actualizar stock (para ventas/compras)
    @Modifying
    @Transactional
    @Query("UPDATE Producto p SET p.stockActual = p.stockActual + :cantidad WHERE p.id = :productoId")
    void incrementarStock(@Param("productoId") Long productoId, @Param("cantidad") Integer cantidad);

    @Modifying
    @Transactional
    @Query("UPDATE Producto p SET p.stockActual = p.stockActual - :cantidad WHERE p.id = :productoId AND p.stockActual >= :cantidad")
    int decrementarStock(@Param("productoId") Long productoId, @Param("cantidad") Integer cantidad);

    // Verificar stock suficiente
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Producto p WHERE p.id = :productoId AND p.stockActual >= :cantidad")
    boolean tieneStockSuficiente(@Param("productoId") Long productoId, @Param("cantidad") Integer cantidad);
}