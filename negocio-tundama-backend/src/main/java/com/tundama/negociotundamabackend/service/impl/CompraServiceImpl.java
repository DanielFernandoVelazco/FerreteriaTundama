package com.tundama.negociotundamabackend.service.impl;

import com.tundama.negociotundamabackend.model.dto.CompraDTO;
import com.tundama.negociotundamabackend.model.dto.DetalleCompraDTO;
import com.tundama.negociotundamabackend.model.entity.*;
import com.tundama.negociotundamabackend.repository.*;
import com.tundama.negociotundamabackend.service.contracts.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompraServiceImpl implements CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private DetalleCompraRepository detalleCompraRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoServiceImpl productoService;

    @Override
    public Compra registrarCompra(CompraDTO compraDTO) {
        // Validar proveedor
        Proveedor proveedor = proveedorRepository.findById(compraDTO.getProveedorId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + compraDTO.getProveedorId()));

        // Crear nueva compra
        Compra compra = new Compra();
        compra.setIdCompra(generarIdCompra());
        compra.setIdComprador(compraDTO.getIdComprador());
        compra.setProveedor(proveedor);
        compra.setFechaCompra(LocalDateTime.now());
        compra.setEstado("COMPLETADA");

        // Calcular totales
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal ivaTotal = BigDecimal.ZERO;

        List<DetalleCompra> detalles = new ArrayList<>();

        for (DetalleCompraDTO detalleDTO : compraDTO.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + detalleDTO.getProductoId()));

            // Calcular valores del detalle
            BigDecimal precioUnitario = detalleDTO.getPrecioUnitario() != null ?
                    detalleDTO.getPrecioUnitario() : producto.getPrecioUnitario();

            BigDecimal ivaProducto = producto.getIva() != null ?
                    producto.getIva().divide(new BigDecimal(100)) : BigDecimal.ZERO;

            BigDecimal subtotalDetalle = precioUnitario.multiply(new BigDecimal(detalleDTO.getCantidad()));
            BigDecimal ivaDetalle = subtotalDetalle.multiply(ivaProducto);
            BigDecimal totalDetalle = subtotalDetalle.add(ivaDetalle);

            // Crear detalle
            DetalleCompra detalle = new DetalleCompra();
            detalle.setCompra(compra);
            detalle.setProducto(producto);
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setIva(producto.getIva());
            detalle.setSubtotal(subtotalDetalle);
            detalle.setTotal(totalDetalle);
            detalle.setCodigoProducto(producto.getIdProducto());
            detalle.setNombreProducto(producto.getNombre());
            detalle.setUnidadMedida(producto.getUnidadMedida());

            detalles.add(detalle);

            // Acumular totales
            subtotal = subtotal.add(subtotalDetalle);
            ivaTotal = ivaTotal.add(ivaDetalle);

            // Incrementar stock (compra aumenta inventario)
            productoService.incrementarStock(producto.getId(), detalleDTO.getCantidad());

            // Actualizar precio si es diferente
            if (detalleDTO.getPrecioUnitario() != null &&
                    !detalleDTO.getPrecioUnitario().equals(producto.getPrecioUnitario())) {
                producto.setPrecioUnitario(detalleDTO.getPrecioUnitario());
                productoRepository.save(producto);
            }
        }

        // Asignar totales a la compra
        compra.setSubtotal(subtotal);
        compra.setIvaTotal(ivaTotal);
        compra.setTotal(subtotal.add(ivaTotal));
        compra.setDetalles(detalles);

        // Guardar compra (en cascada guarda los detalles)
        return compraRepository.save(compra);
    }

    @Override
    public Optional<Compra> buscarPorId(Long id) {
        return compraRepository.findById(id);
    }

    @Override
    public Optional<Compra> buscarPorIdCompra(String idCompra) {
        return compraRepository.findByIdCompra(idCompra);
    }

    @Override
    public List<Compra> obtenerComprasPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return compraRepository.findByFechaCompraBetween(fechaInicio, fechaFin);
    }

    @Override
    public List<Compra> obtenerComprasPorProveedor(Long proveedorId) {
        Proveedor proveedor = proveedorRepository.findById(proveedorId)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + proveedorId));
        return compraRepository.findByProveedor(proveedor);
    }

    @Override
    public List<Compra> obtenerTodas() {
        return compraRepository.findAll();
    }

    @Override
    public Optional<Compra> obtenerCompraConDetalles(Long id) {
        return compraRepository.findCompraWithDetalles(id);
    }

    @Override
    public BigDecimal calcularTotalComprasPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return compraRepository.sumTotalComprasPorPeriodo(fechaInicio, fechaFin);
    }

    @Override
    public Compra anularCompra(Long id) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada con ID: " + id));

        if (!"COMPLETADA".equals(compra.getEstado())) {
            throw new RuntimeException("La compra ya está anulada o no se puede anular");
        }

        // Restaurar stock (decrementar lo que se había incrementado)
        for (DetalleCompra detalle : compra.getDetalles()) {
            productoService.decrementarStock(detalle.getProducto().getId(), detalle.getCantidad());
        }

        compra.setEstado("ANULADA");
        return compraRepository.save(compra);
    }

    @Override
    public List<Compra> buscarPorIdComprador(String idComprador) {
        return compraRepository.findByIdComprador(idComprador);
    }

    /**
     * Genera ID de compra: NTCO-YYYY-MMM-XXXX
     */
    private String generarIdCompra() {
        LocalDateTime ahora = LocalDateTime.now();
        String año = String.valueOf(ahora.getYear());
        String mes = ahora.format(DateTimeFormatter.ofPattern("MMM")).toUpperCase();
        String numero = String.format("%04d", (int)(Math.random() * 10000));

        return "NTCO-" + año + "-" + mes + "-" + numero;
    }
}