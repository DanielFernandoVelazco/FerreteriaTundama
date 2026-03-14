package com.tundama.negociotundamabackend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_cliente", unique = true, nullable = false, length = 50)
    private String idCliente;  // Formato: NTC-2023-ABR-0052

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "tipo_identificacion", length = 20)
    private String tipoIdentificacion;  // NIT o CEDULA

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

    @PrePersist
    public void prePersist() {
        fechaRegistro = LocalDateTime.now();
    }
}