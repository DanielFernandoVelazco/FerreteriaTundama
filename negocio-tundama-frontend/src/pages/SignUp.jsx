import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './SignUp.css';

const SignUp = () => {
  const navigate = useNavigate();
  const { register } = useAuth();
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({
    nombre: '',
    email: '',
    contrasena: '',
    confirmarContrasena: '',
    direccion: '',
    notas: ''
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Validar campos requeridos
    if (!formData.nombre.trim()) {
      alert('Por favor ingrese su nombre');
      return;
    }

    if (!formData.email.trim()) {
      alert('Por favor ingrese su correo electrónico');
      return;
    }

    if (!formData.contrasena) {
      alert('Por favor ingrese una contraseña');
      return;
    }

    // Validar que las contraseñas coincidan
    if (formData.contrasena !== formData.confirmarContrasena) {
      alert('Las contraseñas no coinciden');
      return;
    }

    // Validar longitud de contraseña
    if (formData.contrasena.length < 3) {
      alert('La contraseña debe tener al menos 3 caracteres');
      return;
    }

    setLoading(true);

    try {
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
        // Mostrar mensaje de éxito con alert nativo
        alert('✅ Usuario registrado exitosamente.\n\nPor favor inicie sesión con sus credenciales.');

        // Limpiar formulario
        setFormData({
          nombre: '',
          email: '',
          contrasena: '',
          confirmarContrasena: '',
          direccion: '',
          notas: ''
        });

        // Redirigir a login
        navigate('/login');
      } else {
        // Mostrar mensaje de error
        alert(`❌ Error al registrar usuario:\n\n${result.message}`);
        setLoading(false);
      }
    } catch (err) {
      console.error('Error en registro:', err);
      alert('❌ Error de conexión con el servidor.\n\nPor favor, intente de nuevo más tarde.');
      setLoading(false);
    }
  };

  const handleCancel = () => {
    if (window.confirm('¿Está seguro que desea cancelar el registro?\nLos datos no guardados se perderán.')) {
      navigate('/login');
    }
  };

  return (
    <div className="signup-container">
      <div className="signup-box">
        <div className="signup-header">
          <img src="/img/logo.png" alt="Logo" className="signup-logo" />
          <h1>MODULO SIGNUP</h1>
        </div>

        <form onSubmit={handleSubmit} className="signup-form">
          <div className="form-group">
            <label htmlFor="nombre">NOMBRE *</label>
            <input
              type="text"
              id="nombre"
              name="nombre"
              value={formData.nombre}
              onChange={handleChange}
              placeholder="Ingrese su nombre completo"
              required
              disabled={loading}
            />
          </div>

          <div className="form-group">
            <label htmlFor="email">E-MAIL *</label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="ejemplo@correo.com"
              required
              disabled={loading}
            />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="contrasena">CONTRASEÑA *</label>
              <input
                type="password"
                id="contrasena"
                name="contrasena"
                value={formData.contrasena}
                onChange={handleChange}
                placeholder="Mínimo 3 caracteres"
                required
                disabled={loading}
              />
            </div>

            <div className="form-group">
              <label htmlFor="confirmarContrasena">CONFIRMAR CONTRASEÑA *</label>
              <input
                type="password"
                id="confirmarContrasena"
                name="confirmarContrasena"
                value={formData.confirmarContrasena}
                onChange={handleChange}
                placeholder="Repita su contraseña"
                required
                disabled={loading}
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
              placeholder="Ingrese su dirección"
              disabled={loading}
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
              placeholder="Información adicional (opcional)"
              disabled={loading}
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

            <button
              type="button"
              className="button button-secondary"
              onClick={handleCancel}
              disabled={loading}
            >
              CANCELAR
            </button>

            <Link to="/login" className="button button-danger">
              VOLVER AL LOGIN
            </Link>
          </div>
        </form>

        <div className="signup-footer">
          <p>NEGOCIO TUNDAMA LTDA</p>
          <p>TODOS LOS DERECHOS RESERVADOS</p>
        </div>
      </div>
    </div>
  );
};

export default SignUp;