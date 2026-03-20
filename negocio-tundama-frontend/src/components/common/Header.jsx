import React from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

const Header = () => {
  const navigate = useNavigate();
  const { user, logout } = useAuth();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
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

      <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
        <span style={{ color: '#e0e0e0' }}>VERSION 1.0</span>
        {user && (
          <span style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <img src="/img/user_icon.png" alt="Usuario" style={{ width: '30px', height: '30px' }} />
            {user.nombre}
          </span>
        )}
        <button
          onClick={handleLogout}
          style={{
            backgroundColor: '#dc3545',
            color: 'white',
            border: 'none',
            padding: '8px 16px',
            borderRadius: '4px',
            cursor: 'pointer'
          }}
        >
          SALIR
        </button>
        <Link
          to="/menu"
          style={{
            backgroundColor: '#007bff',
            color: 'white',
            textDecoration: 'none',
            padding: '8px 16px',
            borderRadius: '4px'
          }}
        >
          MENÚ
        </Link>
      </div>
    </header>
  );
};

export default Header;