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

  useEffect(() => {
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
      const response = await api.post('/usuarios/login', { email, contrasena });

      if (response.data && response.data.success === true) {
        const userData = response.data.data;
        setUser(userData);
        localStorage.setItem('user', JSON.stringify(userData));
        localStorage.setItem('token', 'logged-in');
        return { success: true, data: userData };
      } else {
        const message = response.data?.message || 'Error al iniciar sesión';
        return { success: false, message };
      }
    } catch (err) {
      let message = 'Error de conexión con el servidor';

      if (err.response) {
        message = err.response.data?.message || `Error ${err.response.status}`;
      } else if (err.request) {
        message = 'No se pudo conectar al servidor. Verifica que el backend esté corriendo.';
      }

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
      const response = await api.post('/usuarios/registrar', userData);

      if (response.data && response.data.success) {
        return { success: true, data: response.data.data };
      } else {
        const message = response.data?.message || 'Error al registrar usuario';
        return { success: false, message };
      }
    } catch (err) {
      let message = 'Error al registrar usuario';
      if (err.response?.data?.message) {
        message = err.response.data.message;
      }
      return { success: false, message };
    }
  };

  const value = {
    user,
    loading,
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