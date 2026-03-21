import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Login.css';

const Login = () => {
  const navigate = useNavigate();
  const { login, error } = useAuth();
  const [formData, setFormData] = useState({
    email: '',
    contrasena: ''
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

    try {
      const result = await login(formData.email, formData.contrasena);

      if (result.success) {
        navigate('/menu');
      } else {
        setLocalError(result.message || 'Error al iniciar sesión');
      }
    } catch (err) {
      setLocalError('Error de conexión con el servidor');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="login-box">
        <div className="login-header">
          <img src="/img/logo.png" alt="Logo Tundama" className="login-logo" />
          <h1>BIENVENIDO AL NEGOCIO TUNDAMA LTDA</h1>
        </div>

        <div className="login-content">
          {(error || localError) && (
            <div className="login-error">
              <img src="/img/warning_icon.png" alt="Error" className="error-icon" />
              <span>{error || localError}</span>
            </div>
          )}

          <div className="login-form-container">
            <div className="login-user-section">
              <img src="/img/user_icon.png" alt="Usuario" className="login-user-icon" />

              <form onSubmit={handleSubmit} className="login-form">
                <div className="form-group">
                  <label htmlFor="usuario">USUARIO</label>
                  <input
                    type="email"
                    id="usuario"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    placeholder="Ingrese su usuario"
                    required
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="clave">CLAVE</label>
                  <input
                    type="password"
                    id="clave"
                    name="contrasena"
                    value={formData.contrasena}
                    onChange={handleChange}
                    placeholder="******"
                    required
                  />
                </div>
              </form>
            </div>
          </div>

          <div className="login-actions">
            <button
              type="submit"
              className="button button-primary"
              onClick={handleSubmit}
              disabled={loading}
            >
              {loading ? 'INGRESANDO...' : 'INGRESAR'}
            </button>

            <Link to="/signup" className="button button-secondary">
              SIGN UP
            </Link>
          </div>
        </div>

        <div className="login-footer">
          <p>NEGOCIO TUNDAMA LTDA</p>
          <p>VERSION 1.0</p>
        </div>
      </div>
    </div>
  );
};

export default Login;