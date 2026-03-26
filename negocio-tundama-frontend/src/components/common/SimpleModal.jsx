import React from 'react';
import ReactDOM from 'react-dom';

const SimpleModal = ({ isOpen, onClose, message }) => {
  if (!isOpen) return null;

  return ReactDOM.createPortal(
    <div style={{
      position: 'fixed',
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
      backgroundColor: 'rgba(0, 0, 0, 0.7)',
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      zIndex: 9999
    }} onClick={onClose}>
      <div style={{
        backgroundColor: '#708090',
        padding: '30px',
        borderRadius: '10px',
        textAlign: 'center',
        maxWidth: '400px',
        width: '90%',
        color: 'white'
      }} onClick={(e) => e.stopPropagation()}>
        <div style={{ marginBottom: '20px' }}>
          <svg width="60" height="60" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 8V12M12 16H12.01M3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12Z"
              stroke="#FF6B6B" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          </svg>
        </div>
        <h3 style={{ marginBottom: '10px', fontSize: '20px' }}>¡Error de autenticación!</h3>
        <p style={{ marginBottom: '20px', color: '#E0E0E0' }}>{message}</p>
        <button
          onClick={onClose}
          style={{
            backgroundColor: '#3ABDBF',
            color: 'white',
            border: 'none',
            padding: '10px 30px',
            borderRadius: '5px',
            fontSize: '16px',
            fontWeight: 'bold',
            cursor: 'pointer'
          }}
        >
          ACEPTAR
        </button>
      </div>
    </div>,
    document.body
  );
};

export default SimpleModal;