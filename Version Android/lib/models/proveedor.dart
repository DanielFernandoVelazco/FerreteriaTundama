// ==================== lib/models/proveedor.dart ====================
class Proveedor {
  final int? id;
  final String idProveedor;
  final String nombreEmpresa;
  final String? tipoIdentificacion;
  final String? identificacion;
  final String? direccion;
  final String? telefono;
  final String? notas;
  final DateTime? fechaRegistro;
  final bool activo;

  Proveedor({
    this.id,
    required this.idProveedor,
    required this.nombreEmpresa,
    this.tipoIdentificacion,
    this.identificacion,
    this.direccion,
    this.telefono,
    this.notas,
    this.fechaRegistro,
    this.activo = true,
  });

  factory Proveedor.fromJson(Map<String, dynamic> json) {
    return Proveedor(
      id: json['id'],
      idProveedor: json['idProveedor'] ?? '',
      nombreEmpresa: json['nombreEmpresa'] ?? '',
      tipoIdentificacion: json['tipoIdentificacion'],
      identificacion: json['identificacion'],
      direccion: json['direccion'],
      telefono: json['telefono'],
      notas: json['notas'],
      fechaRegistro: json['fechaRegistro'] != null 
          ? DateTime.parse(json['fechaRegistro']) 
          : null,
      activo: json['activo'] ?? true,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'nombreEmpresa': nombreEmpresa,
      'tipoIdentificacion': tipoIdentificacion,
      'identificacion': identificacion,
      'direccion': direccion,
      'telefono': telefono,
      'notas': notas,
    };
  }
}
