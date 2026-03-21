import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import { formatCurrency } from '../utils/formatters';
import './Compras.css';

const Compras = () => {
  const navigate = useNavigate();
  const [proveedores, setProveedores] = useState([]);
  const [productos, setProductos] = useState([]);
  const [compras, setCompras] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [carrito, setCarrito] = useState([]);
  const [selectedProveedor, setSelectedProveedor] = useState('');
  const [selectedProducto, setSelectedProducto] = useState('');
  const [cantidad, setCantidad] = useState(1);
  const [precioUnitario, setPrecioUnitario] = useState(0);
  const [iva, setIva] = useState(19);
  const [subtotal, setSubtotal] = useState(0);
  const [ivaTotal, setIvaTotal] = useState(0);
  const [total, setTotal] = useState(0);
  const [idComprador] = useState('EMP-001'); // Temporal, luego vendrá del usuario logueado

  useEffect(() => {
    cargarProveedores();
    cargarProductos();
    cargarCompras();
  }, []);

  useEffect(() => {
    calcularTotales();
  }, [carrito]);

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

  const cargarProductos = async () => {
    try {
      const response = await api.get('/productos');
      if (response.data.success) {
        setProductos(response.data.data);
      }
    } catch (err) {
      console.error('Error al cargar productos:', err);
    }
  };

  const cargarCompras = async () => {
    try {
      const response = await api.get('/compras');
      if (response.data.success) {
        setCompras(response.data.data);
      }
    } catch (err) {
      console.error('Error al cargar compras:', err);
    }
  };

  const handleProductoChange = (e) => {
    const productoId = e.target.value;
    setSelectedProducto(productoId);

    const producto = productos.find(p => p.id.toString() === productoId);
    if (producto) {
      setPrecioUnitario(producto.precioUnitario);
      setIva(producto.iva || 19);
    }
  };

  const agregarAlCarrito = () => {
    if (!selectedProducto) {
      alert('Seleccione un producto');
      return;
    }

    if (cantidad <= 0) {
      alert('La cantidad debe ser mayor a 0');
      return;
    }

    const producto = productos.find(p => p.id.toString() === selectedProducto);

    const subtotalItem = precioUnitario * cantidad;
    const ivaItem = subtotalItem * (iva / 100);
    const totalItem = subtotalItem + ivaItem;

    const nuevoItem = {
      productoId: producto.id,
      codigo: producto.idProducto,
      nombre: producto.nombre,
      iva: iva,
      unidadMedida: producto.unidadMedida,
      precioUnitario: precioUnitario,
      cantidad: cantidad,
      subtotal: subtotalItem,
      total: totalItem
    };

    setCarrito([...carrito, nuevoItem]);

    setSelectedProducto('');
    setCantidad(1);
    setPrecioUnitario(0);
  };

  const eliminarDelCarrito = (index) => {
    const nuevoCarrito = carrito.filter((_, i) => i !== index);
    setCarrito(nuevoCarrito);
  };

  const calcularTotales = () => {
    const subt = carrito.reduce((acc, item) => acc + item.subtotal, 0);
    const iva = carrito.reduce((acc, item) => acc + (item.total - item.subtotal), 0);
    const tot = carrito.reduce((acc, item) => acc + item.total, 0);

    setSubtotal(subt);
    setIvaTotal(iva);
    setTotal(tot);
  };

  const handleComprar = async () => {
    if (!selectedProveedor) {
      alert('Seleccione un proveedor');
      return;
    }

    if (carrito.length === 0) {
      alert('Agregue productos al carrito');
      return;
    }

    try {
      setLoading(true);
      setError('');

      const compraData = {
        idComprador: idComprador,
        proveedorId: parseInt(selectedProveedor),
        detalles: carrito.map(item => ({
          productoId: item.productoId,
          cantidad: item.cantidad,
          precioUnitario: item.precioUnitario
        }))
      };

      console.log('Enviando compra:', compraData);

      const response = await api.post('/compras/registrar', compraData);

      if (response.data.success) {
        alert('Compra registrada exitosamente');
        setCarrito([]);
        setSelectedProveedor('');
        cargarProductos();
        cargarCompras();
      } else {
        setError(response.data.message);
      }
    } catch (err) {
      console.error('Error al registrar compra:', err);
      setError(err.response?.data?.message || 'Error al registrar compra');
    } finally {
      setLoading(false);
    }
  };

  const handleAnularCompra = async (id) => {
    if (!window.confirm('¿Está seguro de anular esta compra?')) {
      return;
    }

    try {
      const response = await api.put(`/compras/${id}/anular`);

      if (response.data.success) {
        alert('Compra anulada exitosamente');
        cargarCompras();
        cargarProductos();
      } else {
        alert(response.data.message);
      }
    } catch (err) {
      alert('Error al anular compra');
    }
  };

  return (
    <div className="compras-container">
      <div className="compras-box">
        <h1>MÓDULO COMPRAS</h1>

        {error && <div className="error-message">{error}</div>}

        <div className="compras-header">
          <div className="form-group">
            <label>ID_CO</label>
            <input type="text" value="NTCO-" readOnly className="read-only-input" />
            <small>(Código Compras)</small>
          </div>
        </div>

        <div className="compras-form">
          <div className="form-row">
            <div className="form-group">
              <label>ID COMPRADOR</label>
              <input
                type="text"
                value={idComprador}
                readOnly
                className="read-only-input"
              />
            </div>

            <div className="form-group">
              <label>FECHA</label>
              <input
                type="date"
                value={new Date().toISOString().split('T')[0]}
                readOnly
                className="read-only-input"
              />
            </div>

            <div className="form-group">
              <label>ID PROVEEDOR</label>
              <select
                value={selectedProveedor}
                onChange={(e) => setSelectedProveedor(e.target.value)}
                required
              >
                <option value="">Seleccione un proveedor</option>
                {proveedores.map(prov => (
                  <option key={prov.id} value={prov.id}>
                    {prov.nombreEmpresa} - {prov.identificacion}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label>ID PRODUCTO</label>
              <select
                value={selectedProducto}
                onChange={handleProductoChange}
              >
                <option value="">Seleccione un producto</option>
                {productos.map(producto => (
                  <option key={producto.id} value={producto.id}>
                    {producto.nombre} - Stock: {producto.stockActual}
                  </option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label>CANTIDAD</label>
              <input
                type="number"
                min="1"
                value={cantidad}
                onChange={(e) => setCantidad(parseInt(e.target.value) || 1)}
              />
            </div>

            <div className="form-group">
              <label>P.UNITARIO</label>
              <input
                type="number"
                value={precioUnitario}
                onChange={(e) => setPrecioUnitario(parseFloat(e.target.value) || 0)}
                step="1000"
                min="0"
              />
            </div>

            <div className="form-group">
              <label>IVA</label>
              <input
                type="text"
                value={iva + '%'}
                readOnly
                className="read-only-input"
              />
            </div>

            <button
              type="button"
              className="button button-primary add-button"
              onClick={agregarAlCarrito}
            >
              AÑADIR
            </button>
          </div>
        </div>

        <div className="carrito-table">
          <h2>DETALLE DE COMPRA</h2>
          <div className="table-responsive">
            <table>
              <thead>
                <tr>
                  <th>CÓDIGO</th>
                  <th>NOMBRE PRODUCTO</th>
                  <th>IVA</th>
                  <th>UNIDAD</th>
                  <th>PRECIO UNIT.</th>
                  <th>CANTIDAD</th>
                  <th>TOTAL</th>
                  <th>ACCIÓN</th>
                </tr>
              </thead>
              <tbody>
                {carrito.map((item, index) => (
                  <tr key={index}>
                    <td>{item.codigo}</td>
                    <td>{item.nombre}</td>
                    <td>{item.iva}%</td>
                    <td>{item.unidadMedida}</td>
                    <td>{formatCurrency(item.precioUnitario)}</td>
                    <td>{item.cantidad}</td>
                    <td>{formatCurrency(item.total)}</td>
                    <td>
                      <button
                        className="button-small button-delete"
                        onClick={() => eliminarDelCarrito(index)}
                      >
                        ELIMINAR
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        <div className="totales-section">
          <div className="totales-row">
            <span>SUBTOTAL:</span>
            <span className="total-value">{formatCurrency(subtotal)}</span>
          </div>
          <div className="totales-row">
            <span>IVA:</span>
            <span className="total-value">{formatCurrency(ivaTotal)}</span>
          </div>
          <div className="totales-row total-final">
            <span>TOTAL:</span>
            <span className="total-value">{formatCurrency(total)}</span>
          </div>
        </div>

        <div className="compras-actions">
          <button
            className="button button-primary"
            onClick={handleComprar}
            disabled={loading}
          >
            {loading ? 'PROCESANDO...' : 'COMPRAR'}
          </button>
          <button className="button button-secondary" onClick={() => navigate('/menu')}>
            CANCELAR
          </button>
          <button className="button button-info" onClick={() => {
            setCarrito([]);
            setSelectedProveedor('');
          }}>
            NUEVA COMPRA
          </button>
        </div>

        <div className="compras-historial">
          <h2>HISTORIAL DE COMPRAS</h2>
          <div className="table-responsive">
            <table>
              <thead>
                <tr>
                  <th>ID COMPRA</th>
                  <th>FECHA</th>
                  <th>PROVEEDOR</th>
                  <th>SUBTOTAL</th>
                  <th>IVA</th>
                  <th>TOTAL</th>
                  <th>ESTADO</th>
                  <th>ACCIONES</th>
                </tr>
              </thead>
              <tbody>
                {compras.map(compra => (
                  <tr key={compra.id} className={compra.estado === 'ANULADA' ? 'compra-anulada' : ''}>
                    <td>{compra.idCompra}</td>
                    <td>{new Date(compra.fechaCompra).toLocaleDateString()}</td>
                    <td>{compra.proveedor?.nombreEmpresa}</td>
                    <td>{formatCurrency(compra.subtotal)}</td>
                    <td>{formatCurrency(compra.ivaTotal)}</td>
                    <td>{formatCurrency(compra.total)}</td>
                    <td>
                      <span className={`estado-badge ${compra.estado.toLowerCase()}`}>
                        {compra.estado}
                      </span>
                    </td>
                    <td>
                      {compra.estado === 'COMPLETADA' && (
                        <button
                          className="button-small button-warning"
                          onClick={() => handleAnularCompra(compra.id)}
                        >
                          ANULAR
                        </button>
                      )}
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

export default Compras;