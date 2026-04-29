// ==================== lib/main.dart ====================
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'providers/auth_provider.dart';
import 'providers/cart_provider.dart';
import 'screens/login_screen.dart';
import 'screens/menu_screen.dart';
import 'screens/signup_screen.dart';
import 'screens/clientes/cliente_screen.dart';
import 'screens/proveedores/proveedor_screen.dart';
import 'screens/productos/producto_screen.dart';
import 'screens/ventas/ventas_screen.dart';
import 'screens/compras/compras_screen.dart';
import 'utils/constants.dart';

void main() {
  runApp(const NegocioTundamaApp());
}

class NegocioTundamaApp extends StatelessWidget {
  const NegocioTundamaApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (_) => AuthProvider()),
        ChangeNotifierProvider(create: (_) => CartProvider()),
      ],
      child: MaterialApp(
        title: 'Negocio Tundama',
        debugShowCheckedModeBanner: false,
        theme: ThemeData(
          primarySwatch: Colors.blueGrey,
          visualDensity: VisualDensity.adaptivePlatformDensity,
          appBarTheme: const AppBarTheme(
            backgroundColor: Color(0xFF506070),
            titleTextStyle: TextStyle(color: Colors.white, fontSize: 20),
            iconTheme: IconThemeData(color: Colors.white),
          ),
        ),
        initialRoute: '/',
        routes: {
          '/': (context) => const LoginScreen(),
          '/menu': (context) => const MenuScreen(),
          '/signup': (context) => const SignupScreen(),
          '/clientes': (context) => const ClienteScreen(),
          '/proveedores': (context) => const ProveedorScreen(),
          '/productos': (context) => const ProductoScreen(),
          '/ventas': (context) => const VentasScreen(),
          '/compras': (context) => const ComprasScreen(),
        },
      ),
    );
  }
}
