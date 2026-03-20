import axios from 'axios';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8081/api';

console.log('API URL configurada:', API_URL);

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000, // 10 segundos de timeout
});

// Interceptor para agregar token
api.interceptors.request.use(
  (config) => {
    console.log(`Enviando petición ${config.method.toUpperCase()} a ${config.url}`);
    console.log('Datos:', config.data);

    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    console.error('Error en la petición:', error);
    return Promise.reject(error);
  }
);

// Interceptor para manejar respuestas
api.interceptors.response.use(
  (response) => {
    console.log('Respuesta recibida:', response.data);
    return response;
  },
  (error) => {
    console.error('Error en la respuesta:', error);

    if (error.code === 'ECONNABORTED') {
      console.error('Timeout - El servidor no respondió');
    } else if (error.response) {
      // El servidor respondió con un código de error
      console.error('Status:', error.response.status);
      console.error('Data:', error.response.data);

      if (error.response.status === 401) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = '/login';
      }
    } else if (error.request) {
      // La petición se hizo pero no se recibió respuesta
      console.error('No se recibió respuesta del servidor');
    } else {
      // Error al configurar la petición
      console.error('Error:', error.message);
    }

    return Promise.reject(error);
  }
);

export default api;