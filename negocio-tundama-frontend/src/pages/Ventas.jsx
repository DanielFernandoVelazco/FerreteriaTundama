import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Ventas.css';

const Ventas = () => {
  const navigate = useNavigate();

  return (
    <div className="ventas-container">
      <div className="ventas-box">
        <h1>MÓDULO VENTAS</h1>
        <p className="coming-soon">Módulo en construcción</p>
        <button className="button button-primary" onClick={() => navigate('/menu')}>
          VOLVER AL MENÚ
        </button>
      </div>
    </div>
  );
};

export default Ventas;