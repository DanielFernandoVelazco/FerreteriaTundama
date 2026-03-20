import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Login.css';

const Login = () => {
  const navigate = useNavigate();
  const { login, error } = useAuth();
  const [formData, setFormData] = useState({
    email: 'jerry@mail.com', // Usuario de prueba
    contrasena: 'Jerry.123'   // Contraseña de prueba
  });
  const [loading, setLoading] = useState(false);
  const [localError, setLocalError] = useState('');

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setLocalError('');

    console.log('Enviando formulario de login...');

    try {
      const result = await login(formData.email, formData.contrasena);
      console.log('Resultado del login:', result);

      if (result.success) {
        console.log('Redirigiendo a /menu');
        navigate('/menu');
      } else {
        setLocalError(result.message || 'Error al iniciar sesión');
      }
    } catch (err) {
      console.error('Error inesperado:', err);
      setLocalError('Error inesperado al procesar el login');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="login-box">
        <div className="login-header">
          <img src="/img/logo.png" alt="Logo" className="login-logo" />
          <h1>BIENVENIDO AL NEGOCIO TUNDAMA LTDA</h1>
        </div>

        {(error || localError) && (
          <div className="error-message">
            {error || localError}
          </div>
        )}

        <form onSubmit={handleSubmit} className="login-form">
          <div className="form-group">
            <label htmlFor="email">USUARIO (EMAIL)</label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="ej: admin@email.com"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="contrasena">CLAVE</label>
            <input
              type="password"
              id="contrasena"
              name="contrasena"
              value={formData.contrasena}
              onChange={handleChange}
              placeholder="******"
              required
            />
          </div>

          <div className="login-actions">
            <button
              type="submit"
              className="button button-primary"
              disabled={loading}
            >
              {loading ? 'INGRESANDO...' : 'INGRESAR'}
            </button>

            <Link to="/signup" className="button button-secondary">
              SIGN UP
            </Link>
          </div>
        </form>

        <div className="login-help">
          <p><strong>Usuario de prueba:</strong> jerry@mail.com</p>
          <p><strong>Contraseña:</strong> Jerry.123</p>
          <p style={{fontSize: '0.8rem', marginTop: '10px', color: '#FFC107'}}>
            Verifica que el backend esté corriendo en http://localhost:8081
          </p>
        </div>
      </div>
    </div>
  );
};

export default Login;