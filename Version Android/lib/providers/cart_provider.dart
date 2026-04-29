// ==================== lib/providers/cart_provider.dart ====================
import 'package:flutter/material.dart';
import '../models/cart_item.dart';

class CartProvider extends ChangeNotifier {
  List<CartItem> _items = [];
  int? _selectedClienteId;
  int? _selectedProveedorId;

  List<CartItem> get items => _items;
  int get itemCount => _items.length;
  int? get selectedClienteId => _selectedClienteId;
  int? get selectedProveedorId => _selectedProveedorId;

  double get subtotal {
    return _items.fold(0, (sum, item) => sum + item.subtotal);
  }

  double get ivaTotal {
    return _items.fold(0, (sum, item) => sum + (item.total - item.subtotal));
  }

  double get total {
    return _items.fold(0, (sum, item) => sum + item.total);
  }

  void setSelectedCliente(int? id) {
    _selectedClienteId = id;
    notifyListeners();
  }

  void setSelectedProveedor(int? id) {
    _selectedProveedorId = id;
    notifyListeners();
  }

  void addItem(CartItem item) {
    _items.add(item);
    notifyListeners();
  }

  void removeItem(int index) {
    _items.removeAt(index);
    notifyListeners();
  }

  void clearCart() {
    _items.clear();
    _selectedClienteId = null;
    _selectedProveedorId = null;
    notifyListeners();
  }
}
