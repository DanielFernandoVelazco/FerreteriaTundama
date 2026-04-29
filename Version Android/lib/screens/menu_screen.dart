import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/auth_provider.dart';
import '../utils/constants.dart';

class MenuScreen extends StatelessWidget {
  const MenuScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final authProvider = Provider.of<AuthProvider>(context);
    final userName = authProvider.user?.nombre ?? 'Usuario';

    return Scaffold(
      appBar: AppBar(
        title: const Text('MENÚ PRINCIPAL'),
        backgroundColor: AppColors.primary,
        actions: [
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Row(
              children: [
                const Icon(Icons.person),
                const SizedBox(width: 5),
                Text(userName),
              ],
            ),
          ),
        ],
      ),
      body: Container(
        color: AppColors.secondary,
        child: Center(
          child: SingleChildScrollView(
            padding: const EdgeInsets.all(20),
            child: Container(
              constraints: const BoxConstraints(maxWidth: 600),
              padding: const EdgeInsets.all(40),
              decoration: BoxDecoration(
                color: AppColors.secondary,
                borderRadius: BorderRadius.circular(10),
                boxShadow: const [
                  BoxShadow(
                      color: Colors.black26,
                      blurRadius: 15,
                      offset: Offset(0, 5)),
                ],
              ),
              child: Column(
                children: [
                  const Text(
                    'BIENVENIDO - ELIGE UNA OPCIÓN',
                    style: TextStyle(
                        fontSize: 24,
                        fontWeight: FontWeight.bold,
                        color: Colors.white),
                    textAlign: TextAlign.center,
                  ),
                  const SizedBox(height: 30),

                  // Botones del menú
                  _buildMenuButton(
                      context, 'REGISTRAR CLIENTE', Icons.people, '/clientes'),
                  const SizedBox(height: 15),
                  _buildMenuButton(context, 'REGISTRAR PROVEEDOR',
                      Icons.business, '/proveedores'),
                  const SizedBox(height: 15),
                  _buildMenuButton(context, 'REGISTRAR PRODUCTO',
                      Icons.inventory, '/productos'),
                  const SizedBox(height: 15),
                  _buildMenuButton(context, 'REGISTRAR VENTAS',
                      Icons.shopping_cart, '/ventas'),
                  const SizedBox(height: 15),
                  _buildMenuButton(context, 'REGISTRAR COMPRAS',
                      Icons.local_mall, '/compras'),
                  const SizedBox(height: 30),

                  // Botón cerrar sesión
                  ElevatedButton(
                    onPressed: () async {
                      await authProvider.logout();
                      if (context.mounted) {
                        Navigator.pushReplacementNamed(context, '/');
                      }
                    },
                    style: ElevatedButton.styleFrom(
                      backgroundColor: AppColors.danger,
                      padding: const EdgeInsets.symmetric(
                          horizontal: 40, vertical: 12),
                    ),
                    child: const Text('CERRAR SESIÓN',
                        style: TextStyle(fontSize: 16)),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildMenuButton(
      BuildContext context, String text, IconData icon, String route) {
    return SizedBox(
      width: double.infinity,
      child: ElevatedButton(
        onPressed: () => Navigator.pushNamed(context, route),
        style: ElevatedButton.styleFrom(
          backgroundColor: AppColors.accent,
          padding: const EdgeInsets.symmetric(vertical: 20),
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(icon, color: Colors.white),
            const SizedBox(width: 10),
            Text(text,
                style:
                    const TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
          ],
        ),
      ),
    );
  }
}
