import React, { createContext, useState, useContext, useEffect } from 'react';
import api from '../services/api';

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth debe ser usado dentro de un AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Verificar si hay usuario en localStorage al cargar la app
    const storedUser = localStorage.getItem('user');
    const token = localStorage.getItem('token');

    if (storedUser && token) {
      try {
        setUser(JSON.parse(storedUser));
      } catch (e) {
        console.error('Error parsing stored user:', e);
        localStorage.removeItem('user');
        localStorage.removeItem('token');
      }
    }
    setLoading(false);
  }, []);

  const login = async (email, contrasena) => {
    try {
      setError(null);
      console.log('Intentando login con:', { email, contrasena });

      const response = await api.post('/usuarios/login', { email, contrasena });

      console.log('Respuesta completa del servidor:', response);
      console.log('Datos de la respuesta:', response.data);

      // La API devuelve { success, message, data }
      if (response.data && response.data.success === true) {
        const userData = response.data.data;
        console.log('Login exitoso, usuario:', userData);

        setUser(userData);
        localStorage.setItem('user', JSON.stringify(userData));
        localStorage.setItem('token', 'logged-in'); // Token temporal

        return { success: true, data: userData };
      } else {
        const message = response.data?.message || 'Error al iniciar sesión';
        console.log('Login falló:', message);
        setError(message);
        return { success: false, message };
      }
    } catch (err) {
      console.error('Error en login:', err);

      let message = 'Error de conexión con el servidor';

      if (err.response) {
        // El servidor respondió con un error
        console.error('Status:', err.response.status);
        console.error('Data:', err.response.data);
        message = err.response.data?.message || `Error ${err.response.status}`;
      } else if (err.request) {
        // No se recibió respuesta
        console.error('No hubo respuesta del servidor');
        message = 'No se pudo conectar al servidor. Verifica que el backend esté corriendo en http://localhost:8081';
      } else if (err.message) {
        message = err.message;
      }

      setError(message);
      return { success: false, message };
    }
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem('user');
    localStorage.removeItem('token');
  };

  const register = async (userData) => {
    try {
      setError(null);
      console.log('Registrando usuario:', userData);

      const response = await api.post('/usuarios/registrar', userData);

      console.log('Respuesta de registro:', response.data);

      if (response.data && response.data.success) {
        return { success: true, data: response.data.data };
      } else {
        const message = response.data?.message || 'Error al registrar usuario';
        setError(message);
        return { success: false, message };
      }
    } catch (err) {
      console.error('Error en registro:', err);

      let message = 'Error al registrar usuario';
      if (err.response?.data?.message) {
        message = err.response.data.message;
      } else if (err.message) {
        message = err.message;
      }

      setError(message);
      return { success: false, message };
    }
  };

  const value = {
    user,
    loading,
    error,
    login,
    logout,
    register,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};