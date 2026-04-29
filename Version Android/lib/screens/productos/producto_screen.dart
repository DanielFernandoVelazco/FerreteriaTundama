// ==================== lib/screens/productos/producto_screen.dart ====================
import 'package:flutter/material.dart';
import '../../services/api_service.dart';
import '../../models/producto.dart';
import '../../models/proveedor.dart';
import '../../utils/constants.dart';
import '../../utils/formatters.dart';

class ProductoScreen extends StatefulWidget {
  const ProductoScreen({super.key});

  @override
  State<ProductoScreen> createState() => _ProductoScreenState();
}

class _ProductoScreenState extends State<ProductoScreen> {
  final ApiService _apiService = ApiService();
  List<Producto> _productos = [];
  List<Proveedor> _proveedores = [];
  bool _isLoading = true;
  bool _isSaving = false;
  final _formKey = GlobalKey<FormState>();
  int? _editingId;
  
  final _nombreController = TextEditingController();
  final _descripcionController = TextEditingController();
  final _precioController = TextEditingController();
  final _stockMinimoController = TextEditingController();
  String _unidadMedida = 'Und';
  double _iva = 19;
  int? _proveedorId;

  @override
  void initState() {
    super.initState();
    _loadData();
  }

  Future<void> _loadData() async {
    setState(() => _isLoading = true);
    await Future.wait([
      _apiService.getProductos().then((r) {
        if (r.success && r.data != null) setState(() => _productos = r.data!);
      }),
      _apiService.getProveedores().then((r) {
        if (r.success && r.data != null) setState(() => _proveedores = r.data!);
      }),
    ]);
    setState(() => _isLoading = false);
  }

  void _resetForm() {
    _editingId = null;
    _nombreController.clear();
    _descripcionController.clear();
    _precioController.clear();
    _stockMinimoController.clear();
    _unidadMedida = 'Und';
    _iva = 19;
    _proveedorId = null;
  }

  void _editProducto(Producto producto) {
    _editingId = producto.id;
    _nombreController.text = producto.nombre;
    _descripcionController.text = producto.descripcion ?? '';
    _precioController.text = producto.precioUnitario?.toString() ?? '';
    _stockMinimoController.text = producto.stockMinimo.toString();
    _unidadMedida = producto.unidadMedida ?? 'Und';
    _iva = producto.iva ?? 19;
    _proveedorId = producto.proveedorId;
    setState(() {});
  }

