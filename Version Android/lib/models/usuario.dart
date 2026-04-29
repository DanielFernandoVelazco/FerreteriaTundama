// ==================== lib/models/usuario.dart ====================
class Usuario {
  final int? id;
  final String idUsuario;
  final String nombre;
  final String? direccion;
  final String email;
  final String? contrasena;
  final String? notas;
  final DateTime? fechaRegistro;
  final bool activo;

  Usuario({
    this.id,
    required this.idUsuario,
    required this.nombre,
    this.direccion,
    required this.email,
    this.contrasena,
    this.notas,
    this.fechaRegistro,
    this.activo = true,
  });

  factory Usuario.fromJson(Map<String, dynamic> json) {
    return Usuario(
      id: json['id'],
      idUsuario: json['idUsuario'] ?? '',
      nombre: json['nombre'] ?? '',
      direccion: json['direccion'],
      email: json['email'] ?? '',
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
      'idUsuario': idUsuario,
      'nombre': nombre,
      'direccion': direccion,
      'email': email,
      'contrasena': contrasena,
      'notas': notas,
    };
  }
}
