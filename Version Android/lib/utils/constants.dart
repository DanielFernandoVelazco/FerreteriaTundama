import 'package:flutter/material.dart';

class AppColors {
  static const primary = Color(0xFF506070);
  static const secondary = Color(0xFF708090);
  static const accent = Color(0xFF3ABDBF);
  static const danger = Color(0xFFDC3545);
  static const warning = Color(0xFFFFC107);
  static const success = Color(0xFF28A745);
  static const dark = Color(0xFF3A4750);
  static const light = Color(0xFF607080);
  static const white = Colors.white;
  static const black = Colors.black;
}

class AppStrings {
  static const appName = 'Negocio Tundama';
  static const version = 'VERSION 1.0';
  static const footer = 'NEGOCIO TUNDAMA LTDA\nTODOS LOS DERECHOS RESERVADOS';
}

class ApiEndpoints {
  // Para emulador Android
  static const baseUrlEmulator = 'http://10.0.2.2:8081/api';
  // Para dispositivo físico (cambia IP por la de tu computadora)
  static const baseUrlPhysical = 'http://192.168.1.X:8081/api';
  // Para web local
  static const baseUrlWeb = 'http://localhost:8081/api';

  // Usa esta variable para desarrollo (cambia según donde ejecutes)
  static const baseUrl = baseUrlWeb;

  static const login = '/usuarios/login';
  static const register = '/usuarios/registrar';
  static const clientes = '/clientes';
  static const proveedores = '/proveedores';
  static const productos = '/productos';
  static const ventas = '/ventas';
  static const compras = '/compras';
}
