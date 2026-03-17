package com.tundama.negociotundamabackend.service.contracts;

import com.tundama.negociotundamabackend.model.entity.Producto;
import com.tundama.negociotundamabackend.model.entity.Proveedor;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductoService {

    /**
     * Registra un nuevo producto
     * @param producto Datos del producto
     * @return Producto registrado
     */
    Producto registrarProducto(Producto producto);

    /**
     * Busca producto por ID
     * @param id ID del producto
     * @return Optional con el producto
     */
    Optional<Producto> buscarPorId(Long id);

    /**
     * Busca producto por ID de producto
     * @param idProducto ID de producto
     * @return Optional con el producto
     */
    Optional<Producto> buscarPorIdProducto(String idProducto);

    /**
     * Busca productos por nombre
     * @param nombre Nombre o parte del nombre
     * @return Lista de productos
     */
    List<Producto> buscarPorNombre(String nombre);

    /**
     * Obtiene todos los productos activos
     * @return Lista de productos
     */
    List<Producto> obtenerTodosActivos();

    /**
     * Obtiene productos con stock bajo
     * @return Lista de productos con stock <= stock mínimo
     */
    List<Producto> obtenerProductosConStockBajo();

    /**
     * Busca productos por proveedor
     * @param proveedor Proveedor
     * @return Lista de productos del proveedor
     */
    List<Producto> buscarPorProveedor(Proveedor proveedor);

    /**
     * Busca productos por rango de precio
     * @param precioMin Precio mínimo
     * @param precioMax Precio máximo
     * @return Lista de productos
     */
    List<Producto> buscarPorRangoPrecio(BigDecimal precioMin, BigDecimal precioMax);

    /**
     * Actualiza datos de un producto
     * @param id ID del producto
     * @param producto Datos actualizados
     * @return Producto actualizado
     */
    Producto actualizarProducto(Long id, Producto producto);

    /**
     * Elimina lógicamente un producto
     * @param id ID del producto
     */
    void eliminarProducto(Long id);

    /**
     * Incrementa el stock de un producto
     * @param productoId ID del producto
     * @param cantidad Cantidad a incrementar
     */
    void incrementarStock(Long productoId, Integer cantidad);

    /**
     * Decrementa el stock de un producto (para ventas)
     * @param productoId ID del producto
     * @param cantidad Cantidad a decrementar
     * @return true si se pudo decrementar
     */
    boolean decrementarStock(Long productoId, Integer cantidad);

    /**
     * Verifica si hay stock suficiente
     * @param productoId ID del producto
     * @param cantidad Cantidad requerida
     * @return true si hay stock suficiente
     */
    boolean tieneStockSuficiente(Long productoId, Integer cantidad);

    /**
     * Asigna un proveedor a un producto
     * @param productoId ID del producto
     * @param proveedorId ID del proveedor
     * @return Producto actualizado
     */
    Producto asignarProveedor(Long productoId, Long proveedorId);
}