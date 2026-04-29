// ==================== lib/screens/clientes/cliente_screen.dart ====================
import 'package:flutter/material.dart';
import '../../services/api_service.dart';
import '../../models/cliente.dart';
import '../../utils/constants.dart';
import '../../utils/formatters.dart';

class ClienteScreen extends StatefulWidget {
  const ClienteScreen({super.key});

  @override
  State<ClienteScreen> createState() => _ClienteScreenState();
}

class _ClienteScreenState extends State<ClienteScreen> {
  final ApiService _apiService = ApiService();
  List<Cliente> _clientes = [];
  bool _isLoading = true;
  bool _isSaving = false;
  final _formKey = GlobalKey<FormState>();
  int? _editingId;
  
  final _nombreController = TextEditingController();
  final _identificacionController = TextEditingController();
  final _direccionController = TextEditingController();
  final _telefonoController = TextEditingController();
  final _notasController = TextEditingController();
  String _tipoIdentificacion = 'NIT';

  @override
  void initState() {
    super.initState();
    _loadClientes();
  }

  Future<void> _loadClientes() async {
    setState(() => _isLoading = true);
    final response = await _apiService.getClientes();
    if (response.success && response.data != null) {
      setState(() => _clientes = response.data!);
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
    _tipoIdentificacion = 'NIT';
  }

  void _editCliente(Cliente cliente) {
    _editingId = cliente.id;
    _nombreController.text = cliente.nombre;
    _identificacionController.text = cliente.identificacion ?? '';
    _direccionController.text = cliente.direccion ?? '';
    _telefonoController.text = cliente.telefono ?? '';
    _notasController.text = cliente.notas ?? '';
    _tipoIdentificacion = cliente.tipoIdentificacion ?? 'NIT';
    setState(() {});
  }

  Future<void> _saveCliente() async {
    if (!_formKey.currentState!.validate()) return;

    setState(() => _isSaving = true);

    final clienteData = {
      'nombre': _nombreController.text.trim(),
      'tipoIdentificacion': _tipoIdentificacion,
      'identificacion': _identificacionController.text.trim(),
      'direccion': _direccionController.text.trim(),
      'telefono': _telefonoController.text.trim(),
      'notas': _notasController.text.trim(),
    };

    bool success;
    if (_editingId != null) {
      final response = await _apiService.actualizarCliente(_editingId!, clienteData);
      success = response.success;
    } else {
      final response = await _apiService.registrarCliente(clienteData);
      success = response.success;
    }

    setState(() => _isSaving = false);

    if (success) {
      _resetForm();
      await _loadClientes();
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(_editingId != null ? 'Cliente actualizado' : 'Cliente registrado'),
          backgroundColor: AppColors.success,
        ),
      );
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Error al guardar cliente'),
          backgroundColor: AppColors.danger,
        ),
      );
    }
  }

  Future<void> _deleteCliente(int id, String nombre) async {
    final confirm = await showDialog<bool>(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Confirmar'),
        content: Text('¿Eliminar cliente "$nombre"?'),
        actions: [
          TextButton(onPressed: () => Navigator.pop(context, false), child: const Text('CANCELAR')),
          TextButton(onPressed: () => Navigator.pop(context, true), child: const Text('ELIMINAR')),
        ],
      ),
    );

    if (confirm != true) return;

    final response = await _apiService.eliminarCliente(id);
    if (response.success) {
      await _loadClientes();
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Cliente eliminado'), backgroundColor: AppColors.success),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('MÓDULO CLIENTE'),
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
                    // Formulario
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
                            Row(
                              children: [
                                Expanded(
                                  child: DropdownButtonFormField<String>(
                                    value: _tipoIdentificacion,
                                    items: const [
                                      DropdownMenuItem(value: 'NIT', child: Text('NIT')),
                                      DropdownMenuItem(value: 'CEDULA', child: Text('CÉDULA')),
                                    ],
                                    onChanged: (v) => setState(() => _tipoIdentificacion = v!),
                                    decoration: const InputDecoration(labelText: 'TIPO'),
                                    style: const TextStyle(color: Colors.white),
                                  ),
                                ),
                                const SizedBox(width: 15),
                                Expanded(
                                  child: TextFormField(
                                    controller: _identificacionController,
                                    decoration: const InputDecoration(labelText: '# IDENTIFICACIÓN'),
                                    style: const TextStyle(color: Colors.white),
                                  ),
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
                                    onPressed: _isSaving ? null : _saveCliente,
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

                    // Tabla de clientes
                    const Text(
                      'LISTA DE CLIENTES',
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
                          columnSpacing: 20,
                          headingRowColor: WidgetStateProperty.all(AppColors.dark),
                          dataRowColor: WidgetStateProperty.all(AppColors.primary),
                          columns: const [
                            DataColumn(label: Text('ID', style: TextStyle(color: Colors.white))),
                            DataColumn(label: Text('NOMBRE', style: TextStyle(color: Colors.white))),
                            DataColumn(label: Text('IDENTIFICACIÓN', style: TextStyle(color: Colors.white))),
                            DataColumn(label: Text('DIRECCIÓN', style: TextStyle(color: Colors.white))),
                            DataColumn(label: Text('TELÉFONO', style: TextStyle(color: Colors.white))),
                            DataColumn(label: Text('ACCIONES', style: TextStyle(color: Colors.white))),
                          ],
                          rows: _clientes.map((cliente) {
                            return DataRow(cells: [
                              DataCell(Text(cliente.idCliente, style: const TextStyle(color: Colors.white))),
                              DataCell(Text(cliente.nombre, style: const TextStyle(color: Colors.white))),
                              DataCell(Text(cliente.identificacion ?? '-', style: const TextStyle(color: Colors.white))),
                              DataCell(Text(cliente.direccion ?? '-', style: const TextStyle(color: Colors.white))),
                              DataCell(Text(cliente.telefono ?? '-', style: const TextStyle(color: Colors.white))),
                              DataCell(
                                Row(
                                  children: [
                                    IconButton(
                                      icon: const Icon(Icons.edit, color: AppColors.warning),
                                      onPressed: () => _editCliente(cliente),
                                    ),
                                    IconButton(
                                      icon: const Icon(Icons.delete, color: AppColors.danger),
                                      onPressed: () => _deleteCliente(cliente.id!, cliente.nombre),
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
