package com.tundama.negociotundamabackend.service.contracts;

import com.tundama.negociotundamabackend.model.entity.Cliente;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ClienteService {

    /**
     * Registra un nuevo cliente
     * @param cliente Datos del cliente
     * @return Cliente registrado
     */
    Cliente registrarCliente(Cliente cliente);

    /**
     * Busca cliente por ID
     * @param id ID del cliente
     * @return Optional con el cliente
     */
    Optional<Cliente> buscarPorId(Long id);

    /**
     * Busca cliente por ID de cliente (formato NTC-...)
     * @param idCliente ID de cliente
     * @return Optional con el cliente
     */
    Optional<Cliente> buscarPorIdCliente(String idCliente);

    /**
     * Busca cliente por identificación (NIT o Cédula)
     * @param identificacion Número de identificación
     * @return Optional con el cliente
     */
    Optional<Cliente> buscarPorIdentificacion(String identificacion);

    /**
     * Obtiene todos los clientes activos
     * @return Lista de clientes
     */
    List<Cliente> obtenerTodosActivos();

    /**
     * Busca clientes por nombre (búsqueda parcial)
     * @param nombre Parte del nombre a buscar
     * @return Lista de clientes que coinciden
     */
    List<Cliente> buscarPorNombre(String nombre);

    /**
     * Actualiza datos de un cliente
     * @param id ID del cliente
     * @param cliente Datos actualizados
     * @return Cliente actualizado
     */
    Cliente actualizarCliente(Long id, Cliente cliente);

    /**
     * Elimina lógicamente un cliente
     * @param id ID del cliente
     */
    void eliminarCliente(Long id);

    /**
     * Cuenta clientes registrados en un período
     * @param fechaInicio Fecha inicio
     * @param fechaFin Fecha fin
     * @return Número de clientes
     */
    long contarClientesPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Verifica si existe una identificación
     * @param identificacion Número a verificar
     * @return true si ya existe
     */
    boolean existeIdentificacion(String identificacion);
}