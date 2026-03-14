package com.tundama.negociotundamabackend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_venta", unique = true, nullable = false, length = 50)
    private String idVenta;  // Formato: NTV-2023-ABR-0074

    @Column(name = "fecha_venta")
    private LocalDateTime fechaVenta;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(name = "subtotal", precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "iva_total", precision = 10, scale = 2)
    private BigDecimal ivaTotal;

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "estado", length = 20)
    private String estado = "COMPLETADA";  // COMPLETADA, ANULADA

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        fechaVenta = LocalDateTime.now();
    }
}