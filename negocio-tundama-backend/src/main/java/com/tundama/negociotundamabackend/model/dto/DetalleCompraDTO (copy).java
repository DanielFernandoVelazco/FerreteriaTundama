package com.tundama.negociotundamabackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCompraDTO {
    private Long productoId;
    private Integer cantidad;
    private BigDecimal precioUnitario; // Opcional, si es diferente al actual
}