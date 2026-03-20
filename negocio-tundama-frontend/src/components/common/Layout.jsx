import React from 'react';
import { useLocation } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
// Comenta temporalmente los estilos
// import './Layout.css';

const Layout = ({ children }) => {
  const location = useLocation();
  const { loading } = useAuth();
  const isLoginPage = location.pathname === '/' || location.pathname === '/login' || location.pathname === '/signup';

  console.log('Layout - Ruta actual:', location.pathname);

  if (loading) {
    return <div style={{ textAlign: 'center', padding: '50px' }}>Cargando...</div>;
  }

  return (
    <div style={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      {!isLoginPage && (
        <header style={{
          backgroundColor: '#506070',
          color: 'white',
          padding: '15px 30px',
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center'
        }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '15px' }}>
            <img src="/img/logo.png" alt="Logo" style={{ width: '50px', height: '50px' }} />
            <h1 style={{ fontSize: '1.5rem', margin: 0 }}>NEGOCIO TUNDAMA LTDA</h1>
          </div>
          <div style={{ color: '#e0e0e0' }}>VERSION 1.0</div>
        </header>
      )}

      <main style={{ flex: 1, padding: '20px' }}>
        {children}
      </main>

      <footer style={{
        backgroundColor: '#405060',
        color: 'white',
        textAlign: 'center',
        padding: '15px'
      }}>
        <p style={{ margin: '5px 0' }}>NEGOCIO TUNDAMA LTDA</p>
        <p style={{ margin: '5px 0' }}>TODOS LOS DERECHOS RESERVADOS</p>
      </footer>
    </div>
  );
};

export default Layout;