  Future<void> _saveProducto() async {
    if (!_formKey.currentState!.validate()) return;

    setState(() => _isSaving = true);

    final productoData = {
      'nombre': _nombreController.text.trim(),
      'descripcion': _descripcionController.text.trim(),
      'unidadMedida': _unidadMedida,
      'precioUnitario': double.parse(_precioController.text),
      'iva': _iva,
      'stockMinimo': int.parse(_stockMinimoController.text),
      'proveedorId': _proveedorId,
    };

    bool success;
    if (_editingId != null) {
      final response = await _apiService.actualizarProducto(_editingId!, productoData);
      success = response.success;
    } else {
      final response = await _apiService.registrarProducto(productoData);
      success = response.success;
    }

    setState(() => _isSaving = false);

    if (success) {
      _resetForm();
      await _loadData();
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(_editingId != null ? 'Producto actualizado' : 'Producto registrado'),
          backgroundColor: AppColors.success,
        ),
      );
    }
  }

  Future<void> _deleteProducto(int id, String nombre) async {
    final confirm = await showDialog<bool>(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Confirmar'),
        content: Text('¿Eliminar producto "$nombre"?'),
        actions: [
          TextButton(onPressed: () => Navigator.pop(context, false), child: const Text('CANCELAR')),
          TextButton(onPressed: () => Navigator.pop(context, true), child: const Text('ELIMINAR')),
        ],
      ),
    );
    if (confirm != true) return;

    final response = await _apiService.eliminarProducto(id);
    if (response.success) {
      await _loadData();
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Producto eliminado'), backgroundColor: AppColors.success),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('MÓDULO PRODUCTO'),
        backgroundColor: AppColors.primary,
      ),
      body: Container(
        color: AppColors.secondary,
        child: _isLoading
            ? const Center(child: CircularProgressIndicator())
            : SingleChildScrollView(
                padding: const EdgeInsets.all(20),
                child: Column(
                  children: [
                    Container(
                      padding: const EdgeInsets.all(20),
                      decoration: BoxDecoration(
                        color: AppColors.light,
                        borderRadius: BorderRadius.circular(8),
                      ),
                      child: Form(
                        key: _formKey,
                        child: Column(
                          children: [
                            TextFormField(
                              controller: _nombreController,
                              decoration: const InputDecoration(labelText: 'NOMBRE'),
                              style: const TextStyle(color: Colors.white),
                              validator: (v) => v == null || v.isEmpty ? 'Required' : null,
                            ),
                            const SizedBox(height: 15),
                            TextFormField(
                              controller: _descripcionController,
                              maxLines: 2,
                              decoration: const InputDecoration(labelText: 'DESCRIPCIÓN'),
                              style: const TextStyle(color: Colors.white),
                            ),
                            const SizedBox(height: 15),
                            Row(
                              children: [
                                Expanded(
                                  child: DropdownButtonFormField<String>(
                                    value: _unidadMedida,
                                    items: const [
                                      DropdownMenuItem(value: 'Und', child: Text('Unidad')),
                                      DropdownMenuItem(value: 'Kg', child: Text('Kilogramo')),
                                      DropdownMenuItem(value: 'Lt', child: Text('Litro')),
                                      DropdownMenuItem(value: 'Mts', child: Text('Metro')),
                                      DropdownMenuItem(value: 'Caja', child: Text('Caja')),
                                    ],
                                    onChanged: (v) => setState(() => _unidadMedida = v!),
                                    decoration: const InputDecoration(labelText: 'UNIDAD MEDIDA'),
                                    style: const TextStyle(color: Colors.white),
                                  ),
                                ),
                                const SizedBox(width: 15),
                                Expanded(
                                  child: TextFormField(
                                    controller: _precioController,
                                    keyboardType: TextInputType.number,
                                    decoration: const InputDecoration(labelText: 'PRECIO UNITARIO'),
                                    style: const TextStyle(color: Colors.white),
                                    validator: (v) => v == null || v.isEmpty ? 'Required' : null,
                                  ),
                                ),
                                const SizedBox(width: 15),
                                Expanded(
                                  child: DropdownButtonFormField<double>(
                                    value: _iva,
                                    items: const [
                                      DropdownMenuItem(value: 0.0, child: Text('0%')),
                                      DropdownMenuItem(value: 5.0, child: Text('5%')),
                                      DropdownMenuItem(value: 19.0, child: Text('19%')),
                                    ],
                                    onChanged: (v) => setState(() => _iva = v!),
                                    decoration: const InputDecoration(labelText: 'IVA'),
                                    style: const TextStyle(color: Colors.white),
                                  ),
                                ),
                              ],
                            ),
                            const SizedBox(height: 15),
                            Row(
                              children: [
                                Expanded(
                                  child: TextFormField(
                                    controller: _stockMinimoController,
                                    keyboardType: TextInputType.number,
                                    decoration: const InputDecoration(labelText: 'STOCK MÍNIMO'),
                                    style: const TextStyle(color: Colors.white),
                                  ),
                                ),
                                const SizedBox(width: 15),
                                Expanded(
                                  child: DropdownButtonFormField<int?>(
                                    value: _proveedorId,
                                    items: [
                                      const DropdownMenuItem(value: null, child: Text('Sin proveedor')),
                                      ..._proveedores.map((p) => DropdownMenuItem(
                                        value: p.id, child: Text(p.nombreEmpresa))),
                                    ],
                                    onChanged: (v) => setState(() => _proveedorId = v),
                                    decoration: const InputDecoration(labelText: 'PROVEEDOR'),
                                    style: const TextStyle(color: Colors.white),
                                  ),
                                ),
                              ],
                            ),
                            const SizedBox(height: 20),
                            Row(
                              children: [
                                Expanded(
                                  child: ElevatedButton(
                                    onPressed: _isSaving ? null : _saveProducto,
                                    style: ElevatedButton.styleFrom(backgroundColor: AppColors.accent),
                                    child: _isSaving
                                        ? const CircularProgressIndicator()
                                        : Text(_editingId != null ? 'MODIFICAR' : 'REGISTRAR'),
                                  ),
                                ),
                                if (_editingId != null) ...[
                                  const SizedBox(width: 10),
                                  Expanded(
                                    child: ElevatedButton(
                                      onPressed: _resetForm,
                                      style: ElevatedButton.styleFrom(backgroundColor: AppColors.warning),
                                      child: const Text('NUEVO'),
                                    ),
                                  ),
                                ],
                                const SizedBox(width: 10),
                                Expanded(
                                  child: ElevatedButton(
                                    onPressed: () => Navigator.pop(context),
                                    style: ElevatedButton.styleFrom(backgroundColor: AppColors.danger),
                                    child: const Text('CANCELAR'),
                                  ),
                                ),
                              ],
                            ),
                          ],
                        ),
                      ),
                    ),
                    const SizedBox(height: 30),
                    const Text(
                      'LISTA DE PRODUCTOS',
                      style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold, color: Colors.white),
                    ),
                    const SizedBox(height: 15),
                    Container(
                      decoration: BoxDecoration(
                        color: AppColors.primary,
                        borderRadius: BorderRadius.circular(8),
                      ),
                      child: SingleChildScrollView(
                        scrollDirection: Axis.horizontal,
                        child: DataTable(
                          headingRowColor: WidgetStateProperty.all(AppColors.dark),
                          dataRowColor: WidgetStateProperty.all(AppColors.primary),
                          columns: const [
                            DataColumn(label: Text('CÓDIGO', style: TextStyle(color: Colors.white))),
                            DataColumn(label: Text('NOMBRE', style: TextStyle(color: Colors.white))),
                            DataColumn(label: Text('UNIDAD', style: TextStyle(color: Colors.white))),
                            DataColumn(label: Text('PRECIO', style: TextStyle(color: Colors.white))),
                            DataColumn(label: Text('IVA', style: TextStyle(color: Colors.white))),
                            DataColumn(label: Text('STOCK', style: TextStyle(color: Colors.white))),
                            DataColumn(label: Text('PROVEEDOR', style: TextStyle(color: Colors.white))),
                            DataColumn(label: Text('ACCIONES', style: TextStyle(color: Colors.white))),
                          ],
                          rows: _productos.map((producto) {
                            final isLowStock = producto.isLowStock;
                            return DataRow(cells: [
                              DataCell(Text(producto.idProducto, style: const TextStyle(color: Colors.white))),
                              DataCell(Text(producto.nombre, style: const TextStyle(color: Colors.white))),
                              DataCell(Text(producto.unidadMedida ?? '-', style: const TextStyle(color: Colors.white))),
                              DataCell(Text(Formatters.formatCurrency(producto.precioUnitario ?? 0), 
                                style: const TextStyle(color: Colors.white))),
                              DataCell(Text('${producto.iva?.toStringAsFixed(0) ?? 0}%', 
                                style: const TextStyle(color: Colors.white))),
                              DataCell(
                                Text(producto.stockActual.toString(), 
                                  style: TextStyle(
                                    color: isLowStock ? AppColors.danger : Colors.white,
                                    fontWeight: isLowStock ? FontWeight.bold : FontWeight.normal,
                                  ),
                                ),
                              ),
                              DataCell(Text(producto.proveedorNombre ?? 'N/A', 
                                style: const TextStyle(color: Colors.white))),
                              DataCell(
                                Row(
                                  children: [
                                    IconButton(
                                      icon: const Icon(Icons.edit, color: AppColors.warning),
                                      onPressed: () => _editProducto(producto),
                                    ),
                                    IconButton(
                                      icon: const Icon(Icons.delete, color: AppColors.danger),
                                      onPressed: () => _deleteProducto(producto.id!, producto.nombre),
                                    ),
                                  ],
                                ),
                              ),
                            ]);
                          }).toList(),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
      ),
    );
  }
}
