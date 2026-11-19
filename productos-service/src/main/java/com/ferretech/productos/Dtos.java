package com.ferretech.productos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public final class Dtos {

    private Dtos() {
    }

    public record CrearProductoRequest(
            @NotBlank(message = "El nombre es obligatorio.") String nombre,
            @NotNull(message = "El precio es obligatorio.") @Min(value = 0, message = "El precio no puede ser negativo.") Integer precio,
            @NotNull(message = "El stock es obligatorio.") @Min(value = 0, message = "El stock no puede ser negativo.") Integer stock,
            String imagen,
            String detalles) {
    }

    /** Todos los campos son opcionales: solo se cambia lo que venga informado. */
    public record ActualizarProductoRequest(
            String nombre,
            @Min(value = 0, message = "El precio no puede ser negativo.") Integer precio,
            @Min(value = 0, message = "El stock no puede ser negativo.") Integer stock,
            String imagen,
            String detalles) {
    }

    public record AjusteStockRequest(
            @NotNull(message = "Indica la variación de stock.") Integer delta) {
    }
}
