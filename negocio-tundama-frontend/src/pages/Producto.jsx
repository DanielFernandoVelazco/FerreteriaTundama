import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import './Producto.css';

const Producto = () => {
  const navigate = useNavigate();
  const [productos, setProductos] = useState([]);
  const [proveedores, setProveedores] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [formData, setFormData] = useState({
    id: null,
    nombre: '',
    descripcion: '',
    unidadMedida: 'Und',
    precioUnitario: '',
    iva: 19,
    stockActual: 0,
    stockMinimo: 0,
    proveedorId: ''
  });
  const [isEditing, setIsEditing] = useState(false);

  useEffect(() => {
    cargarProductos();
    cargarProveedores();
  }, []);

  const cargarProductos = async () => {
    try {
      setLoading(true);
      const response = await api.get('/productos');
      if (response.data.success) {
        setProductos(response.data.data);
      }
    } catch (err) {
      setError('Error al cargar productos');
    } finally {
      setLoading(false);
    }
  };

  const cargarProveedores = async () => {
    try {
      const response = await api.get('/proveedores');
      if (response.data.success) {
        setProveedores(response.data.data);
      }
    } catch (err) {
      console.error('Error al cargar proveedores:', err);
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
        response = await api.put(`/productos/${formData.id}`, formData);
      } else {
        response = await api.post('/productos/registrar', formData);
      }

      if (response.data.success) {
        alert(isEditing ? 'Producto actualizado exitosamente' : 'Producto registrado exitosamente');
        resetForm();
        cargarProductos();
      } else {
        setError(response.data.message);
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Error al guardar producto');
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (producto) => {
    setFormData({
      id: producto.id,
      nombre: producto.nombre || '',
      descripcion: producto.descripcion || '',
      unidadMedida: producto.unidadMedida || 'Und',
      precioUnitario: producto.precioUnitario || '',
      iva: producto.iva || 19,
      stockActual: producto.stockActual || 0,
      stockMinimo: producto.stockMinimo || 0,
      proveedorId: producto.proveedor?.id || ''
    });
    setIsEditing(true);
  };

  const handleDelete = async (id) => {
    if (!window.confirm('¿Está seguro de eliminar este producto?')) {
      return;
    }

    try {
      setLoading(true);
      const response = await api.delete(`/productos/${id}`);

      if (response.data.success) {
        alert('Producto eliminado exitosamente');
        cargarProductos();
      } else {
        setError(response.data.message);
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Error al eliminar producto');
    } finally {
      setLoading(false);
    }
  };

  const resetForm = () => {
    setFormData({
      id: null,
      nombre: '',
      descripcion: '',
      unidadMedida: 'Und',
      precioUnitario: '',
      iva: 19,
      stockActual: 0,
      stockMinimo: 0,
      proveedorId: ''
    });
    setIsEditing(false);
  };

  return (
    <div className="producto-container">
      <div className="producto-box">
        <h1>MÓDULO PRODUCTO</h1>

        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleSubmit} className="producto-form">
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

          <div className="form-group">
            <label>DESCRIPCIÓN</label>
            <textarea
              name="descripcion"
              value={formData.descripcion}
              onChange={handleChange}
              rows="2"
            />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label>UNIDAD MEDIDA</label>
              <select
                name="unidadMedida"
                value={formData.unidadMedida}
                onChange={handleChange}
              >
                <option value="Und">Unidad</option>
                <option value="Kg">Kilogramo</option>
                <option value="Lt">Litro</option>
                <option value="Mts">Metro</option>
                <option value="Caja">Caja</option>
              </select>
            </div>

            <div className="form-group">
              <label>PRECIO UNITARIO</label>
              <input
                type="number"
                name="precioUnitario"
                value={formData.precioUnitario}
                onChange={handleChange}
                step="1000"
                min="0"
                required
              />
            </div>

            <div className="form-group">
              <label>IVA (%)</label>
              <input
                type="number"
                name="iva"
                value={formData.iva}
                onChange={handleChange}
                step="1"
                min="0"
                max="100"
              />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label>STOCK ACTUAL</label>
              <input
                type="number"
                name="stockActual"
                value={formData.stockActual}
                onChange={handleChange}
                min="0"
                readOnly={isEditing}
              />
            </div>

            <div className="form-group">
              <label>STOCK MÍNIMO</label>
              <input
                type="number"
                name="stockMinimo"
                value={formData.stockMinimo}
                onChange={handleChange}
                min="0"
              />
            </div>

            <div className="form-group">
              <label>PROVEEDOR</label>
              <select
                name="proveedorId"
                value={formData.proveedorId}
                onChange={handleChange}
              >
                <option value="">Seleccione un proveedor</option>
                {proveedores.map(prov => (
                  <option key={prov.id} value={prov.id}>
                    {prov.nombreEmpresa}
                  </option>
                ))}
              </select>
            </div>
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

        <div className="productos-table">
          <h2>LISTA DE PRODUCTOS</h2>
          <div className="table-responsive">
            <table>
              <thead>
                <tr>
                  <th>CÓDIGO</th>
                  <th>NOMBRE</th>
                  <th>UNIDAD</th>
                  <th>PRECIO</th>
                  <th>IVA</th>
                  <th>STOCK</th>
                  <th>PROVEEDOR</th>
                  <th>ACCIONES</th>
                </tr>
              </thead>
              <tbody>
                {productos.map(producto => (
                  <tr key={producto.id}>
                    <td>{producto.idProducto}</td>
                    <td>{producto.nombre}</td>
                    <td>{producto.unidadMedida}</td>
                    <td>${producto.precioUnitario?.toLocaleString()}</td>
                    <td>{producto.iva}%</td>
                    <td className={producto.stockActual <= producto.stockMinimo ? 'stock-bajo' : ''}>
                      {producto.stockActual}
                    </td>
                    <td>{producto.proveedor?.nombreEmpresa || 'N/A'}</td>
                    <td>
                      <button className="button-small button-edit" onClick={() => handleEdit(producto)}>
                        EDITAR
                      </button>
                      <button className="button-small button-delete" onClick={() => handleDelete(producto.id)}>
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

export default Producto;