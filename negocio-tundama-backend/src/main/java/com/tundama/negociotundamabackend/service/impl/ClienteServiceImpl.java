package com.tundama.negociotundamabackend.service.impl;

import com.tundama.negociotundamabackend.model.entity.Cliente;
import com.tundama.negociotundamabackend.repository.ClienteRepository;
import com.tundama.negociotundamabackend.service.contracts.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Cliente registrarCliente(Cliente cliente) {
        // Generar ID de cliente si no tiene
        if (cliente.getIdCliente() == null || cliente.getIdCliente().isEmpty()) {
            cliente.setIdCliente(generarIdCliente());
        }

        // Validar que la identificación no exista
        if (cliente.getIdentificacion() != null &&
                !cliente.getIdentificacion().isEmpty() &&
                clienteRepository.existsByIdentificacion(cliente.getIdentificacion())) {
            throw new RuntimeException("Ya existe un cliente con identificación: " + cliente.getIdentificacion());
        }

        cliente.setActivo(true);
        return clienteRepository.save(cliente);
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    @Override
    public Optional<Cliente> buscarPorIdCliente(String idCliente) {
        return clienteRepository.findByIdCliente(idCliente);
    }

    @Override
    public Optional<Cliente> buscarPorIdentificacion(String identificacion) {
        return clienteRepository.findByIdentificacion(identificacion);
    }

    @Override
    public List<Cliente> obtenerTodosActivos() {
        return clienteRepository.findByActivoTrue();
    }

    @Override
    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public Cliente actualizarCliente(Long id, Cliente cliente) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));

        // Validar que la nueva identificación no esté en uso por otro cliente
        if (cliente.getIdentificacion() != null &&
                !cliente.getIdentificacion().isEmpty() &&
                !cliente.getIdentificacion().equals(clienteExistente.getIdentificacion()) &&
                clienteRepository.existsByIdentificacion(cliente.getIdentificacion())) {
            throw new RuntimeException("Ya existe otro cliente con identificación: " + cliente.getIdentificacion());
        }

        // Actualizar campos
        clienteExistente.setNombre(cliente.getNombre());
        clienteExistente.setTipoIdentificacion(cliente.getTipoIdentificacion());
        clienteExistente.setIdentificacion(cliente.getIdentificacion());
        clienteExistente.setDireccion(cliente.getDireccion());
        clienteExistente.setTelefono(cliente.getTelefono());
        clienteExistente.setNotas(cliente.getNotas());

        return clienteRepository.save(clienteExistente);
    }

    @Override
    public void eliminarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }

    @Override
    public long contarClientesPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return clienteRepository.contarClientesPorPeriodo(fechaInicio, fechaFin);
    }

    @Override
    public boolean existeIdentificacion(String identificacion) {
        return clienteRepository.existsByIdentificacion(identificacion);
    }

    /**
     * Genera ID de cliente: NTC-YYYY-MMM-XXXX
     */
    private String generarIdCliente() {
        LocalDateTime ahora = LocalDateTime.now();
        String año = String.valueOf(ahora.getYear());
        String mes = ahora.format(DateTimeFormatter.ofPattern("MMM")).toUpperCase();
        String numero = String.format("%04d", (int)(Math.random() * 10000));

        return "NTC-" + año + "-" + mes + "-" + numero;
    }
}