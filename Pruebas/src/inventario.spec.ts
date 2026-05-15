import { Test, TestingModule } from '@nestjs/testing';

// Simulación de un servicio de inventario
class InventarioService {
  private stock = 10;
  private clientes = [];

  registrarCliente(cliente: any) {
    if (!cliente.nombre) throw new Error('Datos incompletos');
    this.clientes.push(cliente);
    return { status: 'success', data: cliente };
  }

  accederModuloAdmin(rol: string) {
    if (rol !== 'Administrador') {
      // Caso 002: El sistema bloquea pero no emite alerta clara
      return null; 
    }
    return true;
  }

  registrarVenta(cantidad: number) {
    // Caso 003: Error de lógica - permite stock negativo
    this.stock -= cantidad;
    return { nuevoStock: this.stock };
  }
}

describe('Pruebas de Verificación - Sistema de Inventarios', () => {
  let service: InventarioService;

  beforeEach(() => {
    service = new InventarioService();
  });

  // CASO DE PRUEBA 001: REGISTRO DE CLIENTES
  it('CP-001: Debe registrar un cliente exitosamente (Resultado: Aprobado)', () => {
    const cliente = { nombre: 'Juan Perez', id: '123' };
    const resultado = service.registrarCliente(cliente);
    expect(resultado.status).toBe('success');
    console.log('✅ CP-001 EXITOSO: Cliente registrado en BD.');
  });

  // CASO DE PRUEBA 002: RESTRICCIÓN DE ROLES
  it('CP-002: Debe denegar acceso a Empleado (Resultado: En Seguimiento)', () => {
    const acceso = service.accederModuloAdmin('Empleado');
    // Simulamos el error: El acceso es null (bloqueado) pero no hay mensaje de alerta
    expect(acceso).toBeNull();
    console.warn('⚠️ CP-002 OBSERVACIÓN: Acceso denegado, pero falta mensaje de alerta claro.');
  });

  // CASO DE PRUEBA 003: VALIDACIÓN DE STOCK
  it('CP-003: Debe fallar al vender más del stock disponible (Resultado: Rechazado)', () => {
    const venta = service.registrarVenta(15); 
    // Aquí se detecta el fallo: El stock quedó en -5
    expect(venta.nuevoStock).toBeLessThan(0);
    console.error('❌ CP-003 FALLIDO: El sistema permitió stock negativo (-5).');
  });
});
