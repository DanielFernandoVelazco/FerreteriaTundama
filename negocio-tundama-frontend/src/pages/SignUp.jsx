import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './SignUp.css';

const SignUp = () => {
  const navigate = useNavigate();
  const { register, error } = useAuth();
  const [formData, setFormData] = useState({
    nombre: '',
    email: '',
    contrasena: '',
    confirmarContrasena: '',
    direccion: '',
    notas: ''
  });
  const [loading, setLoading] = useState(false);
  const [passwordError, setPasswordError] = useState('');

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Validar contraseñas
    if (formData.contrasena !== formData.confirmarContrasena) {
      setPasswordError('Las contraseñas no coinciden');
      return;
    }

    setPasswordError('');
    setLoading(true);

    // Crear objeto para enviar (sin confirmarContrasena)
    const userData = {
      nombre: formData.nombre,
      email: formData.email,
      contrasena: formData.contrasena,
      direccion: formData.direccion,
      notas: formData.notas
    };

    const result = await register(userData);

    if (result.success) {
      alert('Usuario registrado exitosamente. Por favor inicie sesión.');
      navigate('/login');
    }

    setLoading(false);
  };

  return (
    <div className="signup-container">
      <div className="signup-box">
        <div className="signup-header">
          <img src="/img/logo.png" alt="Logo" className="signup-logo" />
          <h1>MODULO SIGNUP</h1>
        </div>

        {error && (
          <div className="error-message">
            {error}
          </div>
        )}

        {passwordError && (
          <div className="error-message">
            {passwordError}
          </div>
        )}

        <form onSubmit={handleSubmit} className="signup-form">
          <div className="form-group">
            <label htmlFor="nombre">NOMBRE</label>
            <input
              type="text"
              id="nombre"
              name="nombre"
              value={formData.nombre}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="email">E-MAIL</label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="contrasena">CONTRASEÑA</label>
              <input
                type="password"
                id="contrasena"
                name="contrasena"
                value={formData.contrasena}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="confirmarContrasena">CONFIRMAR CONTRASEÑA</label>
              <input
                type="password"
                id="confirmarContrasena"
                name="confirmarContrasena"
                value={formData.confirmarContrasena}
                onChange={handleChange}
                required
              />
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="direccion">DIRECCIÓN</label>
            <input
              type="text"
              id="direccion"
              name="direccion"
              value={formData.direccion}
              onChange={handleChange}
            />
          </div>

          <div className="form-group">
            <label htmlFor="notas">NOTAS</label>
            <textarea
              id="notas"
              name="notas"
              value={formData.notas}
              onChange={handleChange}
              rows="3"
            />
          </div>

          <div className="signup-actions">
            <button
              type="submit"
              className="button button-primary"
              disabled={loading}
            >
              {loading ? 'REGISTRANDO...' : 'REGISTRAR'}
            </button>

            <Link to="/login" className="button button-secondary">
              CANCELAR
            </Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default SignUp;