package com.tundama.negociotundamabackend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore; // IMPORTAR ESTA

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "proveedores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_proveedor", unique = true, nullable = false, length = 50)
    private String idProveedor;

    @Column(name = "nombre_empresa", nullable = false, length = 100)
    private String nombreEmpresa;

    @Column(name = "tipo_identificacion", length = 20)
    private String tipoIdentificacion;

    @Column(name = "identificacion", unique = true, length = 50)
    private String identificacion;

    @Column(name = "direccion", length = 200)
    private String direccion;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "notas", length = 500)
    private String notas;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "activo")
    private Boolean activo = true;

    // ============= AGREGAR @JsonIgnore AQUÍ =============
    @JsonIgnore  // Esta anotación evita que se serialice la colección
    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Producto> productos = new ArrayList<>();
    // ====================================================

    @PrePersist
    public void prePersist() {
        fechaRegistro = LocalDateTime.now();
    }
}