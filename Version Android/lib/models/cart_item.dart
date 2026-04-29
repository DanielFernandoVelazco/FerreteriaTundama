// ==================== lib/models/cart_item.dart ====================
class CartItem {
  final int productoId;
  final String codigo;
  final String nombre;
  final double iva;
  final String unidadMedida;
  final double precioUnitario;
  final int cantidad;
  final double subtotal;
  final double total;

  CartItem({
    required this.productoId,
    required this.codigo,
    required this.nombre,
    required this.iva,
    required this.unidadMedida,
    required this.precioUnitario,
    required this.cantidad,
    required this.subtotal,
    required this.total,
  });

  CartItem copyWith({int? cantidad}) {
    final newCantidad = cantidad ?? this.cantidad;
    final newSubtotal = precioUnitario * newCantidad;
    final newTotal = newSubtotal + (newSubtotal * iva / 100);
    
    return CartItem(
      productoId: productoId,
      codigo: codigo,
      nombre: nombre,
      iva: iva,
      unidadMedida: unidadMedida,
      precioUnitario: precioUnitario,
      cantidad: newCantidad,
      subtotal: newSubtotal,
      total: newTotal,
    );
  }
}
