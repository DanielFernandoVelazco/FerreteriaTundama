import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Login.css';

const Login = () => {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [formData, setFormData] = useState({
    email: '',
    contrasena: ''
  });
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!formData.email.trim()) {
      alert('⚠️ Por favor ingrese su usuario');
      return;
    }

    if (!formData.contrasena.trim()) {
      alert('⚠️ Por favor ingrese su contraseña');
      return;
    }

    setLoading(true);

    try {
      const result = await login(formData.email, formData.contrasena);

      if (result.success) {
        // Login exitoso
        navigate('/menu');
      } else {
        // Mostrar error con alert nativo
        alert(`❌ Error de autenticación\n\n${result.message || 'Usuario o clave incorrectos.'}`);
        setLoading(false);
      }
    } catch (err) {
      console.error('Error en login:', err);
      alert('❌ Error de conexión con el servidor.\n\nPor favor, verifique que el backend esté corriendo en http://localhost:8081');
      setLoading(false);
    }
  };

  const handleCancel = () => {
    if (window.confirm('¿Está seguro que desea cancelar el inicio de sesión?')) {
      setFormData({ email: '', contrasena: '' });
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
                    autoComplete="off"
                    disabled={loading}
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
                    disabled={loading}
                  />
                </div>
              </form>
            </div>
          </div>

          <div className="login-actions">
            <button
              type="button"
              className="button button-primary"
              onClick={handleSubmit}
              disabled={loading}
            >
              {loading ? 'INGRESANDO...' : 'INGRESAR'}
            </button>

            <button
              type="button"
              className="button button-danger"
              onClick={handleCancel}
              disabled={loading}
            >
              CANCELAR
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