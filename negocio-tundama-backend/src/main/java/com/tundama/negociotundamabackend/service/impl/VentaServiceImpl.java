package com.tundama.negociotundamabackend.service.impl;

import com.tundama.negociotundamabackend.model.dto.VentaDTO;
import com.tundama.negociotundamabackend.model.dto.DetalleVentaDTO;
import com.tundama.negociotundamabackend.model.entity.*;
import com.tundama.negociotundamabackend.repository.*;
import com.tundama.negociotundamabackend.service.contracts.VentaService;
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
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoServiceImpl productoService;

    @Override
    public Venta registrarVenta(VentaDTO ventaDTO) {
        // Validar cliente
        Cliente cliente = clienteRepository.findById(ventaDTO.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + ventaDTO.getClienteId()));

        // Crear nueva venta
        Venta venta = new Venta();
        venta.setIdVenta(generarIdVenta());
        venta.setCliente(cliente);
        venta.setFechaVenta(LocalDateTime.now());
        venta.setEstado("COMPLETADA");

        // Calcular totales
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal ivaTotal = BigDecimal.ZERO;

        List<DetalleVenta> detalles = new ArrayList<>();

        for (DetalleVentaDTO detalleDTO : ventaDTO.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + detalleDTO.getProductoId()));

            // Validar stock
            if (!productoService.tieneStockSuficiente(producto.getId(), detalleDTO.getCantidad())) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            // Calcular valores del detalle
            BigDecimal precioUnitario = producto.getPrecioUnitario();
            BigDecimal ivaProducto = producto.getIva() != null ?
                    producto.getIva().divide(new BigDecimal(100)) : BigDecimal.ZERO;

            BigDecimal subtotalDetalle = precioUnitario.multiply(new BigDecimal(detalleDTO.getCantidad()));
            BigDecimal ivaDetalle = subtotalDetalle.multiply(ivaProducto);
            BigDecimal totalDetalle = subtotalDetalle.add(ivaDetalle);

            // Crear detalle
            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
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

            // Decrementar stock
            productoService.decrementarStock(producto.getId(), detalleDTO.getCantidad());
        }

        // Asignar totales a la venta
        venta.setSubtotal(subtotal);
        venta.setIvaTotal(ivaTotal);
        venta.setTotal(subtotal.add(ivaTotal));
        venta.setDetalles(detalles);

        // Guardar venta (en cascada guarda los detalles)
        return ventaRepository.save(venta);
    }

    @Override
    public Optional<Venta> buscarPorId(Long id) {
        return ventaRepository.findById(id);
    }

    @Override
    public Optional<Venta> buscarPorIdVenta(String idVenta) {
        return ventaRepository.findByIdVenta(idVenta);
    }

    @Override
    public List<Venta> obtenerVentasPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ventaRepository.findByFechaVentaBetween(fechaInicio, fechaFin);
    }

    @Override
    public List<Venta> obtenerVentasPorCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));
        return ventaRepository.findByCliente(cliente);
    }

    @Override
    public List<Venta> obtenerTodas() {
        return ventaRepository.findAll();
    }

    @Override
    public Optional<Venta> obtenerVentaConDetalles(Long id) {
        return ventaRepository.findVentaWithDetalles(id);
    }

    @Override
    public BigDecimal calcularTotalVentasPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ventaRepository.sumTotalVentasPorPeriodo(fechaInicio, fechaFin);
    }

    @Override
    public Venta anularVenta(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));

        if (!"COMPLETADA".equals(venta.getEstado())) {
            throw new RuntimeException("La venta ya está anulada o no se puede anular");
        }

        // Restaurar stock de los productos
        for (DetalleVenta detalle : venta.getDetalles()) {
            productoService.incrementarStock(detalle.getProducto().getId(), detalle.getCantidad());
        }

        venta.setEstado("ANULADA");
        return ventaRepository.save(venta);
    }

    @Override
    public List<Object[]> obtenerTopProductosVendidos(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ventaRepository.findTopProductosVendidos(fechaInicio, fechaFin);
    }

    @Override
    public long contarVentasPorCliente(Long clienteId) {
        return ventaRepository.countVentasByCliente(clienteId);
    }

    /**
     * Genera ID de venta: NTV-YYYY-MMM-XXXX
     */
    private String generarIdVenta() {
        LocalDateTime ahora = LocalDateTime.now();
        String año = String.valueOf(ahora.getYear());
        String mes = ahora.format(DateTimeFormatter.ofPattern("MMM")).toUpperCase();
        String numero = String.format("%04d", (int)(Math.random() * 10000));

        return "NTV-" + año + "-" + mes + "-" + numero;
    }
}