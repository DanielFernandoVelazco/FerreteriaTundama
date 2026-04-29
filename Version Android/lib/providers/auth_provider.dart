import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../services/api_service.dart';
import '../models/usuario.dart';

class AuthProvider extends ChangeNotifier {
  final ApiService _apiService = ApiService();
  Usuario? _user;
  bool _isLoading = false;

  Usuario? get user => _user;
  bool get isLoading => _isLoading;
  bool get isAuthenticated => _user != null;

  AuthProvider() {
    _loadUser();
  }

  Future<void> _loadUser() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      final userId = prefs.getInt('userId');
      final idUsuario = prefs.getString('idUsuario');
      final nombre = prefs.getString('nombre');
      final email = prefs.getString('email');

      if (userId != null && nombre != null && email != null) {
        _user = Usuario(
          id: userId,
          idUsuario: idUsuario ?? '',
          nombre: nombre,
          email: email,
        );
        notifyListeners();
      }
    } catch (e) {
      debugPrint('Error loading user: $e');
    }
  }

  Future<bool> login(String email, String contrasena) async {
    _isLoading = true;
    notifyListeners();

    try {
      final response = await _apiService.login(email, contrasena);

      if (response.success && response.data != null) {
        _user = response.data;

        final prefs = await SharedPreferences.getInstance();
        await prefs.setString('user', 'logged');
        await prefs.setInt('userId', _user!.id!);
        await prefs.setString('idUsuario', _user!.idUsuario);
        await prefs.setString('nombre', _user!.nombre);
        await prefs.setString('email', _user!.email);

        _isLoading = false;
        notifyListeners();
        return true;
      }

      _isLoading = false;
      notifyListeners();
      return false;
    } catch (e) {
      debugPrint('Login error: $e');
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }

  Future<bool> register(Map<String, dynamic> userData) async {
    _isLoading = true;
    notifyListeners();

    try {
      final response = await _apiService.register(userData);
      _isLoading = false;
      notifyListeners();
      return response.success;
    } catch (e) {
      debugPrint('Register error: $e');
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }

  Future<void> logout() async {
    _user = null;
    final prefs = await SharedPreferences.getInstance();
    await prefs.clear();
    notifyListeners();
  }
}
