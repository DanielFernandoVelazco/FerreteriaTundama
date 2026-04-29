// ==================== lib/models/producto.dart ====================
class Producto {
  final int? id;
  final String idProducto;
  final String nombre;
  final String? descripcion;
  final String? unidadMedida;
  final double? precioUnitario;
  final double? iva;
  final int stockActual;
  final int stockMinimo;
  final int? proveedorId;
  final String? proveedorNombre;
  final DateTime? fechaRegistro;
  final bool activo;

  Producto({
    this.id,
    required this.idProducto,
    required this.nombre,
    this.descripcion,
    this.unidadMedida,
    this.precioUnitario,
    this.iva,
    this.stockActual = 0,
    this.stockMinimo = 0,
    this.proveedorId,
    this.proveedorNombre,
    this.fechaRegistro,
    this.activo = true,
  });

  factory Producto.fromJson(Map<String, dynamic> json) {
    return Producto(
      id: json['id'],
      idProducto: json['idProducto'] ?? '',
      nombre: json['nombre'] ?? '',
      descripcion: json['descripcion'],
      unidadMedida: json['unidadMedida'],
      precioUnitario: (json['precioUnitario'] ?? 0).toDouble(),
      iva: (json['iva'] ?? 0).toDouble(),
      stockActual: json['stockActual'] ?? 0,
      stockMinimo: json['stockMinimo'] ?? 0,
      proveedorId: json['proveedor'] != null ? json['proveedor']['id'] : null,
      proveedorNombre: json['proveedor'] != null ? json['proveedor']['nombreEmpresa'] : null,
      fechaRegistro: json['fechaRegistro'] != null 
          ? DateTime.parse(json['fechaRegistro']) 
          : null,
      activo: json['activo'] ?? true,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'nombre': nombre,
      'descripcion': descripcion,
      'unidadMedida': unidadMedida,
      'precioUnitario': precioUnitario,
      'iva': iva,
      'stockActual': stockActual,
      'stockMinimo': stockMinimo,
    };
  }

  bool get isLowStock => stockActual <= stockMinimo;
}
