import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import './Proveedor.css';

const Proveedor = () => {
  const navigate = useNavigate();
  const [proveedores, setProveedores] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [formData, setFormData] = useState({
    id: null,
    nombreEmpresa: '',
    tipoIdentificacion: 'NIT',
    identificacion: '',
    direccion: '',
    telefono: '',
    notas: ''
  });
  const [isEditing, setIsEditing] = useState(false);

  useEffect(() => {
    cargarProveedores();
  }, []);

  const cargarProveedores = async () => {
    try {
      setLoading(true);
      const response = await api.get('/proveedores');
      if (response.data.success) {
        setProveedores(response.data.data);
      }
    } catch (err) {
      setError('Error al cargar proveedores');
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
        response = await api.put(`/proveedores/${formData.id}`, formData);
      } else {
        response = await api.post('/proveedores/registrar', formData);
      }

      if (response.data.success) {
        alert(isEditing ? 'Proveedor actualizado exitosamente' : 'Proveedor registrado exitosamente');
        resetForm();
        cargarProveedores();
      } else {
        setError(response.data.message);
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Error al guardar proveedor');
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (proveedor) => {
    setFormData({
      id: proveedor.id,
      nombreEmpresa: proveedor.nombreEmpresa || '',
      tipoIdentificacion: proveedor.tipoIdentificacion || 'NIT',
      identificacion: proveedor.identificacion || '',
      direccion: proveedor.direccion || '',
      telefono: proveedor.telefono || '',
      notas: proveedor.notas || ''
    });
    setIsEditing(true);
  };

  const handleDelete = async (id) => {
    if (!window.confirm('¿Está seguro de eliminar este proveedor?')) {
      return;
    }

    try {
      setLoading(true);
      const response = await api.delete(`/proveedores/${id}`);

      if (response.data.success) {
        alert('Proveedor eliminado exitosamente');
        cargarProveedores();
      } else {
        setError(response.data.message);
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Error al eliminar proveedor');
    } finally {
      setLoading(false);
    }
  };

  const resetForm = () => {
    setFormData({
      id: null,
      nombreEmpresa: '',
      tipoIdentificacion: 'NIT',
      identificacion: '',
      direccion: '',
      telefono: '',
      notas: ''
    });
    setIsEditing(false);
  };

  return (
    <div className="proveedor-container">
      <div className="proveedor-box">
        <h1>MÓDULO PROVEEDOR</h1>

        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleSubmit} className="proveedor-form">
          <div className="form-group">
            <label>NOMBRE EMPRESA</label>
            <input
              type="text"
              name="nombreEmpresa"
              value={formData.nombreEmpresa}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label># IDENTIFICACIÓN</label>
              <input
                type="text"
                name="identificacion"
                value={formData.identificacion}
                onChange={handleChange}
              />
            </div>

            <div className="form-group">
              <label>TIPO</label>
              <select
                name="tipoIdentificacion"
                value={formData.tipoIdentificacion}
                onChange={handleChange}
              >
                <option value="NIT">NIT</option>
              </select>
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

        <div className="proveedores-table">
          <h2>LISTA DE PROVEEDORES</h2>
          <div className="table-responsive">
            <table>
              <thead>
                <tr>
                  <th>ID_PROVEEDOR</th>
                  <th>NOMBRE EMPRESA</th>
                  <th>IDENTIFICACIÓN</th>
                  <th>DIRECCIÓN</th>
                  <th>TELÉFONO</th>
                  <th>NOTAS</th>
                  <th>ACCIONES</th>
                </tr>
              </thead>
              <tbody>
                {proveedores.map(proveedor => (
                  <tr key={proveedor.id}>
                    <td>{proveedor.idProveedor}</td>
                    <td>{proveedor.nombreEmpresa}</td>
                    <td>{proveedor.identificacion}</td>
                    <td>{proveedor.direccion}</td>
                    <td>{proveedor.telefono}</td>
                    <td>{proveedor.notas}</td>
                    <td>
                      <button className="button-small button-edit" onClick={() => handleEdit(proveedor)}>
                        EDITAR
                      </button>
                      <button className="button-small button-delete" onClick={() => handleDelete(proveedor.id)}>
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

export default Proveedor;