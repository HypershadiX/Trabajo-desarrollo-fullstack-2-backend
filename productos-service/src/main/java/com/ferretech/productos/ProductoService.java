package com.ferretech.productos;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoService {

    private static final String IMAGEN_POR_DEFECTO = "img/descarga.png";
    private static final String DETALLES_POR_DEFECTO = "Sin descripción.";

    private final ProductoRepository repositorio;

    public ProductoService(ProductoRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional(readOnly = true)
    public List<Producto> listar() {
        return repositorio.findAll().stream()
                .sorted((a, b) -> Long.compare(a.getId(), b.getId()))
                .toList();
    }

    @Transactional(readOnly = true)
    public Producto obtener(Long id) {
        return repositorio.findById(id).orElseThrow(() -> noEncontrado());
    }

    @Transactional
    public Producto crear(Dtos.CrearProductoRequest datos) {
        return repositorio.save(new Producto(
                datos.nombre().trim(),
                datos.precio(),
                datos.stock(),
                valorOPorDefecto(datos.imagen(), IMAGEN_POR_DEFECTO),
                valorOPorDefecto(datos.detalles(), DETALLES_POR_DEFECTO)));
    }

    @Transactional
    public Producto actualizar(Long id, Dtos.ActualizarProductoRequest datos) {
        Producto producto = repositorio.findById(id).orElseThrow(() -> noEncontrado());

        if (datos.nombre() != null && !datos.nombre().isBlank()) {
            producto.setNombre(datos.nombre().trim());
        }
        if (datos.precio() != null) {
            producto.setPrecio(datos.precio());
        }
        if (datos.stock() != null) {
            producto.setStock(datos.stock());
        }
        if (datos.imagen() != null) {
            producto.setImagen(valorOPorDefecto(datos.imagen(), IMAGEN_POR_DEFECTO));
        }
        if (datos.detalles() != null) {
            producto.setDetalles(valorOPorDefecto(datos.detalles(), DETALLES_POR_DEFECTO));
        }
        return repositorio.save(producto);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw noEncontrado();
        }
        repositorio.deleteById(id);
    }

    /** delta negativo = reservar (agregar al carrito); positivo = devolver stock. */
    @Transactional
    public Producto ajustarStock(Long id, int delta) {
        Producto producto = repositorio.buscarParaActualizar(id).orElseThrow(() -> noEncontrado());
        int nuevoStock = producto.getStock() + delta;

        if (nuevoStock < 0) {
            throw new ApiException(HttpStatus.CONFLICT,
                    "\"" + producto.getNombre() + "\" no tiene stock disponible en este momento.");
        }
        producto.setStock(nuevoStock);
        return repositorio.save(producto);
    }

    private static ApiException noEncontrado() {
        return new ApiException(HttpStatus.NOT_FOUND, "Producto no encontrado.");
    }

    private static String valorOPorDefecto(String valor, String porDefecto) {
        return valor == null || valor.isBlank() ? porDefecto : valor.trim();
    }
}
