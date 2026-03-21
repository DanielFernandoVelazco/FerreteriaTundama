import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import { formatCurrency } from '../utils/formatters';
import './Ventas.css';

const Ventas = () => {
  const navigate = useNavigate();
  const [clientes, setClientes] = useState([]);
  const [productos, setProductos] = useState([]);
  const [ventas, setVentas] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [carrito, setCarrito] = useState([]);
  const [selectedCliente, setSelectedCliente] = useState('');
  const [selectedProducto, setSelectedProducto] = useState('');
  const [cantidad, setCantidad] = useState(1);
  const [precioUnitario, setPrecioUnitario] = useState(0);
  const [iva, setIva] = useState(19);
  const [subtotal, setSubtotal] = useState(0);
  const [ivaTotal, setIvaTotal] = useState(0);
  const [total, setTotal] = useState(0);

  useEffect(() => {
    cargarClientes();
    cargarProductos();
    cargarVentas();
  }, []);

  useEffect(() => {
    calcularTotales();
  }, [carrito]);

  const cargarClientes = async () => {
    try {
      const response = await api.get('/clientes');
      if (response.data.success) {
        setClientes(response.data.data);
      }
    } catch (err) {
      console.error('Error al cargar clientes:', err);
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

  const cargarVentas = async () => {
    try {
      const response = await api.get('/ventas');
      if (response.data.success) {
        setVentas(response.data.data);
      }
    } catch (err) {
      console.error('Error al cargar ventas:', err);
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

    // Verificar stock
    if (producto.stockActual < cantidad) {
      alert(`Stock insuficiente. Stock disponible: ${producto.stockActual}`);
      return;
    }

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

    // Resetear selección
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

  const handleVender = async () => {
    if (!selectedCliente) {
      alert('Seleccione un cliente');
      return;
    }

    if (carrito.length === 0) {
      alert('Agregue productos al carrito');
      return;
    }

    try {
      setLoading(true);
      setError('');

      const ventaData = {
        clienteId: parseInt(selectedCliente),
        detalles: carrito.map(item => ({
          productoId: item.productoId,
          cantidad: item.cantidad
        }))
      };

      console.log('Enviando venta:', ventaData);

      const response = await api.post('/ventas/registrar', ventaData);

      if (response.data.success) {
        alert('Venta registrada exitosamente');
        // Limpiar carrito
        setCarrito([]);
        setSelectedCliente('');
        // Recargar productos (para actualizar stock)
        cargarProductos();
        cargarVentas();
      } else {
        setError(response.data.message);
      }
    } catch (err) {
      console.error('Error al registrar venta:', err);
      setError(err.response?.data?.message || 'Error al registrar venta');
    } finally {
      setLoading(false);
    }
  };

  const handleAnularVenta = async (id) => {
    if (!window.confirm('¿Está seguro de anular esta venta?')) {
      return;
    }

    try {
      const response = await api.put(`/ventas/${id}/anular`);

      if (response.data.success) {
        alert('Venta anulada exitosamente');
        cargarVentas();
        cargarProductos(); // Actualizar stock
      } else {
        alert(response.data.message);
      }
    } catch (err) {
      alert('Error al anular venta');
    }
  };

  return (
    <div className="ventas-container">
      <div className="ventas-box">
        <h1>MÓDULO VENTAS</h1>

        {error && <div className="error-message">{error}</div>}

        <div className="ventas-header">
          <div className="form-group">
            <label>ID_V</label>
            <input type="text" value="NTV-" readOnly className="read-only-input" />
            <small>(Código Ventas)</small>
          </div>
        </div>

        <div className="ventas-form">
          <div className="form-row">
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
              <label>ID CLIENTE</label>
              <select
                value={selectedCliente}
                onChange={(e) => setSelectedCliente(e.target.value)}
                required
              >
                <option value="">Seleccione un cliente</option>
                {clientes.map(cliente => (
                  <option key={cliente.id} value={cliente.id}>
                    {cliente.nombre} - {cliente.identificacion}
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
                    {producto.nombre} - {formatCurrency(producto.precioUnitario)} - Stock: {producto.stockActual}
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
                type="text"
                value={formatCurrency(precioUnitario)}
                readOnly
                className="read-only-input"
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
          <h2>DETALLE DE VENTA</h2>
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
            <span>PRECIO:</span>
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

        <div className="ventas-actions">
          <button
            className="button button-primary"
            onClick={handleVender}
            disabled={loading}
          >
            {loading ? 'PROCESANDO...' : 'VENDER'}
          </button>
          <button className="button button-secondary" onClick={() => navigate('/menu')}>
            CANCELAR
          </button>
          <button className="button button-info" onClick={() => {
            setCarrito([]);
            setSelectedCliente('');
          }}>
            NUEVA VENTA
          </button>
        </div>

        <div className="ventas-historial">
          <h2>HISTORIAL DE VENTAS</h2>
          <div className="table-responsive">
            <table>
              <thead>
                <tr>
                  <th>ID VENTA</th>
                  <th>FECHA</th>
                  <th>CLIENTE</th>
                  <th>SUBTOTAL</th>
                  <th>IVA</th>
                  <th>TOTAL</th>
                  <th>ESTADO</th>
                  <th>ACCIONES</th>
                </tr>
              </thead>
              <tbody>
                {ventas.map(venta => (
                  <tr key={venta.id} className={venta.estado === 'ANULADA' ? 'venta-anulada' : ''}>
                    <td>{venta.idVenta}</td>
                    <td>{new Date(venta.fechaVenta).toLocaleDateString()}</td>
                    <td>{venta.cliente?.nombre}</td>
                    <td>{formatCurrency(venta.subtotal)}</td>
                    <td>{formatCurrency(venta.ivaTotal)}</td>
                    <td>{formatCurrency(venta.total)}</td>
                    <td>
                      <span className={`estado-badge ${venta.estado.toLowerCase()}`}>
                        {venta.estado}
                      </span>
                    </td>
                    <td>
                      {venta.estado === 'COMPLETADA' && (
                        <button
                          className="button-small button-warning"
                          onClick={() => handleAnularVenta(venta.id)}
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

export default Ventas;