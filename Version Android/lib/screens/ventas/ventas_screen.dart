// ==================== lib/screens/ventas/ventas_screen.dart ====================
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../services/api_service.dart';
import '../../models/cliente.dart';
import '../../models/producto.dart';
import '../../models/cart_item.dart';
import '../../providers/cart_provider.dart';
import '../../utils/constants.dart';
import '../../utils/formatters.dart';

class VentasScreen extends StatefulWidget {
  const VentasScreen({super.key});

  @override
  State<VentasScreen> createState() => _VentasScreenState();
}

class _VentasScreenState extends State<VentasScreen> {
  final ApiService _apiService = ApiService();
  List<Cliente> _clientes = [];
  List<Producto> _productos = [];
  List<dynamic> _ventas = [];
  bool _isLoading = true;
  bool _isSaving = false;
  
  int? _selectedProductoId;
  int _cantidad = 1;
  double _precioUnitario = 0;
  double _iva = 19;

  @override
  void initState() {
    super.initState();
    _loadData();
  }

  Future<void> _loadData() async {
    setState(() => _isLoading = true);
    await Future.wait([
      _apiService.getClientes().then((r) {
        if (r.success && r.data != null) setState(() => _clientes = r.data!);
      }),
      _apiService.getProductos().then((r) {
        if (r.success && r.data != null) setState(() => _productos = r.data!);
      }),
      _apiService.getVentas().then((r) {
        if (r.success && r.data != null) setState(() => _ventas = r.data!);
      }),
    ]);
    setState(() => _isLoading = false);
  }

  void _onProductoSelected(int? productoId) {
    setState(() {
      _selectedProductoId = productoId;
      final producto = _productos.firstWhere((p) => p.id == productoId);
      _precioUnitario = producto.precioUnitario ?? 0;
      _iva = producto.iva ?? 19;
    });
  }

  void _agregarAlCarrito(BuildContext context) {
    if (_selectedProductoId == null) {
      _showError('Seleccione un producto');
      return;
    }
    if (_cantidad <= 0) {
      _showError('La cantidad debe ser mayor a 0');
      return;
    }

    final producto = _productos.firstWhere((p) => p.id == _selectedProductoId);
    
    if (producto.stockActual < _cantidad) {
      _showError('Stock insuficiente. Disponible: ${producto.stockActual}');
      return;
    }

    final subtotalItem = _precioUnitario * _cantidad;
    final ivaItem = subtotalItem * (_iva / 100);
    final totalItem = subtotalItem + ivaItem;

    final cartItem = CartItem(
      productoId: producto.id!,
      codigo: producto.idProducto,
      nombre: producto.nombre,
      iva: _iva,
      unidadMedida: producto.unidadMedida ?? 'Und',
      precioUnitario: _precioUnitario,
      cantidad: _cantidad,
      subtotal: subtotalItem,
      total: totalItem,
    );

    Provider.of<CartProvider>(context, listen: false).addItem(cartItem);
    
    setState(() {
      _selectedProductoId = null;
      _cantidad = 1;
      _precioUnitario = 0;
    });
  }

  Future<void> _registrarVenta(BuildContext context) async {
    final cartProvider = Provider.of<CartProvider>(context, listen: false);
    
    if (cartProvider.selectedClienteId == null) {
      _showError('Seleccione un cliente');
      return;
    }
    if (cartProvider.items.isEmpty) {
      _showError('Agregue productos al carrito');
      return;
    }

    setState(() => _isSaving = true);

    final ventaData = {
      'clienteId': cartProvider.selectedClienteId,
      'detalles': cartProvider.items.map((item) => {
        'productoId': item.productoId,
        'cantidad': item.cantidad,
      }).toList(),
    };

    final response = await _apiService.registrarVenta(ventaData);

    setState(() => _isSaving = false);

    if (response.success) {
      cartProvider.clearCart();
      await _loadData();
      _showSuccess('Venta registrada exitosamente');
    } else {
      _showError(response.message);
    }
  }

  Future<void> _anularVenta(int id) async {
    final confirm = await _showConfirmDialog('¿Anular esta venta?');
    if (!confirm) return;

    final response = await _apiService.anularVenta(id);
    if (response.success) {
      await _loadData();
      _showSuccess('Venta anulada');
    } else {
      _showError(response.message);
    }
  }

