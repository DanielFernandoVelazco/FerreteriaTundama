import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Layout from './components/common/Layout';

// Páginas
import Login from './pages/Login';
import Menu from './pages/Menu';
import SignUp from './pages/SignUp';
import Cliente from './pages/Cliente';
import Proveedor from './pages/Proveedor';
import Producto from './pages/Producto';
import Ventas from './pages/Ventas';
import Compras from './pages/Compras';

function App() {
  return (
    <AuthProvider>
      <Router>
        <Layout>
          <Routes>
            {/* Rutas públicas */}
            <Route path="/" element={<Login />} />
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<SignUp />} />
            
            {/* Rutas privadas (requieren autenticación) */}
            <Route path="/menu" element={<Menu />} />
            <Route path="/clientes" element={<Cliente />} />
            <Route path="/proveedores" element={<Proveedor />} />
            <Route path="/productos" element={<Producto />} />
            <Route path="/ventas" element={<Ventas />} />
            <Route path="/compras" element={<Compras />} />
            
            {/* Ruta por defecto */}
            <Route path="*" element={<Navigate to="/" />} />
          </Routes>
        </Layout>
      </Router>
    </AuthProvider>
  );
}

export default App;
