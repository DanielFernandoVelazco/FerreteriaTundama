// ==================== lib/screens/proveedores/proveedor_screen.dart ====================
import 'package:flutter/material.dart';
import '../../services/api_service.dart';
import '../../models/proveedor.dart';
import '../../utils/constants.dart';

class ProveedorScreen extends StatefulWidget {
  const ProveedorScreen({super.key});

  @override
  State<ProveedorScreen> createState() => _ProveedorScreenState();
}

class _ProveedorScreenState extends State<ProveedorScreen> {
  final ApiService _apiService = ApiService();
  List<Proveedor> _proveedores = [];
  bool _isLoading = true;
  bool _isSaving = false;
  final _formKey = GlobalKey<FormState>();
  int? _editingId;
  
  final _nombreController = TextEditingController();
  final _identificacionController = TextEditingController();
  final _direccionController = TextEditingController();
  final _telefonoController = TextEditingController();
  final _notasController = TextEditingController();

  @override
  void initState() {
    super.initState();
    _loadProveedores();
  }

  Future<void> _loadProveedores() async {
    setState(() => _isLoading = true);
    final response = await _apiService.getProveedores();
    if (response.success && response.data != null) {
      setState(() => _proveedores = response.data!);
    }
    setState(() => _isLoading = false);
  }

  void _resetForm() {
    _editingId = null;
    _nombreController.clear();
    _identificacionController.clear();
    _direccionController.clear();
    _telefonoController.clear();
    _notasController.clear();
  }

  void _editProveedor(Proveedor proveedor) {
    _editingId = proveedor.id;
    _nombreController.text = proveedor.nombreEmpresa;
    _identificacionController.text = proveedor.identificacion ?? '';
    _direccionController.text = proveedor.direccion ?? '';
    _telefonoController.text = proveedor.telefono ?? '';
    _notasController.text = proveedor.notas ?? '';
    setState(() {});
  }

  Future<void> _saveProveedor() async {
    if (!_formKey.currentState!.validate()) return;

    setState(() => _isSaving = true);

    final proveedorData = {
      'nombreEmpresa': _nombreController.text.trim(),
      'tipoIdentificacion': 'NIT',
      'identificacion': _identificacionController.text.trim(),
      'direccion': _direccionController.text.trim(),
      'telefono': _telefonoController.text.trim(),
      'notas': _notasController.text.trim(),
    };

    bool success;
    if (_editingId != null) {
      final response = await _apiService.actualizarProveedor(_editingId!, proveedorData);
      success = response.success;
    } else {
      final response = await _apiService.registrarProveedor(proveedorData);
      success = response.success;
    }

    setState(() => _isSaving = false);

    if (success) {
      _resetForm();
      await _loadProveedores();
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(_editingId != null ? 'Proveedor actualizado' : 'Proveedor registrado'),
          backgroundColor: AppColors.success,
        ),
      );
    }
  }

  Future<void> _deleteProveedor(int id, String nombre) async {
    final confirm = await showDialog<bool>(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Confirmar'),
        content: Text('¿Eliminar proveedor "$nombre"?'),
        actions: [
          TextButton(onPressed: () => Navigator.pop(context, false), child: const Text('CANCELAR')),
          TextButton(onPressed: () => Navigator.pop(context, true), child: const Text('ELIMINAR')),
        ],
      ),
    );
    if (confirm != true) return;

    final response = await _apiService.eliminarProveedor(id);
    if (response.success) {
      await _loadProveedores();
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Proveedor eliminado'), backgroundColor: AppColors.success),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('MÓDULO PROVEEDOR'),
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
                              decoration: const InputDecoration(labelText: 'NOMBRE EMPRESA'),
                              style: const TextStyle(color: Colors.white),
                              validator: (v) => v == null || v.isEmpty ? 'Required' : null,
                            ),
                            const SizedBox(height: 15),
                            Row(
                              children: [
                                Expanded(
                                  child: TextFormField(
                                    controller: _identificacionController,
                                    decoration: const InputDecoration(labelText: '# IDENTIFICACIÓN'),
                                    style: const TextStyle(color: Colors.white),
                                  ),
                                ),
                                const SizedBox(width: 15),
                                const Expanded(
                                  child: Text('TIPO: NIT', style: TextStyle(color: Colors.white70)),
                                ),
                              ],
                            ),
                            const SizedBox(height: 15),
                            TextFormField(
                              controller: _direccionController,
                              decoration: const InputDecoration(labelText: 'DIRECCIÓN'),
                              style: const TextStyle(color: Colors.white),
                            ),
                            const SizedBox(height: 15),
                            TextFormField(
                              controller: _telefonoController,
                              decoration: const InputDecoration(labelText: 'TELÉFONO'),
                              style: const TextStyle(color: Colors.white),
                            ),
                            const SizedBox(height: 15),
                            TextFormField(
                              controller: _notasController,
                              maxLines: 3,
                              decoration: const InputDecoration(labelText: 'NOTAS'),
                              style: const TextStyle(color: Colors.white),
                            ),
                            const SizedBox(height: 20),
                            Row(
                              children: [
                                Expanded(
                                  child: ElevatedButton(
                                    onPressed: _isSaving ? null : _saveProveedor,
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
                      'LISTA DE PROVEEDORES',
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
                            DataColumn(label: Text('ID', style: TextStyle(color: Colors.white))),
                            DataColumn(label: Text('EMPRESA', style: TextStyle(color: Colors.white))),
                            DataColumn(label: Text('NIT', style: TextStyle(color: Colors.white))),
                            DataColumn(label: Text('DIRECCIÓN', style: TextStyle(color: Colors.white))),
                            DataColumn(label: Text('TELÉFONO', style: TextStyle(color: Colors.white))),
                            DataColumn(label: Text('ACCIONES', style: TextStyle(color: Colors.white))),
                          ],
                          rows: _proveedores.map((proveedor) {
                            return DataRow(cells: [
                              DataCell(Text(proveedor.idProveedor, style: const TextStyle(color: Colors.white))),
                              DataCell(Text(proveedor.nombreEmpresa, style: const TextStyle(color: Colors.white))),
                              DataCell(Text(proveedor.identificacion ?? '-', style: const TextStyle(color: Colors.white))),
                              DataCell(Text(proveedor.direccion ?? '-', style: const TextStyle(color: Colors.white))),
                              DataCell(Text(proveedor.telefono ?? '-', style: const TextStyle(color: Colors.white))),
                              DataCell(
                                Row(
                                  children: [
                                    IconButton(
                                      icon: const Icon(Icons.edit, color: AppColors.warning),
                                      onPressed: () => _editProveedor(proveedor),
                                    ),
                                    IconButton(
                                      icon: const Icon(Icons.delete, color: AppColors.danger),
                                      onPressed: () => _deleteProveedor(proveedor.id!, proveedor.nombreEmpresa),
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
