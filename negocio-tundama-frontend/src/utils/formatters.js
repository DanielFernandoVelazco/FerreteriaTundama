// Formateadores
export const formatCurrency = (value) => {
  return new Intl.NumberFormat('es-CO', {
    style: 'currency',
    currency: 'COP',
    minimumFractionDigits: 0,
    maximumFractionDigits: 0,
  }).format(value);
};

export const formatDate = (date) => {
  if (!date) return '';
  const d = new Date(date);
  return d.toLocaleDateString('es-CO', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
  });
};

export const formatDateTime = (date) => {
  if (!date) return '';
  const d = new Date(date);
  return d.toLocaleString('es-CO', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
  });
};

export const formatIdCode = (prefix, year, month, number) => {
  const monthMap = {
    '01': 'ENE', '02': 'FEB', '03': 'MAR', '04': 'ABR',
    '05': 'MAY', '06': 'JUN', '07': 'JUL', '08': 'AGO',
    '09': 'SEP', '10': 'OCT', '11': 'NOV', '12': 'DIC'
  };
  
  const monthCode = monthMap[month] || month;
  return `${prefix}-${year}-${monthCode}-${number.toString().padStart(4, '0')}`;
};
