import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/api_response.dart';
import '../models/usuario.dart';
import '../models/cliente.dart';
import '../models/proveedor.dart';
import '../models/producto.dart';
import '../utils/constants.dart';

class ApiService {
  // Para web local
  static const String baseUrl = 'http://localhost:8081/api';

  // Para emulador Android
  // static const String baseUrl = 'http://10.0.2.2:8081/api';

  // Para dispositivo físico (reemplaza con tu IP)
  // static const String baseUrl = 'http://192.168.1.100:8081/api';

  // ==================== USUARIOS ====================
  Future<ApiResponse<Usuario>> login(String email, String contrasena) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/usuarios/login'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({'email': email, 'contrasena': contrasena}),
      );

      print('Login response status: ${response.statusCode}');
      print('Login response body: ${response.body}');

      final data = jsonDecode(response.body);
      if (response.statusCode == 200 && data['success'] == true) {
        return ApiResponse<Usuario>.fromJson(
          data,
          (json) => Usuario.fromJson(json),
        );
      }
      return ApiResponse(
        success: false,
        message: data['message'] ?? 'Error de autenticación',
      );
    } catch (e) {
      print('Login error: $e');
      return ApiResponse(success: false, message: 'Error de conexión: $e');
    }
  }

  Future<ApiResponse<Usuario>> register(Map<String, dynamic> userData) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/usuarios/registrar'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode(userData),
      );

      final data = jsonDecode(response.body);
      if (response.statusCode == 200 && data['success'] == true) {
        return ApiResponse<Usuario>.fromJson(
          data,
          (json) => Usuario.fromJson(json),
        );
      }
      return ApiResponse(
        success: false,
        message: data['message'] ?? 'Error al registrar',
      );
    } catch (e) {
      return ApiResponse(success: false, message: 'Error de conexión: $e');
    }
  }

  // ==================== CLIENTES ====================
  Future<ApiResponse<List<Cliente>>> getClientes() async {
    try {
      final response = await http.get(Uri.parse('$baseUrl/clientes'));
      final data = jsonDecode(response.body);

      if (response.statusCode == 200 && data['success'] == true) {
        final List<dynamic> clientesJson = data['data'];
        return ApiResponse<List<Cliente>>.fromListJson(
          data,
          (list) => clientesJson.map((j) => Cliente.fromJson(j)).toList(),
        );
      }
      return ApiResponse(success: false, message: 'Error al cargar clientes');
    } catch (e) {
      return ApiResponse(success: false, message: 'Error de conexión: $e');
    }
  }

  Future<ApiResponse<Cliente>> registrarCliente(
      Map<String, dynamic> cliente) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/clientes/registrar'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode(cliente),
      );

      final data = jsonDecode(response.body);
      if (response.statusCode == 200 && data['success'] == true) {
        return ApiResponse<Cliente>.fromJson(
          data,
          (json) => Cliente.fromJson(json),
        );
      }
      return ApiResponse(
        success: false,
        message: data['message'] ?? 'Error al registrar cliente',
      );
    } catch (e) {
      return ApiResponse(success: false, message: 'Error de conexión: $e');
    }
  }

  Future<ApiResponse<Cliente>> actualizarCliente(
      int id, Map<String, dynamic> cliente) async {
    try {
      final response = await http.put(
        Uri.parse('$baseUrl/clientes/$id'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode(cliente),
      );

      final data = jsonDecode(response.body);
      return ApiResponse<Cliente>.fromJson(
        data,
        (json) => Cliente.fromJson(json),
      );
    } catch (e) {
      return ApiResponse(success: false, message: 'Error de conexión: $e');
    }
  }

  Future<ApiResponse<void>> eliminarCliente(int id) async {
    try {
      final response = await http.delete(Uri.parse('$baseUrl/clientes/$id'));
      final data = jsonDecode(response.body);
      return ApiResponse(
          success: data['success'] ?? false, message: data['message'] ?? '');
    } catch (e) {
      return ApiResponse(success: false, message: 'Error de conexión: $e');
    }
  }

  // ==================== PROVEEDORES ====================
  Future<ApiResponse<List<Proveedor>>> getProveedores() async {
    try {
      final response = await http.get(Uri.parse('$baseUrl/proveedores'));
      final data = jsonDecode(response.body);

      if (response.statusCode == 200 && data['success'] == true) {
        final List<dynamic> proveedoresJson = data['data'];
        return ApiResponse<List<Proveedor>>.fromListJson(
          data,
          (list) => proveedoresJson.map((j) => Proveedor.fromJson(j)).toList(),
        );
      }
      return ApiResponse(
          success: false, message: 'Error al cargar proveedores');
    } catch (e) {
      return ApiResponse(success: false, message: 'Error de conexión: $e');
    }
  }

  Future<ApiResponse<Proveedor>> registrarProveedor(
      Map<String, dynamic> proveedor) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/proveedores/registrar'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode(proveedor),
      );

      final data = jsonDecode(response.body);
      if (response.statusCode == 200 && data['success'] == true) {
        return ApiResponse<Proveedor>.fromJson(
          data,
          (json) => Proveedor.fromJson(json),
        );
      }
      return ApiResponse(
        success: false,
        message: data['message'] ?? 'Error al registrar proveedor',
      );
    } catch (e) {
      return ApiResponse(success: false, message: 'Error de conexión: $e');
    }
  }

  Future<ApiResponse<Proveedor>> actualizarProveedor(
      int id, Map<String, dynamic> proveedor) async {
    try {
      final response = await http.put(
        Uri.parse('$baseUrl/proveedores/$id'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode(proveedor),
      );

      final data = jsonDecode(response.body);
      return ApiResponse<Proveedor>.fromJson(
        data,
        (json) => Proveedor.fromJson(json),
      );
    } catch (e) {
      return ApiResponse(success: false, message: 'Error de conexión: $e');
    }
  }

  Future<ApiResponse<void>> eliminarProveedor(int id) async {
    try {
      final response = await http.delete(Uri.parse('$baseUrl/proveedores/$id'));
      final data = jsonDecode(response.body);
      return ApiResponse(
          success: data['success'] ?? false, message: data['message'] ?? '');
    } catch (e) {
      return ApiResponse(success: false, message: 'Error de conexión: $e');
    }
  }

  // ==================== PRODUCTOS ====================
  Future<ApiResponse<List<Producto>>> getProductos() async {
    try {
      final response = await http.get(Uri.parse('$baseUrl/productos'));
      final data = jsonDecode(response.body);

      if (response.statusCode == 200 && data['success'] == true) {
        final List<dynamic> productosJson = data['data'];
        return ApiResponse<List<Producto>>.fromListJson(
          data,
          (list) => productosJson.map((j) => Producto.fromJson(j)).toList(),
        );
      }
      return ApiResponse(success: false, message: 'Error al cargar productos');
    } catch (e) {
      return ApiResponse(success: false, message: 'Error de conexión: $e');
    }
  }

  Future<ApiResponse<Producto>> registrarProducto(
      Map<String, dynamic> producto) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/productos/registrar'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode(producto),
      );

      final data = jsonDecode(response.body);
      if (response.statusCode == 200 && data['success'] == true) {
        return ApiResponse<Producto>.fromJson(
          data,
          (json) => Producto.fromJson(json),
        );
      }
      return ApiResponse(
        success: false,
        message: data['message'] ?? 'Error al registrar producto',
      );
    } catch (e) {
      return ApiResponse(success: false, message: 'Error de conexión: $e');
    }
  }

  Future<ApiResponse<Producto>> actualizarProducto(
      int id, Map<String, dynamic> producto) async {
    try {
      final response = await http.put(
        Uri.parse('$baseUrl/productos/$id'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode(producto),
      );

      final data = jsonDecode(response.body);
      return ApiResponse<Producto>.fromJson(
        data,
        (json) => Producto.fromJson(json),
      );
    } catch (e) {
      return ApiResponse(success: false, message: 'Error de conexión: $e');
    }
  }

  Future<ApiResponse<void>> eliminarProducto(int id) async {
    try {
      final response = await http.delete(Uri.parse('$baseUrl/productos/$id'));
      final data = jsonDecode(response.body);
      return ApiResponse(
          success: data['success'] ?? false, message: data['message'] ?? '');
    } catch (e) {
      return ApiResponse(success: false, message: 'Error de conexión: $e');
    }
  }

  // ==================== VENTAS ====================
  Future<ApiResponse<dynamic>> registrarVenta(
      Map<String, dynamic> ventaData) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/ventas/registrar'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode(ventaData),
      );

      final data = jsonDecode(response.body);
      return ApiResponse(
        success: data['success'] ?? false,
        message: data['message'] ?? '',
        data: data['data'],
      );
    } catch (e) {
      return ApiResponse(success: false, message: 'Error de conexión: $e');
    }
  }

  Future<ApiResponse<List<dynamic>>> getVentas() async {
    try {
      final response = await http.get(Uri.parse('$baseUrl/ventas'));
      final data = jsonDecode(response.body);
      return ApiResponse(
        success: data['success'] ?? false,
        message: data['message'] ?? '',
        data: data['data'],
      );
    } catch (e) {
      return ApiResponse(success: false, message: 'Error de conexión: $e');
    }
  }

  Future<ApiResponse<dynamic>> anularVenta(int id) async {
    try {
      final response = await http.put(Uri.parse('$baseUrl/ventas/$id/anular'));
      final data = jsonDecode(response.body);
      return ApiResponse(
        success: data['success'] ?? false,
        message: data['message'] ?? '',
        data: data['data'],
      );
    } catch (e) {
      return ApiResponse(success: false, message: 'Error de conexión: $e');
    }
  }

  // ==================== COMPRAS ====================
  Future<ApiResponse<dynamic>> registrarCompra(
      Map<String, dynamic> compraData) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/compras/registrar'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode(compraData),
      );

      final data = jsonDecode(response.body);
      return ApiResponse(
        success: data['success'] ?? false,
        message: data['message'] ?? '',
        data: data['data'],
      );
    } catch (e) {
      return ApiResponse(success: false, message: 'Error de conexión: $e');
    }
  }

  Future<ApiResponse<List<dynamic>>> getCompras() async {
    try {
      final response = await http.get(Uri.parse('$baseUrl/compras'));
      final data = jsonDecode(response.body);
      return ApiResponse(
        success: data['success'] ?? false,
        message: data['message'] ?? '',
        data: data['data'],
      );
    } catch (e) {
      return ApiResponse(success: false, message: 'Error de conexión: $e');
    }
  }

  Future<ApiResponse<dynamic>> anularCompra(int id) async {
    try {
      final response = await http.put(Uri.parse('$baseUrl/compras/$id/anular'));
      final data = jsonDecode(response.body);
      return ApiResponse(
        success: data['success'] ?? false,
        message: data['message'] ?? '',
        data: data['data'],
      );
    } catch (e) {
      return ApiResponse(success: false, message: 'Error de conexión: $e');
    }
  }
}
