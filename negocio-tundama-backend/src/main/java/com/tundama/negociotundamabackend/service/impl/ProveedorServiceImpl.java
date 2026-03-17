package com.tundama.negociotundamabackend.service.impl;

import com.tundama.negociotundamabackend.model.entity.Proveedor;
import com.tundama.negociotundamabackend.repository.ProveedorRepository;
import com.tundama.negociotundamabackend.service.contracts.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProveedorServiceImpl implements ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    public Proveedor registrarProveedor(Proveedor proveedor) {
        // Generar ID de proveedor si no tiene
        if (proveedor.getIdProveedor() == null || proveedor.getIdProveedor().isEmpty()) {
            proveedor.setIdProveedor(generarIdProveedor());
        }

        // Validar que la identificación no exista
        if (proveedor.getIdentificacion() != null &&
                !proveedor.getIdentificacion().isEmpty() &&
                proveedorRepository.existsByIdentificacion(proveedor.getIdentificacion())) {
            throw new RuntimeException("Ya existe un proveedor con identificación: " + proveedor.getIdentificacion());
        }

        proveedor.setActivo(true);
        return proveedorRepository.save(proveedor);
    }

    @Override
    public Optional<Proveedor> buscarPorId(Long id) {
        return proveedorRepository.findById(id);
    }

    @Override
    public Optional<Proveedor> buscarPorIdProveedor(String idProveedor) {
        return proveedorRepository.findByIdProveedor(idProveedor);
    }

    @Override
    public Optional<Proveedor> buscarPorIdentificacion(String identificacion) {
        return proveedorRepository.findByIdentificacion(identificacion);
    }

    @Override
    public List<Proveedor> obtenerTodosActivos() {
        return proveedorRepository.findByActivoTrue();
    }

    @Override
    public List<Proveedor> buscarPorNombreEmpresa(String nombreEmpresa) {
        return proveedorRepository.findByNombreEmpresaContainingIgnoreCase(nombreEmpresa);
    }

    @Override
    public Proveedor actualizarProveedor(Long id, Proveedor proveedor) {
        Proveedor proveedorExistente = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + id));

        // Validar que la nueva identificación no esté en uso por otro proveedor
        if (proveedor.getIdentificacion() != null &&
                !proveedor.getIdentificacion().isEmpty() &&
                !proveedor.getIdentificacion().equals(proveedorExistente.getIdentificacion()) &&
                proveedorRepository.existsByIdentificacion(proveedor.getIdentificacion())) {
            throw new RuntimeException("Ya existe otro proveedor con identificación: " + proveedor.getIdentificacion());
        }

        // Actualizar campos
        proveedorExistente.setNombreEmpresa(proveedor.getNombreEmpresa());
        proveedorExistente.setTipoIdentificacion(proveedor.getTipoIdentificacion());
        proveedorExistente.setIdentificacion(proveedor.getIdentificacion());
        proveedorExistente.setDireccion(proveedor.getDireccion());
        proveedorExistente.setTelefono(proveedor.getTelefono());
        proveedorExistente.setNotas(proveedor.getNotas());

        return proveedorRepository.save(proveedorExistente);
    }

    @Override
    public void eliminarProveedor(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + id));
        proveedor.setActivo(false);
        proveedorRepository.save(proveedor);
    }

    @Override
    public List<Proveedor> obtenerProveedoresConProductosActivos() {
        return proveedorRepository.findProveedoresConProductosActivos();
    }

    @Override
    public boolean existeIdentificacion(String identificacion) {
        return proveedorRepository.existsByIdentificacion(identificacion);
    }

    /**
     * Genera ID de proveedor: NTP-YYYY-MMM-XXXX
     */
    private String generarIdProveedor() {
        LocalDateTime ahora = LocalDateTime.now();
        String año = String.valueOf(ahora.getYear());
        String mes = ahora.format(DateTimeFormatter.ofPattern("MMM")).toUpperCase();
        String numero = String.format("%04d", (int)(Math.random() * 10000));

        return "NTP-" + año + "-" + mes + "-" + numero;
    }
}