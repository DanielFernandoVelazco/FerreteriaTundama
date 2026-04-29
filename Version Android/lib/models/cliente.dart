// ==================== lib/models/cliente.dart ====================
class Cliente {
  final int? id;
  final String idCliente;
  final String nombre;
  final String? tipoIdentificacion;
  final String? identificacion;
  final String? direccion;
  final String? telefono;
  final String? notas;
  final DateTime? fechaRegistro;
  final bool activo;

  Cliente({
    this.id,
    required this.idCliente,
    required this.nombre,
    this.tipoIdentificacion,
    this.identificacion,
    this.direccion,
    this.telefono,
    this.notas,
    this.fechaRegistro,
    this.activo = true,
  });

  factory Cliente.fromJson(Map<String, dynamic> json) {
    return Cliente(
      id: json['id'],
      idCliente: json['idCliente'] ?? '',
      nombre: json['nombre'] ?? '',
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
      'nombre': nombre,
      'tipoIdentificacion': tipoIdentificacion,
      'identificacion': identificacion,
      'direccion': direccion,
      'telefono': telefono,
      'notas': notas,
    };
  }
}
