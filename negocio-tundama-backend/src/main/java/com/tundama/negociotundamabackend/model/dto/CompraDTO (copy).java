package com.tundama.negociotundamabackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompraDTO {
    private String idComprador; // Empleado que realiza la compra
    private Long proveedorId;
    private List<DetalleCompraDTO> detalles;
}