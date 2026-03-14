package com.tundama.negociotundamabackend.repository;

import com.tundama.negociotundamabackend.model.entity.Compra;
import com.tundama.negociotundamabackend.model.entity.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Long> {

    // Buscar detalles por compra
    List<DetalleCompra> findByCompra(Compra compra);

    // Buscar detalles por producto
    List<DetalleCompra> findByProductoId(Long productoId);

    // Eliminar detalles de una compra
    void deleteByCompra(Compra compra);
}