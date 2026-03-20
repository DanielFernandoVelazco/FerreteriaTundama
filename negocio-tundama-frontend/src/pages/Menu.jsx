import React from 'react';
import { Link } from 'react-router-dom';
// Comenta temporalmente la importación del CSS
// import './Menu.css';

const Menu = () => {
  console.log('✅ MENÚ CARGADO CORRECTAMENTE');

  return (
    <div style={{
      backgroundColor: '#708090',
      padding: '40px',
      borderRadius: '10px',
      maxWidth: '600px',
      margin: '50px auto',
      color: 'white',
      textAlign: 'center',
      boxShadow: '0 5px 15px rgba(0,0,0,0.3)'
    }}>
      <h1 style={{ fontSize: '2rem', marginBottom: '30px', color: 'white' }}>
        BIENVENIDO - ELIGE UNA OPCIÓN
      </h1>

      <div style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
        <Link to="/clientes" style={{
          backgroundColor: '#3ABDBF',
          color: 'white',
          padding: '20px',
          borderRadius: '8px',
          textDecoration: 'none',
          fontSize: '1.2rem',
          fontWeight: 'bold',
          transition: 'all 0.3s',
          cursor: 'pointer'
        }}
        onMouseEnter={(e) => e.target.style.backgroundColor = '#2CA7A9'}
        onMouseLeave={(e) => e.target.style.backgroundColor = '#3ABDBF'}>
          REGISTRAR CLIENTE
        </Link>

        <Link to="/proveedores" style={{
          backgroundColor: '#3ABDBF',
          color: 'white',
          padding: '20px',
          borderRadius: '8px',
          textDecoration: 'none',
          fontSize: '1.2rem',
          fontWeight: 'bold'
        }}
        onMouseEnter={(e) => e.target.style.backgroundColor = '#2CA7A9'}
        onMouseLeave={(e) => e.target.style.backgroundColor = '#3ABDBF'}>
          REGISTRAR PROVEEDOR
        </Link>

        <Link to="/productos" style={{
          backgroundColor: '#3ABDBF',
          color: 'white',
          padding: '20px',
          borderRadius: '8px',
          textDecoration: 'none',
          fontSize: '1.2rem',
          fontWeight: 'bold'
        }}
        onMouseEnter={(e) => e.target.style.backgroundColor = '#2CA7A9'}
        onMouseLeave={(e) => e.target.style.backgroundColor = '#3ABDBF'}>
          REGISTRAR PRODUCTO
        </Link>

        <Link to="/ventas" style={{
          backgroundColor: '#3ABDBF',
          color: 'white',
          padding: '20px',
          borderRadius: '8px',
          textDecoration: 'none',
          fontSize: '1.2rem',
          fontWeight: 'bold'
        }}
        onMouseEnter={(e) => e.target.style.backgroundColor = '#2CA7A9'}
        onMouseLeave={(e) => e.target.style.backgroundColor = '#3ABDBF'}>
          REGISTRAR VENTAS
        </Link>

        <Link to="/compras" style={{
          backgroundColor: '#3ABDBF',
          color: 'white',
          padding: '20px',
          borderRadius: '8px',
          textDecoration: 'none',
          fontSize: '1.2rem',
          fontWeight: 'bold'
        }}
        onMouseEnter={(e) => e.target.style.backgroundColor = '#2CA7A9'}
        onMouseLeave={(e) => e.target.style.backgroundColor = '#3ABDBF'}>
          REGISTRAR COMPRAS
        </Link>
      </div>

      <button
        onClick={() => {
          localStorage.removeItem('user');
          localStorage.removeItem('token');
          window.location.href = '/login';
        }}
        style={{
          backgroundColor: '#DC3545',
          color: 'white',
          border: 'none',
          padding: '12px 30px',
          borderRadius: '5px',
          fontSize: '1rem',
          fontWeight: 'bold',
          marginTop: '30px',
          cursor: 'pointer'
        }}
      >
        CERRAR SESIÓN
      </button>
    </div>
  );
};

export default Menu;