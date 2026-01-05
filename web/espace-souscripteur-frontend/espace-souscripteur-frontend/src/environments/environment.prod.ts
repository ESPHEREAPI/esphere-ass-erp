/**
 * Configuration pour l'environnement de production
 */
export const environment = {
  production: true,
  apiUrl: 'https://api.espaceclient.com/api/v1',
  apiTimeout: 30000,
  enableDebug: false,
  enableLogging: false,
  defaultLanguage: 'fr',
  supportedLanguages: ['fr', 'en'],
  tokenKey: 'souscripteur_auth_token',
  refreshTokenKey: 'souscripteur_refresh_token',
  userKey: 'souscripteur_user',
  sessionTimeout: 3600000, // 1 heure en ms
  paginationOptions: {
    defaultPageSize: 10,
    pageSizeOptions: [5, 10, 20, 50, 100]
  },
  dateFormat: 'dd/MM/yyyy',
  dateTimeFormat: 'dd/MM/yyyy HH:mm',
  currencySymbol: 'FCFA',
  chartColors: {
    primary: '#007bff',
    success: '#28a745',
    warning: '#ffc107',
    danger: '#dc3545',
    info: '#17a2b8',
    secondary: '#6c757d'
  },
  adminLTE: {
    sidebarMiniBreakpoint: 768,
    defaultSkin: 'skin-blue',
    boxedLayout: false,
    fixedLayout: true,
    sidebarCollapse: false
  }
};