  void _showError(String message) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(message), backgroundColor: AppColors.danger),
    );
  }

  void _showSuccess(String message) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(message), backgroundColor: AppColors.success),
    );
  }

  Future<bool> _showConfirmDialog(String message) async {
    return await showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Confirmar'),
        content: Text(message),
        actions: [
          TextButton(onPressed: () => Navigator.pop(context, false), child: const Text('CANCELAR')),
          TextButton(onPressed: () => Navigator.pop(context, true), child: const Text('ACEPTAR')),
        ],
      ),
    ) ?? false;
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<CartProvider>(
      builder: (context, cartProvider, child) {
        return Scaffold(
          appBar: AppBar(
            title: const Text('MÓDULO VENTAS'),
            backgroundColor: AppColors.primary,
          ),
          body: _isLoading
              ? const Center(child: CircularProgressIndicator())
              : Container(
                  color: AppColors.secondary,
                  child: SingleChildScrollView(
                    padding: const EdgeInsets.all(20),
                    child: Column(
                      children: [
                        // Header
                        Container(
                          padding: const EdgeInsets.all(20),
                          decoration: BoxDecoration(
                            color: AppColors.light,
                            borderRadius: BorderRadius.circular(8),
                          ),
                          child: Row(
                            children: [
                              const Icon(Icons.receipt, color: Colors.white),
                              const SizedBox(width: 10),
                              const Text('ID_V: NTV-', style: TextStyle(color: Colors.white, fontSize: 16)),
                              const SizedBox(width: 5),
                              Text(
                                DateTime.now().toString().substring(0, 10).replaceAll('-', ''),
                                style: const TextStyle(color: Colors.white70),
                              ),
                            ],
                          ),
                        ),
                        const SizedBox(height: 20),

                        // Formulario de venta
                        Container(
                          padding: const EdgeInsets.all(20),
                          decoration: BoxDecoration(
                            color: AppColors.light,
                            borderRadius: BorderRadius.circular(8),
                          ),
                          child: Column(
                            children: [
                              // Fecha y Cliente
                              Row(
                                children: [
                                  Expanded(
                                    child: Column(
                                      crossAxisAlignment: CrossAxisAlignment.start,
                                      children: [
                                        const Text('FECHA', style: TextStyle(color: Colors.white70)),
                                        const SizedBox(height: 5),
                                        Text(
                                          Formatters.formatDate(DateTime.now()),
                                          style: const TextStyle(color: Colors.white, fontSize: 16),
                                        ),
                                      ],
                                    ),
                                  ),
                                  const SizedBox(width: 20),
                                  Expanded(
                                    child: Column(
                                      crossAxisAlignment: CrossAxisAlignment.start,
                                      children: [
                                        const Text('ID CLIENTE', style: TextStyle(color: Colors.white70)),
                                        const SizedBox(height: 5),
                                        DropdownButton<int?>(
                                          value: cartProvider.selectedClienteId,
                                          hint: const Text('Seleccione cliente', style: TextStyle(color: Colors.white70)),
                                          dropdownColor: AppColors.primary,
                                          isExpanded: true,
                                          items: [
                                            const DropdownMenuItem(value: null, child: Text('Seleccione cliente')),
                                            ..._clientes.map((c) => DropdownMenuItem(
                                              value: c.id,
                                              child: Text('${c.nombre} - ${c.identificacion ?? ''}'),
                                            )),
                                          ],
                                          onChanged: (value) => cartProvider.setSelectedCliente(value),
                                        ),
                                      ],
                                    ),
                                  ),
                                ],
                              ),
                              const SizedBox(height: 20),
                              const Divider(color: Colors.white54),
                              const SizedBox(height: 20),

                              // Productos
                              Row(
                                children: [
                                  Expanded(
                                    child: DropdownButton<int?>(
                                      value: _selectedProductoId,
                                      hint: const Text('ID PRODUCTO', style: TextStyle(color: Colors.white70)),
                                      dropdownColor: AppColors.primary,
                                      isExpanded: true,
                                      items: [
                                        const DropdownMenuItem(value: null, child: Text('Seleccione producto')),
                                        ..._productos.map((p) => DropdownMenuItem(
                                          value: p.id,
                                          child: Text('${p.nombre} - Stock: ${p.stockActual}'),
                                        )),
                                      ],
                                      onChanged: _onProductoSelected,
                                    ),
                                  ),
                                  const SizedBox(width: 15),
                                  SizedBox(
                                    width: 100,
                                    child: TextFormField(
                                      initialValue: _cantidad.toString(),
                                      keyboardType: TextInputType.number,
                                      style: const TextStyle(color: Colors.white),
                                      decoration: const InputDecoration(labelText: 'CANTIDAD'),
                                      onChanged: (v) => setState(() => _cantidad = int.tryParse(v) ?? 1),
                                    ),
                                  ),
                                  const SizedBox(width: 15),
                                  SizedBox(
                                    width: 120,
                                    child: Text(
                                      'P.UNIT: ${Formatters.formatCurrency(_precioUnitario)}',
                                      style: const TextStyle(color: Colors.white),
                                    ),
                                  ),
                                  const SizedBox(width: 15),
                                  SizedBox(
                                    width: 60,
                                    child: Text('${_iva.toStringAsFixed(0)}%', style: const TextStyle(color: Colors.white)),
                                  ),
                                  const SizedBox(width: 15),
                                  ElevatedButton(
                                    onPressed: () => _agregarAlCarrito(context),
                                    style: ElevatedButton.styleFrom(backgroundColor: AppColors.accent),
                                    child: const Text('AÑADIR'),
                                  ),
                                ],
                              ),
                            ],
                          ),
                        ),
                        const SizedBox(height: 20),

                        // Carrito de compras
                        if (cartProvider.items.isNotEmpty) ...[
                          Container(
                            padding: const EdgeInsets.all(20),
                            decoration: BoxDecoration(
                              color: AppColors.light,
                              borderRadius: BorderRadius.circular(8),
                            ),
                            child: Column(
                              children: [
                                const Text(
                                  'DETALLE DE VENTA',
                                  style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: Colors.white),
                                ),
                                const SizedBox(height: 15),
                                SingleChildScrollView(
                                  scrollDirection: Axis.horizontal,
                                  child: DataTable(
                                    headingRowColor: WidgetStateProperty.all(AppColors.dark),
                                    dataRowColor: WidgetStateProperty.all(AppColors.primary),
                                    columns: const [
                                      DataColumn(label: Text('CÓDIGO', style: TextStyle(color: Colors.white))),
                                      DataColumn(label: Text('NOMBRE', style: TextStyle(color: Colors.white))),
                                      DataColumn(label: Text('IVA', style: TextStyle(color: Colors.white))),
                                      DataColumn(label: Text('UNIDAD', style: TextStyle(color: Colors.white))),
                                      DataColumn(label: Text('PRECIO', style: TextStyle(color: Colors.white))),
                                      DataColumn(label: Text('CANT', style: TextStyle(color: Colors.white))),
                                      DataColumn(label: Text('TOTAL', style: TextStyle(color: Colors.white))),
                                      DataColumn(label: Text('ACCIÓN', style: TextStyle(color: Colors.white))),
                                    ],
                                    rows: cartProvider.items.asMap().entries.map((entry) {
                                      final index = entry.key;
                                      final item = entry.value;
                                      return DataRow(cells: [
                                        DataCell(Text(item.codigo, style: const TextStyle(color: Colors.white))),
                                        DataCell(Text(item.nombre, style: const TextStyle(color: Colors.white))),
                                        DataCell(Text('${item.iva.toStringAsFixed(0)}%', style: const TextStyle(color: Colors.white))),
                                        DataCell(Text(item.unidadMedida, style: const TextStyle(color: Colors.white))),
                                        DataCell(Text(Formatters.formatCurrency(item.precioUnitario), 
                                          style: const TextStyle(color: Colors.white))),
                                        DataCell(Text(item.cantidad.toString(), style: const TextStyle(color: Colors.white))),
                                        DataCell(Text(Formatters.formatCurrency(item.total), 
                                          style: const TextStyle(color: Colors.white))),
                                        DataCell(
                                          IconButton(
                                            icon: const Icon(Icons.delete, color: AppColors.danger),
                                            onPressed: () => cartProvider.removeItem(index),
                                          ),
                                        ),
                                      ]);
                                    }).toList(),
                                  ),
                                ),
                              ],
                            ),
                          ),
                          const SizedBox(height: 20),

                          // Totales
                          Container(
                            padding: const EdgeInsets.all(20),
                            decoration: BoxDecoration(
                              color: AppColors.light,
                              borderRadius: BorderRadius.circular(8),
                            ),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.end,
                              children: [
                                _buildTotalRow('PRECIO:', cartProvider.subtotal),
                                _buildTotalRow('IVA:', cartProvider.ivaTotal),
                                const Divider(color: Colors.white54),
                                _buildTotalRow('TOTAL:', cartProvider.total, isBold: true),
                              ],
                            ),
                          ),
                          const SizedBox(height: 20),
                        ],

                        // Botones de acción
                        Row(
                          children: [
                            Expanded(
                              child: ElevatedButton(
                                onPressed: _isSaving ? null : () => _registrarVenta(context),
                                style: ElevatedButton.styleFrom(
                                  backgroundColor: AppColors.accent,
                                  padding: const EdgeInsets.symmetric(vertical: 15),
                                ),
                                child: _isSaving
                                    ? const CircularProgressIndicator(color: Colors.white)
                                    : const Text('VENDER', style: TextStyle(fontSize: 16)),
                              ),
                            ),
                            const SizedBox(width: 15),
                            Expanded(
                              child: ElevatedButton(
                                onPressed: () => Navigator.pop(context),
                                style: ElevatedButton.styleFrom(
                                  backgroundColor: AppColors.danger,
                                  padding: const EdgeInsets.symmetric(vertical: 15),
                                ),
                                child: const Text('CANCELAR', style: TextStyle(fontSize: 16)),
                              ),
                            ),
                            if (cartProvider.items.isNotEmpty) ...[
                              const SizedBox(width: 15),
                              Expanded(
                                child: ElevatedButton(
                                  onPressed: () => cartProvider.clearCart(),
                                  style: ElevatedButton.styleFrom(
                                    backgroundColor: AppColors.warning,
                                    padding: const EdgeInsets.symmetric(vertical: 15),
                                  ),
                                  child: const Text('NUEVA VENTA', style: TextStyle(fontSize: 16)),
                                ),
                              ),
                            ],
                          ],
                        ),
                        const SizedBox(height: 30),

                        // Historial de ventas
                        const Text(
                          'HISTORIAL DE VENTAS',
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
                                DataColumn(label: Text('ID VENTA', style: TextStyle(color: Colors.white))),
                                DataColumn(label: Text('FECHA', style: TextStyle(color: Colors.white))),
                                DataColumn(label: Text('CLIENTE', style: TextStyle(color: Colors.white))),
                                DataColumn(label: Text('SUBTOTAL', style: TextStyle(color: Colors.white))),
                                DataColumn(label: Text('IVA', style: TextStyle(color: Colors.white))),
                                DataColumn(label: Text('TOTAL', style: TextStyle(color: Colors.white))),
                                DataColumn(label: Text('ESTADO', style: TextStyle(color: Colors.white))),
                                DataColumn(label: Text('ACCIÓN', style: TextStyle(color: Colors.white))),
                              ],
                              rows: _ventas.map((venta) {
                                final isAnulada = venta['estado'] == 'ANULADA';
                                return DataRow(cells: [
                                  DataCell(Text(venta['idVenta'] ?? '-', style: const TextStyle(color: Colors.white))),
                                  DataCell(Text(venta['fechaVenta'] != null 
                                    ? Formatters.formatDate(DateTime.parse(venta['fechaVenta'])) 
                                    : '-', style: const TextStyle(color: Colors.white))),
                                  DataCell(Text(venta['cliente']?['nombre'] ?? '-', style: const TextStyle(color: Colors.white))),
                                  DataCell(Text(Formatters.formatCurrency((venta['subtotal'] ?? 0).toDouble()), 
                                    style: const TextStyle(color: Colors.white))),
                                  DataCell(Text(Formatters.formatCurrency((venta['ivaTotal'] ?? 0).toDouble()), 
                                    style: const TextStyle(color: Colors.white))),
                                  DataCell(Text(Formatters.formatCurrency((venta['total'] ?? 0).toDouble()), 
                                    style: const TextStyle(color: Colors.white))),
                                  DataCell(
                                    Container(
                                      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                                      decoration: BoxDecoration(
                                        color: isAnulada ? AppColors.danger : AppColors.success,
                                        borderRadius: BorderRadius.circular(4),
                                      ),
                                      child: Text(venta['estado'] ?? '-', style: const TextStyle(color: Colors.white, fontSize: 12)),
                                    ),
                                  ),
                                  DataCell(
                                    !isAnulada
                                        ? IconButton(
                                            icon: const Icon(Icons.cancel, color: AppColors.warning),
                                            onPressed: () => _anularVenta(venta['id']),
                                          )
                                        : const SizedBox(),
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
      },
    );
  }

  Widget _buildTotalRow(String label, double value, {bool isBold = false}) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 5),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.end,
        children: [
          Text(
            label,
            style: TextStyle(
              color: Colors.white,
              fontSize: isBold ? 20 : 16,
              fontWeight: isBold ? FontWeight.bold : FontWeight.normal,
            ),
          ),
          const SizedBox(width: 15),
          Text(
            Formatters.formatCurrency(value),
            style: TextStyle(
              color: isBold ? AppColors.accent : Colors.white,
              fontSize: isBold ? 20 : 16,
              fontWeight: isBold ? FontWeight.bold : FontWeight.normal,
            ),
          ),
        ],
      ),
    );
  }
}
