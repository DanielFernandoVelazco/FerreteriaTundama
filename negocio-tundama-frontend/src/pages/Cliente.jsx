import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import './Cliente.css';

const Cliente = () => {
  const navigate = useNavigate();
  const [clientes, setClientes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [formData, setFormData] = useState({
    id: null,
    nombre: '',
    tipoIdentificacion: 'NIT',
    identificacion: '',
    direccion: '',
    telefono: '',
    notas: ''
  });
  const [isEditing, setIsEditing] = useState(false);

  // Cargar clientes al montar el componente
  useEffect(() => {
    cargarClientes();
  }, []);

  const cargarClientes = async () => {
    try {
      setLoading(true);
      const response = await api.get('/clientes');
      if (response.data.success) {
        setClientes(response.data.data);
      }
    } catch (err) {
      setError('Error al cargar clientes');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      setError('');

      let response;
      if (isEditing) {
        // Actualizar cliente
        response = await api.put(`/clientes/${formData.id}`, formData);
      } else {
        // Registrar nuevo cliente
        response = await api.post('/clientes/registrar', formData);
      }

      if (response.data.success) {
        alert(isEditing ? 'Cliente actualizado exitosamente' : 'Cliente registrado exitosamente');
        resetForm();
        cargarClientes();
      } else {
        setError(response.data.message);
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Error al guardar cliente');
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (cliente) => {
    setFormData({
      id: cliente.id,
      nombre: cliente.nombre || '',
      tipoIdentificacion: cliente.tipoIdentificacion || 'NIT',
      identificacion: cliente.identificacion || '',
      direccion: cliente.direccion || '',
      telefono: cliente.telefono || '',
      notas: cliente.notas || ''
    });
    setIsEditing(true);
  };

  const handleDelete = async (id) => {
    if (!window.confirm('¿Está seguro de eliminar este cliente?')) {
      return;
    }

    try {
      setLoading(true);
      const response = await api.delete(`/clientes/${id}`);

      if (response.data.success) {
        alert('Cliente eliminado exitosamente');
        cargarClientes();
      } else {
        setError(response.data.message);
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Error al eliminar cliente');
    } finally {
      setLoading(false);
    }
  };

  const resetForm = () => {
    setFormData({
      id: null,
      nombre: '',
      tipoIdentificacion: 'NIT',
      identificacion: '',
      direccion: '',
      telefono: '',
      notas: ''
    });
    setIsEditing(false);
  };

  return (
    <div className="cliente-container">
      <div className="cliente-box">
        <h1>MÓDULO CLIENTE</h1>

        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleSubmit} className="cliente-form">
          <div className="form-group">
            <label>NOMBRE</label>
            <input
              type="text"
              name="nombre"
              value={formData.nombre}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label>TIPO IDENTIFICACIÓN</label>
              <select
                name="tipoIdentificacion"
                value={formData.tipoIdentificacion}
                onChange={handleChange}
              >
                <option value="NIT">NIT</option>
                <option value="CEDULA">CÉDULA</option>
              </select>
            </div>

            <div className="form-group">
              <label># IDENTIFICACIÓN</label>
              <input
                type="text"
                name="identificacion"
                value={formData.identificacion}
                onChange={handleChange}
              />
            </div>
          </div>

          <div className="form-group">
            <label>DIRECCIÓN</label>
            <input
              type="text"
              name="direccion"
              value={formData.direccion}
              onChange={handleChange}
            />
          </div>

          <div className="form-group">
            <label>TELÉFONO</label>
            <input
              type="text"
              name="telefono"
              value={formData.telefono}
              onChange={handleChange}
            />
          </div>

          <div className="form-group">
            <label>NOTAS</label>
            <textarea
              name="notas"
              value={formData.notas}
              onChange={handleChange}
              rows="3"
            />
          </div>

          <div className="form-actions">
            <button type="submit" className="button button-primary" disabled={loading}>
              {loading ? 'GUARDANDO...' : (isEditing ? 'MODIFICAR' : 'REGISTRAR')}
            </button>
            {isEditing && (
              <button type="button" className="button button-secondary" onClick={resetForm}>
                NUEVO
              </button>
            )}
            <button type="button" className="button button-danger" onClick={() => navigate('/menu')}>
              CANCELAR
            </button>
          </div>
        </form>

        <div className="clientes-table">
          <h2>LISTA DE CLIENTES</h2>
          <div className="table-responsive">
            <table>
              <thead>
                <tr>
                  <th>ID_CLIENTE</th>
                  <th>NOMBRE</th>
                  <th>IDENTIFICACIÓN</th>
                  <th>DIRECCIÓN</th>
                  <th>TELÉFONO</th>
                  <th>NOTAS</th>
                  <th>ACCIONES</th>
                </tr>
              </thead>
              <tbody>
                {clientes.map(cliente => (
                  <tr key={cliente.id}>
                    <td>{cliente.idCliente}</td>
                    <td>{cliente.nombre}</td>
                    <td>{cliente.identificacion}</td>
                    <td>{cliente.direccion}</td>
                    <td>{cliente.telefono}</td>
                    <td>{cliente.notas}</td>
                    <td>
                      <button className="button-small button-edit" onClick={() => handleEdit(cliente)}>
                        EDITAR
                      </button>
                      <button className="button-small button-delete" onClick={() => handleDelete(cliente.id)}>
                        ELIMINAR
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Cliente;