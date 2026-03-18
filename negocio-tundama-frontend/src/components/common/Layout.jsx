import React from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import './Header.css';

const Header = () => {
  const navigate = useNavigate();
  const { user, logout } = useAuth();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header className="header">
      <div className="header-left">
        <img src="/img/logo.png" alt="Logo" className="header-logo" />
        <h1>NEGOCIO TUNDAMA LTDA</h1>
      </div>
      
      <div className="header-right">
        <span className="version">VERSION 1.0</span>
        {user && (
          <span className="user-info">
            <img src="/img/user_icon.png" alt="Usuario" className="user-icon" />
            {user.nombre}
          </span>
        )}
        <button onClick={handleLogout} className="button button-logout">
          SALIR
        </button>
        <Link to="/menu" className="button button-regresar">
          MENÚ
        </Link>
      </div>
    </header>
  );
};

export default Header;
