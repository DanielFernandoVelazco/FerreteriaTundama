package com.tundama.negociotundamabackend.repository;

import com.tundama.negociotundamabackend.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Buscar cliente por ID de cliente (formato NTC-...)
    Optional<Cliente> findByIdCliente(String idCliente);

    // Buscar cliente por identificación (NIT o Cédula)
    Optional<Cliente> findByIdentificacion(String identificacion);

    // Buscar clientes activos
    List<Cliente> findByActivoTrue();

    // Buscar por nombre (búsqueda parcial)
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);

    // Verificar si existe identificación
    boolean existsByIdentificacion(String identificacion);

    // Buscar clientes con deudas o condición especial (ejemplo)
    @Query("SELECT c FROM Cliente c WHERE LOWER(c.notas) LIKE LOWER(CONCAT('%', :palabra, '%'))")
    List<Cliente> buscarEnNotas(@Param("palabra") String palabra);

    // Contar clientes registrados en un período
    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.fechaRegistro BETWEEN :fechaInicio AND :fechaFin")
    long contarClientesPorPeriodo(@Param("fechaInicio") java.time.LocalDateTime fechaInicio,
                                  @Param("fechaFin") java.time.LocalDateTime fechaFin);
}