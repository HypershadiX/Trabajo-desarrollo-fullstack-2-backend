package com.ferretech.productos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Catálogo inicial: los mismos 3 productos que tenía el frontend (solo si la tabla está vacía). */
@Component
public class DatosIniciales implements CommandLineRunner {

    private final ProductoRepository repositorio;

    public DatosIniciales(ProductoRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void run(String... args) {
        if (repositorio.count() > 0) {
            return;
        }
        repositorio.save(new Producto("Brocha", 5990, 12, "img/images.jpg",
                "Brocha de cerdas sintéticas de 2 pulgadas. Ideal para pintura al agua y esmalte, mango de madera ergonómico y virola resistente a la corrosión."));
        repositorio.save(new Producto("Tarro de pintura", 45990, 5, "img/images (1).jpg",
                "Pintura látex interior/exterior, tarro de 1 galón. Alto poder cubritivo, secado al tacto en 4 horas y bajo olor."));
        repositorio.save(new Producto("Bolsa de clavos", 6990, 30, "img/images (2).jpg",
                "Bolsa de 1 kg de clavos de acero galvanizado de 2 pulgadas, resistentes a la corrosión, ideales para estructuras de madera."));
    }
}
