import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Compras.css';

const Compras = () => {
  const navigate = useNavigate();

  return (
    <div className="compras-container">
      <div className="compras-box">
        <h1>MÓDULO COMPRAS</h1>
        <p className="coming-soon">Módulo en construcción</p>
        <button className="button button-primary" onClick={() => navigate('/menu')}>
          VOLVER AL MENÚ
        </button>
      </div>
    </div>
  );
};

export default Compras;