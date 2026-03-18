// Constantes del sistema
export const TIPO_IDENTIFICACION = {
  NIT: 'NIT',
  CEDULA: 'CEDULA',
};

export const UNIDADES_MEDIDA = [
  { value: 'Und', label: 'Unidad' },
  { value: 'Kg', label: 'Kilogramo' },
  { value: 'Lt', label: 'Litro' },
  { value: 'Mts', label: 'Metro' },
  { value: 'Caja', label: 'Caja' },
  { value: 'Paquete', label: 'Paquete' },
];

export const ESTADOS = {
  COMPLETADA: 'COMPLETADA',
  ANULADA: 'ANULADA',
};

export const MENSAJES = {
  ERROR_CONEXION: 'Error de conexión con el servidor',
  SESION_EXPIRADA: 'Su sesión ha expirado. Por favor inicie sesión nuevamente',
  REGISTRO_EXITOSO: 'Registro guardado exitosamente',
  ACTUALIZACION_EXITOSA: 'Actualización guardada exitosamente',
  ELIMINACION_EXITOSA: 'Registro eliminado exitosamente',
  CONFIRMAR_ELIMINACION: '¿Está seguro de eliminar este registro?',
};
