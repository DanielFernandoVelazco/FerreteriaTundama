import React, { useEffect, useRef } from 'react';
import './ErrorModal.css';

const ErrorModal = ({ show, message, onClose }) => {
  const modalRef = useRef(null);

  useEffect(() => {
    if (show) {
      // Prevenir scroll del body cuando el modal está abierto
      document.body.style.overflow = 'hidden';

      // Cerrar modal con tecla ESC
      const handleEsc = (event) => {
        if (event.key === 'Escape') {
          onClose();
        }
      };

      window.addEventListener('keydown', handleEsc);

      return () => {
        document.body.style.overflow = 'unset';
        window.removeEventListener('keydown', handleEsc);
      };
    }
  }, [show, onClose]);

  if (!show) return null;

  return (
    <div className="error-modal-overlay" onClick={onClose}>
      <div
        className="error-modal-content"
        onClick={(e) => e.stopPropagation()}
        ref={modalRef}
      >
        <div className="error-modal-header">
          <div className="error-modal-icon-wrapper">
            <svg className="error-modal-icon" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 8V12M12 16H12.01M3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12Z"
                stroke="#FF6B6B" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
            </svg>
          </div>
        </div>

        <div className="error-modal-body">
          <p className="error-message-main">¡Error de autenticación!</p>
          <p className="error-message-details">{message || 'Usuario o contraseña incorrectos'}</p>
        </div>

        <div className="error-modal-footer">
          <button className="error-modal-button" onClick={onClose} autoFocus>
            ACEPTAR
          </button>
        </div>
      </div>
    </div>
  );
};

export default ErrorModal;