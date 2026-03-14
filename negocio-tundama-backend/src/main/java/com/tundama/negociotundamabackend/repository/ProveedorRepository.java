package com.tundama.negociotundamabackend.repository;

import com.tundama.negociotundamabackend.model.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    // Buscar proveedor por ID de proveedor (formato NTP-...)
    Optional<Proveedor> findByIdProveedor(String idProveedor);

    // Buscar proveedor por identificación (NIT)
    Optional<Proveedor> findByIdentificacion(String identificacion);

    // Buscar proveedores activos
    List<Proveedor> findByActivoTrue();

    // Buscar por nombre de empresa
    List<Proveedor> findByNombreEmpresaContainingIgnoreCase(String nombreEmpresa);

    // Verificar si existe identificación
    boolean existsByIdentificacion(String identificacion);

    // Buscar proveedores por teléfono
    Optional<Proveedor> findByTelefono(String telefono);

    // ============= CONSULTA CORREGIDA =============
    // Query para buscar proveedores con productos activos
    @Query("SELECT DISTINCT p FROM Proveedor p JOIN p.productos pr WHERE pr.activo = true")
    List<Proveedor> findProveedoresConProductosActivos();
    // ===============================================
}