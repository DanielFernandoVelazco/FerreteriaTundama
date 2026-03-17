package com.tundama.negociotundamabackend.service.impl;

import com.tundama.negociotundamabackend.model.entity.Producto;
import com.tundama.negociotundamabackend.model.entity.Proveedor;
import com.tundama.negociotundamabackend.repository.ProductoRepository;
import com.tundama.negociotundamabackend.repository.ProveedorRepository;
import com.tundama.negociotundamabackend.service.contracts.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    public Producto registrarProducto(Producto producto) {
        // Generar ID de producto si no tiene
        if (producto.getIdProducto() == null || producto.getIdProducto().isEmpty()) {
            producto.setIdProducto(generarIdProducto());
        }

        producto.setActivo(true);
        return productoRepository.save(producto);
    }

    @Override
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public Optional<Producto> buscarPorIdProducto(String idProducto) {
        return productoRepository.findByIdProducto(idProducto);
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public List<Producto> obtenerTodosActivos() {
        return productoRepository.findByActivoTrue();
    }

    @Override
    public List<Producto> obtenerProductosConStockBajo() {
        return productoRepository.findProductosConStockBajo();
    }

    @Override
    public List<Producto> buscarPorProveedor(Proveedor proveedor) {
        // Método a implementar si se necesita
        return List.of();
    }

    @Override
    public List<Producto> buscarPorRangoPrecio(BigDecimal precioMin, BigDecimal precioMax) {
        return productoRepository.findByPrecioUnitarioBetween(precioMin, precioMax);
    }

    @Override
    public Producto actualizarProducto(Long id, Producto producto) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        // Actualizar campos
        productoExistente.setNombre(producto.getNombre());
        productoExistente.setDescripcion(producto.getDescripcion());
        productoExistente.setUnidadMedida(producto.getUnidadMedida());
        productoExistente.setPrecioUnitario(producto.getPrecioUnitario());
        productoExistente.setIva(producto.getIva());
        productoExistente.setStockMinimo(producto.getStockMinimo());

        return productoRepository.save(productoExistente);
    }

    @Override
    public void eliminarProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    @Override
    public void incrementarStock(Long productoId, Integer cantidad) {
        productoRepository.incrementarStock(productoId, cantidad);
    }

    @Override
    public boolean decrementarStock(Long productoId, Integer cantidad) {
        int rowsAffected = productoRepository.decrementarStock(productoId, cantidad);
        return rowsAffected > 0;
    }

    @Override
    public boolean tieneStockSuficiente(Long productoId, Integer cantidad) {
        return productoRepository.tieneStockSuficiente(productoId, cantidad);
    }

    @Override
    public Producto asignarProveedor(Long productoId, Long proveedorId) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId));

        Proveedor proveedor = proveedorRepository.findById(proveedorId)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + proveedorId));

        producto.setProveedor(proveedor);
        return productoRepository.save(producto);
    }

    /**
     * Genera ID de producto: NTPRO-YYYY-MMM-XXXX
     */
    private String generarIdProducto() {
        LocalDateTime ahora = LocalDateTime.now();
        String año = String.valueOf(ahora.getYear());
        String mes = ahora.format(DateTimeFormatter.ofPattern("MMM")).toUpperCase();
        String numero = String.format("%04d", (int)(Math.random() * 10000));

        return "NTPRO-" + año + "-" + mes + "-" + numero;
    }
}