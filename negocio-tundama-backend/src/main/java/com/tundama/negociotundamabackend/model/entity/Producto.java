package com.tundama.negociotundamabackend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore; // IMPORTAR ESTA

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_producto", unique = true, nullable = false, length = 50)
    private String idProducto;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "unidad_medida", length = 20)
    private String unidadMedida;

    @Column(name = "precio_unitario", precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "iva", precision = 5, scale = 2)
    private BigDecimal iva;

    @Column(name = "stock_actual")
    private Integer stockActual = 0;

    @Column(name = "stock_minimo")
    private Integer stockMinimo = 0;

    // ============= AGREGAR @JsonIgnore AQUÍ (opcional) =============
    @JsonIgnore  // Si no quieres que aparezca el proveedor en la respuesta JSON de productos
    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;
    // ===============================================================

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "activo")
    private Boolean activo = true;

    @PrePersist
    public void prePersist() {
        fechaRegistro = LocalDateTime.now();
    }
}