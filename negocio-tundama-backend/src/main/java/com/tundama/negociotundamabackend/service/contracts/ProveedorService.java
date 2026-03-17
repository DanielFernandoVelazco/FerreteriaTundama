package com.tundama.negociotundamabackend.service.contracts;

import com.tundama.negociotundamabackend.model.entity.Proveedor;
import java.util.List;
import java.util.Optional;

public interface ProveedorService {

    /**
     * Registra un nuevo proveedor
     * @param proveedor Datos del proveedor
     * @return Proveedor registrado
     */
    Proveedor registrarProveedor(Proveedor proveedor);

    /**
     * Busca proveedor por ID
     * @param id ID del proveedor
     * @return Optional con el proveedor
     */
    Optional<Proveedor> buscarPorId(Long id);

    /**
     * Busca proveedor por ID de proveedor (formato NTP-...)
     * @param idProveedor ID de proveedor
     * @return Optional con el proveedor
     */
    Optional<Proveedor> buscarPorIdProveedor(String idProveedor);

    /**
     * Busca proveedor por identificación (NIT)
     * @param identificacion Número de identificación
     * @return Optional con el proveedor
     */
    Optional<Proveedor> buscarPorIdentificacion(String identificacion);

    /**
     * Obtiene todos los proveedores activos
     * @return Lista de proveedores
     */
    List<Proveedor> obtenerTodosActivos();

    /**
     * Busca proveedores por nombre de empresa
     * @param nombreEmpresa Nombre o parte del nombre
     * @return Lista de proveedores que coinciden
     */
    List<Proveedor> buscarPorNombreEmpresa(String nombreEmpresa);

    /**
     * Actualiza datos de un proveedor
     * @param id ID del proveedor
     * @param proveedor Datos actualizados
     * @return Proveedor actualizado
     */
    Proveedor actualizarProveedor(Long id, Proveedor proveedor);

    /**
     * Elimina lógicamente un proveedor
     * @param id ID del proveedor
     */
    void eliminarProveedor(Long id);

    /**
     * Obtiene proveedores que tienen productos activos
     * @return Lista de proveedores con productos
     */
    List<Proveedor> obtenerProveedoresConProductosActivos();

    /**
     * Verifica si existe una identificación
     * @param identificacion NIT a verificar
     * @return true si ya existe
     */
    boolean existeIdentificacion(String identificacion);
}